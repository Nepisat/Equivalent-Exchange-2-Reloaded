package ic2.core.block.crop;

import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import ic2.core.Ic2Items;
import ic2.core.block.TileEntityCrop;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class CropTerraWart extends CropCard
{
    public String name()
    {
        return "Terra Wart";
    }

    public int tier()
    {
        return 5;
    }

    public int stat(int n)
    {
        switch (n)
        {
            case 0:
                return 2;

            case 1:
                return 4;

            case 2:
                return 0;

            case 3:
                return 3;

            case 4:
                return 0;

            default:
                return 0;
        }
    }

    public String[] attributes()
    {
        return new String[] {"Blue", "Aether", "Consumable", "Snow"};
    }

    public int maxSize()
    {
        return 3;
    }

    public boolean canGrow(ICropTile crop)
    {
        return crop.getSize() < 3;
    }

    public boolean canBeHarvested(ICropTile crop)
    {
        return crop.getSize() == 3;
    }

    public float dropGainChance()
    {
        return 0.8F;
    }

    public ItemStack getGain(ICropTile crop)
    {
        return new ItemStack(Ic2Items.terraWart.getItem(), 1);
    }

    public void tick(ICropTile crop)
    {
        TileEntityCrop te = (TileEntityCrop)crop;

        if (te.isBlockBelow(Block.blockSnow))
        {
            if (this.canGrow(te))
            {
                te.growthPoints = (int)((double)te.growthPoints + (double)te.calcGrowthRate() * 0.5D);
            }
        }
        else if (te.isBlockBelow(Block.slowSand) && crop.getWorld().rand.nextInt(300) == 0)
        {
            te.id = (short)IC2Crops.cropNetherWart.getId();
        }
    }
}
