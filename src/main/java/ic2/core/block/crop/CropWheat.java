package ic2.core.block.crop;

import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CropWheat extends CropCard
{
    public String name()
    {
        return "Wheat";
    }

    public String discoveredBy()
    {
        return "Notch";
    }

    public int tier()
    {
        return 1;
    }

    public int stat(int n)
    {
        switch (n)
        {
            case 0:
                return 0;

            case 1:
                return 4;

            case 2:
                return 0;

            case 3:
                return 0;

            case 4:
                return 2;

            default:
                return 0;
        }
    }

    public String[] attributes()
    {
        return new String[] {"Yellow", "Food", "Wheat"};
    }

    public int maxSize()
    {
        return 7;
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
        return new ItemStack(Item.wheat, 1);
    }

    public ItemStack getSeeds(ICropTile crop)
    {
        return crop.getGain() <= 1 && crop.getGrowth() <= 1 && crop.getResistance() <= 1 ? new ItemStack(Item.seeds) : super.getSeeds(crop);
    }

    public byte getSizeAfterHarvest(ICropTile crop)
    {
        return (byte)2;
    }
}
