package nepisat.ee2.proxies;


import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import nepisat.ee2.events.FovChangeEvent;
import nepisat.ee2.events.KeyPressEvent;
import nepisat.ee2.events.ToolTipEvent;
import nepisat.ee2.events.TransmutationRenderingEvent;
import nepisat.ee2.gameObjs.ObjHandler;
import nepisat.ee2.gameObjs.entity.EntityLavaProjectile;
import nepisat.ee2.gameObjs.entity.EntityLensProjectile;
import nepisat.ee2.gameObjs.entity.EntityLootBall;
import nepisat.ee2.gameObjs.entity.EntityMobRandomizer;
import nepisat.ee2.gameObjs.entity.EntityNovaCataclysmPrimed;
import nepisat.ee2.gameObjs.entity.EntityNovaCatalystPrimed;
import nepisat.ee2.gameObjs.entity.EntityWaterProjectile;
import nepisat.ee2.gameObjs.tiles.AlchChestTile;
import nepisat.ee2.gameObjs.tiles.CondenserMK2Tile;
import nepisat.ee2.gameObjs.tiles.CondenserTile;
import nepisat.ee2.handlers.KeyHandler;
import nepisat.ee2.rendering.ChestItemRenderer;
import nepisat.ee2.rendering.ChestRenderer;
import nepisat.ee2.rendering.CondenserItemRenderer;
import nepisat.ee2.rendering.CondenserMK2ItemRenderer;
import nepisat.ee2.rendering.CondenserMK2Renderer;
import nepisat.ee2.rendering.CondenserRenderer;
import nepisat.ee2.rendering.NovaCataclysmRenderer;
import nepisat.ee2.rendering.NovaCatalystRenderer;
import nepisat.ee2.utils.KeyBinds;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{	
	@Override
	public void registerKeyBinds()
	{
		boolean[] repeat = {false};
		for (int i = 0; i < KeyBinds.array.length; i++)
		{
		KeyBinding[] key = {KeyBinds.array[i]};
		KeyBindingRegistry.registerKeyBinding(new KeyHandler(key, repeat));
	

	}
		

		
	}

	@Override
	public void registerRenderers() 
	{
		//Items
		MinecraftForgeClient.registerItemRenderer(ObjHandler.alchChest.blockID, new ChestItemRenderer());
		MinecraftForgeClient.registerItemRenderer(ObjHandler.condenser.blockID, new CondenserItemRenderer());
		MinecraftForgeClient.registerItemRenderer(ObjHandler.condenserMk2.blockID, new CondenserMK2ItemRenderer());
		
		//Blocks
		ClientRegistry.bindTileEntitySpecialRenderer(AlchChestTile.class, new ChestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(CondenserTile.class, new CondenserRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(CondenserMK2Tile.class, new CondenserMK2Renderer());
		
		//Entities
		RenderingRegistry.registerEntityRenderingHandler(EntityWaterProjectile.class, new RenderSnowball(ObjHandler.waterOrb));
		RenderingRegistry.registerEntityRenderingHandler(EntityLavaProjectile.class, new RenderSnowball(ObjHandler.lavaOrb));
		RenderingRegistry.registerEntityRenderingHandler(EntityLootBall.class, new RenderSnowball(ObjHandler.lootBall));
		RenderingRegistry.registerEntityRenderingHandler(EntityMobRandomizer.class, new RenderSnowball(ObjHandler.mobRandomizer));
		RenderingRegistry.registerEntityRenderingHandler(EntityLensProjectile.class, new RenderSnowball(ObjHandler.lensExplosive));
		RenderingRegistry.registerEntityRenderingHandler(EntityNovaCatalystPrimed.class, new NovaCatalystRenderer());
		RenderingRegistry.registerEntityRenderingHandler(EntityNovaCataclysmPrimed.class, new NovaCataclysmRenderer());
	}
	
	@Override
	public void registerClientOnlyEvents() 
	{
		MinecraftForge.EVENT_BUS.register(new FovChangeEvent());
		MinecraftForge.EVENT_BUS.register(new ToolTipEvent());
		MinecraftForge.EVENT_BUS.register(new TransmutationRenderingEvent());
		//FMLCommonHandler.instance().register(new KeyPressEvent());
	}
}

