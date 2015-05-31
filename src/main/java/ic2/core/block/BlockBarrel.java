package ic2.core.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.init.InternalName;
import ic2.core.item.block.ItemBlockRare;
import ic2.core.util.StackUtil;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class BlockBarrel extends BlockMultiID
{
    private static final int textureIndexNormal = 0;
    private static final int textureIndexTap = 1;

    public BlockBarrel(Configuration config, InternalName internalName)
    {
        super(config, internalName, Material.wood, ItemBlockRare.class);
        this.setHardness(1.0F);
        this.setStepSound(Block.soundWoodFootstep);
        this.setCreativeTab((CreativeTabs)null);
        Ic2Items.blockBarrel = new ItemStack(this);
    }

    public String getTextureName(int index)
    {
        return index == 0 ? this.getUnlocalizedName() : (index == 1 ? this.getUnlocalizedName() + "." + InternalName.tap.name() : null);
    }

    public int getTextureIndex(int meta)
    {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z, int side)
    {
        TileEntity tileEntity = iBlockAccess.getBlockTileEntity(x, y, z);
        int subIndex = this.getTextureSubIndex(side, 3);
        return side > 1 && tileEntity instanceof TileEntityBarrel && ((TileEntityBarrel)tileEntity).treetapSide == side ? super.textures[1][subIndex] : super.textures[0][subIndex];
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float a, float b, float c)
    {
        return ((TileEntityBarrel)world.getBlockTileEntity(x, y, z)).rightclick(entityPlayer);
    }

    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
    {
        TileEntityBarrel barrel = (TileEntityBarrel)world.getBlockTileEntity(x, y, z);

        if (barrel.treetapSide > 1)
        {
            if (IC2.platform.isSimulating())
            {
                StackUtil.dropAsEntity(world, x, y, z, new ItemStack(Ic2Items.treetap.getItem()));
            }

            barrel.treetapSide = 0;
            barrel.update();
            barrel.drainLiquid(1);
        }
        else
        {
            if (IC2.platform.isSimulating())
            {
                StackUtil.dropAsEntity(world, x, y, z, new ItemStack(Ic2Items.barrel.getItem(), 1, barrel.calculateMetaValue()));
            }

            world.setBlock(x, y, z, Ic2Items.scaffold.itemID, 0, 3);
        }
    }

    public TileEntity createTileEntity(World world, int metaData)
    {
        return new TileEntityBarrel();
    }

    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList re = new ArrayList();
        re.add(new ItemStack(Ic2Items.scaffold.getItem()));
        re.add(new ItemStack(Ic2Items.barrel.getItem(), 1, 0));
        return re;
    }
}
