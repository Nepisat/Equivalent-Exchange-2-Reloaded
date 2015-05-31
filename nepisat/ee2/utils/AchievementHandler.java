package nepisat.ee2.utils;


import java.util.ArrayList;
import java.util.List;

import nepisat.ee2.gameObjs.ObjHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.AchievementPage;


public final class AchievementHandler
{
	private static List<Achievement> list;
	
	public final static Achievement PHIL_STONE = (Achievement) new Achievement(2001, "phil_stone", 0, 2, ObjHandler.philosStone, null).initIndependentStat().registerStat();
	public final static Achievement ALCH_CHEST = (Achievement) new Achievement(2002, "alch_chest", 0, -2, ObjHandler.alchChest, null).initIndependentStat().registerStat();
	public final static Achievement ALCH_BAG = (Achievement) new Achievement(2003, "alch_bag", 0, -4, ObjHandler.alchBag, ALCH_CHEST).registerStat();
	public final static Achievement TRANSMUTATION = (Achievement) new Achievement(2004, "transmutation", 0, 0, ObjHandler.transmuteStone, PHIL_STONE).registerStat();
	public final static Achievement CONDENSER = (Achievement) new Achievement(2005, "condenser", -2, -2, ObjHandler.condenser, ALCH_CHEST).setSpecial().registerStat();
	public final static Achievement COLLECTOR = (Achievement) new Achievement(2006, "collector", -2, -4, ObjHandler.energyCollector, CONDENSER).setSpecial().registerStat();
	public final static Achievement RELAY = (Achievement) new Achievement(2007, "relay", -4, -4, ObjHandler.relay, COLLECTOR).setSpecial().registerStat();
	public final static Achievement PORTABLE_TRANSMUTATION = (Achievement) new Achievement(2008, "portable_transmutation", -2, 0, ObjHandler.transmutationTablet, TRANSMUTATION).setSpecial().registerStat();
	public final static Achievement DARK_MATTER = (Achievement) new Achievement(2009, "dark_matter", 2, 0, new ItemStack(ObjHandler.matter, 1, 0), null).initIndependentStat().registerStat();
	public final static Achievement RED_MATTER = (Achievement) new Achievement(2010, "red_matter", 2, -2, new ItemStack(ObjHandler.matter, 1, 1), DARK_MATTER).setSpecial().registerStat();
	public final static Achievement DM_BLOCK = (Achievement) new Achievement(2011, "dm_block", 4, 0, new ItemStack(ObjHandler.matterBlock, 1, 0), DARK_MATTER).setSpecial().registerStat();
	public final static Achievement RM_BLOCK = (Achievement) new Achievement(2012, "rm_block", 4, -2, new ItemStack(ObjHandler.matterBlock, 1, 1), RED_MATTER).setSpecial().registerStat();
	public final static Achievement DM_FURNACE = (Achievement) new Achievement(2013, "dm_furnace", 6, 0, ObjHandler.dmFurnaceOff, DM_BLOCK).setSpecial().registerStat();
	public final static Achievement RM_FURNACE = (Achievement) new Achievement(2014, "rm_furnace", 6, -2, ObjHandler.rmFurnaceOff, RM_BLOCK).setSpecial().registerStat();
	public final static Achievement DM_PICK = (Achievement) new Achievement(2015, "dm_pick", 2, 2, ObjHandler.dmPick, DARK_MATTER).registerStat();
	public final static Achievement RM_PICK = (Achievement) new Achievement(2016, "rm_pick", 2, 4, ObjHandler.rmPick, DM_PICK).setSpecial().registerStat();
	public final static Achievement KLEIN_BASIC = (Achievement) new Achievement(2017, "klein", 0, 4, new ItemStack(ObjHandler.kleinStars, 1, 0), PHIL_STONE).registerStat();
	public final static Achievement KLEIN_MASTER = (Achievement) new Achievement(2018, "klein_big", -2, 4, new ItemStack(ObjHandler.kleinStars, 1, 5), KLEIN_BASIC).setSpecial().registerStat();
	public final static Achievement TOME = (Achievement) new Achievement(2019, "tome", -4, 2, ObjHandler.tome, KLEIN_MASTER).setSpecial().registerStat();
	
	
	public static void init()
	{
		list = new ArrayList();
		
		list.add(PHIL_STONE);
		list.add(ALCH_CHEST);
		list.add(ALCH_BAG);
		list.add(TRANSMUTATION);
		list.add(CONDENSER);
		list.add(COLLECTOR);
		list.add(RELAY);
		list.add(PORTABLE_TRANSMUTATION);
		list.add(DARK_MATTER);
		list.add(RED_MATTER);
		list.add(DM_BLOCK);
		list.add(RM_BLOCK);
		list.add(DM_FURNACE);
		list.add(RM_FURNACE);
		list.add(DM_PICK);
		list.add(RM_PICK);
		list.add(KLEIN_BASIC);
		list.add(KLEIN_MASTER);
		list.add(TOME);
		
		AchievementPage.registerAchievementPage(new AchievementPage("ProjectE", list.toArray(new Achievement[list.size()])));
	}
	
	public static Achievement getAchievementForItem(ItemStack stack)
	{
		if (stack == null)
		{
			return null;
		}
		
		for (Achievement ach : list)
		{
			ItemStack s = ach.theItemStack;
			
			if (s.getItem() == stack.getItem() && s.getItemDamage() == stack.getItemDamage())
			{
				return ach;
			}
		}
		
		return null;
	}
}
