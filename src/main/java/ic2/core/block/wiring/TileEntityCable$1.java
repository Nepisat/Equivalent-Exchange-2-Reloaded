package ic2.core.block.wiring;

import ic2.core.ITickCallback;
import net.minecraft.world.World;

class TileEntityCable$1 implements ITickCallback
{
    final TileEntityCable this$0;

    TileEntityCable$1(TileEntityCable var1)
    {
        this.this$0 = var1;
    }

    public void tickCallback(World world)
    {
        if (world.rand.nextInt(500) == 0 && world.getBlockLightValue(this.this$0.xCoord, this.this$0.yCoord, this.this$0.zCoord) * 6 >= this.this$0.worldObj.rand.nextInt(1000))
        {
            this.this$0.changeFoam((byte)2);
        }
    }
}
