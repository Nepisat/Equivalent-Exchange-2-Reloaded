package ic2.core.util;

import ic2.core.util.Liquid$LiquidData;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;

public class Liquid
{
    public static Liquid$LiquidData getLiquid(World world, int x, int y, int z)
    {
        int blockId = world.getBlockId(x, y, z);
        Fluid liquid = null;
        boolean isSource = false;

        if (Block.blocksList[blockId] instanceof IFluidBlock)
        {
            IFluidBlock block = (IFluidBlock)Block.blocksList[blockId];
            liquid = block.getFluid();
            isSource = block.canDrain(world, x, y, z);
        }
        else if (blockId != Block.waterStill.blockID && blockId != Block.waterMoving.blockID)
        {
            if (blockId == Block.lavaStill.blockID || blockId == Block.lavaMoving.blockID)
            {
                liquid = FluidRegistry.LAVA;
                isSource = world.getBlockMetadata(x, y, z) == 0;
            }
        }
        else
        {
            liquid = FluidRegistry.WATER;
            isSource = world.getBlockMetadata(x, y, z) == 0;
        }

        return liquid != null ? new Liquid$LiquidData(liquid, isSource) : null;
    }
}
