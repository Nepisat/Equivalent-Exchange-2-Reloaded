package ic2.core.item;

import ic2.api.item.ElectricItem;
import ic2.api.item.ICustomElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GatewayElectricItemManager implements IElectricItemManager
{
    public int charge(ItemStack itemStack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate)
    {
        Item item = itemStack.getItem();
        return !(item instanceof IElectricItem) ? 0 : (item instanceof ICustomElectricItem ? ((ICustomElectricItem)item).charge(itemStack, amount, tier, ignoreTransferLimit, simulate) : (item instanceof ISpecialElectricItem ? ((ISpecialElectricItem)item).getManager(itemStack).charge(itemStack, amount, tier, ignoreTransferLimit, simulate) : ElectricItem.rawManager.charge(itemStack, amount, tier, ignoreTransferLimit, simulate)));
    }

    public int discharge(ItemStack itemStack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate)
    {
        Item item = itemStack.getItem();
        return !(item instanceof IElectricItem) ? 0 : (item instanceof ICustomElectricItem ? ((ICustomElectricItem)item).discharge(itemStack, amount, tier, ignoreTransferLimit, simulate) : (item instanceof ISpecialElectricItem ? ((ISpecialElectricItem)item).getManager(itemStack).discharge(itemStack, amount, tier, ignoreTransferLimit, simulate) : ElectricItem.rawManager.discharge(itemStack, amount, tier, ignoreTransferLimit, simulate)));
    }

    public int getCharge(ItemStack itemStack)
    {
        Item item = itemStack.getItem();
        return !(item instanceof IElectricItem) ? 0 : (item instanceof ISpecialElectricItem ? ((ISpecialElectricItem)item).getManager(itemStack).getCharge(itemStack) : ElectricItem.rawManager.getCharge(itemStack));
    }

    public boolean canUse(ItemStack itemStack, int amount)
    {
        Item item = itemStack.getItem();
        return !(item instanceof IElectricItem) ? false : (item instanceof ICustomElectricItem ? ((ICustomElectricItem)item).canUse(itemStack, amount) : (item instanceof ISpecialElectricItem ? ((ISpecialElectricItem)item).getManager(itemStack).canUse(itemStack, amount) : ElectricItem.rawManager.canUse(itemStack, amount)));
    }

    public boolean use(ItemStack itemStack, int amount, EntityLivingBase entity)
    {
        Item item = itemStack.getItem();
        return !(item instanceof IElectricItem) ? false : (item instanceof ISpecialElectricItem ? ((ISpecialElectricItem)item).getManager(itemStack).use(itemStack, amount, entity) : ElectricItem.rawManager.use(itemStack, amount, entity));
    }

    public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity)
    {
        Item item = itemStack.getItem();

        if (entity != null && item instanceof IElectricItem)
        {
            if (item instanceof ISpecialElectricItem)
            {
                ((ISpecialElectricItem)item).getManager(itemStack).chargeFromArmor(itemStack, entity);
            }
            else
            {
                ElectricItem.rawManager.chargeFromArmor(itemStack, entity);
            }
        }
    }

    public String getToolTip(ItemStack itemStack)
    {
        Item item = itemStack.getItem();
        return !(item instanceof IElectricItem) ? null : (item instanceof ICustomElectricItem ? (((ICustomElectricItem)item).canShowChargeToolTip(itemStack) ? ElectricItem.rawManager.getToolTip(itemStack) : null) : (item instanceof ISpecialElectricItem ? ((ISpecialElectricItem)item).getManager(itemStack).getToolTip(itemStack) : ElectricItem.rawManager.getToolTip(itemStack)));
    }
}
