package ic2.core.block.crop;

import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CropAurelia extends CropCard
{
    public String name()
    {
        return "Aurelia";
    }

    public int tier()
    {
        return 8;
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
        return new String[] {"Gold", "Leaves", "Metal"};
    }

    public int maxSize()
    {
        return 5;
    }

    public boolean canGrow(ICropTile crop)
    {
        return crop.getSize() < 4 ? true : crop.getSize() == 4 && crop.isBlockBelow(Block.oreGold);
    }

    public boolean canBeHarvested(ICropTile crop)
    {
        return crop.getSize() == 5;
    }

    public ItemStack getGain(ICropTile crop)
    {
        return new ItemStack(Item.goldNugget);
    }

    public int growthDuration(ICropTile crop)
    {
        return crop.getSize() == 4 ? 2200 : 750;
    }

    public byte getSizeAfterHarvest(ICropTile crop)
    {
        return (byte)2;
    }
}
