package ic2.core.item.armor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.IC2;
import ic2.core.init.InternalName;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemArmorNightvisionGoggles extends ItemArmorUtility implements IElectricItem
{
    public ItemArmorNightvisionGoggles(Configuration config, InternalName internalName)
    {
        super(config, internalName, InternalName.nightvision, 0);
        this.setMaxDamage(27);
    }

    public boolean canProvideEnergy(ItemStack itemStack)
    {
        return false;
    }

    public int getChargedItemId(ItemStack itemStack)
    {
        return super.itemID;
    }

    public int getEmptyItemId(ItemStack itemStack)
    {
        return super.itemID;
    }

    public int getMaxCharge(ItemStack itemStack)
    {
        return 20000;
    }

    public int getTier(ItemStack itemStack)
    {
        return 1;
    }

    public int getTransferLimit(ItemStack itemStack)
    {
        return 200;
    }

    public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack)
    {
        if (!player.worldObj.isRemote)
        {
            boolean ret = false;

            if (ElectricItem.manager.use(itemStack, 1, player))
            {
                if (player.worldObj.isDaytime())
                {
                    int x = MathHelper.floor_double(player.posX);
                    int z = MathHelper.floor_double(player.posZ);
                    double fixedY = player.posY;

                    if (fixedY < 0.0D)
                    {
                        fixedY = 0.0D;
                    }
                    else if (fixedY > (double)player.worldObj.getHeight())
                    {
                        fixedY = (double)player.worldObj.getHeight();
                    }

                    int skylight = player.worldObj.getChunkFromBlockCoords(x, z).getSavedLightValue(EnumSkyBlock.Sky, x & 15, MathHelper.floor_double(fixedY), z & 15);

                    if (skylight > 12)
                    {
                        IC2.platform.removePotion(player, Potion.nightVision.id);
                        player.addPotionEffect(new PotionEffect(Potion.blindness.id, 100 + (skylight - 13) * 50, 0, true));
                    }
                }
                else
                {
                    IC2.platform.removePotion(player, Potion.blindness.id);
                    player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 250, 0, true));
                }

                ret = true;
            }

            if (ret)
            {
                player.inventoryContainer.detectAndSendChanges();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List itemList)
    {
        ItemStack itemStack = new ItemStack(this, 1);

        if (this.getChargedItemId(itemStack) == super.itemID)
        {
            ItemStack charged = new ItemStack(this, 1);
            ElectricItem.manager.charge(charged, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false);
            itemList.add(charged);
        }

        if (this.getEmptyItemId(itemStack) == super.itemID)
        {
            itemList.add(new ItemStack(this, 1, this.getMaxDamage()));
        }
    }

    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
        return false;
    }
}
