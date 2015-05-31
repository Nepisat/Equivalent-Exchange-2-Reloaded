package ic2.core.block.machine;

import ic2.core.ContainerFullInv;
import ic2.core.block.machine.tileentity.TileEntityMatter;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerMatter extends ContainerFullInv
{
    public final TileEntityMatter tileEntity;
    private int lastEnergy = -1;
    private int lastScrap = -1;

    public ContainerMatter(EntityPlayer entityPlayer, TileEntityMatter tileEntity)
    {
        super(entityPlayer, tileEntity, 166);
        this.tileEntity = tileEntity;
        this.addSlotToContainer(new SlotInvSlot(tileEntity.amplifierSlot, 0, 114, 54));
        this.addSlotToContainer(new SlotInvSlot(tileEntity.outputSlot, 0, 114, 18));
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

            if (this.tileEntity.scrap != this.lastScrap)
            {
                icrafting.sendProgressBarUpdate(this, 2, this.tileEntity.scrap & 65535);
                icrafting.sendProgressBarUpdate(this, 3, this.tileEntity.scrap >>> 16);
            }
        }

        this.lastEnergy = this.tileEntity.energy;
        this.lastScrap = this.tileEntity.scrap;
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
                break;

            case 2:
                this.tileEntity.scrap = this.tileEntity.scrap & -65536 | value;
                break;

            case 3:
                this.tileEntity.scrap = this.tileEntity.scrap & 65535 | value << 16;
        }
    }
}
