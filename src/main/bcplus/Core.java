package bcplus;

import java.io.File;
import java.io.FileWriter;

import buildcraft.BuildCraftCore;
import buildcraft.BuildCraftTransport;
import buildcraft.BuildCraftTransport.PipeRecipe;
import buildcraft.core.DefaultProps;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.ItemPipe;
import buildcraft.transport.Pipe;
import buildcraft.transport.pipes.PipeItemsGold;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;



@Mod(name = "BuildCraftPlus", version = "1.0",modid = "BCP")

public class Core {
	public static Item EXPipe;
	public static Block Builder=new blockonBuilder(2096,Material.iron);
	public static Block loader=new blockbuilder(2097,Material.iron);

	 @EventHandler
	 public void pinit(FMLPreInitializationEvent evt) {
	
		 
		 EXPipe = buildPipe(28920, EXPipe.class, "Transport Pipe", Item.ingotGold, Block.glass, Item.ingotGold);
		 GameRegistry.registerItem(EXPipe, "EXpipe");
		 GameRegistry.registerBlock(Builder, "builder");
		 GameRegistry.registerBlock(loader, "loader");
	 }
	 
	 
	 public void init(FMLPreInitializationEvent evt) {
		
	 }
	 
	 
	 
	 
	 public static Item buildPipe(int defaultID, Class<? extends Pipe> clas, String descr, Object... ingredients) {
			String name = Character.toLowerCase(clas.getSimpleName().charAt(0)) + clas.getSimpleName().substring(1);

			Property prop = BuildCraftCore.mainConfiguration.getItem(name + ".id", defaultID);

			int id = prop.getInt(defaultID);
			ItemPipe res = BlockGenericPipe.registerPipe(id, clas);
			res.setUnlocalizedName(clas.getSimpleName());
			LanguageRegistry.addName(res, descr);

			// Add appropriate recipe to temporary list
			buildcraft.BuildCraftTransport.PipeRecipe recipe = new PipeRecipe();

			if (ingredients.length == 3) {
				recipe.result = new ItemStack(res, 8);
				recipe.input = new Object[]{"ABC", 'A', ingredients[0], 'B', ingredients[1], 'C', ingredients[2]};

				BuildCraftTransport.pipeRecipes.add(recipe);
			} else if (ingredients.length == 2) {
				recipe.isShapeless = true;
				recipe.result = new ItemStack(res, 1);
				recipe.input = new Object[]{ingredients[0], ingredients[1]};

				BuildCraftTransport.pipeRecipes.add(recipe);

				if (ingredients[1] instanceof ItemPipe) {
					PipeRecipe uncraft = new PipeRecipe();
					uncraft.isShapeless = true;
					uncraft.input = new Object[]{new ItemStack(res)};
					uncraft.result = new ItemStack((Item) ingredients[1]);
					BuildCraftTransport.pipeRecipes.add(uncraft);
				}
			}

			return res;
		}
}
