package ic2.core.block.wiring;

import ic2.core.ITickCallback;
import net.minecraft.world.World;

class TileEntityLuminator$1 implements ITickCallback
{
    final TileEntityLuminator this$0;

    TileEntityLuminator$1(TileEntityLuminator var1)
    {
        this.this$0 = var1;
    }

    public void tickCallback(World world)
    {
        if (!this.this$0.isInvalid() && world.blockExists(this.this$0.xCoord, this.this$0.yCoord, this.this$0.zCoord))
        {
            this.this$0.onLoaded();

            if (this.this$0.enableUpdateEntity())
            {
                world.loadedTileEntityList.add(this.this$0);
            }
        }
    }
}
