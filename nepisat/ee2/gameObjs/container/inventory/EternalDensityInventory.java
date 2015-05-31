package nepisat.ee2.gameObjs.container.inventory;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
public class EternalDensityInventory implements IInventory
{
	private ItemStack inventory[];
	private EntityPlayer player;
	private boolean isInWhitelist;
	
	public EternalDensityInventory(ItemStack stack, EntityPlayer player) 
	{
		inventory = new ItemStack[10];
		
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		
		readFromNBT(stack.stackTagCompound);
		
		this.player = player;
	}
	

	@Override
	public int getSizeInventory()
	{
		return 10;
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int num) 
	{
		ItemStack stack = getStackInSlot(slot);
		
		if(stack != null)
		{
			if(stack.stackSize > num)
			{
				stack = stack.splitStack(num);
				//markDirty();
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
		this.inventory[slot] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}
		
		//markDirty();
	}

	@Override
	public String getInvName() 
	{
		return null;
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
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		isInWhitelist = nbt.getBoolean("Whitelist");
		
		NBTTagList items = nbt.getTagList("Items");
		
		for (int i = 0; i < items.tagCount(); i++)
		{
			NBTTagCompound tag = (NBTTagCompound) items.tagAt(i);
			
			inventory[tag.getByte("Slot")] = ItemStack.loadItemStackFromNBT(tag);
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setBoolean("Whitelist", isInWhitelist);
		
		NBTTagList items = new NBTTagList();
		
		for (int i = 0; i < inventory.length; i++)
		{
			if (inventory[i] != null)
			{
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(tag);
				items.appendTag(tag);
			}
		}
		
		nbt.setTag("Items", items);
	}
	
	public void changeMode()
	{
		isInWhitelist = !isInWhitelist;
		//markDirty();
		
	//	PacketHandler.sendToServer(new UpdateGemModePKT(isInWhitelist));
	}
	
	public boolean isWhitelistMode()
	{
		return isInWhitelist;
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
