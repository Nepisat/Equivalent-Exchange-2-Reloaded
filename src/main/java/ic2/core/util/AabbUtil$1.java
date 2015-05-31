package ic2.core.util;

import ic2.api.Direction;
import ic2.core.util.AabbUtil$Edge;

class AabbUtil$1
{
    static final int[] $SwitchMap$ic2$api$Direction;

    static final int[] $SwitchMap$ic2$core$util$AabbUtil$Edge = new int[AabbUtil$Edge.values().length];

    static
    {
        try
        {
            $SwitchMap$ic2$core$util$AabbUtil$Edge[AabbUtil$Edge.AD.ordinal()] = 1;
        }
        catch (NoSuchFieldError var18)
        {
            ;
        }

        try
        {
            $SwitchMap$ic2$core$util$AabbUtil$Edge[AabbUtil$Edge.AB.ordinal()] = 2;
        }
        catch (NoSuchFieldError var17)
        {
            ;
        }

        try
        {
            $SwitchMap$ic2$core$util$AabbUtil$Edge[AabbUtil$Edge.AE.ordinal()] = 3;
        }
        catch (NoSuchFieldError var16)
        {
            ;
        }

        try
        {
            $SwitchMap$ic2$core$util$AabbUtil$Edge[AabbUtil$Edge.DC.ordinal()] = 4;
        }
        catch (NoSuchFieldError var15)
        {
            ;
        }

        try
        {
            $SwitchMap$ic2$core$util$AabbUtil$Edge[AabbUtil$Edge.DH.ordinal()] = 5;
        }
        catch (NoSuchFieldError var14)
        {
            ;
        }

        try
        {
            $SwitchMap$ic2$core$util$AabbUtil$Edge[AabbUtil$Edge.BC.ordinal()] = 6;
        }
        catch (NoSuchFieldError var13)
        {
            ;
        }

        try
        {
            $SwitchMap$ic2$core$util$AabbUtil$Edge[AabbUtil$Edge.BF.ordinal()] = 7;
        }
        catch (NoSuchFieldError var12)
        {
            ;
        }

        try
        {
            $SwitchMap$ic2$core$util$AabbUtil$Edge[AabbUtil$Edge.EH.ordinal()] = 8;
        }
        catch (NoSuchFieldError var11)
        {
            ;
        }

        try
        {
            $SwitchMap$ic2$core$util$AabbUtil$Edge[AabbUtil$Edge.EF.ordinal()] = 9;
        }
        catch (NoSuchFieldError var10)
        {
            ;
        }

        try
        {
            $SwitchMap$ic2$core$util$AabbUtil$Edge[AabbUtil$Edge.CG.ordinal()] = 10;
        }
        catch (NoSuchFieldError var9)
        {
            ;
        }

        try
        {
            $SwitchMap$ic2$core$util$AabbUtil$Edge[AabbUtil$Edge.FG.ordinal()] = 11;
        }
        catch (NoSuchFieldError var8)
        {
            ;
        }

        try
        {
            $SwitchMap$ic2$core$util$AabbUtil$Edge[AabbUtil$Edge.HG.ordinal()] = 12;
        }
        catch (NoSuchFieldError var7)
        {
            ;
        }

        $SwitchMap$ic2$api$Direction = new int[Direction.values().length];

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
