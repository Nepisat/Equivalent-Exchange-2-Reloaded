package ic2.core.block.invslot;

import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot$Access;
import ic2.core.item.IUpgradeItem;
import net.minecraft.item.ItemStack;

public class InvSlotUpgrade extends InvSlot
{
    public InvSlotUpgrade(TileEntityInventory base, String name, int oldStartIndex, int count)
    {
        super(base, name, oldStartIndex, InvSlot$Access.NONE, count);
    }

    public boolean accepts(ItemStack itemStack)
    {
        return itemStack.getItem() instanceof IUpgradeItem;
    }
}
