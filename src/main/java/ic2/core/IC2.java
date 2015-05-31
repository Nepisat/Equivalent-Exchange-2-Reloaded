package ic2.core;

import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkMod.VersionCheckHandler;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.crops.Crops;
import ic2.api.info.Info;
import ic2.api.item.ElectricItem;
import ic2.api.recipe.Recipes;
import ic2.api.tile.ExplosionWhitelist;
import ic2.core.IC2$1;
import ic2.core.audio.AudioManager;
import ic2.core.block.BlockBarrel;
import ic2.core.block.BlockCrop;
import ic2.core.block.BlockDynamite;
import ic2.core.block.BlockFoam;
import ic2.core.block.BlockIC2Door;
import ic2.core.block.BlockITNT;
import ic2.core.block.BlockMetal;
import ic2.core.block.BlockPoleFence;
import ic2.core.block.BlockResin;
import ic2.core.block.BlockRubLeaves;
import ic2.core.block.BlockRubSapling;
import ic2.core.block.BlockRubWood;
import ic2.core.block.BlockRubberSheet;
import ic2.core.block.BlockScaffold;
import ic2.core.block.BlockSimple;
import ic2.core.block.BlockTexGlass;
import ic2.core.block.BlockTextureStitched;
import ic2.core.block.BlockWall;
import ic2.core.block.EntityDynamite;
import ic2.core.block.EntityItnt;
import ic2.core.block.EntityNuke;
import ic2.core.block.EntityStickyDynamite;
import ic2.core.block.TileEntityBarrel;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.TileEntityCrop;
import ic2.core.block.TileEntityWall;
import ic2.core.block.WorldGenRubTree;
import ic2.core.block.crop.IC2Crops;
import ic2.core.block.generator.block.BlockGenerator;
import ic2.core.block.generator.block.BlockReactorChamber;
import ic2.core.block.generator.tileentity.TileEntityGenerator;
import ic2.core.block.generator.tileentity.TileEntityGeoGenerator;
import ic2.core.block.generator.tileentity.TileEntityNuclearReactorElectric;
import ic2.core.block.generator.tileentity.TileEntityReactorChamberElectric;
import ic2.core.block.generator.tileentity.TileEntitySolarGenerator;
import ic2.core.block.generator.tileentity.TileEntityWaterGenerator;
import ic2.core.block.generator.tileentity.TileEntityWindGenerator;
import ic2.core.block.machine.BlockMachine;
import ic2.core.block.machine.BlockMachine2;
import ic2.core.block.machine.BlockMiningPipe;
import ic2.core.block.machine.BlockMiningTip;
import ic2.core.block.machine.tileentity.TileEntityCanner;
import ic2.core.block.machine.tileentity.TileEntityCompressor;
import ic2.core.block.machine.tileentity.TileEntityCropmatron;
import ic2.core.block.machine.tileentity.TileEntityElectricFurnace;
import ic2.core.block.machine.tileentity.TileEntityElectrolyzer;
import ic2.core.block.machine.tileentity.TileEntityExtractor;
import ic2.core.block.machine.tileentity.TileEntityInduction;
import ic2.core.block.machine.tileentity.TileEntityIronFurnace;
import ic2.core.block.machine.tileentity.TileEntityMacerator;
import ic2.core.block.machine.tileentity.TileEntityMagnetizer;
import ic2.core.block.machine.tileentity.TileEntityMatter;
import ic2.core.block.machine.tileentity.TileEntityMiner;
import ic2.core.block.machine.tileentity.TileEntityPump;
import ic2.core.block.machine.tileentity.TileEntityRecycler;
import ic2.core.block.machine.tileentity.TileEntityTeleporter;
import ic2.core.block.machine.tileentity.TileEntityTerra;
import ic2.core.block.machine.tileentity.TileEntityTesla;
import ic2.core.block.personal.BlockPersonal;
import ic2.core.block.personal.TileEntityEnergyOMat;
import ic2.core.block.personal.TileEntityPersonalChest;
import ic2.core.block.personal.TileEntityTradeOMat;
import ic2.core.block.wiring.BlockCable;
import ic2.core.block.wiring.BlockElectric;
import ic2.core.block.wiring.BlockLuminator;
import ic2.core.block.wiring.TileEntityCable;
import ic2.core.block.wiring.TileEntityCableDetector;
import ic2.core.block.wiring.TileEntityCableSplitter;
import ic2.core.block.wiring.TileEntityElectricBatBox;
import ic2.core.block.wiring.TileEntityElectricMFE;
import ic2.core.block.wiring.TileEntityElectricMFSU;
import ic2.core.block.wiring.TileEntityLuminator;
import ic2.core.block.wiring.TileEntityTransformerHV;
import ic2.core.block.wiring.TileEntityTransformerLV;
import ic2.core.block.wiring.TileEntityTransformerMV;
import ic2.core.init.InternalName;
import ic2.core.init.Localization;
import ic2.core.init.Recipies;
import ic2.core.item.ElectricItemManager;
import ic2.core.item.EntityBoatCarbon;
import ic2.core.item.EntityBoatElectric;
import ic2.core.item.EntityBoatRubber;
import ic2.core.item.GatewayElectricItemManager;
import ic2.core.item.ItemBattery;
import ic2.core.item.ItemBatteryDischarged;
import ic2.core.item.ItemBatterySU;
import ic2.core.item.ItemBooze;
import ic2.core.item.ItemCell;
import ic2.core.item.ItemCropSeed;
import ic2.core.item.ItemFertilizer;
import ic2.core.item.ItemFuelCanEmpty;
import ic2.core.item.ItemFuelCanFilled;
import ic2.core.item.ItemGradual;
import ic2.core.item.ItemIC2;
import ic2.core.item.ItemIC2Boat;
import ic2.core.item.ItemMigrate;
import ic2.core.item.ItemMug;
import ic2.core.item.ItemMugCoffee;
import ic2.core.item.ItemResin;
import ic2.core.item.ItemScrapbox;
import ic2.core.item.ItemTerraWart;
import ic2.core.item.ItemTinCan;
import ic2.core.item.ItemToolbox;
import ic2.core.item.ItemUpgradeModule;
import ic2.core.item.armor.ItemArmorBatpack;
import ic2.core.item.armor.ItemArmorCFPack;
import ic2.core.item.armor.ItemArmorHazmat;
import ic2.core.item.armor.ItemArmorIC2;
import ic2.core.item.armor.ItemArmorJetpack;
import ic2.core.item.armor.ItemArmorJetpackElectric;
import ic2.core.item.armor.ItemArmorLappack;
import ic2.core.item.armor.ItemArmorNanoSuit;
import ic2.core.item.armor.ItemArmorNightvisionGoggles;
import ic2.core.item.armor.ItemArmorQuantumSuit;
import ic2.core.item.armor.ItemArmorSolarHelmet;
import ic2.core.item.armor.ItemArmorStaticBoots;
import ic2.core.item.block.ItemBarrel;
import ic2.core.item.block.ItemCable;
import ic2.core.item.block.ItemDynamite;
import ic2.core.item.block.ItemIC2Door;
import ic2.core.item.reactor.ItemReactorCondensator;
import ic2.core.item.reactor.ItemReactorDepletedUranium;
import ic2.core.item.reactor.ItemReactorHeatStorage;
import ic2.core.item.reactor.ItemReactorHeatSwitch;
import ic2.core.item.reactor.ItemReactorHeatpack;
import ic2.core.item.reactor.ItemReactorPlating;
import ic2.core.item.reactor.ItemReactorReflector;
import ic2.core.item.reactor.ItemReactorUranium;
import ic2.core.item.reactor.ItemReactorVent;
import ic2.core.item.reactor.ItemReactorVentSpread;
import ic2.core.item.tfbp.ItemTFBPChilling;
import ic2.core.item.tfbp.ItemTFBPCultivation;
import ic2.core.item.tfbp.ItemTFBPDesertification;
import ic2.core.item.tfbp.ItemTFBPFlatification;
import ic2.core.item.tfbp.ItemTFBPIrrigation;
import ic2.core.item.tfbp.ItemTFBPMushroom;
import ic2.core.item.tool.EntityMiningLaser;
import ic2.core.item.tool.ItemCropnalyzer;
import ic2.core.item.tool.ItemDebug;
import ic2.core.item.tool.ItemElectricToolChainsaw;
import ic2.core.item.tool.ItemElectricToolDDrill;
import ic2.core.item.tool.ItemElectricToolDrill;
import ic2.core.item.tool.ItemElectricToolHoe;
import ic2.core.item.tool.ItemFrequencyTransmitter;
import ic2.core.item.tool.ItemIC2Axe;
import ic2.core.item.tool.ItemIC2Hoe;
import ic2.core.item.tool.ItemIC2Pickaxe;
import ic2.core.item.tool.ItemIC2Spade;
import ic2.core.item.tool.ItemIC2Sword;
import ic2.core.item.tool.ItemNanoSaber;
import ic2.core.item.tool.ItemObscurator;
import ic2.core.item.tool.ItemRemote;
import ic2.core.item.tool.ItemScanner;
import ic2.core.item.tool.ItemScannerAdv;
import ic2.core.item.tool.ItemSprayer;
import ic2.core.item.tool.ItemToolCutter;
import ic2.core.item.tool.ItemToolMeter;
import ic2.core.item.tool.ItemToolMiningLaser;
import ic2.core.item.tool.ItemToolPainter;
import ic2.core.item.tool.ItemToolWrench;
import ic2.core.item.tool.ItemToolWrenchElectric;
import ic2.core.item.tool.ItemTreetap;
import ic2.core.item.tool.ItemTreetapElectric;
import ic2.core.network.NetworkManager;
import ic2.core.network.NetworkManagerClient;
import ic2.core.util.ItemInfo;
import ic2.core.util.Keyboard;
import ic2.core.util.StackUtil;
import ic2.core.util.TextureIndex;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.logging.Logger;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.client.event.TextureStitchEvent.Post;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.SpecialSpawn;
import net.minecraftforge.event.world.ChunkWatchEvent.Watch;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

@Mod(
    modid = "IC2",
    name = "IndustrialCraft 2",
    version = "1.118.401-lf",
    useMetadata = true,
    certificateFingerprint = "de041f9f6187debbc77034a344134053277aa3b0"
)
@NetworkMod(
    clientSideRequired = true,
    clientPacketHandlerSpec =       @SidedPacketHandler(
                                        channels = {"ic2"},
                                        packetHandler = NetworkManagerClient.class
                                    ),
    serverPacketHandlerSpec =       @SidedPacketHandler(
                                        channels = {"ic2"},
                                        packetHandler = NetworkManager.class
                                    )
)
public class IC2 implements ITickHandler, IWorldGenerator, IFuelHandler, IConnectionHandler, IPlayerTracker
{
    public static final String VERSION = "1.118.401-lf";
    private static IC2 instance = null;
    @SidedProxy(
        clientSide = "ic2.core.PlatformClient",
        serverSide = "ic2.core.Platform"
    )
    public static Platform platform;
    @SidedProxy(
        clientSide = "ic2.core.network.NetworkManagerClient",
        serverSide = "ic2.core.network.NetworkManager"
    )
    public static NetworkManager network;
    @SidedProxy(
        clientSide = "ic2.core.util.KeyboardClient",
        serverSide = "ic2.core.util.Keyboard"
    )
    public static Keyboard keyboard;
    @SidedProxy(
        clientSide = "ic2.core.audio.AudioManagerClient",
        serverSide = "ic2.core.audio.AudioManager"
    )
    public static AudioManager audioManager;
    @SidedProxy(
        clientSide = "ic2.core.util.TextureIndexClient",
        serverSide = "ic2.core.util.TextureIndex"
    )
    public static TextureIndex textureIndex;
    public static Logger log;
    public static IC2Achievements achievements;
    public static int cableRenderId;
    public static int fenceRenderId;
    public static int miningPipeRenderId;
    public static int luminatorRenderId;
    public static int cropRenderId;
    public static Random random = new Random();
    public static int windStrength;
    public static int windTicker;
    public static Map<Integer, Map<Integer, Integer>> valuableOres = new TreeMap();
    public static boolean enableCraftingBucket;
    public static boolean enableCraftingCoin;
    public static boolean enableCraftingGlowstoneDust;
    public static boolean enableCraftingGunpowder;
    public static boolean enableCraftingITnt;
    public static boolean enableCraftingNuke;
    public static boolean enableCraftingRail;
    public static boolean enableDynamicIdAllocation;
    public static boolean enableLoggingWrench;
    public static boolean enableSecretRecipeHiding;
    public static boolean enableQuantumSpeedOnSprint;
    public static boolean enableMinerLapotron;
    public static boolean enableTeleporterInventory;
    public static boolean enableBurningScrap;
    public static boolean enableWorldGenTreeRubber;
    public static boolean enableWorldGenOreCopper;
    public static boolean enableWorldGenOreTin;
    public static boolean enableWorldGenOreUranium;
    public static float explosionPowerNuke;
    public static float explosionPowerReactorMax;
    public static int energyGeneratorBase;
    public static int energyGeneratorGeo;
    public static int energyGeneratorWater;
    public static int energyGeneratorSolar;
    public static int energyGeneratorWind;
    public static int energyGeneratorNuclear;
    public static boolean suddenlyHoes;
    public static boolean seasonal;
    public static boolean enableSteamReactor;
    public static float oreDensityFactor;
    public static boolean initialized;
    public static Properties runtimeIdProperties;
    public static CreativeTabIC2 tabIC2;
    private static boolean silverDustSmeltingRegistered;
    private static Set<Integer> blockedIds;
    public static final int networkProtocolVersion = 1;
    public static final String textureDomain = "ic2";
    public static final int setBlockNotify = 1;
    public static final int setBlockUpdate = 2;
    public static final int setBlockNoUpdateFromClient = 4;

