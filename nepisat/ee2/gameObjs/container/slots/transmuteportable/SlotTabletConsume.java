package nepisat.ee2.gameObjs.container.slots.transmuteportable;

import cpw.mods.fml.common.FMLCommonHandler;
import nepisat.ee2.gameObjs.ObjHandler;
import nepisat.ee2.gameObjs.container.inventory.TransmuteTabletInventory;
import nepisat.ee2.gameObjs.items.KleinStar;
import nepisat.ee2.utils.Utils;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotTabletConsume extends Slot
{
	private TransmuteTabletInventory table;
	
	public SlotTabletConsume(TransmuteTabletInventory table, int par2, int par3, int par4) 
	{
		super(table, par2, par3, par4);
		this.table = table;
	}
	
	@Override
	public void putStack(ItemStack stack)
	{
		if (stack == null)
		{
			return;
		}
		
		ItemStack cache = stack.copy();
		
		double toAdd = 0;
		
		while (!table.hasMaxedEmc() && stack.stackSize > 0)
		{
			toAdd += Utils.getEmcValue(stack);
			stack.stackSize--;
		}
		
		if (cache.getItem() == ObjHandler.kleinStars)
		{
			toAdd += KleinStar.getEmc(cache);
		}
		
		table.addEmc(toAdd);
		this.onSlotChanged();
		table.handleKnowledge(cache);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return !table.hasMaxedEmc() && Utils.doesItemHaveEmc(stack);
	}
}
