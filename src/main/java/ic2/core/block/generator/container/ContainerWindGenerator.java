package ic2.core.block.generator.container;

import ic2.core.block.generator.tileentity.TileEntityWindGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerWindGenerator extends ContainerBaseGenerator
{
    public ContainerWindGenerator(EntityPlayer entityPlayer, TileEntityWindGenerator tileEntity)
    {
        super(entityPlayer, tileEntity, 166, 80, 26);
    }
}
