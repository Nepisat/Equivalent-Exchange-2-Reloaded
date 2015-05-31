package ic2.core.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

public abstract class RenderBlock implements ISimpleBlockRenderingHandler
{
    private final int renderId = RenderingRegistry.getNextAvailableRenderId();

    public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int modelId, RenderBlocks renderblocks)
    {
        ((BlockMultiID)block).onRender(blockAccess, x, y, z);
        return false;
    }

    public abstract void renderInventoryBlock(Block var1, int var2, int var3, RenderBlocks var4);

    public boolean shouldRender3DInInventory()
    {
        return true;
    }

    public int getRenderId()
    {
        return this.renderId;
    }

    public static Icon getMissingIcon(ResourceLocation textureSheet)
    {
        return ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(textureSheet)).getAtlasSprite("missingno");
    }
}
