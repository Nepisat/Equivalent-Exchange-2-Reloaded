package ic2.core.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.IBoxable;
import ic2.api.item.ItemWrapper;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.init.InternalName;
import ic2.core.util.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemToolbox extends ItemIC2 implements IBoxable
{
    public ItemToolbox(Configuration config, InternalName internalName)
    {
        super(config, internalName);
        this.setMaxStackSize(1);
    }

    public String getTextureName(int index)
    {
        switch (index)
        {
            case 0:
                return this.getUnlocalizedName();

            case 1:
                return this.getUnlocalizedName() + "." + InternalName.open.name();

            default:
                return null;
        }
    }

    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int meta)
    {
        return meta == 0 ? super.textures[0] : super.textures[1];
    }

    public String getUnlocalizedName(ItemStack itemstack)
    {
        if (itemstack == null)
        {
            return "DAMN TMI CAUSING NPE\'s!";
        }
        else if (itemstack.getItemDamage() == 0)
        {
            return "itemToolbox";
        }
        else
        {
            ItemStack[] inventory = getInventoryFromNBT(itemstack);
            return inventory[0] == null ? "itemToolbox" : inventory[0].getItem().getUnlocalizedName(inventory[0]);
        }
    }

    public static ItemStack[] getInventoryFromNBT(ItemStack is)
    {
        ItemStack[] re = new ItemStack[8];

        if (is.getTagCompound() == null)
        {
            return re;
        }
        else
        {
            NBTTagCompound tag = is.getTagCompound();

            for (int i = 0; i < 8; ++i)
            {
                NBTTagCompound item = tag.getCompoundTag("box" + i);

                if (item != null)
                {
                    re[i] = ItemStack.loadItemStackFromNBT(item);
                }
            }

            return re;
        }
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (!IC2.platform.isSimulating())
        {
            return itemstack;
        }
        else
        {
            if (itemstack.getItemDamage() == 0)
            {
                this.pack(itemstack, entityplayer);
            }
            else
            {
                this.unpack(itemstack, entityplayer);
            }

            if (!IC2.platform.isRendering())
            {
                entityplayer.openContainer.detectAndSendChanges();
            }

            return itemstack;
        }
    }

    public boolean canBeStoredInToolbox(ItemStack wayne)
    {
        return false;
    }

    public void pack(ItemStack toolbox, EntityPlayer player)
    {
        ItemStack[] hotbar = player.inventory.mainInventory;
        NBTTagCompound mainbox = new NBTTagCompound();
        int boxcount = 0;

        for (int i = 0; i < 9; ++i)
        {
            if (hotbar[i] != null && hotbar[i] != toolbox && ItemWrapper.canBeStoredInToolbox(hotbar[i]) && (hotbar[i].getMaxStackSize() <= 1 || hotbar[i].itemID == Ic2Items.scaffold.itemID || hotbar[i].itemID == Ic2Items.miningPipe.itemID))
            {
                NBTTagCompound myBox = new NBTTagCompound();
                hotbar[i].writeToNBT(myBox);
                hotbar[i] = null;
                mainbox.setCompoundTag("box" + boxcount, myBox);
                ++boxcount;
            }
        }

        if (boxcount != 0)
        {
            toolbox.setTagCompound(mainbox);
            toolbox.setItemDamage(1);
        }
    }

    public void unpack(ItemStack toolbox, EntityPlayer player)
    {
        NBTTagCompound box = toolbox.getTagCompound();

        if (box != null)
        {
            ItemStack[] inventory = getInventoryFromNBT(toolbox);
            ItemStack[] hotbar = player.inventory.mainInventory;
            int inv = 0;

            for (int i = 0; i < inventory.length && inventory[inv] != null; ++i)
            {
                if (hotbar[i] == null)
                {
                    hotbar[i] = inventory[inv];
                    ++inv;
                }
            }

            while (inv < 8 && inventory[inv] != null)
            {
                StackUtil.dropAsEntity(player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ, inventory[inv]);
                ++inv;
            }

            toolbox.setTagCompound((NBTTagCompound)null);
            toolbox.setItemDamage(0);
        }
    }
}
