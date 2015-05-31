package ic2.core.block.wiring;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.block.BlockMultiID;
import ic2.core.block.TileEntityBlock;
import ic2.core.init.InternalName;
import ic2.core.item.block.ItemElectricBlock;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;

public class BlockElectric extends BlockMultiID
{
    public BlockElectric(Configuration config, InternalName internalName)
    {
        super(config, internalName, Material.iron, ItemElectricBlock.class);
        this.setHardness(1.5F);
        this.setStepSound(Block.soundMetalFootstep);
        Ic2Items.batBox = new ItemStack(this, 1, 0);
        Ic2Items.mfeUnit = new ItemStack(this, 1, 1);
        Ic2Items.mfsUnit = new ItemStack(this, 1, 2);
        Ic2Items.lvTransformer = new ItemStack(this, 1, 3);
        Ic2Items.mvTransformer = new ItemStack(this, 1, 4);
        Ic2Items.hvTransformer = new ItemStack(this, 1, 5);
    }

    public String getTextureFolder()
    {
        return "wiring";
    }

    public int idDropped(int meta, Random random, int j)
    {
        switch (meta)
        {
            case 0:
                return super.blockID;

            case 3:
                return super.blockID;

            default:
                return Ic2Items.machine.itemID;
        }
    }

    public int damageDropped(int meta)
    {
        switch (meta)
        {
            case 0:
                return meta;

            case 3:
                return meta;

            default:
                return Ic2Items.machine.getItemDamage();
        }
    }

    public int quantityDropped(Random random)
    {
        return 1;
    }

    public int isProvidingWeakPower(IBlockAccess iblockaccess, int x, int y, int z, int side)
    {
        TileEntity te = iblockaccess.getBlockTileEntity(x, y, z);

        if (te instanceof TileEntityElectricBlock)
        {
            TileEntityElectricBlock electricBlock = (TileEntityElectricBlock)te;
            return electricBlock.isEmittingRedstone() ? 15 : 0;
        }
        else
        {
            return 0;
        }
    }

    public boolean isBlockNormalCube(World world, int i, int j, int k)
    {
        return false;
    }

    public boolean isBlockOpaqueCube(World world, int i, int j, int k)
    {
        return true;
    }

    public boolean isBlockSolid(IBlockAccess world, int i, int j, int k, int l)
    {
        return true;
    }

    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
    {
        return true;
    }

    public TileEntityBlock createTileEntity1(World world, int meta)
    {
        switch (meta)
        {
            case 0:
                return new TileEntityElectricBatBox();

            case 1:
                return new TileEntityElectricMFE();

            case 2:
                return new TileEntityElectricMFSU();

            case 3:
                return new TileEntityTransformerLV();

            case 4:
                return new TileEntityTransformerMV();

            case 5:
                return new TileEntityTransformerHV();

            default:
                return null;
        }
    }

    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entityliving, ItemStack itemStack)
    {
        if (IC2.platform.isSimulating())
        {
            TileEntityBlock te = (TileEntityBlock)world.getBlockTileEntity(i, j, k);

            if (entityliving == null)
            {
                te.setFacing((short)1);
            }
            else
            {
                int yaw = MathHelper.floor_double((double)(entityliving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
                int pitch = Math.round(entityliving.rotationPitch);

                if (pitch >= 65)
                {
                    te.setFacing((short)1);
                }
                else if (pitch <= -65)
                {
                    te.setFacing((short)0);
                }
                else
                {
                    switch (yaw)
                    {
                        case 0:
                            te.setFacing((short)2);
                            break;

                        case 1:
                            te.setFacing((short)5);
                            break;

                        case 2:
                            te.setFacing((short)3);
                            break;

                        case 3:
                            te.setFacing((short)4);
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack)
    {
        return stack.getItemDamage() != 2 && stack.getItemDamage() != 5 ? EnumRarity.common : EnumRarity.uncommon;
    }

    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5)
    {
        TileEntity te = par1World.getBlockTileEntity(par2, par3, par4);

        if (te instanceof TileEntityElectricBlock)
        {
            TileEntityElectricBlock teb = (TileEntityElectricBlock)te;
            return (int)Math.floor((double)((float)teb.energy / (float)teb.maxStorage * 15.0F));
        }
        else
        {
            return 0;
        }
    }

    public TileEntity createTileEntity(World x0, int x1)
    {
        return this.createTileEntity1(x0, x1);
    }
}
