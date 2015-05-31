package ic2.core.init;

import ic2.api.recipe.Recipes;
import ic2.core.AdvCraftingRecipeManager;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.RecipeGradual;
import ic2.core.item.ItemGradual;
import ic2.core.util.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Recipies
{
    public static void registerCraftingRecipes()
    {
        AdvCraftingRecipeManager advRecipes = (AdvCraftingRecipeManager) (Recipes.advRecipes = new AdvCraftingRecipeManager());
        advRecipes.addRecipe(Ic2Items.copperBlock, new Object[] {"MMM", "MMM", "MMM", 'M', "ingotCopper"});
        advRecipes.addRecipe(Ic2Items.bronzeBlock, new Object[] {"MMM", "MMM", "MMM", 'M', "ingotBronze"});
        advRecipes.addRecipe(Ic2Items.tinBlock, new Object[] {"MMM", "MMM", "MMM", 'M', "ingotTin"});
        advRecipes.addRecipe(Ic2Items.uraniumBlock, new Object[] {"MMM", "MMM", "MMM", 'M', "ingotUranium"});
        advRecipes.addRecipe(Ic2Items.ironFurnace, new Object[] {"III", "I I", "III", 'I', Item.ingotIron});
        advRecipes.addRecipe(Ic2Items.ironFurnace, new Object[] {" I ", "I I", "IFI", 'I', Item.ingotIron, 'F', Block.furnaceIdle});
        advRecipes.addRecipe(Ic2Items.electroFurnace, new Object[] {" C ", "RFR", 'C', Ic2Items.electronicCircuit, 'R', Item.redstone, 'F', Ic2Items.ironFurnace});
        advRecipes.addRecipe(Ic2Items.macerator, new Object[] {"FFF", "SMS", " C ", 'F', Item.flint, 'S', Block.cobblestone, 'M', Ic2Items.machine, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.extractor, new Object[] {"TMT", "TCT", 'T', Ic2Items.treetap, 'M', Ic2Items.machine, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.compressor, new Object[] {"S S", "SMS", "SCS", 'S', Block.stone, 'M', Ic2Items.machine, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.miner, new Object[] {"CMC", " P ", " P ", 'P', Ic2Items.miningPipe, 'M', Ic2Items.machine, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.pump, new Object[] {"cCc", "cMc", "PTP", 'c', Ic2Items.cell, 'T', Ic2Items.treetap, 'P', Ic2Items.miningPipe, 'M', Ic2Items.machine, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.magnetizer, new Object[] {"RFR", "RMR", "RFR", 'R', Item.redstone, 'F', Ic2Items.ironFence, 'M', Ic2Items.machine});
        advRecipes.addRecipe(Ic2Items.electrolyzer, new Object[] {"c c", "cCc", "EME", 'E', Ic2Items.cell, 'c', Ic2Items.insulatedCopperCableItem, 'M', Ic2Items.machine, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.advancedMachine, new Object[] {" A ", "CMC", " A ", 'A', Ic2Items.advancedAlloy, 'C', Ic2Items.carbonPlate, 'M', Ic2Items.machine});
        advRecipes.addRecipe(Ic2Items.advancedMachine, new Object[] {" C ", "AMA", " C ", 'A', Ic2Items.advancedAlloy, 'C', Ic2Items.carbonPlate, 'M', Ic2Items.machine});
        advRecipes.addRecipe(Ic2Items.personalSafe, new Object[] {"c", "M", "C", 'c', Ic2Items.electronicCircuit, 'C', Block.chest, 'M', Ic2Items.machine});
        advRecipes.addRecipe(Ic2Items.tradeOMat, new Object[] {"RRR", "CMC", 'R', Item.redstone, 'C', Block.chest, 'M', Ic2Items.machine});
        advRecipes.addRecipe(Ic2Items.energyOMat, new Object[] {"RBR", "CMC", 'R', Item.redstone, 'C', Ic2Items.insulatedCopperCableItem, 'M', Ic2Items.machine, 'B', Ic2Items.reBattery});
        advRecipes.addRecipe(Ic2Items.massFabricator, new Object[] {"GCG", "ALA", "GCG", 'A', Ic2Items.advancedMachine, 'L', Ic2Items.lapotronCrystal, 'G', Item.glowstone, 'C', Ic2Items.advancedCircuit});
        advRecipes.addRecipe(Ic2Items.terraformer, new Object[] {"GTG", "DMD", "GDG", 'T', Ic2Items.terraformerBlueprint, 'G', Item.glowstone, 'D', Block.dirt, 'M', Ic2Items.advancedMachine});
        advRecipes.addRecipe(Ic2Items.teleporter, new Object[] {"GFG", "CMC", "GDG", 'M', Ic2Items.advancedMachine, 'C', Ic2Items.glassFiberCableItem, 'F', Ic2Items.frequencyTransmitter, 'G', Ic2Items.advancedCircuit, 'D', Ic2Items.industrialDiamond});
        advRecipes.addRecipe(Ic2Items.teleporter, new Object[] {"GFG", "CMC", "GDG", 'M', Ic2Items.advancedMachine, 'C', Ic2Items.glassFiberCableItem, 'F', Ic2Items.frequencyTransmitter, 'G', Ic2Items.advancedCircuit, 'D', Item.diamond});
        advRecipes.addRecipe(Ic2Items.inductionFurnace, new Object[] {"CCC", "CFC", "CMC", 'C', "ingotCopper", 'F', Ic2Items.electroFurnace, 'M', Ic2Items.advancedMachine});
        advRecipes.addRecipe(Ic2Items.machine, new Object[] {"III", "I I", "III", 'I', "ingotRefinedIron"});
        advRecipes.addRecipe(Ic2Items.recycler, new Object[] {" G ", "DMD", "IDI", 'D', Block.dirt, 'G', Item.glowstone, 'M', Ic2Items.compressor, 'I', "ingotRefinedIron"});
        advRecipes.addRecipe(Ic2Items.canner, new Object[] {"TCT", "TMT", "TTT", 'T', "ingotTin", 'M', Ic2Items.machine, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.teslaCoil, new Object[] {"RRR", "RMR", "ICI", 'M', Ic2Items.mvTransformer, 'R', Item.redstone, 'C', Ic2Items.electronicCircuit, 'I', "ingotRefinedIron"});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.luminator, 8), new Object[] {"ICI", "GTG", "GGG", 'G', Block.glass, 'I', "ingotRefinedIron", 'T', Ic2Items.tinCableItem, 'C', Ic2Items.insulatedCopperCableItem});
        advRecipes.addRecipe(Ic2Items.generator, new Object[] {" B ", "III", " F ", 'B', Ic2Items.reBattery, 'F', Ic2Items.ironFurnace, 'I', "ingotRefinedIron"});
        advRecipes.addRecipe(Ic2Items.generator, new Object[] {" B ", "III", " F ", 'B', Ic2Items.chargedReBattery, 'F', Ic2Items.ironFurnace, 'I', "ingotRefinedIron"});
        advRecipes.addRecipe(Ic2Items.generator, new Object[] {"B", "M", "F", 'B', Ic2Items.reBattery, 'F', Block.furnaceIdle, 'M', Ic2Items.machine});
        advRecipes.addRecipe(Ic2Items.generator, new Object[] {"B", "M", "F", 'B', Ic2Items.chargedReBattery, 'F', Block.furnaceIdle, 'M', Ic2Items.machine});
        advRecipes.addRecipe(Ic2Items.reactorChamber, new Object[] {" C ", "CMC", " C ", 'C', Ic2Items.denseCopperPlate, 'M', Ic2Items.machine});

        if (IC2.energyGeneratorWater > 0)
        {
            advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.waterMill, 2), new Object[] {"SPS", "PGP", "SPS", 'S', "stickWood", 'P', "plankWood", 'G', Ic2Items.generator});
        }

        if (IC2.energyGeneratorSolar > 0)
        {
            advRecipes.addRecipe(Ic2Items.solarPanel, new Object[] {"CgC", "gCg", "cGc", 'G', Ic2Items.generator, 'C', "dustCoal", 'g', Block.glass, 'c', Ic2Items.electronicCircuit});
        }

        if (IC2.energyGeneratorWind > 0)
        {
            advRecipes.addRecipe(Ic2Items.windMill, new Object[] {"I I", " G ", "I I", 'I', Item.ingotIron, 'G', Ic2Items.generator});
        }

        if (IC2.energyGeneratorNuclear > 0)
        {
            advRecipes.addRecipe(Ic2Items.nuclearReactor, new Object[] {" c ", "CCC", " G ", 'C', Ic2Items.reactorChamber, 'c', Ic2Items.advancedCircuit, 'G', Ic2Items.generator});
        }

        if (IC2.energyGeneratorGeo > 0)
        {
            advRecipes.addRecipe(Ic2Items.geothermalGenerator, new Object[] {"gCg", "gCg", "IGI", 'G', Ic2Items.generator, 'C', Ic2Items.cell, 'g', Block.glass, 'I', "ingotRefinedIron"});
        }

        advRecipes.addShapelessRecipe(Ic2Items.reactorUraniumSimple, new Object[] {Ic2Items.reEnrichedUraniumCell, "dustCoal"});
        advRecipes.addShapelessRecipe(new ItemStack(Ic2Items.reactorIsotopeCell.itemID, 1, 9999), new Object[] {Ic2Items.nearDepletedUraniumCell, "dustCoal"});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.nearDepletedUraniumCell, 8), new Object[] {"CCC", "CUC", "CCC", 'C', Ic2Items.cell, 'U', "ingotUranium"});
        advRecipes.addShapelessRecipe(Ic2Items.reactorUraniumSimple, new Object[] {Ic2Items.cell, "ingotUranium"});
        advRecipes.addRecipe(Ic2Items.reactorUraniumDual, new Object[] {"UCU", 'U', Ic2Items.reactorUraniumSimple, 'C', Ic2Items.denseCopperPlate});
        advRecipes.addRecipe(Ic2Items.reactorUraniumQuad, new Object[] {" U ", "CCC", " U ", 'U', Ic2Items.reactorUraniumDual, 'C', Ic2Items.denseCopperPlate});
        advRecipes.addRecipe(Ic2Items.reactorCoolantSimple, new Object[] {" T ", "TWT", " T ", 'W', "liquid$water", 'T', "ingotTin"});
        advRecipes.addRecipe(Ic2Items.reactorCoolantTriple, new Object[] {"TTT", "CCC", "TTT", 'C', Ic2Items.reactorCoolantSimple, 'T', "ingotTin"});
        advRecipes.addRecipe(Ic2Items.reactorCoolantSix, new Object[] {"TCT", "TcT", "TCT", 'C', Ic2Items.reactorCoolantTriple, 'T', "ingotTin", 'c', Ic2Items.denseCopperPlate});
        advRecipes.addShapelessRecipe(Ic2Items.reactorPlating, new Object[] {Ic2Items.denseCopperPlate, Ic2Items.advancedAlloy});
        advRecipes.addShapelessRecipe(Ic2Items.reactorPlatingHeat, new Object[] {Ic2Items.reactorPlating, Ic2Items.denseCopperPlate, Ic2Items.denseCopperPlate});
        advRecipes.addShapelessRecipe(Ic2Items.reactorPlatingExplosive, new Object[] {Ic2Items.reactorPlating, Ic2Items.advancedAlloy, Ic2Items.advancedAlloy});
        advRecipes.addRecipe(Ic2Items.reactorHeatSwitch, new Object[] {" c ", "TCT", " T ", 'c', Ic2Items.electronicCircuit, 'T', "ingotTin", 'C', Ic2Items.denseCopperPlate});
        advRecipes.addRecipe(Ic2Items.reactorHeatSwitchCore, new Object[] {"C", "S", "C", 'S', Ic2Items.reactorHeatSwitch, 'C', Ic2Items.denseCopperPlate});
        advRecipes.addRecipe(Ic2Items.reactorHeatSwitchSpread, new Object[] {" G ", "GSG", " G ", 'S', Ic2Items.reactorHeatSwitch, 'G', Item.ingotGold});
        advRecipes.addRecipe(Ic2Items.reactorHeatSwitchDiamond, new Object[] {"GcG", "SCS", "GcG", 'S', Ic2Items.reactorHeatSwitch, 'C', Ic2Items.denseCopperPlate, 'G', Ic2Items.glassFiberCableItem, 'c', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.reactorVent, new Object[] {"I#I", "# #", "I#I", 'I', Ic2Items.refinedIronIngot, '#', Block.fenceIron});
        advRecipes.addRecipe(Ic2Items.reactorVentCore, new Object[] {"C", "V", "C", 'V', Ic2Items.reactorVent, 'C', Ic2Items.denseCopperPlate});
        advRecipes.addRecipe(Ic2Items.reactorVentGold, new Object[] {"G", "V", "G", 'V', Ic2Items.reactorVentCore, 'G', Item.ingotGold});
        advRecipes.addRecipe(Ic2Items.reactorVentSpread, new Object[] {"#T#", "TVT", "#T#", 'V', Ic2Items.reactorVent, '#', Block.fenceIron, 'T', "ingotTin"});
        advRecipes.addRecipe(Ic2Items.reactorVentDiamond, new Object[] {"#V#", "#D#", "#V#", 'V', Ic2Items.reactorVent, '#', Block.fenceIron, 'D', Item.diamond});
        advRecipes.addRecipe(Ic2Items.reactorHeatpack, new Object[] {"c", "L", "C", 'c', Ic2Items.electronicCircuit, 'C', Ic2Items.denseCopperPlate, 'L', Ic2Items.lavaCell});
        advRecipes.addRecipe(Ic2Items.reactorReflector, new Object[] {"TcT", "cCc", "TcT", 'c', "dustCoal", 'C', Ic2Items.denseCopperPlate, 'T', "dustTin"});
        advRecipes.addRecipe(Ic2Items.reactorReflectorThick, new Object[] {" R ", "RCR", " R ", 'C', Ic2Items.denseCopperPlate, 'R', Ic2Items.reactorReflector});
        advRecipes.addRecipe(Ic2Items.reactorCondensator, new Object[] {"RRR", "RVR", "RSR", 'R', Item.redstone, 'V', Ic2Items.reactorVent, 'S', Ic2Items.reactorHeatSwitch});
        new RecipeGradual((ItemGradual)Ic2Items.reactorCondensator.getItem(), new ItemStack(Item.redstone), 10000);
        advRecipes.addRecipe(Ic2Items.reactorCondensatorLap, new Object[] {"RVR", "CLC", "RSR", 'R', Item.redstone, 'V', Ic2Items.reactorVentCore, 'S', Ic2Items.reactorHeatSwitchCore, 'C', Ic2Items.reactorCondensator, 'L', Block.blockLapis});
        new RecipeGradual((ItemGradual)Ic2Items.reactorCondensatorLap.getItem(), new ItemStack(Item.redstone), 5000);
        new RecipeGradual((ItemGradual)Ic2Items.reactorCondensatorLap.getItem(), new ItemStack(Item.dyePowder, 1, 4), 40000);
        advRecipes.addRecipe(Ic2Items.batBox, new Object[] {"PCP", "BBB", "PPP", 'P', "plankWood", 'C', Ic2Items.insulatedCopperCableItem, 'B', Ic2Items.reBattery});
        advRecipes.addRecipe(Ic2Items.batBox, new Object[] {"PCP", "BBB", "PPP", 'P', "plankWood", 'C', Ic2Items.insulatedCopperCableItem, 'B', Ic2Items.chargedReBattery});
        advRecipes.addRecipe(Ic2Items.mfeUnit, new Object[] {"cCc", "CMC", "cCc", 'M', Ic2Items.machine, 'c', Ic2Items.doubleInsulatedGoldCableItem, 'C', Ic2Items.energyCrystal});
        advRecipes.addRecipe(Ic2Items.mfsUnit, new Object[] {"LCL", "LML", "LAL", 'M', Ic2Items.mfeUnit, 'A', Ic2Items.advancedMachine, 'C', Ic2Items.advancedCircuit, 'L', Ic2Items.lapotronCrystal});
        advRecipes.addRecipe(Ic2Items.lvTransformer, new Object[] {"PCP", "ccc", "PCP", 'P', "plankWood", 'C', Ic2Items.insulatedCopperCableItem, 'c', "ingotCopper"});
        advRecipes.addRecipe(Ic2Items.mvTransformer, new Object[] {" C ", " M ", " C ", 'M', Ic2Items.machine, 'C', Ic2Items.doubleInsulatedGoldCableItem});
        advRecipes.addRecipe(Ic2Items.hvTransformer, new Object[] {" c ", "CED", " c ", 'E', Ic2Items.mvTransformer, 'c', Ic2Items.trippleInsulatedIronCableItem, 'D', Ic2Items.energyCrystal, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.reinforcedStone, 8), new Object[] {"SSS", "SAS", "SSS", 'S', Block.stone, 'A', Ic2Items.advancedAlloy});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.reinforcedGlass, 7), new Object[] {"GAG", "GGG", "GAG", 'G', Block.glass, 'A', Ic2Items.advancedAlloy});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.reinforcedGlass, 7), new Object[] {"GGG", "AGA", "GGG", 'G', Block.glass, 'A', Ic2Items.advancedAlloy});
        advRecipes.addRecipe(Ic2Items.remote, new Object[] {" c ", "GCG", "TTT", 'c', Ic2Items.insulatedCopperCableItem, 'G', Item.glowstone, 'C', Ic2Items.electronicCircuit, 'T', Block.tnt});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.rubberTrampoline, 3), new Object[] {"RRR", "RRR", 'R', "itemRubber"});
        advRecipes.addRecipe(new ItemStack(Block.torchWood, 4), new Object[] {"R", "I", 'I', "stickWood", 'R', Ic2Items.resin, Boolean.valueOf(true)});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.scaffold, 16), new Object[] {"PPP", " s ", "s s", 'P', "plankWood", 's', "stickWood"});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.ironScaffold, 16), new Object[] {"PPP", " s ", "s s", 'P', "ingotRefinedIron", 's', Ic2Items.ironFence.getItem()});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.ironFence, 12), new Object[] {"III", "III", 'I', "ingotRefinedIron"});

        if (IC2.enableCraftingITnt)
        {
            advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.industrialTnt, 4), new Object[] {"FFF", "TTT", "FFF", 'F', Item.flint, 'T', Block.tnt});
            advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.industrialTnt, 4), new Object[] {"FTF", "FTF", "FTF", 'F', Item.flint, 'T', Block.tnt});
        }

        if (IC2.enableCraftingNuke)
        {
            advRecipes.addRecipe(Ic2Items.nuke, new Object[] {"RCR", "UMU", "RCR", 'R', Ic2Items.reEnrichedUraniumCell, 'C', Ic2Items.advancedCircuit, 'U', "blockUranium", 'M', Ic2Items.advancedMachine});
        }

        advRecipes.addRecipe(new ItemStack(Block.stone, 16), new Object[] {"   ", " M ", "   ", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Block.glass, 32), new Object[] {" M ", "M M", " M ", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Block.grass, 16), new Object[] {"   ", "M  ", "M  ", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Block.cobblestoneMossy, 16), new Object[] {"   ", " M ", "M M", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Block.sandStone, 16), new Object[] {"   ", "  M", " M ", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Block.blockSnow, 4), new Object[] {"M M", "   ", "   ", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Block.waterStill, 1), new Object[] {"   ", " M ", " M ", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Block.lavaStill, 1), new Object[] {" M ", " M ", " M ", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Block.oreIron, 2), new Object[] {"M M", " M ", "M M", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Block.oreGold, 2), new Object[] {" M ", "MMM", " M ", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Block.obsidian, 12), new Object[] {"M M", "M M", "   ", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Block.netherrack, 16), new Object[] {"  M", " M ", "M  ", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Block.glowStone, 8), new Object[] {" M ", "M M", "MMM", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Block.wood, 8), new Object[] {" M ", "   ", "   ", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Block.cactus, 48), new Object[] {" M ", "MMM", "M M", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Block.vine, 24), new Object[] {"M  ", "M  ", "M  ", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Block.cloth, 12), new Object[] {"M M", "   ", " M ", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Item.coal, 20), new Object[] {"  M", "M  ", "  M", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Item.diamond, 1), new Object[] {"MMM", "MMM", "MMM", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Item.redstone, 24), new Object[] {"   ", " M ", "MMM", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Item.dyePowder, 9, 4), new Object[] {" M ", " M ", " MM", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Item.feather, 32), new Object[] {" M ", " M ", "M M", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Item.snowball, 16), new Object[] {"   ", "   ", "MMM", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Item.gunpowder, 15), new Object[] {"MMM", "M  ", "MMM", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Item.clay, 48), new Object[] {"MM ", "M  ", "MM ", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Item.dyePowder, 32, 3), new Object[] {"MM ", "  M", "MM ", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Item.dyePowder, 48, 0), new Object[] {" MM", " MM", " M ", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Item.reed, 48), new Object[] {"M M", "M M", "M M", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Item.flint, 32), new Object[] {" M ", "MM ", "MM ", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Item.bone, 32), new Object[] {"M  ", "MM ", "M  ", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.resin, 21), new Object[] {"M M", "   ", "M M", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.iridiumOre, 1), new Object[] {"MMM", " M ", "MMM", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Block.mycelium, 24), new Object[] {"   ", "M M", "MMM", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Block.stoneBrick, 48, 3), new Object[] {"MM ", "MM ", "M  ", 'M', Ic2Items.matter, Boolean.valueOf(true)});

        if (Ic2Items.copperOre != null)
        {
            advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.copperOre, 5), new Object[] {"  M", "M M", "   ", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        }
        else
        {
            advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.copperDust, 10), new Object[] {"  M", "M M", "   ", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        }

        if (Ic2Items.tinOre != null)
        {
            advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.tinOre, 5), new Object[] {"   ", "M M", "  M", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        }
        else
        {
            advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.tinDust, 10), new Object[] {"   ", "M M", "  M", 'M', Ic2Items.matter, Boolean.valueOf(true)});
        }

        if (Ic2Items.rubberWood != null)
        {
            advRecipes.addRecipe(new ItemStack(Block.planks, 3, 3), new Object[] {"W", 'W', Ic2Items.rubberWood});
        }

        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.insulatedCopperCableItem, 6), new Object[] {"RRR", "CCC", "RRR", 'C', "ingotCopper", 'R', "itemRubber"});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.insulatedCopperCableItem, 6), new Object[] {"RCR", "RCR", "RCR", 'C', "ingotCopper", 'R', "itemRubber"});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.copperCableItem, 6), new Object[] {"CCC", 'C', "ingotCopper"});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.goldCableItem, 12), new Object[] {"GGG", 'G', Item.ingotGold});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.insulatedGoldCableItem, 4), new Object[] {" R ", "RGR", " R ", 'G', Item.ingotGold, 'R', "itemRubber"});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.glassFiberCableItem, 4), new Object[] {"GGG", "RDR", "GGG", 'G', Block.glass, 'R', Item.redstone, 'D', Item.diamond});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.glassFiberCableItem, 4), new Object[] {"GGG", "RDR", "GGG", 'G', Block.glass, 'R', Item.redstone, 'D', Ic2Items.industrialDiamond});
        advRecipes.addRecipe(Ic2Items.detectorCableItem, new Object[] {" C ", "RIR", " R ", 'R', Item.redstone, 'I', Ic2Items.trippleInsulatedIronCableItem, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.splitterCableItem, new Object[] {" R ", "ILI", " R ", 'R', Item.redstone, 'I', Ic2Items.trippleInsulatedIronCableItem, 'L', Block.lever});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.ironCableItem, 12), new Object[] {"III", 'I', "ingotRefinedIron"});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.insulatedIronCableItem, 4), new Object[] {" R ", "RIR", " R ", 'I', "ingotRefinedIron", 'R', "itemRubber"});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.glassFiberCableItem, 6), new Object[] {"GGG", "SDS", "GGG", 'G', Block.glass, 'S', "ingotSilver", 'R', Item.redstone, 'D', Item.diamond});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.glassFiberCableItem, 6), new Object[] {"GGG", "SDS", "GGG", 'G', Block.glass, 'S', "ingotSilver", 'R', Item.redstone, 'D', Ic2Items.industrialDiamond});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.tinCableItem, 9), new Object[] {"TTT", 'T', "ingotTin"});
        advRecipes.addShapelessRecipe(Ic2Items.insulatedCopperCableItem, new Object[] {"itemRubber", Ic2Items.copperCableItem});
        advRecipes.addShapelessRecipe(Ic2Items.insulatedGoldCableItem, new Object[] {"itemRubber", Ic2Items.goldCableItem});
        advRecipes.addShapelessRecipe(Ic2Items.doubleInsulatedGoldCableItem, new Object[] {"itemRubber", Ic2Items.insulatedGoldCableItem});
        advRecipes.addShapelessRecipe(Ic2Items.doubleInsulatedGoldCableItem, new Object[] {"itemRubber", "itemRubber", Ic2Items.goldCableItem});
        advRecipes.addShapelessRecipe(Ic2Items.insulatedIronCableItem, new Object[] {"itemRubber", Ic2Items.ironCableItem});
        advRecipes.addShapelessRecipe(Ic2Items.doubleInsulatedIronCableItem, new Object[] {"itemRubber", Ic2Items.insulatedIronCableItem});
        advRecipes.addShapelessRecipe(Ic2Items.trippleInsulatedIronCableItem, new Object[] {"itemRubber", Ic2Items.doubleInsulatedIronCableItem});
        advRecipes.addShapelessRecipe(Ic2Items.trippleInsulatedIronCableItem, new Object[] {"itemRubber", "itemRubber", Ic2Items.insulatedIronCableItem});
        advRecipes.addShapelessRecipe(Ic2Items.doubleInsulatedIronCableItem, new Object[] {"itemRubber", "itemRubber", Ic2Items.ironCableItem});
        advRecipes.addShapelessRecipe(Ic2Items.trippleInsulatedIronCableItem, new Object[] {"itemRubber", "itemRubber", "itemRubber", Ic2Items.ironCableItem});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.suBattery, 5), new Object[] {"C", "R", "D", 'D', "dustCoal", 'R', Item.redstone, 'C', Ic2Items.insulatedCopperCableItem});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.suBattery, 5), new Object[] {"C", "D", "R", 'D', "dustCoal", 'R', Item.redstone, 'C', Ic2Items.insulatedCopperCableItem});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.suBattery, 8), new Object[] {"c", "C", "R", 'R', Item.redstone, 'C', Ic2Items.hydratedCoalDust, 'c', Ic2Items.insulatedCopperCableItem});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.suBattery, 8), new Object[] {"c", "R", "C", 'R', Item.redstone, 'C', Ic2Items.hydratedCoalDust, 'c', Ic2Items.insulatedCopperCableItem});
        advRecipes.addRecipe(Ic2Items.reBattery, new Object[] {" C ", "TRT", "TRT", 'T', "ingotTin", 'R', Item.redstone, 'C', Ic2Items.insulatedCopperCableItem});
        advRecipes.addRecipe(Ic2Items.energyCrystal, new Object[] {"RRR", "RDR", "RRR", 'D', Item.diamond, 'R', Item.redstone});
        advRecipes.addRecipe(Ic2Items.energyCrystal, new Object[] {"RRR", "RDR", "RRR", 'D', Ic2Items.industrialDiamond, 'R', Item.redstone});
        advRecipes.addRecipe(Ic2Items.lapotronCrystal, new Object[] {"LCL", "LDL", "LCL", 'D', Ic2Items.energyCrystal, 'C', Ic2Items.electronicCircuit, 'L', new ItemStack(Item.dyePowder, 1, 4)});
        advRecipes.addRecipe(Ic2Items.treetap, new Object[] {" P ", "PPP", "P  ", 'P', "plankWood"});
        advRecipes.addRecipe(Ic2Items.painter, new Object[] {" CC", " IC", "I  ", 'C', Block.cloth, 'I', Item.ingotIron});
        advRecipes.addRecipe(new ItemStack(Item.pickaxeDiamond, 1), new Object[] {"DDD", " S ", " S ", 'S', "stickWood", 'D', Ic2Items.industrialDiamond});
        advRecipes.addRecipe(new ItemStack(Item.hoeDiamond, 1), new Object[] {"DD ", " S ", " S ", 'S', "stickWood", 'D', Ic2Items.industrialDiamond});
        advRecipes.addRecipe(new ItemStack(Item.shovelDiamond, 1), new Object[] {"D", "S", "S", 'S', "stickWood", 'D', Ic2Items.industrialDiamond});
        advRecipes.addRecipe(new ItemStack(Item.axeDiamond, 1), new Object[] {"DD ", "DS ", " S ", 'S', "stickWood", 'D', Ic2Items.industrialDiamond});
        advRecipes.addRecipe(new ItemStack(Item.swordDiamond, 1), new Object[] {"D", "D", "S", 'S', "stickWood", 'D', Ic2Items.industrialDiamond});
        advRecipes.addRecipe(new ItemStack(Ic2Items.constructionFoamSprayer.itemID, 1, 1601), new Object[] {"SS ", "Ss ", "  S", 'S', Block.cobblestone, 's', "stickWood"});
        new RecipeGradual((ItemGradual)Ic2Items.constructionFoamSprayer.getItem(), Ic2Items.constructionFoamPellet, 100);
        advRecipes.addRecipe(Ic2Items.bronzePickaxe, new Object[] {"BBB", " S ", " S ", 'B', "ingotBronze", 'S', "stickWood"});
        advRecipes.addRecipe(Ic2Items.bronzeAxe, new Object[] {"BB", "SB", "S ", 'B', "ingotBronze", 'S', "stickWood"});
        advRecipes.addRecipe(Ic2Items.bronzeHoe, new Object[] {"BB", "S ", "S ", 'B', "ingotBronze", 'S', "stickWood"});
        advRecipes.addRecipe(Ic2Items.bronzeSword, new Object[] {"B", "B", "S", 'B', "ingotBronze", 'S', "stickWood"});
        advRecipes.addRecipe(Ic2Items.bronzeShovel, new Object[] {" B ", " S ", " S ", 'B', "ingotBronze", 'S', "stickWood"});
        advRecipes.addRecipe(Ic2Items.wrench, new Object[] {"B B", "BBB", " B ", 'B', "ingotBronze"});
        advRecipes.addRecipe(Ic2Items.cutter, new Object[] {"A A", " A ", "I I", 'A', "ingotRefinedIron", 'I', Item.ingotIron});
        advRecipes.addRecipe(Ic2Items.toolbox, new Object[] {"ICI", "III", 'C', Block.chest, 'I', "ingotRefinedIron"});
        advRecipes.addRecipe(Ic2Items.miningDrill, new Object[] {" I ", "ICI", "IBI", 'I', "ingotRefinedIron", 'B', Ic2Items.reBattery, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.miningDrill, new Object[] {" I ", "ICI", "IBI", 'I', "ingotRefinedIron", 'B', Ic2Items.chargedReBattery, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.chainsaw, new Object[] {" II", "ICI", "BI ", 'I', "ingotRefinedIron", 'B', Ic2Items.reBattery, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.chainsaw, new Object[] {" II", "ICI", "BI ", 'I', "ingotRefinedIron", 'B', Ic2Items.chargedReBattery, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.diamondDrill, new Object[] {" D ", "DdD", 'D', Item.diamond, 'd', Ic2Items.miningDrill});
        advRecipes.addRecipe(Ic2Items.diamondDrill, new Object[] {" D ", "DdD", 'D', Item.diamond, 'd', Ic2Items.miningDrill});
        advRecipes.addRecipe(Ic2Items.odScanner, new Object[] {" G ", "CBC", "ccc", 'B', Ic2Items.reBattery, 'c', Ic2Items.insulatedCopperCableItem, 'G', Item.glowstone, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.odScanner, new Object[] {" G ", "CBC", "ccc", 'B', Ic2Items.chargedReBattery, 'c', Ic2Items.insulatedCopperCableItem, 'G', Item.glowstone, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.ovScanner, new Object[] {" G ", "GCG", "cSc", 'S', Ic2Items.odScanner, 'c', Ic2Items.doubleInsulatedGoldCableItem, 'G', Item.glowstone, 'C', Ic2Items.advancedCircuit});
        advRecipes.addRecipe(Ic2Items.ovScanner, new Object[] {" G ", "GCG", "cSc", 'S', Ic2Items.chargedReBattery, 'c', Ic2Items.doubleInsulatedGoldCableItem, 'G', Item.glowstone, 'C', Ic2Items.advancedCircuit});
        advRecipes.addRecipe(Ic2Items.obscurator, new Object[] {"rEr", "CAC", "rrr", 'r', Item.redstone, 'E', Ic2Items.energyCrystal, 'C', Ic2Items.doubleInsulatedGoldCableItem, 'A', Ic2Items.advancedCircuit});
        advRecipes.addRecipe(Ic2Items.electricWrench, new Object[] {"  W", " C ", "B  ", 'W', Ic2Items.wrench, 'B', Ic2Items.reBattery, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.electricWrench, new Object[] {"  W", " C ", "B  ", 'W', Ic2Items.wrench, 'B', Ic2Items.chargedReBattery, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.electricTreetap, new Object[] {"  W", " C ", "B  ", 'W', Ic2Items.treetap, 'B', Ic2Items.reBattery, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.electricTreetap, new Object[] {"  W", " C ", "B  ", 'W', Ic2Items.treetap, 'B', Ic2Items.chargedReBattery, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.ecMeter, new Object[] {" G ", "cCc", "c c", 'G', Item.glowstone, 'c', Ic2Items.insulatedCopperCableItem, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.miningLaser, new Object[] {"Rcc", "AAC", " AA", 'A', Ic2Items.advancedAlloy, 'C', Ic2Items.advancedCircuit, 'c', Ic2Items.energyCrystal, 'R', Item.redstone});
        advRecipes.addRecipe(Ic2Items.nanoSaber, new Object[] {"GA ", "GA ", "CcC", 'C', Ic2Items.carbonPlate, 'c', Ic2Items.energyCrystal, 'G', Item.glowstone, 'A', Ic2Items.advancedAlloy});
        advRecipes.addRecipe(Ic2Items.electricHoe, new Object[] {"II ", " C ", " B ", 'I', "ingotRefinedIron", 'B', Ic2Items.reBattery, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.electricHoe, new Object[] {"II ", " C ", " B ", 'I', "ingotRefinedIron", 'B', Ic2Items.chargedReBattery, 'C', Ic2Items.electronicCircuit});
        advRecipes.addShapelessRecipe(Ic2Items.frequencyTransmitter, new Object[] {Ic2Items.electronicCircuit, Ic2Items.insulatedCopperCableItem});
        advRecipes.addRecipe(Ic2Items.advancedCircuit, new Object[] {"RGR", "LCL", "RGR", 'L', new ItemStack(Item.dyePowder, 1, 4), 'G', Item.glowstone, 'R', Item.redstone, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.advancedCircuit, new Object[] {"RLR", "GCG", "RLR", 'L', new ItemStack(Item.dyePowder, 1, 4), 'G', Item.glowstone, 'R', Item.redstone, 'C', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.plantBall, new Object[] {"PPP", "P P", "PPP", 'P', Item.wheat});
        advRecipes.addRecipe(Ic2Items.plantBall, new Object[] {"PPP", "P P", "PPP", 'P', Item.reed});
        advRecipes.addRecipe(Ic2Items.plantBall, new Object[] {"PPP", "P P", "PPP", 'P', Item.seeds});
        advRecipes.addRecipe(Ic2Items.plantBall, new Object[] {"PPP", "P P", "PPP", 'P', "treeLeaves"});

        if (Ic2Items.rubberLeaves != null)
        {
            advRecipes.addRecipe(Ic2Items.plantBall, new Object[] {"PPP", "P P", "PPP", 'P', Ic2Items.rubberLeaves});
        }

        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.plantBall, 2), new Object[] {"PPP", "P P", "PPP", 'P', "treeSapling"});

        if (Ic2Items.rubberSapling != null)
        {
            advRecipes.addRecipe(Ic2Items.plantBall, new Object[] {"PPP", "P P", "PPP", 'P', Ic2Items.rubberSapling});
        }

        advRecipes.addRecipe(Ic2Items.plantBall, new Object[] {"PPP", "P P", "PPP", 'P', Block.tallGrass});
        advRecipes.addRecipe(Ic2Items.carbonFiber, new Object[] {"CC", "CC", 'C', "dustCoal"});
        advRecipes.addRecipe(Ic2Items.iridiumPlate, new Object[] {"IAI", "ADA", "IAI", 'I', Ic2Items.iridiumOre, 'A', Ic2Items.advancedAlloy, 'D', Item.diamond});
        advRecipes.addRecipe(Ic2Items.iridiumPlate, new Object[] {"IAI", "ADA", "IAI", 'I', Ic2Items.iridiumOre, 'A', Ic2Items.advancedAlloy, 'D', Ic2Items.industrialDiamond});
        advRecipes.addRecipe(Ic2Items.coalBall, new Object[] {"CCC", "CFC", "CCC", 'C', "dustCoal", 'F', Item.flint});
        advRecipes.addRecipe(Ic2Items.coalChunk, new Object[] {"###", "#O#", "###", '#', Ic2Items.compressedCoalBall, 'O', Block.obsidian});
        advRecipes.addRecipe(Ic2Items.coalChunk, new Object[] {"###", "#O#", "###", '#', Ic2Items.compressedCoalBall, 'O', Block.blockIron, Boolean.valueOf(true)});
        advRecipes.addRecipe(Ic2Items.coalChunk, new Object[] {"###", "#O#", "###", '#', Ic2Items.compressedCoalBall, 'O', Block.brick, Boolean.valueOf(true)});
        advRecipes.addRecipe(Ic2Items.smallIronDust, new Object[] {"CTC", "TCT", "CTC", 'C', "dustCopper", 'T', "dustTin", Boolean.valueOf(true)});
        advRecipes.addRecipe(Ic2Items.smallIronDust, new Object[] {"TCT", "CTC", "TCT", 'C', "dustCopper", 'T', "dustTin", Boolean.valueOf(true)});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.hydratedCoalDust, 8), new Object[] {"CCC", "CWC", "CCC", 'C', "dustCoal", 'W', "liquid$water"});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.refinedIronIngot, 8), new Object[] {"M", 'M', Ic2Items.machine});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.copperIngot, 9), new Object[] {"B", 'B', Ic2Items.copperBlock});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.tinIngot, 9), new Object[] {"B", 'B', Ic2Items.tinBlock});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.bronzeIngot, 9), new Object[] {"B", 'B', Ic2Items.bronzeBlock});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.uraniumIngot, 9), new Object[] {"B", 'B', Ic2Items.uraniumBlock});
        advRecipes.addRecipe(Ic2Items.electronicCircuit, new Object[] {"CCC", "RIR", "CCC", 'I', "ingotRefinedIron", 'R', Item.redstone, 'C', Ic2Items.insulatedCopperCableItem});
        advRecipes.addRecipe(Ic2Items.electronicCircuit, new Object[] {"CRC", "CIC", "CRC", 'I', "ingotRefinedIron", 'R', Item.redstone, 'C', Ic2Items.insulatedCopperCableItem});
        advRecipes.addRecipe(Ic2Items.compositeArmor, new Object[] {"A A", "ALA", "AIA", 'L', Item.plateLeather, 'I', Item.plateIron, 'A', Ic2Items.advancedAlloy});
        advRecipes.addRecipe(Ic2Items.compositeArmor, new Object[] {"A A", "AIA", "ALA", 'L', Item.plateLeather, 'I', Item.plateIron, 'A', Ic2Items.advancedAlloy});
        advRecipes.addRecipe(Ic2Items.nanoHelmet, new Object[] {"CcC", "CGC", 'C', Ic2Items.carbonPlate, 'c', Ic2Items.energyCrystal, 'G', Block.glass});
        advRecipes.addRecipe(Ic2Items.nanoBodyarmor, new Object[] {"C C", "CcC", "CCC", 'C', Ic2Items.carbonPlate, 'c', Ic2Items.energyCrystal});
        advRecipes.addRecipe(Ic2Items.nanoLeggings, new Object[] {"CcC", "C C", "C C", 'C', Ic2Items.carbonPlate, 'c', Ic2Items.energyCrystal});
        advRecipes.addRecipe(Ic2Items.nanoBoots, new Object[] {"C C", "CcC", 'C', Ic2Items.carbonPlate, 'c', Ic2Items.energyCrystal});
        advRecipes.addRecipe(Ic2Items.quantumHelmet, new Object[] {" n ", "ILI", "CGC", 'n', Ic2Items.nanoHelmet, 'I', Ic2Items.iridiumPlate, 'L', Ic2Items.lapotronCrystal, 'G', Ic2Items.reinforcedGlass, 'C', Ic2Items.advancedCircuit});
        advRecipes.addRecipe(Ic2Items.quantumBodyarmor, new Object[] {"AnA", "ILI", "IAI", 'n', Ic2Items.nanoBodyarmor, 'I', Ic2Items.iridiumPlate, 'L', Ic2Items.lapotronCrystal, 'A', Ic2Items.advancedAlloy});
        advRecipes.addRecipe(Ic2Items.quantumLeggings, new Object[] {"MLM", "InI", "G G", 'n', Ic2Items.nanoLeggings, 'I', Ic2Items.iridiumPlate, 'L', Ic2Items.lapotronCrystal, 'G', Item.glowstone, 'M', Ic2Items.machine});
        advRecipes.addRecipe(Ic2Items.quantumBoots, new Object[] {"InI", "RLR", 'n', Ic2Items.nanoBoots, 'I', Ic2Items.iridiumPlate, 'L', Ic2Items.lapotronCrystal, 'R', Ic2Items.hazmatBoots});
        advRecipes.addRecipe(Ic2Items.hazmatHelmet, new Object[] {" O ", "RGR", "R#R", 'R', "itemRubber", 'G', Block.glass, '#', Block.fenceIron, 'O', new ItemStack(Item.dyePowder, 1, 14)});
        advRecipes.addRecipe(Ic2Items.hazmatChestplate, new Object[] {"R R", "ROR", "ROR", 'R', "itemRubber", 'O', new ItemStack(Item.dyePowder, 1, 14)});
        advRecipes.addRecipe(Ic2Items.hazmatLeggings, new Object[] {"ROR", "R R", "R R", 'R', "itemRubber", 'O', new ItemStack(Item.dyePowder, 1, 14)});
        advRecipes.addRecipe(Ic2Items.hazmatBoots, new Object[] {"R R", "R R", "RWR", 'R', "itemRubber", 'W', Block.cloth});
        advRecipes.addRecipe(Ic2Items.batPack, new Object[] {"BCB", "BTB", "B B", 'T', "ingotTin", 'C', Ic2Items.electronicCircuit, 'B', Ic2Items.chargedReBattery});
        advRecipes.addRecipe(Ic2Items.batPack, new Object[] {"BCB", "BTB", "B B", 'T', "ingotTin", 'C', Ic2Items.electronicCircuit, 'B', Ic2Items.reBattery});
        advRecipes.addRecipe(Ic2Items.lapPack, new Object[] {"LAL", "LBL", "L L", 'L', Block.blockLapis, 'A', Ic2Items.advancedCircuit, 'B', Ic2Items.batPack});
        advRecipes.addRecipe(Ic2Items.solarHelmet, new Object[] {"III", "ISI", "CCC", 'I', Item.ingotIron, 'S', Ic2Items.solarPanel, 'C', Ic2Items.insulatedCopperCableItem});
        advRecipes.addRecipe(Ic2Items.solarHelmet, new Object[] {" H ", " S ", "CCC", 'H', Item.helmetIron, 'S', Ic2Items.solarPanel, 'C', Ic2Items.insulatedCopperCableItem});
        advRecipes.addRecipe(Ic2Items.staticBoots, new Object[] {"I I", "ISI", "CCC", 'I', Item.ingotIron, 'S', Block.cloth, 'C', Ic2Items.insulatedCopperCableItem});
        advRecipes.addRecipe(Ic2Items.staticBoots, new Object[] {" H ", " S ", "CCC", 'H', Item.bootsIron, 'S', Block.cloth, 'C', Ic2Items.insulatedCopperCableItem});
        advRecipes.addRecipe(Ic2Items.nightvisionGoggles, new Object[] {"X@X", "LGL", "RCR", 'X', Ic2Items.reactorHeatSwitchDiamond, '@', Ic2Items.nanoHelmet, 'L', Ic2Items.luminator, 'G', Ic2Items.reinforcedGlass, 'R', "itemRubber", 'C', Ic2Items.advancedCircuit});
        advRecipes.addRecipe(Ic2Items.bronzeHelmet, new Object[] {"BBB", "B B", 'B', "ingotBronze"});
        advRecipes.addRecipe(Ic2Items.bronzeChestplate, new Object[] {"B B", "BBB", "BBB", 'B', "ingotBronze"});
        advRecipes.addRecipe(Ic2Items.bronzeLeggings, new Object[] {"BBB", "B B", "B B", 'B', "ingotBronze"});
        advRecipes.addRecipe(Ic2Items.bronzeBoots, new Object[] {"B B", "B B", 'B', "ingotBronze"});
        advRecipes.addRecipe(new ItemStack(Ic2Items.jetpack.itemID, 1, 18001), new Object[] {"ICI", "IFI", "R R", 'I', "ingotRefinedIron", 'C', Ic2Items.electronicCircuit, 'F', Ic2Items.fuelCan, 'R', Item.redstone});
        advRecipes.addRecipe(Ic2Items.electricJetpack, new Object[] {"ICI", "IBI", "G G", 'I', "ingotRefinedIron", 'C', Ic2Items.advancedCircuit, 'B', Ic2Items.batBox, 'G', Item.glowstone});
        advRecipes.addRecipe(Ic2Items.terraformerBlueprint, new Object[] {" C ", " A ", "R R", 'C', Ic2Items.electronicCircuit, 'A', Ic2Items.advancedCircuit, 'R', Item.redstone});
        advRecipes.addRecipe(Ic2Items.cultivationTerraformerBlueprint, new Object[] {" S ", "S#S", " S ", '#', Ic2Items.terraformerBlueprint, 'S', Item.seeds});
        advRecipes.addRecipe(Ic2Items.desertificationTerraformerBlueprint, new Object[] {" S ", "S#S", " S ", '#', Ic2Items.terraformerBlueprint, 'S', Block.sand});
        advRecipes.addRecipe(Ic2Items.irrigationTerraformerBlueprint, new Object[] {" W ", "W#W", " W ", '#', Ic2Items.terraformerBlueprint, 'W', Item.bucketWater});
        advRecipes.addRecipe(Ic2Items.chillingTerraformerBlueprint, new Object[] {" S ", "S#S", " S ", '#', Ic2Items.terraformerBlueprint, 'S', Item.snowball});
        advRecipes.addRecipe(Ic2Items.flatificatorTerraformerBlueprint, new Object[] {" D ", "D#D", " D ", '#', Ic2Items.terraformerBlueprint, 'D', Block.dirt});
        advRecipes.addRecipe(Ic2Items.mushroomTerraformerBlueprint, new Object[] {"mMm", "M#M", "mMm", '#', Ic2Items.terraformerBlueprint, 'M', Block.mushroomBrown, 'm', Block.mycelium});
        advRecipes.addShapelessRecipe(Ic2Items.terraformerBlueprint, new Object[] {Ic2Items.cultivationTerraformerBlueprint});
        advRecipes.addShapelessRecipe(Ic2Items.terraformerBlueprint, new Object[] {Ic2Items.irrigationTerraformerBlueprint});
        advRecipes.addShapelessRecipe(Ic2Items.terraformerBlueprint, new Object[] {Ic2Items.chillingTerraformerBlueprint});
        advRecipes.addShapelessRecipe(Ic2Items.terraformerBlueprint, new Object[] {Ic2Items.desertificationTerraformerBlueprint});
        advRecipes.addShapelessRecipe(Ic2Items.terraformerBlueprint, new Object[] {Ic2Items.flatificatorTerraformerBlueprint});
        advRecipes.addRecipe(Ic2Items.overclockerUpgrade, new Object[] {"CCC", "WEW", 'C', Ic2Items.reactorCoolantSimple, 'W', Ic2Items.insulatedCopperCableItem, 'E', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.transformerUpgrade, new Object[] {"GGG", "WTW", "GEG", 'G', Block.glass, 'W', Ic2Items.doubleInsulatedGoldCableItem, 'T', Ic2Items.mvTransformer, 'E', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.energyStorageUpgrade, new Object[] {"www", "WBW", "wEw", 'w', Block.planks, 'W', Ic2Items.insulatedCopperCableItem, 'B', Ic2Items.reBattery, 'E', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.energyStorageUpgrade, new Object[] {"www", "WBW", "wEw", 'w', Block.planks, 'W', Ic2Items.insulatedCopperCableItem, 'B', Ic2Items.chargedReBattery, 'E', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.ejectorUpgrade, new Object[] {"PHP", "WEW", 'P', Block.pistonBase, 'H', Block.hopperBlock, 'W', Ic2Items.insulatedCopperCableItem, 'E', Ic2Items.electronicCircuit});
        advRecipes.addRecipe(Ic2Items.boatCarbon, new Object[] {"X X", "XXX", 'X', Ic2Items.carbonPlate});
        advRecipes.addRecipe(Ic2Items.boatRubber, new Object[] {"X X", "XXX", 'X', "itemRubber"});
        advRecipes.addShapelessRecipe(Ic2Items.boatRubber, new Object[] {Ic2Items.boatRubberBroken, "itemRubber"});
        advRecipes.addRecipe(Ic2Items.boatElectric, new Object[] {"CCC", "X@X", "XXX", 'X', "ingotRefinedIron", 'C', Ic2Items.insulatedCopperCableItem, '@', Ic2Items.waterMill});
        advRecipes.addRecipe(Ic2Items.boatElectric, new Object[] {"CCC", "X@X", "XXX", 'X', "ingotRefinedIron", 'C', Ic2Items.insulatedCopperCableItem, '@', Ic2Items.waterMill});
        advRecipes.addRecipe(Ic2Items.reinforcedDoor, new Object[] {"SS", "SS", "SS", 'S', Ic2Items.reinforcedStone});
        advRecipes.addRecipe(Ic2Items.scrapBox, new Object[] {"SSS", "SSS", "SSS", 'S', Ic2Items.scrap});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.stickyDynamite, 8), new Object[] {"DDD", "DRD", "DDD", 'D', Ic2Items.dynamite, 'R', Ic2Items.resin});
        advRecipes.addShapelessRecipe(StackUtil.copyWithSize(Ic2Items.dynamite, 8), new Object[] {Ic2Items.industrialTnt, Item.silk});
        advRecipes.addShapelessRecipe(StackUtil.copyWithSize(Ic2Items.bronzeDust, 2), new Object[] {"dustTin", "dustCopper", "dustCopper", "dustCopper"});
        advRecipes.addShapelessRecipe(Ic2Items.ironDust, new Object[] {Ic2Items.smallIronDust, Ic2Items.smallIronDust});
        advRecipes.addShapelessRecipe(Ic2Items.carbonMesh, new Object[] {Ic2Items.carbonFiber, Ic2Items.carbonFiber});
        advRecipes.addShapelessRecipe(new ItemStack(Block.pistonStickyBase, 1), new Object[] {Block.pistonBase, Ic2Items.resin, Boolean.valueOf(true)});
        advRecipes.addRecipe(new ItemStack(Block.pistonBase, 1), new Object[] {"TTT", "#X#", "#R#", '#', Block.cobblestone, 'X', "ingotBronze", 'R', Item.redstone, 'T', Block.planks, Boolean.valueOf(true)});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.miningPipe, 8), new Object[] {"I I", "I I", "ITI", 'I', "ingotRefinedIron", 'T', Ic2Items.treetap});

        if (Ic2Items.rubberSapling != null)
        {
            advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.plantBall, 2), new Object[] {"PPP", "P P", "PPP", 'P', Ic2Items.rubberSapling});
        }

        if (IC2.enableCraftingGlowstoneDust)
        {
            advRecipes.addRecipe(new ItemStack(Item.glowstone, 1), new Object[] {"RGR", "GRG", "RGR", 'R', Item.redstone, 'G', "dustGold", Boolean.valueOf(true)});
        }

        if (IC2.enableCraftingGunpowder)
        {
            advRecipes.addRecipe(new ItemStack(Item.gunpowder, 3), new Object[] {"RCR", "CRC", "RCR", 'R', Item.redstone, 'C', "dustCoal", Boolean.valueOf(true)});
        }

        if (IC2.enableCraftingBucket)
        {
            advRecipes.addRecipe(new ItemStack(Item.bucketEmpty, 1), new Object[] {"T T", " T ", 'T', "ingotTin", Boolean.valueOf(true)});
        }

        if (IC2.enableCraftingCoin)
        {
            advRecipes.addRecipe(Ic2Items.refinedIronIngot, new Object[] {"III", "III", "III", 'I', Ic2Items.coin});
        }

        if (IC2.enableCraftingCoin)
        {
            advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.coin, 16), new Object[] {"II", "II", 'I', "ingotRefinedIron"});
        }

        if (IC2.enableCraftingRail)
        {
            advRecipes.addRecipe(new ItemStack(Block.rail, 8), new Object[] {"B B", "BsB", "B B", 'B', "ingotBronze", 's', "stickWood", Boolean.valueOf(true)});
        }

        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.crop, 2), new Object[] {"S S", "S S", 'S', "stickWood"});
        advRecipes.addRecipe(new ItemStack(Ic2Items.cropnalyzer.getItem()), new Object[] {"cc ", "RGR", "RCR", 'G', Block.glass, 'c', Ic2Items.insulatedCopperCableItem, 'R', Item.redstone, 'C', Ic2Items.electronicCircuit});
        advRecipes.addShapelessRecipe(StackUtil.copyWithSize(Ic2Items.fertilizer, 2), new Object[] {Ic2Items.scrap, new ItemStack(Item.dyePowder, 1, 15)});
        advRecipes.addShapelessRecipe(StackUtil.copyWithSize(Ic2Items.fertilizer, 2), new Object[] {Ic2Items.scrap, Ic2Items.scrap, Ic2Items.fertilizer});
        advRecipes.addRecipe(Ic2Items.weedEx, new Object[] {"R", "G", "C", 'R', Item.redstone, 'G', Ic2Items.grinPowder, 'C', Ic2Items.cell});
        advRecipes.addRecipe(Ic2Items.cropmatron, new Object[] {"cBc", "CMC", "CCC", 'M', Ic2Items.machine, 'C', Ic2Items.crop, 'c', Ic2Items.electronicCircuit, 'B', Block.chest});
        advRecipes.addRecipe(new ItemStack(Ic2Items.mugEmpty.getItem()), new Object[] {"SS ", "SSS", "SS ", 'S', Block.stone});
        advRecipes.addShapelessRecipe(new ItemStack(Ic2Items.coffeePowder.getItem()), new Object[] {Ic2Items.coffeeBeans});
        advRecipes.addShapelessRecipe(new ItemStack(Ic2Items.mugCoffee.getItem()), new Object[] {Ic2Items.mugEmpty, Ic2Items.coffeePowder, "liquid$water"});
        advRecipes.addShapelessRecipe(new ItemStack(Ic2Items.mugCoffee.getItem(), 1, 2), new Object[] {new ItemStack(Ic2Items.mugCoffee.getItem(), 1, 1), Item.sugar, Item.bucketMilk});

        if (Ic2Items.rubberWood != null)
        {
            advRecipes.addRecipe(new ItemStack(Ic2Items.barrel.getItem()), new Object[] {"P", "W", "P", 'P', Block.planks, 'W', Ic2Items.rubberWood});
        }

        advRecipes.addRecipe(new ItemStack(Ic2Items.mugEmpty.getItem()), new Object[] {"#", '#', new ItemStack(Ic2Items.mugBooze.getItem(), 1, -1)});
        advRecipes.addRecipe(new ItemStack(Ic2Items.barrel.getItem()), new Object[] {"#", '#', new ItemStack(Ic2Items.barrel.getItem(), 1, -1)});
        advRecipes.addShapelessRecipe(Ic2Items.blackPainter, new Object[] {Ic2Items.painter, "dyeBlack"});
        advRecipes.addShapelessRecipe(Ic2Items.redPainter, new Object[] {Ic2Items.painter, "dyeRed"});
        advRecipes.addShapelessRecipe(Ic2Items.greenPainter, new Object[] {Ic2Items.painter, "dyeGreen"});
        advRecipes.addShapelessRecipe(Ic2Items.brownPainter, new Object[] {Ic2Items.painter, "dyeBrown"});
        advRecipes.addShapelessRecipe(Ic2Items.bluePainter, new Object[] {Ic2Items.painter, "dyeBlue"});
        advRecipes.addShapelessRecipe(Ic2Items.purplePainter, new Object[] {Ic2Items.painter, "dyePurple"});
        advRecipes.addShapelessRecipe(Ic2Items.cyanPainter, new Object[] {Ic2Items.painter, "dyeCyan"});
        advRecipes.addShapelessRecipe(Ic2Items.lightGreyPainter, new Object[] {Ic2Items.painter, "dyeLightGray"});
        advRecipes.addShapelessRecipe(Ic2Items.darkGreyPainter, new Object[] {Ic2Items.painter, "dyeGray"});
        advRecipes.addShapelessRecipe(Ic2Items.pinkPainter, new Object[] {Ic2Items.painter, "dyePink"});
        advRecipes.addShapelessRecipe(Ic2Items.limePainter, new Object[] {Ic2Items.painter, "dyeLime"});
        advRecipes.addShapelessRecipe(Ic2Items.yellowPainter, new Object[] {Ic2Items.painter, "dyeYellow"});
        advRecipes.addShapelessRecipe(Ic2Items.cloudPainter, new Object[] {Ic2Items.painter, "dyeLightBlue"});
        advRecipes.addShapelessRecipe(Ic2Items.magentaPainter, new Object[] {Ic2Items.painter, "dyeMagenta"});
        advRecipes.addShapelessRecipe(Ic2Items.orangePainter, new Object[] {Ic2Items.painter, "dyeOrange"});
        advRecipes.addShapelessRecipe(Ic2Items.whitePainter, new Object[] {Ic2Items.painter, "dyeWhite"});
        advRecipes.addShapelessRecipe(Ic2Items.painter, new Object[] {new ItemStack(Ic2Items.blackPainter.itemID, 1, -1)});
        advRecipes.addShapelessRecipe(Ic2Items.painter, new Object[] {new ItemStack(Ic2Items.redPainter.itemID, 1, -1)});
        advRecipes.addShapelessRecipe(Ic2Items.painter, new Object[] {new ItemStack(Ic2Items.greenPainter.itemID, 1, -1)});
        advRecipes.addShapelessRecipe(Ic2Items.painter, new Object[] {new ItemStack(Ic2Items.brownPainter.itemID, 1, -1)});
        advRecipes.addShapelessRecipe(Ic2Items.painter, new Object[] {new ItemStack(Ic2Items.bluePainter.itemID, 1, -1)});
        advRecipes.addShapelessRecipe(Ic2Items.painter, new Object[] {new ItemStack(Ic2Items.purplePainter.itemID, 1, -1)});
        advRecipes.addShapelessRecipe(Ic2Items.painter, new Object[] {new ItemStack(Ic2Items.cyanPainter.itemID, 1, -1)});
        advRecipes.addShapelessRecipe(Ic2Items.painter, new Object[] {new ItemStack(Ic2Items.lightGreyPainter.itemID, 1, -1)});
        advRecipes.addShapelessRecipe(Ic2Items.painter, new Object[] {new ItemStack(Ic2Items.darkGreyPainter.itemID, 1, -1)});
        advRecipes.addShapelessRecipe(Ic2Items.painter, new Object[] {new ItemStack(Ic2Items.pinkPainter.itemID, 1, -1)});
        advRecipes.addShapelessRecipe(Ic2Items.painter, new Object[] {new ItemStack(Ic2Items.limePainter.itemID, 1, -1)});
        advRecipes.addShapelessRecipe(Ic2Items.painter, new Object[] {new ItemStack(Ic2Items.yellowPainter.itemID, 1, -1)});
        advRecipes.addShapelessRecipe(Ic2Items.painter, new Object[] {new ItemStack(Ic2Items.cloudPainter.itemID, 1, -1)});
        advRecipes.addShapelessRecipe(Ic2Items.painter, new Object[] {new ItemStack(Ic2Items.magentaPainter.itemID, 1, -1)});
        advRecipes.addShapelessRecipe(Ic2Items.painter, new Object[] {new ItemStack(Ic2Items.orangePainter.itemID, 1, -1)});
        advRecipes.addShapelessRecipe(Ic2Items.painter, new Object[] {new ItemStack(Ic2Items.whitePainter.itemID, 1, -1)});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.cell, 16), new Object[] {" T ", "T T", " T ", 'T', "ingotTin"});
        advRecipes.addRecipe(Ic2Items.fuelCan, new Object[] {" TT", "T T", "TTT", 'T', "ingotTin"});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.tinCan, 4), new Object[] {"T T", "TTT", 'T', "ingotTin"});
        advRecipes.addShapelessRecipe(Ic2Items.waterCell, new Object[] {Ic2Items.cell, Item.bucketWater});
        advRecipes.addShapelessRecipe(Ic2Items.lavaCell, new Object[] {Ic2Items.cell, Item.bucketLava});
        advRecipes.addShapelessRecipe(new ItemStack(Block.obsidian, 1), new Object[] {Ic2Items.waterCell, Ic2Items.waterCell, Ic2Items.lavaCell, Ic2Items.lavaCell});
        advRecipes.addShapelessRecipe(Ic2Items.hydratedCoalDust, new Object[] {"dustCoal", "liquid$water"});
        advRecipes.addShapelessRecipe(Ic2Items.hydratedCoalCell, new Object[] {Ic2Items.cell, Ic2Items.hydratedCoalClump});
        advRecipes.addShapelessRecipe(Ic2Items.bioCell, new Object[] {Ic2Items.cell, Ic2Items.compressedPlantBall});
        advRecipes.addRecipe(new ItemStack(Ic2Items.cfPack.itemID, 1, 259), new Object[] {"SCS", "FTF", "F F", 'T', "ingotTin", 'C', Ic2Items.electronicCircuit, 'F', Ic2Items.fuelCan, 'S', new ItemStack(Ic2Items.constructionFoamSprayer.itemID, 1, 1601)});
        advRecipes.addShapelessRecipe(StackUtil.copyWithSize(Ic2Items.constructionFoam, 3), new Object[] {"dustClay", "liquid$water", Item.redstone, "dustCoal"});
        advRecipes.addShapelessRecipe(new ItemStack(Item.diamond), new Object[] {Ic2Items.industrialDiamond});
        advRecipes.addRecipe(StackUtil.copyWithSize(Ic2Items.mixedMetalIngot, 2), new Object[] {"III", "BBB", "TTT", 'I', "ingotRefinedIron", 'B', "ingotBronze", 'T', "ingotTin"});
        advRecipes.addRecipe(Ic2Items.remote, new Object[] {" C ", "TLT", " F ", 'C', Ic2Items.insulatedCopperCableItem, 'F', Ic2Items.frequencyTransmitter, 'L', new ItemStack(Item.dyePowder, 1, 4), 'T', "ingotTin"});
    }
}