    public IC2()
    {
        instance = this;
    }

    public static IC2 getInstance()
    {
        return instance;
    }

    @VersionCheckHandler
    public boolean checkVersion(String version)
    {
        String[] partsLocal = "1.118.401-lf".split("\\.");
        String[] partsRemote = version.split("\\.");
        return partsLocal.length >= 2 && partsRemote.length >= 2 && partsLocal[0].equals(partsRemote[0]) && partsLocal[1].equals(partsRemote[1]);
    }

    @EventHandler
    public void load(FMLPreInitializationEvent event)
    {
        log = event.getModLog();
        short minForge = 759;
        int forge = ForgeVersion.getBuildVersion();

        if (forge > 0 && forge < minForge)
        {
            platform.displayError("The currently installed version of Minecraft Forge (" + ForgeVersion.getMajorVersion() + "." + ForgeVersion.getMinorVersion() + "." + ForgeVersion.getRevisionVersion() + "." + forge + ") is too old.\n" + "Please update the Minecraft Forge.\n" + "\n" + "(Technical information: " + forge + " < " + minForge + ")");
        }

        Configuration config;

        try
        {
            File prop = new File(new File(platform.getMinecraftDir(), "config"), "IC2.cfg");
            config = new Configuration(prop);
            config.load();
            log.info("Config loaded from " + prop.getAbsolutePath());
        }
        catch (Exception var17)
        {
            log.warning("Error while trying to access configuration! " + var17);
            config = null;
        }

        Property dynamicIdAllocationProp = null;

        if (config != null)
        {
            dynamicIdAllocationProp = config.get("general", "enableDynamicIdAllocation", enableDynamicIdAllocation);
            dynamicIdAllocationProp.comment = "Enable searching for free block ids, will get disabled after the next successful load";
            enableDynamicIdAllocation = dynamicIdAllocationProp.getBoolean(enableDynamicIdAllocation);
            Property var18 = config.get("general", "enableCraftingBucket", enableCraftingBucket);
            var18.comment = "Enable crafting of buckets out of tin";
            enableCraftingBucket = var18.getBoolean(enableCraftingBucket);
            var18 = config.get("general", "enableCraftingCoin", enableCraftingCoin);
            var18.comment = "Enable crafting of Industrial Credit coins";
            enableCraftingCoin = var18.getBoolean(enableCraftingCoin);
            var18 = config.get("general", "enableCraftingGlowstoneDust", enableCraftingGlowstoneDust);
            var18.comment = "Enable crafting of glowstone dust out of dusts";
            enableCraftingGlowstoneDust = var18.getBoolean(enableCraftingGlowstoneDust);
            var18 = config.get("general", "enableCraftingGunpowder", enableCraftingGunpowder);
            var18.comment = "Enable crafting of gunpowder out of dusts";
            enableCraftingGunpowder = var18.getBoolean(enableCraftingGunpowder);
            var18 = config.get("general", "enableCraftingITnt", enableCraftingITnt);
            var18.comment = "Enable crafting of ITNT";
            enableCraftingITnt = var18.getBoolean(enableCraftingITnt);
            var18 = config.get("general", "enableCraftingNuke", enableCraftingNuke);
            var18.comment = "Enable crafting of nukes";
            enableCraftingNuke = var18.getBoolean(enableCraftingNuke);
            var18 = config.get("general", "enableCraftingRail", enableCraftingRail);
            var18.comment = "Enable crafting of rails out of bronze";
            enableCraftingRail = var18.getBoolean(enableCraftingRail);
            var18 = config.get("general", "enableSecretRecipeHiding", enableSecretRecipeHiding);
            var18.comment = "Enable hiding of secret recipes in CraftGuide/NEI";
            enableSecretRecipeHiding = var18.getBoolean(enableSecretRecipeHiding);
            var18 = config.get("general", "enableQuantumSpeedOnSprint", enableQuantumSpeedOnSprint);
            var18.comment = "Enable activation of the quantum leggings\' speed boost when sprinting instead of holding the boost key";
            enableQuantumSpeedOnSprint = var18.getBoolean(enableQuantumSpeedOnSprint);
            var18 = config.get("general", "enableMinerLapotron", enableMinerLapotron);
            var18.comment = "Enable usage of lapotron crystals on miners";
            enableMinerLapotron = var18.getBoolean(enableMinerLapotron);
            var18 = config.get("general", "enableTeleporterInventory", enableTeleporterInventory);
            var18.comment = "Enable calculation of inventory weight when going through a teleporter";
            enableTeleporterInventory = var18.getBoolean(enableTeleporterInventory);
            var18 = config.get("general", "enableBurningScrap", enableBurningScrap);
            var18.comment = "Enable burning of scrap in a generator";
            enableBurningScrap = var18.getBoolean(enableBurningScrap);
            var18 = config.get("general", "enableLoggingWrench", enableLoggingWrench);
            var18.comment = "Enable logging of players when they remove a machine using a wrench";
            enableLoggingWrench = var18.getBoolean(enableLoggingWrench);
            var18 = config.get("general", "enableWorldGenTreeRubber", enableWorldGenTreeRubber);
            var18.comment = "Enable generation of rubber trees in the world";
            enableWorldGenTreeRubber = var18.getBoolean(enableWorldGenTreeRubber);
            var18 = config.get("general", "enableWorldGenOreCopper", enableWorldGenOreCopper);
            var18.comment = "Enable generation of copper in the world";
            enableWorldGenOreCopper = var18.getBoolean(enableWorldGenOreCopper);
            var18 = config.get("general", "enableWorldGenOreTin", enableWorldGenOreTin);
            var18.comment = "Enable generation of tin in the world";
            enableWorldGenOreTin = var18.getBoolean(enableWorldGenOreTin);
            var18 = config.get("general", "enableWorldGenOreUranium", enableWorldGenOreUranium);
            var18.comment = "Enable generation of uranium in the world";
            enableWorldGenOreUranium = var18.getBoolean(enableWorldGenOreUranium);
            var18 = config.get("general", "enableSteamReactor", enableSteamReactor);
            var18.comment = "Enable steam-outputting reactors if Railcraft is installed";
            enableSteamReactor = var18.getBoolean(enableSteamReactor);
            var18 = config.get("general", "explosionPowerNuke", Float.toString(explosionPowerNuke));
            var18.comment = "Explosion power of a nuke, where TNT is 4";
            explosionPowerNuke = (float)var18.getDouble((double)explosionPowerNuke);
            var18 = config.get("general", "explosionPowerReactorMax", Float.toString(explosionPowerReactorMax));
            var18.comment = "Maximum explosion power of a nuclear reactor, where TNT is 4";
            explosionPowerReactorMax = (float)var18.getDouble((double)explosionPowerReactorMax);
            var18 = config.get("general", "energyGeneratorBase", energyGeneratorBase);
            var18.comment = "Base energy generation values - increase those for higher energy yield";
            energyGeneratorBase = var18.getInt(energyGeneratorBase);
            energyGeneratorGeo = config.get("general", "energyGeneratorGeo", energyGeneratorGeo).getInt(energyGeneratorGeo);
            energyGeneratorWater = config.get("general", "energyGeneratorWater", energyGeneratorWater).getInt(energyGeneratorWater);
            energyGeneratorSolar = config.get("general", "energyGeneratorSolar", energyGeneratorSolar).getInt(energyGeneratorSolar);
            energyGeneratorWind = config.get("general", "energyGeneratorWind", energyGeneratorWind).getInt(energyGeneratorWind);
            energyGeneratorNuclear = config.get("general", "energyGeneratorNuclear", energyGeneratorNuclear).getInt(energyGeneratorNuclear);
            var18 = config.get("general", "valuableOres", getValuableOreString());
            var18.comment = "List of valuable ores the miner should look for. Comma separated, format is id-metadata:value where value should be at least 1 to be considered by the miner";
            setValuableOreFromString(var18.getString());
            var18 = config.get("general", "valuableOres", getValuableOreString());
            var18.comment = "List of valuable ores the miner should look for. Comma separated, format is id-metadata:value where value should be at least 1 to be considered by the miner";
            setValuableOreFromString(var18.getString());
            var18 = config.get("general", "oreDensityFactor", Float.toString(oreDensityFactor));
            var18.comment = "Factor to adjust the ore generation rate";
            oreDensityFactor = (float)var18.getDouble((double)oreDensityFactor);

            if (config.hasChanged())
            {
                config.save();
            }

            Iterator bronzeArmorMaterial = config.getCategory("block").values().iterator();
            Property alloyArmorMaterial;

            while (bronzeArmorMaterial.hasNext())
            {
                alloyArmorMaterial = (Property)bronzeArmorMaterial.next();
                blockedIds.add(Integer.valueOf(alloyArmorMaterial.getInt()));
            }

            bronzeArmorMaterial = config.getCategory("item").values().iterator();

            while (bronzeArmorMaterial.hasNext())
            {
                alloyArmorMaterial = (Property)bronzeArmorMaterial.next();
                blockedIds.add(Integer.valueOf(alloyArmorMaterial.getInt() + 256));
            }
        }

        audioManager.initialize(config);
        runtimeIdProperties.put("initialVersion", "1.118.401-lf");
        EnumHelper.addToolMaterial("IC2_BRONZE", 2, 350, 6.0F, 2.0F, 13);
        EnumArmorMaterial var19 = EnumHelper.addArmorMaterial("IC2_BRONZE", 15, new int[] {3, 8, 6, 3}, 9);
        EnumArmorMaterial var20 = EnumHelper.addArmorMaterial("IC2_ALLOY", 50, new int[] {4, 9, 7, 4}, 12);

        if (enableWorldGenOreCopper)
        {
            Ic2Items.copperOre = new ItemStack((new BlockSimple(config, InternalName.blockOreCopper, Material.rock)).setHardness(3.0F).setResistance(5.0F));
        }

        if (enableWorldGenOreTin)
        {
            Ic2Items.tinOre = new ItemStack((new BlockSimple(config, InternalName.blockOreTin, Material.rock)).setHardness(3.0F).setResistance(5.0F));
        }

        if (enableWorldGenOreUranium)
        {
            Ic2Items.uraniumOre = new ItemStack((new BlockSimple(config, InternalName.blockOreUran, Material.rock)).setHardness(4.0F).setResistance(6.0F));
        }

        if (enableWorldGenTreeRubber)
        {
            new BlockRubWood(config, InternalName.blockRubWood);
            new BlockRubLeaves(config, InternalName.blockRubLeaves);
            new BlockRubSapling(config, InternalName.blockRubSapling);
        }

        new BlockResin(config, InternalName.blockHarz);
        new BlockRubberSheet(config, InternalName.blockRubber);
        new BlockPoleFence(config, InternalName.blockFenceIron);
        Ic2Items.reinforcedStone = new ItemStack((new BlockSimple(config, InternalName.blockAlloy, Material.iron)).setHardness(80.0F).setResistance(150.0F).setStepSound(Block.soundMetalFootstep));
        Ic2Items.reinforcedGlass = new ItemStack(new BlockTexGlass(config, InternalName.blockAlloyGlass));
        Ic2Items.reinforcedDoorBlock = new ItemStack(new BlockIC2Door(config, InternalName.blockDoorAlloy));
        new BlockFoam(config, InternalName.blockFoam);
        new BlockWall(config, InternalName.blockWall);
        new BlockScaffold(config, InternalName.blockScaffold);
        new BlockScaffold(config, InternalName.blockIronScaffold);
        new BlockMetal(config, InternalName.blockMetal);
        new BlockCable(config, InternalName.blockCable);
        new BlockGenerator(config, InternalName.blockGenerator);
        new BlockReactorChamber(config, InternalName.blockReactorChamber);
        new BlockElectric(config, InternalName.blockElectric);
        new BlockMachine(config, InternalName.blockMachine);
        new BlockMachine2(config, InternalName.blockMachine2);
        Ic2Items.luminator = new ItemStack(new BlockLuminator(config, InternalName.blockLuminatorDark));
        Ic2Items.activeLuminator = new ItemStack(new BlockLuminator(config, InternalName.blockLuminator));
        new BlockMiningPipe(config, InternalName.blockMiningPipe);
        new BlockMiningTip(config, InternalName.blockMiningTip);
        new BlockPersonal(config, InternalName.blockPersonal);
        Ic2Items.industrialTnt = new ItemStack(new BlockITNT(config, InternalName.blockITNT));
        Ic2Items.nuke = new ItemStack(new BlockITNT(config, InternalName.blockNuke));
        Ic2Items.dynamiteStick = new ItemStack(new BlockDynamite(config, InternalName.blockDynamite));
        Ic2Items.dynamiteStickWithRemote = new ItemStack(new BlockDynamite(config, InternalName.blockDynamiteRemote));
        new BlockCrop(config, InternalName.blockCrop);
        new BlockBarrel(config, InternalName.blockBarrel);
        Ic2Items.resin = new ItemStack(new ItemResin(config, InternalName.itemHarz));
        Ic2Items.rubber = new ItemStack(new ItemIC2(config, InternalName.itemRubber));
        Ic2Items.uraniumDrop = new ItemStack(new ItemIC2(config, InternalName.itemOreUran));
        Ic2Items.bronzeDust = new ItemStack(new ItemIC2(config, InternalName.itemDustBronze));
        Ic2Items.clayDust = new ItemStack(new ItemIC2(config, InternalName.itemDustClay));
        Ic2Items.coalDust = new ItemStack(new ItemIC2(config, InternalName.itemDustCoal));
        Ic2Items.copperDust = new ItemStack(new ItemIC2(config, InternalName.itemDustCopper));
        Ic2Items.goldDust = new ItemStack(new ItemIC2(config, InternalName.itemDustGold));
        Ic2Items.ironDust = new ItemStack(new ItemIC2(config, InternalName.itemDustIron));
        Ic2Items.silverDust = new ItemStack(new ItemIC2(config, InternalName.itemDustSilver));
        Ic2Items.smallIronDust = new ItemStack(new ItemIC2(config, InternalName.itemDustIronSmall));
        Ic2Items.tinDust = new ItemStack(new ItemIC2(config, InternalName.itemDustTin));
        Ic2Items.hydratedCoalDust = new ItemStack(new ItemIC2(config, InternalName.itemFuelCoalDust));
        Ic2Items.refinedIronIngot = new ItemStack(new ItemIC2(config, InternalName.itemIngotAdvIron));
        Ic2Items.copperIngot = new ItemStack(new ItemIC2(config, InternalName.itemIngotCopper));
        Ic2Items.tinIngot = new ItemStack(new ItemIC2(config, InternalName.itemIngotTin));
        Ic2Items.bronzeIngot = new ItemStack(new ItemIC2(config, InternalName.itemIngotBronze));
        Ic2Items.mixedMetalIngot = new ItemStack(new ItemIC2(config, InternalName.itemIngotAlloy));
        Ic2Items.uraniumIngot = new ItemStack(new ItemIC2(config, InternalName.itemIngotUran));
        Ic2Items.electronicCircuit = new ItemStack(new ItemIC2(config, InternalName.itemPartCircuit));
        Ic2Items.advancedCircuit = new ItemStack((new ItemIC2(config, InternalName.itemPartCircuitAdv)).setRarity(1).setUnlocalizedName("itemPartCircuitAdv").setCreativeTab(tabIC2));
        Ic2Items.advancedAlloy = new ItemStack(new ItemIC2(config, InternalName.itemPartAlloy));
        Ic2Items.carbonFiber = new ItemStack(new ItemIC2(config, InternalName.itemPartCarbonFibre));
        Ic2Items.carbonMesh = new ItemStack(new ItemIC2(config, InternalName.itemPartCarbonMesh));
        Ic2Items.carbonPlate = new ItemStack(new ItemIC2(config, InternalName.itemPartCarbonPlate));
        Ic2Items.matter = new ItemStack((new ItemIC2(config, InternalName.itemMatter)).setRarity(2).setUnlocalizedName("itemMatter").setCreativeTab(tabIC2));
        Ic2Items.iridiumOre = new ItemStack((new ItemIC2(config, InternalName.itemOreIridium)).setRarity(2).setUnlocalizedName("itemOreIridium").setCreativeTab(tabIC2));
        Ic2Items.iridiumPlate = new ItemStack((new ItemIC2(config, InternalName.itemPartIridium)).setRarity(2).setUnlocalizedName("itemPartIridium").setCreativeTab(tabIC2));
        Ic2Items.denseCopperPlate = new ItemStack(new ItemIC2(config, InternalName.itemPartDCP));
        Ic2Items.treetap = new ItemStack(new ItemTreetap(config, InternalName.itemTreetap));
        Ic2Items.bronzePickaxe = new ItemStack(new ItemIC2Pickaxe(config, InternalName.itemToolBronzePickaxe, EnumToolMaterial.IRON, 5.0F, "ingotBronze"));
        Ic2Items.bronzeAxe = new ItemStack(new ItemIC2Axe(config, InternalName.itemToolBronzeAxe, EnumToolMaterial.IRON, 5.0F, "ingotBronze"));
        Ic2Items.bronzeSword = new ItemStack(new ItemIC2Sword(config, InternalName.itemToolBronzeSword, EnumToolMaterial.IRON, 7, "ingotBronze"));
        Ic2Items.bronzeShovel = new ItemStack(new ItemIC2Spade(config, InternalName.itemToolBronzeSpade, EnumToolMaterial.IRON, 5.0F, "ingotBronze"));
        Ic2Items.bronzeHoe = new ItemStack(new ItemIC2Hoe(config, InternalName.itemToolBronzeHoe, EnumToolMaterial.IRON, "ingotBronze"));
        Ic2Items.wrench = new ItemStack(new ItemToolWrench(config, InternalName.itemToolWrench));
        Ic2Items.cutter = new ItemStack(new ItemToolCutter(config, InternalName.itemToolCutter));
        Ic2Items.constructionFoamSprayer = new ItemStack(new ItemSprayer(config, InternalName.itemFoamSprayer));
        Ic2Items.toolbox = new ItemStack(new ItemToolbox(config, InternalName.itemToolbox));
        Ic2Items.miningDrill = new ItemStack(new ItemElectricToolDrill(config, InternalName.itemToolDrill));
        Ic2Items.diamondDrill = new ItemStack(new ItemElectricToolDDrill(config, InternalName.itemToolDDrill));
        Ic2Items.chainsaw = new ItemStack(new ItemElectricToolChainsaw(config, InternalName.itemToolChainsaw));
        Ic2Items.electricWrench = new ItemStack(new ItemToolWrenchElectric(config, InternalName.itemToolWrenchElectric));
        Ic2Items.electricTreetap = new ItemStack(new ItemTreetapElectric(config, InternalName.itemTreetapElectric));
        Ic2Items.miningLaser = new ItemStack(new ItemToolMiningLaser(config, InternalName.itemToolMiningLaser));
        Ic2Items.ecMeter = new ItemStack(new ItemToolMeter(config, InternalName.itemToolMEter));
        Ic2Items.odScanner = new ItemStack(new ItemScanner(config, InternalName.itemScanner, 1));
        Ic2Items.ovScanner = new ItemStack(new ItemScannerAdv(config, InternalName.itemScannerAdv, 2));
        Ic2Items.obscurator = new ItemStack(new ItemObscurator(config, InternalName.obscurator));
        Ic2Items.frequencyTransmitter = new ItemStack(new ItemFrequencyTransmitter(config, InternalName.itemFreq));
        Ic2Items.nanoSaber = new ItemStack(new ItemNanoSaber(config, InternalName.itemNanoSaber));
        Ic2Items.hazmatHelmet = new ItemStack(new ItemArmorHazmat(config, InternalName.itemArmorHazmatHelmet, 0));
        Ic2Items.hazmatChestplate = new ItemStack(new ItemArmorHazmat(config, InternalName.itemArmorHazmatChestplate, 1));
        Ic2Items.hazmatLeggings = new ItemStack(new ItemArmorHazmat(config, InternalName.itemArmorHazmatLeggings, 2));
        Ic2Items.hazmatBoots = new ItemStack(new ItemArmorHazmat(config, InternalName.itemArmorRubBoots, 3));
        Ic2Items.bronzeHelmet = new ItemStack(new ItemArmorIC2(config, InternalName.itemArmorBronzeHelmet, var19, InternalName.bronze, 0, "ingotBronze"));
        Ic2Items.bronzeChestplate = new ItemStack(new ItemArmorIC2(config, InternalName.itemArmorBronzeChestplate, var19, InternalName.bronze, 1, "ingotBronze"));
        Ic2Items.bronzeLeggings = new ItemStack(new ItemArmorIC2(config, InternalName.itemArmorBronzeLegs, var19, InternalName.bronze, 2, "ingotBronze"));
        Ic2Items.bronzeBoots = new ItemStack(new ItemArmorIC2(config, InternalName.itemArmorBronzeBoots, var19, InternalName.bronze, 3, "ingotBronze"));
        Ic2Items.compositeArmor = new ItemStack(new ItemArmorIC2(config, InternalName.itemArmorAlloyChestplate, var20, InternalName.alloy, 1, Ic2Items.advancedAlloy));
        Ic2Items.nanoHelmet = new ItemStack(new ItemArmorNanoSuit(config, InternalName.itemArmorNanoHelmet, 0));
        Ic2Items.nanoBodyarmor = new ItemStack(new ItemArmorNanoSuit(config, InternalName.itemArmorNanoChestplate, 1));
        Ic2Items.nanoLeggings = new ItemStack(new ItemArmorNanoSuit(config, InternalName.itemArmorNanoLegs, 2));
        Ic2Items.nanoBoots = new ItemStack(new ItemArmorNanoSuit(config, InternalName.itemArmorNanoBoots, 3));
        Ic2Items.quantumHelmet = new ItemStack(new ItemArmorQuantumSuit(config, InternalName.itemArmorQuantumHelmet, 0));
        Ic2Items.quantumBodyarmor = new ItemStack(new ItemArmorQuantumSuit(config, InternalName.itemArmorQuantumChestplate, 1));
        Ic2Items.quantumLeggings = new ItemStack(new ItemArmorQuantumSuit(config, InternalName.itemArmorQuantumLegs, 2));
        Ic2Items.quantumBoots = new ItemStack(new ItemArmorQuantumSuit(config, InternalName.itemArmorQuantumBoots, 3));
        Ic2Items.jetpack = new ItemStack(new ItemArmorJetpack(config, InternalName.itemArmorJetpack));
        Ic2Items.electricJetpack = new ItemStack(new ItemArmorJetpackElectric(config, InternalName.itemArmorJetpackElectric));
        Ic2Items.batPack = new ItemStack(new ItemArmorBatpack(config, InternalName.itemArmorBatpack));
        Ic2Items.lapPack = new ItemStack(new ItemArmorLappack(config, InternalName.itemArmorLappack));
        Ic2Items.cfPack = new ItemStack(new ItemArmorCFPack(config, InternalName.itemArmorCFPack));
        Ic2Items.solarHelmet = new ItemStack(new ItemArmorSolarHelmet(config, InternalName.itemSolarHelmet));
        Ic2Items.staticBoots = new ItemStack(new ItemArmorStaticBoots(config, InternalName.itemStaticBoots));
        Ic2Items.nightvisionGoggles = new ItemStack(new ItemArmorNightvisionGoggles(config, InternalName.itemNightvisionGoggles));
        Ic2Items.reBattery = new ItemStack(new ItemBatteryDischarged(config, InternalName.itemBatREDischarged, 10000, 100, 1));
        Ic2Items.chargedReBattery = new ItemStack(new ItemBattery(config, InternalName.itemBatRE, 10000, 100, 1));
        Ic2Items.energyCrystal = new ItemStack(new ItemBattery(config, InternalName.itemBatCrystal, 100000, 250, 2));
        Ic2Items.lapotronCrystal = new ItemStack((new ItemBattery(config, InternalName.itemBatLamaCrystal, 1000000, 600, 3)).setRarity(1));
        Ic2Items.suBattery = new ItemStack(new ItemBatterySU(config, InternalName.itemBatSU, 1000, 1));
        new ItemCable(config, InternalName.itemCable);
        Ic2Items.cell = new ItemStack(new ItemCell(config, InternalName.itemCellEmpty));
        Ic2Items.lavaCell = new ItemStack(new ItemIC2(config, InternalName.itemCellLava));
        Ic2Items.hydratedCoalCell = new ItemStack(new ItemIC2(config, InternalName.itemCellCoal));
        Ic2Items.bioCell = new ItemStack(new ItemIC2(config, InternalName.itemCellBio));
        Ic2Items.coalfuelCell = new ItemStack(new ItemIC2(config, InternalName.itemCellCoalRef));
        Ic2Items.biofuelCell = new ItemStack(new ItemIC2(config, InternalName.itemCellBioRef));
        Ic2Items.waterCell = new ItemStack(new ItemIC2(config, InternalName.itemCellWater));
        Ic2Items.electrolyzedWaterCell = new ItemStack(new ItemIC2(config, InternalName.itemCellWaterElectro));
        Ic2Items.fuelCan = new ItemStack(new ItemFuelCanEmpty(config, InternalName.itemFuelCanEmpty));
        Ic2Items.filledFuelCan = new ItemStack(new ItemFuelCanFilled(config, InternalName.itemFuelCan));
        Ic2Items.tinCan = new ItemStack(new ItemIC2(config, InternalName.itemTinCan));
        Ic2Items.filledTinCan = new ItemStack(new ItemTinCan(config, InternalName.itemTinCanFilled));
        Ic2Items.airCell = new ItemStack(new ItemIC2(config, InternalName.itemCellAir));
        Ic2Items.reactorUraniumSimple = new ItemStack(new ItemReactorUranium(config, InternalName.reactorUraniumSimple, 1));
        Ic2Items.reactorUraniumDual = new ItemStack(new ItemReactorUranium(config, InternalName.reactorUraniumDual, 2));
        Ic2Items.reactorUraniumQuad = new ItemStack(new ItemReactorUranium(config, InternalName.reactorUraniumQuad, 4));
        Ic2Items.reactorCoolantSimple = new ItemStack(new ItemReactorHeatStorage(config, InternalName.reactorCoolantSimple, 10000));
        Ic2Items.reactorCoolantTriple = new ItemStack(new ItemReactorHeatStorage(config, InternalName.reactorCoolantTriple, 30000));
        Ic2Items.reactorCoolantSix = new ItemStack(new ItemReactorHeatStorage(config, InternalName.reactorCoolantSix, 60000));
        Ic2Items.reactorPlating = new ItemStack(new ItemReactorPlating(config, InternalName.reactorPlating, 1000, 0.95F));
        Ic2Items.reactorPlatingHeat = new ItemStack(new ItemReactorPlating(config, InternalName.reactorPlatingHeat, 2000, 0.99F));
        Ic2Items.reactorPlatingExplosive = new ItemStack(new ItemReactorPlating(config, InternalName.reactorPlatingExplosive, 500, 0.9F));
        Ic2Items.reactorHeatSwitch = new ItemStack(new ItemReactorHeatSwitch(config, InternalName.reactorHeatSwitch, 2500, 12, 4));
        Ic2Items.reactorHeatSwitchCore = new ItemStack(new ItemReactorHeatSwitch(config, InternalName.reactorHeatSwitchCore, 5000, 0, 72));
        Ic2Items.reactorHeatSwitchSpread = new ItemStack(new ItemReactorHeatSwitch(config, InternalName.reactorHeatSwitchSpread, 5000, 36, 0));
        Ic2Items.reactorHeatSwitchDiamond = new ItemStack(new ItemReactorHeatSwitch(config, InternalName.reactorHeatSwitchDiamond, 10000, 24, 8));
        Ic2Items.reactorVent = new ItemStack(new ItemReactorVent(config, InternalName.reactorVent, 1000, 6, 0));
        Ic2Items.reactorVentCore = new ItemStack(new ItemReactorVent(config, InternalName.reactorVentCore, 1000, 5, 5));
        Ic2Items.reactorVentGold = new ItemStack(new ItemReactorVent(config, InternalName.reactorVentGold, 1000, 20, 36));
        Ic2Items.reactorVentSpread = new ItemStack(new ItemReactorVentSpread(config, InternalName.reactorVentSpread, 4));
        Ic2Items.reactorVentDiamond = new ItemStack(new ItemReactorVent(config, InternalName.reactorVentDiamond, 1000, 12, 0));
        Ic2Items.reactorIsotopeCell = new ItemStack(new ItemReactorDepletedUranium(config, InternalName.reactorIsotopeCell));
        Ic2Items.reEnrichedUraniumCell = new ItemStack(new ItemIC2(config, InternalName.itemCellUranEnriched));
        Ic2Items.nearDepletedUraniumCell = new ItemStack(new ItemIC2(config, InternalName.itemCellUranEmpty));
        Ic2Items.reactorHeatpack = new ItemStack(new ItemReactorHeatpack(config, InternalName.reactorHeatpack, 1000, 1));
        Ic2Items.reactorReflector = new ItemStack(new ItemReactorReflector(config, InternalName.reactorReflector, 10000));
        Ic2Items.reactorReflectorThick = new ItemStack(new ItemReactorReflector(config, InternalName.reactorReflectorThick, 40000));
        Ic2Items.reactorCondensator = new ItemStack(new ItemReactorCondensator(config, InternalName.reactorCondensator, 20000));
        Ic2Items.reactorCondensatorLap = new ItemStack(new ItemReactorCondensator(config, InternalName.reactorCondensatorLap, 100000));
        Ic2Items.terraformerBlueprint = new ItemStack(new ItemIC2(config, InternalName.itemTFBP));
        Ic2Items.cultivationTerraformerBlueprint = new ItemStack(new ItemTFBPCultivation(config, InternalName.itemTFBPCultivation));
        Ic2Items.irrigationTerraformerBlueprint = new ItemStack(new ItemTFBPIrrigation(config, InternalName.itemTFBPIrrigation));
        Ic2Items.chillingTerraformerBlueprint = new ItemStack(new ItemTFBPChilling(config, InternalName.itemTFBPChilling));
        Ic2Items.desertificationTerraformerBlueprint = new ItemStack(new ItemTFBPDesertification(config, InternalName.itemTFBPDesertification));
        Ic2Items.flatificatorTerraformerBlueprint = new ItemStack(new ItemTFBPFlatification(config, InternalName.itemTFBPFlatification));
        Ic2Items.mushroomTerraformerBlueprint = new ItemStack(new ItemTFBPMushroom(config, InternalName.itemTFBPMushroom));
        Ic2Items.coalBall = new ItemStack(new ItemIC2(config, InternalName.itemPartCoalBall));
        Ic2Items.compressedCoalBall = new ItemStack(new ItemIC2(config, InternalName.itemPartCoalBlock));
        Ic2Items.coalChunk = new ItemStack(new ItemIC2(config, InternalName.itemPartCoalChunk));
        Ic2Items.industrialDiamond = new ItemStack((new ItemIC2(config, InternalName.itemPartIndustrialDiamond)).setUnlocalizedName("itemPartIndustrialDiamond"));
        Ic2Items.scrap = new ItemStack(new ItemIC2(config, InternalName.itemScrap));
        Ic2Items.scrapBox = new ItemStack(new ItemScrapbox(config, InternalName.itemScrapbox));
        Ic2Items.hydratedCoalClump = new ItemStack(new ItemIC2(config, InternalName.itemFuelCoalCmpr));
        Ic2Items.plantBall = new ItemStack(new ItemIC2(config, InternalName.itemFuelPlantBall));
        Ic2Items.compressedPlantBall = new ItemStack(new ItemIC2(config, InternalName.itemFuelPlantCmpr));
        Ic2Items.painter = new ItemStack(new ItemIC2(config, InternalName.itemToolPainter));
        Ic2Items.blackPainter = new ItemStack(new ItemToolPainter(config, InternalName.itemToolPainterBlack, 0));
        Ic2Items.redPainter = new ItemStack(new ItemToolPainter(config, InternalName.itemToolPainterRed, 1));
        Ic2Items.greenPainter = new ItemStack(new ItemToolPainter(config, InternalName.itemToolPainterGreen, 2));
        Ic2Items.brownPainter = new ItemStack(new ItemToolPainter(config, InternalName.itemToolPainterBrown, 3));
        Ic2Items.bluePainter = new ItemStack(new ItemToolPainter(config, InternalName.itemToolPainterBlue, 4));
        Ic2Items.purplePainter = new ItemStack(new ItemToolPainter(config, InternalName.itemToolPainterPurple, 5));
        Ic2Items.cyanPainter = new ItemStack(new ItemToolPainter(config, InternalName.itemToolPainterCyan, 6));
        Ic2Items.lightGreyPainter = new ItemStack(new ItemToolPainter(config, InternalName.itemToolPainterLightGrey, 7));
        Ic2Items.darkGreyPainter = new ItemStack(new ItemToolPainter(config, InternalName.itemToolPainterDarkGrey, 8));
        Ic2Items.pinkPainter = new ItemStack(new ItemToolPainter(config, InternalName.itemToolPainterPink, 9));
        Ic2Items.limePainter = new ItemStack(new ItemToolPainter(config, InternalName.itemToolPainterLime, 10));
        Ic2Items.yellowPainter = new ItemStack(new ItemToolPainter(config, InternalName.itemToolPainterYellow, 11));
        Ic2Items.cloudPainter = new ItemStack(new ItemToolPainter(config, InternalName.itemToolPainterCloud, 12));
        Ic2Items.magentaPainter = new ItemStack(new ItemToolPainter(config, InternalName.itemToolPainterMagenta, 13));
        Ic2Items.orangePainter = new ItemStack(new ItemToolPainter(config, InternalName.itemToolPainterOrange, 14));
        Ic2Items.whitePainter = new ItemStack(new ItemToolPainter(config, InternalName.itemToolPainterWhite, 15));
        Ic2Items.dynamite = new ItemStack(new ItemDynamite(config, InternalName.itemDynamite, false));
        Ic2Items.stickyDynamite = new ItemStack(new ItemDynamite(config, InternalName.itemDynamiteSticky, true));
        Ic2Items.remote = new ItemStack(new ItemRemote(config, InternalName.itemRemote));
        new ItemUpgradeModule(config, InternalName.upgradeModule);
        Ic2Items.coin = new ItemStack(new ItemIC2(config, InternalName.itemCoin));
        Ic2Items.reinforcedDoor = new ItemStack(new ItemIC2Door(config, InternalName.itemDoorAlloy, Block.blocksList[Ic2Items.reinforcedDoorBlock.itemID]));
        Ic2Items.constructionFoamPellet = new ItemStack(new ItemIC2(config, InternalName.itemPartPellet));
        Ic2Items.grinPowder = new ItemStack(new ItemIC2(config, InternalName.itemGrinPowder));
        Ic2Items.debug = new ItemStack(new ItemDebug(config, InternalName.itemDebug));
        Ic2Items.coolant = new ItemStack(new ItemIC2(config, InternalName.itemCoolant));
        new ItemIC2Boat(config, InternalName.itemBoat);
        Ic2Items.cropSeed = new ItemStack(new ItemCropSeed(config, InternalName.itemCropSeed));
        Ic2Items.cropnalyzer = new ItemStack(new ItemCropnalyzer(config, InternalName.itemCropnalyzer));
        Ic2Items.fertilizer = new ItemStack(new ItemFertilizer(config, InternalName.itemFertilizer));
        Ic2Items.hydratingCell = new ItemStack(new ItemGradual(config, InternalName.itemCellHydrant));
        Ic2Items.electricHoe = new ItemStack(new ItemElectricToolHoe(config, InternalName.itemToolHoe));
        Ic2Items.terraWart = new ItemStack(new ItemTerraWart(config, InternalName.itemTerraWart));
        Ic2Items.weedEx = new ItemStack((new ItemIC2(config, InternalName.itemWeedEx)).setMaxStackSize(1).setMaxDamage(64));
        Ic2Items.mugEmpty = new ItemStack(new ItemMug(config, InternalName.itemMugEmpty));
        Ic2Items.coffeeBeans = new ItemStack(new ItemIC2(config, InternalName.itemCofeeBeans));
        Ic2Items.coffeePowder = new ItemStack(new ItemIC2(config, InternalName.itemCofeePowder));
        Ic2Items.mugCoffee = new ItemStack(new ItemMugCoffee(config, InternalName.itemMugCoffee));
        Ic2Items.hops = new ItemStack(new ItemIC2(config, InternalName.itemHops));
        Ic2Items.barrel = new ItemStack(new ItemBarrel(config, InternalName.itemBarrel));
        Ic2Items.mugBooze = new ItemStack(new ItemBooze(config, InternalName.itemMugBooze));
        new ItemMigrate(config, InternalName.itemNanoSaberOff, Ic2Items.nanoSaber.getItem());
        Block.obsidian.setResistance(60.0F);
        Block.enchantmentTable.setResistance(60.0F);
        Block.enderChest.setResistance(60.0F);
        Block.anvil.setResistance(60.0F);
        Block.waterMoving.setResistance(30.0F);
        Block.waterStill.setResistance(30.0F);
        Block.lavaStill.setResistance(30.0F);
        ((BlockIC2Door)Block.blocksList[Ic2Items.reinforcedDoorBlock.itemID]).setItemDropped(Ic2Items.reinforcedDoor.itemID);
        ElectricItem.manager = new GatewayElectricItemManager();
        ElectricItem.rawManager = new ElectricItemManager();
        ItemInfo itemInfo = new ItemInfo();
        Info.itemEnergy = itemInfo;
        Info.itemFuel = itemInfo;
        ExplosionWhitelist.addWhitelistedBlock(Block.bedrock);
        Recipes.matterAmplifier = new BasicMachineRecipeManager();
        Recipes.matterAmplifier.addRecipe(Ic2Items.scrap, Integer.valueOf(5000));
        Recipes.matterAmplifier.addRecipe(Ic2Items.scrapBox, Integer.valueOf(45000));
        FurnaceRecipes furnaceRecipes = FurnaceRecipes.smelting();

        if (Ic2Items.rubberWood != null)
        {
            furnaceRecipes.addSmelting(Ic2Items.rubberWood.itemID, Ic2Items.rubberWood.getItemDamage(), new ItemStack(Block.wood, 1, 3), 0.1F);
        }

        if (Ic2Items.tinOre != null)
        {
            furnaceRecipes.addSmelting(Ic2Items.tinOre.itemID, Ic2Items.tinOre.getItemDamage(), Ic2Items.tinIngot, 0.5F);
        }

        if (Ic2Items.copperOre != null)
        {
            furnaceRecipes.addSmelting(Ic2Items.copperOre.itemID, Ic2Items.copperOre.getItemDamage(), Ic2Items.copperIngot, 0.5F);
        }

        furnaceRecipes.addSmelting(Item.ingotIron.itemID, Ic2Items.refinedIronIngot, 0.2F);
        furnaceRecipes.addSmelting(Ic2Items.ironDust.itemID, Ic2Items.ironDust.getItemDamage(), new ItemStack(Item.ingotIron, 1), 0.0F);
        furnaceRecipes.addSmelting(Ic2Items.goldDust.itemID, Ic2Items.goldDust.getItemDamage(), new ItemStack(Item.ingotGold, 1), 0.0F);
        furnaceRecipes.addSmelting(Ic2Items.tinDust.itemID, Ic2Items.tinDust.getItemDamage(), Ic2Items.tinIngot.copy(), 0.0F);
        furnaceRecipes.addSmelting(Ic2Items.copperDust.itemID, Ic2Items.copperDust.getItemDamage(), Ic2Items.copperIngot.copy(), 0.0F);
        furnaceRecipes.addSmelting(Ic2Items.hydratedCoalDust.itemID, Ic2Items.hydratedCoalDust.getItemDamage(), Ic2Items.coalDust.copy(), 0.0F);
        furnaceRecipes.addSmelting(Ic2Items.bronzeDust.itemID, Ic2Items.bronzeDust.getItemDamage(), Ic2Items.bronzeIngot.copy(), 0.0F);
        furnaceRecipes.addSmelting(Ic2Items.resin.itemID, Ic2Items.resin.getItemDamage(), Ic2Items.rubber.copy(), 0.3F);
        furnaceRecipes.addSmelting(Ic2Items.mugCoffee.itemID, new ItemStack(Ic2Items.mugCoffee.getItem(), 1, 1), 0.1F);
        ((ItemElectricToolChainsaw)Ic2Items.chainsaw.getItem()).init();
        ((ItemElectricToolDrill)Ic2Items.miningDrill.getItem()).init();
        ((ItemElectricToolDDrill)Ic2Items.diamondDrill.getItem()).init();
        ItemScrapbox.init();
        ItemTFBPCultivation.init();
        ItemTFBPFlatification.init();
        TileEntityCompressor.init();
        TileEntityExtractor.init();
        TileEntityMacerator.init();
        TileEntityRecycler.init(config);
        MinecraftForge.setToolClass(Ic2Items.bronzePickaxe.getItem(), "pickaxe", 2);
        MinecraftForge.setToolClass(Ic2Items.bronzeAxe.getItem(), "axe", 2);
        MinecraftForge.setToolClass(Ic2Items.bronzeShovel.getItem(), "shovel", 2);
        MinecraftForge.setToolClass(Ic2Items.chainsaw.getItem(), "axe", 2);
        MinecraftForge.setToolClass(Ic2Items.miningDrill.getItem(), "pickaxe", 2);
        MinecraftForge.setToolClass(Ic2Items.diamondDrill.getItem(), "pickaxe", 3);
        MinecraftForge.setBlockHarvestLevel(Block.blocksList[Ic2Items.reinforcedStone.itemID], "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(Block.blocksList[Ic2Items.reinforcedDoorBlock.itemID], "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(Block.blocksList[Ic2Items.insulatedCopperCableBlock.itemID], "axe", 0);
        MinecraftForge.setBlockHarvestLevel(Block.blocksList[Ic2Items.constructionFoamWall.itemID], "pickaxe", 1);

        if (Ic2Items.copperOre != null)
        {
            MinecraftForge.setBlockHarvestLevel(Block.blocksList[Ic2Items.copperOre.itemID], "pickaxe", 1);
        }

        if (Ic2Items.tinOre != null)
        {
            MinecraftForge.setBlockHarvestLevel(Block.blocksList[Ic2Items.tinOre.itemID], "pickaxe", 1);
        }

        if (Ic2Items.uraniumOre != null)
        {
            MinecraftForge.setBlockHarvestLevel(Block.blocksList[Ic2Items.uraniumOre.itemID], "pickaxe", 2);
        }

        if (Ic2Items.rubberWood != null)
        {
            MinecraftForge.setBlockHarvestLevel(Block.blocksList[Ic2Items.rubberWood.itemID], "axe", 0);
        }

        windStrength = 10 + random.nextInt(10);
        windTicker = 0;
        Block.setBurnProperties(Ic2Items.scaffold.itemID, 8, 20);

        if (Ic2Items.rubberLeaves != null)
        {
            Block.setBurnProperties(Ic2Items.rubberLeaves.itemID, 30, 20);
        }

        if (Ic2Items.rubberWood != null)
        {
            Block.setBurnProperties(Ic2Items.rubberWood.itemID, 4, 20);
        }

        MinecraftForge.EVENT_BUS.register(this);
        Recipies.registerCraftingRecipes();
        String[] d = OreDictionary.getOreNames();
        int coolant = d.length;

        for (int i$ = 0; i$ < coolant; ++i$)
        {
            String oreName = d[i$];
            Iterator i$1 = OreDictionary.getOres(oreName).iterator();

            while (i$1.hasNext())
            {
                ItemStack ore = (ItemStack)i$1.next();
                this.registerOre(new OreRegisterEvent(oreName, ore));
            }
        }

        assert Ic2Items.uraniumDrop != null;
        assert Ic2Items.bronzeIngot != null;
        assert Ic2Items.copperIngot != null;
        assert Ic2Items.refinedIronIngot != null;
        assert Ic2Items.tinIngot != null;
        assert Ic2Items.uraniumIngot != null;
        assert Ic2Items.rubber != null;

        if (Ic2Items.copperOre != null)
        {
            OreDictionary.registerOre("oreCopper", Ic2Items.copperOre);
        }

        if (Ic2Items.tinOre != null)
        {
            OreDictionary.registerOre("oreTin", Ic2Items.tinOre);
        }

        if (Ic2Items.uraniumOre != null)
        {
            OreDictionary.registerOre("oreUranium", Ic2Items.uraniumOre);
        }

        OreDictionary.registerOre("dropUranium", Ic2Items.uraniumDrop);
        OreDictionary.registerOre("dustBronze", Ic2Items.bronzeDust);
        OreDictionary.registerOre("dustClay", Ic2Items.clayDust);
        OreDictionary.registerOre("dustCoal", Ic2Items.coalDust);
        OreDictionary.registerOre("dustCopper", Ic2Items.copperDust);
        OreDictionary.registerOre("dustGold", Ic2Items.goldDust);
        OreDictionary.registerOre("dustIron", Ic2Items.ironDust);
        OreDictionary.registerOre("dustSilver", Ic2Items.silverDust);
        OreDictionary.registerOre("dustTin", Ic2Items.tinDust);
        OreDictionary.registerOre("ingotBronze", Ic2Items.bronzeIngot);
        OreDictionary.registerOre("ingotCopper", Ic2Items.copperIngot);
        OreDictionary.registerOre("ingotRefinedIron", Ic2Items.refinedIronIngot);
        OreDictionary.registerOre("ingotTin", Ic2Items.tinIngot);
        OreDictionary.registerOre("ingotUranium", Ic2Items.uraniumIngot);
        OreDictionary.registerOre("itemRubber", Ic2Items.rubber);
        OreDictionary.registerOre("blockBronze", Ic2Items.bronzeBlock);
        OreDictionary.registerOre("blockCopper", Ic2Items.copperBlock);
        OreDictionary.registerOre("blockTin", Ic2Items.tinBlock);
        OreDictionary.registerOre("blockUranium", Ic2Items.uraniumBlock);

        if (Ic2Items.rubberWood != null)
        {
            OreDictionary.registerOre("woodRubber", Ic2Items.rubberWood);
        }

        EnergyNet.initialize();
        IC2Crops.init();
        Crops.instance.addBiomeBonus(BiomeGenBase.river, 2, 0);
        Crops.instance.addBiomeBonus(BiomeGenBase.swampland, 2, 2);
        Crops.instance.addBiomeBonus(BiomeGenBase.forest, 1, 1);
        Crops.instance.addBiomeBonus(BiomeGenBase.forestHills, 1, 1);
        Crops.instance.addBiomeBonus(BiomeGenBase.jungle, 1, 2);
        Crops.instance.addBiomeBonus(BiomeGenBase.jungleHills, 1, 2);
        Crops.instance.addBiomeBonus(BiomeGenBase.desert, -1, 0);
        Crops.instance.addBiomeBonus(BiomeGenBase.desertHills, -1, 0);
        Crops.instance.addBiomeBonus(BiomeGenBase.mushroomIsland, 0, 2);
        Crops.instance.addBiomeBonus(BiomeGenBase.mushroomIslandShore, 0, 2);
        IC2Potion.init();
        new IC2Loot();
        achievements = new IC2Achievements();
        enableDynamicIdAllocation = false;

        if (dynamicIdAllocationProp != null)
        {
            dynamicIdAllocationProp.set(false);
        }

        if (config != null && config.hasChanged())
        {
            config.save();
        }

        EntityRegistry.registerModEntity(EntityMiningLaser.class, "MiningLaser", 0, this, 160, 5, true);
        EntityRegistry.registerModEntity(EntityDynamite.class, "Dynamite", 1, this, 160, 5, true);
        EntityRegistry.registerModEntity(EntityStickyDynamite.class, "StickyDynamite", 2, this, 160, 5, true);
        EntityRegistry.registerModEntity(EntityItnt.class, "Itnt", 3, this, 160, 5, true);
        EntityRegistry.registerModEntity(EntityNuke.class, "Nuke", 4, this, 160, 5, true);
        EntityRegistry.registerModEntity(EntityBoatCarbon.class, "BoatCarbon", 5, this, 80, 3, true);
        EntityRegistry.registerModEntity(EntityBoatRubber.class, "BoatRubber", 6, this, 80, 3, true);
        EntityRegistry.registerModEntity(EntityBoatElectric.class, "BoatElectric", 7, this, 80, 3, true);
        int var21 = Integer.parseInt((new SimpleDateFormat("Mdd")).format(new Date()));
        suddenlyHoes = (double)var21 > Math.cbrt(6.4E7D) && (double)var21 < Math.cbrt(6.5939264E7D);
        seasonal = (double)var21 > Math.cbrt(1.089547389E9D) && (double)var21 < Math.cbrt(1.338273208E9D);
        TickRegistry.registerTickHandler(this, Side.CLIENT);
        TickRegistry.registerTickHandler(this, Side.SERVER);
        GameRegistry.registerWorldGenerator(this);
        GameRegistry.registerFuelHandler(this);
        NetworkRegistry.instance().registerConnectionHandler(this);
        GameRegistry.registerPlayerTracker(this);
        FluidContainerRegistry.registerFluidContainer(FluidRegistry.WATER, Ic2Items.waterCell.copy(), Ic2Items.cell.copy());
        FluidContainerRegistry.registerFluidContainer(FluidRegistry.LAVA, Ic2Items.lavaCell.copy(), Ic2Items.cell.copy());
        Fluid var22 = new Fluid("coolant");
        FluidRegistry.registerFluid(var22);
        FluidContainerRegistry.registerFluidContainer(var22, Ic2Items.reactorCoolantSimple.copy(), Ic2Items.cell.copy());
        Localization.init(event.getSourceFile());
        initialized = true;
    }

    @EventHandler
    public void modsLoaded(FMLPostInitializationEvent event)
    {
        if (!initialized)
        {
            platform.displayError("IndustrialCraft 2 has failed to initialize properly.");
        }

        if (loadSubModule("bcIntegration"))
        {
            log.info("BuildCraft integration module loaded");
        }

        String noGrab = "" + Ic2Items.miningPipe.itemID + ", " + Ic2Items.miningPipeTip.itemID + ", " + Ic2Items.scaffold.itemID + ", " + Ic2Items.rubberTrampoline.itemID;

        if (Ic2Items.rubberWood != null)
        {
            noGrab = noGrab + ", " + Ic2Items.rubberWood.itemID + ": 2: 3: 4: 5: 6: 7: 8: 9: 10: 11";
        }

        FMLInterModComms.sendMessage("PortalGun", "addBlockIDToGrabList", noGrab);
        FMLInterModComms.sendMessage("GraviGun", "addBlockIDToGrabList", noGrab);

        try
        {
            Method e = Class.forName("mod_Gibbing").getMethod("addCustomItem", new Class[] {Integer.TYPE, Double.TYPE});
            e.invoke((Object)null, new Object[] {Integer.valueOf(Ic2Items.nanoSaber.itemID), Double.valueOf(0.5D)});
            e.invoke((Object)null, new Object[] {Integer.valueOf(Ic2Items.chainsaw.itemID), Double.valueOf(0.5D)});
            e.invoke((Object)null, new Object[] {Integer.valueOf(Ic2Items.miningDrill.itemID), Double.valueOf(0.333D)});
            e.invoke((Object)null, new Object[] {Integer.valueOf(Ic2Items.diamondDrill.itemID), Double.valueOf(0.333D)});
        }
        catch (Throwable var5)
        {
            ;
        }

        try
        {
            Field e1 = Class.forName("mod_Timber").getDeclaredField("axes");
            e1.set((Object)null, e1.get((Object)null) + ", " + Ic2Items.bronzeAxe.itemID + ", " + Ic2Items.chainsaw.itemID);
        }
        catch (Throwable var4)
        {
            ;
        }

        GameRegistry.registerTileEntity(TileEntityBlock.class, "Empty Management TileEntity");
        GameRegistry.registerTileEntity(TileEntityIronFurnace.class, "Iron Furnace");
        GameRegistry.registerTileEntity(TileEntityElectricFurnace.class, "Electric Furnace");
        GameRegistry.registerTileEntity(TileEntityMacerator.class, "Macerator");
        GameRegistry.registerTileEntity(TileEntityExtractor.class, "Extractor");
        GameRegistry.registerTileEntity(TileEntityCompressor.class, "Compressor");
        GameRegistry.registerTileEntity(TileEntityGenerator.class, "Generator");
        GameRegistry.registerTileEntity(TileEntityGeoGenerator.class, "Geothermal Generator");
        GameRegistry.registerTileEntity(TileEntityWaterGenerator.class, "Water Mill");
        GameRegistry.registerTileEntity(TileEntitySolarGenerator.class, "Solar Panel");
        GameRegistry.registerTileEntity(TileEntityWindGenerator.class, "Wind Mill");
        GameRegistry.registerTileEntity(TileEntityCanner.class, "Canning Machine");
        GameRegistry.registerTileEntity(TileEntityMiner.class, "Miner");
        GameRegistry.registerTileEntity(TileEntityPump.class, "Pump");

        if (BlockGenerator.tileEntityNuclearReactorClass == TileEntityNuclearReactorElectric.class)
        {
            GameRegistry.registerTileEntity(TileEntityNuclearReactorElectric.class, "Nuclear Reactor");
        }

        if (BlockReactorChamber.tileEntityReactorChamberClass == TileEntityReactorChamberElectric.class)
        {
            GameRegistry.registerTileEntity(TileEntityReactorChamberElectric.class, "Reactor Chamber");
        }

        GameRegistry.registerTileEntity(TileEntityMagnetizer.class, "Magnetizer");
        GameRegistry.registerTileEntity(TileEntityCable.class, "Cable");
        GameRegistry.registerTileEntity(TileEntityElectricBatBox.class, "BatBox");
        GameRegistry.registerTileEntity(TileEntityElectricMFE.class, "MFE");
        GameRegistry.registerTileEntity(TileEntityElectricMFSU.class, "MFSU");
        GameRegistry.registerTileEntity(TileEntityTransformerLV.class, "LV-Transformer");
        GameRegistry.registerTileEntity(TileEntityTransformerMV.class, "MV-Transformer");
        GameRegistry.registerTileEntity(TileEntityTransformerHV.class, "HV-Transformer");
        GameRegistry.registerTileEntity(TileEntityLuminator.class, "Luminator");
        GameRegistry.registerTileEntity(TileEntityElectrolyzer.class, "Electrolyzer");

        if (BlockPersonal.tileEntityPersonalChestClass == TileEntityPersonalChest.class)
        {
            GameRegistry.registerTileEntity(TileEntityPersonalChest.class, "Personal Safe");
        }

        GameRegistry.registerTileEntity(TileEntityTradeOMat.class, "Trade-O-Mat");
        GameRegistry.registerTileEntity(TileEntityEnergyOMat.class, "Energy-O-Mat");
        GameRegistry.registerTileEntity(TileEntityRecycler.class, "Recycler");
        GameRegistry.registerTileEntity(TileEntityInduction.class, "Induction Furnace");
        GameRegistry.registerTileEntity(TileEntityMatter.class, "Mass Fabricator");
        GameRegistry.registerTileEntity(TileEntityTerra.class, "Terraformer");
        GameRegistry.registerTileEntity(TileEntityTeleporter.class, "Teleporter");
        GameRegistry.registerTileEntity(TileEntityTesla.class, "Tesla Coil");
        GameRegistry.registerTileEntity(TileEntityCableDetector.class, "Detector Cable");
        GameRegistry.registerTileEntity(TileEntityCableSplitter.class, "SplitterCable");
        GameRegistry.registerTileEntity(TileEntityCrop.class, "TECrop");
        GameRegistry.registerTileEntity(TileEntityBarrel.class, "TEBarrel");
        GameRegistry.registerTileEntity(TileEntityCropmatron.class, "Crop-Matron");
        GameRegistry.registerTileEntity(TileEntityWall.class, "CF-Wall");
        platform.onPostInit();
    }

    private static boolean loadSubModule(String name)
    {
        log.info("Loading IC2 submodule: " + name);

        try
        {
            Class t = IC2.class.getClassLoader().loadClass("ic2." + name + ".SubModule");
            return ((Boolean)t.getMethod("init", new Class[0]).invoke((Object)null, new Object[0])).booleanValue();
        }
        catch (Throwable var2)
        {
            log.info("Submodule " + name + " not loaded");
            return false;
        }
    }

    public int getBurnTime(ItemStack stack)
    {
        return Ic2Items.rubberSapling != null && stack.equals(Ic2Items.rubberSapling) ? 80 : (stack.itemID == Item.reed.itemID ? 50 : (stack.itemID == Block.cactus.blockID ? 50 : (stack.itemID == Ic2Items.scrap.itemID ? 350 : (stack.itemID == Ic2Items.scrapBox.itemID ? 3150 : (stack.itemID == Ic2Items.lavaCell.itemID ? TileEntityFurnace.getItemBurnTime(new ItemStack(Item.bucketLava)) : 0)))));
    }

    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        int baseScale;

        if (enableWorldGenTreeRubber)
        {
            BiomeGenBase baseHeight = world.getWorldChunkManager().getBiomeGenAt(chunkX * 16 + 16, chunkZ * 16 + 16);

            if (baseHeight != null && baseHeight.biomeName != null)
            {
                baseScale = 0;

                if (baseHeight.biomeName.toLowerCase().contains("taiga"))
                {
                    baseScale += random.nextInt(3);
                }

                if (baseHeight.biomeName.toLowerCase().contains("forest") || baseHeight.biomeName.toLowerCase().contains("jungle"))
                {
                    baseScale += random.nextInt(5) + 1;
                }

                if (baseHeight.biomeName.toLowerCase().contains("swamp"))
                {
                    baseScale += random.nextInt(10) + 5;
                }

                if (random.nextInt(100) + 1 <= baseScale * 2)
                {
                    (new WorldGenRubTree()).generate(world, random, chunkX * 16 + random.nextInt(16), baseScale, chunkZ * 16 + random.nextInt(16));
                }
            }
        }

        int var15 = getSeaLevel(world) + 1;
        baseScale = Math.round((float)var15 * oreDensityFactor);
        int baseCount;
        int count;
        int n;
        int x;
        int y;
        int z;

        if (enableWorldGenOreCopper && Ic2Items.copperOre != null)
        {
            baseCount = 15 * baseScale / 64;
            count = (int)Math.round(random.nextGaussian() * Math.sqrt((double)baseCount) + (double)baseCount);

            for (n = 0; n < count; ++n)
            {
                x = chunkX * 16 + random.nextInt(16);
                y = random.nextInt(40 * var15 / 64) + random.nextInt(20 * var15 / 64) + 10 * var15 / 64;
                z = chunkZ * 16 + random.nextInt(16);
                (new WorldGenMinable(Ic2Items.copperOre.itemID, 10)).generate(world, random, x, y, z);
            }
        }

        if (enableWorldGenOreTin && Ic2Items.tinOre != null)
        {
            baseCount = 25 * baseScale / 64;
            count = (int)Math.round(random.nextGaussian() * Math.sqrt((double)baseCount) + (double)baseCount);

            for (n = 0; n < count; ++n)
            {
                x = chunkX * 16 + random.nextInt(16);
                y = random.nextInt(40 * var15 / 64);
                z = chunkZ * 16 + random.nextInt(16);
                (new WorldGenMinable(Ic2Items.tinOre.itemID, 6)).generate(world, random, x, y, z);
            }
        }

        if (enableWorldGenOreUranium && Ic2Items.uraniumOre != null)
        {
            baseCount = 20 * baseScale / 64;
            count = (int)Math.round(random.nextGaussian() * Math.sqrt((double)baseCount) + (double)baseCount);

            for (n = 0; n < count; ++n)
            {
                x = chunkX * 16 + random.nextInt(16);
                y = random.nextInt(64 * var15 / 64);
                z = chunkZ * 16 + random.nextInt(16);
                (new WorldGenMinable(Ic2Items.uraniumOre.itemID, 3)).generate(world, random, x, y, z);
            }
        }
    }

