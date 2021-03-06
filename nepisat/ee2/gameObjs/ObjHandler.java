package nepisat.ee2.gameObjs;


import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import nepisat.ee2.PECore;
import nepisat.ee2.gameObjs.blocks.AlchemicalChest;
import nepisat.ee2.gameObjs.blocks.Collector;
import nepisat.ee2.gameObjs.blocks.Condenser;
import nepisat.ee2.gameObjs.blocks.CondenserMK2;
import nepisat.ee2.gameObjs.blocks.FuelBlock;
import nepisat.ee2.gameObjs.blocks.InterdictionTorch;
import nepisat.ee2.gameObjs.blocks.MatterBlock;
import nepisat.ee2.gameObjs.blocks.MatterFurnace;
import nepisat.ee2.gameObjs.blocks.NovaCataclysm;
import nepisat.ee2.gameObjs.blocks.NovaCatalyst;
import nepisat.ee2.gameObjs.blocks.Relay;
import nepisat.ee2.gameObjs.blocks.TransmutationStone;
import nepisat.ee2.gameObjs.customRecipes.RecipesAlchemyBags;
import nepisat.ee2.gameObjs.customRecipes.RecipesCovalenceRepair;
import nepisat.ee2.gameObjs.customRecipes.RecipesKleinStars;
import nepisat.ee2.gameObjs.entity.EntityHomingArrow;
import nepisat.ee2.gameObjs.entity.EntityLavaProjectile;
import nepisat.ee2.gameObjs.entity.EntityLensProjectile;
import nepisat.ee2.gameObjs.entity.EntityLootBall;
import nepisat.ee2.gameObjs.entity.EntityMobRandomizer;
import nepisat.ee2.gameObjs.entity.EntityNovaCataclysmPrimed;
import nepisat.ee2.gameObjs.entity.EntityNovaCatalystPrimed;
import nepisat.ee2.gameObjs.entity.EntityWaterProjectile;
import nepisat.ee2.gameObjs.items.AlchemicalBag;
import nepisat.ee2.gameObjs.items.AlchemicalFuel;
import nepisat.ee2.gameObjs.items.CataliticLens;
import nepisat.ee2.gameObjs.items.CovalenceDust;
import nepisat.ee2.gameObjs.items.DestructionCatalyst;
import nepisat.ee2.gameObjs.items.DiviningRodHigh;
import nepisat.ee2.gameObjs.items.DiviningRodLow;
import nepisat.ee2.gameObjs.items.DiviningRodMedium;
import nepisat.ee2.gameObjs.items.EvertideAmulet;
import nepisat.ee2.gameObjs.items.GemEternalDensity;
import nepisat.ee2.gameObjs.items.HyperkineticLens;
import nepisat.ee2.gameObjs.items.KleinStar;
import nepisat.ee2.gameObjs.items.Matter;
import nepisat.ee2.gameObjs.items.MercurialEye;
import nepisat.ee2.gameObjs.items.PhilosophersStone;
import nepisat.ee2.gameObjs.items.RepairTalisman;
import nepisat.ee2.gameObjs.items.TimeWatch;
import nepisat.ee2.gameObjs.items.Tome;
import nepisat.ee2.gameObjs.items.TransmutationTablet;
import nepisat.ee2.gameObjs.items.VolcaniteAmulet;
import nepisat.ee2.gameObjs.items.armor.DMArmor;
import nepisat.ee2.gameObjs.items.armor.GemArmor;
import nepisat.ee2.gameObjs.items.armor.RMArmor;
import nepisat.ee2.gameObjs.items.itemBlocks.ItemAlchemyChestBlock;
import nepisat.ee2.gameObjs.items.itemBlocks.ItemCollectorBlock;
import nepisat.ee2.gameObjs.items.itemBlocks.ItemCondenserBlock;
import nepisat.ee2.gameObjs.items.itemBlocks.ItemDMFurnaceBlock;
import nepisat.ee2.gameObjs.items.itemBlocks.ItemFuelBlock;
import nepisat.ee2.gameObjs.items.itemBlocks.ItemMatterBlock;
import nepisat.ee2.gameObjs.items.itemBlocks.ItemRMFurnaceBlock;
import nepisat.ee2.gameObjs.items.itemBlocks.ItemRelayBlock;
import nepisat.ee2.gameObjs.items.itemBlocks.ItemTransmutationBlock;
import nepisat.ee2.gameObjs.items.itemEntities.LavaOrb;
import nepisat.ee2.gameObjs.items.itemEntities.LensExplosive;
import nepisat.ee2.gameObjs.items.itemEntities.LootBallItem;
import nepisat.ee2.gameObjs.items.itemEntities.RandomizerProjectile;
import nepisat.ee2.gameObjs.items.itemEntities.WaterOrb;
import nepisat.ee2.gameObjs.items.rings.ArchangelSmite;
import nepisat.ee2.gameObjs.items.rings.BlackHoleBand;
import nepisat.ee2.gameObjs.items.rings.BodyStone;
import nepisat.ee2.gameObjs.items.rings.HarvestGoddess;
import nepisat.ee2.gameObjs.items.rings.Ignition;
import nepisat.ee2.gameObjs.items.rings.IronBand;
import nepisat.ee2.gameObjs.items.rings.LifeStone;
import nepisat.ee2.gameObjs.items.rings.MindStone;
import nepisat.ee2.gameObjs.items.rings.SWRG;
import nepisat.ee2.gameObjs.items.rings.SoulStone;
import nepisat.ee2.gameObjs.items.rings.Zero;
import nepisat.ee2.gameObjs.items.tools.DarkAxe;
import nepisat.ee2.gameObjs.items.tools.DarkHammer;
import nepisat.ee2.gameObjs.items.tools.DarkHoe;
import nepisat.ee2.gameObjs.items.tools.DarkPickaxe;
import nepisat.ee2.gameObjs.items.tools.DarkShears;
import nepisat.ee2.gameObjs.items.tools.DarkShovel;
import nepisat.ee2.gameObjs.items.tools.DarkSword;
import nepisat.ee2.gameObjs.items.tools.RedAxe;
import nepisat.ee2.gameObjs.items.tools.RedHammer;
import nepisat.ee2.gameObjs.items.tools.RedHoe;
import nepisat.ee2.gameObjs.items.tools.RedKatar;
import nepisat.ee2.gameObjs.items.tools.RedPick;
import nepisat.ee2.gameObjs.items.tools.RedShears;
import nepisat.ee2.gameObjs.items.tools.RedShovel;
import nepisat.ee2.gameObjs.items.tools.RedStar;
import nepisat.ee2.gameObjs.items.tools.RedSword;
import nepisat.ee2.gameObjs.tiles.AlchChestTile;
import nepisat.ee2.gameObjs.tiles.CollectorMK1Tile;
import nepisat.ee2.gameObjs.tiles.CollectorMK2Tile;
import nepisat.ee2.gameObjs.tiles.CollectorMK3Tile;
import nepisat.ee2.gameObjs.tiles.CondenserMK2Tile;
import nepisat.ee2.gameObjs.tiles.CondenserTile;
import nepisat.ee2.gameObjs.tiles.DMFurnaceTile;
import nepisat.ee2.gameObjs.tiles.InterdictionTile;
import nepisat.ee2.gameObjs.tiles.RMFurnaceTile;
import nepisat.ee2.gameObjs.tiles.RelayMK1Tile;
import nepisat.ee2.gameObjs.tiles.RelayMK2Tile;
import nepisat.ee2.gameObjs.tiles.RelayMK3Tile;
import nepisat.ee2.gameObjs.tiles.TransmuteTile;
import nepisat.ee2.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.HashMap;
import java.util.Map.Entry;

