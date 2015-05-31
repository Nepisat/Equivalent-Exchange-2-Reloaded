package ic2.core.block.machine;

import ic2.core.block.machine.tileentity.TileEntityPump;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerPump extends ContainerElectricMachine
{
    public final TileEntityPump tileEntity;
    private short lastPumpCharge = -1;

    public ContainerPump(EntityPlayer entityPlayer, TileEntityPump tileEntity)
    {
        super(entityPlayer, tileEntity, 166, 62, 53);
        this.tileEntity = tileEntity;
        this.addSlotToContainer(new SlotInvSlot(tileEntity.containerSlot, 0, 62, 17));
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < super.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)super.crafters.get(i);

            if (this.tileEntity.pumpCharge != this.lastPumpCharge)
            {
                icrafting.sendProgressBarUpdate(this, 3, this.tileEntity.pumpCharge);
            }
        }

        this.lastPumpCharge = this.tileEntity.pumpCharge;
    }

    public void updateProgressBar(int index, int value)
    {
        super.updateProgressBar(index, value);

        switch (index)
        {
            case 3:
                this.tileEntity.pumpCharge = (short)value;

            default:
        }
    }
}
