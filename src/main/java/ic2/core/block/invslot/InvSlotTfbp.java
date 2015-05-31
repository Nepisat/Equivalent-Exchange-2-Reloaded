package ic2.core.block.invslot;

import ic2.api.item.ITerraformingBP;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot$Access;
import net.minecraft.item.ItemStack;

public class InvSlotTfbp extends InvSlot
{
    public InvSlotTfbp(TileEntityInventory base, String name, int oldStartIndex, int count)
    {
        super(base, name, oldStartIndex, InvSlot$Access.IO, count);
    }

    public boolean accepts(ItemStack itemStack)
    {
        return itemStack.getItem() instanceof ITerraformingBP;
    }
}
