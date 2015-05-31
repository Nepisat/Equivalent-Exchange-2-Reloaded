package ic2.core.block.invslot;

import ic2.core.block.TileEntityInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class InvSlotProcessableSmelting extends InvSlotProcessable
{
    public InvSlotProcessableSmelting(TileEntityInventory base, String name, int oldStartIndex, int count)
    {
        super(base, name, oldStartIndex, count);
    }

    public boolean accepts(ItemStack itemStack)
    {
        return FurnaceRecipes.smelting().getSmeltingResult(itemStack) != null;
    }

    public ItemStack process(boolean simulate)
    {
        ItemStack input = this.consume(1, simulate, true);
        return input == null ? null : FurnaceRecipes.smelting().getSmeltingResult(input).copy();
    }
}
