package ic2.core.item;

import ic2.core.Ic2Items;
import ic2.core.init.InternalName;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemResin extends ItemIC2
{
    public ItemResin(Configuration config, InternalName internalName)
    {
        super(config, internalName);
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float a, float b, float c)
    {
        if (world.getBlockId(i, j, k) == Block.pistonBase.blockID && world.getBlockMetadata(i, j, k) == side)
        {
            world.setBlock(i, j, k, Block.pistonStickyBase.blockID, side, 3);

            if (!entityplayer.capabilities.isCreativeMode)
            {
                --itemstack.stackSize;
            }

            return true;
        }
        else if (side != 1)
        {
            return false;
        }
        else
        {
            ++j;

            if (world.getBlockId(i, j, k) == 0 && Block.blocksList[Ic2Items.resinSheet.itemID].canPlaceBlockAt(world, i, j, k))
            {
                world.setBlock(i, j, k, Ic2Items.resinSheet.itemID, 0, 3);

                if (!entityplayer.capabilities.isCreativeMode)
                {
                    --itemstack.stackSize;
                }

                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
