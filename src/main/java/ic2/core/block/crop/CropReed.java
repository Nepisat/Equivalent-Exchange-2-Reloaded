package ic2.core.block.crop;

import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CropReed extends CropCard
{
    public String name()
    {
        return "Reed";
    }

    public String discoveredBy()
    {
        return "Notch";
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
                return 0;

            case 2:
                return 1;

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
        return new String[] {"Reed"};
    }

    public int maxSize()
    {
        return 3;
    }

    public boolean canGrow(ICropTile crop)
    {
        return crop.getSize() < 3;
    }

    public int weightInfluences(ICropTile crop, float humidity, float nutrients, float air)
    {
        return (int)((double)humidity * 1.2D + (double)nutrients + (double)air * 0.8D);
    }

    public boolean canBeHarvested(ICropTile crop)
    {
        return crop.getSize() > 1;
    }

    public ItemStack getGain(ICropTile crop)
    {
        return new ItemStack(Item.reed, crop.getSize() - 1);
    }

    public boolean onEntityCollision(ICropTile crop, Entity entity)
    {
        return false;
    }

    public int growthDuration(ICropTile crop)
    {
        return 200;
    }
}
