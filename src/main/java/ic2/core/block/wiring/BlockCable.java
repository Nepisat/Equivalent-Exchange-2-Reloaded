package ic2.core.block.wiring;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.Direction;
import ic2.api.event.PaintEvent;
import ic2.api.event.RetextureEvent;
import ic2.core.EnergyNet;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.block.BlockMultiID;
import ic2.core.block.BlockTextureStitched;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.wiring.BlockCable$1;
import ic2.core.init.InternalName;
import ic2.core.item.block.ItemBlockRare;
import ic2.core.item.tool.ItemToolCutter;
import ic2.core.util.AabbUtil;
import ic2.core.util.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;

public class BlockCable extends BlockMultiID
{
    private static final Direction[] directions = Direction.values();
    private static final int[] coloredMetas = new int[] {0, 3, 4, 6, 7, 8, 9};
    public boolean enableBreakBlock = true;
    @SideOnly(Side.CLIENT)
    private Icon[][] coloredTextures;

    public BlockCable(Configuration config, InternalName internalName)
    {
        super(config, internalName, Material.iron, ItemBlockRare.class);
        this.setHardness(0.2F);
        this.setStepSound(Block.soundClothFootstep);
        this.setCreativeTab((CreativeTabs)null);
        Ic2Items.copperCableBlock = new ItemStack(this, 1, 1);
        Ic2Items.insulatedCopperCableBlock = new ItemStack(this, 1, 0);
        Ic2Items.goldCableBlock = new ItemStack(this, 1, 2);
        Ic2Items.insulatedGoldCableBlock = new ItemStack(this, 1, 3);
        Ic2Items.doubleInsulatedGoldCableBlock = new ItemStack(this, 1, 4);
        Ic2Items.ironCableBlock = new ItemStack(this, 1, 5);
        Ic2Items.insulatedIronCableBlock = new ItemStack(this, 1, 6);
        Ic2Items.doubleInsulatedIronCableBlock = new ItemStack(this, 1, 7);
        Ic2Items.trippleInsulatedIronCableBlock = new ItemStack(this, 1, 8);
        Ic2Items.glassFiberCableBlock = new ItemStack(this, 1, 9);
        Ic2Items.tinCableBlock = new ItemStack(this, 1, 10);
        Ic2Items.detectorCableBlock = new ItemStack(this, 1, 11);
        Ic2Items.splitterCableBlock = new ItemStack(this, 1, 12);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public String getTextureFolder()
    {
        return "wiring/cable";
    }

    public String getTextureName(int index)
    {
        Item item = Ic2Items.copperCableItem.getItem();
        ItemStack itemStack = new ItemStack(this, 1, index);
        String ret = item.getUnlocalizedName(itemStack);
        return ret == null ? null : ret.replace("item", "block");
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        super.registerIcons(iconRegister);
        this.coloredTextures = new Icon[coloredMetas.length][90];

        for (int index = 0; index < coloredMetas.length; ++index)
        {
            int meta = coloredMetas[index];

            for (int color = 1; color < 16; ++color)
            {
                String name = "ic2:" + this.getTextureFolder() + "/" + this.getTextureName(meta) + "." + Util.getColorName(color).name();

                for (int side = 0; side < 6; ++side)
                {
                    String subName = name + ":" + side;
                    BlockTextureStitched texture = new BlockTextureStitched(subName, side);
                    this.coloredTextures[index][(color - 1) * 6 + side] = texture;
                    ((TextureMap)iconRegister).setTextureEntry(subName, texture);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z, int side)
    {
        TileEntity te = iBlockAccess.getBlockTileEntity(x, y, z);

        if (!(te instanceof TileEntityCable))
        {
            return null;
        }
        else
        {
            TileEntityCable cable = (TileEntityCable)te;

            if (cable.foamed == 0)
            {
                if (!(te instanceof TileEntityCableDetector) && !(te instanceof TileEntityCableSplitter) && cable.color != 0)
                {
                    int referencedBlock1 = Arrays.binarySearch(coloredMetas, cable.cableType);
                    return this.coloredTextures[referencedBlock1][(cable.color - 1) * 6 + side];
                }
                else
                {
                    return super.getBlockTexture(iBlockAccess, x, y, z, side);
                }
            }
            else if (cable.foamed == 1)
            {
                return Block.blocksList[Ic2Items.constructionFoam.itemID].getIcon(side, 0);
            }
            else
            {
                if (cable.retextureRefId != null && cable.retextureRefId[side] != 0 && cable.retextureRefMeta != null && cable.retextureRefSide != null)
                {
                    Block referencedBlock = Block.blocksList[cable.retextureRefId[side]];

                    if (referencedBlock != null)
                    {
                        try
                        {
                            return referencedBlock.getIcon(cable.retextureRefSide[side], cable.retextureRefMeta[side]);
                        }
                        catch (Exception var10)
                        {
                            ;
                        }
                    }
                }

                return Block.blocksList[Ic2Items.constructionFoamWall.itemID].getIcon(side, cable.foamColor);
            }
        }
    }

    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 origin, Vec3 absDirection)
    {
        TileEntity te = world.getBlockTileEntity(x, y, z);

        if (!(te instanceof TileEntityCable))
        {
            return null;
        }
        else
        {
            TileEntityCable tileEntityCable = (TileEntityCable)te;
            Vec3 direction = Vec3.createVectorHelper(absDirection.xCoord - origin.xCoord, absDirection.yCoord - origin.yCoord, absDirection.zCoord - origin.zCoord);
            double maxLength = direction.lengthVector();
            double halfThickness = tileEntityCable.foamed > 0 ? 0.5D : (double)tileEntityCable.getCableThickness() / 2.0D;
            boolean hit = false;
            Vec3 intersection = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
            Direction intersectingDirection = AabbUtil.getIntersection(origin, direction, AxisAlignedBB.getAABBPool().getAABB((double)x + 0.5D - halfThickness, (double)y + 0.5D - halfThickness, (double)z + 0.5D - halfThickness, (double)x + 0.5D + halfThickness, (double)y + 0.5D + halfThickness, (double)z + 0.5D + halfThickness), intersection);

            if (intersectingDirection != null && intersection.distanceTo(origin) <= maxLength)
            {
                hit = true;
            }
            else if (halfThickness < 0.5D)
            {
                int mask = 1;
                Direction[] arr$ = directions;
                int len$ = arr$.length;

                for (int i$ = 0; i$ < len$; ++i$)
                {
                    Direction dir = arr$[i$];

                    if ((tileEntityCable.connectivity & mask) == 0)
                    {
                        mask *= 2;
                    }
                    else
                    {
                        mask *= 2;
                        AxisAlignedBB bbox = null;

                        switch (BlockCable$1.$SwitchMap$ic2$api$Direction[dir.ordinal()])
                        {
                            case 1:
                                bbox = AxisAlignedBB.getAABBPool().getAABB((double)x, (double)y + 0.5D - halfThickness, (double)z + 0.5D - halfThickness, (double)x + 0.5D, (double)y + 0.5D + halfThickness, (double)z + 0.5D + halfThickness);
                                break;

                            case 2:
                                bbox = AxisAlignedBB.getAABBPool().getAABB((double)x + 0.5D, (double)y + 0.5D - halfThickness, (double)z + 0.5D - halfThickness, (double)x + 1.0D, (double)y + 0.5D + halfThickness, (double)z + 0.5D + halfThickness);
                                break;

                            case 3:
                                bbox = AxisAlignedBB.getAABBPool().getAABB((double)x + 0.5D - halfThickness, (double)y, (double)z + 0.5D - halfThickness, (double)x + 0.5D + halfThickness, (double)y + 0.5D, (double)z + 0.5D + halfThickness);
                                break;

                            case 4:
                                bbox = AxisAlignedBB.getAABBPool().getAABB((double)x + 0.5D - halfThickness, (double)y + 0.5D, (double)z + 0.5D - halfThickness, (double)x + 0.5D + halfThickness, (double)y + 1.0D, (double)z + 0.5D + halfThickness);
                                break;

                            case 5:
                                bbox = AxisAlignedBB.getAABBPool().getAABB((double)x + 0.5D - halfThickness, (double)y + 0.5D - halfThickness, (double)z, (double)x + 0.5D + halfThickness, (double)y + 0.5D, (double)z + 0.5D);
                                break;

                            case 6:
                                bbox = AxisAlignedBB.getAABBPool().getAABB((double)x + 0.5D - halfThickness, (double)y + 0.5D - halfThickness, (double)z + 0.5D, (double)x + 0.5D + halfThickness, (double)y + 0.5D + halfThickness, (double)z + 1.0D);
                        }

                        intersectingDirection = AabbUtil.getIntersection(origin, direction, bbox, intersection);

                        if (intersectingDirection != null && intersection.distanceTo(origin) <= maxLength)
                        {
                            hit = true;
                            break;
                        }
                    }
                }
            }

            return hit ? new MovingObjectPosition(x, y, z, intersectingDirection.toSideValue(), intersection) : null;
        }
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z, int meta)
    {
        double halfThickness = (double)TileEntityCable.getCableThickness(meta);
        return AxisAlignedBB.getAABBPool().getAABB((double)x + 0.5D - halfThickness, (double)y + 0.5D - halfThickness, (double)z + 0.5D - halfThickness, (double)x + 0.5D + halfThickness, (double)y + 0.5D + halfThickness, (double)z + 0.5D + halfThickness);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return this.getCommonBoundingBoxFromPool(world, x, y, z, false);
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return this.getCommonBoundingBoxFromPool(world, x, y, z, true);
    }

    public AxisAlignedBB getCommonBoundingBoxFromPool(World world, int x, int y, int z, boolean selectionBoundingBox)
    {
        TileEntity te = world.getBlockTileEntity(x, y, z);

        if (!(te instanceof TileEntityCable))
        {
            return this.getCollisionBoundingBoxFromPool(world, x, y, z, 3);
        }
        else
        {
            TileEntityCable cable = (TileEntityCable)te;
            double halfThickness = cable.foamed == 1 && selectionBoundingBox ? 0.5D : (double)cable.getCableThickness() / 2.0D;
            double minX = (double)x + 0.5D - halfThickness;
            double minY = (double)y + 0.5D - halfThickness;
            double minZ = (double)z + 0.5D - halfThickness;
            double maxX = (double)x + 0.5D + halfThickness;
            double maxY = (double)y + 0.5D + halfThickness;
            double maxZ = (double)z + 0.5D + halfThickness;

            if ((cable.connectivity & 1) != 0)
            {
                minX = (double)x;
            }

            if ((cable.connectivity & 4) != 0)
            {
                minY = (double)y;
            }

            if ((cable.connectivity & 16) != 0)
            {
                minZ = (double)z;
            }

            if ((cable.connectivity & 2) != 0)
            {
                maxX = (double)(x + 1);
            }

            if ((cable.connectivity & 8) != 0)
            {
                maxY = (double)(y + 1);
            }

            if ((cable.connectivity & 32) != 0)
            {
                maxZ = (double)(z + 1);
            }

            return AxisAlignedBB.getAABBPool().getAABB(minX, minY, minZ, maxX, maxY, maxZ);
        }
    }

    public boolean isBlockNormalCube(World world, int x, int y, int z)
    {
        TileEntity te = world.getBlockTileEntity(x, y, z);

        if (te instanceof TileEntityCable)
        {
            TileEntityCable cable = (TileEntityCable)te;

            if (cable.foamed > 0)
            {
                return true;
            }
        }

        return false;
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float a, float b, float c)
    {
        ItemStack cur = entityPlayer.getCurrentEquippedItem();

        if (cur != null && (cur.itemID == Block.sand.blockID || cur.itemID == Ic2Items.constructionFoam.itemID))
        {
            TileEntity te = world.getBlockTileEntity(x, y, z);

            if (te instanceof TileEntityCable)
            {
                TileEntityCable cable = (TileEntityCable)te;

                if (cur.itemID == Block.sand.blockID && cable.foamed == 1 && cable.changeFoam((byte)2) || cur.itemID == Ic2Items.constructionFoam.itemID && cable.foamed == 0 && cable.changeFoam((byte)1))
                {
                    if (IC2.platform.isSimulating() && !entityPlayer.capabilities.isCreativeMode)
                    {
                        --cur.stackSize;

                        if (cur.stackSize <= 0)
                        {
                            entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem] = null;
                        }
                    }

                    return true;
                }
            }
        }

        return false;
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int srcBlockId)
    {
        super.onNeighborBlockChange(world, x, y, z, srcBlockId);

        if (IC2.platform.isSimulating())
        {
            TileEntity te = world.getBlockTileEntity(x, y, z);

            if (te instanceof TileEntityCable)
            {
                ((TileEntityCable)te).onNeighborBlockChange();
            }
        }
    }

    public boolean removeBlockByPlayer(World world, EntityPlayer entityPlayer, int x, int y, int z)
    {
        TileEntity te = world.getBlockTileEntity(x, y, z);

        if (te instanceof TileEntityCable)
        {
            TileEntityCable cable = (TileEntityCable)te;

            if (cable.foamed > 0)
            {
                cable.changeFoam((byte)0);
                world.notifyBlocksOfNeighborChange(x, y, z, super.blockID);
                return false;
            }
        }

        return world.setBlock(x, y, z, 0, 0, 3);
    }

    public void breakBlock(World world, int x, int y, int z, int blockId, int meta)
    {
        if (this.enableBreakBlock)
        {
            super.breakBlock(world, x, y, z, blockId, meta);
        }
    }

    public static int getCableColor(IBlockAccess iblockaccess, int i, int j, int k)
    {
        TileEntity te = iblockaccess.getBlockTileEntity(i, j, k);
        return te instanceof TileEntityCable ? ((TileEntityCable)te).color : 0;
    }

    public boolean recolourBlock(World world, int x, int y, int z, ForgeDirection side, int color)
    {
        color = BlockColored.getDyeFromBlock(color);
        return ((TileEntityCable)world.getBlockTileEntity(x, y, z)).changeColor(color);
    }

    @ForgeSubscribe
    public void colorBlock(PaintEvent event)
    {
        if (event.world.getBlockId(event.x, event.y, event.z) == super.blockID)
        {
            event.painted = ((TileEntityCable)event.world.getBlockTileEntity(event.x, event.y, event.z)).changeColor(event.color);
        }
    }

    public boolean canHarvestBlock(EntityPlayer player, int md)
    {
        return true;
    }

    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList ret = new ArrayList();
        TileEntity te = world.getBlockTileEntity(x, y, z);

        if (te instanceof TileEntityCable)
        {
            TileEntityCable cable = (TileEntityCable)te;
            ret.add(new ItemStack(Ic2Items.insulatedCopperCableItem.itemID, 1, cable.cableType));
        }
        else
        {
            ret.add(new ItemStack(Ic2Items.insulatedCopperCableItem.itemID, 1, metadata));
        }

        return ret;
    }

    public TileEntityBlock createTileEntity1(World world, int meta)
    {
        return (TileEntityBlock)(meta == 11 ? new TileEntityCableDetector((short)meta) : (meta == 12 ? new TileEntityCableSplitter((short)meta) : new TileEntityCable((short)meta)));
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int getRenderType()
    {
        return IC2.platform.getRenderId("cable");
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if (entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().getItem() instanceof ItemToolCutter)
        {
            ItemToolCutter.cutInsulationFrom(entityplayer.getCurrentEquippedItem(), world, i, j, k);
        }
    }

    public int isProvidingWeakPower(IBlockAccess iblockaccess, int x, int y, int z, int side)
    {
        TileEntity te = iblockaccess.getBlockTileEntity(x, y, z);
        return te instanceof TileEntityCableDetector ? (((TileEntityCableDetector)te).getActive() ? 15 : 0) : 0;
    }

    public void getSubBlocks(int i, CreativeTabs tabs, List itemList) {}

    public float getBlockHardness(World world, int x, int y, int z)
    {
        TileEntity te = world.getBlockTileEntity(x, y, z);

        if (te instanceof TileEntityCable)
        {
            TileEntityCable cable = (TileEntityCable)te;

            if (cable.foamed == 1)
            {
                return 0.01F;
            }

            if (cable.foamed == 2)
            {
                return 3.0F;
            }
        }

        return 0.2F;
    }

    public float getExplosionResistance(Entity exploder, World world, int x, int y, int z, double src_x, double src_y, double src_z)
    {
        TileEntity te = world.getBlockTileEntity(x, y, z);

        if (te instanceof TileEntityCable)
        {
            TileEntityCable cable = (TileEntityCable)te;

            if (cable.foamed == 2)
            {
                return 90.0F;
            }
        }

        return 6.0F;
    }

    public int getLightOpacity(World world, int x, int y, int z)
    {
        TileEntity te = world.getBlockTileEntity(x, y, z);

        if (te instanceof TileEntityCable)
        {
            TileEntityCable cable = (TileEntityCable)te;

            if (cable.foamed == 2)
            {
                return 255;
            }
        }

        return 0;
    }

    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int direction)
    {
        int meta = world.getBlockMetadata(x, y, z);
        return meta == 11 || meta == 12;
    }

    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        ArrayList ret = this.getBlockDropped(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        return ret.isEmpty() ? null : (ItemStack)ret.get(0);
    }

    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5)
    {
        TileEntity te = par1World.getBlockTileEntity(par2, par3, par4);

        if (te != null && te.getClass() == TileEntityCableDetector.class)
        {
            TileEntityCableDetector tec = (TileEntityCableDetector)te;
            return (int)Math.floor((double)((float)EnergyNet.getForWorld(par1World).getTotalEnergyEmitted(te) / ((float)tec.getConductorBreakdownEnergy() - 1.0F) * 15.0F));
        }
        else
        {
            return 0;
        }
    }

    @ForgeSubscribe
    public void onRetexture(RetextureEvent event)
    {
        TileEntity te = event.world.getBlockTileEntity(event.x, event.y, event.z);

        if (te instanceof TileEntityCable && ((TileEntityCable)te).retexture(event.side, event.referencedBlockId, event.referencedMeta, event.referencedSide))
        {
            event.applied = true;
        }
    }

    public TileEntity createTileEntity(World x0, int x1)
    {
        return this.createTileEntity1(x0, x1);
    }
}
