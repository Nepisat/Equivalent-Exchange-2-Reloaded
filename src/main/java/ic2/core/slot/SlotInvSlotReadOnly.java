package ic2.core.slot;

import ic2.core.block.invslot.InvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class SlotInvSlotReadOnly extends SlotInvSlot
{
    public SlotInvSlotReadOnly(InvSlot invSlot, int index, int xDisplayPosition, int yDisplayPosition)
    {
        super(invSlot, index, xDisplayPosition, yDisplayPosition);
    }

    public boolean isItemValid(ItemStack stack)
    {
        return false;
    }

    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {}

    public boolean canTakeStack(EntityPlayer player)
    {
        return false;
    }

    public ItemStack decrStackSize(int par1)
    {
        return null;
    }
}
