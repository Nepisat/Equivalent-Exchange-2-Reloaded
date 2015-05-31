package nepisat.ee2.gameObjs.container.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;


public class MercurialEyeInventory implements IInventory
{
	private final ItemStack invItem;
	private ItemStack kleinStar;
	private ItemStack target;
	
	public MercurialEyeInventory(ItemStack stack)
	{
		invItem = stack;
		
		if (!invItem.hasTagCompound())
		{
			invItem.setTagCompound(new NBTTagCompound());
		}
		
		readFromNBT(invItem.stackTagCompound);
	}

	@Override
	public int getSizeInventory() 
	{
		return 2;
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		return slot == 0 ? kleinStar : target;
	}
	
	public ItemStack getKleinStack()
	{
		return getStackInSlot(0);
	}
	
	public ItemStack getTargetStack()
	{
		return getStackInSlot(1);
	}

	@Override
	public ItemStack decrStackSize(int slot, int qty)
	{
		ItemStack stack = getStackInSlot(slot);
		
		if(stack != null)
		{
			if(stack.stackSize > qty)
			{
				stack = stack.splitStack(qty);
				
			}
			else
			{
				setInventorySlotContents(slot, null);
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) 
	{
		ItemStack stack = getStackInSlot(slot);
		
		if(stack != null)
		{
			setInventorySlotContents(slot, null);
		}
		
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) 
	{
		if (slot == 0)
		{
			kleinStar = stack;
		}
		else 
		{
			target = stack;
		}

		if (stack != null && stack.stackSize > getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}

		
	}

	@Override
	public String getInvName() 
	{
		return "Mercurial Eye";
	}

	

	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}


	@Override
	public boolean isUseableByPlayer(EntityPlayer player) 
	{
		return true;
	}

	

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) 
	{
		return true;
	}
	
	public void update()
	{
		readFromNBT(invItem.stackTagCompound);
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		NBTTagList list = nbt.getTagList("Items");
		
		for (int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound subNBT = (NBTTagCompound) list.tagAt(i);
			this.setInventorySlotContents(subNBT.getByte("Slot"), ItemStack.loadItemStackFromNBT(subNBT));
		}	
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		
		for (int i = 0; i < 2; i++)
		{
			if (getStackInSlot(i) != null)
			{
				NBTTagCompound subNBT = new NBTTagCompound();
				subNBT.setByte(("Slot"), (byte) i);
				getStackInSlot(i).writeToNBT(subNBT);
				list.appendTag(subNBT);
			}
		}
		
		nbt.setTag("Items", list);
	}


	@Override
	public boolean isInvNameLocalized() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public void onInventoryChanged() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void openChest() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void closeChest() {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}
