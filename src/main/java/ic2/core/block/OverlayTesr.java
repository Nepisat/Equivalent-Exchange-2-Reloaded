package ic2.core.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class OverlayTesr extends TileEntitySpecialRenderer
{
    private final RenderBlocks renderBlocks = new RenderBlocks();

    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTick)
    {
        TileEntityBlock te = (TileEntityBlock)tileEntity;
        int mask = te.getTesrMask();

        if (mask != 0)
        {
            BlockMultiID block = (BlockMultiID)tileEntity.getBlockType();
            block.renderMask = mask;
            GL11.glPushAttrib(64);
            GL11.glPushMatrix();
            RenderHelper.disableStandardItemLighting();
            GL11.glShadeModel(GL11.GL_SMOOTH);
            this.bindTexture(TextureMap.locationBlocksTexture);
            float zScale = 1.001F;
            GL11.glTranslatef((float)(x + 0.5D), (float)(y + 0.5D), (float)(z + 0.5D));
            GL11.glScalef(zScale, zScale, zScale);
            GL11.glTranslatef((float)(-(x + 0.5D)), (float)(-(y + 0.5D)), (float)(-(z + 0.5D)));
            float f = 1.000001F;
            GL11.glTranslatef(-8.0F, -8.0F, -8.0F);
            GL11.glScalef(f, f, f);
            GL11.glTranslatef(8.0F, 8.0F, 8.0F);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.setTranslation(x - (double)tileEntity.xCoord, y - (double)tileEntity.yCoord, z - (double)tileEntity.zCoord);
            this.renderBlocks.blockAccess = te.worldObj;
            this.renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
            this.renderBlocks.renderStandardBlock(block, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
            tessellator.draw();
            tessellator.setTranslation(0.0D, 0.0D, 0.0D);
            GL11.glPopMatrix();
            GL11.glPopAttrib();
            block.renderMask = 63;

            if (--te.tesrTtl == 0)
            {
                tileEntity.worldObj.markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
            }
        }
    }
}
