package ic2.core.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

@SideOnly(Side.CLIENT)
public class RenderBlockCrop extends RenderBlock
{
    public static void renderBlockCropsImpl(Icon icon, int x, int y, int z)
    {
        Tessellator tessellator = Tessellator.instance;
        double yBase = (double)y - 0.0625D;
        double uStart = (double)icon.getInterpolatedU(0.0D);
        double uEnd = (double)icon.getInterpolatedU(16.0D);
        double vStart = (double)icon.getInterpolatedV(0.0D);
        double vEnd = (double)icon.getInterpolatedV(16.0D);
        double x1 = (double)x + 0.5D - 0.25D;
        double x2 = (double)x + 0.5D + 0.25D;
        double z1 = (double)z + 0.5D - 0.5D;
        double z2 = (double)z + 0.5D + 0.5D;
        tessellator.addVertexWithUV(x1, yBase + 1.0D, z1, uStart, vStart);
        tessellator.addVertexWithUV(x1, yBase + 0.0D, z1, uStart, vEnd);
        tessellator.addVertexWithUV(x1, yBase + 0.0D, z2, uEnd, vEnd);
        tessellator.addVertexWithUV(x1, yBase + 1.0D, z2, uEnd, vStart);
        tessellator.addVertexWithUV(x1, yBase + 1.0D, z2, uStart, vStart);
        tessellator.addVertexWithUV(x1, yBase + 0.0D, z2, uStart, vEnd);
        tessellator.addVertexWithUV(x1, yBase + 0.0D, z1, uEnd, vEnd);
        tessellator.addVertexWithUV(x1, yBase + 1.0D, z1, uEnd, vStart);
        tessellator.addVertexWithUV(x2, yBase + 1.0D, z2, uStart, vStart);
        tessellator.addVertexWithUV(x2, yBase + 0.0D, z2, uStart, vEnd);
        tessellator.addVertexWithUV(x2, yBase + 0.0D, z1, uEnd, vEnd);
        tessellator.addVertexWithUV(x2, yBase + 1.0D, z1, uEnd, vStart);
        tessellator.addVertexWithUV(x2, yBase + 1.0D, z1, uStart, vStart);
        tessellator.addVertexWithUV(x2, yBase + 0.0D, z1, uStart, vEnd);
        tessellator.addVertexWithUV(x2, yBase + 0.0D, z2, uEnd, vEnd);
        tessellator.addVertexWithUV(x2, yBase + 1.0D, z2, uEnd, vStart);
        x1 = (double)x + 0.5D - 0.5D;
        x2 = (double)x + 0.5D + 0.5D;
        z1 = (double)z + 0.5D - 0.25D;
        z2 = (double)z + 0.5D + 0.25D;
        tessellator.addVertexWithUV(x1, yBase + 1.0D, z1, uStart, vStart);
        tessellator.addVertexWithUV(x1, yBase + 0.0D, z1, uStart, vEnd);
        tessellator.addVertexWithUV(x2, yBase + 0.0D, z1, uEnd, vEnd);
        tessellator.addVertexWithUV(x2, yBase + 1.0D, z1, uEnd, vStart);
        tessellator.addVertexWithUV(x2, yBase + 1.0D, z1, uStart, vStart);
        tessellator.addVertexWithUV(x2, yBase + 0.0D, z1, uStart, vEnd);
        tessellator.addVertexWithUV(x1, yBase + 0.0D, z1, uEnd, vEnd);
        tessellator.addVertexWithUV(x1, yBase + 1.0D, z1, uEnd, vStart);
        tessellator.addVertexWithUV(x2, yBase + 1.0D, z2, uStart, vStart);
        tessellator.addVertexWithUV(x2, yBase + 0.0D, z2, uStart, vEnd);
        tessellator.addVertexWithUV(x1, yBase + 0.0D, z2, uEnd, vEnd);
        tessellator.addVertexWithUV(x1, yBase + 1.0D, z2, uEnd, vStart);
        tessellator.addVertexWithUV(x1, yBase + 1.0D, z2, uStart, vStart);
        tessellator.addVertexWithUV(x1, yBase + 0.0D, z2, uStart, vEnd);
        tessellator.addVertexWithUV(x2, yBase + 0.0D, z2, uEnd, vEnd);
        tessellator.addVertexWithUV(x2, yBase + 1.0D, z2, uEnd, vStart);
    }

    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {}

    public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        super.renderWorldBlock(blockAccess, x, y, z, block, modelId, renderer);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, x, y, z));
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        renderBlockCropsImpl(block.getBlockTexture(blockAccess, x, y, z, 0), x, y, z);
        return true;
    }

    public boolean shouldRender3DInInventory()
    {
        return false;
    }
}
