package ic2.core.block.wiring;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.tile.IEnergyStorage;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot$InvSide;
import ic2.core.block.invslot.InvSlotCharge;
import ic2.core.block.invslot.InvSlotDischarge;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;

public abstract class TileEntityElectricBlock extends TileEntityInventory implements IEnergySink, IEnergySource, IHasGui, INetworkClientTileEntityEventListener, IEnergyStorage
{
    public final int tier;
    public int output;
    public int maxStorage;
    public int energy = 0;
    public byte redstoneMode = 0;
    public static byte redstoneModes = 6;
    private boolean isEmittingRedstone = false;
    private int redstoneUpdateInhibit = 5;
    public boolean addedToEnergyNet = false;
    public final InvSlotCharge chargeSlot;
    public final InvSlotDischarge dischargeSlot;

    public TileEntityElectricBlock(int tier, int output, int maxStorage)
    {
        this.tier = tier;
        this.output = output;
        this.maxStorage = maxStorage;
        this.chargeSlot = new InvSlotCharge(this, 0, tier);
        this.dischargeSlot = new InvSlotDischarge(this, 1, tier, InvSlot$InvSide.BOTTOM);
    }

    public float getChargeLevel()
    {
        float ret = (float)this.energy / (float)this.maxStorage;

        if (ret > 1.0F)
        {
            ret = 1.0F;
        }

        return ret;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        this.setActiveWithoutNotify(nbttagcompound.getBoolean("active"));
        this.energy = nbttagcompound.getInteger("energy");

        if (this.maxStorage > Integer.MAX_VALUE)
        {
            this.energy *= 10;
        }

        this.redstoneMode = nbttagcompound.getByte("redstoneMode");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        int write = this.energy;

        if (this.maxStorage > Integer.MAX_VALUE)
        {
            write /= 10;
        }

        nbttagcompound.setInteger("energy", write);
        nbttagcompound.setBoolean("active", this.getActive());
        nbttagcompound.setByte("redstoneMode", this.redstoneMode);
    }

