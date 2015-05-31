package ic2.core.item;

import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.core.Ic2Items;
import ic2.core.block.TileEntityCrop;
import ic2.core.init.InternalName;
import ic2.core.util.Util;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemCropSeed extends ItemIC2
{
    public ItemCropSeed(Configuration config, InternalName internalName)
    {
        super(config, internalName);
        this.setMaxStackSize(1);

        if (!Util.inDev())
        {
            this.setCreativeTab((CreativeTabs)null);
        }
    }

    public String getUnlocalizedName(ItemStack itemstack)
    {
        if (itemstack == null)
        {
            return "cropSeedUn";
        }
        else
        {
            byte level = getScannedFromStack(itemstack);
            return level == 0 ? "cropSeedUn" : (level < 0 ? "cropSeedInvalid" : "cropSeed" + getIdFromStack(itemstack));
        }
    }

    public boolean isDamageable()
    {
        return true;
    }

    public boolean isRepairable()
    {
        return false;
    }

    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean debugTooltips)
    {
        if (getScannedFromStack(stack) >= 4)
        {
            info.add("\u00a72Gr\u00a77 " + getGrowthFromStack(stack));
            info.add("\u00a76Ga\u00a77 " + getGainFromStack(stack));
            info.add("\u00a73Re\u00a77 " + getResistanceFromStack(stack));
        }
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float a, float b, float c)
    {
        if (world.getBlockTileEntity(x, y, z) instanceof TileEntityCrop)
        {
            TileEntityCrop crop = (TileEntityCrop)world.getBlockTileEntity(x, y, z);

            if (crop.tryPlantIn(getIdFromStack(itemstack), 1, getGrowthFromStack(itemstack), getGainFromStack(itemstack), getResistanceFromStack(itemstack), getScannedFromStack(itemstack)))
            {
                entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = null;
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public void getSubItems(int id, CreativeTabs tabs, List items)
    {
        CropCard[] crops = Crops.instance.getCropList();

        for (int i = 0; i < crops.length; ++i)
        {
            if (crops[i] != null)
            {
                items.add(generateItemStackFromValues((short)i, (byte)1, (byte)1, (byte)1, (byte)4));
            }
        }
    }

    public static ItemStack generateItemStackFromValues(short id, byte statGrowth, byte statGain, byte statResistance, byte scan)
    {
        ItemStack is = new ItemStack(Ic2Items.cropSeed.getItem());
        NBTTagCompound tag = new NBTTagCompound();
        tag.setShort("id", id);
        tag.setByte("growth", statGrowth);
        tag.setByte("gain", statGain);
        tag.setByte("resistance", statResistance);
        tag.setByte("scan", scan);
        is.setTagCompound(tag);
        return is;
    }

    public static short getIdFromStack(ItemStack is)
    {
        return is.getTagCompound() == null ? -1 : is.getTagCompound().getShort("id");
    }

    public static byte getGrowthFromStack(ItemStack is)
    {
        return is.getTagCompound() == null ? -1 : is.getTagCompound().getByte("growth");
    }

    public static byte getGainFromStack(ItemStack is)
    {
        return is.getTagCompound() == null ? -1 : is.getTagCompound().getByte("gain");
    }

    public static byte getResistanceFromStack(ItemStack is)
    {
        return is.getTagCompound() == null ? -1 : is.getTagCompound().getByte("resistance");
    }

    public static byte getScannedFromStack(ItemStack is)
    {
        return is.getTagCompound() == null ? -1 : is.getTagCompound().getByte("scan");
    }

    public static void incrementScannedOfStack(ItemStack is)
    {
        if (is.getTagCompound() != null)
        {
            is.getTagCompound().setByte("scan", (byte)(getScannedFromStack(is) + 1));
        }
    }
}
