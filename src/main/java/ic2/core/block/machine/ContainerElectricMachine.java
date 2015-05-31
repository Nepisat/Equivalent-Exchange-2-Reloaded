package ic2.core.block.machine;

import ic2.core.ContainerFullInv;
import ic2.core.block.machine.tileentity.TileEntityElectricMachine;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public abstract class ContainerElectricMachine extends ContainerFullInv
{
    public final TileEntityElectricMachine base;
    private int lastEnergy = -1;
    private int lastTier = -1;

    public ContainerElectricMachine(EntityPlayer entityPlayer, TileEntityElectricMachine base, int height, int dischargeX, int dischargeY)
    {
        super(entityPlayer, base, height);
        this.base = base;
        this.addSlotToContainer(new SlotInvSlot(base.dischargeSlot, 0, dischargeX, dischargeY));
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < super.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)super.crafters.get(i);

            if (this.base.energy != this.lastEnergy)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.base.energy & 65535);
                icrafting.sendProgressBarUpdate(this, 1, this.base.energy >>> 16);
            }

            if (this.base.dischargeSlot.tier != this.lastTier)
            {
                icrafting.sendProgressBarUpdate(this, 2, this.base.dischargeSlot.tier);
            }
        }

        this.lastEnergy = this.base.energy;
        this.lastTier = this.base.dischargeSlot.tier;
    }

    public void updateProgressBar(int index, int value)
    {
        super.updateProgressBar(index, value);

        switch (index)
        {
            case 0:
                this.base.energy = this.base.energy & -65536 | value;
                break;

            case 1:
                this.base.energy = this.base.energy & 65535 | value << 16;
                break;

            case 2:
                this.base.dischargeSlot.tier = value;
        }
    }
}
