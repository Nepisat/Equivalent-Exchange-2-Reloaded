package ic2.core.slot;

import ic2.core.block.invslot.InvSlot;
import java.util.Iterator;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotInvSlot extends Slot
{
    public final InvSlot invSlot;
    public final int index;

    public SlotInvSlot(InvSlot invSlot, int index, int xDisplayPosition, int yDisplayPosition)
    {
        super(invSlot.base, -1, xDisplayPosition, yDisplayPosition);
        this.invSlot = invSlot;
        this.index = index;
    }

    public boolean isItemValid(ItemStack itemStack)
    {
        return this.invSlot.accepts(itemStack);
    }

    public ItemStack getStack()
    {
        return this.invSlot.get(this.index);
    }

    public void putStack(ItemStack itemStack)
    {
        this.invSlot.put(this.index, itemStack);
        this.onSlotChanged();
    }

    public ItemStack decrStackSize(int amount)
    {
        ItemStack itemStack = this.invSlot.get(this.index);

        if (itemStack == null)
        {
            return null;
        }
        else if (itemStack.stackSize <= amount)
        {
            this.invSlot.put(this.index, (ItemStack)null);
            this.onSlotChanged();
            return itemStack;
        }
        else
        {
            ItemStack ret = itemStack.copy();
            ret.stackSize = amount;
            itemStack.stackSize -= amount;
            this.onSlotChanged();
            return ret;
        }
    }

    public boolean isSlotInInventory(IInventory inventory, int index)
    {
        if (inventory != this.invSlot.base)
        {
            return false;
        }
        else
        {
            InvSlot invSlot;

            for (Iterator i$ = this.invSlot.base.invSlots.iterator(); i$.hasNext(); index -= invSlot.size())
            {
                invSlot = (InvSlot)i$.next();

                if (index < invSlot.size())
                {
                    return index == this.index;
                }
            }

            return false;
        }
    }
}
