package ic2.core.item.tfbp;

import ic2.core.Ic2Items;
import ic2.core.block.machine.tileentity.TileEntityTerra;
import ic2.core.init.InternalName;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemTFBPDesertification extends ItemTFBP
{
    public ItemTFBPDesertification(Configuration config, InternalName internalName)
    {
        super(config, internalName);
    }

    public int getConsume()
    {
        return 2500;
    }

    public int getRange()
    {
        return 40;
    }

    public boolean terraform(World world, int x, int z, int yCoord)
    {
        int y = TileEntityTerra.getFirstBlockFrom(world, x, z, yCoord + 10);

        if (y == -1)
        {
            return false;
        }
        else if (!TileEntityTerra.switchGround(world, Block.dirt, Block.sand, x, y, z, false) && !TileEntityTerra.switchGround(world, Block.grass, Block.sand, x, y, z, false) && !TileEntityTerra.switchGround(world, Block.tilledField, Block.sand, x, y, z, false))
        {
            int id = world.getBlockId(x, y, z);

            if (id != Block.waterMoving.blockID && id != Block.waterStill.blockID && id != Block.snow.blockID && id != Block.leaves.blockID && id != Ic2Items.rubberLeaves.itemID && !this.isPlant(id))
            {
                if (id != Block.ice.blockID && id != Block.blockSnow.blockID)
                {
                    if ((id == Block.planks.blockID || id == Block.wood.blockID || id == Ic2Items.rubberWood.itemID) && world.rand.nextInt(15) == 0)
                    {
                        world.setBlock(x, y, z, Block.fire.blockID, 0, 7);
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    world.setBlock(x, y, z, Block.waterMoving.blockID, 0, 7);
                    return true;
                }
            }
            else
            {
                world.setBlock(x, y, z, 0, 0, 7);
                return true;
            }
        }
        else
        {
            TileEntityTerra.switchGround(world, Block.dirt, Block.sand, x, y, z, false);
            return true;
        }
    }

    public boolean isPlant(int id)
    {
        for (int i = 0; i < ItemTFBPCultivation.plantIDs.size(); ++i)
        {
            if (((Integer)ItemTFBPCultivation.plantIDs.get(i)).intValue() == id)
            {
                return true;
            }
        }

        return false;
    }
}
