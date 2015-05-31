package ic2.core.block.crop;

import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class CropWeed extends CropCard
{
    public String name()
    {
        return "Weed";
    }

    public int tier()
    {
        return 0;
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
                return 5;

            default:
                return 0;
        }
    }

    public String[] attributes()
    {
        return new String[] {"Weed", "Bad"};
    }

    public int maxSize()
    {
        return 3;
    }

    public boolean canGrow(ICropTile crop)
    {
        return crop.getSize() < 3;
    }

    public boolean leftclick(ICropTile crop, EntityPlayer player)
    {
        crop.reset();
        return true;
    }

    public boolean canBeHarvested(ICropTile crop)
    {
        return false;
    }

    public ItemStack getGain(ICropTile crop)
    {
        return null;
    }

    public int growthDuration(ICropTile crop)
    {
        return 300;
    }

    public boolean onEntityCollision(ICropTile crop, Entity entity)
    {
        return false;
    }
}
