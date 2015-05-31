package ic2.core.block.crop;

import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class CropVenomilia extends CropCard
{
    public String name()
    {
        return "Venomilia";
    }

    public String discoveredBy()
    {
        return "raGan";
    }

    public int tier()
    {
        return 3;
    }

    public int stat(int n)
    {
        switch (n)
        {
            case 0:
                return 3;

            case 1:
                return 1;

            case 2:
                return 3;

            case 3:
                return 3;

            case 4:
                return 3;

            default:
                return 0;
        }
    }

    public String[] attributes()
    {
        return new String[] {"Purple", "Flower", "Tulip", "Poison"};
    }

    public int maxSize()
    {
        return 6;
    }

    public boolean canGrow(ICropTile crop)
    {
        return crop.getSize() <= 4 && crop.getLightLevel() >= 12 || crop.getSize() == 5;
    }

    public boolean canBeHarvested(ICropTile crop)
    {
        return crop.getSize() >= 4;
    }

    public ItemStack getGain(ICropTile crop)
    {
        return crop.getSize() == 5 ? new ItemStack(Ic2Items.grinPowder.getItem(), 1) : (crop.getSize() >= 4 ? new ItemStack(Item.dyePowder, 1, 5) : null);
    }

    public byte getSizeAfterHarvest(ICropTile crop)
    {
        return (byte)3;
    }

    public int growthDuration(ICropTile crop)
    {
        return crop.getSize() >= 3 ? 600 : 400;
    }

    public boolean rightclick(ICropTile crop, EntityPlayer player)
    {
        if (!player.isSneaking())
        {
            this.onEntityCollision(crop, player);
        }

        return crop.harvest(true);
    }

    public boolean leftclick(ICropTile crop, EntityPlayer player)
    {
        if (!player.isSneaking())
        {
            this.onEntityCollision(crop, player);
        }

        return crop.pick(true);
    }

    public boolean onEntityCollision(ICropTile crop, Entity entity)
    {
        if (crop.getSize() == 5 && entity instanceof EntityLivingBase)
        {
            if (entity instanceof EntityPlayer && ((EntityPlayer)entity).isSneaking() && IC2.random.nextInt(50) != 0)
            {
                return super.onEntityCollision(crop, entity);
            }

            ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.poison.id, (IC2.random.nextInt(10) + 5) * 20, 0));
            crop.setSize((byte)4);
            crop.updateState();
        }

        return super.onEntityCollision(crop, entity);
    }

    public boolean isWeed(ICropTile crop)
    {
        return crop.getSize() == 5 && crop.getGrowth() >= 8;
    }
}
