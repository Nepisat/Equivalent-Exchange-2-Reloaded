package nepisat.ee2.gameObjs.container.slots.condenser;



import nepisat.ee2.gameObjs.tiles.CondenserTile;
import nepisat.ee2.utils.Utils;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotCondenserInput extends Slot
{
	public SlotCondenserInput(CondenserTile inventory, int slotIndex, int xPos, int yPos)
	{
		super(inventory, slotIndex, xPos, yPos);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return Utils.doesItemHaveEmc(stack);
	}
}
