package nepisat.ee2.EMC;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class BlockEMCMapper {
	/*Project-Eをパk...参考にしました。
	 * 
	 */
	public static Map<EMCStacks, Integer> emc = new LinkedHashMap<EMCStacks, Integer>();
	public static void EMCMap(){
		setEMC(new ItemStack(Block.stone), 1);
	}
	public static void setEMC(ItemStack stack, int value)
	{
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
	public static int getEmcValue(ItemStack stack)
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
			
			if (EMCMapper.mapContains(iStack))
			{
				int emc = EMCMapper.getEmcValue(iStack);
				
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
			if (EMCMapper.mapContains(iStack))
			{
				return EMCMapper.getEmcValue(iStack) + getEnchantEmcBonus(stack);
			}
		}
			
		return 0;
	}
}
