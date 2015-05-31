package ic2.core.item;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.IC2;
import ic2.core.init.DefaultIds;
import ic2.core.init.InternalName;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemTerraWart extends ItemFood
{
    public ItemTerraWart(Configuration config, InternalName internalName)
    {
        super(IC2.getItemIdFor(config, internalName, DefaultIds.get(internalName)), 0, 1.0F, false);
        this.setAlwaysEdible();
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

    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer player)
    {
        --itemstack.stackSize;
        world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        IC2.platform.removePotion(player, Potion.confusion.id);
        IC2.platform.removePotion(player, Potion.digSlowdown.id);
        IC2.platform.removePotion(player, Potion.hunger.id);
        IC2.platform.removePotion(player, Potion.moveSlowdown.id);
        IC2.platform.removePotion(player, Potion.weakness.id);
        return itemstack;
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.rare;
    }
}
