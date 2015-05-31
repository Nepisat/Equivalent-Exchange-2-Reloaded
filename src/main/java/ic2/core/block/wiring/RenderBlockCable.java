package ic2.core.block.wiring;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.block.RenderBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

@SideOnly(Side.CLIENT)
public class RenderBlockCable extends RenderBlock
{
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {}

    public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int modelId, RenderBlocks renderblocks)
    {
        super.renderWorldBlock(blockAccess, x, y, z, block, modelId, renderblocks);
        TileEntity te = blockAccess.getBlockTileEntity(x, y, z);

        if (!(te instanceof TileEntityCable))
        {
            return true;
        }
        else
        {
            TileEntityCable cable = (TileEntityCable)te;

            if (cable.foamed > 0)
            {
                renderblocks.renderStandardBlock(block, x, y, z);
            }
            else
            {
                float th = cable.getCableThickness();
                float sp = (1.0F - th) / 2.0F;
                byte connectivity = cable.connectivity;
                byte renderSide = cable.renderSide;
                Icon[] textures = new Icon[6];

                for (int var23 = 0; var23 < 6; ++var23)
                {
                    Icon var22 = block.getBlockTexture(blockAccess, x, y, z, var23);

                    if (var22 != null)
                    {
                        textures[var23] = var22;
                    }
                    else
                    {
                        textures[var23] = getMissingIcon(TextureMap.locationBlocksTexture);
                    }
                }

                Tessellator var231 = Tessellator.instance;
                double var221 = (double)x;
                double yD = (double)y;
                double zD = (double)z;
                var231.setBrightness(block.getMixedBrightnessForBlock(blockAccess, x, y, z));

                if (connectivity == 0)
                {
                    block.setBlockBounds(sp, sp, sp, sp + th, sp + th, sp + th);
                    renderblocks.setRenderBoundsFromBlock(block);
                    var231.setColorOpaque_F(0.5F, 0.5F, 0.5F);
                    renderblocks.renderFaceYNeg(block, var221, yD, zD, textures[0]);
                    var231.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                    renderblocks.renderFaceYPos(block, var221, yD, zD, textures[1]);
                    var231.setColorOpaque_F(0.8F, 0.8F, 0.8F);
                    renderblocks.renderFaceZNeg(block, var221, yD, zD, textures[2]);
                    renderblocks.renderFaceZPos(block, var221, (double)y, zD, textures[3]);
                    var231.setColorOpaque_F(0.6F, 0.6F, 0.6F);
                    renderblocks.renderFaceXNeg(block, var221, yD, zD, textures[4]);
                    renderblocks.renderFaceXPos(block, var221, yD, zD, textures[5]);
                }
                else if (connectivity == 3)
                {
                    block.setBlockBounds(0.0F, sp, sp, 1.0F, sp + th, sp + th);
                    renderblocks.setRenderBoundsFromBlock(block);
                    var231.setColorOpaque_F(0.5F, 0.5F, 0.5F);
                    renderblocks.renderFaceYNeg(block, var221, yD, zD, textures[0]);
                    var231.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                    renderblocks.renderFaceYPos(block, var221, yD, zD, textures[1]);
                    var231.setColorOpaque_F(0.8F, 0.8F, 0.8F);
                    renderblocks.renderFaceZNeg(block, var221, yD, zD, textures[2]);
                    renderblocks.renderFaceZPos(block, var221, (double)y, zD, textures[3]);

                    if ((renderSide & 1) != 0)
                    {
                        var231.setColorOpaque_F(0.6F, 0.6F, 0.6F);
                        renderblocks.renderFaceXNeg(block, var221, yD, zD, textures[4]);
                    }

                    if ((renderSide & 2) != 0)
                    {
                        var231.setColorOpaque_F(0.6F, 0.6F, 0.6F);
                        renderblocks.renderFaceXPos(block, var221, yD, zD, textures[5]);
                    }
                }
                else if (connectivity == 12)
                {
                    block.setBlockBounds(sp, 0.0F, sp, sp + th, 1.0F, sp + th);
                    renderblocks.setRenderBoundsFromBlock(block);
                    var231.setColorOpaque_F(0.8F, 0.8F, 0.8F);
                    renderblocks.renderFaceZNeg(block, var221, yD, zD, textures[2]);
                    renderblocks.renderFaceZPos(block, var221, (double)y, zD, textures[3]);
                    var231.setColorOpaque_F(0.6F, 0.6F, 0.6F);
                    renderblocks.renderFaceXNeg(block, var221, yD, zD, textures[4]);
                    renderblocks.renderFaceXPos(block, var221, yD, zD, textures[5]);

                    if ((renderSide & 4) != 0)
                    {
                        var231.setColorOpaque_F(0.5F, 0.5F, 0.5F);
                        renderblocks.renderFaceYNeg(block, var221, yD, zD, textures[0]);
                    }

                    if ((renderSide & 8) != 0)
                    {
                        var231.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                        renderblocks.renderFaceYPos(block, var221, yD, zD, textures[1]);
                    }
                }
                else if (connectivity == 48)
                {
                    block.setBlockBounds(sp, sp, 0.0F, sp + th, sp + th, 1.0F);
                    renderblocks.setRenderBoundsFromBlock(block);
                    var231.setColorOpaque_F(0.5F, 0.5F, 0.5F);
                    renderblocks.renderFaceYNeg(block, var221, yD, zD, textures[0]);
                    var231.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                    renderblocks.renderFaceYPos(block, var221, yD, zD, textures[1]);
                    var231.setColorOpaque_F(0.6F, 0.6F, 0.6F);
                    renderblocks.renderFaceXNeg(block, var221, yD, zD, textures[4]);
                    renderblocks.renderFaceXPos(block, var221, yD, zD, textures[5]);

                    if ((renderSide & 16) != 0)
                    {
                        var231.setColorOpaque_F(0.8F, 0.8F, 0.8F);
                        renderblocks.renderFaceZNeg(block, var221, (double)y, zD, textures[2]);
                    }

                    if ((renderSide & 32) != 0)
                    {
                        var231.setColorOpaque_F(0.8F, 0.8F, 0.8F);
                        renderblocks.renderFaceZPos(block, var221, yD, zD, textures[3]);
                    }
                }
                else
                {
                    if ((connectivity & 1) == 0)
                    {
                        block.setBlockBounds(sp, sp, sp, sp + th, sp + th, sp + th);
                        renderblocks.setRenderBoundsFromBlock(block);
                        var231.setColorOpaque_F(0.6F, 0.6F, 0.6F);
                        renderblocks.renderFaceXNeg(block, var221, yD, zD, textures[4]);
                    }
                    else
                    {
                        block.setBlockBounds(0.0F, sp, sp, sp, sp + th, sp + th);
                        renderblocks.setRenderBoundsFromBlock(block);
                        var231.setColorOpaque_F(0.5F, 0.5F, 0.5F);
                        renderblocks.renderFaceYNeg(block, var221, yD, zD, textures[0]);
                        var231.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                        renderblocks.renderFaceYPos(block, var221, yD, zD, textures[1]);
                        var231.setColorOpaque_F(0.8F, 0.8F, 0.8F);
                        renderblocks.renderFaceZNeg(block, var221, yD, zD, textures[2]);
                        renderblocks.renderFaceZPos(block, var221, (double)y, zD, textures[3]);

                        if ((renderSide & 1) != 0)
                        {
                            var231.setColorOpaque_F(0.6F, 0.6F, 0.6F);
                            renderblocks.renderFaceXNeg(block, var221, yD, zD, textures[4]);
                        }
                    }

                    if ((connectivity & 2) == 0)
                    {
                        block.setBlockBounds(sp, sp, sp, sp + th, sp + th, sp + th);
                        renderblocks.setRenderBoundsFromBlock(block);
                        var231.setColorOpaque_F(0.6F, 0.6F, 0.6F);
                        renderblocks.renderFaceXPos(block, var221, yD, zD, textures[5]);
                    }
                    else
                    {
                        block.setBlockBounds(sp + th, sp, sp, 1.0F, sp + th, sp + th);
                        renderblocks.setRenderBoundsFromBlock(block);
                        var231.setColorOpaque_F(0.5F, 0.5F, 0.5F);
                        renderblocks.renderFaceYNeg(block, var221, yD, zD, textures[0]);
                        var231.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                        renderblocks.renderFaceYPos(block, var221, yD, zD, textures[1]);
                        var231.setColorOpaque_F(0.8F, 0.8F, 0.8F);
                        renderblocks.renderFaceZNeg(block, var221, yD, zD, textures[2]);
                        renderblocks.renderFaceZPos(block, var221, (double)y, zD, textures[3]);

                        if ((renderSide & 2) != 0)
                        {
                            var231.setColorOpaque_F(0.6F, 0.6F, 0.6F);
                            renderblocks.renderFaceXPos(block, var221, yD, zD, textures[5]);
                        }
                    }

                    if ((connectivity & 4) == 0)
                    {
                        block.setBlockBounds(sp, sp, sp, sp + th, sp + th, sp + th);
                        renderblocks.setRenderBoundsFromBlock(block);
                        var231.setColorOpaque_F(0.5F, 0.5F, 0.5F);
                        renderblocks.renderFaceYNeg(block, var221, yD, zD, textures[0]);
                    }
                    else
                    {
                        block.setBlockBounds(sp, 0.0F, sp, sp + th, sp, sp + th);
                        renderblocks.setRenderBoundsFromBlock(block);
                        var231.setColorOpaque_F(0.8F, 0.8F, 0.8F);
                        renderblocks.renderFaceZNeg(block, var221, yD, zD, textures[2]);
                        renderblocks.renderFaceZPos(block, var221, (double)y, zD, textures[3]);
                        var231.setColorOpaque_F(0.6F, 0.6F, 0.6F);
                        renderblocks.renderFaceXNeg(block, var221, yD, zD, textures[4]);
                        renderblocks.renderFaceXPos(block, var221, yD, zD, textures[5]);

                        if ((renderSide & 4) != 0)
                        {
                            var231.setColorOpaque_F(0.5F, 0.5F, 0.5F);
                            renderblocks.renderFaceYNeg(block, var221, yD, zD, textures[0]);
                        }
                    }

                    if ((connectivity & 8) == 0)
                    {
                        block.setBlockBounds(sp, sp, sp, sp + th, sp + th, sp + th);
                        renderblocks.setRenderBoundsFromBlock(block);
                        var231.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                        renderblocks.renderFaceYPos(block, var221, yD, zD, textures[1]);
                    }
                    else
                    {
                        block.setBlockBounds(sp, sp + th, sp, sp + th, 1.0F, sp + th);
                        renderblocks.setRenderBoundsFromBlock(block);
                        var231.setColorOpaque_F(0.8F, 0.8F, 0.8F);
                        renderblocks.renderFaceZNeg(block, var221, yD, zD, textures[2]);
                        renderblocks.renderFaceZPos(block, var221, (double)y, zD, textures[3]);
                        var231.setColorOpaque_F(0.6F, 0.6F, 0.6F);
                        renderblocks.renderFaceXNeg(block, var221, yD, zD, textures[4]);
                        renderblocks.renderFaceXPos(block, var221, yD, zD, textures[5]);

                        if ((renderSide & 8) != 0)
                        {
                            var231.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                            renderblocks.renderFaceYPos(block, var221, yD, zD, textures[1]);
                        }
                    }

                    if ((connectivity & 16) == 0)
                    {
                        block.setBlockBounds(sp, sp, sp, sp + th, sp + th, sp + th);
                        renderblocks.setRenderBoundsFromBlock(block);
                        var231.setColorOpaque_F(0.8F, 0.8F, 0.8F);
                        renderblocks.renderFaceZNeg(block, var221, (double)y, zD, textures[2]);
                    }
                    else
                    {
                        block.setBlockBounds(sp, sp, 0.0F, sp + th, sp + th, sp);
                        renderblocks.setRenderBoundsFromBlock(block);
                        var231.setColorOpaque_F(0.5F, 0.5F, 0.5F);
                        renderblocks.renderFaceYNeg(block, var221, yD, zD, textures[0]);
                        var231.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                        renderblocks.renderFaceYPos(block, var221, yD, zD, textures[1]);
                        var231.setColorOpaque_F(0.6F, 0.6F, 0.6F);
                        renderblocks.renderFaceXNeg(block, var221, yD, zD, textures[4]);
                        renderblocks.renderFaceXPos(block, var221, yD, zD, textures[5]);

                        if ((renderSide & 16) != 0)
                        {
                            var231.setColorOpaque_F(0.8F, 0.8F, 0.8F);
                            renderblocks.renderFaceZNeg(block, var221, (double)y, zD, textures[2]);
                        }
                    }

                    if ((connectivity & 32) == 0)
                    {
                        block.setBlockBounds(sp, sp, sp, sp + th, sp + th, sp + th);
                        renderblocks.setRenderBoundsFromBlock(block);
                        var231.setColorOpaque_F(0.8F, 0.8F, 0.8F);
                        renderblocks.renderFaceZPos(block, var221, yD, zD, textures[3]);
                    }
                    else
                    {
                        block.setBlockBounds(sp, sp, sp + th, sp + th, sp + th, 1.0F);
                        renderblocks.setRenderBoundsFromBlock(block);
                        var231.setColorOpaque_F(0.5F, 0.5F, 0.5F);
                        renderblocks.renderFaceYNeg(block, var221, yD, zD, textures[0]);
                        var231.setColorOpaque_F(1.0F, 1.0F, 1.0F);
                        renderblocks.renderFaceYPos(block, var221, yD, zD, textures[1]);
                        var231.setColorOpaque_F(0.6F, 0.6F, 0.6F);
                        renderblocks.renderFaceXNeg(block, var221, yD, zD, textures[4]);
                        renderblocks.renderFaceXPos(block, var221, yD, zD, textures[5]);

                        if ((renderSide & 32) != 0)
                        {
                            var231.setColorOpaque_F(0.8F, 0.8F, 0.8F);
                            renderblocks.renderFaceZPos(block, var221, yD, zD, textures[3]);
                        }
                    }
                }

                block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                renderblocks.setRenderBoundsFromBlock(block);
            }

            return true;
        }
    }

    public boolean shouldRender3DInInventory()
    {
        return false;
    }
}
