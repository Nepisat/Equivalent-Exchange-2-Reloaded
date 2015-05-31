package ic2.core.block.personal;

import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerTradeOMatOpen extends ContainerFullInv
{
    public final TileEntityTradeOMat tileEntity;
    private int lastStock = -2;
    private int lastTotalTradeCount = -1;

    public ContainerTradeOMatOpen(EntityPlayer entityPlayer, TileEntityTradeOMat tileEntity)
    {
        super(entityPlayer, tileEntity, 166);
        this.tileEntity = tileEntity;
        this.addSlotToContainer(new SlotInvSlot(tileEntity.demandSlot, 0, 50, 19));
        this.addSlotToContainer(new SlotInvSlot(tileEntity.offerSlot, 0, 50, 53));
        this.addSlotToContainer(new SlotInvSlot(tileEntity.inputSlot, 0, 80, 19));
        this.addSlotToContainer(new SlotInvSlot(tileEntity.outputSlot, 0, 80, 53));
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

            if (this.tileEntity.totalTradeCount != this.lastTotalTradeCount)
            {
                icrafting.sendProgressBarUpdate(this, 2, this.tileEntity.totalTradeCount & 65535);
                icrafting.sendProgressBarUpdate(this, 3, this.tileEntity.totalTradeCount >>> 16);
            }
        }

        this.lastStock = this.tileEntity.stock;
        this.lastTotalTradeCount = this.tileEntity.totalTradeCount;
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
                break;

            case 2:
                this.tileEntity.totalTradeCount = this.tileEntity.totalTradeCount & -65536 | value;
                break;

            case 3:
                this.tileEntity.totalTradeCount = this.tileEntity.totalTradeCount & 65535 | value << 16;
        }
    }
}
