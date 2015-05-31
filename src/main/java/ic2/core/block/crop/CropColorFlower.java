package ic2.core.block.crop;

import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CropColorFlower extends CropCard
{
    public String name;
    public String[] attributes;
    public int color;

    public CropColorFlower(String n, String[] a, int c)
    {
        this.name = n;
        this.attributes = a;
        this.color = c;
    }

    public String discoveredBy()
    {
        return !this.name.equals("Dandelion") && !this.name.equals("Rose") ? "Alblaka" : "Notch";
    }

    public String name()
    {
        return this.name;
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
                return 1;

            case 1:
                return 1;

            case 2:
                return 0;

            case 3:
                return 5;

            case 4:
                return 1;

            default:
                return 0;
        }
    }

    public String[] attributes()
    {
        return this.attributes;
    }

    public int maxSize()
    {
        return 4;
    }

    public boolean canGrow(ICropTile crop)
    {
        return crop.getSize() <= 3 && crop.getLightLevel() >= 12;
    }

    public boolean canBeHarvested(ICropTile crop)
    {
        return crop.getSize() == 4;
    }

    public ItemStack getGain(ICropTile crop)
    {
        return new ItemStack(Item.dyePowder, 1, this.color);
    }

    public byte getSizeAfterHarvest(ICropTile crop)
    {
        return (byte)3;
    }

    public int growthDuration(ICropTile crop)
    {
        return crop.getSize() == 3 ? 600 : 400;
    }
}
