package ic2.core;

import ic2.api.recipe.ICraftingRecipeManager;
import net.minecraft.item.ItemStack;

public class AdvCraftingRecipeManager implements ICraftingRecipeManager
{
    public void addRecipe(ItemStack output, Object ... input)
    {
        AdvRecipe.addAndRegister(output, input);
    }

    public void addShapelessRecipe(ItemStack output, Object ... input)
    {
        AdvShapelessRecipe.addAndRegister(output, input);
    }
}
