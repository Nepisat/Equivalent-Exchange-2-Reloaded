package ic2.core.item.armor;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.IMetalArmor;
import ic2.core.IC2;
import ic2.core.init.DefaultIds;
import ic2.core.init.InternalName;
import ic2.core.util.Util;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.Configuration;

public class ItemArmorIC2 extends ItemArmor implements IMetalArmor
{
    private final Object repairMaterial;
    private final String armorName;

    public ItemArmorIC2(Configuration config, InternalName internalName, EnumArmorMaterial enumArmorMaterial, InternalName armorName, int armorType, Object repairMaterial)
    {
        super(IC2.getItemIdFor(config, internalName, DefaultIds.get(internalName)), enumArmorMaterial, IC2.platform.addArmor(armorName.name()), armorType);
        this.repairMaterial = repairMaterial;
        this.armorName = armorName.name();
        this.setMaxDamage(enumArmorMaterial.getDurability(armorType));
        this.setUnlocalizedName(internalName.name());
        this.setCreativeTab(IC2.tabIC2);
        GameRegistry.registerItem(this, internalName.name());
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        super.itemIcon = iconRegister.registerIcon("ic2:" + this.getUnlocalizedName());
    }

    public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer)
    {
        int suffix = super.armorType == 2 ? 2 : 1;
        return "ic2:textures/armor/" + this.armorName + "_" + suffix + ".png";
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

    public boolean isMetalArmor(ItemStack itemstack, EntityPlayer player)
    {
        return true;
    }

    public boolean getIsRepairable(ItemStack stack1, ItemStack stack2)
    {
        return stack2 != null && Util.matchesOD(stack2, this.repairMaterial);
    }
}
