package nepisat.ee2.obj.gui.Container.Slots;

import nepisat.ee2.EMC.BlockEMCMapper;
import nepisat.ee2.obj.gui.Container.ContainerUtil;
import nepisat.ee2.obj.tileentity.TileEntityRenseiban;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotIn extends Slot{

	private TileEntityRenseiban tile;
	
	public SlotIn(TileEntityRenseiban tile, int par2, int par3, int par4) 
	{
		super(tile, par2, par3, par4);
		this.tile = tile;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return ContainerUtil.doesItemHaveEmc(stack);
	}
	
	@Override
	public void putStack(ItemStack stack)
	{
		if (stack == null)
		{
			return;
		}
			if (stack.stackSize == 1){
				tile.addEmc(BlockEMCMapper.getEmc(stack));
			}else{
				tile.addEmc(BlockEMCMapper.getEmc(stack)*stack.stackSize);
			}
			tile.KIOKU(stack);
			tile.Update();
			return;
		
		
	}
	
	@Override
	public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
	{
		super.onPickupFromSlot(par1EntityPlayer, par2ItemStack);
		
		tile.Update();
	}
	
	@Override
	public int getSlotStackLimit()
	{
		return 64;
	}
}
