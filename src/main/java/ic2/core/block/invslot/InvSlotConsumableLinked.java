package ic2.core.block.invslot;

import ic2.core.block.TileEntityInventory;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;

public class InvSlotConsumableLinked extends InvSlotConsumable
{
    public final InvSlot linkedSlot;

    public InvSlotConsumableLinked(TileEntityInventory base, String name, int oldStartIndex, int count, InvSlot linkedSlot)
    {
        super(base, name, oldStartIndex, count);
        this.linkedSlot = linkedSlot;
    }

    public boolean accepts(ItemStack itemStack)
    {
        ItemStack required = this.linkedSlot.get();
        return required == null ? false : StackUtil.isStackEqual(required, itemStack);
    }

    public ItemStack consumeLinked(boolean simulate)
    {
        ItemStack required = this.linkedSlot.get();

        if (required != null && required.stackSize > 0)
        {
            ItemStack available = this.consume(required.stackSize, true, true);
            return available != null && available.stackSize == required.stackSize ? this.consume(required.stackSize, simulate, true) : null;
        }
        else
        {
            return null;
        }
    }
}
