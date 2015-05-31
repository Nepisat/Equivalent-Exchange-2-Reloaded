package ic2.core.item;

import ic2.api.recipe.IMachineRecipeManager;
import ic2.core.BasicMachineRecipeManager;
import ic2.core.item.ItemScrapbox$Drop;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;

class ItemScrapbox$ScrapboxRecipeManager implements IMachineRecipeManager<Float>
{
    private final IMachineRecipeManager<ItemScrapbox$Drop> backend = new BasicMachineRecipeManager();

    public void addRecipe(ItemStack input, Float output)
    {
        this.backend.addRecipe(input, new ItemScrapbox$Drop(output.floatValue()));
    }

    public Float getOutputFor1(ItemStack input, boolean adjustInput)
    {
        ItemScrapbox$Drop drop = (ItemScrapbox$Drop)this.backend.getOutputFor(input, adjustInput);
        return drop == null ? null : Float.valueOf(drop.originalChance);
    }

    public Map<ItemStack, Float> getRecipes()
    {
        HashMap recipes = new HashMap();
        Iterator i$ = this.backend.getRecipes().entrySet().iterator();

        while (i$.hasNext())
        {
            Entry entry = (Entry)i$.next();
            recipes.put(entry.getKey(), Float.valueOf(((ItemScrapbox$Drop)entry.getValue()).originalChance / ItemScrapbox$Drop.topChance));
        }

        return Collections.unmodifiableMap(recipes);
    }

    Map<ItemStack, ItemScrapbox$Drop> getDrops()
    {
        return this.backend.getRecipes();
    }

    public Float getOutputFor(ItemStack x0, boolean x1)
    {
        return this.getOutputFor1(x0, x1);
    }

}
