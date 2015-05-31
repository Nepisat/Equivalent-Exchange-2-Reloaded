package nepisat.ee2;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import nepisat.ee2.config.CustomEMCParser;
import nepisat.ee2.config.NBTWhitelistParser;
import nepisat.ee2.emc.EMCMapper;
import nepisat.ee2.emc.RecipeMapper;
import nepisat.ee2.events.PlayerEvents;
import nepisat.ee2.events.TickEvents;
import nepisat.ee2.gameObjs.ObjHandler;
import nepisat.ee2.handlers.PlayerChecks;
import nepisat.ee2.handlers.TileEntityHandler;
import nepisat.ee2.playerData.AlchemicalBags;
import nepisat.ee2.playerData.IOHandler;
import nepisat.ee2.playerData.Transmutation;
import nepisat.ee2.proxies.CommonProxy;
import nepisat.ee2.utils.AchievementHandler;
import nepisat.ee2.utils.GuiHandler;
import nepisat.ee2.utils.Utils;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;

@Mod(modid = PECore.MODID, name = PECore.MODNAME, version = PECore.VERSION)
@NetworkMod(clientSideRequired = true,serverSideRequired = true)

public class PECore
{	
	public static final String MODID = "ProjectE";
	public static final String MODNAME = "ProjectE";
	public static final String VERSION = "Alpha 0.2d-dev11";

	public static File CONFIG_DIR;

	
	@SidedProxy(clientSide = "nepisat.ee2.proxies.ClientProxy", serverSide = "nepisat.ee2.proxies.CommonProxy")
	public static CommonProxy proxy;
	@Instance(MODID)
	public static PECore instance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		
		
		//ProjectEConfig.init(new File(CONFIG_DIR, "ProjectE.cfg"));

		CustomEMCParser.init();

		NBTWhitelistParser.init();

	//	PacketHandler.register();
		
		//MinecraftForge.EVENT_BUS.register(new PlayerEvents());
		
//		FMLCommonHandler.instance().bus().register(new TickEvents());
	//	FMLCommonHandler.instance().register(new ConnectionHandler());
		
		

		ObjHandler.register();
		ObjHandler.addRecipes();
		  instance = this;
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		proxy.registerClientOnlyEvents();
		NetworkRegistry.instance().registerGuiHandler(instance, new GuiHandler());
		proxy.registerKeyBinds();
		proxy.registerRenderers();
		
		Utils.init();
		//NeiHelper.init();
		AchievementHandler.init();
		

	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	//	ObjHandler.registerPhiloStoneSmelting();

		NBTWhitelistParser.readUserData();
	}
	
	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		/*
		event.registerServerCommand(new ChangelogCMD());
		event.registerServerCommand(new ReloadEmcCMD());
		event.registerServerCommand(new SetEmcCMD());
		event.registerServerCommand(new RemoveEmcCMD());
		event.registerServerCommand(new ResetEmcCMD());
		event.registerServerCommand(new ClearKnowledgeCMD());
*/
	//	if (!ThreadCheckUpdate.hasRunServer())
		//{
		//	new ThreadCheckUpdate(true).start();
		//}
		
		CustomEMCParser.readUserData();

		//PELogger.logInfo("Starting server-side EMC mapping.");
		
		RecipeMapper.map();
		EMCMapper.map();
		
		//PELogger.logInfo("Registered " + EMCMapper.emc.size() + " EMC values.");
		
		//File dir = new File(event.getServer().getEntityWorld().getSaveHandler().getWorldDirectory(), "ProjectE");
		
		
		
	}


	@Mod.EventHandler
	public void serverQuit(FMLServerStoppedEvent event)
	{
		TileEntityHandler.clearAll();
	//	PELogger.logDebug("Cleared tile entity maps.");

		Transmutation.clear();
		AlchemicalBags.clear();
		//PELogger.logDebug("Cleared player data.");
		
		PlayerChecks.clearLists();
		//PELogger.logDebug("Cleared player check-lists: server stopping.");
		
		EMCMapper.clearMaps();
		//PELogger.logInfo("Completed server-stop actions.");
	}
	
	
}