    public void tickStart(EnumSet<TickType> type, Object ... tickData)
    {
        if (type.contains(TickType.WORLD))
        {
            platform.profilerStartSection("Init");
            World arr$ = (World)tickData[0];
            platform.profilerEndStartSection("Wind");

            if (windTicker % 128 == 0)
            {
                updateWind(arr$);
            }

            ++windTicker;
            ++textureIndex.t;
            ++ItemNanoSaber.ticker;
            platform.profilerEndStartSection("EnergyNet");
            EnergyNet.onTick(arr$);
            platform.profilerEndStartSection("Networking");
            network.onTick(arr$);
            platform.profilerEndStartSection("TickCallbacks");
            this.processTickCallbacks(arr$);
            platform.profilerEndSection();
        }

        if (type.contains(TickType.WORLDLOAD) && platform.isSimulating())
        {
            Integer[] var24 = DimensionManager.getIDs();
            int len$ = var24.length;

            for (int i$ = 0; i$ < len$; ++i$)
            {
                int worldId = var24[i$].intValue();
                World world = DimensionManager.getProvider(worldId).worldObj;

                if (world != null && world.getSaveHandler() instanceof SaveHandler)
                {
                    SaveHandler saveHandler = (SaveHandler)world.getSaveHandler();
                    File saveFolder = null;
                    Field[] e = SaveHandler.class.getDeclaredFields();
                    int mapIdPropertiesFile = e.length;

                    for (int fileOutputStream = 0; fileOutputStream < mapIdPropertiesFile; ++fileOutputStream)
                    {
                        Field properties = e[fileOutputStream];

                        if (properties.getType() == File.class)
                        {
                            properties.setAccessible(true);

                            try
                            {
                                File outdatedProperties = (File)properties.get(saveHandler);

                                if (saveFolder == null || saveFolder.getParentFile() == outdatedProperties)
                                {
                                    saveFolder = outdatedProperties;
                                }
                            }
                            catch (Exception var23)
                            {
                                ;
                            }
                        }
                    }

                    if (saveFolder != null)
                    {
                        try
                        {
                            IC2$1 var26 = new IC2$1(this);
                            var26.putAll(runtimeIdProperties);
                            File var25 = new File(saveFolder, "ic2_map.cfg");

                            if (var25.exists())
                            {
                                FileInputStream var27 = new FileInputStream(var25);
                                Properties var29 = new Properties();
                                var29.load(var27);
                                var27.close();
                                Vector var30 = new Vector();
                                Iterator i$1 = var29.entrySet().iterator();

                                while (i$1.hasNext())
                                {
                                    Entry key = (Entry)i$1.next();
                                    String key1 = (String)key.getKey();
                                    String value = (String)key.getValue();

                                    if (!runtimeIdProperties.containsKey(key1))
                                    {
                                        var30.add(key1);
                                    }
                                    else
                                    {
                                        int separatorPos = key1.indexOf(46);

                                        if (separatorPos != -1)
                                        {
                                            String section = key1.substring(0, separatorPos);
                                            String entry = key1.substring(separatorPos + 1);

                                            if ((section.equals("block") || section.equals("item")) && !value.equals(runtimeIdProperties.get(key1)))
                                            {
                                                platform.displayError("IC2 detected an ID conflict between your IC2.cfg and the map you are\ntrying to load.\n\nMap: " + saveFolder.getName() + "\n" + "\n" + "Config section: " + section + "\n" + "Config entry: " + entry + "\n" + "Config value: " + runtimeIdProperties.get(key1) + "\n" + "Map value: " + value + "\n" + "\n" + "Adjust your config to match the IDs used by the map or convert your\n" + "map to use the IDs specified in the config.\n" + "\n" + "See also: config/IC2.cfg " + (platform.isRendering() ? "saves/" : "") + saveFolder.getName() + "/ic2_map.cfg");
                                            }
                                        }
                                    }
                                }

                                i$1 = var30.iterator();

                                while (i$1.hasNext())
                                {
                                    String var31 = (String)i$1.next();
                                    var29.remove(var31);
                                }

                                var26.putAll(var29);
                            }

                            FileOutputStream var28 = new FileOutputStream(var25);
                            var26.store(var28, "ic2 map related configuration data");
                            var28.close();
                        }
                        catch (IOException var22)
                        {
                            var22.printStackTrace();
                        }

                        break;
                    }
                }
            }
        }
    }

