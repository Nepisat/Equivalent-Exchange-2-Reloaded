package nepisat.ee2.obj.gui.Container.Slots;

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
		
		super.putStack(stack);
		tile.Update();
		/*
		if (stack.getItem() == ObjHandler.kleinStars)
		{
			int remainEmc = tile.maxemc()() - (int) Math.ceil(tile.getStoredEmc());
			
			if (ItemPE.getEmc(stack) >= remainEmc)
			{
				tile.addEmcWithPKT(remainEmc);
				ItemPE.removeEmc(stack, remainEmc);
			}
			else
			{
				tile.addEmcWithPKT(ItemPE.getEmc(stack));
				ItemPE.setEmc(stack, 0);
			}
			
			tile.handleKnowledge(stack.copy());
			return;
		}
		
		if (stack.getItem() != ObjHandler.tome)
		{
			tile.handleKnowledge(stack.copy());
		}
		else
		{
			tile.updateOutputs();
		}
		*/
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
		return 1;
	}
}
