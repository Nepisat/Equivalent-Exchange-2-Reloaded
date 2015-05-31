package ic2.core.block.machine;

import ic2.core.block.machine.tileentity.TileEntityMiner;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerMiner extends ContainerElectricMachine
{
    public final TileEntityMiner tileEntity;
    private short lastProgress = -1;

    public ContainerMiner(EntityPlayer entityPlayer, TileEntityMiner tileEntity)
    {
        super(entityPlayer, tileEntity, 166, 81, 59);
        this.tileEntity = tileEntity;
        this.addSlotToContainer(new SlotInvSlot(tileEntity.scannerSlot, 0, 117, 22));
        this.addSlotToContainer(new SlotInvSlot(tileEntity.pipeSlot, 0, 81, 22));
        this.addSlotToContainer(new SlotInvSlot(tileEntity.drillSlot, 0, 45, 22));
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

        this.lastProgress = (short)this.tileEntity.progress;
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
