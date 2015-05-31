package ic2.core.item.tool;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.ITickCallback;
import ic2.core.Ic2Items;
import ic2.core.item.ItemCropSeed;
import ic2.core.util.StackUtil;
import java.util.Random;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class HandHeldCropnalyzer implements IHasGui, ITickCallback
{
    private final ItemStack itemStack;
    private final ItemStack[] inventory = new ItemStack[3];

    public HandHeldCropnalyzer(EntityPlayer entityPlayer, ItemStack itemStack)
    {
        this.itemStack = itemStack;

        if (IC2.platform.isSimulating())
        {
            NBTTagCompound nbtTagCompound = StackUtil.getOrCreateNbtData(itemStack);
            nbtTagCompound.setInteger("uid", (new Random()).nextInt());
            NBTTagList nbtTagList = nbtTagCompound.getTagList("Items");

            for (int i = 0; i < nbtTagList.tagCount(); ++i)
            {
                NBTTagCompound nbtTagCompoundSlot = (NBTTagCompound)nbtTagList.tagAt(i);
                byte slot = nbtTagCompoundSlot.getByte("Slot");

                if (slot >= 0 && slot < this.inventory.length)
                {
                    this.inventory[slot] = ItemStack.loadItemStackFromNBT(nbtTagCompoundSlot);
                }
            }

            IC2.addContinuousTickCallback(entityPlayer.worldObj, this);
        }
    }

    public int getSizeInventory()
    {
        return this.inventory.length;
    }

    public ItemStack getStackInSlot(int i)
    {
        return this.inventory[i];
    }

    public ItemStack decrStackSize(int slot, int amount)
    {
        if (this.inventory[slot] != null)
        {
            ItemStack ret;

            if (this.inventory[slot].stackSize <= amount)
            {
                ret = this.inventory[slot];
                this.inventory[slot] = null;
                return ret;
            }
            else
            {
                ret = this.inventory[slot].splitStack(amount);

                if (this.inventory[slot].stackSize == 0)
                {
                    this.inventory[slot] = null;
                }

                return ret;
            }
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int slot, ItemStack itemStack)
    {
        this.inventory[slot] = itemStack;

        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit())
        {
            itemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    public String getInvName()
    {
        return "Cropnalyzer";
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public void onInventoryChanged() {}

    public boolean isUseableByPlayer(EntityPlayer entityPlayer)
    {
        return true;
    }

    public void openChest() {}

    public void closeChest() {}

    public ItemStack getStackInSlotOnClosing(int var1)
    {
        return null;
    }

    public boolean isInvNameLocalized()
    {
        return false;
    }

    public boolean isItemValidForSlot(int slot, ItemStack itemStack)
    {
        return false;
    }

    public ContainerBase getGuiContainer(EntityPlayer entityPlayer)
    {
        return new ContainerCropnalyzer(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return new GuiCropnalyzer(new ContainerCropnalyzer(entityPlayer, this));
    }

    public void onGuiClosed(EntityPlayer entityPlayer)
    {
        if (IC2.platform.isSimulating())
        {
            IC2.removeContinuousTickCallback(entityPlayer.worldObj, this);
            NBTTagCompound nbtTagCompound = StackUtil.getOrCreateNbtData(this.itemStack);
            boolean dropItself = false;

            for (int var8 = 0; var8 < this.getSizeInventory(); ++var8)
            {
                if (this.inventory[var8] != null)
                {
                    NBTTagCompound var9 = StackUtil.getOrCreateNbtData(this.inventory[var8]);

                    if (nbtTagCompound.getInteger("uid") == var9.getInteger("uid"))
                    {
                        this.itemStack.stackSize = 1;
                        this.inventory[var8] = null;
                        dropItself = true;
                        break;
                    }
                }
            }

            NBTTagList var81 = new NBTTagList();
            int var91;

            for (var91 = 0; var91 < this.inventory.length; ++var91)
            {
                if (this.inventory[var91] != null)
                {
                    NBTTagCompound var10 = new NBTTagCompound();
                    var10.setByte("Slot", (byte)var91);
                    this.inventory[var91].writeToNBT(var10);
                    var81.appendTag(var10);
                }
            }

            nbtTagCompound.setTag("Items", var81);

            if (dropItself)
            {
                StackUtil.dropAsEntity(entityPlayer.worldObj, (int)entityPlayer.posX, (int)entityPlayer.posY, (int)entityPlayer.posZ, this.itemStack);
            }
            else
            {
                for (var91 = -1; var91 < entityPlayer.inventory.getSizeInventory(); ++var91)
                {
                    ItemStack var101;

                    if (var91 == -1)
                    {
                        var101 = entityPlayer.inventory.getItemStack();
                    }
                    else
                    {
                        var101 = entityPlayer.inventory.getStackInSlot(var91);
                    }

                    if (var101 != null)
                    {
                        NBTTagCompound nbtTagCompoundSlot = var101.getTagCompound();

                        if (nbtTagCompoundSlot != null && nbtTagCompound.getInteger("uid") == nbtTagCompoundSlot.getInteger("uid"))
                        {
                            this.itemStack.stackSize = 1;

                            if (var91 == -1)
                            {
                                entityPlayer.inventory.setItemStack(this.itemStack);
                            }
                            else
                            {
                                entityPlayer.inventory.setInventorySlotContents(var91, this.itemStack);
                            }

                            break;
                        }
                    }
                }
            }
        }
    }

    public void tickCallback(World world)
    {
        if (this.inventory[1] == null && this.inventory[0] != null && this.inventory[0].itemID == Ic2Items.cropSeed.itemID)
        {
            byte level = ItemCropSeed.getScannedFromStack(this.inventory[0]);

            if (level == 4)
            {
                this.inventory[1] = this.inventory[0];
                this.inventory[0] = null;
                return;
            }

            if (this.inventory[2] == null || !(this.inventory[2].getItem() instanceof IElectricItem))
            {
                return;
            }

            int ned = this.energyForLevel(level);
            int got = ElectricItem.manager.discharge(this.inventory[2], ned, 2, true, false);

            if (got < ned)
            {
                return;
            }

            ItemCropSeed.incrementScannedOfStack(this.inventory[0]);
            this.inventory[1] = this.inventory[0];
            this.inventory[0] = null;
        }
    }

    public int energyForLevel(int i)
    {
        switch (i)
        {
            case 1:
                return 90;

            case 2:
                return 900;

            case 3:
                return 9000;

            default:
                return 10;
        }
    }

    public CropCard crop()
    {
        return Crops.instance.getCropList()[ItemCropSeed.getIdFromStack(this.inventory[1])];
    }

    public int getScannedLevel()
    {
        return this.inventory[1] != null && this.inventory[1].getItem() == Ic2Items.cropSeed.getItem() ? ItemCropSeed.getScannedFromStack(this.inventory[1]) : -1;
    }

    public String getSeedName()
    {
        return this.crop().name();
    }

    public String getSeedTier()
    {
        switch (this.crop().tier())
        {
            case 1:
                return "I";

            case 2:
                return "II";

            case 3:
                return "III";

            case 4:
                return "IV";

            case 5:
                return "V";

            case 6:
                return "VI";

            case 7:
                return "VII";

            case 8:
                return "VIII";

            case 9:
                return "IX";

            case 10:
                return "X";

            case 11:
                return "XI";

            case 12:
                return "XII";

            case 13:
                return "XIII";

            case 14:
                return "XIV";

            case 15:
                return "XV";

            case 16:
                return "XVI";

            default:
                return "0";
        }
    }

    public String getSeedDiscovered()
    {
        return this.crop().discoveredBy();
    }

    public String getSeedDesc(int i)
    {
        return this.crop().desc(i);
    }

    public int getSeedGrowth()
    {
        return ItemCropSeed.getGrowthFromStack(this.inventory[1]);
    }

    public int getSeedGain()
    {
        return ItemCropSeed.getGainFromStack(this.inventory[1]);
    }

    public int getSeedResistence()
    {
        return ItemCropSeed.getResistanceFromStack(this.inventory[1]);
    }

    public boolean matchesUid(int uid)
    {
        NBTTagCompound nbtTagCompound = StackUtil.getOrCreateNbtData(this.itemStack);
        return nbtTagCompound.getInteger("uid") == uid;
    }
}
