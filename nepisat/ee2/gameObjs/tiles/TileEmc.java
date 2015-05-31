package nepisat.ee2.gameObjs.tiles;


import nepisat.ee2.api.ITileEmc;
import nepisat.ee2.utils.Constants;
import nepisat.ee2.utils.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEmc extends TileEntity implements ITileEmc
{
	private double emc;
	private final int maxAmount;
	
	public TileEmc()
	{
		maxAmount = Constants.TILE_MAX_EMC;
	}
	
	public TileEmc(int maxAmount)
	{
		this.maxAmount = maxAmount;
	}
	
	@Override
	public void setEmc(double value) 
	{
		this.emc = value <= maxAmount ? value : maxAmount;
	}
	
	@Override
	public void addEmc(double amount)
	{
		emc += amount;
		
		if (emc > maxAmount || emc < 0)
		{
			emc = maxAmount;
		}
	}
	
	public void addEmcWithPKT(double amount)
	{
		addEmc(amount);
		
		sendUpdatePKT();
	}
	
	public void addEmc(ItemStack stack)
	{
		addEmc(Utils.getEmcValue(stack) * stack.stackSize);
	}
	
	@Override
	public void removeEmc(double amount)
	{
		emc -= amount;
		
		if (emc < 0)
		{
			emc = 0;
		}
	}
	
	public void removeEmcWithPKT(double amount)
	{
		removeEmc(amount);
		
		sendUpdatePKT();
	}
	
	public void removeItemRelativeEmc(ItemStack stack)
	{
		removeEmc(Utils.getEmcValue(stack));
	}
	
	public void removeItemRelativeEmcWithPKT(ItemStack stack)
	{
		removeItemRelativeEmc(stack);
		
		sendUpdatePKT();
	}
	
	@Override
	public double getStoredEmc()
	{
		return emc;
	}
	
	public int getMaxEmc()
	{
		return maxAmount;
	}
	
	@Override
	public boolean hasMaxedEmc()
	{
		return emc >= maxAmount;
	}
	
	public void setEmcValue(double value)
	{
		emc = value;
	}
	
	public void setEmcValueWithPKT(double value)
	{
		setEmcValue(value);
		
		sendUpdatePKT();
	}
	
	public void sendUpdatePKT()
	{
		if (this.worldObj != null && !this.worldObj.isRemote)
		{
		//	PacketHandler.sendToAll(new ClientTableSyncPKT(emc, this.xCoord, this.yCoord, this.zCoord));
		}
	}
}
