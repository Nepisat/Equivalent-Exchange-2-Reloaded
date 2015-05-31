package ic2.core.block.personal;

import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import ic2.core.slot.SlotInvSlotReadOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerTradeOMatClosed extends ContainerFullInv
{
    public final TileEntityTradeOMat tileEntity;
    private int lastStock = -2;

    public ContainerTradeOMatClosed(EntityPlayer entityPlayer, TileEntityTradeOMat tileEntity)
    {
        super(entityPlayer, tileEntity, 166);
        this.tileEntity = tileEntity;
        this.addSlotToContainer(new SlotInvSlotReadOnly(tileEntity.demandSlot, 0, 50, 19));
        this.addSlotToContainer(new SlotInvSlotReadOnly(tileEntity.offerSlot, 0, 50, 38));
        this.addSlotToContainer(new SlotInvSlot(tileEntity.inputSlot, 0, 143, 19));
        this.addSlotToContainer(new SlotInvSlot(tileEntity.outputSlot, 0, 143, 53));
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < super.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)super.crafters.get(i);

            if (this.tileEntity.stock != this.lastStock)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.stock & 65535);
                icrafting.sendProgressBarUpdate(this, 1, this.tileEntity.stock >>> 16);
            }
        }

        this.lastStock = this.tileEntity.stock;
    }

    public void updateProgressBar(int index, int value)
    {
        super.updateProgressBar(index, value);

        switch (index)
        {
            case 0:
                this.tileEntity.stock = this.tileEntity.stock & -65536 | value;
                break;

            case 1:
                this.tileEntity.stock = this.tileEntity.stock & 65535 | value << 16;
        }
    }
}
