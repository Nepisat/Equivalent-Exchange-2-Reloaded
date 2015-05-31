package ic2.core.item.tfbp;

import ic2.core.block.machine.tileentity.TileEntityTerra;
import ic2.core.init.InternalName;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemTFBPChilling extends ItemTFBP
{
    public ItemTFBPChilling(Configuration config, InternalName internalName)
    {
        super(config, internalName);
    }

    public int getConsume()
    {
        return 2000;
    }

    public int getRange()
    {
        return 50;
    }

    public boolean terraform(World world, int x, int z, int yCoord)
    {
        int y = TileEntityTerra.getFirstBlockFrom(world, x, z, yCoord + 10);

        if (y == -1)
        {
            return false;
        }
        else
        {
            int id = world.getBlockId(x, y, z);

            if (id != Block.waterMoving.blockID && id != Block.waterStill.blockID)
            {
                if (id == Block.ice.blockID)
                {
                    int id2 = world.getBlockId(x, y - 1, z);

                    if (id2 == Block.waterMoving.blockID || id2 == Block.waterStill.blockID)
                    {
                        world.setBlock(x, y - 1, z, Block.ice.blockID, 0, 7);
                        return true;
                    }
                }

                if (id == Block.snow.blockID && this.isSurroundedBySnow(world, x, y, z))
                {
                    world.setBlock(x, y, z, Block.blockSnow.blockID, 0, 7);
                    return true;
                }
                else
                {
                    if (Block.snow.canPlaceBlockAt(world, x, y + 1, z) || id == Block.ice.blockID)
                    {
                        world.setBlock(x, y + 1, z, Block.snow.blockID, 0, 7);
                    }

                    return false;
                }
            }
            else
            {
                world.setBlock(x, y, z, Block.ice.blockID, 0, 7);
                return true;
            }
        }
    }

    public boolean isSurroundedBySnow(World world, int x, int y, int z)
    {
        return this.isSnowHere(world, x + 1, y, z) && this.isSnowHere(world, x - 1, y, z) && this.isSnowHere(world, x, y, z + 1) && this.isSnowHere(world, x, y, z - 1);
    }

    public boolean isSnowHere(World world, int x, int y, int z)
    {
        int saveY = y;
        y = TileEntityTerra.getFirstBlockFrom(world, x, z, y + 16);

        if (saveY > y)
        {
            return false;
        }
        else
        {
            int id = world.getBlockId(x, y, z);

            if (id != Block.snow.blockID && id != Block.blockSnow.blockID)
            {
                if (Block.snow.canPlaceBlockAt(world, x, y + 1, z) || id == Block.ice.blockID)
                {
                    world.setBlock(x, y + 1, z, Block.snow.blockID, 0, 7);
                }

                return false;
            }
            else
            {
                return true;
            }
        }
    }
}
