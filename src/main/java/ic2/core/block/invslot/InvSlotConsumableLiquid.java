package ic2.core.block.invslot;

import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot$Access;
import ic2.core.block.invslot.InvSlot$InvSide;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class InvSlotConsumableLiquid extends InvSlotConsumable
{
    public final Fluid fluid;

    public InvSlotConsumableLiquid(TileEntityInventory base, String name, int oldStartIndex, int count, Fluid fluid)
    {
        this(base, name, oldStartIndex, InvSlot$Access.I, count, InvSlot$InvSide.TOP, fluid);
    }

    public InvSlotConsumableLiquid(TileEntityInventory base, String name, int oldStartIndex, InvSlot$Access access, int count, InvSlot$InvSide preferredSide, Fluid fluid)
    {
        super(base, name, oldStartIndex, access, count, preferredSide);
        this.fluid = fluid;
    }

    public boolean accepts(ItemStack itemStack)
    {
        FluidStack offeredLiquid = FluidContainerRegistry.getFluidForFilledItem(itemStack);
        return offeredLiquid != null && offeredLiquid.getFluid() == this.fluid;
    }
}
