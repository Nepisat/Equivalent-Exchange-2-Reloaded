package nepisat.ee2.gameObjs.gui;

import nepisat.ee2.PECore;
import nepisat.ee2.gameObjs.container.RelayMK2Container;
import nepisat.ee2.gameObjs.tiles.RelayMK2Tile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GUIRelayMK2 extends GuiContainer
{
	private static final ResourceLocation texture = new ResourceLocation(PECore.MODID.toLowerCase(), "textures/gui/relay2.png");
	private RelayMK2Tile tile;
	
	public GUIRelayMK2(InventoryPlayer invPlayer, RelayMK2Tile tile)
	{
		super(new RelayMK2Container(invPlayer, tile));
		this.tile = tile;
		this.xSize = 193;
		this.ySize = 182;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int var1, int var2)
	{
		this.fontRenderer.drawString("Relay MK2", 28, 6, 4210752);
		this.fontRenderer.drawString(Integer.toString(tile.displayEmc), 107, 25, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) 
	{
		GL11.glColor4f(1F, 1F, 1F, 1F);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		//Emc bar progress
		int progress = tile.getEmcScaled(102);
		this.drawTexturedModalRect(x + 86, y + 6, 30, 183, progress, 10);
		
		//Klein start bar progress. Max is 30.
		progress = tile.getKleinEmcScaled(30);
		this.drawTexturedModalRect(x + 133, y + 68, 0, 183, progress, 10);
				
		//Burn Slot bar progress. Max is 30.
		progress = tile.getRawEmcScaled(30);
		drawTexturedModalRect(x + 81, y + 68, 0, 183, progress, 10);
	}
}
