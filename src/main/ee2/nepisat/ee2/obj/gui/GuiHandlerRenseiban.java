package nepisat.ee2.obj.gui;

import nepisat.ee2.obj.gui.Container.ContainerRenseiban;
import nepisat.ee2.obj.tileentity.TileEntityRenseiban;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandlerRenseiban implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (!world.blockExists(x, y, z))
			return null;
 
		TileEntity tileentity = world.getBlockTileEntity(x, y, z);
		if (tileentity instanceof TileEntityRenseiban) {
			return new ContainerRenseiban(player, (TileEntityRenseiban) tileentity);
		}
		return null;
	}
 
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (!world.blockExists(x, y, z))
			return null;
 
		TileEntity tileentity = world.getBlockTileEntity(x, y, z);
		if (tileentity instanceof TileEntityRenseiban) {
			return new GuiRenseiban(player, (TileEntityRenseiban) tileentity);
		}
		return null;
	}

}
