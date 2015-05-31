package ic2.core.block.personal;

import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerEnergyOMatOpen extends ContainerFullInv
{
    public final TileEntityEnergyOMat tileEntity;
    private int lastPaidFor = -1;
    private int lastEuBuffer = -1;
    private int lastEuOffer = -1;
    private int lastTier = -1;

    public ContainerEnergyOMatOpen(EntityPlayer entityPlayer, TileEntityEnergyOMat tileEntity)
    {
        super(entityPlayer, tileEntity, 166);
        this.tileEntity = tileEntity;
        this.addSlotToContainer(new SlotInvSlot(tileEntity.demandSlot, 0, 24, 17));
        this.addSlotToContainer(new SlotInvSlot(tileEntity.upgradeSlot, 0, 24, 53));
        this.addSlotToContainer(new SlotInvSlot(tileEntity.inputSlot, 0, 60, 17));
        this.addSlotToContainer(new SlotInvSlot(tileEntity.chargeSlot, 0, 60, 53));
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < super.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)super.crafters.get(i);

            if (this.tileEntity.paidFor != this.lastPaidFor)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.paidFor & 65535);
                icrafting.sendProgressBarUpdate(this, 1, this.tileEntity.paidFor >>> 16);
            }

            if (this.tileEntity.euBuffer != this.lastEuBuffer)
            {
                icrafting.sendProgressBarUpdate(this, 2, this.tileEntity.euBuffer & 65535);
                icrafting.sendProgressBarUpdate(this, 3, this.tileEntity.euBuffer >>> 16);
            }

            if (this.tileEntity.euOffer != this.lastEuOffer)
            {
                icrafting.sendProgressBarUpdate(this, 4, this.tileEntity.euOffer & 65535);
                icrafting.sendProgressBarUpdate(this, 5, this.tileEntity.euOffer >>> 16);
            }

            if (this.tileEntity.chargeSlot.tier != this.lastTier)
            {
                icrafting.sendProgressBarUpdate(this, 6, this.tileEntity.chargeSlot.tier);
            }
        }

        this.lastPaidFor = this.tileEntity.paidFor;
        this.lastEuBuffer = this.tileEntity.euBuffer;
        this.lastEuOffer = this.tileEntity.euOffer;
        this.lastTier = this.tileEntity.chargeSlot.tier;
    }

    public void updateProgressBar(int index, int value)
    {
        super.updateProgressBar(index, value);

        switch (index)
        {
            case 0:
                this.tileEntity.paidFor = this.tileEntity.paidFor & -65536 | value;
                break;

            case 1:
                this.tileEntity.paidFor = this.tileEntity.paidFor & 65535 | value << 16;
                break;

            case 2:
                this.tileEntity.euBuffer = this.tileEntity.euBuffer & -65536 | value;
                break;

            case 3:
                this.tileEntity.euBuffer = this.tileEntity.euBuffer & 65535 | value << 16;
                break;

            case 4:
                this.tileEntity.euOffer = this.tileEntity.euOffer & -65536 | value;
                break;

            case 5:
                this.tileEntity.euOffer = this.tileEntity.euOffer & 65535 | value << 16;
                break;

            case 6:
                this.tileEntity.chargeSlot.tier = value;
        }
    }
}
