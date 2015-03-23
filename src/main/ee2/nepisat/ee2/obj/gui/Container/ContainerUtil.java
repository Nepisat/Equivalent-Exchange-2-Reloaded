package nepisat.ee2.obj.gui.Container;

import nepisat.ee2.EMC.BlockEMCMapper;
import nepisat.ee2.EMC.EMCStacks;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ContainerUtil {
	public static ItemStack getNormalizedStack(ItemStack stack)
	{
		ItemStack result = stack.copy();
		result.stackSize = 1;
		return result;
	}
	public static boolean areItemStacksEqual(ItemStack stack1, ItemStack stack2)
	{
		return ItemStack.areItemStacksEqual(getNormalizedStack(stack1), getNormalizedStack(stack2));
	}
	public static boolean doesItemHaveEmc(ItemStack stack)
	{
		if (stack == null) 
		{
			return false;
		}
		
		
		EMCStacks iStack = new EMCStacks(stack);

		if (!iStack.isValid())
		{
			return false;
		}

		if (!stack.getHasSubtypes() && stack.getMaxDamage() != 0)
		{
			iStack.damage = 0;
		}

		return BlockEMCMapper.mapContains(iStack);
	}
	public static ItemStack pushStackInInv(IInventory inv, ItemStack stack)
	{
		int limit;
		
		if (inv instanceof InventoryPlayer)
		{
			limit = 36;
		}
		else
		{
			limit = inv.getSizeInventory();
		}
		
		for (int i = 0; i < limit; i++)
		{
			ItemStack invStack = inv.getStackInSlot(i);
			
			if (invStack == null)
			{
				inv.setInventorySlotContents(i, stack);
				return null;
			}
			
			if (areItemStacksEqual(stack, invStack) && invStack.stackSize < invStack.getMaxStackSize())
			{
				int remaining = invStack.getMaxStackSize() - invStack.stackSize;
				
				if (remaining >= stack.stackSize)
				{
					invStack.stackSize += stack.stackSize;
					inv.setInventorySlotContents(i, invStack);
					return null;
				}
				
				invStack.stackSize += remaining;
				inv.setInventorySlotContents(i, invStack);
				stack.stackSize -= remaining;
			}
		}
		
		return stack.copy();
	}
	
	/**
	 *	Returns an itemstack if the stack passed could not entirely fit in the inventory, otherwise returns null. 
	 */
	public static ItemStack pushStackInInv(ItemStack[] inv, ItemStack stack)
	{
		for (int i = 0; i < inv.length; i++)
		{
			ItemStack invStack = inv[i];
			
			if (invStack == null)
			{
				inv[i] = stack;
				return null;
			}
			
			if (areItemStacksEqual(stack, invStack) && invStack.stackSize < invStack.getMaxStackSize())
			{
				int remaining = invStack.getMaxStackSize() - invStack.stackSize;
				
				if (remaining >= stack.stackSize)
				{
					invStack.stackSize += stack.stackSize;
					inv[i] = invStack;
					return null;
				}
				
				invStack.stackSize += remaining;
				inv[i] = invStack;
				stack.stackSize -= remaining;
			}
		}
		
		return stack.copy();
	}
	public static boolean basicAreStacksEqual(ItemStack stack1, ItemStack stack2)
	{
		return (stack1.getItem() == stack2.getItem()) && (stack1.getItemDamage() == stack2.getItemDamage());
	}
	public static boolean hasSpace(IInventory inv, ItemStack stack)
	{
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack invStack = inv.getStackInSlot(i);
			
			if (invStack == null) 
			{
				return true;
			}
			
			if (areItemStacksEqual(stack, invStack) && invStack.stackSize < invStack.getMaxStackSize())
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean hasSpace(ItemStack[] inv, ItemStack stack)
	{
		for (ItemStack invStack : inv)
		{
			if (invStack == null) 
			{
				return true;
			}
			
			if (areItemStacksEqual(stack, invStack) && invStack.stackSize < invStack.getMaxStackSize())
			{
				return true;
			}
		}
		
		return false;
	}
}
