package nepisat.ee2.gameObjs.gui;

import nepisat.ee2.gameObjs.container.PhilosStoneContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GUIPhilosStone extends GuiContainer
{
	private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation("textures/gui/container/crafting_table.png");
	
	public GUIPhilosStone(InventoryPlayer inventoryPlayer)
	{
		super(new PhilosStoneContainer(inventoryPlayer));
	}
	
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
	{
		this.fontRenderer.drawString(I18n.getStringParams("container.crafting", new Object[0]), 28, 6, 4210752);
		this.fontRenderer.drawString(I18n.getStringParams("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(craftingTableGuiTextures);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}
}
