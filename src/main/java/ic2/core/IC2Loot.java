package ic2.core;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

public class IC2Loot
{
    private static final WeightedRandomChestContent[] MINESHAFT_CORRIDOR = new WeightedRandomChestContent[] {new WeightedRandomChestContent(Ic2Items.uraniumDrop.copy(), 1, 2, 1), new WeightedRandomChestContent(Ic2Items.bronzePickaxe.copy(), 1, 1, 1), new WeightedRandomChestContent(Ic2Items.filledTinCan.copy(), 4, 16, 8)};
    private static final WeightedRandomChestContent[] STRONGHOLD_CORRIDOR = new WeightedRandomChestContent[] {new WeightedRandomChestContent(Ic2Items.matter.copy(), 1, 4, 1)};
    private static final WeightedRandomChestContent[] STRONGHOLD_CROSSING = new WeightedRandomChestContent[] {new WeightedRandomChestContent(Ic2Items.bronzePickaxe.copy(), 1, 1, 1)};
    private static final WeightedRandomChestContent[] VILLAGE_BLACKSMITH = new WeightedRandomChestContent[] {new WeightedRandomChestContent(Ic2Items.bronzeIngot.copy(), 2, 4, 5)};
    private static final WeightedRandomChestContent[] BONUS_CHEST = new WeightedRandomChestContent[] {new WeightedRandomChestContent(Ic2Items.treetap.copy(), 1, 1, 2)};
    private static final WeightedRandomChestContent[] DUNGEON_CHEST = new WeightedRandomChestContent[] {new WeightedRandomChestContent(Ic2Items.copperIngot.copy(), 2, 5, 100), new WeightedRandomChestContent(Ic2Items.tinIngot.copy(), 2, 5, 100), new WeightedRandomChestContent(new ItemStack(Item.recordBlocks), 1, 1, 5), new WeightedRandomChestContent(new ItemStack(Item.recordChirp), 1, 1, 5), new WeightedRandomChestContent(new ItemStack(Item.recordFar), 1, 1, 5), new WeightedRandomChestContent(new ItemStack(Item.recordMall), 1, 1, 5), new WeightedRandomChestContent(new ItemStack(Item.recordMellohi), 1, 1, 5), new WeightedRandomChestContent(new ItemStack(Item.recordStal), 1, 1, 5), new WeightedRandomChestContent(new ItemStack(Item.recordStrad), 1, 1, 5), new WeightedRandomChestContent(new ItemStack(Item.recordWard), 1, 1, 5), new WeightedRandomChestContent(new ItemStack(Item.record11), 1, 1, 5), new WeightedRandomChestContent(new ItemStack(Item.recordWait), 1, 1, 5)};
    private static final WeightedRandomChestContent[] bronzeToolsArmor = new WeightedRandomChestContent[] {new WeightedRandomChestContent(Ic2Items.bronzePickaxe.copy(), 1, 1, 3), new WeightedRandomChestContent(Ic2Items.bronzeSword.copy(), 1, 1, 3), new WeightedRandomChestContent(Ic2Items.bronzeHelmet.copy(), 1, 1, 3), new WeightedRandomChestContent(Ic2Items.bronzeChestplate.copy(), 1, 1, 3), new WeightedRandomChestContent(Ic2Items.bronzeLeggings.copy(), 1, 1, 3), new WeightedRandomChestContent(Ic2Items.bronzeBoots.copy(), 1, 1, 3)};
    private static final WeightedRandomChestContent[] ingots = new WeightedRandomChestContent[] {new WeightedRandomChestContent(Ic2Items.copperIngot.copy(), 2, 6, 9), new WeightedRandomChestContent(Ic2Items.tinIngot.copy(), 1, 5, 8)};
    private static WeightedRandomChestContent[] rubberSapling = new WeightedRandomChestContent[0];

    public IC2Loot()
    {
        if (Ic2Items.rubberSapling != null)
        {
            rubberSapling = new WeightedRandomChestContent[] {new WeightedRandomChestContent(Ic2Items.rubberSapling.copy(), 1, 4, 4)};
        }

        this.addLoot("mineshaftCorridor", new WeightedRandomChestContent[][] {MINESHAFT_CORRIDOR, ingots});
        this.addLoot("pyramidDesertyChest", new WeightedRandomChestContent[][] {bronzeToolsArmor, ingots});
        this.addLoot("pyramidJungleChest", new WeightedRandomChestContent[][] {bronzeToolsArmor, ingots});
        this.addLoot("strongholdCorridor", new WeightedRandomChestContent[][] {STRONGHOLD_CORRIDOR, bronzeToolsArmor, ingots});
        this.addLoot("strongholdCrossing", new WeightedRandomChestContent[][] {STRONGHOLD_CROSSING, bronzeToolsArmor, ingots});
        this.addLoot("villageBlacksmith", new WeightedRandomChestContent[][] {VILLAGE_BLACKSMITH, bronzeToolsArmor, ingots, rubberSapling});
        this.addLoot("bonusChest", new WeightedRandomChestContent[][] {BONUS_CHEST});
        this.addLoot("dungeonChest", new WeightedRandomChestContent[][] {DUNGEON_CHEST});
    }

    private void addLoot(String category, WeightedRandomChestContent[] ... loot)
    {
        ChestGenHooks cgh = ChestGenHooks.getInfo(category);
        WeightedRandomChestContent[][] arr$ = loot;
        int len$ = loot.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            WeightedRandomChestContent[] lootList = arr$[i$];
            WeightedRandomChestContent[] arr$1 = lootList;
            int len$1 = lootList.length;

            for (int i$1 = 0; i$1 < len$1; ++i$1)
            {
                WeightedRandomChestContent lootEntry = arr$1[i$1];
                cgh.addItem(lootEntry);
            }
        }
    }
}
