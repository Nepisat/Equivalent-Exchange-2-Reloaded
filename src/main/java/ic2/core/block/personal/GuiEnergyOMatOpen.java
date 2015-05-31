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
public class GuiEnergyOMatOpen extends GuiContainer
{
    public ContainerEnergyOMatOpen container;
    public String name;
    public String offerLabel;
    public String inv;
    private static final ResourceLocation background = new ResourceLocation("ic2", "textures/gui/GUIEnergyOMatOpen.png");

    public GuiEnergyOMatOpen(ContainerEnergyOMatOpen container)
    {
        super(container);
        this.container = container;
        this.name = StatCollector.translateToLocal("ic2.blockPersonalTraderEnergy");
        this.offerLabel = StatCollector.translateToLocal("ic2.container.personalTrader.offer");
        this.inv = StatCollector.translateToLocal("container.inventory");
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.fontRenderer.drawString(this.name, (super.xSize - super.fontRenderer.getStringWidth(this.name)) / 2, 6, 4210752);
        super.fontRenderer.drawString(this.inv, 8, super.ySize - 96 + 2, 4210752);
        super.fontRenderer.drawString(this.offerLabel, 100, 60, 4210752);
        super.fontRenderer.drawString(this.container.tileEntity.euOffer + " EU", 100, 68, 4210752);
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
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        super.buttonList.add(new GuiSmallButton(0, super.guiLeft + 102, super.guiTop + 16, 32, 10, "-100k"));
        super.buttonList.add(new GuiSmallButton(1, super.guiLeft + 102, super.guiTop + 26, 32, 10, "-10k"));
        super.buttonList.add(new GuiSmallButton(2, super.guiLeft + 102, super.guiTop + 36, 32, 10, "-1k"));
        super.buttonList.add(new GuiSmallButton(3, super.guiLeft + 102, super.guiTop + 46, 32, 10, "-100"));
        super.buttonList.add(new GuiSmallButton(4, super.guiLeft + 134, super.guiTop + 16, 32, 10, "+100k"));
        super.buttonList.add(new GuiSmallButton(5, super.guiLeft + 134, super.guiTop + 26, 32, 10, "+10k"));
        super.buttonList.add(new GuiSmallButton(6, super.guiLeft + 134, super.guiTop + 36, 32, 10, "+1k"));
        super.buttonList.add(new GuiSmallButton(7, super.guiLeft + 134, super.guiTop + 46, 32, 10, "+100"));
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton guibutton)
    {
        super.actionPerformed(guibutton);
        IC2.network.initiateClientTileEntityEvent(this.container.tileEntity, guibutton.id);
    }
}
