package ic2.core;

import ic2.api.Direction;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.ForgeDirection;

class EnergyNet$Tile
{
    final TileEntity entity;
    final EnergyNet$Tile[] neighbors;

    final EnergyNet this$0;

    EnergyNet$Tile(EnergyNet var1, TileEntity te)
    {
        this.this$0 = var1;
        this.neighbors = new EnergyNet$Tile[6];
        this.entity = te;
        Direction[] arr$ = Direction.directions;
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            Direction dir = arr$[i$];
            ForgeDirection fdir = dir.toForgeDirection();
            ChunkCoordinates coords = new ChunkCoordinates(te.xCoord + fdir.offsetX, te.yCoord + fdir.offsetY, te.zCoord + fdir.offsetZ);
            int index = dir.ordinal();
            this.neighbors[index] = (EnergyNet$Tile)EnergyNet.access$000(var1).get(coords);

            if (this.neighbors[index] != null)
            {
                this.neighbors[index].neighbors[dir.getInverse().ordinal()] = this;
            }
        }
    }

    void destroy()
    {
        Direction[] arr$ = Direction.directions;
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            Direction dir = arr$[i$];
            EnergyNet$Tile neighbor = this.neighbors[dir.ordinal()];

            if (neighbor != null)
            {
                neighbor.neighbors[dir.getInverse().ordinal()] = null;
            }
        }
    }
}
