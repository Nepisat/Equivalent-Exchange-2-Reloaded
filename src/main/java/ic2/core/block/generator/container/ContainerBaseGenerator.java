package ic2.core.block.generator.container;

import ic2.core.ContainerFullInv;
import ic2.core.block.generator.tileentity.TileEntityBaseGenerator;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public abstract class ContainerBaseGenerator extends ContainerFullInv
{
    public final TileEntityBaseGenerator tileEntity;
    public short lastStorage = -1;
    public int lastFuel = -1;

    public ContainerBaseGenerator(EntityPlayer entityPlayer, TileEntityBaseGenerator tileEntity, int height, int chargeX, int chargeY)
    {
        super(entityPlayer, tileEntity, height);
        this.tileEntity = tileEntity;
        this.addSlotToContainer(new SlotInvSlot(tileEntity.chargeSlot, 0, chargeX, chargeY));
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < super.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)super.crafters.get(i);

            if (this.tileEntity.storage != this.lastStorage)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.storage);
            }

            if (this.tileEntity.fuel != this.lastFuel)
            {
                icrafting.sendProgressBarUpdate(this, 1, this.tileEntity.fuel & 65535);
                icrafting.sendProgressBarUpdate(this, 2, this.tileEntity.fuel >>> 16);
            }
        }

        this.lastStorage = this.tileEntity.storage;
        this.lastFuel = this.tileEntity.fuel;
    }

    public void updateProgressBar(int index, int value)
    {
        super.updateProgressBar(index, value);

        switch (index)
        {
            case 0:
                this.tileEntity.storage = (short)value;
                break;

            case 1:
                this.tileEntity.fuel = this.tileEntity.fuel & -65536 | value;
                break;

            case 2:
                this.tileEntity.fuel = this.tileEntity.fuel & 65535 | value << 16;
        }
    }
}
