package ic2.core.block.invslot;

import ic2.api.recipe.IMachineRecipeManager;
import ic2.core.block.TileEntityInventory;
import net.minecraft.item.ItemStack;

public class InvSlotProcessableGeneric extends InvSlotProcessable
{
    public final IMachineRecipeManager recipeManager;

    public InvSlotProcessableGeneric(TileEntityInventory base, String name, int oldStartIndex, int count, IMachineRecipeManager recipeManager)
    {
        super(base, name, oldStartIndex, count);
        this.recipeManager = recipeManager;
    }

    public boolean accepts(ItemStack itemStack)
    {
        ItemStack tmp = itemStack.copy();
        tmp.stackSize = Integer.MAX_VALUE;
        return this.recipeManager.getOutputFor(tmp, false) != null;
    }

    public ItemStack process(boolean simulate)
    {
        Object ret = this.processRaw(simulate);
        return ret instanceof ItemStack ? (ItemStack)ret : null;
    }

    public Object processRaw(boolean simulate)
    {
        ItemStack input = this.get();

        if (input == null)
        {
            return null;
        }
        else
        {
            Object output = this.recipeManager.getOutputFor(input, !simulate);

            if (output instanceof ItemStack)
            {
                output = ((ItemStack)output).copy();
            }

            if (input.stackSize <= 0)
            {
                this.put((ItemStack)null);
            }

            return output;
        }
    }
}
