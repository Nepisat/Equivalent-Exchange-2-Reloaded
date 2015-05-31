package ic2.core.block.invslot;

import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot$Access;
import ic2.core.block.invslot.InvSlot$InvSide;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.item.ItemStack;

public class InvSlotConsumableId extends InvSlotConsumable
{
    private final Set<Integer> itemIds;

    public InvSlotConsumableId(TileEntityInventory base, String name, int oldStartIndex, int count, int ... itemIds)
    {
        this(base, name, oldStartIndex, InvSlot$Access.I, count, InvSlot$InvSide.TOP, itemIds);
    }

    public InvSlotConsumableId(TileEntityInventory base, String name, int oldStartIndex, InvSlot$Access access, int count, InvSlot$InvSide preferredSide, int ... itemIds)
    {
        super(base, name, oldStartIndex, access, count, preferredSide);
        this.itemIds = new HashSet();
        int[] arr$ = itemIds;
        int len$ = itemIds.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            int itemId = arr$[i$];
            this.itemIds.add(Integer.valueOf(itemId));
        }
    }

    public boolean accepts(ItemStack itemStack)
    {
        return this.itemIds.contains(Integer.valueOf(itemStack.itemID));
    }
}
