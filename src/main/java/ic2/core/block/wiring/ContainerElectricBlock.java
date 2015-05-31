package ic2.core.block.wiring;

import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotArmor;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerElectricBlock extends ContainerFullInv
{
    public final TileEntityElectricBlock tileEntity;
    private int lastEnergy = -1;

    public ContainerElectricBlock(EntityPlayer entityPlayer, TileEntityElectricBlock tileEntity)
    {
        super(entityPlayer, tileEntity, 196);
        this.tileEntity = tileEntity;

        for (int col = 0; col < 4; ++col)
        {
            this.addSlotToContainer(new SlotArmor(entityPlayer.inventory, col, 8 + col * 18, 84));
        }

        this.addSlotToContainer(new SlotInvSlot(tileEntity.chargeSlot, 0, 56, 17));
        this.addSlotToContainer(new SlotInvSlot(tileEntity.dischargeSlot, 0, 56, 53));
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < super.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)super.crafters.get(i);

            if (this.tileEntity.energy != this.lastEnergy)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.energy & 65535);
                icrafting.sendProgressBarUpdate(this, 1, this.tileEntity.energy >>> 16);
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
                this.tileEntity.energy = this.tileEntity.energy & -65536 | value;
                break;

            case 1:
                this.tileEntity.energy = this.tileEntity.energy & 65535 | value << 16;
        }
    }
}
