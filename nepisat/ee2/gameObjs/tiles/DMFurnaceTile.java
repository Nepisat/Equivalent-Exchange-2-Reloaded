package nepisat.ee2.gameObjs.tiles;


import nepisat.ee2.gameObjs.ObjHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DMFurnaceTile extends RMFurnaceTile implements IInventory, ISidedInventory
{
	public DMFurnaceTile()
	{
		this.inventory = new ItemStack[19];
		this.ticksBeforeSmelt = 10;
		this.efficiencyBonus = 3;
		this.outputSlot = 10;
		this.inputStorage = new int[] {2, 9};
		this.outputStorage = new int[] {11, 18};
	}
	
	@Override
	public int getSizeInventory() 
	{
		return 19;
	}
	
	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int value)
	{
		return furnaceCookTime * value / ticksBeforeSmelt;
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) 
	{
		if (stack == null)
		{
			return false;
		}
		
		if (slot == 0)
		{
			return TileEntityFurnace.isItemFuel(stack) || stack.getItem() == ObjHandler.kleinStars;
		}
		else if (slot >= 1 && slot <= 9)
		{
			return FurnaceRecipes.smelting().getSmeltingResult(stack) != null;
		}
		
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) 
	{
		if (side == 1 || side == 0)
		{
			return new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		}
		else
		{
			return new int[] {10, 11, 12, 13, 14, 15, 16, 17, 18};
		}
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) 
	{
		return slot <= 9;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) 
	{
		return slot >= 10;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public String getInvName() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public boolean isInvNameLocalized() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public void onInventoryChanged() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
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