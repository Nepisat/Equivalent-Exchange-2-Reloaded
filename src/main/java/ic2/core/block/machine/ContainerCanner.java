package ic2.core.block.machine;

import ic2.core.block.machine.tileentity.TileEntityCanner;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerCanner extends ContainerElectricMachine
{
    public final TileEntityCanner tileEntity;
    private short lastProgress = -1;

    public ContainerCanner(EntityPlayer entityPlayer, TileEntityCanner tileEntity)
    {
        super(entityPlayer, tileEntity, 166, 30, 45);
        this.tileEntity = tileEntity;
        this.addSlotToContainer(new SlotInvSlot(tileEntity.resInputSlot, 0, 69, 17));
        this.addSlotToContainer(new SlotInvSlot(tileEntity.outputSlot, 0, 119, 35));
        this.addSlotToContainer(new SlotInvSlot(tileEntity.inputSlot, 0, 69, 53));
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < super.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)super.crafters.get(i);

            if (this.tileEntity.progress != this.lastProgress)
            {
                icrafting.sendProgressBarUpdate(this, 3, this.tileEntity.progress);
            }
        }

        this.lastProgress = this.tileEntity.progress;
    }

    public void updateProgressBar(int index, int value)
    {
        super.updateProgressBar(index, value);

        switch (index)
        {
            case 3:
                this.tileEntity.progress = (short)value;

            default:
        }
    }
}
