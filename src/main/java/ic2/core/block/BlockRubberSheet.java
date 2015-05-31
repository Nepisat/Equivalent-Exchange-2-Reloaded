package ic2.core.block;

import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.init.InternalName;
import ic2.core.item.block.ItemBlockRare;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class BlockRubberSheet extends BlockSimple
{
    public BlockRubberSheet(Configuration config, InternalName internalName)
    {
        super(config, internalName, Material.cloth, ItemBlockRare.class);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.setHardness(0.8F);
        this.setResistance(2.0F);
        this.setStepSound(Block.soundClothFootstep);
        Ic2Items.rubberTrampoline = new ItemStack(this);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        return this.isBlockSupporter(world, i - 1, j, k) || this.isBlockSupporter(world, i + 1, j, k) || this.isBlockSupporter(world, i, j, k - 1) || this.isBlockSupporter(world, i, j, k + 1);
    }

    public boolean isBlockSupporter(World world, int i, int j, int k)
    {
        return world.isBlockOpaqueCube(i, j, k) || world.getBlockId(i, j, k) == super.blockID;
    }

    public boolean canSupportWeight(World world, int i, int j, int k)
    {
        if (world.getBlockMetadata(i, j, k) == 1)
        {
            return true;
        }
        else
        {
            boolean xup = false;
            boolean xdown = false;
            boolean zup = false;
            boolean zdown = false;
            int z = i;

            while (true)
            {
                if (world.isBlockOpaqueCube(z, j, k))
                {
                    xdown = true;
                    break;
                }

                if (world.getBlockId(z, j, k) != super.blockID)
                {
                    break;
                }

                if (world.isBlockOpaqueCube(z, j - 1, k))
                {
                    xdown = true;
                    break;
                }

                --z;
            }

            z = i;

            while (true)
            {
                if (world.isBlockOpaqueCube(z, j, k))
                {
                    xup = true;
                    break;
                }

                if (world.getBlockId(z, j, k) != super.blockID)
                {
                    break;
                }

                if (world.isBlockOpaqueCube(z, j - 1, k))
                {
                    xup = true;
                    break;
                }

                ++z;
            }

            if (xup && xdown)
            {
                world.setBlockMetadataWithNotify(i, j, k, 1, 3);
                return true;
            }
            else
            {
                z = k;

                while (true)
                {
                    if (world.isBlockOpaqueCube(i, j, z))
                    {
                        zdown = true;
                        break;
                    }

                    if (world.getBlockId(i, j, z) != super.blockID)
                    {
                        break;
                    }

                    if (world.isBlockOpaqueCube(i, j - 1, z))
                    {
                        zdown = true;
                        break;
                    }

                    --z;
                }

                z = k;

                while (true)
                {
                    if (world.isBlockOpaqueCube(i, j, z))
                    {
                        zup = true;
                        break;
                    }

                    if (world.getBlockId(i, j, z) != super.blockID)
                    {
                        break;
                    }

                    if (world.isBlockOpaqueCube(i, j - 1, z))
                    {
                        zup = true;
                        break;
                    }

                    ++z;
                }

                if (zup && zdown)
                {
                    world.setBlockMetadataWithNotify(i, j, k, 1, 3);
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborId)
    {
        if (world.getBlockMetadata(x, y, z) == 1)
        {
            world.setBlockMetadataWithNotify(x, y, z, 0, 7);
        }

        if (!this.canPlaceBlockAt(world, x, y, z))
        {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }
    }

    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        if (!world.isBlockNormalCube(i, j - 1, k))
        {
            if (entity instanceof EntityLivingBase && !this.canSupportWeight(world, i, j, k))
            {
                world.setBlockToAir(i, j, k);
            }
            else if (entity.motionY <= -0.4000000059604645D)
            {
                entity.fallDistance = 0.0F;
                entity.motionX *= 1.100000023841858D;

                if (entity instanceof EntityLivingBase)
                {
                    if (entity instanceof EntityPlayer && IC2.keyboard.isJumpKeyDown((EntityPlayer)entity))
                    {
                        entity.motionY *= -1.2999999523162842D;
                    }
                    else if (entity instanceof EntityPlayer && ((EntityPlayer)entity).isSneaking())
                    {
                        entity.motionY *= -0.10000000149011612D;
                    }
                    else
                    {
                        entity.motionY *= -0.800000011920929D;
                    }
                }
                else
                {
                    entity.motionY *= -0.800000011920929D;
                }

                entity.motionZ *= 1.100000023841858D;
            }
        }
    }
}
