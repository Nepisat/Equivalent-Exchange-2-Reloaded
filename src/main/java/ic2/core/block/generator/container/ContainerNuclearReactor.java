package ic2.core.block.generator.container;

import ic2.core.ContainerFullInv;
import ic2.core.block.generator.tileentity.TileEntityNuclearReactor;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerNuclearReactor extends ContainerFullInv
{
    public TileEntityNuclearReactor tileEntity;
    public short output = -1;
    public int heat = -1;
    public short size;

    public ContainerNuclearReactor(EntityPlayer entityPlayer, TileEntityNuclearReactor tileEntity)
    {
        super(entityPlayer, tileEntity, 222);
        this.tileEntity = tileEntity;
        this.size = tileEntity.getReactorSize();
        int startX = 89 - 9 * this.size;
        byte startY = 18;
        int x = 0;
        int y = 0;

        for (int i = 0; i < tileEntity.reactorSlot.size(); ++i)
        {
            if (x < this.size)
            {
                this.addSlotToContainer(new SlotInvSlot(tileEntity.reactorSlot, i, startX + 18 * x, startY + 18 * y));
            }

            ++x;

            if (x >= 9)
            {
                ++y;
                x = 0;
            }
        }
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < super.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)super.crafters.get(i);

            if (this.output != this.tileEntity.getOutput())
            {
                icrafting.sendProgressBarUpdate(this, 0, this.tileEntity.getOutput());
            }

            if (this.heat != this.tileEntity.heat)
            {
                icrafting.sendProgressBarUpdate(this, 1, this.tileEntity.heat & 65535);
                icrafting.sendProgressBarUpdate(this, 2, this.tileEntity.heat >>> 16);
            }
        }

        this.output = (short)this.tileEntity.getOutput();
        this.heat = this.tileEntity.heat;
    }

    public void updateProgressBar(int index, int value)
    {
        super.updateProgressBar(index, value);

        switch (index)
        {
            case 0:
                this.tileEntity.output = (float)((short)value);
                break;

            case 1:
                this.tileEntity.heat = this.tileEntity.heat & -65536 | value;
                break;

            case 2:
                this.tileEntity.heat = this.tileEntity.heat & 65535 | value << 16;
        }
    }
}
