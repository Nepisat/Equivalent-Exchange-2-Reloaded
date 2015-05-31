package ic2.core.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.init.InternalName;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.Configuration;

public class BlockTexGlass extends BlockSimple
{
    public BlockTexGlass(Configuration config, InternalName internalName)
    {
        super(config, internalName, Material.glass);
        this.setHardness(5.0F);
        this.setResistance(150.0F);
        this.setStepSound(Block.soundGlassFootstep);
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, int x, int y, int z, int side)
    {
        return iBlockAccess.getBlockId(x, y, z) == super.blockID ? false : super.shouldSideBeRendered(iBlockAccess, x, y, z, side);
    }
}
