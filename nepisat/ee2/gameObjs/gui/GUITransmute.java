package nepisat.ee2.gameObjs.gui;


import nepisat.ee2.PECore;
import nepisat.ee2.gameObjs.container.TransmuteContainer;
import nepisat.ee2.gameObjs.tiles.TransmuteTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;


public class GUITransmute extends GuiContainer
{
	private static final ResourceLocation texture = new ResourceLocation(PECore.MODID.toLowerCase(), "textures/gui/transmute.png");
	private TransmuteTile tile;

	public GUITransmute(InventoryPlayer invPlayer, TransmuteTile tile) 
	{
		super(new TransmuteContainer(invPlayer, tile));
		this.tile = tile;
		this.xSize = 228;
		this.ySize = 202;
	}
	
	@Override
	public void initGui() 
	{
		tile.setPlayer(Minecraft.getMinecraft().thePlayer);
		
	//	NeiHelper.resetSearchBar();
		
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) 
	{
		GL11.glColor4f(1F, 1F, 1F, 1F);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int var1, int var2) 
	{
		this.fontRenderer.drawString("Transmutation", 24, 10, 4210752);
		String emc = String.format("EMC: %,d", (int) tile.getStoredEmc());
		this.fontRenderer.drawString(emc, 6, this.ySize - 96, 4210752);
		
		if (tile.learnFlag > 0)
		{
			this.fontRenderer.drawString("L", 98, 36, 4210752);
			this.fontRenderer.drawString("e", 99, 44, 4210752);
			this.fontRenderer.drawString("a", 100, 52, 4210752);
			this.fontRenderer.drawString("r", 101, 60, 4210752);
			this.fontRenderer.drawString("n", 102, 68, 4210752);
			this.fontRenderer.drawString("e", 103, 76, 4210752);
			this.fontRenderer.drawString("d", 104, 84, 4210752);
			this.fontRenderer.drawString("!", 107, 92, 4210752);
			
			tile.learnFlag--;
		}
	}
	
	@Override
	public void updateScreen() 
	{/*
		if (NeiHelper.haveNei) 
		{
			String srch = NeiHelper.getSearchText();
			
			if (!tile.filter.equals(srch)) 
			{
				PacketHandler.sendToServer(new SearchUpdatePKT(srch));
				tile.filter = srch.toLowerCase();
				tile.updateOutputs();
			}
		}
		*/
		super.updateScreen();
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		tile.learnFlag = 0;
	}
}
