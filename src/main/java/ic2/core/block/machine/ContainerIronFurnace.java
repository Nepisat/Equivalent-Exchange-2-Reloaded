package ic2.core.block.machine;

import ic2.core.ContainerFullInv;
import ic2.core.block.machine.tileentity.TileEntityIronFurnace;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerIronFurnace extends ContainerFullInv
{
    public final TileEntityIronFurnace tileEntity;
    private short lastProgress = -1;
    private int lastFuel = -1;
    private int lastMaxFuel = -1;

    public ContainerIronFurnace(EntityPlayer entityPlayer, TileEntityIronFurnace tileEntity)
    {
        super(entityPlayer, tileEntity, 166);
        this.tileEntity = tileEntity;
        this.addSlotToContainer(new SlotInvSlot(tileEntity.inputSlot, 0, 56, 17));
        this.addSlotToContainer(new SlotInvSlot(tileEntity.fuelSlot, 0, 56, 53));
        this.addSlotToContainer(new SlotInvSlot(tileEntity.outputSlot, 0, 116, 35));
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < super.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)super.crafters.get(i);

            if (this.tileEntity.progress != this.lastProgress)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.progress);
            }

            if (this.tileEntity.fuel != this.lastFuel)
            {
                icrafting.sendProgressBarUpdate(this, 1, this.tileEntity.fuel);
                icrafting.sendProgressBarUpdate(this, 2, this.tileEntity.fuel);
            }

            if (this.tileEntity.maxFuel != this.lastMaxFuel)
            {
                icrafting.sendProgressBarUpdate(this, 3, this.tileEntity.maxFuel);
                icrafting.sendProgressBarUpdate(this, 4, this.tileEntity.maxFuel);
            }
        }

        this.lastProgress = this.tileEntity.progress;
        this.lastFuel = this.tileEntity.fuel;
        this.lastMaxFuel = this.tileEntity.maxFuel;
    }

    public void updateProgressBar(int index, int value)
    {
        super.updateProgressBar(index, value);

        switch (index)
        {
            case 0:
                this.tileEntity.progress = (short)value;
                break;

            case 1:
                this.tileEntity.fuel = this.tileEntity.fuel & -65536 | value;
                break;

            case 2:
                this.tileEntity.fuel = this.tileEntity.fuel & 65535 | value << 16;
                break;

            case 3:
                this.tileEntity.maxFuel = this.tileEntity.maxFuel & -65536 | value;
                break;

            case 4:
                this.tileEntity.maxFuel = this.tileEntity.maxFuel & 65535 | value << 16;
        }
    }
}
