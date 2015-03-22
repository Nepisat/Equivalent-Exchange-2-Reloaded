package nepisat.ee2.Core;

import java.util.logging.Logger;
import nepisat.ee2.EMC.BlockEMCMapper;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import nepisat.ee2.EMC.BlockEMCMapper;
import nepisat.ee2.event.ToolTipEvent;
import nepisat.ee2.obj.block.BlockRenseiban;


import nepisat.ee2.obj.gui.GuiHandlerRenseiban;
import nepisat.ee2.obj.item.ItemKenzyanoisi;
import nepisat.ee2.obj.tileentity.TileEntityRenseiban;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
@Mod(modid="EE2R",name="Equivalent Exchange 2 Reloaded",version = "b1.0")
public class EE2 {
	public static String MODN="EE2R";
	public static CreativeTabs CreativeTabEE2=new CreativeTabEE2R("EE2R");;
	public static Block BlockRenseiban;
	public static Item Kenzyanoisi;
	public static int KenzyanoisiID=28520;
	public static int RenseibanID = 4087;
	public static int RenseibanGuiID=1000;
	private static Logger logger = Logger.getLogger("EE2R");
	@Instance("EE2")
	public static EE2 instance;
	
	@EventHandler
     public void init(FMLInitializationEvent event){
		logger.setParent(FMLLog.getLogger());
    	 LanguageRegistry.addName(BlockRenseiban, "Transmutation");
    	 LanguageRegistry.instance().addNameForObject(BlockRenseiban, "ja_JP", "錬成版");
    	 LanguageRegistry.addName(Kenzyanoisi, "Philosopher's Stone");
    	 LanguageRegistry.instance().addNameForObject(Kenzyanoisi, "ja_JP", "賢者の石");
         GameRegistry.registerTileEntity(TileEntityRenseiban.class, "TileEntityRenseiban");
         System.out.println("[EE2Rel]Starting EMCMap");
         BlockEMCMapper.EMCMap();
         System.out.println("[EE2Rel]Finish EMCMap");
         NetworkRegistry.instance().registerGuiHandler(instance, new GuiHandlerRenseiban());
         MinecraftForge.EVENT_BUS.register(new ToolTipEvent());
         KeyBinding[] key = {new KeyBinding("Name of Button", Keyboard.KEY_G)};
         boolean[] repeat = {false};
      
         KeyBindingRegistry.registerKeyBinding(new Key(key, repeat));
         


	}
	@EventHandler
	public void preinit(FMLPreInitializationEvent event){
		BlockRenseiban = new BlockRenseiban(RenseibanID,Material.rock);
		Kenzyanoisi=new ItemKenzyanoisi(KenzyanoisiID);
		GameRegistry.registerBlock(BlockRenseiban, "Renseiban");
		GameRegistry.registerItem(Kenzyanoisi, "KEnzyanoisi");
		 instance = this;
	}
}
