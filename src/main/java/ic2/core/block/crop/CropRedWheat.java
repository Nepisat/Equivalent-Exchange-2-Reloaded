package ic2.core.block.crop;

import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;

public class CropRedWheat extends CropCard
{
    public String name()
    {
        return "Redwheat";
    }

    public String discoveredBy()
    {
        return "raa1337";
    }

    public int tier()
    {
        return 6;
    }

    public int stat(int n)
    {
        switch (n)
        {
            case 0:
                return 3;

            case 1:
                return 0;

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
        return new String[] {"Red", "Redstone", "Wheat"};
    }

    public int maxSize()
    {
        return 7;
    }

    public boolean canGrow(ICropTile crop)
    {
        return crop.getSize() < 7 && crop.getLightLevel() <= 10 && crop.getLightLevel() >= 5;
    }

    public boolean canBeHarvested(ICropTile crop)
    {
        return crop.getSize() == 7;
    }

    public float dropGainChance()
    {
        return 0.5F;
    }

    public ItemStack getGain(ICropTile crop)
    {
        ChunkCoordinates coords = crop.getLocation();
        return !crop.getWorld().isBlockIndirectlyGettingPowered(coords.posX, coords.posY, coords.posZ) && !crop.getWorld().rand.nextBoolean() ? new ItemStack(Item.wheat, 1) : new ItemStack(Item.redstone, 1);
    }

    public int emitRedstone(ICropTile crop)
    {
        return crop.getSize() == 7 ? 15 : 0;
    }

    public int getEmittedLight(ICropTile crop)
    {
        return crop.getSize() == 7 ? 7 : 0;
    }

    public int growthDuration(ICropTile crop)
    {
        return 600;
    }

    public byte getSizeAfterHarvest(ICropTile crop)
    {
        return (byte)2;
    }
}
