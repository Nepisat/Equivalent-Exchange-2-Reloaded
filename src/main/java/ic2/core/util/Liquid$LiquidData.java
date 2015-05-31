package ic2.core.util;

import net.minecraftforge.fluids.Fluid;

public class Liquid$LiquidData
{
    public final Fluid liquid;
    public final boolean isSource;

    Liquid$LiquidData(Fluid liquid, boolean isSource)
    {
        this.liquid = liquid;
        this.isSource = isSource;
    }
}
