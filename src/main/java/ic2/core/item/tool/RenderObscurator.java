package ic2.core.item.tool;

import ic2.core.block.RenderBlock;
import ic2.core.util.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import org.lwjgl.opengl.GL11;

public class RenderObscurator implements IItemRenderer
{
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return type == ItemRenderType.INVENTORY || type == ItemRenderType.EQUIPPED;
    }

    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return false;
    }

    public void renderItem(ItemRenderType type, ItemStack itemStack, Object ... data)
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
        Icon overlayIcon = this.getOverlayIcon(itemStack);
        int overlayColor = this.getOverlayColor(itemStack);

        if (type == ItemRenderType.INVENTORY)
        {
            this.renderIcon(itemStack.getIconIndex(), 0.0F, 0.0F, 16.0F, 16.0F, 0.0F);

            if (overlayIcon != null)
            {
                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
                GL11.glColor3f((float)(overlayColor >> 16 & 255) / 255.0F, (float)(overlayColor >> 8 & 255) / 255.0F, (float)(overlayColor & 255) / 255.0F);
                this.renderIcon(overlayIcon, 2.0F, 2.0F, 10.0F, 10.0F, 0.0F);
            }
        }
        else if (type == ItemRenderType.EQUIPPED)
        {
            Icon baseIcon = itemStack.getIconIndex();
            ItemRenderer.renderItemIn2D(Tessellator.instance, baseIcon.getMaxU(), baseIcon.getMinV(), baseIcon.getMinU(), baseIcon.getMaxV(), baseIcon.getIconWidth(), baseIcon.getIconHeight(), 0.0625F);

            if (overlayIcon != null)
            {
                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
                GL11.glColor3f((float)(overlayColor >> 16 & 255) / 255.0F, (float)(overlayColor >> 8 & 255) / 255.0F, (float)(overlayColor & 255) / 255.0F);
                this.renderIconWithNormal(overlayIcon, 0.875F, 0.875F, 0.375F, 0.375F, 0.001F, 0.0F, 0.0F, 1.0F);
                this.renderIconWithNormal(overlayIcon, 0.875F, 0.875F, 0.375F, 0.375F, -0.0635F, 0.0F, 0.0F, -1.0F);
            }
        }
    }

    private Icon getOverlayIcon(ItemStack itemStack)
    {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);
        int referencedBlockId = nbtData.getInteger("referencedBlockId");

        if (referencedBlockId != 0 && Block.blocksList[referencedBlockId] != null)
        {
            try
            {
                return Block.blocksList[referencedBlockId].getIcon(nbtData.getInteger("referencedSide"), nbtData.getInteger("referencedMeta"));
            }
            catch (Exception var5)
            {
                ;
            }
        }

        return null;
    }

    private int getOverlayColor(ItemStack itemStack)
    {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);
        int referencedBlockId = nbtData.getInteger("referencedBlockId");

        if (referencedBlockId != 0 && Block.blocksList[referencedBlockId] != null)
        {
            try
            {
                return Block.blocksList[referencedBlockId].getBlockColor();
            }
            catch (Exception var5)
            {
                ;
            }
        }

        return 16777215;
    }

    private void renderIcon(Icon icon, float xStart, float yStart, float xEnd, float yEnd, float z)
    {
        if (icon == null)
        {
            icon = RenderBlock.getMissingIcon(TextureMap.locationItemsTexture);
        }

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)xStart, (double)yEnd, (double)z, (double)icon.getMinU(), (double)icon.getMaxV());
        tessellator.addVertexWithUV((double)xEnd, (double)yEnd, (double)z, (double)icon.getMaxU(), (double)icon.getMaxV());
        tessellator.addVertexWithUV((double)xEnd, (double)yStart, (double)z, (double)icon.getMaxU(), (double)icon.getMinV());
        tessellator.addVertexWithUV((double)xStart, (double)yStart, (double)z, (double)icon.getMinU(), (double)icon.getMinV());
        tessellator.draw();
    }

    private void renderIconWithNormal(Icon icon, float xStart, float yStart, float xEnd, float yEnd, float z, float nx, float ny, float nz)
    {
        if (icon == null)
        {
            icon = RenderBlock.getMissingIcon(TextureMap.locationItemsTexture);
        }

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setNormal(nx, ny, nz);
        tessellator.addVertexWithUV((double)xStart, (double)yEnd, (double)z, (double)icon.getMinU(), (double)icon.getMaxV());
        tessellator.addVertexWithUV((double)xEnd, (double)yEnd, (double)z, (double)icon.getMaxU(), (double)icon.getMaxV());
        tessellator.addVertexWithUV((double)xEnd, (double)yStart, (double)z, (double)icon.getMaxU(), (double)icon.getMinV());
        tessellator.addVertexWithUV((double)xStart, (double)yStart, (double)z, (double)icon.getMinU(), (double)icon.getMinV());
        tessellator.draw();
    }
}
