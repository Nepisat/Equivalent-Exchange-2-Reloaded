package ic2.api.recipe;

import java.util.List;
import net.minecraft.item.ItemStack;

public interface IListRecipeManager extends Iterable<ItemStack>
{
    void add(ItemStack var1);

    boolean contains(ItemStack var1);

    List<ItemStack> getStacks();
}
