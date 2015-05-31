package ic2.core.item.armor;

import ic2.core.init.InternalName;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

public class ItemArmorBatpack extends ItemArmorElectric
{
    public ItemArmorBatpack(Configuration config, InternalName internalName)
    {
        super(config, internalName, InternalName.batpack, 1, 60000, 100, 1);
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
}
