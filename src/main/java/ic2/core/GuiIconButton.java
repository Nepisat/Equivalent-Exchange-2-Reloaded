package ic2.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSmallButton;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiIconButton extends GuiSmallButton
{
    private ResourceLocation texture;
    private int textureX;
    private int textureY;
    private ItemStack itemStack = null;
    private boolean drawQuantity;
    private RenderItem renderItem;

    public GuiIconButton(int id, int x, int y, int w, int h, ResourceLocation texture, int textureX, int textureY)
    {
        super(id, x, y, w, h, "");
        this.texture = texture;
        this.textureX = textureX;
        this.textureY = textureY;
    }

    public GuiIconButton(int id, int x, int y, int w, int h, ItemStack icon, boolean drawQuantity)
    {
        super(id, x, y, w, h, "");
        this.itemStack = icon;
        this.drawQuantity = drawQuantity;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft minecraft, int i, int j)
    {
        super.drawButton(minecraft, i, j);

        if (this.itemStack == null)
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            minecraft.getTextureManager().bindTexture(this.texture);
            this.drawTexturedModalRect(this.xPosition + 2, this.yPosition + 1, this.textureX, this.textureY, this.width - 4, this.height - 4);
        }
        else
        {
            if (this.renderItem == null)
            {
                this.renderItem = new RenderItem();
            }

            this.renderItem.renderItemIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, this.itemStack, this.xPosition + 2, this.yPosition + 1);

            if (this.drawQuantity)
            {
                this.renderItem.renderItemOverlayIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, this.itemStack, this.xPosition + 2, this.xPosition + 1);
            }
        }
    }
}
