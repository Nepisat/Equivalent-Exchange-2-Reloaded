package ic2.core.item.armor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.init.InternalName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingFallEvent;

public class ItemArmorNanoSuit extends ItemArmorElectric
{
    public ItemArmorNanoSuit(Configuration config, InternalName internalName, int armorType)
    {
        super(config, internalName, InternalName.nano, armorType, 100000, 160, 2);

        if (armorType == 3)
        {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot)
    {
        if (source == DamageSource.fall && super.armorType == 3)
        {
            int energyPerDamage = this.getEnergyPerDamage();
            int damageLimit = energyPerDamage > 0 ? 25 * ElectricItem.manager.getCharge(armor) / energyPerDamage : 0;
            return new ArmorProperties(10, damage < 8.0D ? 1.0D : 0.875D, damageLimit);
        }
        else
        {
            return super.getProperties(player, armor, source, damage, slot);
        }
    }

    @ForgeSubscribe
    public void onEntityLivingFallEvent(LivingFallEvent event)
    {
        if (IC2.platform.isSimulating() && event.entity instanceof EntityLivingBase)
        {
            EntityLivingBase entity = (EntityLivingBase)event.entity;
            ItemStack armor = entity.getCurrentItemOrArmor(1);

            if (armor != null && armor.getItem() == this)
            {
                int fallDamage = (int)event.distance - 3;

                if (fallDamage >= 8)
                {
                    return;
                }

                int energyCost = this.getEnergyPerDamage() * fallDamage;

                if (energyCost <= ElectricItem.manager.getCharge(armor))
                {
                    ElectricItem.manager.discharge(armor, energyCost, Integer.MAX_VALUE, true, false);
                    event.setCanceled(true);
                }
            }
        }
    }

    public double getDamageAbsorptionRatio()
    {
        return 0.9D;
    }

    public int getEnergyPerDamage()
    {
        return 800;
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.uncommon;
    }
}
