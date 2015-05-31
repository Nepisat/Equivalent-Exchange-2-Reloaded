package ic2.core.block.crop;

import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import ic2.core.Ic2Items;
import net.minecraft.item.ItemStack;

public class CropCoffee extends CropCard
{
    public String name()
    {
        return "Coffee";
    }

    public String discoveredBy()
    {
        return "Snoochy";
    }

    public int tier()
    {
        return 7;
    }

    public int stat(int n)
    {
        switch (n)
        {
            case 0:
                return 1;

            case 1:
                return 4;

            case 2:
                return 1;

            case 3:
                return 2;

            case 4:
                return 0;

            default:
                return 0;
        }
    }

    public String[] attributes()
    {
        return new String[] {"Leaves", "Ingrident", "Beans"};
    }

    public int maxSize()
    {
        return 5;
    }

    public boolean canGrow(ICropTile crop)
    {
        return crop.getSize() < 5 && crop.getLightLevel() >= 9;
    }

    public int weightInfluences(ICropTile crop, float humidity, float nutrients, float air)
    {
        return (int)(0.4D * (double)humidity + 1.4D * (double)nutrients + 1.2D * (double)air);
    }

    public int growthDuration(ICropTile crop)
    {
        return crop.getSize() == 3 ? (int)((double)super.growthDuration(crop) * 0.5D) : (crop.getSize() == 4 ? (int)((double)super.growthDuration(crop) * 1.5D) : super.growthDuration(crop));
    }

    public boolean canBeHarvested(ICropTile crop)
    {
        return crop.getSize() >= 4;
    }

    public ItemStack getGain(ICropTile crop)
    {
        return crop.getSize() == 4 ? null : new ItemStack(Ic2Items.coffeeBeans.getItem());
    }

    public byte getSizeAfterHarvest(ICropTile crop)
    {
        return (byte)3;
    }
}
