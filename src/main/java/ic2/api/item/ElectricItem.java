package ic2.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public final class ElectricItem
{
    public static IElectricItemManager manager;
    public static IElectricItemManager rawManager;

    @Deprecated
    public static int charge(ItemStack itemStack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate)
    {
        return manager.charge(itemStack, amount, tier, ignoreTransferLimit, simulate);
    }

    @Deprecated
    public static int discharge(ItemStack itemStack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate)
    {
        return manager.discharge(itemStack, amount, tier, ignoreTransferLimit, simulate);
    }

    @Deprecated
    public static boolean canUse(ItemStack itemStack, int amount)
    {
        return manager.canUse(itemStack, amount);
    }

    @Deprecated
    public static boolean use(ItemStack itemStack, int amount, EntityPlayer player)
    {
        return manager.use(itemStack, amount, player);
    }

    @Deprecated
    public static void chargeFromArmor(ItemStack itemStack, EntityPlayer player)
    {
        manager.chargeFromArmor(itemStack, player);
    }
}
