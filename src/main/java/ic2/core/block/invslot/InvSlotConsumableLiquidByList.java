package ic2.core.block.invslot;

import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot$Access;
import ic2.core.block.invslot.InvSlot$InvSide;
import ic2.core.block.invslot.InvSlotConsumableLiquid$OpType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.minecraftforge.fluids.Fluid;

public class InvSlotConsumableLiquidByList extends InvSlotConsumableLiquid
{
    private final Set<Fluid> acceptedFluids;

    public InvSlotConsumableLiquidByList(TileEntityInventory base1, String name1, int oldStartIndex1, int count, Fluid f)
    {
        super(base1, name1, oldStartIndex1, count,f);
        this.acceptedFluids = new HashSet(Arrays.asList(f));
    }

    public InvSlotConsumableLiquidByList(TileEntityInventory base1, String name1, int oldStartIndex1, InvSlot$Access access1, int count, InvSlot$InvSide preferredSide1, InvSlotConsumableLiquid$OpType opType, Fluid f)
    {
        super(base1, name1, oldStartIndex1, access1, count, preferredSide1, f);
        this.acceptedFluids = new HashSet(Arrays.asList(f));
    }

    protected boolean acceptsLiquid(Fluid fluid)
    {
        return this.acceptedFluids.contains(fluid);
    }

    protected Iterable<Fluid> getPossibleFluids()
    {
        return this.acceptedFluids;
    }
}
