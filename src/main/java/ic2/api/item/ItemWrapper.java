package ic2.api.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemWrapper
{
    private static final Multimap<Item, IBoxable> boxableItems = ArrayListMultimap.create();
    private static final Multimap<Item, IMetalArmor> metalArmorItems = ArrayListMultimap.create();

    public static void registerBoxable(Item item, IBoxable boxable)
    {
        boxableItems.put(item, boxable);
    }

    public static boolean canBeStoredInToolbox(ItemStack stack)
    {
        Item item = stack.getItem();
        Iterator i$ = boxableItems.get(item).iterator();
        IBoxable boxable;

        do
        {
            if (!i$.hasNext())
            {
                if (item instanceof IBoxable && ((IBoxable)item).canBeStoredInToolbox(stack))
                {
                    return true;
                }

                return false;
            }

            boxable = (IBoxable)i$.next();
        }
        while (!boxable.canBeStoredInToolbox(stack));

        return true;
    }

    public static void registerMetalArmor(Item item, IMetalArmor armor)
    {
        metalArmorItems.put(item, armor);
    }

    public static boolean isMetalArmor(ItemStack stack, EntityPlayer player)
    {
        Item item = stack.getItem();
        Iterator i$ = metalArmorItems.get(item).iterator();
        IMetalArmor metalArmor;

        do
        {
            if (!i$.hasNext())
            {
                if (item instanceof IMetalArmor && ((IMetalArmor)item).isMetalArmor(stack, player))
                {
                    return true;
                }

                return false;
            }

            metalArmor = (IMetalArmor)i$.next();
        }
        while (!metalArmor.isMetalArmor(stack, player));

        return true;
    }
}
