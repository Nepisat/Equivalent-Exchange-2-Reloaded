package ic2.core.block.crop;

import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import ic2.core.IC2;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CropMelon extends CropCard
{
    public String name()
    {
        return "Melon";
    }

    public String discoveredBy()
    {
        return "Chao";
    }

    public int tier()
    {
        return 2;
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
                return 2;

            case 4:
                return 0;

            default:
                return 0;
        }
    }

    public String[] attributes()
    {
        return new String[] {"Green", "Food", "Stem"};
    }

    public int maxSize()
    {
        return 4;
    }

    public boolean canGrow(ICropTile crop)
    {
        return crop.getSize() <= 3;
    }

    public int weightInfluences(ICropTile crop, float humidity, float nutrients, float air)
    {
        return (int)((double)humidity * 1.1D + (double)nutrients * 0.9D + (double)air);
    }

    public boolean canBeHarvested(ICropTile crop)
    {
        return crop.getSize() == 4;
    }

    public ItemStack getGain(ICropTile crop)
    {
        return IC2.random.nextInt(3) == 0 ? new ItemStack(Block.melon) : new ItemStack(Item.melon, IC2.random.nextInt(4) + 2);
    }

    public ItemStack getSeeds(ICropTile crop)
    {
        return crop.getGain() <= 1 && crop.getGrowth() <= 1 && crop.getResistance() <= 1 ? new ItemStack(Item.melonSeeds, IC2.random.nextInt(2) + 1) : super.getSeeds(crop);
    }

    public int growthDuration(ICropTile crop)
    {
        return crop.getSize() == 3 ? 700 : 250;
    }

    public byte getSizeAfterHarvest(ICropTile crop)
    {
        return (byte)3;
    }
}