    public void processTickCallbacks(World world)
    {
        WorldData worldData = WorldData.get(world);
        platform.profilerStartSection("SingleTickCallback");

        for (ITickCallback i$ = (ITickCallback)worldData.singleTickCallbacks.poll(); i$ != null; i$ = (ITickCallback)worldData.singleTickCallbacks.poll())
        {
            platform.profilerStartSection(i$.getClass().getName());
            i$.tickCallback(world);
            platform.profilerEndSection();
        }

        platform.profilerEndStartSection("ContTickCallback");
        worldData.continuousTickCallbacksInUse = true;
        Iterator i$1 = worldData.continuousTickCallbacks.iterator();

        while (i$1.hasNext())
        {
            ITickCallback tickCallback = (ITickCallback)i$1.next();
            platform.profilerStartSection(tickCallback.getClass().getName());
            tickCallback.tickCallback(world);
            platform.profilerEndSection();
        }

        worldData.continuousTickCallbacksInUse = false;
        worldData.continuousTickCallbacks.addAll(worldData.continuousTickCallbacksToAdd);
        worldData.continuousTickCallbacksToAdd.clear();
        worldData.continuousTickCallbacks.removeAll(worldData.continuousTickCallbacksToRemove);
        worldData.continuousTickCallbacksToRemove.clear();
        platform.profilerEndSection();
    }

