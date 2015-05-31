package ic2.core.block.crop;

import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CropStickReed extends CropCard
{
    public String name()
    {
        return "Stickreed";
    }

    public String discoveredBy()
    {
        return "raa1337";
    }

    public int tier()
    {
        return 4;
    }

    public int stat(int n)
    {
        switch (n)
        {
            case 0:
                return 2;

            case 1:
                return 0;

            case 2:
                return 1;

            case 3:
                return 0;

            case 4:
                return 1;

            default:
                return 0;
        }
    }

    public String[] attributes()
    {
        return new String[] {"Reed", "Resin"};
    }

    public int maxSize()
    {
        return 4;
    }

    public boolean canGrow(ICropTile crop)
    {
        return crop.getSize() < 4;
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
        return crop.getSize() <= 3 ? new ItemStack(Item.reed, crop.getSize() - 1) : new ItemStack(Ic2Items.resin.getItem());
    }

    public byte getSizeAfterHarvest(ICropTile crop)
    {
        return crop.getSize() == 4 ? (byte)(3 - IC2.random.nextInt(3)) : 1;
    }

    public boolean onEntityCollision(ICropTile crop, Entity entity)
    {
        return false;
    }

    public int growthDuration(ICropTile crop)
    {
        return crop.getSize() == 4 ? 400 : 100;
    }
}
