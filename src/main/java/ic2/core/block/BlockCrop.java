package ic2.core.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.crops.Crops;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.init.InternalName;
import ic2.core.item.block.ItemBlockRare;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class BlockCrop extends BlockMultiID
{
    public static TileEntityCrop tempStore;
    private static final int textureIndexStick = 0;
    private static final int textureIndexStickUpgraded = 1;

    public BlockCrop(Configuration config, InternalName internalName)
    {
        super(config, internalName, Material.plants, ItemBlockRare.class);
        this.setHardness(0.8F);
        this.setResistance(0.2F);
        this.setStepSound(Block.soundGrassFootstep);
        Ic2Items.crop = new ItemStack(this, 1, 0);
    }

    public String getTextureFolder()
    {
        return "crop";
    }

    public int getTextureIndex(int meta)
    {
        return meta != 0 && meta != 1 ? 0 : meta;
    }

    public String getTextureName(int index)
    {
        switch (index)
        {
            case 0:
                return this.getUnlocalizedName() + "." + InternalName.stick.name();

            case 1:
                return this.getUnlocalizedName() + "." + InternalName.stick.name() + "." + InternalName.upgraded.name();

            default:
                return null;
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        super.registerIcons(iconRegister);
        Crops.instance.startSpriteRegistration(iconRegister);
    }

    public TileEntity createTileEntity(World world, int metaData)
    {
        return new TileEntityCrop();
    }

    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z, int side)
    {
        TileEntity te = iBlockAccess.getBlockTileEntity(x, y, z);

        if (te instanceof TileEntityCrop)
        {
            TileEntityCrop tileEntityCrop = (TileEntityCrop)te;
            return tileEntityCrop.id < 0 ? (!tileEntityCrop.upgraded ? this.getIcon(side, 0) : this.getIcon(side, 1)) : tileEntityCrop.crop().getSprite(tileEntityCrop);
        }
        else
        {
            return super.getBlockTexture(iBlockAccess, x, y, z, side);
        }
    }

    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        return world.getBlockId(i, j - 1, k) == Block.tilledField.blockID && super.canPlaceBlockAt(world, i, j, k);
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        super.onNeighborBlockChange(world, i, j, k, l);

        if (world.getBlockId(i, j - 1, k) != Block.tilledField.blockID)
        {
            world.setBlock(i, j, k, 0, 0, 7);
            this.dropBlockAsItem(world, i, j, k, 0, 0);
        }
        else
        {
            ((TileEntityCrop)world.getBlockTileEntity(i, j, k)).onNeighbourChange();
        }
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        double d = 0.2D;
        return AxisAlignedBB.getBoundingBox(d, 0.0D, d, 1.0D - d, 0.7D, 1.0D - d);
    }

    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        ((TileEntityCrop)world.getBlockTileEntity(i, j, k)).onEntityCollision(entity);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int getRenderType()
    {
        return IC2.platform.getRenderId("crop");
    }

    public int isProvidingWeakPower(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        return ((TileEntityCrop)iblockaccess.getBlockTileEntity(i, j, k)).emitRedstone();
    }

    public void breakBlock(World world, int i, int j, int k, int a, int b)
    {
        if (world != null)
        {
            tempStore = (TileEntityCrop)world.getBlockTileEntity(i, j, k);
        }

        super.breakBlock(world, i, j, k, a, b);
    }

    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion)
    {
        if (tempStore != null)
        {
            tempStore.onBlockDestroyed();
        }
    }

    public int getLightValue(IBlockAccess iblockaccess, int i, int j, int k)
    {
        return ((TileEntityCrop)iblockaccess.getBlockTileEntity(i, j, k)).getEmittedLight();
    }

    public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        ((TileEntityCrop)world.getBlockTileEntity(i, j, k)).leftclick(entityplayer);
    }

    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float a, float b, float c)
    {
        return ((TileEntityCrop)world.getBlockTileEntity(i, j, k)).rightclick(entityplayer);
    }
}
