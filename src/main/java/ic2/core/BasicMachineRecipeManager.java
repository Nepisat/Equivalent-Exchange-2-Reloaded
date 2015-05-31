package ic2.core;

import ic2.api.recipe.IMachineRecipeManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.item.ItemStack;

public class BasicMachineRecipeManager<V extends Object> implements IMachineRecipeManager<V>
{
    private final Map<ItemStack, V> recipes = new HashMap();

    public void addRecipe(ItemStack input, V output)
    {
        this.recipes.put(input, output);
    }

    public V getOutputFor(ItemStack input, boolean adjustInput)
    {
        if (input == null)
        {
            return null;
        }
        else
        {
            Iterator i$ = this.recipes.entrySet().iterator();
            Entry entry;
            ItemStack recipeInput;

            do
            {
                if (!i$.hasNext())
                {
                    return null;
                }

                entry = (Entry)i$.next();
                recipeInput = (ItemStack)entry.getKey();
            }
            while (input.itemID != recipeInput.itemID || input.getItemDamage() != recipeInput.getItemDamage() && recipeInput.getItemDamage() != 32767 || input.stackSize < recipeInput.stackSize);

            if (adjustInput)
            {
                input.stackSize -= recipeInput.stackSize;
            }

            return (V) entry.getValue();
        }
    }

    public Map<ItemStack, V> getRecipes()
    {
        return this.recipes;
    }
}
