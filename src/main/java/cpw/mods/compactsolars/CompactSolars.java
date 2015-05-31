/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *     cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.compactsolars;

import ic2.api.item.Items;
import ic2.api.recipe.Recipes;

import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid="CompactSolars", name="Compact Solar Arrays", dependencies="required-after:IC2@[1.118,);required-after:Forge@[9.10,)")
@NetworkMod(clientSideRequired=false,serverSideRequired=false,versionBounds="[4.3,)")
public class CompactSolars {
  @SidedProxy(clientSide="cpw.mods.compactsolars.client.ClientProxy", serverSide="cpw.mods.compactsolars.CommonProxy")
	public static CommonProxy proxy;
	public static BlockCompactSolar compactSolarBlock;
	public static Item icitem;
	public static int productionRate=1;
	@Instance("CompactSolars")
	public static CompactSolars instance;
	public String[] names = {"",
			"マタープレート",
			"錬成マタープレート",
			"圧縮マタープレート",
			"超プレートのかけら",
			"超プレート",
			"イリジウムプレート改",
			"圧縮イリジウムプレート",
			"土のかけら",
			"石のかけら",
			"鉱石のかけら",
			"木のかけら",
			"ブロックsのかけら",
			"赤石のかけら",
			"Mobのかけら",
			"ソーラーアイテム",
			"世界アイテム"};
	@EventHandler
	public void preInit(FMLPreInitializationEvent preinit) {
		Version.init(preinit.getVersionProperties());
        preinit.getModMetadata().version = Version.version();
		Configuration cfg = new Configuration(preinit.getSuggestedConfigurationFile());
		try {
			cfg.load();
			Property block = cfg.getBlock("compactSolar", 650);
			block.comment="The block id for the compact solar arrays.";
			compactSolarBlock = new BlockCompactSolar(block.getInt(650));
			CompactSolarType.buildHats(cfg, 19551);
			Property scale = cfg.get(Configuration.CATEGORY_GENERAL, "scaleFactor", 1);
			scale.comment="The EU generation scaling factor. " +
					"The average number of ticks needed to generate one EU packet." +
					"1 is every tick, 2 is every other tick etc. " +
					"Each Solar will still generate a whole packet (8, 64, 512 EU).";
			productionRate = scale.getInt(1);
		} catch (Exception e) {
			FMLLog.log(Level.SEVERE, e, "CompactSolars was unable to load it's configuration successfully");
			throw new RuntimeException(e);
		} finally {
			cfg.save();
		}
		icitem = new itemics(19552);
    	GameRegistry.registerItem(icitem, "SolarCraftingItem");
    	Recipes.compressor.addRecipe(new ItemStack(icitem, 64,4), new ItemStack(icitem,1,5));

	    Recipes.compressor.addRecipe(new ItemStack(icitem, 64,6), new ItemStack(icitem,1,7));
    	Recipes.compressor.addRecipe(new ItemStack(icitem, 64,2), new ItemStack(icitem,1,3));
    //	Recipes.compressor.addRecipe(new ItemStack(icitem, 64,6), new ItemStack(icitem,1,3));

	   // Recipes.compressor.addRecipe(new ItemStack(Block.dirt, 4), new ItemStack(Block.bedrock, 1));

	}
    @EventHandler
	public void load(FMLInitializationEvent init) {
    
		GameRegistry.registerBlock(compactSolarBlock, ItemCompactSolar.class, "CompactSolarBlock");
		for (CompactSolarType typ : CompactSolarType.values()) {
			GameRegistry.registerTileEntity(typ.clazz, typ.tileEntityName());
		}
		for(int i=0; i<names.length; i++){
			LanguageRegistry.addName(new ItemStack(icitem, 1, i), names[i]);
			}
		
		proxy.registerTileEntityRenderers();
		proxy.registerRenderInformation();
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		FurnaceRecipes.smelting().addSmelting(icitem.itemID, 1, new ItemStack(icitem,1,2), 0.1F);

		//   Recipes.compressor.addRecipe(new ItemStack(icitem, 2,64), new ItemStack(icitem,3, 1));
		GameRegistry.addRecipe(new ItemStack(icitem,0,15), 
				 "SSS",
				 "SSS",
				 "SSS"
				 ,'S',Items.getItem("solarPanel"));
		
		GameRegistry.addRecipe(new ItemStack(icitem,0,1), 
				 "SSS",
				 "SSS",
				 "SSS"
				 ,'S',Items.getItem("matter"));
		GameRegistry.addRecipe(new ItemStack(icitem,0,6), 
				 "SSS",
				 "SSS",
				 "SSS"
				 ,'S',Items.getItem("iridiumPlate"));
		GameRegistry.addRecipe(new ItemStack(icitem,0,4), 
				 "SYS",
				 "YSY",
				 "SYS"
				 ,'S',new ItemStack(icitem,0,7),
				 'Y',new ItemStack(icitem,0,3));
		GameRegistry.addRecipe(new ItemStack(icitem,0,16), 
				 "ABC",
				 "DEF",
				 "GHI"
				 ,'A',new ItemStack(icitem,0,8)
				 ,'B',new ItemStack(icitem,0,9)
		,'C',new ItemStack(icitem,0,10),
		 'D',new ItemStack(icitem,0,11),
		 'E',new ItemStack(icitem,0,12),
		 'F',new ItemStack(icitem,0,13),
		 'G',new ItemStack(icitem,0,14),
		 'H',new ItemStack(icitem,0,5),
		 'I',new ItemStack(icitem,0,7));
		GameRegistry.addRecipe(new ItemStack(compactSolarBlock,0,1), "SYS",
																	 "NXN",
																	 "SYS"
											,'S',new ItemStack(icitem,0,3)
											,'Y',new ItemStack(icitem,0,15)
											,'X',new ItemStack(Block.blockDiamond)
											,'N',new ItemStack(Block.torchRedstoneIdle));
		GameRegistry.addRecipe(new ItemStack(compactSolarBlock,0,3), 
				 "SSS",
				 "NXN",
				 "SSS"
,'S',new ItemStack(icitem,0,5)
,'X',new ItemStack(compactSolarBlock,0,1)
,'N',new ItemStack(icitem,0,13));
		GameRegistry.addRecipe(new ItemStack(compactSolarBlock,0,4), 
				 "SSS",
				 "NXN",
				 "SSS"
,'S',new ItemStack(icitem,0,5)
,'X',new ItemStack(compactSolarBlock,0,3)
,'N',new ItemStack(icitem,0,13));
		
		
		GameRegistry.addRecipe(new ItemStack(compactSolarBlock,0,5), 
				 "SSS",
				 "SSS",
				 "SSS"
,'S',new ItemStack(compactSolarBlock,0,4));
		
		GameRegistry.addRecipe(new ItemStack(compactSolarBlock,0,6), 
				 "SSS",
				 "SSS",
				 "SSS"
,'S',new ItemStack(compactSolarBlock,0,5));
		
		GameRegistry.addRecipe(new ItemStack(compactSolarBlock,0,7), 
				 "SSS",
				 "SSS",
				 "SSS"
,'S',new ItemStack(compactSolarBlock,0,6));
		
		GameRegistry.addRecipe(new ItemStack(compactSolarBlock,0,8), 
				 "SSS",
				 "SYS",
				 "SSS"
,'S',new ItemStack(compactSolarBlock,0,7)
,'Y',new ItemStack(icitem,0,16));
		GameRegistry.addRecipe(new ItemStack(compactSolarBlock,0,9), 
				 "YSY",
				 "SYS",
				 "YSY"
,'S',new ItemStack(compactSolarBlock,0,7)
,'Y',new ItemStack(icitem,0,16));

    }
    @EventHandler
	public void modsLoaded(FMLPostInitializationEvent postinit) {
		//CompactSolarType.generateRecipes(compactSolarBlock);
    
		//CompactSolarType.generateHatRecipes(compactSolarBlock);
	}

    @EventHandler
	public void resetMap(FMLServerStoppingEvent evt)
	{
	    ItemSolarHat.clearRaining();
	}
}
