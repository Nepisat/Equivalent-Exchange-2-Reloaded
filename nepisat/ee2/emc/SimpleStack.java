package nepisat.ee2.emc;



import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class SimpleStack
{
	public int id;
	public int damage;
	public int qnty;

	public SimpleStack(int id, int qnty, int damage)
	{
		this.id = id;
		this.qnty = qnty;
		this.damage = damage;
	}
	
	public SimpleStack(ItemStack stack)
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

	public ItemStack toItemStack()
	{
		if (isValid())
		{
			Item item = Item.itemsList[id];

			if (item != null)
			{
				return new ItemStack(Item.itemsList[id], qnty, damage);
			}
		}

		return null;
	}

	public SimpleStack copy()
	{
		return new SimpleStack(id, qnty, damage);
	}

	@Override
	public int hashCode() 
	{
		return id;
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if (obj instanceof SimpleStack)
		{
			SimpleStack other = (SimpleStack) obj;
			 
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
		Object obj = Item.itemsList[id];
		
		if (obj != null)
		{
			return obj + " " + qnty + " " + damage;
		}
		
		return "id:" + id + " damage:" + damage + " qnty:" + qnty;
	}
}
