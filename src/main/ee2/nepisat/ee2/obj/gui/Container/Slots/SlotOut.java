package nepisat.ee2.obj.gui.Container.Slots;

import scala.collection.mutable.Stack;
import nepisat.ee2.obj.tileentity.TileEntityRenseiban;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotOut extends Slot
{
	public ItemStack stackl;
	private TileEntityRenseiban tile;
	
	public SlotOut(TileEntityRenseiban tile, int par2, int par3, int par4) 
	{
		super(tile, par2, par3, par4);
		this.tile = tile;
	}
	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		return false;
	}
	@Override
	public void putStack(ItemStack stack)
	{
		//減少
		 this.inventory.setInventorySlotContents(this.getSlotIndex(), stack);
	     //this.onSlotChanged();
		return;
	}
	@Override
	public ItemStack decrStackSize(int slot)
	{
		
		//if(tile.worldObj!=null && !tile.worldObj.isRemote){
		ItemStack stack = getStack().copy();
		stack.stackSize=slot;
		tile.checkForUpdates();
		tile.removeEmc(stack);
		return stack;
	

	}
	@Override
	public boolean canTakeStack(EntityPlayer player)
	{
		return true;
	}

}
