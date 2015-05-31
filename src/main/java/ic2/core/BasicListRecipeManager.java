package ic2.core;

import ic2.api.recipe.IListRecipeManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.item.ItemStack;

public class BasicListRecipeManager implements IListRecipeManager
{
    private final List<ItemStack> list = new ArrayList();

    public Iterator<ItemStack> iterator()
    {
        return this.list.iterator();
    }

    public void add(ItemStack stack)
    {
        this.list.add(stack);
    }

    public boolean contains(ItemStack input)
    {
        if (input == null)
        {
            return false;
        }
        else
        {
            Iterator i$ = this.list.iterator();
            ItemStack stack;

            do
            {
                if (!i$.hasNext())
                {
                    return false;
                }

                stack = (ItemStack)i$.next();
            }
            while (input.itemID != stack.itemID || input.getItemDamage() != stack.getItemDamage() && stack.getItemDamage() != 32767 || input.stackSize < stack.stackSize);

            return true;
        }
    }

    public List<ItemStack> getStacks()
    {
        return this.list;
    }
}
