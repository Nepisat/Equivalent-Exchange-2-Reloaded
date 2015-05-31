package ic2.core.block.wiring;

import ic2.api.Direction;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.block.BlockMultiID;
import ic2.core.block.BlockPoleFence;
import ic2.core.init.InternalName;
import ic2.core.item.block.ItemLuminator;
import ic2.core.util.AabbUtil;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class BlockLuminator extends BlockMultiID
{
    boolean light;

    public BlockLuminator(Configuration config, InternalName internalName)
    {
        super(config, internalName, Material.glass, ItemLuminator.class);
        this.setStepSound(Block.soundGlassFootstep);
        this.setHardness(0.3F);
        this.setResistance(0.5F);

        if (internalName == InternalName.blockLuminator)
        {
            this.light = true;
            this.setLightValue(1.0F);
            this.setCreativeTab((CreativeTabs)null);
        }
        else
        {
            this.light = false;
        }
    }

    public String getTextureFolder()
    {
        return "wiring";
    }

    public int getTextureIndex(int meta)
    {
        return 0;
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int direction)
    {
        if (world.getBlockId(i, j, k) != 0)
        {
            return false;
        }
        else
        {
            switch (direction)
            {
                case 0:
                    ++j;
                    break;

                case 1:
                    --j;
                    break;

                case 2:
                    ++k;
                    break;

                case 3:
                    --k;
                    break;

                case 4:
                    ++i;
                    break;

                case 5:
                    --i;
            }

            return isSupportingBlock(world, i, j, k);
        }
    }

    public static boolean isSupportingBlock(World world, int i, int j, int k)
    {
        return world.getBlockId(i, j, k) == 0 ? false : (world.isBlockOpaqueCube(i, j, k) ? true : isSpecialSupporter(world, i, j, k));
    }

    public static boolean isSpecialSupporter(IBlockAccess world, int i, int j, int k)
    {
        Block block = Block.blocksList[world.getBlockId(i, j, k)];
        return block == null ? false : (!(block instanceof BlockFence) && !(block instanceof BlockPoleFence) && !(block instanceof BlockCable) ? block.blockID == Ic2Items.reinforcedGlass.itemID || block == Block.glass : true);
    }

    public boolean canBlockStay(World world, int i, int j, int k)
    {
        TileEntity te = world.getBlockTileEntity(i, j, k);

        if (te != null && ((TileEntityLuminator)te).ignoreBlockStay)
        {
            return true;
        }
        else
        {
            int facing = world.getBlockMetadata(i, j, k);

            switch (facing)
            {
                case 0:
                    ++j;
                    break;

                case 1:
                    --j;
                    break;

                case 2:
                    ++k;
                    break;

                case 3:
                    --k;
                    break;

                case 4:
                    ++i;
                    break;

                case 5:
                    --i;
            }

            return isSupportingBlock(world, i, j, k);
        }
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        if (!this.canBlockStay(world, i, j, k))
        {
            world.setBlock(i, j, k, 0, 0, 7);
        }

        super.onNeighborBlockChange(world, i, j, k, l);
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int getRenderType()
    {
        return IC2.platform.getRenderId("luminator");
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z, int meta)
    {
        return this.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        float[] box = getBoxOfLuminator(world, x, y, z);
        return AxisAlignedBB.getAABBPool().getAABB((double)(box[0] + (float)x), (double)(box[1] + (float)y), (double)(box[2] + (float)z), (double)(box[3] + (float)x), (double)(box[4] + (float)y), (double)(box[5] + (float)z));
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return this.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 origin, Vec3 absDirection)
    {
        Vec3 direction = Vec3.createVectorHelper(absDirection.xCoord - origin.xCoord, absDirection.yCoord - origin.yCoord, absDirection.zCoord - origin.zCoord);
        double maxLength = direction.lengthVector();
        Vec3 intersection = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
        Direction intersectingDirection = AabbUtil.getIntersection(origin, direction, this.getCollisionBoundingBoxFromPool(world, x, y, z), intersection);
        return intersectingDirection != null && intersection.distanceTo(origin) <= maxLength ? new MovingObjectPosition(x, y, z, intersectingDirection.toSideValue(), intersection) : null;
    }

    public static float[] getBoxOfLuminator(IBlockAccess world, int x, int y, int z)
    {
        int facing = world.getBlockMetadata(x, y, z);
        float px = 0.0625F;

        switch (facing)
        {
            case 0:
                ++y;
                break;

            case 1:
                --y;
                break;

            case 2:
                ++z;
                break;

            case 3:
                --z;
                break;

            case 4:
                ++x;
                break;

            case 5:
                --x;
        }

        boolean fullCover = isSpecialSupporter(world, x, y, z);

        switch (facing)
        {
            case 1:
                return new float[] {0.0F, 0.0F, 0.0F, 1.0F, 1.0F * px, 1.0F};
            case 2:
                if (fullCover)
                {
                    return new float[] {0.0F, 0.0F, 15.0F * px, 1.0F, 1.0F, 1.0F};
                }

                return new float[] {6.0F * px, 3.0F * px, 14.0F * px, 1.0F - 6.0F * px, 1.0F - 3.0F * px, 1.0F};

            case 3:
                if (fullCover)
                {
                    return new float[] {0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F * px};
                }

                return new float[] {6.0F * px, 3.0F * px, 0.0F, 1.0F - 6.0F * px, 1.0F - 3.0F * px, 2.0F * px};

            case 4:
                if (fullCover)
                {
                    return new float[] {15.0F * px, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F};
                }

                return new float[] {14.0F * px, 3.0F * px, 6.0F * px, 1.0F, 1.0F - 3.0F * px, 1.0F - 6.0F * px};

            case 5:
                if (fullCover)
                {
                    return new float[] {0.0F, 0.0F, 0.0F, 1.0F * px, 1.0F, 1.0F};
                }

                return new float[] {0.0F, 3.0F * px, 6.0F * px, 2.0F * px, 1.0F - 3.0F * px, 1.0F - 6.0F * px};

            default:
                return fullCover ? new float[] {0.0F, 15.0F * px, 0.0F, 1.0F, 1.0F, 1.0F}: new float[] {4.0F * px, 13.0F * px, 4.0F * px, 1.0F - 4.0F * px, 1.0F, 1.0F - 4.0F * px};
        }
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isBlockNormalCube(World world, int i, int j, int k)
    {
        return false;
    }

    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float a, float b, float c)
    {
        ItemStack itemStack = entityplayer.getCurrentEquippedItem();

        if (itemStack != null && itemStack.getItem() instanceof IElectricItem)
        {
            itemStack.getItem();
            TileEntityLuminator lumi = (TileEntityLuminator)world.getBlockTileEntity(i, j, k);
            int transfer = lumi.getMaxEnergy() - lumi.energy;

            if (transfer <= 0)
            {
                return false;
            }
            else
            {
                transfer = ElectricItem.manager.discharge(itemStack, transfer, 2, true, false);

                if (!this.light)
                {
                    world.setBlock(i, j, k, Ic2Items.activeLuminator.itemID, world.getBlockMetadata(i, j, k), 7);
                    lumi = (TileEntityLuminator)world.getBlockTileEntity(i, j, k);
                }

                lumi.energy += transfer;
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        if (this.light && entity instanceof EntityMob)
        {
            entity.setFire(entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getCreatureAttribute() == EnumCreatureAttribute.UNDEAD ? 20 : 10);
        }
    }

    public TileEntity createTileEntity(World world, int meta)
    {
        return new TileEntityLuminator();
    }

    public void getSubBlocks(int i, CreativeTabs tabs, List itemList)
    {
        if (!this.light)
        {
            super.getSubBlocks(i, tabs, itemList);
        }
    }

    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5)
    {
        TileEntity te = par1World.getBlockTileEntity(par2, par3, par4);

        if (te != null && te.getClass() == TileEntityLuminator.class)
        {
            TileEntityLuminator tel = (TileEntityLuminator)te;
            return (int)Math.floor((double)((float)tel.energy / (float)tel.getMaxEnergy() * 15.0F));
        }
        else
        {
            return 0;
        }
    }
}
