package ic2.core.item.armor;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.init.InternalName;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;

public abstract class ItemArmorElectric extends ItemArmorIC2 implements ISpecialArmor, IElectricItem
{
    public int maxCharge;
    public int transferLimit;
    public int tier;

    public ItemArmorElectric(Configuration config, InternalName internalName, InternalName armorName, int armorType, int maxCharge, int transferLimit, int tier)
    {
        super(config, internalName, EnumArmorMaterial.DIAMOND, armorName, armorType, (Object)null);
        this.maxCharge = maxCharge;
        this.tier = tier;
        this.transferLimit = transferLimit;
        this.setMaxDamage(27);
        this.setMaxStackSize(1);
    }

    public int getItemEnchantability()
    {
        return 0;
    }

    public void getSubItems(int i, CreativeTabs tabs, List itemList)
    {
        ItemStack charged = new ItemStack(this, 1);
        ElectricItem.manager.charge(charged, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false);
        itemList.add(charged);
        itemList.add(new ItemStack(this, 1, this.getMaxDamage()));
    }

    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot)
    {
        if (source.isUnblockable())
        {
            return new ArmorProperties(0, 0.0D, 0);
        }
        else
        {
            double absorptionRatio = this.getBaseAbsorptionRatio() * this.getDamageAbsorptionRatio();
            int energyPerDamage = this.getEnergyPerDamage();
            int damageLimit = energyPerDamage > 0 ? 25 * ElectricItem.manager.getCharge(armor) / energyPerDamage : 0;
            return new ArmorProperties(0, absorptionRatio, damageLimit);
        }
    }

    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot)
    {
        return ElectricItem.manager.getCharge(armor) >= this.getEnergyPerDamage() ? (int)Math.round(20.0D * this.getBaseAbsorptionRatio() * this.getDamageAbsorptionRatio()) : 0;
    }

    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot)
    {
        ElectricItem.manager.discharge(stack, damage * this.getEnergyPerDamage(), Integer.MAX_VALUE, true, false);
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
        return this.maxCharge;
    }

    public int getTier(ItemStack itemStack)
    {
        return this.tier;
    }

    public int getTransferLimit(ItemStack itemStack)
    {
        return this.transferLimit;
    }

    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
        return false;
    }

    public abstract double getDamageAbsorptionRatio();

    public abstract int getEnergyPerDamage();

    private double getBaseAbsorptionRatio()
    {
        switch (super.armorType)
        {
            case 0:
                return 0.15D;

            case 1:
                return 0.4D;

            case 2:
                return 0.3D;

            case 3:
                return 0.15D;

            default:
                return 0.0D;
        }
    }
}
