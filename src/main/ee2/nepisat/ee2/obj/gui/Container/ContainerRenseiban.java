package nepisat.ee2.obj.gui.Container;

import nepisat.ee2.EMC.BlockEMCMapper;
import nepisat.ee2.obj.gui.Container.Slots.SlotOut;
import nepisat.ee2.obj.tileentity.TileEntityRenseiban;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerRenseiban extends Container {

	private TileEntityRenseiban tile;

	private int lastCookTime;
	private int lastBurnTime;
	private int lastItemBurnTime;

	public ContainerRenseiban(EntityPlayer player,TileEntityRenseiban par2TileEntity) {
		this.tile = par2TileEntity;

		// InventorySampleで追加するインベントリ
		this.addSlotToContainer(new Slot(this.tile, 0, 43, 23));
		this.addSlotToContainer(new Slot(this.tile, 1, 34, 41));
		this.addSlotToContainer(new Slot(this.tile, 2, 52, 41));
		this.addSlotToContainer(new Slot(this.tile, 3, 16, 50));
		this.addSlotToContainer(new Slot(this.tile, 4, 70, 50));
		this.addSlotToContainer(new Slot(this.tile, 5, 34, 59));
		this.addSlotToContainer(new Slot(this.tile, 6, 52, 59));
		this.addSlotToContainer(new Slot(this.tile, 7, 43, 77));
		this.addSlotToContainer(new Slot(this.tile, 8, 158, 50));
		this.addSlotToContainer(new Slot(this.tile, 9, 107, 97));//
		this.addSlotToContainer(new SlotOut(this.tile, 10, 123, 30));
		this.addSlotToContainer(new SlotOut(this.tile, 11, 140, 13));
		this.addSlotToContainer(new SlotOut(this.tile, 12, 158, 9));
		this.addSlotToContainer(new SlotOut(this.tile, 13, 176, 13));
		this.addSlotToContainer(new SlotOut(this.tile, 14, 193, 30));
		this.addSlotToContainer(new SlotOut(this.tile, 15, 199, 50));
		this.addSlotToContainer(new SlotOut(this.tile, 16, 193, 70));
		this.addSlotToContainer(new SlotOut(this.tile, 17, 176, 87));
		this.addSlotToContainer(new SlotOut(this.tile, 18, 158, 91));
		this.addSlotToContainer(new SlotOut(this.tile, 19, 140, 87));
		this.addSlotToContainer(new SlotOut(this.tile, 20, 123, 70));
		this.addSlotToContainer(new SlotOut(this.tile, 21, 116, 50));
		this.addSlotToContainer(new SlotOut(this.tile, 22, 158, 31));
		this.addSlotToContainer(new SlotOut(this.tile, 23, 139, 50));
		this.addSlotToContainer(new SlotOut(this.tile, 24, 177, 50));
		this.addSlotToContainer(new SlotOut(this.tile, 25, 158, 69));

		// 1 ～ 3段目のインベントリ
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 35 + j * 18, 117 + i * 18));

		//Player Hotbar
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(player.inventory, i, 35 + i * 18, 175));

	}

	@Override
	public boolean canInteractWith(EntityPlayer var1) 
	{
		return true;
	}
	
	//@Override
	//public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
	//{
		/*
		Slot slot = this.getSlot(slotIndex);
		
		if (slot == null || !slot.getHasStack()) 
		{
			return null;
		}
		
		ItemStack stack = slot.getStack();
		ItemStack newStack = stack.copy();
		
		if (slotIndex <= 7)
		{
			return null;
		}
		else if (slotIndex >= 10 && slotIndex <= 25)
		{	
		//	int emc = BlockEMCMapper.loadEMC((BlockEMCMapper.is[newStack.itemID]));
			
			int stackSize = 0;
			
		//	while (tile.getStoredEmc() >= emc && stackSize < newStack.getMaxStackSize())
			{
			//	tile.removeEmc(emc);
				//Utils.pushStackInInv(player.inventory, Utils.getNormalizedStack(newStack));
				stackSize++;
			}
			
			//tile.updateOutputs();
		}
		else if (slotIndex >= 26)
		//{
	//		int emc = BlockEMCMapper.loadEMC((BlockEMCMapper.is[stack.itemID]));
			
			//if (emc == 0)
			//{
			//	return null;
			//}
			
			//while(!(stack.stackSize > 0))
			//{
		//		tile.emc+=1;
		//		--stack.stackSize;
			//}
			
			//tile.handleKnowledge(newStack);

		//	if (stack.stackSize == 0)
		//	{
			//	slot.putStack(null);
			//}
		//}
		
		//return null;
		 * */

//	}

	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);
		//tile.closeInventory();
	}
	
	@Override
	public boolean canDragIntoSlot(Slot slot) 
	{
		return false;
	}
}
