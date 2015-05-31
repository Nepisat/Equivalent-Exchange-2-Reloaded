package ic2.core.block.machine;

import ic2.core.ContainerFullInv;
import ic2.core.block.machine.tileentity.TileEntityCropmatron;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerCropmatron extends ContainerFullInv
{
    public final TileEntityCropmatron tileEntity;
    private int lastEnergy = -1;

    public ContainerCropmatron(EntityPlayer entityPlayer, TileEntityCropmatron tileEntity)
    {
        super(entityPlayer, tileEntity, 166);
        this.tileEntity = tileEntity;
        int i;

        for (i = 0; i < tileEntity.fertilizerSlot.size(); ++i)
        {
            this.addSlotToContainer(new SlotInvSlot(tileEntity.fertilizerSlot, i, 62, 20 + i * 18));
        }

        for (i = 0; i < tileEntity.hydrationSlot.size(); ++i)
        {
            this.addSlotToContainer(new SlotInvSlot(tileEntity.hydrationSlot, i, 98, 20 + i * 18));
        }

        for (i = 0; i < tileEntity.weedExSlot.size(); ++i)
        {
            this.addSlotToContainer(new SlotInvSlot(tileEntity.weedExSlot, i, 134, 20 + i * 18));
        }
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < super.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)super.crafters.get(i);

            if (this.tileEntity.energy != this.lastEnergy)
            {
                icrafting.sendProgressBarUpdate(this, 1, this.tileEntity.energy & 65535);
                icrafting.sendProgressBarUpdate(this, 2, this.tileEntity.energy >>> 16);
            }
        }

        this.lastEnergy = this.tileEntity.energy;
    }

    public void updateProgressBar(int index, int value)
    {
        super.updateProgressBar(index, value);

        switch (index)
        {
            case 1:
                this.tileEntity.energy = this.tileEntity.energy & -65536 | value;
                break;

            case 2:
                this.tileEntity.energy = this.tileEntity.energy & 65535 | value << 16;
        }
    }
}
