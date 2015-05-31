package ic2.core.block.personal;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiEnergyOMatClosed extends GuiContainer
{
    public ContainerEnergyOMatClosed container;
    public String name;
    public String wantLabel;
    public String offerLabel;
    public String paidForLabel;
    public String inv;
    private static final ResourceLocation background = new ResourceLocation("ic2", "textures/gui/GUIEnergyOMatClosed.png");

    public GuiEnergyOMatClosed(ContainerEnergyOMatClosed container)
    {
        super(container);
        this.container = container;
        this.name = StatCollector.translateToLocal("ic2.blockPersonalTraderEnergy");
        this.wantLabel = StatCollector.translateToLocal("ic2.container.personalTrader.want");
        this.offerLabel = StatCollector.translateToLocal("ic2.container.personalTrader.offer");
        this.paidForLabel = StatCollector.translateToLocal("ic2.container.personalTraderEnergy.paidFor");
        this.inv = StatCollector.translateToLocal("container.inventory");
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.fontRenderer.drawString(this.name, (super.xSize - super.fontRenderer.getStringWidth(this.name)) / 2, 6, 4210752);
        super.fontRenderer.drawString(this.inv, 8, super.ySize - 96 + 2, 4210752);
        super.fontRenderer.drawString(this.wantLabel, 12, 21, 4210752);
        super.fontRenderer.drawString(this.offerLabel, 12, 39, 4210752);
        super.fontRenderer.drawString(this.container.tileEntity.euOffer + " EU", 50, 39, 4210752);
        super.fontRenderer.drawString(StatCollector.translateToLocalFormatted("ic2.container.personalTraderEnergy.paidFor", new Object[] {Integer.valueOf(this.container.tileEntity.paidFor)}), 12, 57, 4210752);
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
