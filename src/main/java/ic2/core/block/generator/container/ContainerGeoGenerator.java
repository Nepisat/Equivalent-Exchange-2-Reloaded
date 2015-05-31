package ic2.core.block.generator.container;

import ic2.core.block.generator.tileentity.TileEntityGeoGenerator;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerGeoGenerator extends ContainerBaseGenerator
{
    public ContainerGeoGenerator(EntityPlayer entityPlayer, TileEntityGeoGenerator tileEntity)
    {
        super(entityPlayer, tileEntity, 166, 65, 17);
        this.addSlotToContainer(new SlotInvSlot(tileEntity.fuelSlot, 0, 65, 53));
    }
}