public class ObjHandler
{
	public static final CreativeTabs cTab = new CreativeTab();
	public static Block alchChest = new AlchemicalChest(1024);
	public static Block confuseTorch = new InterdictionTorch(1025);
	public static Block transmuteStone = new TransmutationStone(1026);
	public static Block condenser = new Condenser(1027);
	public static Block condenserMk2 = new CondenserMK2(1028);
	public static Block rmFurnaceOff = new MatterFurnace(1029,false, true);
	public static Block rmFurnaceOn = new MatterFurnace(1030,true, true);
	public static Block dmFurnaceOff = new MatterFurnace(1031,false, false);
	public static Block dmFurnaceOn = new MatterFurnace(1032,true, false);
	public static Block matterBlock = new MatterBlock(1033);
	public static Block fuelBlock = new FuelBlock(1034);
	public static Block energyCollector = new Collector(1036,1);
	public static Block collectorMK2 = new Collector(1037,2);
	public static Block collectorMK3 = new Collector(1038,3);
	public static Block relay = new Relay(1039,1);
	public static Block relayMK2 = new Relay(1040,2);
	public static Block relayMK3 = new Relay(1041,3);
	public static Block novaCatalyst = new NovaCatalyst(1050);
	public static Block novaCataclysm = new NovaCataclysm(1051);
	
	public static Item philosStone = new PhilosophersStone(1052);
	public static Item alchBag = new AlchemicalBag(1053);
	public static Item repairTalisman = new RepairTalisman(1054);
	public static Item kleinStars = new KleinStar(1055);
	public static Item fuels = new AlchemicalFuel(1056);
	public static Item covalence = new CovalenceDust(1057);
	public static Item matter = new Matter(1058);
	
	public static Item dmPick = new DarkPickaxe(1059);
	public static Item dmAxe = new DarkAxe(1060);
	public static Item dmShovel = new DarkShovel(1061);
	public static Item dmSword = new DarkSword(1062);
	public static Item dmHoe = new DarkHoe(1063);
	public static Item dmShears = new DarkShears(1064);
	public static Item dmHammer = new DarkHammer(1065);
	
	public static Item rmPick = new RedPick(1066);
	public static Item rmAxe = new RedAxe(1067);
	public static Item rmShovel = new RedShovel(1068);
	public static Item rmSword = new RedSword(1069);
	public static Item rmHoe = new RedHoe(1070);
	public static Item rmShears = new RedShears(1071);
	public static Item rmHammer = new RedHammer(1072);
	public static Item rmKatar = new RedKatar(1073);
	public static Item rmStar = new RedStar(1074);
	
	public static Item dmHelmet = new DMArmor(1075,0);
	public static Item dmChest = new DMArmor(1075,1);
	public static Item dmLegs = new DMArmor(1075,2);
	public static Item dmFeet = new DMArmor(1075,3);
	
	public static Item rmHelmet = new RMArmor(1076,0);
	public static Item rmChest = new RMArmor(1076,1);
	public static Item rmLegs = new RMArmor(1076,2);
	public static Item rmFeet = new RMArmor(1076,3);
	
	public static Item gemHelmet = new GemArmor(1077,0);
	public static Item gemChest = new GemArmor(1077,1);
	public static Item gemLegs = new GemArmor(1077,2);
	public static Item gemFeet = new GemArmor(1077,3);
	
	public static Item ironBand = new IronBand(1078);
	public static Item blackHole = new BlackHoleBand(1079);
	public static Item angelSmite = new ArchangelSmite(1080);
	public static Item harvestGod = new HarvestGoddess(1081);
	public static Item ignition = new Ignition(1082);
	public static Item zero = new Zero(1083);
	public static Item swrg = new SWRG(1084);
	public static Item timeWatch = new TimeWatch(1085);
	public static Item everTide = new EvertideAmulet(1086);
	public static Item volcanite = new VolcaniteAmulet(1087);
	public static Item eternalDensity = new GemEternalDensity(1088);
	public static Item dRod1 = new DiviningRodLow(1089);
	public static Item dRod2 = new DiviningRodMedium(1090);
	public static Item dRod3 = new DiviningRodHigh(1091);
	public static Item mercEye = new MercurialEye(1092);
	//public static Item arcana = new Arcana();
	
