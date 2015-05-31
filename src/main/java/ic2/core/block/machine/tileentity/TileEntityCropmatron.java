package ic2.core.block.machine.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.Ic2Items;
import ic2.core.block.TileEntityCrop;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlotConsumable;
import ic2.core.block.invslot.InvSlotConsumableId;
import ic2.core.block.machine.ContainerCropmatron;
import ic2.core.block.machine.gui.GuiCropmatron;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityCropmatron extends TileEntityInventory implements IEnergySink, IHasGui
{
    public int energy = 0;
    public int ticker = 0;
    public int maxEnergy = 1000;
    public int scanX = -4;
    public int scanY = -1;
    public int scanZ = -4;
    public boolean addedToEnergyNet = false;
    public static int maxInput = 32;
    public final InvSlotConsumable fertilizerSlot;
    public final InvSlotConsumable hydrationSlot;
    public final InvSlotConsumable weedExSlot;

    public TileEntityCropmatron()
    {
        this.fertilizerSlot = new InvSlotConsumableId(this, "fertilizer", 0, 3, new int[] {Ic2Items.fertilizer.itemID});
        this.hydrationSlot = new InvSlotConsumableId(this, "hydration", 3, 3, new int[] {Ic2Items.hydratingCell.itemID});
        this.weedExSlot = new InvSlotConsumableId(this, "weedEx", 6, 3, new int[] {Ic2Items.weedEx.itemID});
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        this.energy = nbttagcompound.getShort("energy");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("energy", (short)this.energy);
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
        this.fertilizerSlot.organize();
        this.hydrationSlot.organize();
        this.weedExSlot.organize();

        if (this.energy >= 31)
        {
            this.scan();
        }
    }

    public void scan()
    {
        ++this.scanX;

        if (this.scanX > 4)
        {
            this.scanX = -4;
            ++this.scanZ;

            if (this.scanZ > 4)
            {
                this.scanZ = -4;
                ++this.scanY;

                if (this.scanY > 1)
                {
                    this.scanY = -1;
                }
            }
        }

        --this.energy;
        TileEntity te = super.worldObj.getBlockTileEntity(super.xCoord + this.scanX, super.yCoord + this.scanY, super.zCoord + this.scanZ);

        if (te instanceof TileEntityCrop)
        {
            TileEntityCrop crop = (TileEntityCrop)te;

            if (!this.fertilizerSlot.isEmpty() && crop.applyFertilizer(false))
            {
                this.energy -= 10;
                this.fertilizerSlot.consume(1);
            }

            if (!this.hydrationSlot.isEmpty() && crop.applyHydration(false, this.hydrationSlot))
            {
                this.energy -= 10;
            }

            if (!this.weedExSlot.isEmpty() && crop.applyWeedEx(false))
            {
                this.energy -= 10;
                this.weedExSlot.damage(1);
            }
        }
    }

    public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
    {
        return true;
    }

    public boolean isAddedToEnergyNet()
    {
        return this.addedToEnergyNet;
    }

    public int demandsEnergy()
    {
        return this.maxEnergy - this.energy;
    }

    public int gaugeEnergyScaled(int i)
    {
        if (this.energy <= 0)
        {
            return 0;
        }
        else
        {
            int r = this.energy * i / this.maxEnergy;

            if (r > i)
            {
                r = i;
            }

            return r;
        }
    }

    public int injectEnergy(Direction directionFrom, int amount)
    {
        if (amount > maxInput)
        {
            IC2.explodeMachineAt(super.worldObj, super.xCoord, super.yCoord, super.zCoord);
            return 0;
        }
        else if (this.energy >= this.maxEnergy)
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
        return maxInput;
    }

    public String getInvName()
    {
        return "Crop-Matron";
    }

    public ContainerBase getGuiContainer(EntityPlayer entityPlayer)
    {
        return new ContainerCropmatron(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return new GuiCropmatron(new ContainerCropmatron(entityPlayer, this));
    }

    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
