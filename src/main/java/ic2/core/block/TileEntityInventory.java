package ic2.core.block;

import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlot$InvSide;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public abstract class TileEntityInventory extends TileEntityBlock implements ISidedInventory
{
    public final List<InvSlot> invSlots = new ArrayList();

    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);

        if (nbtTagCompound.hasKey("Items"))
        {
            NBTTagList var10 = nbtTagCompound.getTagList("Items");

            for (int var11 = 0; var11 < var10.tagCount(); ++var11)
            {
                NBTTagCompound var12 = (NBTTagCompound)var10.tagAt(var11);
                byte slot = var12.getByte("Slot");
                int maxOldStartIndex = -1;
                InvSlot maxSlot = null;
                Iterator index = this.invSlots.iterator();

                while (index.hasNext())
                {
                    InvSlot var13 = (InvSlot)index.next();

                    if (var13.oldStartIndex <= slot && var13.oldStartIndex > maxOldStartIndex)
                    {
                        maxOldStartIndex = var13.oldStartIndex;
                        maxSlot = var13;
                    }
                }

                if (maxSlot != null)
                {
                    int var131 = Math.min(slot - maxOldStartIndex, maxSlot.size() - 1);
                    maxSlot.put(var131, ItemStack.loadItemStackFromNBT(var12));
                }
            }
        }

        NBTTagCompound var101 = nbtTagCompound.getCompoundTag("InvSlots");
        Iterator var111 = this.invSlots.iterator();

        while (var111.hasNext())
        {
            InvSlot var121 = (InvSlot)var111.next();
            var121.readFromNbt(var101.getCompoundTag(var121.name));
        }
    }

    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        NBTTagCompound invSlotsTag = new NBTTagCompound();
        Iterator i$ = this.invSlots.iterator();

        while (i$.hasNext())
        {
            InvSlot invSlot = (InvSlot)i$.next();
            NBTTagCompound invSlotTag = new NBTTagCompound();
            invSlot.writeToNbt(invSlotTag);
            invSlotsTag.setTag(invSlot.name, invSlotTag);
        }

        nbtTagCompound.setTag("InvSlots", invSlotsTag);
    }

    public int getSizeInventory()
    {
        int ret = 0;
        InvSlot invSlot;

        for (Iterator i$ = this.invSlots.iterator(); i$.hasNext(); ret += invSlot.size())
        {
            invSlot = (InvSlot)i$.next();
        }

        return ret;
    }

    public ItemStack getStackInSlot(int index)
    {
        InvSlot invSlot;

        for (Iterator i$ = this.invSlots.iterator(); i$.hasNext(); index -= invSlot.size())
        {
            invSlot = (InvSlot)i$.next();

            if (index < invSlot.size())
            {
                return invSlot.get(index);
            }
        }

        return null;
    }

    public ItemStack decrStackSize(int index, int amount)
    {
        ItemStack itemStack = this.getStackInSlot(index);

        if (itemStack == null)
        {
            return null;
        }
        else if (amount >= itemStack.stackSize)
        {
            this.setInventorySlotContents(index, (ItemStack)null);
            return itemStack;
        }
        else
        {
            itemStack.stackSize -= amount;
            ItemStack ret = itemStack.copy();
            ret.stackSize = amount;
            return ret;
        }
    }

    public ItemStack getStackInSlotOnClosing(int index)
    {
        ItemStack ret = this.getStackInSlot(index);

        if (ret != null)
        {
            this.setInventorySlotContents(index, (ItemStack)null);
        }

        return ret;
    }

    public void setInventorySlotContents(int index, ItemStack itemStack)
    {
        InvSlot invSlot;

        for (Iterator i$ = this.invSlots.iterator(); i$.hasNext(); index -= invSlot.size())
        {
            invSlot = (InvSlot)i$.next();

            if (index < invSlot.size())
            {
                invSlot.put(index, itemStack);
                break;
            }
        }
    }

    public abstract String getInvName();

    public boolean isInvNameLocalized()
    {
        return false;
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean isUseableByPlayer(EntityPlayer entityPlayer)
    {
        return entityPlayer.getDistance((double)super.xCoord + 0.5D, (double)super.yCoord + 0.5D, (double)super.zCoord + 0.5D) <= 64.0D;
    }

    public void openChest() {}

    public void closeChest() {}

    public boolean isItemValidForSlot(int index, ItemStack itemStack)
    {
        InvSlot invSlot = this.getInvSlot(index);
        return invSlot != null && invSlot.canInput() && invSlot.accepts(itemStack);
    }

    public int[] getAccessibleSlotsFromSide(int var1)
    {
        int[] ret = new int[this.getSizeInventory()];

        for (int i = 0; i < ret.length; ret[i] = i++)
        {
            ;
        }

        return ret;
    }

    public boolean canInsertItem(int index, ItemStack itemStack, int side)
    {
        InvSlot targetSlot = this.getInvSlot(index);

        if (targetSlot == null)
        {
            return false;
        }
        else if (targetSlot.canInput() && targetSlot.accepts(itemStack))
        {
            if (targetSlot.preferredSide != InvSlot$InvSide.ANY && targetSlot.preferredSide.matches(side))
            {
                return true;
            }
            else
            {
                Iterator i$ = this.invSlots.iterator();
                InvSlot invSlot;

                do
                {
                    if (!i$.hasNext())
                    {
                        return true;
                    }

                    invSlot = (InvSlot)i$.next();
                }
                while (invSlot == targetSlot || invSlot.preferredSide == InvSlot$InvSide.ANY || !invSlot.preferredSide.matches(side) || !invSlot.canInput() || !invSlot.accepts(itemStack));

                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean canExtractItem(int index, ItemStack itemStack, int side)
    {
        InvSlot targetSlot = this.getInvSlot(index);

        if (targetSlot == null)
        {
            return false;
        }
        else if (!targetSlot.canOutput())
        {
            return false;
        }
        else if (targetSlot.preferredSide != InvSlot$InvSide.ANY && targetSlot.preferredSide.matches(side))
        {
            return true;
        }
        else
        {
            Iterator i$ = this.invSlots.iterator();
            InvSlot invSlot;

            do
            {
                if (!i$.hasNext())
                {
                    return true;
                }

                invSlot = (InvSlot)i$.next();
            }
            while (invSlot == targetSlot || invSlot.preferredSide == InvSlot$InvSide.ANY || !invSlot.preferredSide.matches(side) || !invSlot.canOutput());

            return false;
        }
    }

    public void addInvSlot(InvSlot invSlot)
    {
        this.invSlots.add(invSlot);
    }

    private InvSlot getInvSlot(int index)
    {
        InvSlot invSlot;

        for (Iterator i$ = this.invSlots.iterator(); i$.hasNext(); index -= invSlot.size())
        {
            invSlot = (InvSlot)i$.next();

            if (index < invSlot.size())
            {
                return invSlot;
            }
        }

        return null;
    }
}
