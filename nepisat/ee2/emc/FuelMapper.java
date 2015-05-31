package nepisat.ee2.emc;


import nepisat.ee2.gameObjs.ObjHandler;
import nepisat.ee2.utils.Comparators;
import nepisat.ee2.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class FuelMapper 
{
	private static final List<SimpleStack> FUEL_MAP = new ArrayList<SimpleStack>();
	
	public static void loadMap()
	{
		if (!FUEL_MAP.isEmpty())
		{
			FUEL_MAP.clear();
		}
		
		addToMap(new ItemStack(Item.coal, 1, 1));
		addToMap(new ItemStack(Item.redstone));
		addToMap(new ItemStack(Block.blockRedstone));
		addToMap(new ItemStack(Item.coal));
		addToMap(new ItemStack(Block.coalBlock));
		addToMap(new ItemStack(Item.gunpowder));
		addToMap(new ItemStack(Item.glowstone));
		addToMap(new ItemStack(ObjHandler.fuels, 1, 0));
		addToMap(new ItemStack(ObjHandler.fuelBlock, 1, 0));
		addToMap(new ItemStack(Item.blazePowder));
		addToMap(new ItemStack(Block.glowStone));
		addToMap(new ItemStack(ObjHandler.fuels, 1, 1));
		addToMap(new ItemStack(ObjHandler.fuelBlock, 1, 1));
		addToMap(new ItemStack(ObjHandler.fuels, 1, 2));
		addToMap(new ItemStack(ObjHandler.fuelBlock, 1, 2));
		
		Collections.sort(FUEL_MAP, Comparators.SIMPLESTACK_ASCENDING);
	}
	
	private static void addToMap(ItemStack stack)
	{
		if (Utils.doesItemHaveEmc(stack))
		{
			addToMap(new SimpleStack(stack));
		}
	}
	
	public static boolean isStackFuel(ItemStack stack)
	{
		return mapContains(new SimpleStack(stack));
	}
	
	public static boolean isStackMaxFuel(ItemStack stack)
	{
		return indexInMap(new SimpleStack(stack)) == FUEL_MAP.size() - 1;
	}
	
	public static ItemStack getFuelUpgrade(ItemStack stack)
	{
		SimpleStack fuel = new SimpleStack(stack);

		int index = indexInMap(fuel);
		
		if (index == -1)
		{
			//PELogger.logFatal("Fuel not found in fuel map: "+stack);
			return null;
		}
		
		int nextIndex = index == FUEL_MAP.size() - 1 ? 0 : index + 1;
		
		return FUEL_MAP.get(nextIndex).toItemStack();
	}

	private static void addToMap(SimpleStack stack)
	{
		if (stack.isValid())
		{
			SimpleStack copy = stack.copy();
			copy.qnty = 1;

			if (!FUEL_MAP.contains(copy))
			{
				FUEL_MAP.add(copy);
			}
		}
	}

	private static boolean mapContains(SimpleStack stack)
	{
		if (!stack.isValid())
		{
			return false;
		}

		SimpleStack copy = stack.copy();
		copy.qnty = 1;

		return FUEL_MAP.contains(copy);
	}

	private static int indexInMap(SimpleStack stack)
	{
		SimpleStack copy = stack.copy();
		copy.qnty = 1;

		return FUEL_MAP.indexOf(copy);
	}
}
