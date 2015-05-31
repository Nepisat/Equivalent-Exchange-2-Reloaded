package ic2.core.block.machine;

import ic2.core.block.machine.tileentity.TileEntityStandardMachine;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerStandardMachine extends ContainerElectricMachine
{
    public final TileEntityStandardMachine tileEntity;
    private float lastChargeLevel = -1.0F;
    private float lastProgress = -1.0F;

    public ContainerStandardMachine(EntityPlayer entityPlayer, TileEntityStandardMachine tileEntity)
    {
        super(entityPlayer, tileEntity, 166, 56, 53);
        this.tileEntity = tileEntity;
        this.addSlotToContainer(new SlotInvSlot(tileEntity.inputSlot, 0, 56, 17));
        this.addSlotToContainer(new SlotInvSlot(tileEntity.outputSlot, 0, 116, 35));

        for (int i = 0; i < 4; ++i)
        {
            this.addSlotToContainer(new SlotInvSlot(tileEntity.upgradeSlot, i, 152, 8 + i * 18));
        }
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        float chargeLevel = this.tileEntity.getChargeLevel();
        float progress = this.tileEntity.getProgress();

        for (int i = 0; i < super.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)super.crafters.get(i);

            if (this.lastChargeLevel != chargeLevel)
            {
                icrafting.sendProgressBarUpdate(this, 3, (short)((int)(chargeLevel * 32767.0F)));
            }

            if (this.lastProgress != progress)
            {
                icrafting.sendProgressBarUpdate(this, 4, (short)((int)(progress * 32767.0F)));
            }
        }

        this.lastChargeLevel = chargeLevel;
        this.lastProgress = progress;
    }

    public void updateProgressBar(int index, int value)
    {
        super.updateProgressBar(index, value);

        switch (index)
        {
            case 3:
                this.tileEntity.setChargeLevel((float)value / 32767.0F);
                break;

            case 4:
                this.tileEntity.setProgress((float)value / 32767.0F);
        }
    }
}
