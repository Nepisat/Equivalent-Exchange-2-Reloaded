package ic2.core.block;

import ic2.api.Direction;

class BlockPoleFence$1
{
    static final int[] $SwitchMap$ic2$api$Direction = new int[Direction.values().length];

    static
    {
        try
        {
            $SwitchMap$ic2$api$Direction[Direction.XN.ordinal()] = 1;
        }
        catch (NoSuchFieldError var4)
        {
            ;
        }

        try
        {
            $SwitchMap$ic2$api$Direction[Direction.XP.ordinal()] = 2;
        }
        catch (NoSuchFieldError var3)
        {
            ;
        }

        try
        {
            $SwitchMap$ic2$api$Direction[Direction.ZN.ordinal()] = 3;
        }
        catch (NoSuchFieldError var2)
        {
            ;
        }

        try
        {
            $SwitchMap$ic2$api$Direction[Direction.ZP.ordinal()] = 4;
        }
        catch (NoSuchFieldError var1)
        {
            ;
        }
    }
}
