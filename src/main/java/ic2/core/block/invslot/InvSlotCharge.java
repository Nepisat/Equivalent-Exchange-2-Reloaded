package ic2.core.block.invslot;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot$Access;
import ic2.core.block.invslot.InvSlot$InvSide;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InvSlotCharge extends InvSlot
{
    public int tier;

    public InvSlotCharge(TileEntityInventory base, int oldStartIndex, int tier)
    {
        super(base, "charge", oldStartIndex, InvSlot$Access.IO, 1, InvSlot$InvSide.TOP);
        this.tier = tier;
    }

    public boolean accepts(ItemStack itemStack)
    {
        Item item = itemStack.getItem();
        return item instanceof IElectricItem ? ((IElectricItem)item).getTier(itemStack) <= this.tier : false;
    }

    public IElectricItem getItem()
    {
        ItemStack itemStack = this.get(0);
        return itemStack == null ? null : (IElectricItem)itemStack.getItem();
    }

    public int charge(int amount)
    {
        ItemStack itemStack = this.get(0);

        if (itemStack == null)
        {
            return 0;
        }
        else
        {
            Item item = itemStack.getItem();
            return item instanceof IElectricItem ? ElectricItem.manager.charge(itemStack, amount, this.tier, false, false) : 0;
        }
    }

    public void setTier(int tier)
    {
        this.tier = tier;
    }
}
