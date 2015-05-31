package ic2.core.block.personal;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiPersonalChest extends GuiContainer
{
    public ContainerPersonalChest container;
    public String name;
    public String inv;
    private static final ResourceLocation background = new ResourceLocation("ic2", "textures/gui/GUIPersonalChest.png");

    public GuiPersonalChest(ContainerPersonalChest container)
    {
        super(container);
        this.container = container;
        this.name = StatCollector.translateToLocal("ic2.blockPersonalChest");
        this.inv = StatCollector.translateToLocal("container.inventory");
        super.ySize = 222;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.fontRenderer.drawString(this.name, 8, 6, 4210752);
        super.fontRenderer.drawString(this.inv, 8, super.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        super.mc.getTextureManager().bindTexture(background);
        int xOffset = (super.width - super.xSize) / 2;
        int yOffset = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(xOffset, yOffset, 0, 0, super.xSize, super.ySize);
    }
}
