package nepisat.ee2.obj.gui;
import nepisat.ee2.obj.gui.Container.ContainerRenseiban;
import nepisat.ee2.obj.tileentity.TileEntityRenseiban;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiRenseiban extends GuiContainer {
	private int maxemc=1073741824;
	public static int learnFlag=0;
	private TileEntityRenseiban tileentity;
	//ResourceLocationの第一引数を付け足してドメインを指定することもできる
	//例:new ResourceLocation("sample", "textures/gui/container/furnace.png")
	private static final ResourceLocation GUITEXTURE = new ResourceLocation("ee2","textures/gui/transmute.png");

	public GuiRenseiban(EntityPlayer player, TileEntityRenseiban par2TileEntity) {
		super(new ContainerRenseiban(player, par2TileEntity));
		this.tileentity = par2TileEntity;
		this.xSize = 228;
		this.ySize = 196;
	}

	@Override
	public void drawGuiContainerForegroundLayer(int par1, int par2)
	{
	
		this.fontRenderer.drawString("錬成版", 6, 8, 4210752);
		String emc = String.format("EMC: %,d", (int) tileentity.getStoredEmc());
		this.fontRenderer.drawString(emc, 6, this.ySize - 94, 4210752);
		if (learnFlag > 0)
		{


			this.fontRenderer.drawString("L", 98, 30, 4210752);
			this.fontRenderer.drawString("e", 99, 38, 4210752);
			this.fontRenderer.drawString("a", 100, 46, 4210752);
			this.fontRenderer.drawString("r", 101, 54, 4210752);
			this.fontRenderer.drawString("n", 102, 62, 4210752);
			this.fontRenderer.drawString("e", 103, 70, 4210752);
			this.fontRenderer.drawString("d", 104, 78, 4210752);
			this.fontRenderer.drawString("!", 107, 86, 4210752);
			
			learnFlag--;
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		//テクスチャの指定
		this.mc.getTextureManager().bindTexture(GUITEXTURE);

		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

}