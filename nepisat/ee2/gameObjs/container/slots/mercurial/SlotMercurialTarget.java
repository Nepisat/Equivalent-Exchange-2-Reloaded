package nepisat.ee2.gameObjs.container.slots.mercurial;


import nepisat.ee2.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotMercurialTarget extends Slot
{
	public SlotMercurialTarget(IInventory par1iInventory, int par2, int par3, int par4) 
	{
		super(par1iInventory, par2, par3, par4);
	}
	
	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		Block block = Block.blocksList[stack.getItem().itemID];
		return block != null && !(block instanceof ITileEntityProvider) && Utils.doesItemHaveEmc(stack);
	}
}
