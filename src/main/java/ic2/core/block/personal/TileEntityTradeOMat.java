package ic2.core.block.personal;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.audio.PositionSpec;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlot$Access;
import ic2.core.block.invslot.InvSlotConsumableLinked;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.util.StackUtil;
import java.util.List;
import java.util.Vector;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class TileEntityTradeOMat extends TileEntityInventory implements IPersonalBlock, IHasGui, INetworkTileEntityEventListener, INetworkClientTileEntityEventListener
{
    private int ticker;
    public String owner = "null";
    public int totalTradeCount = 0;
    public int stock = 0;
    public boolean infinite = false;
    private static final int stockUpdateRate = 64;
    private static final int EventTrade = 0;
    public final InvSlot demandSlot;
    public final InvSlot offerSlot;
    public final InvSlotConsumableLinked inputSlot;
    public final InvSlotOutput outputSlot;

    public TileEntityTradeOMat()
    {
        this.ticker = IC2.random.nextInt(64);
        this.demandSlot = new InvSlot(this, "demand", 0, InvSlot$Access.NONE, 1);
        this.offerSlot = new InvSlot(this, "offer", 1, InvSlot$Access.NONE, 1);
        this.inputSlot = new InvSlotConsumableLinked(this, "input", 2, 1, this.demandSlot);
        this.outputSlot = new InvSlotOutput(this, "output", 3, 1);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        this.owner = nbttagcompound.getString("owner");
        this.totalTradeCount = nbttagcompound.getInteger("totalTradeCount");

        if (nbttagcompound.hasKey("infinite"))
        {
            this.infinite = nbttagcompound.getBoolean("infinite");
        }
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setString("owner", this.owner);
        nbttagcompound.setInteger("totalTradeCount", this.totalTradeCount);

        if (this.infinite)
        {
            nbttagcompound.setBoolean("infinite", this.infinite);
        }
    }

    public List<String> getNetworkedFields()
    {
        Vector ret = new Vector(1);
        ret.add("owner");
        ret.addAll(super.getNetworkedFields());
        return ret;
    }

    public boolean enableUpdateEntity()
    {
        return IC2.platform.isSimulating();
    }

    public void updateEntity()
    {
        super.updateEntity();
        ItemStack tradedIn = this.inputSlot.consumeLinked(true);

        if (!this.offerSlot.isEmpty() && tradedIn != null && this.outputSlot.canAdd(this.offerSlot.get()))
        {
            if (this.infinite)
            {
                this.inputSlot.consumeLinked(false);
                this.outputSlot.add(this.offerSlot.get().copy());
            }
            else
            {
                ItemStack transferredIn = StackUtil.fetch(this, this.offerSlot.get(), true);

                if (transferredIn != null && transferredIn.stackSize == this.offerSlot.get().stackSize)
                {
                    int transferredOut = StackUtil.distribute(this, tradedIn, true);

                    if (transferredOut == tradedIn.stackSize)
                    {
                        StackUtil.distribute(this, this.inputSlot.consumeLinked(false), false);
                        this.outputSlot.add(StackUtil.fetch(this, this.offerSlot.get(), false));
                        ++this.totalTradeCount;
                        this.stock -= this.offerSlot.get().stackSize;
                        IC2.network.initiateTileEntityEvent(this, 0, true);
                        this.onInventoryChanged();
                    }
                }
            }
        }

        if (this.infinite)
        {
            this.stock = -1;
        }
        else if (this.ticker++ % 64 == 0)
        {
            this.updateStock();
        }
    }

    public void onLoaded()
    {
        super.onLoaded();

        if (IC2.platform.isSimulating())
        {
            this.updateStock();
        }
    }

    public void updateStock()
    {
        this.stock = 0;
        ItemStack offer = this.offerSlot.get();

        if (offer != null)
        {
            ItemStack available = StackUtil.fetch(this, StackUtil.copyWithSize(offer, Integer.MAX_VALUE), true);

            if (available == null)
            {
                this.stock = 0;
            }
            else
            {
                this.stock = available.stackSize;
            }
        }
    }

    public boolean wrenchCanRemove(EntityPlayer entityPlayer)
    {
        return this.permitsAccess(entityPlayer);
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

    public String getInvName()
    {
        return "Trade-O-Mat";
    }

    public ContainerBase getGuiContainer(EntityPlayer entityPlayer)
    {
        return (ContainerBase)(this.permitsAccess(entityPlayer) ? new ContainerTradeOMatOpen(entityPlayer, this) : new ContainerTradeOMatClosed(entityPlayer, this));
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return (GuiScreen)(this.permitsAccess(entityPlayer) ? new GuiTradeOMatOpen(new ContainerTradeOMatOpen(entityPlayer, this), isAdmin) : new GuiTradeOMatClosed(new ContainerTradeOMatClosed(entityPlayer, this)));
    }

    public void onGuiClosed(EntityPlayer entityPlayer) {}

    public void onNetworkEvent(int event)
    {
        switch (event)
        {
            case 0:
                IC2.audioManager.playOnce(this, PositionSpec.Center, "Machines/o-mat.ogg", true, IC2.audioManager.defaultVolume);
                break;

            default:
                IC2.platform.displayError("An unknown event type was received over multiplayer.\nThis could happen due to corrupted data or a bug.\n\n(Technical information: event ID " + event + ", tile entity below)\n" + "T: " + this + " (" + super.xCoord + "," + super.yCoord + "," + super.zCoord + ")");
        }
    }

    public void onNetworkEvent(EntityPlayer player, int event)
    {
        if (event == 0)
        {
            MinecraftServer server = MinecraftServer.getServer();

            if (server.getConfigurationManager().isPlayerOpped(player.username))
            {
                this.infinite = !this.infinite;

                if (!this.infinite)
                {
                    this.updateStock();
                }
            }
        }
    }
}
