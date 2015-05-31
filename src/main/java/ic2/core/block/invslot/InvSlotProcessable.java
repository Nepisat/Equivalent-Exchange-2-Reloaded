package ic2.core.block.invslot;

import ic2.core.block.TileEntityInventory;
import net.minecraft.item.ItemStack;

public abstract class InvSlotProcessable extends InvSlotConsumable
{
    public InvSlotProcessable(TileEntityInventory base, String name, int oldStartIndex, int count)
    {
        super(base, name, oldStartIndex, count);
    }

    public abstract boolean accepts(ItemStack var1);

    public abstract ItemStack process(boolean var1);
}
