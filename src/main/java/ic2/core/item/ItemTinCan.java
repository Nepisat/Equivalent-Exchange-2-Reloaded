package ic2.core.item;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.init.DefaultIds;
import ic2.core.init.InternalName;
import java.util.List;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemTinCan extends ItemFood
{
    public ItemTinCan(Configuration config, InternalName internalName)
    {
        super(IC2.getItemIdFor(config, internalName, DefaultIds.get(internalName)), 2, 0.95F, false);
        this.setHasSubtypes(true);
        this.setUnlocalizedName(internalName.name());
        this.setCreativeTab(IC2.tabIC2);
        GameRegistry.registerItem(this, internalName.name());
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        super.itemIcon = iconRegister.registerIcon("ic2:" + this.getUnlocalizedName());
    }

    public String getUnlocalizedName()
    {
        return super.getUnlocalizedName().substring(5);
    }

    public String getUnlocalizedName(ItemStack itemStack)
    {
        return this.getUnlocalizedName();
    }

    public String getItemDisplayName(ItemStack itemStack)
    {
        return StatCollector.translateToLocal("ic2." + this.getUnlocalizedName(itemStack));
    }

    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        super.onEaten(itemstack, world, entityplayer);
        this.onEaten(entityplayer);
        return itemstack;
    }

    public void onEaten(EntityPlayer entityplayer)
    {
        entityplayer.heal(2.0F);
        ItemStack is = Ic2Items.tinCan.copy();

        if (!entityplayer.inventory.addItemStackToInventory(is))
        {
            entityplayer.dropPlayerItem(is);
        }
    }

    public void onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        switch (par1ItemStack.getItemDamage())
        {
            case 1:
                if (par3EntityPlayer.getRNG().nextFloat() < 0.8F)
                {
                    par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.hunger.id, 600 / (((ItemFood)Item.rottenFlesh).getHealAmount() / 2), 0));
                }

                break;

            case 2:
                par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.poison.id, 100 / (((ItemFood)Item.spiderEye).getHealAmount() / 2), 0));
                break;

            case 3:
                if (par3EntityPlayer.getRNG().nextFloat() < 0.3F)
                {
                    par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.hunger.id, 600 / (((ItemFood)Item.chickenRaw).getHealAmount() / 2), 0));
                }

                break;

            case 4:
                par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.regeneration.id, 100 / (((ItemFood)Item.appleGold).getHealAmount() / 2), 0));
                break;

            case 5:
                par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.regeneration.id, 600 / (((ItemFood)Item.appleGold).getHealAmount() / 2), 3));
                par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.resistance.id, 6000 / (((ItemFood)Item.appleGold).getHealAmount() / 2), 0));
                par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 6000 / (((ItemFood)Item.appleGold).getHealAmount() / 2), 0));
        }
    }

    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean debugTooltips)
    {
        int meta = stack.getItemDamage();

        if (meta == 1 || meta == 2 || meta == 3)
        {
            info.add("This looks bad...");
        }
    }

    public int getMaxItemUseDuration(ItemStack itemstack)
    {
        return 20;
    }
}
