package ic2.core.block.crop;

import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CropCocoa extends CropCard
{
    public String name()
    {
        return "Cocoa";
    }

    public String discoveredBy()
    {
        return "Notch";
    }

    public int tier()
    {
        return 3;
    }

    public int stat(int n)
    {
        switch (n)
        {
            case 0:
                return 1;

            case 1:
                return 3;

            case 2:
                return 0;

            case 3:
                return 4;

            case 4:
                return 0;

            default:
                return 0;
        }
    }

    public String[] attributes()
    {
        return new String[] {"Brown", "Food", "Stem"};
    }

    public int maxSize()
    {
        return 4;
    }

    public boolean canGrow(ICropTile crop)
    {
        return crop.getSize() <= 3 && crop.getNutrients() >= 3;
    }

    public int weightInfluences(ICropTile crop, float humidity, float nutrients, float air)
    {
        return (int)((double)humidity * 0.8D + (double)nutrients * 1.3D + (double)air * 0.9D);
    }

    public boolean canBeHarvested(ICropTile crop)
    {
        return crop.getSize() == 4;
    }

    public ItemStack getGain(ICropTile crop)
    {
        return new ItemStack(Item.dyePowder, 1, 3);
    }

    public int growthDuration(ICropTile crop)
    {
        return crop.getSize() == 3 ? 900 : 400;
    }

    public byte getSizeAfterHarvest(ICropTile crop)
    {
        return (byte)3;
    }
}
