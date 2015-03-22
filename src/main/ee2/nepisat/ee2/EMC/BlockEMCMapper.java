package nepisat.ee2.EMC;

import java.util.HashMap;
import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Map.Entry;
public class BlockEMCMapper {
	/*Project-Eをパk...参考にしました。
	 * 
	 */
	
	public static Map<EMCStacks, Integer> emc = new LinkedHashMap<EMCStacks, Integer>();
	public static void EMCMap(){
		
		setEMC(new ItemStack(Block.cobblestone), 1);
		setEMC(new ItemStack(Block.stone), 1);
		setEMC(new ItemStack(Block.netherrack), 1);
		setEMC(new ItemStack(Block.dirt), 1);
		setEMC(new ItemStack(Block.grass), 1);
		setEMC(new ItemStack(Block.mycelium), 1);
		setEMC(new ItemStack(Block.leaves), 1);
		setEMC(new ItemStack(Block.sand, 1, 0), 1);
		setEMC(new ItemStack(Block.sand, 1, 1), 1);
		setEMC(new ItemStack(Block.snow), 1);
		setEMC(new ItemStack(Block.ice), 1);
		setEMC(new ItemStack(Block.deadBush), 1);
		setEMC(new ItemStack(Block.gravel), 4);
		setEMC(new ItemStack(Block.cactus), 8);
		setEMC(new ItemStack(Block.vine), 8);
		setEMC(new ItemStack(Block.torchWood), 9);
		setEMC(new ItemStack(Block.web), 12);
		setEMC(new ItemStack(Item.seeds), 16);
		setEMC(new ItemStack(Item.melon), 16);
		
		setEMC(new ItemStack(Item.clay), 16);
		setEMC(new ItemStack(Block.waterlily), 16);
		setEMC(new ItemStack(Block.plantRed), 16);
		setEMC(new ItemStack(Block.plantYellow), 16);
		setEMC(new ItemStack(Item.wheat), 24);
		setEMC(new ItemStack(Block.netherStalk), 24);
		setEMC(new ItemStack(Item.stick), 4);
		setEMC(new ItemStack(Block.mushroomBrown), 32);
		setEMC(new ItemStack(Block.mushroomRed), 32);
		setEMC(new ItemStack(Item.reed), 32);
		setEMC(new ItemStack(Block.slowSand), 49);
		setEMC(new ItemStack(Block.obsidian), 64);
		
		for (int i = 0; i < 16; i++)
		{
			setEMC(new ItemStack(Block.blockClay, 1, i), 64);
		}
		
		setEMC(new ItemStack(Item.appleRed), 128);
		setEMC(new ItemStack(Item.dyePowder, 1, 3), 128);
		setEMC(new ItemStack(Block.pumpkin), 144);
		setEMC(new ItemStack(Item.bone), 144);
	//	setEMC(new ItemStack(Block.mossy_cobblestone), 145);
		setEMC(new ItemStack(Item.saddle), 192);
		setEMC(new ItemStack(Item.record11), 2048);
		setEMC(new ItemStack(Item.record13), 2048);
		setEMC(new ItemStack(Item.recordBlocks), 2048);
		setEMC(new ItemStack(Item.recordCat), 2048);
		setEMC(new ItemStack(Item.recordChirp), 2048);
		setEMC(new ItemStack(Item.recordFar), 2048);
		setEMC(new ItemStack(Item.recordMall), 2048);
		setEMC(new ItemStack(Item.recordMellohi), 2048);
		setEMC(new ItemStack(Item.recordStal), 2048);
		setEMC(new ItemStack(Item.recordStrad), 2048);
		setEMC(new ItemStack(Item.recordWait), 2048);
		setEMC(new ItemStack(Item.recordWard), 2048);
		//setEMC(new ItemStack(Item.string), 12);
		setEMC(new ItemStack(Item.rottenFlesh), 32);
		setEMC(new ItemStack(Item.slimeBall), 32);
		setEMC(new ItemStack(Item.egg), 32);
		setEMC(new ItemStack(Item.feather), 48);
		setEMC(new ItemStack(Item.leather), 64);
		setEMC(new ItemStack(Item.spiderEye), 128);
		setEMC(new ItemStack(Item.gunpowder), 192);
		setEMC(new ItemStack(Item.enderPearl), 1024);
		setEMC(new ItemStack(Item.blazeRod), 1536);
		setEMC(new ItemStack(Item.ghastTear), 4096);
		//setEMC(new ItemStack(Block.dragon_egg), 262144);
		//setEMC(new ItemStack(Item.porkchop), 64);
		//setEMC(new ItemStack(Item.beef), 64);
	//setEMC(new ItemStack(Item.chicken), 64);
		//setEMC(new ItemStack(Item.fish), 64);
		setEMC(new ItemStack(Item.carrot), 64);
		setEMC(new ItemStack(Item.potato), 64);
	//	setEMC(new ItemStack(Item.iron_ingot), 256);
		//setEMC(new ItemStack(Item.gold_ingot), 2048);
		setEMC(new ItemStack(Item.diamond), 8192);
		setEMC(new ItemStack(Item.flint), 4);
		setEMC(new ItemStack(Item.coal), 128);
		setEMC(new ItemStack(Item.redstone), 64);
		setEMC(new ItemStack(Item.glowstone), 384);
		setEMC(new ItemStack(Item.netherQuartz), 256);
	//	setEMC(new ItemStack(Item.dye, 1, 4), 864);

		for (int i = 0; i < 15; i++)
		{
			if (i == 3 || i == 4) 
			{
				continue;
			}
			
		//	setEMC(new ItemStack(Item.dye, 1, i), 16);
		}
		
		setEMC(new ItemStack(Item.enchantedBook), 2048);
		setEMC(new ItemStack(Item.emerald), 16384);
		
		setEMC(new ItemStack(Item.netherStar), 139264);
		//setEMC(new ItemStack(Item.iron_horse_armor), 1280);
		//setEMC(new ItemStack(Item.golden_horse_armor), 1024);
		//setEMC(new ItemStack(Item.diamond_horse_armor), 40960);
		//setEMC(new ItemStack(Block.tallgrass), 1);
		//setEMC(new ItemStack(Block.packed_ice), 4);
		setEMC(new ItemStack(Item.snowball), 1);
		//setEMC(new ItemStack(Item.filled_map), 1472);
		setEMC(new ItemStack(Item.blazePowder), 768);
		//setEMC(new ItemStack(Item.dye, 1, 15), 48);
		
	}
	public static void setEMC(ItemStack stack, int value)
	{
		  stack.setTagCompound(new NBTTagCompound());
		  NBTTagCompound tag = stack.getTagCompound();
		setEMC(new EMCStacks(stack), value);
	}
	private static void setEMC(EMCStacks stack, int value)
	{
		
		EMCStacks copy = stack.copy();
		copy.qnty = 1;

		if (emc.containsKey(copy))
		{
			return;
		}
		
		if (value > 0)
		{
			emc.put(copy, value);
		}
		
	}
	public static boolean mapContains(EMCStacks key)
	{
		EMCStacks copy = key.copy();
		copy.qnty = 1;

		return emc.containsKey(copy);
	}
	public static int getEmc(EMCStacks stack)
	{
		EMCStacks copy = stack.copy();
		copy.qnty = 1;

		return emc.get(copy);
	}
	public static int getEmc(Block Block)
	{
		EMCStacks stack = new EMCStacks(new ItemStack(Block));

		if (stack.isValid() && mapContains(stack))
		{
			return getEmc(stack);
		}

		return 0;
	}
	public static int getEmc(ItemStack stack)
	{
		if (stack == null) 
		{
			return 0;
		}
		
		EMCStacks iStack = new EMCStacks(stack);

		if (!iStack.isValid())
		{
			return 0;
		}

		if (!stack.getHasSubtypes() && stack.getMaxDamage() != 0)
		{
			iStack.damage = 0;
			
			if (mapContains(iStack))
			{
				int emc = getEmc(iStack);
				
				int relDamage = (stack.getMaxDamage() - stack.getItemDamage());

				if (relDamage <= 0)
				{
					//Impossible?
					return 0;
				}

				long result = emc * relDamage;

				if (result <= 0)
				{
					//Congratulations, big number is big.
					return emc;
				}

				result /= stack.getMaxDamage();
				result += getEnchantEmcBonus(stack);

				if (result > Integer.MAX_VALUE)
				{
					return emc;
				}

				if (result <= 0)
				{
					return 1;
				}

				return (int) result;
			}
		}
		else
		{
			if (mapContains(iStack))
			{
				return getEmc(iStack) + getEnchantEmcBonus(stack);
			}
		}
			
		return 0;
	}
	public static int getEnchantEmcBonus(ItemStack stack)
	{
		int result = 0;
		
		Map<Integer, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
		
		if (!enchants.isEmpty())
		{
			for (Entry<Integer, Integer> entry : enchants.entrySet())
			{
				Enchantment ench = Enchantment.enchantmentsList[entry.getKey()];
				
				if (ench.getWeight() == 0)
				{
					continue;
				}
				
				result += 5000 / ench.getWeight() * entry.getValue();
			}
		}
		
		return result;
	}
}
