package ic2.core.block.crop;

import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import ic2.core.Ic2Items;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class CropFerru extends CropCard
{
    public String name()
    {
        return "Ferru";
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
                return 2;

            case 1:
                return 0;

            case 2:
                return 0;

            case 3:
                return 1;

            case 4:
                return 0;

            default:
                return 0;
        }
    }

    public String[] attributes()
    {
        return new String[] {"Gray", "Leaves", "Metal"};
    }

    public int maxSize()
    {
        return 4;
    }

    public boolean canGrow(ICropTile crop)
    {
        return crop.getSize() < 3 ? true : crop.getSize() == 3 && crop.isBlockBelow(Block.oreIron);
    }

    public boolean canBeHarvested(ICropTile crop)
    {
        return crop.getSize() == 4;
    }

    public ItemStack getGain(ICropTile crop)
    {
        return new ItemStack(Ic2Items.smallIronDust.getItem());
    }

    public float dropGainChance()
    {
        return super.dropGainChance() / 2.0F;
    }

    public int growthDuration(ICropTile crop)
    {
        return crop.getSize() == 3 ? 2000 : 800;
    }

    public byte getSizeAfterHarvest(ICropTile crop)
    {
        return (byte)2;
    }
}
