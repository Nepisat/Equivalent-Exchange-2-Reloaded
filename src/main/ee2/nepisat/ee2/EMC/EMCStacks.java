package nepisat.ee2.EMC;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class EMCStacks
{
	public int id;
	public int damage;
	public int qnty;

	public EMCStacks(int id, int qnty, int damage)
	{
		this.id = id;
		this.qnty = qnty;
		this.damage = damage;
	}
	
	public EMCStacks(ItemStack stack)
	{
		if (stack == null)
		{
			id = -1;
		}
		else
		{
			id = stack.itemID;
			damage = stack.getItemDamage();
			qnty = stack.stackSize;
		}
	}

	public boolean isValid()
	{
		return id != -1;
	}

	

	public EMCStacks copy()
	{
		return new EMCStacks(id, qnty, damage);
	}

	@Override
	public int hashCode() 
	{
		return id;
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if (obj instanceof EMCStacks)
		{
			EMCStacks other = (EMCStacks) obj;
			 
			if (this.damage == OreDictionary.WILDCARD_VALUE || other.damage == OreDictionary.WILDCARD_VALUE)
			{
				//return this.id == other.id;
				return this.qnty == other.qnty && this.id == other.id;
			}

			//return this.id == other.id && this.damage == other.damage;
			return this.id == other.id && this.qnty == other.qnty && this.damage == other.damage;
		}
		
		return false;
	}
	
	@Override
	public String toString() 
	{
		
		
		return "id:" + id + " damage:" + damage + " qnty:" + qnty;
	}
}
