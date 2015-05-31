package ic2.core.block.generator.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.block.generator.container.ContainerNuclearReactor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiNuclearReactor extends GuiContainer
{
    public ContainerNuclearReactor container;
    public String name;
    public String inv;
    private final ResourceLocation background;

    public GuiNuclearReactor(ContainerNuclearReactor container)
    {
        super(container);
        this.container = container;
        this.background = new ResourceLocation("ic2", "textures/gui/GUI6by" + container.size + ".png");
        this.name = StatCollector.translateToLocal("ic2.blockNuclearReactor");
        this.inv = StatCollector.translateToLocal("container.inventory");
        short c = 222;
        int i = c - 108;
        super.ySize = i + 108;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.fontRenderer.drawString(this.name, (super.xSize - super.fontRenderer.getStringWidth(this.name)) / 2, 6, 4210752);
        super.fontRenderer.drawString(this.inv, 8, super.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        super.mc.getTextureManager().bindTexture(this.background);
        int j = (super.width - super.xSize) / 2;
        int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
    }
}
