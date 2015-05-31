package nepisat.ee2.gameObjs.container.slots.collector;

import nepisat.ee2.emc.FuelMapper;
import nepisat.ee2.gameObjs.ObjHandler;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotCollectorInv extends Slot 
{
	public SlotCollectorInv(IInventory inventory, int slotIndex, int xPos, int yPos) 
	{
		super(inventory, slotIndex, xPos, yPos);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		if (stack == null)
		{
			return false;
		}
		
		return stack.getItem().equals(ObjHandler.kleinStars) || (FuelMapper.isStackFuel(stack) && !FuelMapper.isStackMaxFuel(stack));
	}
}
