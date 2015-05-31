package nepisat.ee2.gameObjs.items;

import java.util.HashMap;

import nepisat.ee2.api.IItemCharge;
import nepisat.ee2.handlers.KeyHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemCharge extends ItemPE implements IItemCharge
{
	byte numCharges;
	private HashMap<String, Integer> toolClasses = new HashMap<String, Integer>();
	  private String[] harvestTool = new String[16];
	public ItemCharge(int i,String unlocalName, byte numCharges)
	{
		super(i);
		this.numCharges = numCharges;
		this.setUnlocalizedName(unlocalName);
		this.setMaxStackSize(1);
		
	}
	
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return stack.hasTagCompound();
	}
	public String getHarvestTool(int metadata)
    {
        return harvestTool[metadata];
    }
	  public int getHarvestLevel(ItemStack stack, String toolClass)
	    {
	        Integer ret = toolClasses.get(toolClass);
	        return ret == null ? -1 : ret;
	    }
	  public float getDigSpeed(ItemStack itemstack, Block block, int metadata)
	    {
	        return 1.0F;
	    }
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		byte charge = getCharge(stack);
		
		//Must be beetween 0.0D - 1.0D
		return charge == 0 ? 1.0D : 1.0D - (double) charge / (double) (numCharges - 1);
	}
	
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) 
	{
		if (!world.isRemote)
		{
			stack.stackTagCompound = new NBTTagCompound();
		}
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) 
	{
		EntityPlayer player = (EntityPlayer) entity;
		if (!stack.hasTagCompound())
		{
			stack.stackTagCompound = new NBTTagCompound();
		}
		if(KeyHandler.getCharge()){
		//	System.out.println(KeyHandler.getCharge());
			changeCharge(player,stack);
			System.out.println(getCharge(stack));
		}
		
	}
	
	
	@Override
	public byte getCharge(ItemStack stack)
	{
		return stack.stackTagCompound.getByte("Charge");
	}
	
	@Override
	public void changeCharge(EntityPlayer player, ItemStack stack)
	{
		byte currentCharge = getCharge(stack);
	
		if (player.isSneaking())
		{
			if (currentCharge > 0)
			{
				stack.stackTagCompound.setByte("Charge", (byte) (currentCharge - 1));
			}
		}
		else if (currentCharge < numCharges - 1)
		{
			stack.stackTagCompound.setByte("Charge", (byte) (currentCharge + 1));
		}
	}
}
