package ic2.core;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

class EnergyNet$PostPonedAddCallback implements ITickCallback
{
    private final TileEntity te;

    public EnergyNet$PostPonedAddCallback(TileEntity te)
    {
        this.te = te;
    }

    public void tickCallback(World world)
    {
        if (world.blockExists(this.te.xCoord, this.te.yCoord, this.te.zCoord) && !this.te.isInvalid())
        {
            EnergyNet.getForWorld(world).addTileEntity(this.te);
        }
        else
        {
            IC2.log.info("EnergyNet.addTileEntity: " + this.te + " unloaded, aborting");
        }
    }
}
