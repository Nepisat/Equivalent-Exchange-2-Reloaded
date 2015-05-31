package ic2.core.block.generator.container;

import ic2.core.block.generator.tileentity.TileEntityWaterGenerator;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerWaterGenerator extends ContainerBaseGenerator
{
    public ContainerWaterGenerator(EntityPlayer entityPlayer, TileEntityWaterGenerator tileEntity)
    {
        super(entityPlayer, tileEntity, 166, 80, 17);
        this.addSlotToContainer(new SlotInvSlot(tileEntity.fuelSlot, 0, 80, 53));
    }
}
