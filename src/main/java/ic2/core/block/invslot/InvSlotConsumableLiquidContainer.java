package ic2.core.block.invslot;

import ic2.core.block.TileEntityInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class InvSlotConsumableLiquidContainer extends InvSlotConsumable
{
    public InvSlotConsumableLiquidContainer(TileEntityInventory base, String name, int oldStartIndex, int count)
    {
        super(base, name, oldStartIndex, count);
    }

    public boolean accepts(ItemStack itemStack)
    {
        return FluidContainerRegistry.isEmptyContainer(itemStack);
    }

    public ItemStack fill(FluidStack liquid, boolean simulate)
    {
        ItemStack container = this.consume(1, true, true);

        if (container == null)
        {
            return null;
        }
        else
        {
            ItemStack filled = FluidContainerRegistry.fillFluidContainer(liquid, container);

            if (filled == null)
            {
                return null;
            }
            else
            {
                if (!simulate)
                {
                    this.consume(1, false, true);
                }

                return filled;
            }
        }
    }
}
