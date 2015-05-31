package ic2.core.block.personal;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiTradeOMatClosed extends GuiContainer
{
    private final ContainerTradeOMatClosed container;
    private final String name;
    private final String wantLabel;
    private final String offerLabel;
    private final String stockLabel;
    private final String inv;
    private static final ResourceLocation background = new ResourceLocation("ic2", "textures/gui/GUITradeOMatClosed.png");

    public GuiTradeOMatClosed(ContainerTradeOMatClosed container)
    {
        super(container);
        this.container = container;
        this.name = StatCollector.translateToLocal("ic2.blockPersonalTrader");
        this.wantLabel = StatCollector.translateToLocal("ic2.container.personalTrader.want");
        this.offerLabel = StatCollector.translateToLocal("ic2.container.personalTrader.offer");
        this.stockLabel = StatCollector.translateToLocal("ic2.container.personalTrader.stock");
        this.inv = StatCollector.translateToLocal("container.inventory");
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.fontRenderer.drawString(this.name, (super.xSize - super.fontRenderer.getStringWidth(this.name)) / 2, 6, 4210752);
        super.fontRenderer.drawString(this.inv, 8, super.ySize - 96 + 2, 4210752);
        super.fontRenderer.drawString(this.wantLabel, 12, 23, 4210752);
        super.fontRenderer.drawString(this.offerLabel, 12, 42, 4210752);
        super.fontRenderer.drawString(this.stockLabel, 12, 60, 4210752);
        super.fontRenderer.drawString(this.container.tileEntity.stock < 0 ? "\u221e" : "" + this.container.tileEntity.stock, 50, 60, this.container.tileEntity.stock != 0 ? 4210752 : 16733525);
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
    }
}
