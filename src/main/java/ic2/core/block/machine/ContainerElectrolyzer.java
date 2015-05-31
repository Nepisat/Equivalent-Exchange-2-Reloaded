package ic2.core.block.machine;

import ic2.core.ContainerFullInv;
import ic2.core.block.machine.tileentity.TileEntityElectrolyzer;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerElectrolyzer extends ContainerFullInv
{
    public final TileEntityElectrolyzer tileEntity;
    private short lastEnergy = -1;

    public ContainerElectrolyzer(EntityPlayer entityPlayer, TileEntityElectrolyzer tileEntity)
    {
        super(entityPlayer, tileEntity, 166);
        this.tileEntity = tileEntity;
        this.addSlotToContainer(new SlotInvSlot(tileEntity.waterSlot, 0, 53, 35));
        this.addSlotToContainer(new SlotInvSlot(tileEntity.hydrogenSlot, 0, 112, 35));
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < super.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)super.crafters.get(i);

            if (this.tileEntity.energy != this.lastEnergy)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.energy);
            }
        }

        this.lastEnergy = this.tileEntity.energy;
    }

    public void updateProgressBar(int index, int value)
    {
        super.updateProgressBar(index, value);

        switch (index)
        {
            case 0:
                this.tileEntity.energy = (short)value;

            default:
        }
    }
}
