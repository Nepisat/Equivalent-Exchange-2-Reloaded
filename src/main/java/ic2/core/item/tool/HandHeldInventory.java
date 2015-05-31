package ic2.core.item.tool;

import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.util.StackUtil;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public abstract class HandHeldInventory implements IHasGui
{
    protected ItemStack itemStack;
    protected ItemStack[] inventory;

    public HandHeldInventory(EntityPlayer entityPlayer, ItemStack itemStack, int inventorySize)
    {
        this.itemStack = itemStack;
        this.inventory = new ItemStack[inventorySize];

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

    public void onGuiClosed(EntityPlayer entityPlayer)
    {
        if (IC2.platform.isSimulating())
        {
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

    public boolean matchesUid(int uid)
    {
        NBTTagCompound nbtTagCompound = StackUtil.getOrCreateNbtData(this.itemStack);
        return nbtTagCompound.getInteger("uid") == uid;
    }
}
