package ic2.core.block.crop;

import ic2.api.crops.ICropTile;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CropPotato extends CropSeedFood
{
    public CropPotato()
    {
        super("Potato", "Yellow", new ItemStack(Item.potato));
    }

    public ItemStack getGain(ICropTile crop)
    {
        return crop.getWorld().rand.nextInt(50) == 0 ? new ItemStack(Item.poisonousPotato) : super.getGain(crop);
    }
}
