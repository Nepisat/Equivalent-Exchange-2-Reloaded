package ic2.core.init;

import java.util.HashMap;

public final class DefaultIds
{
    private static HashMap<InternalName, Integer> idMap = new HashMap();

    public static int get(InternalName name)
    {
        if (!idMap.containsKey(name))
        {
            throw new IllegalArgumentException("default id for " + name + " not registered");
        }
        else
        {
            return ((Integer)idMap.get(name)).intValue();
        }
    }

    private static void add(InternalName name, int id)
    {
        if (idMap.containsValue(Integer.valueOf(id)))
        {
            throw new RuntimeException("duplicate default id: " + id);
        }
        else
        {
            idMap.put(name, Integer.valueOf(id));
        }
    }

    static
    {
        add(InternalName.blockAlloy, 231);
        add(InternalName.blockAlloyGlass, 230);
        add(InternalName.blockBarrel, 217);
        add(InternalName.blockCable, 228);
        add(InternalName.blockCrop, 218);
        add(InternalName.blockDoorAlloy, 229);
        add(InternalName.blockDynamite, 236);
        add(InternalName.blockDynamiteRemote, 235);
        add(InternalName.blockElectric, 227);
        add(InternalName.blockFenceIron, 232);
        add(InternalName.blockFoam, 222);
        add(InternalName.blockGenerator, 246);
        add(InternalName.blockHarz, 240);
        add(InternalName.blockITNT, 239);
        add(InternalName.blockIronScaffold, 216);
        add(InternalName.blockLuminator, 226);
        add(InternalName.blockLuminatorDark, 219);
        add(InternalName.blockMachine, 250);
        add(InternalName.blockMachine2, 223);
        add(InternalName.blockMetal, 224);
        add(InternalName.blockMiningPipe, 245);
        add(InternalName.blockMiningTip, 244);
        add(InternalName.blockNuke, 237);
        add(InternalName.blockOreCopper, 249);
        add(InternalName.blockOreTin, 248);
        add(InternalName.blockOreUran, 247);
        add(InternalName.blockPersonal, 225);
        add(InternalName.blockReactorChamber, 233);
        add(InternalName.blockRubLeaves, 242);
        add(InternalName.blockRubSapling, 241);
        add(InternalName.blockRubWood, 243);
        add(InternalName.blockRubber, 234);
        add(InternalName.blockScaffold, 220);
        add(InternalName.blockWall, 221);
        add(InternalName.itemArmorAlloyChestplate, 29923);
        add(InternalName.itemArmorBatpack, 29924);
        add(InternalName.itemArmorBronzeBoots, 29936);
        add(InternalName.itemArmorBronzeChestplate, 29938);
        add(InternalName.itemArmorBronzeHelmet, 29939);
        add(InternalName.itemArmorBronzeLegs, 29937);
        add(InternalName.itemArmorCFPack, 29873);
        add(InternalName.itemArmorHazmatChestplate, 29825);
        add(InternalName.itemArmorHazmatHelmet, 29826);
        add(InternalName.itemArmorHazmatLeggings, 29824);
        add(InternalName.itemArmorJetpack, 29954);
        add(InternalName.itemArmorJetpackElectric, 29953);
        add(InternalName.itemArmorLappack, 29871);
        add(InternalName.itemArmorNanoBoots, 29919);
        add(InternalName.itemArmorNanoChestplate, 29921);
        add(InternalName.itemArmorNanoHelmet, 29922);
        add(InternalName.itemArmorNanoLegs, 29920);
        add(InternalName.itemArmorQuantumBoots, 29915);
        add(InternalName.itemArmorQuantumChestplate, 29917);
        add(InternalName.itemArmorQuantumHelmet, 29918);
        add(InternalName.itemArmorQuantumLegs, 29916);
        add(InternalName.itemArmorRubBoots, 29955);
        add(InternalName.itemBarrel, 29852);
        add(InternalName.itemBatCrystal, 29985);
        add(InternalName.itemBatLamaCrystal, 29984);
        add(InternalName.itemBatRE, 29986);
        add(InternalName.itemBatREDischarged, 29983);
        add(InternalName.itemBatSU, 29982);
        add(InternalName.itemCable, 29928);
        add(InternalName.itemCellAir, 29823);
        add(InternalName.itemCellBio, 29973);
        add(InternalName.itemCellBioRef, 29971);
        add(InternalName.itemCellCoal, 29974);
        add(InternalName.itemCellCoalRef, 29972);
        add(InternalName.itemCellEmpty, 29981);
        add(InternalName.itemCellHydrant, 29864);
        add(InternalName.itemCellLava, 29980);
        add(InternalName.itemCellUranEmpty, 29945);
        add(InternalName.itemCellUranEnriched, 29946);
        add(InternalName.itemCellWater, 29962);
        add(InternalName.itemCellWaterElectro, 29925);
        add(InternalName.itemCofeeBeans, 29857);
        add(InternalName.itemCofeePowder, 29856);
        add(InternalName.itemCoin, 29930);
        add(InternalName.itemCoolant, 29847);
        add(InternalName.itemCropSeed, 29870);
        add(InternalName.itemCropnalyzer, 29866);
        add(InternalName.itemDebug, 29848);
        add(InternalName.itemDoorAlloy, 29929);
        add(InternalName.itemDustBronze, 29995);
        add(InternalName.itemDustClay, 29877);
        add(InternalName.itemDustCoal, 30000);
        add(InternalName.itemDustCopper, 29997);
        add(InternalName.itemDustGold, 29998);
        add(InternalName.itemDustIron, 29999);
        add(InternalName.itemDustIronSmall, 29994);
        add(InternalName.itemDustSilver, 29874);
        add(InternalName.itemDustTin, 29996);
        add(InternalName.itemDynamite, 29959);
        add(InternalName.itemDynamiteSticky, 29958);
        add(InternalName.itemFertilizer, 29865);
        add(InternalName.itemFoamSprayer, 29875);
        add(InternalName.itemFreq, 29878);
        add(InternalName.itemFuelCan, 29976);
        add(InternalName.itemFuelCanEmpty, 29975);
        add(InternalName.itemFuelCoalCmpr, 29969);
        add(InternalName.itemFuelCoalDust, 29970);
        add(InternalName.itemFuelPlantBall, 29968);
        add(InternalName.itemFuelPlantCmpr, 29967);
        add(InternalName.itemGrinPowder, 29850);
        add(InternalName.itemHarz, 29961);
        add(InternalName.itemHops, 29853);
        add(InternalName.itemIngotAdvIron, 29993);
        add(InternalName.itemIngotAlloy, 29989);
        add(InternalName.itemIngotBronze, 29990);
        add(InternalName.itemIngotCopper, 29992);
        add(InternalName.itemIngotTin, 29991);
        add(InternalName.itemIngotUran, 29988);
        add(InternalName.itemMatter, 29932);
        add(InternalName.itemMugBooze, 29851);
        add(InternalName.itemMugCoffee, 29854);
        add(InternalName.itemMugEmpty, 29855);
        add(InternalName.itemNanoSaber, 29893);
        add(InternalName.itemNanoSaberOff, 29892);
        add(InternalName.itemNightvisionGoggles, 29822);
        add(InternalName.itemOreIridium, 29872);
        add(InternalName.itemOreUran, 29987);
        add(InternalName.itemPartAlloy, 29931);
        add(InternalName.itemPartCarbonFibre, 29896);
        add(InternalName.itemPartCarbonMesh, 29895);
        add(InternalName.itemPartCarbonPlate, 29894);
        add(InternalName.itemPartCircuit, 29935);
        add(InternalName.itemPartCircuitAdv, 29934);
        add(InternalName.itemPartCoalBall, 29882);
        add(InternalName.itemPartCoalBlock, 29881);
        add(InternalName.itemPartCoalChunk, 29880);
        add(InternalName.itemPartDCP, 29828);
        add(InternalName.itemPartIndustrialDiamond, 29879);
        add(InternalName.itemPartIridium, 29891);
        add(InternalName.itemPartPellet, 29876);
        add(InternalName.itemRemote, 29957);
        add(InternalName.itemRubber, 29960);
        add(InternalName.itemScanner, 29964);
        add(InternalName.itemScannerAdv, 29963);
        add(InternalName.itemScrap, 29933);
        add(InternalName.itemScrapbox, 29883);
        add(InternalName.itemSolarHelmet, 29860);
        add(InternalName.itemStaticBoots, 29859);
        add(InternalName.itemTFBP, 29890);
        add(InternalName.itemTFBPChilling, 29887);
        add(InternalName.itemTFBPCultivation, 29889);
        add(InternalName.itemTFBPDesertification, 29886);
        add(InternalName.itemTFBPFlatification, 29885);
        add(InternalName.itemTFBPIrrigation, 29888);
        add(InternalName.itemTFBPMushroom, 29862);
        add(InternalName.itemTerraWart, 29858);
        add(InternalName.itemTinCan, 29966);
        add(InternalName.itemTinCanFilled, 29965);
        add(InternalName.itemToolBronzeAxe, 29943);
        add(InternalName.itemToolBronzeHoe, 29940);
        add(InternalName.itemToolBronzePickaxe, 29944);
        add(InternalName.itemToolBronzeSpade, 29941);
        add(InternalName.itemToolBronzeSword, 29942);
        add(InternalName.itemToolChainsaw, 29977);
        add(InternalName.itemToolCutter, 29897);
        add(InternalName.itemToolDDrill, 29978);
        add(InternalName.itemToolDrill, 29979);
        add(InternalName.itemToolHoe, 29863);
        add(InternalName.itemToolMEter, 29926);
        add(InternalName.itemToolMiningLaser, 29952);
        add(InternalName.itemToolPainter, 29914);
        add(InternalName.itemToolPainterBlack, 29913);
        add(InternalName.itemToolPainterBlue, 29909);
        add(InternalName.itemToolPainterBrown, 29910);
        add(InternalName.itemToolPainterCloud, 29901);
        add(InternalName.itemToolPainterCyan, 29907);
        add(InternalName.itemToolPainterDarkGrey, 29905);
        add(InternalName.itemToolPainterGreen, 29911);
        add(InternalName.itemToolPainterLightGrey, 29906);
        add(InternalName.itemToolPainterLime, 29903);
        add(InternalName.itemToolPainterMagenta, 29900);
        add(InternalName.itemToolPainterOrange, 29899);
        add(InternalName.itemToolPainterPink, 29904);
        add(InternalName.itemToolPainterPurple, 29908);
        add(InternalName.itemToolPainterRed, 29912);
        add(InternalName.itemToolPainterWhite, 29898);
        add(InternalName.itemToolPainterYellow, 29902);
        add(InternalName.itemToolWrench, 29927);
        add(InternalName.itemToolWrenchElectric, 29884);
        add(InternalName.itemToolbox, 29861);
        add(InternalName.itemTreetap, 29956);
        add(InternalName.itemTreetapElectric, 29868);
        add(InternalName.itemWeedEx, 29849);
        add(InternalName.obscurator, 29820);
        add(InternalName.reactorCondensator, 29829);
        add(InternalName.reactorCondensatorLap, 29827);
        add(InternalName.reactorCoolantSimple, 29950);
        add(InternalName.reactorCoolantSix, 29843);
        add(InternalName.reactorCoolantTriple, 29844);
        add(InternalName.reactorHeatSwitch, 29948);
        add(InternalName.reactorHeatSwitchCore, 29840);
        add(InternalName.reactorHeatSwitchDiamond, 29838);
        add(InternalName.reactorHeatSwitchSpread, 29839);
        add(InternalName.reactorHeatpack, 29832);
        add(InternalName.reactorIsotopeCell, 29947);
        add(InternalName.reactorPlating, 29949);
        add(InternalName.reactorPlatingExplosive, 29841);
        add(InternalName.reactorPlatingHeat, 29842);
        add(InternalName.reactorReflector, 29831);
        add(InternalName.reactorReflectorThick, 29830);
        add(InternalName.reactorUraniumDual, 29846);
        add(InternalName.reactorUraniumQuad, 29845);
        add(InternalName.reactorUraniumSimple, 29951);
        add(InternalName.reactorVent, 29837);
        add(InternalName.reactorVentCore, 29836);
        add(InternalName.reactorVentDiamond, 29833);
        add(InternalName.reactorVentGold, 29835);
        add(InternalName.reactorVentSpread, 29834);
        add(InternalName.upgradeModule, 29869);
        add(InternalName.itemBoat, 29821);
    }
}
