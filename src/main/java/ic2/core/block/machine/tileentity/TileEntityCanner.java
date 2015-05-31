package ic2.core.block.machine.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.Ic2Items;
import ic2.core.audio.AudioSource;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlot$Access;
import ic2.core.block.invslot.InvSlotConsumable;
import ic2.core.block.invslot.InvSlotConsumableId;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.machine.ContainerCanner;
import ic2.core.block.machine.gui.GuiCanner;
import ic2.core.util.StackUtil;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;

public class TileEntityCanner extends TileEntityElectricMachine implements IHasGui
{
    public static final Map<ChunkCoordIntPair, Integer> specialFood = new HashMap();
    public short progress = 0;
    public int energyconsume = 1;
    public int operationLength = 600;
    private int fuelQuality = 0;
    public AudioSource audioSource;
    public final InvSlot resInputSlot;
    public final InvSlotConsumable inputSlot;
    public final InvSlotOutput outputSlot;

    public TileEntityCanner()
    {
        super(600, 1, 1);
        this.resInputSlot = new InvSlot(this, "input", 0, InvSlot$Access.I, 1);
        this.inputSlot = new InvSlotConsumableId(this, "canInput", 3, 1, new int[] {Ic2Items.tinCan.itemID, Ic2Items.fuelCan.itemID, Ic2Items.jetpack.itemID, Ic2Items.cfPack.itemID});
        this.outputSlot = new InvSlotOutput(this, "output", 2, 1);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);

