package ic2.core.block.invslot;

import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot$Access;
import ic2.core.block.invslot.InvSlot$InvSide;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;

public class InvSlotOutput extends InvSlot
{
    public InvSlotOutput(TileEntityInventory base, String name, int oldStartIndex, int count)
    {
        super(base, name, oldStartIndex, InvSlot$Access.O, count, InvSlot$InvSide.BOTTOM);
    }

    public boolean accepts(ItemStack itemStack)
    {
        return false;
    }

    public int add(ItemStack itemStack)
    {
        return this.add(itemStack, false);
    }

    public boolean canAdd(ItemStack itemStack)
    {
        return this.add(itemStack, true) == 0;
    }

    private int add(ItemStack itemStack, boolean simulate)
    {
        if (itemStack == null)
        {
            return 0;
        }
        else
        {
            int amount = itemStack.stackSize;

            for (int i = 0; i < this.size(); ++i)
            {
                ItemStack existingItemStack = this.get(i);

                if (existingItemStack == null)
                {
                    if (!simulate)
                    {
                        this.put(i, itemStack);
                    }

                    return 0;
                }

                int space = existingItemStack.getMaxStackSize() - existingItemStack.stackSize;

                if (space > 0 && StackUtil.isStackEqual(itemStack, existingItemStack))
                {
                    if (space >= amount)
                    {
                        if (!simulate)
                        {
                            existingItemStack.stackSize += amount;
                        }

                        return 0;
                    }

                    amount -= space;

                    if (!simulate)
                    {
                        existingItemStack.stackSize += space;
                    }
                }
            }

            return amount;
        }
    }
}
