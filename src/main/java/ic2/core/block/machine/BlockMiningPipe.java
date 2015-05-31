package ic2.core.block.machine;

import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.block.BlockSimple;
import ic2.core.init.InternalName;
import ic2.core.item.block.ItemBlockRare;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class BlockMiningPipe extends BlockSimple
{
    public BlockMiningPipe(Configuration config, InternalName internalName)
    {
        super(config, internalName, Material.iron, ItemBlockRare.class);
        this.setHardness(6.0F);
        this.setResistance(10.0F);
        Ic2Items.miningPipe = new ItemStack(this);
    }

    public String getTextureFolder()
    {
        return "machine";
    }

    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isBlockNormalCube(World world, int i, int j, int k)
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int getRenderType()
    {
        return IC2.platform.getRenderId("miningPipe");
    }

    public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, int par2, int par3, int par4)
    {
        this.setBlockBounds(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
    }
}
