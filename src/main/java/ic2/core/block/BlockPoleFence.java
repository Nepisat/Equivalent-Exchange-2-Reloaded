package ic2.core.block;

import ic2.api.Direction;
import ic2.api.item.ItemWrapper;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.block.BlockPoleFence$1;
import ic2.core.init.InternalName;
import ic2.core.item.block.ItemBlockRare;
import ic2.core.util.AabbUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class BlockPoleFence extends BlockSimple
{
    private static final double halfThickness = 0.125D;
    private static final double halfBarThickness = 0.0625D;
    private static final double heightBarBottom = 0.375D;
    private static final double heightBar = 0.1875D;
    private static final double heightBarDistance = 0.375D;
    private static final Direction[] directions = new Direction[] {Direction.XN, Direction.XP, Direction.ZN, Direction.ZP};

    public BlockPoleFence(Configuration config, InternalName internalName)
    {
        super(config, internalName, Material.iron, ItemBlockRare.class);
        this.setHardness(1.5F);
        this.setResistance(5.0F);
        this.setStepSound(Block.soundMetalFootstep);
        Ic2Items.ironFence = new ItemStack(this);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isBlockNormalCube(World world, int i, int j, int k)
    {
        return false;
    }

    public int getRenderType()
    {
        return IC2.platform.getRenderId("fence");
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z, int meta)
    {
        return this.getCommonBoundingBoxFromPool(world, x, y, z, false);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return this.getCommonBoundingBoxFromPool(world, x, y, z, false);
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return this.getCommonBoundingBoxFromPool(world, x, y, z, true);
    }

    private AxisAlignedBB getCommonBoundingBoxFromPool(World world, int x, int y, int z, boolean selectionBoundingBox)
    {
        double minX = (double)x + 0.5D - 0.125D;
        double minY = (double)y;
        double minZ = (double)z + 0.5D - 0.125D;
        double maxX = (double)x + 0.5D + 0.125D;
        double maxY = selectionBoundingBox ? (double)(y + 1) : (double)y + 1.5D;
        double maxZ = (double)z + 0.5D + 0.125D;

        if (world.getBlockId(x - 1, y, z) == super.blockID)
        {
            minX = (double)x;
        }

        if (world.getBlockId(x + 1, y, z) == super.blockID)
        {
            maxX = (double)(x + 1);
        }

        if (world.getBlockId(x, y, z - 1) == super.blockID)
        {
            minZ = (double)z;
        }

        if (world.getBlockId(x, y, z + 1) == super.blockID)
        {
            maxZ = (double)(z + 1);
        }

        return AxisAlignedBB.getAABBPool().getAABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 origin, Vec3 absDirection)
    {
        Vec3 direction = Vec3.createVectorHelper(absDirection.xCoord - origin.xCoord, absDirection.yCoord - origin.yCoord, absDirection.zCoord - origin.zCoord);
        double maxLength = direction.lengthVector();
        boolean hit = false;
        Vec3 intersection = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
        Direction intersectingDirection = AabbUtil.getIntersection(origin, direction, AxisAlignedBB.getAABBPool().getAABB((double)x + 0.5D - 0.125D, (double)y, (double)z + 0.5D - 0.125D, (double)x + 0.5D + 0.125D, (double)(y + 1), (double)z + 0.5D + 0.125D), intersection);

        if (intersectingDirection != null && intersection.distanceTo(origin) <= maxLength)
        {
            hit = true;
        }
        else
        {
            Direction[] arr$ = directions;
            int len$ = arr$.length;

            for (int i$ = 0; i$ < len$; ++i$)
            {
                Direction dir = arr$[i$];

                switch (BlockPoleFence$1.$SwitchMap$ic2$api$Direction[dir.ordinal()])
                {
                    case 1:
                        if (world.getBlockId(x - 1, y, z) != super.blockID)
                        {
                            continue;
                        }

                        break;

                    case 2:
                        if (world.getBlockId(x + 1, y, z) != super.blockID)
                        {
                            continue;
                        }

                        break;

                    case 3:
                        if (world.getBlockId(x, y, z - 1) != super.blockID)
                        {
                            continue;
                        }

                        break;

                    case 4:
                        if (world.getBlockId(x, y, z + 1) != super.blockID)
                        {
                            continue;
                        }

                        break;

                    default:
                        continue;
                }

                for (int pieceToCheck = 0; pieceToCheck < 2; ++pieceToCheck)
                {
                    AxisAlignedBB bbox = null;
                    double yMin = (double)y + 0.375D + (double)pieceToCheck * 0.375D;
                    double yMax = yMin + 0.1875D;

                    switch (BlockPoleFence$1.$SwitchMap$ic2$api$Direction[dir.ordinal()])
                    {
                        case 1:
                            bbox = AxisAlignedBB.getAABBPool().getAABB((double)x, yMin, (double)z + 0.5D - 0.0625D, (double)x + 0.5D, yMax, (double)z + 0.5D + 0.0625D);
                            break;

                        case 2:
                            bbox = AxisAlignedBB.getAABBPool().getAABB((double)x + 0.5D, yMin, (double)z + 0.5D - 0.0625D, (double)x + 1.0D, yMax, (double)z + 0.5D + 0.0625D);
                            break;

                        case 3:
                            bbox = AxisAlignedBB.getAABBPool().getAABB((double)x + 0.5D - 0.0625D, yMin, (double)z, (double)x + 0.5D + 0.0625D, yMax, (double)z + 0.5D);
                            break;

                        case 4:
                            bbox = AxisAlignedBB.getAABBPool().getAABB((double)x + 0.5D - 0.0625D, yMin, (double)z + 0.5D, (double)x + 0.5D + 0.0625D, yMax, (double)z + 1.0D);
                            break;

                        default:
                            continue;
                    }

                    intersectingDirection = AabbUtil.getIntersection(origin, direction, bbox, intersection);

                    if (intersectingDirection != null && intersection.distanceTo(origin) <= maxLength)
                    {
                        hit = true;
                        break;
                    }
                }

                if (hit)
                {
                    break;
                }
            }
        }

        return hit ? new MovingObjectPosition(x, y, z, intersectingDirection.toSideValue(), intersection) : null;
    }

    public boolean isPole(World world, int i, int j, int k)
    {
        return world.getBlockId(i - 1, j, k) != super.blockID && world.getBlockId(i + 1, j, k) != super.blockID && world.getBlockId(i, j, k - 1) != super.blockID && world.getBlockId(i, j, k + 1) != super.blockID;
    }

    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        if (super.blockMaterial == Material.iron && this.isPole(world, i, j, k) && entity instanceof EntityPlayer)
        {
            boolean powered = world.getBlockMetadata(i, j, k) > 0;
            boolean metalShoes = false;
            EntityPlayer player = (EntityPlayer)entity;
            ItemStack shoes = player.inventory.armorInventory[0];

            if (shoes != null)
            {
                int id = shoes.itemID;

                if (id == Item.bootsIron.itemID || id == Item.bootsGold.itemID || id == Item.bootsChain.itemID || ItemWrapper.isMetalArmor(shoes, player))
                {
                    metalShoes = true;
                }
            }

            if (powered && metalShoes)
            {
                world.setBlockMetadataWithNotify(i, j, k, world.getBlockMetadata(i, j, k) - 1, 7);
                player.motionY += 0.07500000298023224D;

                if (player.motionY > 0.0D)
                {
                    player.motionY *= 1.0299999713897705D;
                    player.fallDistance = 0.0F;
                }

                if (player.isSneaking())
                {
                    if (player.motionY > 0.30000001192092896D)
                    {
                        player.motionY = 0.30000001192092896D;
                    }
                }
                else if (player.motionY > 1.5D)
                {
                    player.motionY = 1.5D;
                }
            }
            else if (player.isSneaking())
            {
                if (player.motionY < -0.25D)
                {
                    player.motionY *= 0.8999999761581421D;
                }
                else
                {
                    player.fallDistance = 0.0F;
                }
            }
        }
    }
}
