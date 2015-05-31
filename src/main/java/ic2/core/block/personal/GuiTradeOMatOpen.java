package ic2.core.block.personal;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.IC2;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiSmallButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiTradeOMatOpen extends GuiContainer
{
    private final ContainerTradeOMatOpen container;
    private final String name;
    private final String wantLabel;
    private final String offerLabel;
    private final String totalTradesLabel0;
    private final String totalTradesLabel1;
    private final String stockLabel;
    private final String inv;
    private final boolean isAdmin;
    private static final ResourceLocation background = new ResourceLocation("ic2", "textures/gui/GUITradeOMatOpen.png");

    public GuiTradeOMatOpen(ContainerTradeOMatOpen container, boolean isAdmin)
    {
        super(container);
        this.container = container;
        this.name = StatCollector.translateToLocal("ic2.blockPersonalTrader");
        this.wantLabel = StatCollector.translateToLocal("ic2.container.personalTrader.want");
        this.offerLabel = StatCollector.translateToLocal("ic2.container.personalTrader.offer");
        this.totalTradesLabel0 = StatCollector.translateToLocal("ic2.container.personalTrader.totalTrades0");
        this.totalTradesLabel1 = StatCollector.translateToLocal("ic2.container.personalTrader.totalTrades1");
        this.stockLabel = StatCollector.translateToLocal("ic2.container.personalTrader.stock");
        this.inv = StatCollector.translateToLocal("container.inventory");
        this.isAdmin = isAdmin;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();

        if (this.isAdmin)
        {
            super.buttonList.add(new GuiSmallButton(0, (super.width - super.xSize) / 2 + 152, (super.height - super.ySize) / 2 + 4, 20, 20, "\u221e"));
        }
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.fontRenderer.drawString(this.name, (super.xSize - super.fontRenderer.getStringWidth(this.name)) / 2, 6, 4210752);
        super.fontRenderer.drawString(this.inv, 8, super.ySize - 96 + 2, 4210752);
        super.fontRenderer.drawString(this.wantLabel, 12, 23, 4210752);
        super.fontRenderer.drawString(this.offerLabel, 12, 57, 4210752);
        super.fontRenderer.drawString(this.totalTradesLabel0, 108, 28, 4210752);
        super.fontRenderer.drawString(this.totalTradesLabel1, 108, 36, 4210752);
        super.fontRenderer.drawString("" + this.container.tileEntity.totalTradeCount, 112, 44, 4210752);
        super.fontRenderer.drawString(this.stockLabel + " " + (this.container.tileEntity.stock < 0 ? "\u221e" : "" + this.container.tileEntity.stock), 108, 60, 4210752);
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

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton guibutton)
    {
        super.actionPerformed(guibutton);

        if (guibutton.id == 0)
        {
            IC2.network.initiateClientTileEntityEvent(this.container.tileEntity, 0);
        }
    }
}
