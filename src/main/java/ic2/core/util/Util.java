package ic2.core.util;

import ic2.core.init.InternalName;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class Util
{
    public static int roundToNegInf(float x)
    {
        int ret = (int)x;

        if ((float)ret > x)
        {
            --ret;
        }

        return ret;
    }

    public static int roundToNegInf(double x)
    {
        int ret = (int)x;

        if ((double)ret > x)
        {
            --ret;
        }

        return ret;
    }

    public static int countInArray(Object[] oa, Class<?> cls)
    {
        int ret = 0;
        Object[] arr$ = oa;
        int len$ = oa.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            Object o = arr$[i$];

            if (cls.isAssignableFrom(o.getClass()))
            {
                ++ret;
            }
        }

        return ret;
    }

    public static InternalName getColorName(int color)
    {
        switch (color)
        {
            case 0:
                return InternalName.black;

            case 1:
                return InternalName.red;

            case 2:
                return InternalName.green;

            case 3:
                return InternalName.brown;

            case 4:
                return InternalName.blue;

            case 5:
                return InternalName.purple;

            case 6:
                return InternalName.cyan;

            case 7:
                return InternalName.lightGray;

            case 8:
                return InternalName.gray;

            case 9:
                return InternalName.pink;

            case 10:
                return InternalName.lime;

            case 11:
                return InternalName.yellow;

            case 12:
                return InternalName.lightBlue;

            case 13:
                return InternalName.magenta;

            case 14:
                return InternalName.orange;

            case 15:
                return InternalName.white;

            default:
                return null;
        }
    }

    public static boolean inDev()
    {
        return System.getProperty("INDEV") != null;
    }

    public static boolean matchesOD(ItemStack stack, Object match)
    {
        if (!(match instanceof ItemStack))
        {
            if (match instanceof String)
            {
                if (stack == null)
                {
                    return false;
                }
                else
                {
                    int id = OreDictionary.getOreID(stack);

                    if (id == -1)
                    {
                        return false;
                    }
                    else
                    {
                        Iterator i$ = OreDictionary.getOres(Integer.valueOf(id)).iterator();
                        ItemStack ore;

                        do
                        {
                            if (!i$.hasNext())
                            {
                                return false;
                            }

                            ore = (ItemStack)i$.next();
                        }
                        while (!OreDictionary.itemMatches(stack, ore, false));

                        return true;
                    }
                }
            }
            else
            {
                return stack == match;
            }
        }
        else
        {
            return stack == null || stack.isItemEqual((ItemStack)match);
        }
    }
}
