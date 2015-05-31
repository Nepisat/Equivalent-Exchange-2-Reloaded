package ic2.core.block.crop;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.crops.BaseSeed;
import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.init.Localization;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;

public class IC2Crops extends Crops
{
    private final Map<BiomeGenBase, Integer> humidityBiomeBonus = new HashMap();
    private final Map<BiomeGenBase, Integer> nutrientBiomeBonus = new HashMap();
    private final Map<ItemStack, BaseSeed> baseSeeds = new HashMap();
    private final CropCard[] crops = new CropCard[256];
    public static CropCard weed = new CropWeed();
    public static CropCard cropWheat = new CropWheat();
    public static CropCard cropPumpkin = new CropPumpkin();
    public static CropCard cropMelon = new CropMelon();
    public static CropCard cropYellowFlower = new CropColorFlower("Dandelion", new String[] {"Yellow", "Flower"}, 11);
    public static CropCard cropRedFlower = new CropColorFlower("Rose", new String[] {"Red", "Flower", "Rose"}, 1);
    public static CropCard cropBlackFlower = new CropColorFlower("Blackthorn", new String[] {"Black", "Flower", "Rose"}, 0);
    public static CropCard cropPurpleFlower = new CropColorFlower("Tulip", new String[] {"Purple", "Flower", "Tulip"}, 5);
    public static CropCard cropBlueFlower = new CropColorFlower("Cyazint", new String[] {"Blue", "Flower"}, 6);
    public static CropCard cropVenomilia = new CropVenomilia();
    public static CropCard cropReed = new CropReed();
    public static CropCard cropStickReed = new CropStickReed();
    public static CropCard cropCocoa = new CropCocoa();
    public static CropCard cropFerru = new CropFerru();
    public static CropCard cropAurelia = new CropAurelia();
    public static CropCard cropRedwheat = new CropRedWheat();
    public static CropCard cropNetherWart = new CropNetherWart();
    public static CropCard cropTerraWart = new CropTerraWart();
    public static CropCard cropCoffee = new CropCoffee();
    public static CropCard cropHops = new CropHops();
    public static CropCard cropCarrots = new CropSeedFood("Carrots", "Orange", new ItemStack(Item.carrot));
    public static CropCard cropPotato = new CropPotato();

    public static void init()
    {
        Crops.instance = new IC2Crops();
        registerCrops();
        registerBaseSeeds();
    }

    public static void registerCrops()
    {
        if (!Crops.instance.registerCrop(weed, 0) || !Crops.instance.registerCrop(cropWheat, 1) || !Crops.instance.registerCrop(cropPumpkin, 2) || !Crops.instance.registerCrop(cropMelon, 3) || !Crops.instance.registerCrop(cropYellowFlower, 4) || !Crops.instance.registerCrop(cropRedFlower, 5) || !Crops.instance.registerCrop(cropBlackFlower, 6) || !Crops.instance.registerCrop(cropPurpleFlower, 7) || !Crops.instance.registerCrop(cropBlueFlower, 8) || !Crops.instance.registerCrop(cropVenomilia, 9) || !Crops.instance.registerCrop(cropReed, 10) || !Crops.instance.registerCrop(cropStickReed, 11) || !Crops.instance.registerCrop(cropCocoa, 12) || !Crops.instance.registerCrop(cropFerru, 13) || !Crops.instance.registerCrop(cropAurelia, 14) || !Crops.instance.registerCrop(cropRedwheat, 15) || !Crops.instance.registerCrop(cropNetherWart, 16) || !Crops.instance.registerCrop(cropTerraWart, 17) || !Crops.instance.registerCrop(cropCoffee, 18) || !Crops.instance.registerCrop(cropHops, 19) || !Crops.instance.registerCrop(cropCarrots, 20) || !Crops.instance.registerCrop(cropPotato, 21))
        {
            IC2.platform.displayError("One or more crops have failed to initialize.\nThis could happen due to a crop addon using a crop ID already taken\nby a crop from IndustrialCraft 2.");
        }
    }

