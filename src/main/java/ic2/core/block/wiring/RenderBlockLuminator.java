package ic2.core.block.wiring;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.block.RenderBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBlockLuminator extends RenderBlock
{
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderblocks)
    {
        Tessellator tessellator = Tessellator.instance;
        float f4 = 0.125F;
        block.setBlockBounds(0.5F - f4, 0.0F, 0.5F - f4, 0.5F + f4, 1.0F, 0.5F + f4);
        renderblocks.setRenderBoundsFromBlock(block);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderblocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderblocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderblocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderblocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderblocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderblocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderblocks.setRenderBoundsFromBlock(block);
    }

    public boolean renderWorldBlock(IBlockAccess iblockaccess, int x, int y, int z, Block block, int modelId, RenderBlocks renderblocks)
    {
        super.renderWorldBlock(iblockaccess, x, y, z, block, modelId, renderblocks);
        float[] box = BlockLuminator.getBoxOfLuminator(iblockaccess, x, y, z);
        block.setBlockBounds(box[0], box[1], box[2], box[3], box[4], box[5]);
        renderblocks.setRenderBoundsFromBlock(block);
        renderblocks.renderStandardBlock(block, x, y, z);
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderblocks.setRenderBoundsFromBlock(block);
        return true;
    }
}
