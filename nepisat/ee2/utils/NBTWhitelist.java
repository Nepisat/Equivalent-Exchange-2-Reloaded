package nepisat.ee2.utils;

import nepisat.ee2.emc.SimpleStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public final class NBTWhitelist
{
	private static final List<SimpleStack> LIST = new ArrayList<SimpleStack>();

	public static boolean register(ItemStack stack)
	{
		SimpleStack s = new SimpleStack(stack);

		if (!s.isValid())
		{
			return false;
		}

		s.damage = OreDictionary.WILDCARD_VALUE;

		if (!LIST.contains(s))
		{
			LIST.add(s);
			return true;
		}

		return false;
	}

	public static boolean shouldDupeWithNBT(ItemStack stack)
	{
		SimpleStack s = new SimpleStack(stack);

		if (!s.isValid())
		{
			return false;
		}

		return LIST.contains(s);
	}
}
