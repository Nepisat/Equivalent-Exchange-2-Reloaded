package ic2.api.recipe;

import net.minecraft.item.ItemStack;

public class Recipes
{
    public static IMachineRecipeManager<ItemStack> macerator;
    public static IMachineRecipeManager<ItemStack> extractor;
    public static IMachineRecipeManager<ItemStack> compressor;
    public static IMachineRecipeManager<Integer> matterAmplifier;
    public static IMachineRecipeManager<Float> scrapboxDrops;
    public static IListRecipeManager recyclerBlacklist;
    public static ICraftingRecipeManager advRecipes;
}
