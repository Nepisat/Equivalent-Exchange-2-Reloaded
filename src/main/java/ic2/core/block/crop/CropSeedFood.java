package ic2.core.block.crop;

import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import net.minecraft.item.ItemStack;

public class CropSeedFood extends CropCard
{
    private final String name;
    private final String color;
    private final ItemStack gain;

    public CropSeedFood(String name, String color, ItemStack gain)
    {
        this.name = name;
        this.color = color;
        this.gain = gain;
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
        return new String[] {this.color, "Food", this.name};
    }

    public int maxSize()
    {
        return 3;
    }

    public boolean canGrow(ICropTile crop)
    {
        return crop.getSize() < 3 && crop.getLightLevel() >= 9;
    }

    public boolean canBeHarvested(ICropTile crop)
    {
        return crop.getSize() == 3;
    }

    public ItemStack getGain(ICropTile crop)
    {
        return this.gain.copy();
    }

    public byte getSizeAfterHarvest(ICropTile crop)
    {
        return (byte)1;
    }
}
