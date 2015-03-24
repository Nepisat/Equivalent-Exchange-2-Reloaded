package nepisat.ee2.obj.tileentity;

import nepisat.ee2.EMC.BlockEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEE extends TileEntity{
	private static int emc;
	private int maxemc=1073741824;
	public int getStoredEmc() {
		return emc;
	}
	public int maxemc(){
		return maxemc;
	}
	public int getemc() {
		return emc;
	}
	public boolean hasMaxedEmc()
	{
		return emc >= maxemc;
	}
	public void addEmc(double amount)
	{
		if (this.worldObj != null && !this.worldObj.isRemote){
		emc += amount;
		
		if (emc > maxemc || emc < 0)
		{
			emc = maxemc;
		}
		}
	}
	public void addEmc(int amount)
	{
		if (this.worldObj != null && !this.worldObj.isRemote){
		emc += amount;
		
		if (emc > maxemc || emc < 0)
		{
			emc = maxemc;
		}
		}
	}
	public void removeEmc(ItemStack stack)
	{
		if (this.worldObj != null && !this.worldObj.isRemote){
		removeEmc(BlockEMCMapper.getEmc(stack));
		}
	}
	public void removeEmc(double amount)
	{
		emc -= amount;
		
		if (emc < 0)
		{
			emc = 0;
		}
	}
}
