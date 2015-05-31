package nepisat.ee2.utils;


import nepisat.ee2.gameObjs.container.AlchBagContainer;
import nepisat.ee2.gameObjs.container.AlchChestContainer;
import nepisat.ee2.gameObjs.container.CollectorMK1Container;
import nepisat.ee2.gameObjs.container.CollectorMK2Container;
import nepisat.ee2.gameObjs.container.CollectorMK3Container;
import nepisat.ee2.gameObjs.container.CondenserContainer;
import nepisat.ee2.gameObjs.container.DMFurnaceContainer;
import nepisat.ee2.gameObjs.container.RMFurnaceContainer;
import nepisat.ee2.gameObjs.container.RelayMK1Container;
import nepisat.ee2.gameObjs.container.RelayMK2Container;
import nepisat.ee2.gameObjs.container.TransmuteContainer;
import nepisat.ee2.gameObjs.container.inventory.AlchBagInventory;
import nepisat.ee2.gameObjs.container.inventory.EternalDensityInventory;
import nepisat.ee2.gameObjs.container.inventory.MercurialEyeInventory;
import nepisat.ee2.gameObjs.container.inventory.TransmuteTabletInventory;
import nepisat.ee2.gameObjs.tiles.AlchChestTile;
import nepisat.ee2.gameObjs.tiles.CollectorMK1Tile;
import nepisat.ee2.gameObjs.tiles.CollectorMK2Tile;
import nepisat.ee2.gameObjs.tiles.CollectorMK3Tile;
import nepisat.ee2.gameObjs.tiles.CondenserMK2Tile;
import nepisat.ee2.gameObjs.tiles.CondenserTile;
import nepisat.ee2.gameObjs.tiles.DMFurnaceTile;
import nepisat.ee2.gameObjs.tiles.RMFurnaceTile;
import nepisat.ee2.gameObjs.tiles.RelayMK1Tile;
import nepisat.ee2.gameObjs.tiles.RelayMK2Tile;
import nepisat.ee2.gameObjs.tiles.RelayMK3Tile;
import nepisat.ee2.gameObjs.tiles.TransmuteTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import nepisat.ee2.gameObjs.container.*;
import nepisat.ee2.gameObjs.gui.GUIAlchChest;
import nepisat.ee2.gameObjs.gui.GUICollectorMK1;
import nepisat.ee2.gameObjs.gui.GUICollectorMK2;
import nepisat.ee2.gameObjs.gui.GUICollectorMK3;
import nepisat.ee2.gameObjs.gui.GUICondenser;
import nepisat.ee2.gameObjs.gui.GUICondenserMK2;
import nepisat.ee2.gameObjs.gui.GUIDMFurnace;
import nepisat.ee2.gameObjs.gui.GUIEternalDensity;
import nepisat.ee2.gameObjs.gui.GUIMercurialEye;
import nepisat.ee2.gameObjs.gui.GUIPhilosStone;
import nepisat.ee2.gameObjs.gui.GUIRMFurnace;
import nepisat.ee2.gameObjs.gui.GUIRelayMK1;
import nepisat.ee2.gameObjs.gui.GUIRelayMK2;
import nepisat.ee2.gameObjs.gui.GUIRelayMK3;
import nepisat.ee2.gameObjs.gui.GUITransmute;
import nepisat.ee2.gameObjs.gui.GUITransmuteTablet;
import nepisat.ee2.gameObjs.*;
public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		
		switch (ID)
		{
			case Constants.ALCH_CHEST_GUI:
				if (tile != null && tile instanceof AlchChestTile)
					return new AlchChestContainer(player.inventory, (AlchChestTile) tile);
				break;
			case Constants.ALCH_BAG_GUI:
				return new AlchBagContainer(player.inventory, new AlchBagInventory(player, player.getHeldItem()));
			case Constants.TRANSMUTE_STONE_GUI:
				if (tile != null && tile instanceof TransmuteTile)
					return new TransmuteContainer(player.inventory, (TransmuteTile) tile);
				break;
			case Constants.CONDENSER_GUI:
				if (tile != null && tile instanceof CondenserTile)
					return new CondenserContainer(player.inventory, (CondenserTile) tile);
				break;
			case Constants.RM_FURNACE_GUI:
				if (tile != null && tile instanceof RMFurnaceTile)
					return new RMFurnaceContainer(player.inventory, (RMFurnaceTile) tile);
				break;
			case Constants.DM_FURNACE_GUI:
				if (tile != null && tile instanceof DMFurnaceTile)
					return new DMFurnaceContainer(player.inventory, (DMFurnaceTile) tile);
				break;
			case Constants.COLLECTOR1_GUI:
				if (tile != null && tile instanceof CollectorMK1Tile)
					return new CollectorMK1Container(player.inventory, (CollectorMK1Tile) tile);
				break;
			case Constants.COLLECTOR2_GUI:
				if (tile != null && tile instanceof CollectorMK2Tile)
					return new CollectorMK2Container(player.inventory, (CollectorMK2Tile) tile);
				break;
			case Constants.COLLECTOR3_GUI:
				if (tile != null && tile instanceof CollectorMK3Tile)
					return new CollectorMK3Container(player.inventory, (CollectorMK3Tile) tile);
				break;
			case Constants.RELAY1_GUI:
				if (tile != null && tile instanceof RelayMK1Tile)
					return new RelayMK1Container(player.inventory, (RelayMK1Tile) tile);
				break;
			case Constants.RELAY2_GUI:
				if (tile != null && tile instanceof RelayMK2Tile)
					return new RelayMK2Container(player.inventory, (RelayMK2Tile) tile);
				break;
			case Constants.RELAY3_GUI:
				if (tile != null && tile instanceof RelayMK3Tile)
					return new RelayMK3Container(player.inventory, (RelayMK3Tile) tile);
				break;
			case Constants.MERCURIAL_GUI:
				return new MercurialEyeContainer(player.inventory, new MercurialEyeInventory(player.getHeldItem()));
			case Constants.PHILOS_STONE_GUI:
				return new PhilosStoneContainer(player.inventory);
			case Constants.TRANSMUTE_TABLET_GUI:
				return new TransmuteTabletContainer(player.inventory, new TransmuteTabletInventory(player.getHeldItem(), player));
			case Constants.ETERNAL_DENSITY_GUI:
				return new EternalDensityContainer(player.inventory, new EternalDensityInventory(player.getHeldItem(), player));
			case Constants.CONDENSER_MK2_GUI:
				return new CondenserMK2Container(player.inventory, (CondenserMK2Tile) tile);
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		
		switch (ID)
		{
			case Constants.ALCH_CHEST_GUI:
				if (tile != null && tile instanceof AlchChestTile)
					return new GUIAlchChest(player.inventory, (AlchChestTile) tile);
				break;
			case Constants.ALCH_BAG_GUI:
				return new GUIAlchChest(player.inventory, new AlchBagInventory(player, player.getHeldItem()));
			case Constants.TRANSMUTE_STONE_GUI:
				if (tile != null && tile instanceof TransmuteTile)
					return new GUITransmute(player.inventory, (TransmuteTile) tile);
				break;
			case Constants.CONDENSER_GUI:
				if (tile != null && tile instanceof CondenserTile)
					return new GUICondenser(player.inventory, (CondenserTile) tile);
				break;
			case Constants.RM_FURNACE_GUI:
				if (tile != null && tile instanceof RMFurnaceTile)
					return new GUIRMFurnace(player.inventory, (RMFurnaceTile) tile);
				break;
			case Constants.DM_FURNACE_GUI:
				if (tile != null && tile instanceof DMFurnaceTile)
					return new GUIDMFurnace(player.inventory, (DMFurnaceTile) tile);
				break;
			case Constants.COLLECTOR1_GUI:
				if (tile != null && tile instanceof CollectorMK1Tile)
					return new GUICollectorMK1(player.inventory, (CollectorMK1Tile) tile);
				break;
			case Constants.COLLECTOR2_GUI:
				if (tile != null && tile instanceof CollectorMK2Tile)
					return new GUICollectorMK2(player.inventory, (CollectorMK2Tile) tile);
				break;
			case Constants.COLLECTOR3_GUI:
				if (tile != null && tile instanceof CollectorMK3Tile)
					return new GUICollectorMK3(player.inventory, (CollectorMK3Tile) tile);
				break;
			case Constants.RELAY1_GUI:
				if (tile != null && tile instanceof RelayMK1Tile)
					return new GUIRelayMK1(player.inventory, (RelayMK1Tile) tile);
				break;
			case Constants.RELAY2_GUI:
				if (tile != null && tile instanceof RelayMK2Tile)
					return new GUIRelayMK2(player.inventory, (RelayMK2Tile) tile);
				break;
			case Constants.RELAY3_GUI:
				if (tile != null && tile instanceof RelayMK3Tile)
					return new GUIRelayMK3(player.inventory, (RelayMK3Tile) tile);
				break;
			case Constants.MERCURIAL_GUI:
				return new GUIMercurialEye(player.inventory, new MercurialEyeInventory(player.getHeldItem()));
			case Constants.PHILOS_STONE_GUI:
				return new GUIPhilosStone(player.inventory);
			case Constants.TRANSMUTE_TABLET_GUI:
				return new GUITransmuteTablet(player.inventory, new TransmuteTabletInventory(player.getHeldItem(), player));
			case Constants.ETERNAL_DENSITY_GUI:
				player.getHeldItem();
				return new GUIEternalDensity(player.inventory, new EternalDensityInventory(player.getHeldItem(), player));
			case Constants.CONDENSER_MK2_GUI:
				return new GUICondenserMK2(player.inventory, (CondenserMK2Tile) tile);
		}
		
		return null;
	}
}
