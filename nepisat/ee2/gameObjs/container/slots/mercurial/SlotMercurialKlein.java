package nepisat.ee2.gameObjs.container.slots.mercurial;


import nepisat.ee2.gameObjs.items.KleinStar;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotMercurialKlein extends Slot
{
	public SlotMercurialKlein(IInventory par1iInventory, int par2, int par3, int par4) 
	{
		super(par1iInventory, par2, par3, par4);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return stack.getItem() instanceof KleinStar;
	}
}
