package nepisat.ee2.obj.gui.Container;

import nepisat.ee2.EMC.BlockEMCMapper;
import nepisat.ee2.obj.gui.Container.Slots.SlotIn;
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
		this.addSlotToContainer(new SlotIn(this.tile, 9, 107, 97));//
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

	public boolean canInteractWith(EntityPlayer var1) 
	{
		return true;
	}
	
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(par2);
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
 
			//スロット番号が2の時
			if (par2>=11&&par2<=21)
			{
				//アイテムの移動(スロット3～39へ)
				if (!this.mergeItemStack(itemstack1, 3, 39, true))
				{
					return null;
				}
 
				slot.onSlotChange(itemstack1, itemstack);
			}
			//スロット番号が0、1でない時
			else if (par2 != 1 && par2 != 0)
			{
				if (FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null)
				{
					//アイテムの移動(スロット0～1へ)
					if (!this.mergeItemStack(itemstack1, 0, 1, false))
					{
						return null;
					}
				}
				else if (TileEntityFurnace.isItemFuel(itemstack1))
				{
					//アイテムの移動(スロット1～2へ)
					if (!this.mergeItemStack(itemstack1, 1, 2, false))
					{
						return null;
					}
				}
				else if (par2 >= 3 && par2 < 30)
				{
					//アイテムの移動(スロット30～39へ)
					if (!this.mergeItemStack(itemstack1, 30, 39, false))
					{
						return null;
					}
				}
				else if (par2 >= 30 && par2 < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
				{
					return null;
				}
			}
			//アイテムの移動(スロット3～39へ)
			else if (!this.mergeItemStack(itemstack1, 3, 39, false))
			{
				return null;
			}
 
			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack)null);
			}
			else
			{
				slot.onSlotChanged();
			}
 
			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}
 
			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
		}
 
		return itemstack;
	}
 

	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);
	//	tile.closeInventory();
	}
	
	@Override
	public boolean canDragIntoSlot(Slot slot) 
	{
		return false;
	}
}