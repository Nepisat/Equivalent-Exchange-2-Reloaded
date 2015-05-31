package ic2.core.block.machine;

import ic2.core.block.machine.tileentity.TileEntityInduction;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerInduction extends ContainerElectricMachine
{
    public final TileEntityInduction tileEntity;
    private short lastProgress = -1;
    private short lastHeat = -1;

    public ContainerInduction(EntityPlayer entityPlayer, TileEntityInduction tileEntity)
    {
        super(entityPlayer, tileEntity, 166, 56, 53);
        this.tileEntity = tileEntity;
        this.addSlotToContainer(new SlotInvSlot(tileEntity.inputSlotA, 0, 47, 17));
        this.addSlotToContainer(new SlotInvSlot(tileEntity.inputSlotB, 0, 63, 17));
        this.addSlotToContainer(new SlotInvSlot(tileEntity.outputSlotA, 0, 113, 35));
        this.addSlotToContainer(new SlotInvSlot(tileEntity.outputSlotB, 0, 131, 35));
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

            if (this.tileEntity.heat != this.lastHeat)
            {
                icrafting.sendProgressBarUpdate(this, 4, this.tileEntity.heat);
            }
        }

        this.lastProgress = this.tileEntity.progress;
        this.lastHeat = this.tileEntity.heat;
    }

    public void updateProgressBar(int index, int value)
    {
        super.updateProgressBar(index, value);

        switch (index)
        {
            case 3:
                this.tileEntity.progress = (short)value;
                break;

            case 4:
                this.tileEntity.heat = (short)value;
        }
    }
}
