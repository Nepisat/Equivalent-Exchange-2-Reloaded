package ic2.core.block.invslot;

import ic2.api.info.Info;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot$Access;
import ic2.core.block.invslot.InvSlot$InvSide;
import net.minecraft.item.ItemStack;

public class InvSlotConsumableFuel extends InvSlotConsumable
{
    public final boolean allowLava;

    public InvSlotConsumableFuel(TileEntityInventory base, String name, int oldStartIndex, int count, boolean allowLava)
    {
        super(base, name, oldStartIndex, InvSlot$Access.I, count, InvSlot$InvSide.SIDE);
        this.allowLava = allowLava;
    }

    public boolean accepts(ItemStack itemStack)
    {
        return Info.itemFuel.getFuelValue(itemStack, this.allowLava) > 0;
    }

    public int consumeFuel()
    {
        ItemStack fuel = this.consume(1);
        return fuel == null ? 0 : Info.itemFuel.getFuelValue(fuel, this.allowLava);
    }
}