	public static Item dCatalyst = new DestructionCatalyst(1093);
	public static Item hyperLens = new HyperkineticLens(1094);
	public static Item cataliticLens = new CataliticLens(1095);
	
	public static Item bodyStone = new BodyStone(1096);
	public static Item soulStone = new SoulStone(1097);
	public static Item mindStone = new MindStone(1098);
	public static Item lifeStone = new LifeStone(1099);
	
	public static Item tome = new Tome(1100);
	
	public static Item waterOrb = new WaterOrb(1101);
	public static Item lavaOrb = new LavaOrb(1102);
	public static Item lootBall = new LootBallItem(1103);
	public static Item mobRandomizer = new RandomizerProjectile(1104);
	public static Item lensExplosive = new LensExplosive(1105);
	
	public static Item transmutationTablet = new TransmutationTablet(1106);
	
	public static void register()
	{
		//Blocks
		GameRegistry.registerBlock(alchChest, ItemAlchemyChestBlock.class, "Alchemical Chest");
		GameRegistry.registerBlock(confuseTorch, "Interdiction Torch");
		GameRegistry.registerBlock(transmuteStone, ItemTransmutationBlock.class, "Transmutation Stone");
		GameRegistry.registerBlock(condenser, ItemCondenserBlock.class, "Condenser");
		GameRegistry.registerBlock(condenserMk2, "Condenser MK2");
		GameRegistry.registerBlock(rmFurnaceOff, ItemRMFurnaceBlock.class, "RM Furnace");
		GameRegistry.registerBlock(rmFurnaceOn, "RM Furnace Lit");
		GameRegistry.registerBlock(dmFurnaceOff, ItemDMFurnaceBlock.class, "DM Furnace");
		GameRegistry.registerBlock(dmFurnaceOn, "DM Furnace Lit");
		GameRegistry.registerBlock(matterBlock, ItemMatterBlock.class, "Matter Block");
		GameRegistry.registerBlock(fuelBlock, ItemFuelBlock.class, "Fuel Block");
		GameRegistry.registerBlock(energyCollector, ItemCollectorBlock.class, "Collector MK1");
		GameRegistry.registerBlock(collectorMK2, ItemRelayBlock.class, "Collector MK2");
		GameRegistry.registerBlock(collectorMK3, ItemRelayBlock.class, "Collector MK3");
		GameRegistry.registerBlock(relay, ItemRelayBlock.class, "Relay MK1");
		GameRegistry.registerBlock(relayMK2, ItemRelayBlock.class, "Realy MK2");
		GameRegistry.registerBlock(relayMK3, ItemRelayBlock.class, "Relay MK3");
		GameRegistry.registerBlock(novaCatalyst, "Nova Catalyst");
		GameRegistry.registerBlock(novaCataclysm, "Nova Cataclysm");
		
		//Items
		GameRegistry.registerItem(philosStone, philosStone.getUnlocalizedName());
		GameRegistry.registerItem(alchBag, alchBag.getUnlocalizedName());
		GameRegistry.registerItem(repairTalisman, repairTalisman.getUnlocalizedName());
		GameRegistry.registerItem(kleinStars, kleinStars.getUnlocalizedName());
		GameRegistry.registerItem(fuels, fuels.getUnlocalizedName());
		GameRegistry.registerItem(covalence, covalence.getUnlocalizedName());
		GameRegistry.registerItem(matter, matter.getUnlocalizedName());
		
		GameRegistry.registerItem(dmPick, dmPick.getUnlocalizedName());
		GameRegistry.registerItem(dmAxe, dmAxe.getUnlocalizedName());
		GameRegistry.registerItem(dmShovel, dmShovel.getUnlocalizedName());
		GameRegistry.registerItem(dmSword, dmSword.getUnlocalizedName());
		GameRegistry.registerItem(dmHoe, dmHoe.getUnlocalizedName());
		GameRegistry.registerItem(dmShears, dmShears.getUnlocalizedName());
		GameRegistry.registerItem(dmHammer, dmHammer.getUnlocalizedName());
		
		GameRegistry.registerItem(rmPick, rmPick.getUnlocalizedName());
		GameRegistry.registerItem(rmAxe, rmAxe.getUnlocalizedName());
		GameRegistry.registerItem(rmShovel, rmShovel.getUnlocalizedName());
		GameRegistry.registerItem(rmSword, rmSword.getUnlocalizedName());
		GameRegistry.registerItem(rmHoe, rmHoe.getUnlocalizedName());
		GameRegistry.registerItem(rmShears, rmShears.getUnlocalizedName());
		GameRegistry.registerItem(rmHammer, rmHammer.getUnlocalizedName());
		GameRegistry.registerItem(rmKatar, rmKatar.getUnlocalizedName());
		GameRegistry.registerItem(rmStar, rmStar.getUnlocalizedName());
		
		GameRegistry.registerItem(dmHelmet, dmHelmet.getUnlocalizedName());
		GameRegistry.registerItem(dmChest, dmChest.getUnlocalizedName());
		GameRegistry.registerItem(dmLegs, dmLegs.getUnlocalizedName());
		GameRegistry.registerItem(dmFeet, dmFeet.getUnlocalizedName());
		
		GameRegistry.registerItem(rmHelmet, rmHelmet.getUnlocalizedName());
		GameRegistry.registerItem(rmChest, rmChest.getUnlocalizedName());
		GameRegistry.registerItem(rmLegs, rmLegs.getUnlocalizedName());
		GameRegistry.registerItem(rmFeet, rmFeet.getUnlocalizedName());
		
		GameRegistry.registerItem(gemHelmet, gemHelmet.getUnlocalizedName());
		GameRegistry.registerItem(gemChest, gemChest.getUnlocalizedName());
		GameRegistry.registerItem(gemLegs, gemLegs.getUnlocalizedName());
		GameRegistry.registerItem(gemFeet, gemFeet.getUnlocalizedName());
		
		GameRegistry.registerItem(ironBand, ironBand.getUnlocalizedName());
		GameRegistry.registerItem(blackHole, blackHole.getUnlocalizedName());
		GameRegistry.registerItem(angelSmite, angelSmite.getUnlocalizedName());
		GameRegistry.registerItem(harvestGod, harvestGod.getUnlocalizedName());
		GameRegistry.registerItem(ignition, ignition.getUnlocalizedName());
		GameRegistry.registerItem(zero, zero.getUnlocalizedName());
		GameRegistry.registerItem(swrg, swrg.getUnlocalizedName());
		GameRegistry.registerItem(timeWatch, timeWatch.getUnlocalizedName());
		GameRegistry.registerItem(eternalDensity, eternalDensity.getUnlocalizedName());
		GameRegistry.registerItem(dRod1, dRod1.getUnlocalizedName());
		GameRegistry.registerItem(dRod2, dRod2.getUnlocalizedName());
		GameRegistry.registerItem(dRod3, dRod3.getUnlocalizedName());
		GameRegistry.registerItem(mercEye, mercEye.getUnlocalizedName());
		//GameRegistry.registerItem(arcana, arcana.getUnlocalizedName());
		
		GameRegistry.registerItem(bodyStone, bodyStone.getUnlocalizedName());
		GameRegistry.registerItem(soulStone, soulStone.getUnlocalizedName());
		GameRegistry.registerItem(mindStone, mindStone.getUnlocalizedName());
		GameRegistry.registerItem(lifeStone, lifeStone.getUnlocalizedName());
		
		GameRegistry.registerItem(everTide, everTide.getUnlocalizedName());
		GameRegistry.registerItem(volcanite, volcanite.getUnlocalizedName());
		
		GameRegistry.registerItem(waterOrb, waterOrb.getUnlocalizedName());
		GameRegistry.registerItem(lavaOrb, lavaOrb.getUnlocalizedName());
		GameRegistry.registerItem(lootBall, lootBall.getUnlocalizedName());
		GameRegistry.registerItem(mobRandomizer, mobRandomizer.getUnlocalizedName());
		GameRegistry.registerItem(lensExplosive, lensExplosive.getUnlocalizedName());
		
		GameRegistry.registerItem(dCatalyst, dCatalyst.getUnlocalizedName());
		GameRegistry.registerItem(hyperLens, hyperLens.getUnlocalizedName());
		GameRegistry.registerItem(cataliticLens, cataliticLens.getUnlocalizedName());
		
		GameRegistry.registerItem(tome, tome.getUnlocalizedName());
		GameRegistry.registerItem(transmutationTablet, transmutationTablet.getUnlocalizedName());
		
		//Tile Entities
		GameRegistry.registerTileEntity(AlchChestTile.class, "Alchemical Chest Tile");
		GameRegistry.registerTileEntity(InterdictionTile.class, "Interdiction Torch Tile");
		GameRegistry.registerTileEntity(CondenserTile.class, "Condenser Tile");
		GameRegistry.registerTileEntity(CondenserMK2Tile.class, "Condenser MK2 Tile");
		GameRegistry.registerTileEntity(RMFurnaceTile.class, "RM Furnace Tile");
		GameRegistry.registerTileEntity(DMFurnaceTile.class, "DM Furnace Tile");
		GameRegistry.registerTileEntity(CollectorMK1Tile.class, "Energy Collector MK1 Tile");
		GameRegistry.registerTileEntity(CollectorMK2Tile.class, "Energy Collector MK2 Tile");
		GameRegistry.registerTileEntity(CollectorMK3Tile.class, "Energy Collector MK3 Tile");
		GameRegistry.registerTileEntity(RelayMK1Tile.class, "AM Relay MK1 Tile");
		GameRegistry.registerTileEntity(RelayMK2Tile.class, "AM Relay MK2 Tile");
		GameRegistry.registerTileEntity(RelayMK3Tile.class, "AM Relay MK3 Tile");
		GameRegistry.registerTileEntity(TransmuteTile.class, "Transmutation Tablet Tile");
		
		//Entities
		EntityRegistry.registerModEntity(EntityWaterProjectile.class, "Water Water", 1, PECore.instance, 256, 10, true);
		EntityRegistry.registerModEntity(EntityLavaProjectile.class, "Lava Orb", 2, PECore.instance, 256, 10, true);
		EntityRegistry.registerModEntity(EntityLootBall.class, "Loot Ball", 3, PECore.instance, 64, 10, true);
		EntityRegistry.registerModEntity(EntityMobRandomizer.class, "Mob Randomizer", 4, PECore.instance, 256, 10, true);
		EntityRegistry.registerModEntity(EntityLensProjectile.class, "Explosive Lens Projectile", 5, PECore.instance, 256, 10, true);
		EntityRegistry.registerModEntity(EntityNovaCatalystPrimed.class, "Nova Catalyst", 6, PECore.instance, 256, 10, true);
		EntityRegistry.registerModEntity(EntityNovaCataclysmPrimed.class, "Nova Cataclysm", 7, PECore.instance, 256, 10, true);
		EntityRegistry.registerModEntity(EntityHomingArrow.class, "Homing Arrow", 8, PECore.instance, 256, 10, true);
	}


