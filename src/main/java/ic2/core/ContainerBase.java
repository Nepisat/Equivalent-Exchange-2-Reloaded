package ic2.core;

import ic2.core.slot.SlotInvSlot;
import java.util.Iterator;
import java.util.ListIterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerBase extends Container
{
    public final IInventory base;

    public ContainerBase(IInventory base)
    {
        this.base = base;
    }

    public final ItemStack transferStackInSlot(EntityPlayer player, int sourceSlotIndex)
    {
        Slot sourceSlot = (Slot)super.inventorySlots.get(sourceSlotIndex);

        if (sourceSlot != null && sourceSlot.getHasStack())
        {
            ItemStack sourceItemStack = sourceSlot.getStack();
            int oldSourceItemStackSize = sourceItemStack.stackSize;
            int run;
            Slot targetSlot;

            if (sourceSlot.inventory == player.inventory)
            {
                for (run = 0; run < 4 && sourceItemStack.stackSize > 0; ++run)
                {
                    Iterator it;

                    if (run < 2)
                    {
                        it = super.inventorySlots.iterator();

                        while (it.hasNext())
                        {
                            targetSlot = (Slot)it.next();

                            if (targetSlot instanceof SlotInvSlot && ((SlotInvSlot)targetSlot).invSlot.canInput() && targetSlot.isItemValid(sourceItemStack) && (targetSlot.getStack() != null || run == 1))
                            {
                                this.mergeItemStack(sourceItemStack, targetSlot.slotNumber, targetSlot.slotNumber + 1, false);

                                if (sourceItemStack.stackSize == 0)
                                {
                                    break;
                                }
                            }
                        }
                    }
                    else
                    {
                        it = super.inventorySlots.iterator();

                        while (it.hasNext())
                        {
                            targetSlot = (Slot)it.next();

                            if (targetSlot.inventory != player.inventory && targetSlot.isItemValid(sourceItemStack) && (targetSlot.getStack() != null || run == 3))
                            {
                                this.mergeItemStack(sourceItemStack, targetSlot.slotNumber, targetSlot.slotNumber + 1, false);

                                if (sourceItemStack.stackSize == 0)
                                {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                for (run = 0; run < 2 && sourceItemStack.stackSize > 0; ++run)
                {
                    ListIterator var9 = super.inventorySlots.listIterator(super.inventorySlots.size());

                    while (var9.hasPrevious())
                    {
                        targetSlot = (Slot)var9.previous();

                        if (targetSlot.inventory == player.inventory && targetSlot.isItemValid(sourceItemStack) && (targetSlot.getStack() != null || run == 1))
                        {
                            this.mergeItemStack(sourceItemStack, targetSlot.slotNumber, targetSlot.slotNumber + 1, false);

                            if (sourceItemStack.stackSize == 0)
                            {
                                break;
                            }
                        }
                    }
                }
            }

            if (sourceItemStack.stackSize != oldSourceItemStackSize)
            {
                if (sourceItemStack.stackSize == 0)
                {
                    sourceSlot.putStack((ItemStack)null);
                }
                else
                {
                    sourceSlot.onPickupFromSlot(player, sourceItemStack);
                }

                if (IC2.platform.isSimulating())
                {
                    this.detectAndSendChanges();
                }
            }
        }

        return null;
    }

    public void updateProgressBar(int index, int value) {}

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return this.base.isUseableByPlayer(entityplayer);
    }
}
