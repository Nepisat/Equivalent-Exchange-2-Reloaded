package ic2.core.item;

import ic2.api.item.IElectricItemManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class InfiniteElectricItemManager implements IElectricItemManager
{
    public int charge(ItemStack itemStack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate)
    {
        return amount;
    }

    public int discharge(ItemStack itemStack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate)
    {
        return amount;
    }

    public int getCharge(ItemStack itemStack)
    {
        return Integer.MAX_VALUE;
    }

    public boolean canUse(ItemStack itemStack, int amount)
    {
        return true;
    }

    public boolean use(ItemStack itemStack, int amount, EntityLivingBase entity)
    {
        return true;
    }

    public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity) {}

    public String getToolTip(ItemStack itemStack)
    {
        return "infinite EU";
    }
}
