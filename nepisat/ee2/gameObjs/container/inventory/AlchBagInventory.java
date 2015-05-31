package nepisat.ee2.gameObjs.container.inventory;
import nepisat.ee2.playerData.AlchemicalBags;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class AlchBagInventory implements IInventory
{
	private final ItemStack invItem;
	private ItemStack[] inventory;
	private EntityPlayer player;
	
	public AlchBagInventory(EntityPlayer player, ItemStack stack)
	{
		invItem = stack;
		this.player = player;
		inventory = AlchemicalBags.get(player.getCommandSenderName(), (byte) stack.getItemDamage());
	}

	@Override
	public int getSizeInventory() 
	{
		return 104;
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int qty)
	{
		ItemStack stack = getStackInSlot(slot);
		
		if (stack != null)
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
		
		if (stack != null)
		{
			setInventorySlotContents(slot, null);
		}
		
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) 
	{
		inventory[slot] = stack;
		
		if (stack != null && stack.stackSize > getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}
		
		
	}

	@Override
	public String getInvName() 
	{
		return "Alchemical Bag";
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
	public void openChest() 
	{
	}

	@Override
	public void closeChest() 
	{
		if (!player.worldObj.isRemote)
		{
			AlchemicalBags.set(player.getCommandSenderName(), (byte) invItem.getItemDamage(), inventory);
			AlchemicalBags.sync(player);
		}
	}
	
	public ItemStack[] getInventory()
	{
		return inventory;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) 
	{
		return true;
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

}
