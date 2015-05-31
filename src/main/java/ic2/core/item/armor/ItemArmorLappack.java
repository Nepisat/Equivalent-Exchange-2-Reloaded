package ic2.core.item.armor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.init.InternalName;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

public class ItemArmorLappack extends ItemArmorElectric
{
    public ItemArmorLappack(Configuration config, InternalName internalName)
    {
        super(config, internalName, InternalName.lappack, 1, 300000, 250, 2);
    }

    public boolean canProvideEnergy(ItemStack itemStack)
    {
        return true;
    }

    public double getDamageAbsorptionRatio()
    {
        return 0.0D;
    }

    public int getEnergyPerDamage()
    {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.uncommon;
    }
}
