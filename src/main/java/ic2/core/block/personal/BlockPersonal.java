package ic2.core.block.personal;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.block.BlockMultiID;
import ic2.core.block.TileEntityBlock;
import ic2.core.init.InternalName;
import ic2.core.item.block.ItemPersonalBlock;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class BlockPersonal extends BlockMultiID
{
    public static Class <? extends TileEntityPersonalChest > tileEntityPersonalChestClass = TileEntityPersonalChest.class;

    public BlockPersonal(Configuration config, InternalName internalName)
    {
        super(config, internalName, Material.iron, ItemPersonalBlock.class);
        this.setBlockUnbreakable();
        this.setResistance(6000000.0F);
        this.setStepSound(Block.soundMetalFootstep);
        Block.canBlockGrass[super.blockID] = false;
        Ic2Items.personalSafe = new ItemStack(this, 1, 0);
        Ic2Items.tradeOMat = new ItemStack(this, 1, 1);
        Ic2Items.energyOMat = new ItemStack(this, 1, 2);
    }

    public String getTextureFolder()
    {
        return "personal";
    }

    public int idDropped(int meta, Random random, int j)
    {
        return super.blockID;
    }

    public int damageDropped(int meta)
    {
        return meta;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int getRenderType()
    {
        return IC2.platform.getRenderId("personal");
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
        if (IC2.platform.isSimulating() && IC2.platform.isRendering())
        {
            return super.getBlockDropped(world, x, y, z, metadata, fortune);
        }
        else
        {
            ArrayList ret = new ArrayList();
            ret.add(new ItemStack(super.blockID, 1, metadata));
            return ret;
        }
    }

    public TileEntityBlock createTileEntity1(World world, int meta)
    {
        try
        {
            switch (meta)
            {
                case 0:
                    return (TileEntityBlock)tileEntityPersonalChestClass.newInstance();

                case 1:
                    return new TileEntityTradeOMat();

                case 2:
                    return new TileEntityEnergyOMat();

                default:
                    return null;
            }
        }
        catch (Exception var4)
        {
            throw new RuntimeException(var4);
        }
    }

    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float a, float b, float c)
    {
        if (entityplayer.isSneaking())
        {
            return false;
        }
        else
        {
            int meta = world.getBlockMetadata(i, j, k);
            TileEntity te = world.getBlockTileEntity(i, j, k);
            return IC2.platform.isSimulating() && meta != 1 && meta != 2 && te instanceof IPersonalBlock && !((IPersonalBlock)te).permitsAccess(entityplayer) ? false : super.onBlockActivated(world, i, j, k, entityplayer, side, a, b, c);
        }
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack)
    {
        return stack.getItemDamage() == 0 ? EnumRarity.uncommon : EnumRarity.common;
    }

    public boolean canDragonDestroy(World world, int x, int y, int z)
    {
        return false;
    }

    public TileEntity createTileEntity(World x0, int x1)
    {
        return this.createTileEntity1(x0, x1);
    }
}
