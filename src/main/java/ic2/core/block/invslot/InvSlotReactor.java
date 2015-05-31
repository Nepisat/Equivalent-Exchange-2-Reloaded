package ic2.core.block.invslot;

import ic2.core.block.TileEntityInventory;
import ic2.core.block.generator.tileentity.TileEntityNuclearReactor;
import ic2.core.block.invslot.InvSlot$Access;
import net.minecraft.item.ItemStack;

public class InvSlotReactor extends InvSlot
{
    public InvSlotReactor(TileEntityInventory base, String name, int oldStartIndex, int count)
    {
        super(base, name, oldStartIndex, InvSlot$Access.IO, count);
    }

    public boolean accepts(ItemStack itemStack)
    {
        return TileEntityNuclearReactor.isUsefulItem(itemStack);
    }
}
