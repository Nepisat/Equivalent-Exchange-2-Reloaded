package ic2.core.block.personal;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.Ic2Items;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlot$Access;
import ic2.core.block.invslot.InvSlotCharge;
import ic2.core.block.invslot.InvSlotConsumableLinked;
import ic2.core.block.invslot.InvSlotUpgrade;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityEnergyOMat extends TileEntityInventory implements IPersonalBlock, IHasGui, IEnergySink, IEnergySource, INetworkClientTileEntityEventListener
{
    public int euOffer = 1000;
    public String owner = "null";
    private boolean addedToEnergyNet = false;
    public int paidFor;
    public int euBuffer;
    private int euBufferMax = 10000;
    private int maxOutputRate = 32;
    public final InvSlot demandSlot;
    public final InvSlotConsumableLinked inputSlot;
    public final InvSlotCharge chargeSlot;
    public final InvSlotUpgrade upgradeSlot;

    public TileEntityEnergyOMat()
    {
        this.demandSlot = new InvSlot(this, "demand", 0, InvSlot$Access.NONE, 1);
        this.inputSlot = new InvSlotConsumableLinked(this, "input", 1, 1, this.demandSlot);
        this.chargeSlot = new InvSlotCharge(this, -1, 1);
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 2, 1);
    }

    public String getInvName()
    {
        return "Energy-O-Mat";
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        this.owner = nbttagcompound.getString("owner");
        this.euOffer = nbttagcompound.getInteger("euOffer");
        this.paidFor = nbttagcompound.getInteger("paidFor");
        this.euBuffer = nbttagcompound.getInteger("euBuffer");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setString("owner", this.owner);
        nbttagcompound.setInteger("euOffer", this.euOffer);
        nbttagcompound.setInteger("paidFor", this.paidFor);
        nbttagcompound.setInteger("euBuffer", this.euBuffer);
    }

    public boolean wrenchCanRemove(EntityPlayer entityPlayer)
    {
        return this.permitsAccess(entityPlayer);
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

        if (IC2.platform.isSimulating())
        {
            boolean invChanged = false;
            this.euBufferMax = 10000;
            this.maxOutputRate = 32;
            this.chargeSlot.setTier(1);

            if (!this.upgradeSlot.isEmpty())
            {
                if (this.upgradeSlot.get().isItemEqual(Ic2Items.energyStorageUpgrade))
                {
                    this.euBufferMax = 10000 * (this.upgradeSlot.get().stackSize + 1);
                }
                else if (this.upgradeSlot.get().isItemEqual(Ic2Items.transformerUpgrade))
                {
                    this.maxOutputRate = 32 * (int)Math.pow(4.0D, (double)Math.min(4, this.upgradeSlot.get().stackSize));
                    this.chargeSlot.setTier(1 + this.upgradeSlot.get().stackSize);
                }
            }

            ItemStack tradedIn = this.inputSlot.consumeLinked(true);
            int min;

            if (tradedIn != null)
            {
                min = StackUtil.distribute(this, tradedIn, true);

                if (min == tradedIn.stackSize)
                {
                    StackUtil.distribute(this, this.inputSlot.consumeLinked(false), false);
                    this.paidFor += this.euOffer;
                    invChanged = true;
                }
            }

            if (this.euBuffer > 0)
            {
                min = this.chargeSlot.charge(this.euBuffer);

                if (min > 0)
                {
                    this.euBuffer -= min;
                    invChanged = true;
                }
            }

            if (this.euBuffer > this.euBufferMax)
            {
                this.euBuffer = this.euBufferMax;
            }

            min = Math.min(this.maxOutputRate, this.euBuffer);
            EnergyTileSourceEvent event = new EnergyTileSourceEvent(this, min);
            MinecraftForge.EVENT_BUS.post(event);
            this.euBuffer -= min - event.amount;

            if (invChanged)
            {
                this.onInventoryChanged();
            }
        }
    }

    public boolean permitsAccess(EntityPlayer player)
    {
        if (player == null)
        {
            return false;
        }
        else if (this.owner.equals("null"))
        {
            if (IC2.platform.isSimulating())
            {
                this.owner = player.username;
                IC2.network.updateTileEntityField(this, "owner");
            }

            return true;
        }
        else
        {
            return MinecraftServer.getServer() != null && !MinecraftServer.getServer().isDedicatedServer() ? true : this.owner.equalsIgnoreCase(player.username);
        }
    }

    public boolean permitsAccess(String username)
    {
        return this.owner.equals(username);
    }

    public String getUsername()
    {
        return this.owner;
    }

    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
    {
        return this.getFacing() != side && this.permitsAccess(entityPlayer);
    }

    public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
    {
        return !this.facingMatchesDirection(direction);
    }

    public boolean facingMatchesDirection(Direction direction)
    {
        return direction.toSideValue() == this.getFacing();
    }

    public boolean isAddedToEnergyNet()
    {
        return this.addedToEnergyNet;
    }

    public boolean emitsEnergyTo(TileEntity receiver, Direction direction)
    {
        return this.facingMatchesDirection(direction);
    }

    public int getMaxEnergyOutput()
    {
        return 32;
    }

    public int demandsEnergy()
    {
        return Math.min(this.paidFor, this.euBufferMax - this.euBuffer);
    }

    public int injectEnergy(Direction directionFrom, int amount)
    {
        int toAdd = Math.min(Math.min(amount, this.paidFor), this.euBufferMax - this.euBuffer);
        this.paidFor -= toAdd;
        this.euBuffer += toAdd;
        return amount - toAdd;
    }

    public int getMaxSafeInput()
    {
        return Integer.MAX_VALUE;
    }

    public ContainerBase getGuiContainer(EntityPlayer entityPlayer)
    {
        return (ContainerBase)(this.permitsAccess(entityPlayer) ? new ContainerEnergyOMatOpen(entityPlayer, this) : new ContainerEnergyOMatClosed(entityPlayer, this));
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return (GuiScreen)(this.permitsAccess(entityPlayer) ? new GuiEnergyOMatOpen(new ContainerEnergyOMatOpen(entityPlayer, this)) : new GuiEnergyOMatClosed(new ContainerEnergyOMatClosed(entityPlayer, this)));
    }

    public void onGuiClosed(EntityPlayer entityPlayer) {}

    public void onNetworkEvent(EntityPlayer player, int event)
    {
        if (this.permitsAccess(player))
        {
            switch (event)
            {
                case 0:
                    this.attemptSet(-100000);
                    break;

                case 1:
                    this.attemptSet(-10000);
                    break;

                case 2:
                    this.attemptSet(-1000);
                    break;

                case 3:
                    this.attemptSet(-100);
                    break;

                case 4:
                    this.attemptSet(100000);
                    break;

                case 5:
                    this.attemptSet(10000);
                    break;

                case 6:
                    this.attemptSet(1000);
                    break;

                case 7:
                    this.attemptSet(100);
            }
        }
    }

    private void attemptSet(int amount)
    {
        this.euOffer += amount;

        if (this.euOffer < 100)
        {
            this.euOffer = 100;
        }
    }
}