        try
        {
            this.fuelQuality = nbttagcompound.getInteger("fuelQuality");
        }
        catch (Throwable var3)
        {
            this.fuelQuality = nbttagcompound.getShort("fuelQuality");
        }
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("fuelQuality", this.fuelQuality);
    }

    public int gaugeProgressScaled(int i)
    {
        int l = this.operationLength;

        if (this.getMode() == 1 && this.resInputSlot.get() != null)
        {
            int food = this.getFoodValue(this.resInputSlot.get());

            if (food > 0)
            {
                l = 50 * food;
            }
        }

        if (this.getMode() == 3)
        {
            l = 50;
        }

        return this.progress * i / l;
    }

    public int gaugeFuelScaled(int i)
    {
        if (super.energy <= 0)
        {
            return 0;
        }
        else
        {
            int r = super.energy * i / (this.operationLength * this.energyconsume);
            return r > i ? i : r;
        }
    }

    public void updateEntity()
    {
        super.updateEntity();
        boolean needsInvUpdate = false;
        boolean canOperate = this.canOperate();
        boolean newActive = this.getActive();

        if (canOperate && (this.getMode() == 1 && this.progress >= this.getFoodValue(this.resInputSlot.get()) * 50 || this.getMode() == 2 && this.progress > 0 && this.progress % 100 == 0 || this.getMode() == 3 && this.progress >= 50))
        {
            if (this.getMode() != 1 && this.getMode() != 3 && this.progress < 600)
            {
                this.operate(true);
            }
            else
            {
                this.operate(false);
                this.fuelQuality = 0;
                this.progress = 0;
                newActive = false;
            }

            needsInvUpdate = true;
        }

        if (newActive && this.progress != 0)
        {
            if (!canOperate || super.energy < this.energyconsume)
            {
                if (!canOperate && this.getMode() != 2)
                {
                    this.fuelQuality = 0;
                    this.progress = 0;
                }

                newActive = false;
            }
        }
        else if (canOperate)
        {
            if (super.energy >= this.energyconsume)
            {
                newActive = true;
            }
        }
        else if (this.getMode() != 2)
        {
            this.fuelQuality = 0;
            this.progress = 0;
        }

        if (newActive)
        {
            ++this.progress;
            super.energy -= this.energyconsume;
        }

        if (needsInvUpdate)
        {
            this.onInventoryChanged();
        }

        if (newActive != this.getActive())
        {
            this.setActive(newActive);
        }
    }

    public void operate(boolean incremental)
    {
        switch (this.getMode())
        {
            case 1:
                int food = this.getFoodValue(this.resInputSlot.get());
                int meta = this.getFoodMeta(this.resInputSlot.get());
                --this.resInputSlot.get().stackSize;

                if (this.resInputSlot.get().getItem() == Item.bowlSoup && this.resInputSlot.get().stackSize <= 0)
                {
                    this.resInputSlot.put(new ItemStack(Item.bowlEmpty));
                }

                if (this.resInputSlot.get().stackSize <= 0)
                {
                    this.resInputSlot.clear();
                }

                this.inputSlot.consume(food);
                this.outputSlot.add(new ItemStack(Ic2Items.filledTinCan.getItem(), food, meta));
                break;

            case 2:
                int fuel = this.getFuelValue(this.resInputSlot.get().itemID);
                --this.resInputSlot.get().stackSize;

                if (this.resInputSlot.get().stackSize <= 0)
                {
                    this.resInputSlot.clear();
                }

                this.fuelQuality += fuel;

                if (!incremental)
                {
                    if (this.inputSlot.get().itemID == Ic2Items.fuelCan.itemID)
                    {
                        this.inputSlot.consume(1);
                        ItemStack var7 = Ic2Items.filledFuelCan.copy();
                        NBTTagCompound data = StackUtil.getOrCreateNbtData(var7);
                        data.setInteger("value", this.fuelQuality);
                        this.outputSlot.add(var7);
                    }
                    else
                    {
                        int var71 = this.inputSlot.get().getItemDamage();
                        var71 -= this.fuelQuality;

                        if (var71 < 1)
                        {
                            var71 = 1;
                        }

                        this.inputSlot.clear();
                        this.outputSlot.add(new ItemStack(Ic2Items.jetpack.itemID, 1, var71));
                    }
                }

                break;

            case 3:
                --this.resInputSlot.get().stackSize;

                if (this.resInputSlot.get().stackSize <= 0)
                {
                    this.resInputSlot.clear();
                }

                this.inputSlot.get().setItemDamage(this.inputSlot.get().getItemDamage() - 2);

                if (this.resInputSlot.isEmpty() || this.inputSlot.get().getItemDamage() <= 1)
                {
                    this.outputSlot.add(this.inputSlot.get());
                    this.inputSlot.clear();
                }
        }
    }

    public void onUnloaded()
    {
        if (this.audioSource != null)
        {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
        }

        super.onUnloaded();
    }

    public boolean canOperate()
    {
        if (this.resInputSlot.isEmpty())
        {
            return false;
        }
        else
        {
            switch (this.getMode())
            {
                case 1:
                    int food = this.getFoodValue(this.resInputSlot.get());
                    return food > 0 && food <= this.inputSlot.get().stackSize && this.outputSlot.canAdd(new ItemStack(Ic2Items.filledTinCan.itemID, food, this.getFoodMeta(this.resInputSlot.get())));

                case 2:
                    int fuel = this.getFuelValue(this.resInputSlot.get().itemID);
                    return fuel > 0 && this.outputSlot.canAdd(Ic2Items.jetpack);

                case 3:
                    return this.inputSlot.get().getItemDamage() > 2 && this.getPelletValue(this.resInputSlot.get()) > 0 && this.outputSlot.canAdd(this.resInputSlot.get());

                default:
                    return false;
            }
        }
    }

    public int getMode()
    {
        return this.inputSlot.isEmpty() ? 0 : (this.inputSlot.get().itemID == Ic2Items.tinCan.itemID ? 1 : (this.inputSlot.get().itemID != Ic2Items.fuelCan.itemID && this.inputSlot.get().itemID != Ic2Items.jetpack.itemID ? (this.inputSlot.get().itemID == Ic2Items.cfPack.itemID ? 3 : 0) : 2));
    }

    public String getInvName()
    {
        return "Canning Machine";
    }

    private int getFoodValue(ItemStack item)
    {
        if (item.itemID == Ic2Items.filledTinCan.itemID)
        {
            return 0;
        }
        else if (item.getItem() instanceof ItemFood)
        {
            ItemFood food = (ItemFood)item.getItem();
            return (int)Math.ceil((double)food.getHealAmount() / 2.0D);
        }
        else
        {
            return item.itemID != Item.cake.itemID && item.itemID != Block.cake.blockID ? 0 : 6;
        }
    }

    public int getFuelValue(int id)
    {
        return id == Ic2Items.coalfuelCell.itemID ? 2548 : (id == Ic2Items.biofuelCell.itemID ? 868 : (id == Item.redstone.itemID && this.fuelQuality > 0 ? (int)((double)this.fuelQuality * 0.2D) : (id == Item.glowstone.itemID && this.fuelQuality > 0 ? (int)((double)this.fuelQuality * 0.3D) : (id == Item.gunpowder.itemID && this.fuelQuality > 0 ? (int)((double)this.fuelQuality * 0.4D) : 0))));
    }

    public int getPelletValue(ItemStack item)
    {
        return item == null ? 0 : (item.itemID != Ic2Items.constructionFoamPellet.itemID ? 0 : item.stackSize);
    }

    private int getFoodMeta(ItemStack item)
    {
        if (item == null)
        {
            return 0;
        }
        else
        {
            ChunkCoordIntPair ccip = new ChunkCoordIntPair(item.itemID, item.getItemDamage());
            return specialFood.containsKey(ccip) ? ((Integer)specialFood.get(ccip)).intValue() : 0;
        }
    }

    public String getStartSoundFile()
    {
        return null;
    }

    public String getInterruptSoundFile()
    {
        return null;
    }

    public ContainerBase getGuiContainer(EntityPlayer entityPlayer)
    {
        return new ContainerCanner(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return new GuiCanner(new ContainerCanner(entityPlayer, this));
    }

    public void onGuiClosed(EntityPlayer entityPlayer) {}

    public float getWrenchDropRate()
    {
        return 0.85F;
    }

    static
    {
        specialFood.put(new ChunkCoordIntPair(Item.rottenFlesh.itemID, 0), Integer.valueOf(1));
        specialFood.put(new ChunkCoordIntPair(Item.spiderEye.itemID, 0), Integer.valueOf(2));
        specialFood.put(new ChunkCoordIntPair(Item.poisonousPotato.itemID, 0), Integer.valueOf(2));
        specialFood.put(new ChunkCoordIntPair(Item.chickenRaw.itemID, 0), Integer.valueOf(3));
        specialFood.put(new ChunkCoordIntPair(Item.appleGold.itemID, 0), Integer.valueOf(4));
        specialFood.put(new ChunkCoordIntPair(Item.appleGold.itemID, 1), Integer.valueOf(5));
    }
}