    public static void registerBaseSeeds()
    {
        Crops.instance.registerBaseSeed(new ItemStack(Item.seeds.itemID, 1, 32767), cropWheat.getId(), 1, 1, 1, 1);
        Crops.instance.registerBaseSeed(new ItemStack(Item.pumpkinSeeds.itemID, 1, 32767), cropPumpkin.getId(), 1, 1, 1, 1);
        Crops.instance.registerBaseSeed(new ItemStack(Item.melonSeeds.itemID, 1, 32767), cropMelon.getId(), 1, 1, 1, 1);
        Crops.instance.registerBaseSeed(new ItemStack(Item.netherStalkSeeds.itemID, 1, 32767), cropNetherWart.getId(), 1, 1, 1, 1);
        Crops.instance.registerBaseSeed(new ItemStack(Ic2Items.terraWart.itemID, 1, 32767), cropTerraWart.getId(), 1, 1, 1, 1);
        Crops.instance.registerBaseSeed(new ItemStack(Ic2Items.coffeeBeans.itemID, 1, 32767), cropCoffee.getId(), 1, 1, 1, 1);
        Crops.instance.registerBaseSeed(new ItemStack(Item.reed.itemID, 1, 32767), cropReed.getId(), 1, 3, 0, 2);
        Crops.instance.registerBaseSeed(new ItemStack(Item.dyePowder.itemID, 1, 3), cropCocoa.getId(), 1, 0, 0, 0);
        Crops.instance.registerBaseSeed(new ItemStack(Block.plantRed.blockID, 4, 32767), cropRedFlower.getId(), 4, 1, 1, 1);
        Crops.instance.registerBaseSeed(new ItemStack(Block.plantYellow.blockID, 4, 32767), cropYellowFlower.getId(), 4, 1, 1, 1);
        Crops.instance.registerBaseSeed(new ItemStack(Item.carrot, 1, 32767), cropCarrots.getId(), 1, 1, 1, 1);
        Crops.instance.registerBaseSeed(new ItemStack(Item.potato, 1, 32767), cropPotato.getId(), 1, 1, 1, 1);
    }

    public void addBiomeBonus(BiomeGenBase biome, int humidityBonus, int nutrientsBonus)
    {
        this.humidityBiomeBonus.put(biome, Integer.valueOf(humidityBonus));
        this.nutrientBiomeBonus.put(biome, Integer.valueOf(nutrientsBonus));
    }

    public int getHumidityBiomeBonus(BiomeGenBase biome)
    {
        return this.humidityBiomeBonus.containsKey(biome) ? ((Integer)this.humidityBiomeBonus.get(biome)).intValue() : 0;
    }

    public int getNutrientBiomeBonus(BiomeGenBase biome)
    {
        return this.nutrientBiomeBonus.containsKey(biome) ? ((Integer)this.nutrientBiomeBonus.get(biome)).intValue() : 0;
    }

    public CropCard[] getCropList()
    {
        return this.crops;
    }

    public short registerCrop(CropCard crop)
    {
        for (short x = 0; x < this.crops.length; ++x)
        {
            if (this.crops[x] == null)
            {
                this.registerCrop(crop, x);
                return x;
            }
        }

        return (short) - 1;
    }

    public boolean registerCrop(CropCard crop, int i)
    {
        if (i >= 0 && i < this.crops.length)
        {
            if (this.crops[i] == null)
            {
                this.crops[i] = crop;
                Localization.addLocalization("ic2.cropSeed" + i, crop.name() + " Seeds");
                return true;
            }
            else
            {
                System.out.println("[IndustrialCraft2] Cannot add crop:" + crop.name() + " on ID #" + i + ", slot already occupied by crop:" + this.crops[i].name());
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean registerBaseSeed(ItemStack stack, int id, int size, int growth, int gain, int resistance)
    {
        Iterator i$ = this.baseSeeds.keySet().iterator();
        ItemStack key;

        do
        {
            if (!i$.hasNext())
            {
                this.baseSeeds.put(stack, new BaseSeed(id, size, growth, gain, resistance, stack.stackSize));
                return true;
            }

            key = (ItemStack)i$.next();
        }
        while (key.itemID != stack.itemID || key.getItemDamage() != stack.getItemDamage());

        return false;
    }

    public BaseSeed getBaseSeed(ItemStack stack)
    {
        if (stack == null)
        {
            return null;
        }
        else
        {
            Iterator i$ = this.baseSeeds.entrySet().iterator();
            ItemStack key;

            do
            {
                do
                {
                    if (!i$.hasNext())
                    {
                        return null;
                    }

                    Entry entry = (Entry)i$.next();
                    key = (ItemStack)entry.getKey();
                }
                while (key.itemID != stack.itemID);
            }
            while (key.getItemDamage() != 32767 && key.getItemDamage() != stack.getItemDamage());

            return (BaseSeed)this.baseSeeds.get(key);
        }
    }

    @SideOnly(Side.CLIENT)
    public void startSpriteRegistration(IconRegister iconRegister)
    {
        CropCard[] arr$ = this.crops;
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            CropCard cropCard = arr$[i$];

            if (cropCard != null)
            {
                cropCard.registerSprites(iconRegister);
            }
        }
    }

    public int getIdFor(CropCard crop)
    {
        for (int i = 0; i < this.crops.length; ++i)
        {
            if (this.crops[i] == crop)
            {
                return i;
            }
        }

        return -1;
    }
}
