package ic2.core;

import ic2.api.Direction;

class EnergyNet$1
{
    static final int[] $SwitchMap$ic2$api$Direction = new int[Direction.values().length];

    static
    {
        try
        {
            $SwitchMap$ic2$api$Direction[Direction.XN.ordinal()] = 1;
        }
        catch (NoSuchFieldError var6)
        {
            ;
        }

        try
        {
            $SwitchMap$ic2$api$Direction[Direction.XP.ordinal()] = 2;
        }
        catch (NoSuchFieldError var5)
        {
            ;
        }

        try
        {
            $SwitchMap$ic2$api$Direction[Direction.YN.ordinal()] = 3;
        }
        catch (NoSuchFieldError var4)
        {
            ;
        }

        try
        {
            $SwitchMap$ic2$api$Direction[Direction.YP.ordinal()] = 4;
        }
        catch (NoSuchFieldError var3)
        {
            ;
        }

        try
        {
            $SwitchMap$ic2$api$Direction[Direction.ZN.ordinal()] = 5;
        }
        catch (NoSuchFieldError var2)
        {
            ;
        }

        try
        {
            $SwitchMap$ic2$api$Direction[Direction.ZP.ordinal()] = 6;
        }
        catch (NoSuchFieldError var1)
        {
            ;
        }
    }
}