    public void onLoaded()
    {
        super.onLoaded();

        if (IC2.platform.isSimulating())
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }
    }

    public void onUnloaded()
    {
        if (IC2.platform.isSimulating() && this.addedToEnergyNet)
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }

        super.onUnloaded();
    }

    public boolean enableUpdateEntity()
    {
        return IC2.platform.isSimulating();
    }

    public void updateEntity()
    {
        super.updateEntity();
        boolean needsInvUpdate = false;
        int shouldEmitRedstone;

        if (this.energy > 0)
        {
            shouldEmitRedstone = this.chargeSlot.charge(this.energy);
            this.energy -= shouldEmitRedstone;
            needsInvUpdate = shouldEmitRedstone > 0;
        }

        if (this.demandsEnergy() > 0)
        {
            shouldEmitRedstone = this.dischargeSlot.discharge(this.maxStorage - this.energy, false);
            this.energy += shouldEmitRedstone;
            needsInvUpdate = shouldEmitRedstone > 0;
        }

        if (this.energy >= this.output && (this.redstoneMode != 4 || !super.worldObj.isBlockIndirectlyGettingPowered(super.xCoord, super.yCoord, super.zCoord)) && (this.redstoneMode != 5 || !super.worldObj.isBlockIndirectlyGettingPowered(super.xCoord, super.yCoord, super.zCoord) || this.energy >= this.maxStorage))
        {
            EnergyTileSourceEvent shouldEmitRedstone2 = new EnergyTileSourceEvent(this, this.output);
            MinecraftForge.EVENT_BUS.post(shouldEmitRedstone2);
            this.energy -= this.output - shouldEmitRedstone2.amount;
        }

        boolean shouldEmitRedstone21 = this.shouldEmitRedstone();

        if (shouldEmitRedstone21 != this.isEmittingRedstone)
        {
            this.isEmittingRedstone = shouldEmitRedstone21;
            this.setActive(this.isEmittingRedstone);
            super.worldObj.notifyBlocksOfNeighborChange(super.xCoord, super.yCoord, super.zCoord, super.worldObj.getBlockId(super.xCoord, super.yCoord, super.zCoord));
        }

        if (needsInvUpdate)
        {
            this.onInventoryChanged();
        }
    }

    public boolean isAddedToEnergyNet()
    {
        return this.addedToEnergyNet;
    }

    public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
    {
        return !this.facingMatchesDirection(direction);
    }

    public boolean emitsEnergyTo(TileEntity receiver, Direction direction)
    {
        return this.facingMatchesDirection(direction);
    }

    public boolean facingMatchesDirection(Direction direction)
    {
        return direction.toSideValue() == this.getFacing();
    }

    public int getMaxEnergyOutput()
    {
        return this.output;
    }

    public int demandsEnergy()
    {
        return this.maxStorage - this.energy;
    }

    public int injectEnergy(Direction directionFrom, int amount)
    {
        if (amount > this.output)
        {
            IC2.explodeMachineAt(super.worldObj, super.xCoord, super.yCoord, super.zCoord);
            return 0;
        }
        else if (this.energy >= this.maxStorage)
        {
            return amount;
        }
        else
        {
            this.energy += amount;
            return 0;
        }
    }

    public int getMaxSafeInput()
    {
        return this.output;
    }

    public ContainerBase getGuiContainer(EntityPlayer entityPlayer)
    {
        return new ContainerElectricBlock(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return new GuiElectricBlock(new ContainerElectricBlock(entityPlayer, this));
    }

    public void onGuiClosed(EntityPlayer entityPlayer) {}

    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
    {
        return this.getFacing() != side;
    }

    public void setFacing(short facing)
    {
        if (this.addedToEnergyNet)
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }

        super.setFacing(facing);

        if (this.addedToEnergyNet)
        {
            this.addedToEnergyNet = false;
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }
    }

    public boolean isEmittingRedstone()
    {
        return this.isEmittingRedstone;
    }

    public boolean shouldEmitRedstone()
    {
        boolean shouldEmitRedstone = false;

        switch (this.redstoneMode)
        {
            case 1:
                shouldEmitRedstone = this.energy >= this.maxStorage;
                break;

            case 2:
                shouldEmitRedstone = this.energy > this.output && this.energy < this.maxStorage;
                break;

            case 3:
                shouldEmitRedstone = this.energy < this.output;
        }

        if (this.isEmittingRedstone != shouldEmitRedstone && this.redstoneUpdateInhibit != 0)
        {
            --this.redstoneUpdateInhibit;
            return this.isEmittingRedstone;
        }
        else
        {
            this.redstoneUpdateInhibit = 5;
            return shouldEmitRedstone;
        }
    }

    public void onNetworkEvent(EntityPlayer player, int event)
    {
        ++this.redstoneMode;

        if (this.redstoneMode >= redstoneModes)
        {
            this.redstoneMode = 0;
        }

        switch (this.redstoneMode)
        {
            case 0:
                IC2.platform.messagePlayer(player, "Redstone Behavior: Nothing", new Object[0]);
                break;

            case 1:
                IC2.platform.messagePlayer(player, "Redstone Behavior: Emit if full", new Object[0]);
                break;

            case 2:
                IC2.platform.messagePlayer(player, "Redstone Behavior: Emit if partially filled", new Object[0]);
                break;

            case 3:
                IC2.platform.messagePlayer(player, "Redstone Behavior: Emit if empty", new Object[0]);
                break;

            case 4:
                IC2.platform.messagePlayer(player, "Redstone Behavior: Do not output energy", new Object[0]);
                break;

            case 5:
                IC2.platform.messagePlayer(player, "Redstone Behavior: Do not output energy unless full", new Object[0]);
        }
    }

    public int getStored()
    {
        return this.energy;
    }

    public int getCapacity()
    {
        return this.maxStorage;
    }

    public int getOutput()
    {
        return this.output;
    }

    public void setStored(int energy)
    {
        this.energy = energy;
    }

    public int addEnergy(int amount)
    {
        this.energy += amount;
        return amount;
    }

    public boolean isTeleporterCompatible(Direction side)
    {
        return true;
    }
}
