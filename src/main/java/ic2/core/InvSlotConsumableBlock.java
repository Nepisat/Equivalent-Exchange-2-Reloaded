package ic2.core;

import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot$Access;
import ic2.core.block.invslot.InvSlot$InvSide;
import ic2.core.block.invslot.InvSlotConsumable;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class InvSlotConsumableBlock extends InvSlotConsumable
{
    public InvSlotConsumableBlock(TileEntityInventory base, String name, int oldStartIndex, int count)
    {
        this(base, name, oldStartIndex, InvSlot$Access.I, count, InvSlot$InvSide.TOP);
    }

    public InvSlotConsumableBlock(TileEntityInventory base, String name, int oldStartIndex, InvSlot$Access access, int count, InvSlot$InvSide preferredSide)
    {
        super(base, name, oldStartIndex, access, count, preferredSide);
    }

    public boolean accepts(ItemStack itemStack)
    {
        return itemStack.getItem() instanceof ItemBlock;
    }
}
