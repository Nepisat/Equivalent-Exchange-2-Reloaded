package ic2.core.block.crop;

import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import ic2.core.Ic2Items;
import net.minecraft.item.ItemStack;

public class CropHops extends CropCard
{
    public String name()
    {
        return "Hops";
    }

    public int tier()
    {
        return 5;
    }

    public int stat(int n)
    {
        switch (n)
        {
            case 0:
                return 2;

            case 1:
                return 2;

            case 2:
                return 0;

            case 3:
                return 1;

            case 4:
                return 1;

            default:
                return 0;
        }
    }

    public String[] attributes()
    {
        return new String[] {"Green", "Ingredient", "Wheat"};
    }

    public int maxSize()
    {
        return 7;
    }

    public int growthDuration(ICropTile crop)
    {
        return 600;
    }

    public boolean canGrow(ICropTile crop)
    {
        return crop.getSize() < 7 && crop.getLightLevel() >= 9;
    }

    public boolean canBeHarvested(ICropTile crop)
    {
        return crop.getSize() == 7;
    }

    public ItemStack getGain(ICropTile crop)
    {
        return new ItemStack(Ic2Items.hops.getItem(), 1);
    }

    public byte getSizeAfterHarvest(ICropTile crop)
    {
        return (byte)3;
    }
}
