package ic2.api.recipe;

import java.util.Map;
import net.minecraft.item.ItemStack;

public interface IMachineRecipeManager<V extends Object>
{
    void addRecipe(ItemStack var1, V var2);

    V getOutputFor(ItemStack var1, boolean var2);

    Map<ItemStack, V> getRecipes();
}