    public void tickEnd(EnumSet<TickType> type, Object ... tickData) {}

    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.WORLD, TickType.WORLDLOAD);
    }

    public String getLabel()
    {
        return "IC2";
    }

    public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) {}

    public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager)
    {
        return null;
    }

    public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) {}

    public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) {}

    public void connectionClosed(INetworkManager manager) {}

    public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login)
    {
        network.sendLoginData();
    }

    public void onPlayerLogin(EntityPlayer player) {}

    public void onPlayerLogout(EntityPlayer player)
    {
        if (platform.isSimulating())
        {
            ItemArmorQuantumSuit.removePlayerReferences(player);
            keyboard.removePlayerReferences(player);
        }
    }

    public void onPlayerChangedDimension(EntityPlayer player) {}

    public void onPlayerRespawn(EntityPlayer player) {}

    @ForgeSubscribe
    public void onWorldLoad(Load event)
    {
        textureIndex.reset();
    }

    @ForgeSubscribe
    public void onWorldUnload(Unload event)
    {
        WorldData.onWorldUnload(event.world);
    }

    @SideOnly(Side.CLIENT)
    @ForgeSubscribe
    public void onTextureStitchPost(Post event)
    {
        BlockTextureStitched.onPostStitch();
    }

    @ForgeSubscribe
    public void onChunkWatchEvent(Watch event)
    {
        Chunk chunk = event.player.worldObj.getChunkFromChunkCoords(event.chunk.chunkXPos, event.chunk.chunkZPos);
        Iterator i$ = chunk.chunkTileEntityMap.values().iterator();

        while (i$.hasNext())
        {
            TileEntity tileEntity = (TileEntity)i$.next();
            network.sendInitialData(tileEntity, event.player);
        }
    }

    public static void addSingleTickCallback(World world, ITickCallback tickCallback)
    {
        WorldData worldData = WorldData.get(world);
        worldData.singleTickCallbacks.add(tickCallback);
    }

    public static void addContinuousTickCallback(World world, ITickCallback tickCallback)
    {
        WorldData worldData = WorldData.get(world);

        if (!worldData.continuousTickCallbacksInUse)
        {
            worldData.continuousTickCallbacks.add(tickCallback);
        }
        else
        {
            worldData.continuousTickCallbacksToRemove.remove(tickCallback);
            worldData.continuousTickCallbacksToAdd.add(tickCallback);
        }
    }

    public static void removeContinuousTickCallback(World world, ITickCallback tickCallback)
    {
        WorldData worldData = WorldData.get(world);

        if (!worldData.continuousTickCallbacksInUse)
        {
            worldData.continuousTickCallbacks.remove(tickCallback);
        }
        else
        {
            worldData.continuousTickCallbacksToAdd.remove(tickCallback);
            worldData.continuousTickCallbacksToRemove.add(tickCallback);
        }
    }

    public static void updateWind(World world)
    {
        if (world.provider.dimensionId == 0)
        {
            int upChance = 10;
            int downChance = 10;

            if (windStrength > 20)
            {
                upChance -= windStrength - 20;
            }

            if (windStrength < 10)
            {
                downChance -= 10 - windStrength;
            }

            if (random.nextInt(100) <= upChance)
            {
                ++windStrength;
            }
            else if (random.nextInt(100) <= downChance)
            {
                --windStrength;
            }
        }
    }

    public static int getBlockIdFor(Configuration config, InternalName internalName, int standardId)
    {
        String name = internalName.name();
        Property prop = null;
        Integer ret;

        if (config == null)
        {
            ret = Integer.valueOf(standardId);
        }
        else
        {
            prop = config.get("block", name, standardId);
            ret = Integer.valueOf(prop.getInt(standardId));
        }

        if (ret.intValue() <= 0 || ret.intValue() > Block.blocksList.length)
        {
            platform.displayError("An invalid block ID has been detected on your IndustrialCraft 2\nconfiguration file. Block IDs cannot be higher than " + (Block.blocksList.length - 1) + ".\n" + "\n" + "Block with invalid ID: " + name + "\n" + "Invalid ID: " + ret);
        }

        if (Block.blocksList[ret.intValue()] != null || Item.itemsList[ret.intValue()] != null)
        {
            if (enableDynamicIdAllocation)
            {
                boolean occupiedBy = false;

                for (int blockId = Block.blocksList.length - 1; blockId > 0; --blockId)
                {
                    if (!blockedIds.contains(Integer.valueOf(blockId)) && Block.blocksList[blockId] == null && Item.itemsList[blockId] == null)
                    {
                        if (prop != null)
                        {
                            prop.set(blockId);
                        }

                        ret = Integer.valueOf(blockId);
                        occupiedBy = true;
                        break;
                    }
                }

                if (!occupiedBy)
                {
                    platform.displayError("IC2 ran out of block IDs while trying to allocate a new ID.\nTry relocating item IDs to the range above 3840 or remove\n+some mods too free up more IDs.\nBlock with invalid ID: " + name);
                }
            }
            else
            {
                String var8;

                if (Block.blocksList[ret.intValue()] != null)
                {
                    var8 = "block " + Block.blocksList[ret.intValue()] + " (" + Block.blocksList[ret.intValue()].getUnlocalizedName() + ")";
                }
                else
                {
                    var8 = "item " + Item.itemsList[ret.intValue()] + " (" + Item.itemsList[ret.intValue()].getUnlocalizedName() + ")";
                }

                platform.displayError("A conflicting block ID has been detected on your IndustrialCraft 2\nconfiguration file. Block IDs cannot be used more than once.\n\nBlock with invalid ID: " + name + "\n" + "Invalid ID: " + ret + "\n" + "Already occupied by: " + var8);
            }
        }

        runtimeIdProperties.setProperty("block." + name, ret.toString());
        return ret.intValue();
    }

    public static int getItemIdFor(Configuration config, InternalName internalName, int standardId)
    {
        String name = internalName.name();
        Property prop = null;
        Integer ret;

        if (config == null)
        {
            ret = Integer.valueOf(standardId);
        }
        else
        {
            prop = config.get("item", name, standardId);
            ret = Integer.valueOf(prop.getInt(standardId));
        }

        if (ret.intValue() < 256 || ret.intValue() > Item.itemsList.length - 256)
        {
            platform.displayError("An invalid item ID has been detected on your IndustrialCraft 2\nconfiguration file. Item IDs cannot be lower than 256 or\nhigher than " + (Item.itemsList.length - 1 - 256) + ".\n" + "\n" + "Item with invalid ID: " + name + "\n" + "Invalid ID: " + ret + " (" + (ret.intValue() + 256) + " shifted)");
        }

        if (ret.intValue() + 256 < Block.blocksList.length && Block.blocksList[ret.intValue() + 256] != null || Item.itemsList[ret.intValue() + 256] != null)
        {
            if (enableDynamicIdAllocation)
            {
                boolean occupiedBy = false;

                for (int itemId = Item.itemsList.length - 1 - 256; itemId >= 256; --itemId)
                {
                    if (!blockedIds.contains(Integer.valueOf(itemId + 256)) && (itemId >= Block.blocksList.length - 256 || Block.blocksList[itemId + 256] == null) && Item.itemsList[itemId + 256] == null)
                    {
                        if (prop != null)
                        {
                            prop.set(itemId);
                        }

                        ret = Integer.valueOf(itemId);
                        occupiedBy = true;
                        break;
                    }
                }

                if (!occupiedBy)
                {
                    platform.displayError("IC2 ran out of item IDs while trying to allocate a new ID.\nTry removing some mods too free up more IDs.\nItem with invalid ID: " + name);
                }
            }
            else
            {
                String var8;

                if (ret.intValue() + 256 < Block.blocksList.length && Block.blocksList[ret.intValue() + 256] != null)
                {
                    var8 = "block " + Block.blocksList[ret.intValue() + 256] + " (" + Block.blocksList[ret.intValue() + 256].getUnlocalizedName() + ")";
                }
                else
                {
                    var8 = "item " + Item.itemsList[ret.intValue() + 256] + " (" + Item.itemsList[ret.intValue() + 256].getUnlocalizedName() + ")";
                }

                platform.displayError("A conflicting item ID has been detected on your IndustrialCraft 2\nconfiguration file. Item IDs cannot be used for more than once.\n\nItem with invalid ID: " + name + "\n" + "Invalid ID: " + ret + " (" + (ret.intValue() + 256) + " shifted)\n" + "Already occupied by: " + var8);
            }
        }

        runtimeIdProperties.setProperty("item." + name, ret.toString());
        return ret.intValue();
    }

    public static void explodeMachineAt(World world, int x, int y, int z)
    {
        world.setBlock(x, y, z, 0, 0, 7);
        ExplosionIC2 explosion = new ExplosionIC2(world, (Entity)null, 0.5D + (double)x, 0.5D + (double)y, 0.5D + (double)z, 2.5F, 0.75F, 0.75F);
        explosion.doExplosion();
    }

    public static int getSeaLevel(World world)
    {
        return world.provider.getAverageGroundLevel();
    }

    public static int getWorldHeight(World world)
    {
        return world.getHeight();
    }

    public static void addValuableOre(int blockId, int value)
    {
        addValuableOre(blockId, 32767, value);
    }

    public static void addValuableOre(int blockId, int metaData, int value)
    {
        if (valuableOres.containsKey(Integer.valueOf(blockId)))
        {
            Map metaMap = (Map)valuableOres.get(Integer.valueOf(blockId));

            if (metaMap.containsKey(Integer.valueOf(32767)))
            {
                return;
            }

            if (metaData == 32767)
            {
                metaMap.clear();
                metaMap.put(Integer.valueOf(32767), Integer.valueOf(value));
            }
            else if (!metaMap.containsKey(Integer.valueOf(metaData)))
            {
                metaMap.put(Integer.valueOf(metaData), Integer.valueOf(value));
            }
        }
        else
        {
            TreeMap metaMap1 = new TreeMap();
            metaMap1.put(Integer.valueOf(metaData), Integer.valueOf(value));
            valuableOres.put(Integer.valueOf(blockId), metaMap1);
        }
    }

    private static String getValuableOreString()
    {
        StringBuilder ret = new StringBuilder();
        boolean first = true;
        Iterator i$ = valuableOres.entrySet().iterator();

        while (i$.hasNext())
        {
            Entry entry = (Entry)i$.next();
            Iterator i$1 = ((Map)entry.getValue()).entrySet().iterator();

            while (i$1.hasNext())
            {
                Entry entry2 = (Entry)i$1.next();

                if (first)
                {
                    first = false;
                }
                else
                {
                    ret.append(", ");
                }

                ret.append(entry.getKey());

                if (((Integer)entry2.getKey()).intValue() != 32767)
                {
                    ret.append("-");
                    ret.append(entry2.getKey());
                }

                ret.append(":");
                ret.append(entry2.getValue());
            }
        }

        return ret.toString();
    }

    private static void setValuableOreFromString(String str)
    {
        valuableOres.clear();
        String[] strParts = str.trim().split("\\s*,\\s*");
        String[] arr$ = strParts;
        int len$ = strParts.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            String strPart = arr$[i$];
            String[] idMetaValue = strPart.split("\\s*:\\s*");
            String[] idMeta = idMetaValue[0].split("\\s*-\\s*");

            if (idMeta[0].length() != 0)
            {
                int blockId = Integer.parseInt(idMeta[0]);
                int metaData = 32767;
                int value = 1;

                if (idMeta.length == 2)
                {
                    metaData = Integer.parseInt(idMeta[1]);
                }

                if (idMetaValue.length == 2)
                {
                    value = Integer.parseInt(idMetaValue[1]);
                }

                addValuableOre(blockId, metaData, value);
            }
        }
    }

    @ForgeSubscribe
    public void registerOre(OreRegisterEvent event)
    {
        String oreClass = event.Name;
        ItemStack ore = event.Ore;

        if (oreClass.equals("ingotCopper"))
        {
            Recipes.macerator.addRecipe(ore, Ic2Items.copperDust);
            Recipes.compressor.addRecipe(StackUtil.copyWithSize(ore, 8), Ic2Items.denseCopperPlate);
        }
        else if (oreClass.equals("ingotRefinedIron"))
        {
            Recipes.macerator.addRecipe(ore, Ic2Items.ironDust);
        }
        else if (oreClass.equals("ingotSilver"))
        {
            Recipes.macerator.addRecipe(ore, Ic2Items.silverDust);

            if (!silverDustSmeltingRegistered)
            {
                FurnaceRecipes.smelting().addSmelting(Ic2Items.silverDust.itemID, Ic2Items.silverDust.getItemDamage(), ore, 0.8F);
                silverDustSmeltingRegistered = true;
            }
        }
        else if (oreClass.equals("ingotTin"))
        {
            Recipes.macerator.addRecipe(ore, Ic2Items.tinDust);
        }
        else if (oreClass.equals("dropUranium"))
        {
            Recipes.compressor.addRecipe(ore, Ic2Items.uraniumIngot);
        }
        else if (oreClass.equals("oreCopper"))
        {
            Recipes.macerator.addRecipe(ore, StackUtil.copyWithSize(Ic2Items.copperDust, 2));
            addValuableOre(ore.itemID, ore.getItemDamage(), 2);
        }
        else if (!oreClass.equals("oreGemRuby") && !oreClass.equals("oreGemGreenSapphire") && !oreClass.equals("oreGemSapphire"))
        {
            if (oreClass.equals("oreSilver"))
            {
                Recipes.macerator.addRecipe(ore, StackUtil.copyWithSize(Ic2Items.silverDust, 2));
                addValuableOre(ore.itemID, ore.getItemDamage(), 3);
            }
            else if (oreClass.equals("oreTin"))
            {
                Recipes.macerator.addRecipe(ore, StackUtil.copyWithSize(Ic2Items.tinDust, 2));
                addValuableOre(ore.itemID, ore.getItemDamage(), 2);
            }
            else if (oreClass.equals("oreUranium"))
            {
                Recipes.compressor.addRecipe(ore, Ic2Items.uraniumIngot);
                addValuableOre(ore.itemID, ore.getItemDamage(), 4);
            }
            else if (oreClass.equals("oreTungsten"))
            {
                addValuableOre(ore.itemID, ore.getItemDamage(), 5);
            }
            else if (oreClass.equals("woodRubber"))
            {
                Recipes.extractor.addRecipe(ore, Ic2Items.rubber);
            }
            else if (oreClass.startsWith("ore"))
            {
                addValuableOre(ore.itemID, ore.getItemDamage(), 1);
            }
        }
        else
        {
            addValuableOre(ore.itemID, ore.getItemDamage(), 4);
        }
    }

    @ForgeSubscribe
    public void onLivingSpecialSpawn(SpecialSpawn event)
    {
        if (seasonal && (event.entityLiving instanceof EntityZombie || event.entityLiving instanceof EntitySkeleton) && event.entityLiving.worldObj.rand.nextFloat() < 0.1F)
        {
            EntityLiving entity = (EntityLiving)event.entityLiving;

            for (int i = 0; i <= 4; ++i)
            {
                entity.setEquipmentDropChance(i, Float.NEGATIVE_INFINITY);
            }

            if (entity instanceof EntityZombie)
            {
                entity.setCurrentItemOrArmor(0, Ic2Items.nanoSaber.copy());
            }

            if (event.entityLiving.worldObj.rand.nextFloat() < 0.1F)
            {
                entity.setCurrentItemOrArmor(1, Ic2Items.quantumHelmet.copy());
                entity.setCurrentItemOrArmor(2, Ic2Items.quantumBodyarmor.copy());
                entity.setCurrentItemOrArmor(3, Ic2Items.quantumLeggings.copy());
                entity.setCurrentItemOrArmor(4, Ic2Items.quantumBoots.copy());
            }
            else
            {
                entity.setCurrentItemOrArmor(1, Ic2Items.nanoHelmet.copy());
                entity.setCurrentItemOrArmor(2, Ic2Items.nanoBodyarmor.copy());
                entity.setCurrentItemOrArmor(3, Ic2Items.nanoLeggings.copy());
                entity.setCurrentItemOrArmor(4, Ic2Items.nanoBoots.copy());
            }
        }
    }

    static
    {
        addValuableOre(Block.oreCoal.blockID, 1);
        addValuableOre(Block.oreGold.blockID, 3);
        addValuableOre(Block.oreRedstone.blockID, 3);
        addValuableOre(Block.oreLapis.blockID, 3);
        addValuableOre(Block.oreIron.blockID, 4);
        addValuableOre(Block.oreDiamond.blockID, 5);
        addValuableOre(Block.oreEmerald.blockID, 5);
        enableCraftingBucket = true;
        enableCraftingCoin = true;
        enableCraftingGlowstoneDust = true;
        enableCraftingGunpowder = true;
        enableCraftingITnt = true;
        enableCraftingNuke = true;
        enableCraftingRail = true;
        enableDynamicIdAllocation = true;
        enableLoggingWrench = true;
        enableSecretRecipeHiding = true;
        enableQuantumSpeedOnSprint = true;
        enableMinerLapotron = false;
        enableTeleporterInventory = true;
        enableBurningScrap = true;
        enableWorldGenTreeRubber = true;
        enableWorldGenOreCopper = true;
        enableWorldGenOreTin = true;
        enableWorldGenOreUranium = true;
        explosionPowerNuke = 35.0F;
        explosionPowerReactorMax = 45.0F;
        energyGeneratorBase = 10;
        energyGeneratorGeo = 20;
        energyGeneratorWater = 100;
        energyGeneratorSolar = 100;
        energyGeneratorWind = 100;
        energyGeneratorNuclear = 5;
        suddenlyHoes = false;
        seasonal = false;
        enableSteamReactor = false;
        oreDensityFactor = 1.0F;
        initialized = false;
        runtimeIdProperties = new Properties();
        tabIC2 = new CreativeTabIC2();
        silverDustSmeltingRegistered = false;
        blockedIds = new HashSet();
    }
}
