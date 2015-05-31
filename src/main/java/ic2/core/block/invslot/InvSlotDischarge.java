package ic2.core.block.invslot;

import ic2.api.info.Info;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.Ic2Items;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot$Access;
import ic2.core.block.invslot.InvSlot$InvSide;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InvSlotDischarge extends InvSlot
{
    public int tier;

    public InvSlotDischarge(TileEntityInventory base, int oldStartIndex, int tier)
    {
        this(base, oldStartIndex, tier, InvSlot$InvSide.ANY);
    }

    public InvSlotDischarge(TileEntityInventory base, int oldStartIndex, int tier, InvSlot$InvSide preferredSide)
    {
        super(base, "discharge", oldStartIndex, InvSlot$Access.IO, 1, preferredSide);
        this.tier = tier;
    }

    public boolean accepts(ItemStack itemStack)
    {
        Item item = itemStack.getItem();
        return item instanceof IElectricItem ? ((IElectricItem)item).canProvideEnergy(itemStack) && ((IElectricItem)item).getTier(itemStack) <= this.tier : (item.itemID != Ic2Items.suBattery.itemID && item.itemID != Item.redstone.itemID ? Info.itemEnergy.getEnergyValue(itemStack) > 0 && (!(item instanceof IElectricItem) || ((IElectricItem)item).getTier(itemStack) <= this.tier) : true);
    }

    public IElectricItem getItem()
    {
        ItemStack itemStack = this.get(0);
        return itemStack == null ? null : (IElectricItem)itemStack.getItem();
    }

    public int discharge(int amount, boolean ignoreLimit)
    {
        ItemStack itemStack = this.get(0);

        if (itemStack == null)
        {
            return 0;
        }
        else
        {
            int energyValue = Info.itemEnergy.getEnergyValue(itemStack);

            if (energyValue == 0)
            {
                return 0;
            }
            else
            {
                Item item = itemStack.getItem();

                if (!(item instanceof IElectricItem))
                {
                    --itemStack.stackSize;

                    if (itemStack.stackSize <= 0)
                    {
                        this.put(0, (ItemStack)null);
                    }

                    return energyValue;
                }
                else
                {
                    IElectricItem elItem = (IElectricItem)item;
                    return elItem.canProvideEnergy(itemStack) && elItem.getTier(itemStack) <= this.tier ? ElectricItem.manager.discharge(itemStack, amount, this.tier, ignoreLimit, false) : 0;
                }
            }
        }
    }

    public void setTier(int tier)
    {
        this.tier = tier;
    }
}
