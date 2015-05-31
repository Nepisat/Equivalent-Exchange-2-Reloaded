package ic2.core.item;

import ic2.api.recipe.Recipes;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.init.InternalName;
import ic2.core.item.ItemScrapbox$Drop;
import ic2.core.item.ItemScrapbox$ScrapboxRecipeManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.OreDictionary;

public class ItemScrapbox extends ItemIC2
{
    public ItemScrapbox(Configuration config, InternalName internalName)
    {
        super(config, internalName);
        BlockDispenser.dispenseBehaviorRegistry.putObject(this, new BehaviorScrapboxDispense());
    }

    public static void init()
    {
        Recipes.scrapboxDrops = new ItemScrapbox$ScrapboxRecipeManager();

        if (IC2.suddenlyHoes)
        {
            addDrop(Item.hoeWood, 9001.0F);
        }
        else
        {
            addDrop(Item.hoeWood, 5.01F);
        }

        addDrop(Block.dirt, 5.0F);
        addDrop(Item.stick, 4.0F);
        addDrop((Block)Block.grass, 3.0F);
        addDrop(Block.gravel, 3.0F);
        addDrop(Block.netherrack, 2.0F);
        addDrop(Item.rottenFlesh, 2.0F);
        addDrop(Item.appleRed, 1.5F);
        addDrop(Item.bread, 1.5F);
        addDrop(Ic2Items.filledTinCan.getItem(), 1.5F);
        addDrop(Item.swordWood);
        addDrop(Item.shovelWood);
        addDrop(Item.pickaxeWood);
        addDrop(Block.slowSand);
        addDrop(Item.sign);
        addDrop(Item.leather);
        addDrop(Item.feather);
        addDrop(Item.bone);
        addDrop(Item.porkCooked, 0.9F);
        addDrop(Item.beefCooked, 0.9F);
        addDrop(Block.pumpkin, 0.9F);
        addDrop(Item.chickenCooked, 0.9F);
        addDrop(Item.minecartEmpty, 0.9F);
        addDrop(Item.redstone, 0.9F);
        addDrop(Ic2Items.rubber.getItem(), 0.8F);
        addDrop(Item.glowstone, 0.8F);
        addDrop(Ic2Items.coalDust.getItem(), 0.8F);
        addDrop(Ic2Items.copperDust.getItem(), 0.8F);
        addDrop(Ic2Items.tinDust.getItem(), 0.8F);
        addDrop(Ic2Items.plantBall.getItem(), 0.7F);
        addDrop(Ic2Items.suBattery.getItem(), 0.7F);
        addDrop(Ic2Items.ironDust.getItem(), 0.7F);
        addDrop(Ic2Items.goldDust.getItem(), 0.7F);
        addDrop(Item.slimeBall, 0.6F);
        addDrop(Block.oreIron, 0.5F);
        addDrop((Item)Item.helmetGold, 0.5F);
        addDrop(Block.oreGold, 0.5F);
        addDrop(Item.cake, 0.5F);
        addDrop(Item.diamond, 0.1F);
        addDrop(Item.emerald, 0.05F);
        ArrayList ores;

        if (Ic2Items.copperOre != null)
        {
            addDrop(Ic2Items.copperOre.getItem(), 0.7F);
        }
        else
        {
            ores = OreDictionary.getOres("oreCopper");

            if (!ores.isEmpty())
            {
                addDrop(((ItemStack)ores.get(0)).copy(), 0.7F);
            }
        }

        if (Ic2Items.tinOre != null)
        {
            addDrop(Ic2Items.tinOre.getItem(), 0.7F);
        }
        else
        {
            ores = OreDictionary.getOres("oreTin");

            if (!ores.isEmpty())
            {
                addDrop(((ItemStack)ores.get(0)).copy(), 0.7F);
            }
        }
    }

    public static void addDrop(Item item)
    {
        addDrop(new ItemStack(item), 1.0F);
    }

    public static void addDrop(Item item, float chance)
    {
        addDrop(new ItemStack(item), chance);
    }

    public static void addDrop(Block block)
    {
        addDrop(new ItemStack(block), 1.0F);
    }

    public static void addDrop(Block block, float chance)
    {
        addDrop(new ItemStack(block), chance);
    }

    public static void addDrop(ItemStack item)
    {
        addDrop(item, 1.0F);
    }

    public static void addDrop(ItemStack item, float chance)
    {
        Recipes.scrapboxDrops.addRecipe(item, Float.valueOf(chance));
    }

    public static ItemStack getDrop(World world)
    {
        Map dropList = ((ItemScrapbox$ScrapboxRecipeManager)Recipes.scrapboxDrops).getDrops();

        if (!dropList.isEmpty())
        {
            float dropChance = world.rand.nextFloat() * ItemScrapbox$Drop.topChance;
            Iterator i$ = dropList.entrySet().iterator();

            while (i$.hasNext())
            {
                Entry entry = (Entry)i$.next();
                ItemScrapbox$Drop drop = (ItemScrapbox$Drop)entry.getValue();

                if (drop.upperChanceBound > dropChance && drop.upperChanceBound - drop.originalChance <= dropChance)
                {
                    return ((ItemStack)entry.getKey()).copy();
                }
            }
        }

        return null;
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (!entityplayer.capabilities.isCreativeMode)
        {
            --itemstack.stackSize;
        }

        ItemStack itemStack = getDrop(world);

        if (itemStack != null)
        {
            entityplayer.dropPlayerItem(itemStack);
        }

        return itemstack;
    }
}
