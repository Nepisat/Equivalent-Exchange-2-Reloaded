package ic2.core.block.wiring;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.GuiIconButton;
import ic2.core.IC2;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiElectricBlock extends GuiContainer
{
    private final ContainerElectricBlock container;
    private final String armorInv;
    private final String inv;
    private final String level;
    private final String name;
    private static final ResourceLocation background = new ResourceLocation("ic2", "textures/gui/GUIElectricBlock.png");

    public GuiElectricBlock(ContainerElectricBlock container)
    {
        super(container);
        super.ySize = 196;
        this.container = container;
        this.armorInv = I18n.getString("ic2.container.armor");
        this.inv = I18n.getString("container.inventory");
        this.level = I18n.getString("ic2.container.electricBlock.level");

        switch (container.tileEntity.tier)
        {
            case 1:
                this.name = I18n.getString("ic2.blockBatBox");
                break;

            case 2:
                this.name = I18n.getString("ic2.blockMFE");
                break;

            case 3:
                this.name = I18n.getString("ic2.blockMFSU");
                break;

            default:
                this.name = null;
        }
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        super.buttonList.add(new GuiIconButton(0, (super.width - super.xSize) / 2 + 152, (super.height - super.ySize) / 2 + 4, 20, 20, new ItemStack(Item.redstone), true));
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.fontRenderer.drawString(this.name, (super.xSize - super.fontRenderer.getStringWidth(this.name)) / 2, 6, 4210752);
        super.fontRenderer.drawString(this.armorInv, 8, super.ySize - 126 + 3, 4210752);
        super.fontRenderer.drawString(this.inv, 8, super.ySize - 96 + 3, 4210752);
        super.fontRenderer.drawString(this.level, 79, 25, 4210752);
        int e = Math.min(this.container.tileEntity.energy, this.container.tileEntity.maxStorage);
        super.fontRenderer.drawString(" " + e, 110, 35, 4210752);
        super.fontRenderer.drawString("/" + this.container.tileEntity.maxStorage, 110, 45, 4210752);
        String output = I18n.getStringParams("ic2.container.electricBlock.output", new Object[] {Integer.valueOf(this.container.tileEntity.output)});
        super.fontRenderer.drawString(output, 85, 60, 4210752);
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

        if (this.container.tileEntity.energy > 0)
        {
            int i1 = (int)(24.0F * this.container.tileEntity.getChargeLevel());
            this.drawTexturedModalRect(j + 79, k + 34, 176, 14, i1 + 1, 16);
        }
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
