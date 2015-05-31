package ic2.core.item;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.core.IC2;
import ic2.core.util.StackUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ElectricItemManager implements IElectricItemManager
{
    public int charge(ItemStack itemStack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate)
    {
        IElectricItem item = (IElectricItem)itemStack.getItem();

        if (amount >= 0 && itemStack.stackSize <= 1 && item.getTier(itemStack) <= tier)
        {
            if (amount > item.getTransferLimit(itemStack) && !ignoreTransferLimit)
            {
                amount = item.getTransferLimit(itemStack);
            }

            NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);
            int charge = nbtData.getInteger("charge");

            if (amount > item.getMaxCharge(itemStack) - charge)
            {
                amount = item.getMaxCharge(itemStack) - charge;
            }

            charge += amount;

            if (!simulate)
            {
                nbtData.setInteger("charge", charge);
                itemStack.itemID = charge > 0 ? item.getChargedItemId(itemStack) : item.getEmptyItemId(itemStack);

                if (itemStack.getItem() instanceof IElectricItem)
                {
                    item = (IElectricItem)itemStack.getItem();

                    if (itemStack.getMaxDamage() > 2)
                    {
                        itemStack.setItemDamage(1 + (item.getMaxCharge(itemStack) - charge) * (itemStack.getMaxDamage() - 2) / item.getMaxCharge(itemStack));
                    }
                    else
                    {
                        itemStack.setItemDamage(0);
                    }
                }
                else
                {
                    itemStack.setItemDamage(0);
                }
            }

            return amount;
        }
        else
        {
            return 0;
        }
    }

    public int discharge(ItemStack itemStack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate)
    {
        IElectricItem item = (IElectricItem)itemStack.getItem();

        if (amount >= 0 && itemStack.stackSize <= 1 && item.getTier(itemStack) <= tier)
        {
            if (amount > item.getTransferLimit(itemStack) && !ignoreTransferLimit)
            {
                amount = item.getTransferLimit(itemStack);
            }

            NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);
            int charge = nbtData.getInteger("charge");

            if (amount > charge)
            {
                amount = charge;
            }

            if (!simulate)
            {
                charge -= amount;
                nbtData.setInteger("charge", charge);
                itemStack.itemID = charge > 0 ? item.getChargedItemId(itemStack) : item.getEmptyItemId(itemStack);

                if (itemStack.getItem() instanceof IElectricItem)
                {
                    item = (IElectricItem)itemStack.getItem();

                    if (itemStack.getMaxDamage() > 2)
                    {
                        itemStack.setItemDamage(1 + (item.getMaxCharge(itemStack) - charge) * (itemStack.getMaxDamage() - 2) / item.getMaxCharge(itemStack));
                    }
                    else
                    {
                        itemStack.setItemDamage(0);
                    }
                }
                else
                {
                    itemStack.setItemDamage(0);
                }
            }

            return amount;
        }
        else
        {
            return 0;
        }
    }

    public int getCharge(ItemStack itemStack)
    {
        return ElectricItem.manager.discharge(itemStack, Integer.MAX_VALUE, Integer.MAX_VALUE, true, true);
    }

    public boolean canUse(ItemStack itemStack, int amount)
    {
        return ElectricItem.manager.getCharge(itemStack) >= amount;
    }

    public boolean use(ItemStack itemStack, int amount, EntityLivingBase entity)
    {
        if (!IC2.platform.isSimulating())
        {
            return false;
        }
        else
        {
            ElectricItem.manager.chargeFromArmor(itemStack, entity);
            int transfer = ElectricItem.manager.discharge(itemStack, amount, Integer.MAX_VALUE, true, true);

            if (transfer == amount)
            {
                ElectricItem.manager.discharge(itemStack, amount, Integer.MAX_VALUE, true, false);
                ElectricItem.manager.chargeFromArmor(itemStack, entity);
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity)
    {
        if (IC2.platform.isSimulating())
        {
            boolean inventoryChanged = false;

            for (int i = 0; i < 4; ++i)
            {
                ItemStack armorItemStack = entity.getCurrentItemOrArmor(i + 1);

                if (armorItemStack != null && armorItemStack.getItem() instanceof IElectricItem)
                {
                    IElectricItem armorItem = (IElectricItem)armorItemStack.getItem();

                    if (armorItem.canProvideEnergy(armorItemStack) && armorItem.getTier(armorItemStack) >= ((IElectricItem)itemStack.getItem()).getTier(itemStack))
                    {
                        int transfer = ElectricItem.manager.charge(itemStack, Integer.MAX_VALUE, Integer.MAX_VALUE, true, true);
                        transfer = ElectricItem.manager.discharge(armorItemStack, transfer, Integer.MAX_VALUE, true, false);

                        if (transfer > 0)
                        {
                            ElectricItem.manager.charge(itemStack, transfer, Integer.MAX_VALUE, true, false);
                            inventoryChanged = true;
                        }
                    }
                }
            }

            if (inventoryChanged && entity instanceof EntityPlayer)
            {
                ((EntityPlayer)entity).openContainer.detectAndSendChanges();
            }
        }
    }

    public String getToolTip(ItemStack itemStack)
    {
        if (!(itemStack.getItem() instanceof IElectricItem))
        {
            return null;
        }
        else
        {
            IElectricItem item = (IElectricItem)itemStack.getItem();
            return ElectricItem.manager.getCharge(itemStack) + "/" + item.getMaxCharge(itemStack) + " EU";
        }
    }
}