	public static void addRecipes()
	{
		//Shaped Recipes
		//Philos Stone
		GameRegistry.addRecipe(new ItemStack(philosStone), "RGR", "GDG", "RGR", 'R', Item.redstone, 'G', Item.glowstone, 'D', Item.diamond);
		
		GameRegistry.addRecipe(new ItemStack(philosStone), "GRG", "RDR", "GRG", 'R', Item.redstone, 'G', Item.glowstone, 'D', Item.diamond);
		
		//Interdiction torch
	//	if (ProjectEConfig.enableITorch)
		//{
			GameRegistry.addRecipe(new ItemStack(confuseTorch, 2), "RDR", "DPD", "GGG", 'R', Block.torchRedstoneIdle, 'G', Item.glowstone, 'D', Item.diamond, 'P', philosStone);
		//}
		
		//Repair Talisman
		GameRegistry.addRecipe(new ItemStack(repairTalisman), "LMH", "SPS", "HML", 'P', Item.paper, 'S', Item.silk, 'L', new ItemStack(covalence, 1, 0), 'M', new ItemStack(covalence, 1, 1), 'H', new ItemStack(covalence, 1, 2));
		
		//Klein Star Ein
		GameRegistry.addRecipe(new ItemStack(kleinStars, 1, 0), "MMM", "MDM", "MMM", 'M', new ItemStack(fuels, 1, 1), 'D', Item.diamond);
		
		//Matter
		GameRegistry.addRecipe(new ItemStack(matter, 1, 0), "AAA", "ADA", "AAA", 'D', Block.blockDiamond, 'A', new ItemStack(fuels, 1, 2));
		GameRegistry.addRecipe(new ItemStack(matter, 1, 1), "AAA", "DDD", "AAA", 'D', matter, 'A', new ItemStack(fuels, 1, 2));
		GameRegistry.addRecipe(new ItemStack(matter, 1, 1), "ADA", "ADA", "ADA", 'D', matter, 'A', new ItemStack(fuels, 1, 2));
		
		//Alchemical Chest
		//if (ProjectEConfig.enableAlcChest)
		//{
			GameRegistry.addRecipe(new ItemStack(alchChest), "LMH", "SDS", "ICI", 'D', Item.diamond, 'L', new ItemStack(covalence, 1, 0), 'M', new ItemStack(covalence, 1, 1), 'H', new ItemStack(covalence, 1, 2),'S', Block.stone, 'I', Item.ingotIron, 'C', Block.chest);
		//}
		
		//Alchemical Bags
		for (int i = 0; i < 16; i++)
		{
			GameRegistry.addRecipe(new ItemStack(alchBag, 1, i), "CCC", "WAW", "WWW", 'C', new ItemStack(covalence, 1, 2), 'A', alchChest, 'W', new ItemStack(Block.cloth, 1, i));
		}
		
		//Condenser
	//	if (ProjectEConfig.enableCondenser)
		//{
			GameRegistry.addRecipe(new ItemStack(condenser), "ODO", "DCD", "ODO", 'D', new ItemStack(Item.diamond), 'O', new ItemStack(Block.obsidian), 'C', new ItemStack(alchChest));
		//}

		//Condenser MK2
		//if (ProjectEConfig.enableCondenser2)
		//{
			GameRegistry.addRecipe(new ItemStack(condenserMk2), "RDR", "DCD", "RDR", 'D', new ItemStack(matterBlock, 1, 0), 'R', new ItemStack(matterBlock, 1, 1), 'C', condenser);
		//}
		
		//Transmutation Table
		//if (ProjectEConfig.enableTransTable)
		//{
			GameRegistry.addRecipe(new ItemStack(transmuteStone), "OSO", "SPS", "OSO", 'S', Block.stone, 'O', Block.obsidian, 'P', philosStone);
		//}
		
		//Matter Block
		GameRegistry.addRecipe(new ItemStack(matterBlock, 4, 0), "DD", "DD", 'D', matter);
		GameRegistry.addRecipe(new ItemStack(matterBlock, 4, 1), "DD", "DD", 'D', new ItemStack(matter, 1, 1));
		
		//Matter Furnaces
		//if (ProjectEConfig.enableDarkFurnace)
		//{
			GameRegistry.addRecipe(new ItemStack(dmFurnaceOff), "DDD", "DFD", "DDD", 'D', new ItemStack(matterBlock, 1, 0), 'F', Block.furnaceIdle);
		//}
		//if (ProjectEConfig.enableRedFurnace)
		//{
			GameRegistry.addRecipe(new ItemStack(rmFurnaceOff), "XRX", "RFR", 'R', new ItemStack(matterBlock, 1, 1), 'F', dmFurnaceOff);
		//}
		
		//Collectors
		//if (ProjectEConfig.enableCollector)
		//{
			GameRegistry.addRecipe(new ItemStack(energyCollector), "GTG", "GDG", "GFG", 'G', Block.glowStone, 'F', Block.furnaceIdle, 'D', Block.blockDiamond, 'T', Block.glass);
		//}
		//if (ProjectEConfig.enableCollector2)
		//{
			GameRegistry.addRecipe(new ItemStack(collectorMK2), "GDG", "GCG", "GGG", 'G', Block.glowStone, 'C', energyCollector, 'D', matter);
		//}
		//if (ProjectEConfig.enableCollector3)
		//{
			GameRegistry.addRecipe(new ItemStack(collectorMK3), "GRG", "GCG", "GGG", 'G', Block.glowStone, 'C', collectorMK2, 'R', new ItemStack(matter, 1, 1));
		//}
		
		//AM Relays
		//if (ProjectEConfig.enableRelay)
		//{
			GameRegistry.addRecipe(new ItemStack(relay), "OSO", "ODO", "OOO", 'S', Block.glass, 'D', Block.blockDiamond, 'O', Block.obsidian);
	//	}
		//if (ProjectEConfig.enableRelay2)
		//{
			GameRegistry.addRecipe(new ItemStack(relayMK2), "ODO", "OAO", "OOO", 'A', relay, 'D', matter, 'O', Block.obsidian);
		//}
		//if (ProjectEConfig.enableRelay3)
		//{
			GameRegistry.addRecipe(new ItemStack(relayMK3), "ORO", "OAO", "OOO", 'A', relayMK2, 'R', new ItemStack(matter, 1, 1), 'O', Block.obsidian);
		//}
		
		//DM Tools
		GameRegistry.addRecipe(new ItemStack(dmPick), "MMM", "XDX", "XDX", 'D', Item.diamond, 'M', matter);
		GameRegistry.addRecipe(new ItemStack(dmAxe), "MMX", "MDX", "XDX", 'D', Item.diamond, 'M', matter);
		GameRegistry.addRecipe(new ItemStack(dmShovel), "XMX", "XDX", "XDX", 'D', Item.diamond, 'M', matter);
		GameRegistry.addRecipe(new ItemStack(dmSword), "XMX", "XMX", "XDX", 'D', Item.diamond, 'M', matter);
		GameRegistry.addRecipe(new ItemStack(dmHoe), "MMX", "XDX", "XDX", 'D', Item.diamond, 'M', matter);
		GameRegistry.addRecipe(new ItemStack(dmShears), "XM", "DX", 'D', Item.diamond, 'M', matter);
		GameRegistry.addRecipe(new ItemStack(dmHammer), "MDM", "XDX", "XDX", 'D', Item.diamond, 'M', matter);
		
		//RM Tools
		GameRegistry.addRecipe(new ItemStack(rmPick), "RRR", "XPX", "XMX", 'R', new ItemStack(matter, 1, 1), 'P', dmPick, 'M', matter);
		GameRegistry.addRecipe(new ItemStack(rmAxe), "RRX", "RAX", "XMX", 'R', new ItemStack(matter, 1, 1), 'A', dmAxe, 'M', matter);
		GameRegistry.addRecipe(new ItemStack(rmShovel), "XRX", "XSX", "XMX", 'R', new ItemStack(matter, 1, 1), 'S', dmShovel, 'M', matter);
		GameRegistry.addRecipe(new ItemStack(rmSword), "XRX", "XRX", "XSX", 'R', new ItemStack(matter, 1, 1), 'S', dmSword);
		GameRegistry.addRecipe(new ItemStack(rmHoe), "RRX", "XHX", "XMX", 'R', new ItemStack(matter, 1, 1), 'H', dmHoe, 'M', matter);
		GameRegistry.addRecipe(new ItemStack(rmShears), "XR", "SX", 'R', new ItemStack(matter, 1, 1), 'S', dmShears);
		GameRegistry.addRecipe(new ItemStack(rmHammer), "RMR", "XHX", "XMX", 'R', new ItemStack(matter, 1, 1), 'H', dmHammer, 'M', matter);
		GameRegistry.addRecipe(new ItemStack(rmKatar), "123", "4RR", "RRR", '1', rmShears, '2', rmAxe, '3', rmSword, '4', rmHoe, 'R', new ItemStack(matter, 1, 1));
		GameRegistry.addRecipe(new ItemStack(rmStar), "123", "RRR", "RRR", '1', rmHammer, '2', rmPick, '3', rmShovel, 'R', new ItemStack(matter, 1, 1));
		
		//Armor
		GameRegistry.addRecipe(new ItemStack(dmHelmet), "MMM", "MXM", 'M', matter);
		GameRegistry.addRecipe(new ItemStack(dmChest), "MXM", "MMM", "MMM", 'M', matter);
		GameRegistry.addRecipe(new ItemStack(dmLegs), "MMM", "MXM", "MXM", 'M', matter);
		GameRegistry.addRecipe(new ItemStack(dmFeet), "MXM", "MXM", 'M', matter);
		
		GameRegistry.addRecipe(new ItemStack(rmHelmet), "MMM", "MDM", 'M', new ItemStack(matter, 1, 1), 'D', dmHelmet);
		GameRegistry.addRecipe(new ItemStack(rmChest), "MDM", "MMM", "MMM", 'M', new ItemStack(matter, 1, 1), 'D', dmChest);
		GameRegistry.addRecipe(new ItemStack(rmLegs), "MMM", "MDM", "MXM", 'M', new ItemStack(matter, 1, 1), 'D', dmLegs);
		GameRegistry.addRecipe(new ItemStack(rmFeet), "MDM", "MXM", 'M', new ItemStack(matter, 1, 1), 'D', dmFeet);
		
		//Rings
		GameRegistry.addRecipe(new ItemStack(ironBand), "III", "ILI", "III", 'I', Item.ingotIron, 'L', Item.bucketLava);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(harvestGod), "SFS", "DID", "SFS", 'I', ironBand, 'S', "treeSapling", 'F', Block.plantRed, 'F', Block.plantRed, 'D', matter));
		GameRegistry.addRecipe(new ItemStack(swrg), "DFD", "FIF", "DFD", 'I', ironBand, 'F', Item.feather, 'D', matter);
		GameRegistry.addRecipe(new ItemStack(ignition), "FMF", "DID", "FMF", 'I', ironBand, 'F', new ItemStack(Item.flintAndSteel, 1, OreDictionary.WILDCARD_VALUE), 'D', matter, 'M', new ItemStack(fuels, 1, 1));
		GameRegistry.addRecipe(new ItemStack(bodyStone), "SSS", "RLR", "SSS", 'R', new ItemStack(matter, 1, 1), 'S', Item.sugar, 'L', new ItemStack(Item.dyePowder, 1, 4));
		GameRegistry.addRecipe(new ItemStack(soulStone), "GGG", "RLR", "GGG", 'R', new ItemStack(matter, 1, 1), 'G', Item.glowstone, 'L', new ItemStack(Item.dyePowder, 1, 4));
		GameRegistry.addRecipe(new ItemStack(mindStone), "BBB", "RLR", "BBB", 'R', new ItemStack(matter, 1, 1), 'B', Item.book, 'L', new ItemStack(Item.dyePowder, 1, 4));
		GameRegistry.addRecipe(new ItemStack(blackHole), "SSS", "DID", "SSS", 'I', ironBand, 'S', Item.silk, 'D', matter);
		GameRegistry.addRecipe(new ItemStack(everTide), "WWW", "DDD", "WWW", 'W', Item.bucketWater, 'D', matter);
		GameRegistry.addRecipe(new ItemStack(volcanite), "LLL", "DDD", "LLL", 'L', Item.bucketLava, 'D', matter);
		GameRegistry.addRecipe(new ItemStack(eternalDensity), "DOD", "MDM", "DOD", 'D', Item.diamond, 'O', Block.obsidian, 'M', matter);
		GameRegistry.addRecipe(new ItemStack(zero), "SBS", "MIM", "SBS", 'S', Block.snow, 'B', Item.snowball, 'M', matter, 'I', ironBand);
		//GameRegistry.addRecipe(new ItemStack(arcana), new Object[]{"ZIH", "SMM", "MMM", 'Z', zero, 'I', ignition, 'H', harvestGod, 'S', swrg, 'M', new ItemStack(matter, 1, 1)});
		
		//Watch of flowing time
		GameRegistry.addRecipe(new ItemStack(timeWatch), "DOD", "GCG", "DOD", 'D', matter, 'O', Block.obsidian, 'G', Block.glowStone, 'C', Item.pocketSundial);
		GameRegistry.addRecipe(new ItemStack(timeWatch), "DGD", "OCO", "DGD", 'D', matter, 'O', Block.obsidian, 'G', Block.glowStone, 'C', Item.pocketSundial);
		
		//Divining rods
		GameRegistry.addRecipe(new ItemStack(dRod1), "DDD", "DSD", "DDD", 'D', covalence, 'S', Item.stick);
		GameRegistry.addRecipe(new ItemStack(dRod2), "DDD", "DSD", "DDD", 'D', new ItemStack(covalence, 1, 1), 'S', dRod1);
		GameRegistry.addRecipe(new ItemStack(dRod3), "DDD", "DSD", "DDD", 'D', new ItemStack(covalence, 1, 2), 'S', dRod2);
		
		//Explosive items
		GameRegistry.addRecipe(new ItemStack(dCatalyst), "NMN", "MFM", "NMN", 'N', novaCatalyst, 'M', new ItemStack(fuels, 1, 1), 'F', new ItemStack(Item.flintAndSteel, 1, OreDictionary.WILDCARD_VALUE));
		GameRegistry.addRecipe(new ItemStack(hyperLens), "DDD", "MNM", "DDD", 'N', novaCatalyst, 'M', matter, 'D', Item.diamond);
		GameRegistry.addRecipe(new ItemStack(cataliticLens), "MMM", "HMD", "MMM", 'M', matter, 'H', hyperLens, 'D', dCatalyst);
		GameRegistry.addRecipe(new ItemStack(cataliticLens), "MMM", "DMH", "MMM", 'M', matter, 'H', hyperLens, 'D', dCatalyst);
		
		//Fuel Block
		GameRegistry.addRecipe(new ItemStack(fuelBlock, 1, 0), "FFF", "FFF", "FFF", 'F', fuels);
		GameRegistry.addRecipe(new ItemStack(fuelBlock, 1, 1), "FFF", "FFF", "FFF", 'F', new ItemStack(fuels, 1, 1));
		GameRegistry.addRecipe(new ItemStack(fuelBlock, 1, 2), "FFF", "FFF", "FFF", 'F', new ItemStack(fuels, 1, 2));
		
		//Tome
		GameRegistry.addRecipe(new ItemStack(tome), "HML", "KBK", "LMH", 'L', new ItemStack(covalence, 1, 0), 'M', new ItemStack(covalence, 1, 1), 'H', new ItemStack(covalence, 1, 2), 'B', Item.book, 'K', new ItemStack(kleinStars, 1, 5));
				
		//TransmutationTablet
		GameRegistry.addRecipe(new ItemStack(transmutationTablet), "DSD", "STS", "DSD", 'D', new ItemStack(matterBlock, 1, 0), 'S', Block.stone, 'T', transmuteStone);

		//Mercurial Eye
		GameRegistry.addRecipe(new ItemStack(mercEye), "OBO", "BRB", "BDB", 'O', Block.obsidian, 'B', Block.brick, 'R', new ItemStack(matter, 1, 1), 'D', Item.diamond);
		
		//Shapeless Recipes
		//Philos Stone exchanges
		GameRegistry.addShapelessRecipe(new ItemStack(Item.enderPearl), philosStone, Item.ingotIron, Item.ingotIron, Item.ingotIron, Item.ingotIron);
		GameRegistry.addShapelessRecipe(new ItemStack(Item.ingotIron, 8), philosStone, Item.ingotGold);
		GameRegistry.addShapelessRecipe(new ItemStack(Item.ingotGold), philosStone, Item.ingotIron, Item.ingotIron, Item.ingotIron, Item.ingotIron, Item.ingotIron, Item.ingotIron, Item.ingotIron, Item.ingotIron);
		GameRegistry.addShapelessRecipe(new ItemStack(Item.diamond), philosStone, Item.ingotGold, Item.ingotGold, Item.ingotGold, Item.ingotGold);
		GameRegistry.addShapelessRecipe(new ItemStack(Item.ingotGold, 4), philosStone, Item.diamond);
		GameRegistry.addShapelessRecipe(new ItemStack(Item.emerald), philosStone, Item.diamond, Item.diamond);
		GameRegistry.addShapelessRecipe(new ItemStack(Item.diamond, 2), philosStone, Item.emerald);
		GameRegistry.addShapelessRecipe(new ItemStack(fuels, 1, 0), philosStone, Item.coal, Item.coal, Item.coal, Item.coal);
		GameRegistry.addShapelessRecipe(new ItemStack(Item.coal, 4), philosStone, new ItemStack(fuels, 1, 0));
		GameRegistry.addShapelessRecipe(new ItemStack(fuels, 1, 1), philosStone, new ItemStack(fuels, 1, 0), new ItemStack(fuels, 1, 0), new ItemStack(fuels, 1, 0), new ItemStack(fuels, 1, 0));
		GameRegistry.addShapelessRecipe(new ItemStack(fuels, 4, 0), philosStone, new ItemStack(fuels, 1, 1));
		GameRegistry.addShapelessRecipe(new ItemStack(fuels, 1, 2), philosStone, new ItemStack(fuels, 1, 1), new ItemStack(fuels, 1, 1), new ItemStack(fuels, 1, 1), new ItemStack(fuels, 1, 1));
		GameRegistry.addShapelessRecipe(new ItemStack(fuels, 4, 1), philosStone, new ItemStack(fuels, 1, 2));
		
		//Covalence dust
		GameRegistry.addShapelessRecipe(new ItemStack(covalence, 40, 0), Block.cobblestone, Block.cobblestone, Block.cobblestone, Block.cobblestone, Block.cobblestone, Block.cobblestone, Block.cobblestone, Block.cobblestone, new ItemStack(Item.coal, 1, 1));
		GameRegistry.addShapelessRecipe(new ItemStack(covalence, 40, 1), Item.ingotIron, Item.redstone);
		GameRegistry.addShapelessRecipe(new ItemStack(covalence, 40, 2), Item.diamond, Item.coal);
		
		//Klein Stars
		for (int i = 1; i < 6; i++)
		{
			GameRegistry.addShapelessRecipe(new ItemStack(kleinStars, 1, i), new ItemStack(kleinStars, 1, i - 1), new ItemStack(kleinStars, 1, i - 1), new ItemStack(kleinStars, 1, i - 1), new ItemStack(kleinStars, 1, i - 1));
		}
		
		//Other items
		GameRegistry.addShapelessRecipe(new ItemStack(novaCatalyst, 2), Block.tnt, new ItemStack(fuels, 1, 1));
		GameRegistry.addShapelessRecipe(new ItemStack(novaCataclysm, 2), novaCatalyst, new ItemStack(fuels, 1, 2));
		GameRegistry.addShapelessRecipe(new ItemStack(lifeStone), bodyStone, soulStone);
		GameRegistry.addShapelessRecipe(new ItemStack(Block.ice), new ItemStack(zero, 1, OreDictionary.WILDCARD_VALUE), Item.bucketWater);
		GameRegistry.addShapelessRecipe(new ItemStack(Item.bucketLava), volcanite, Item.bucketEmpty, Item.redstone);
		
		GameRegistry.addShapelessRecipe(new ItemStack(gemHelmet), rmHelmet, new ItemStack(kleinStars, 1, 5), everTide, soulStone);
		GameRegistry.addShapelessRecipe(new ItemStack(gemChest), rmChest, new ItemStack(kleinStars, 1, 5), volcanite, bodyStone);
		GameRegistry.addShapelessRecipe(new ItemStack(gemLegs), rmLegs, new ItemStack(kleinStars, 1, 5), blackHole, timeWatch);
		GameRegistry.addShapelessRecipe(new ItemStack(gemFeet), rmFeet, new ItemStack(kleinStars, 1, 5), swrg, swrg);
		
		GameRegistry.addShapelessRecipe(new ItemStack(matter, 1, 0), matterBlock);
		GameRegistry.addShapelessRecipe(new ItemStack(matter, 1, 1), new ItemStack(matterBlock, 1, 1));
		
		GameRegistry.addShapelessRecipe(new ItemStack(fuels, 9, 0), new ItemStack(fuelBlock, 1, 0));
		GameRegistry.addShapelessRecipe(new ItemStack(fuels, 9, 1), new ItemStack(fuelBlock, 1, 1));
		GameRegistry.addShapelessRecipe(new ItemStack(fuels, 9, 2), new ItemStack(fuelBlock, 1, 2));
		
		//Custom Recipe managment
		GameRegistry.addRecipe(new RecipesAlchemyBags());
		GameRegistry.addRecipe(new RecipesCovalenceRepair());
		GameRegistry.addRecipe(new RecipesKleinStars());
		RecipeSorter.register("Alchemical Bags Recipes", RecipesAlchemyBags.class, Category.SHAPELESS, "before:minecraft:shaped");
		RecipeSorter.register("Covalence Repair Recipes", RecipesCovalenceRepair.class, Category.SHAPELESS, "before:minecraft:shaped");
		RecipeSorter.register("Klein Star Recipes", RecipesKleinStars.class, Category.SHAPELESS, "before:minecraft:shaped");
		
		//Fuel Values
		GameRegistry.registerFuelHandler(new FuelHandler());
	}
	
	//Philosopher's stone smelting recipes, EE3 style
	public static void registerPhiloStoneSmelting()
	{
		for (Entry<ItemStack, ItemStack> entry : (((HashMap<ItemStack, ItemStack>) FurnaceRecipes.smelting().getSmeltingList()).entrySet()))
		{
			if (entry.getKey() == null || entry.getValue() == null)
			{
				continue;
			}
			
			ItemStack input = entry.getKey();
			ItemStack output = entry.getValue().copy();
			output.stackSize *= 7;
			
			GameRegistry.addShapelessRecipe(output, philosStone, input, input, input, input, input, input, input, new ItemStack(Item.coal, 1, OreDictionary.WILDCARD_VALUE));
		}
	}
	
	public static class FuelHandler implements IFuelHandler
	{
		@Override
		public int getBurnTime(ItemStack fuel)
		{
			if (fuel.getItem() == fuels)
			{
				switch (fuel.getItemDamage())
				{
					case 0:
						return Constants.ALCH_BURN_TIME;
					case 1:
						return Constants.MOBIUS_BURN_TIME;
					case 2:
						return Constants.AETERNALIS_BUR_TIME;
				}
			}
			else if (fuel.getItem() == Item.itemsList[fuelBlock.blockID])
			{
				switch (fuel.getItemDamage())
				{
					case 0:
						return Constants.ALCH_BURN_TIME * 9;
					case 1:
						return Constants.MOBIUS_BURN_TIME * 9;
					case 2:
						return Constants.AETERNALIS_BUR_TIME * 9;
				}
			}
			
			return 0;
		}
	}
}
