package ic2.core.block.generator.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.block.generator.container.ContainerWindGenerator;
import ic2.core.block.generator.tileentity.TileEntityWindGenerator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiWindGenerator extends GuiContainer
{
    public ContainerWindGenerator container;
    public String name;
    public String inv;
    private static final ResourceLocation background = new ResourceLocation("ic2", "textures/gui/GUIWindGenerator.png");

    public GuiWindGenerator(ContainerWindGenerator container)
    {
        super(container);
        this.container = container;
        this.name = StatCollector.translateToLocal("ic2.blockWindGenerator");
        this.inv = StatCollector.translateToLocal("container.inventory");
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
        super.mc.getTextureManager().bindTexture(background);
        int j = (super.width - super.xSize) / 2;
        int k = (super.height - super.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, super.xSize, super.ySize);
        int l = this.container.tileEntity.gaugeFuelScaled(14);
        this.drawTexturedModalRect(j + 80, k + 45 + 14 - l, 176, 14 - l, 14, l);
        int o = ((TileEntityWindGenerator)this.container.base).getOverheatScaled(14);

        if (o > 0)
        {
            this.drawTexturedModalRect(j + 80, k + 45 + 14 - o, 176, 28 - o, 14, o);
        }
    }
}
