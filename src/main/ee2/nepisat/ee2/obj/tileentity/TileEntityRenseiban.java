package nepisat.ee2.obj.tileentity;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import nepisat.ee2.EMC.BlockEMCMapper;
import nepisat.ee2.EMC.EMCStacks;
import nepisat.ee2.obj.gui.GuiRenseiban;
import nepisat.ee2.obj.gui.Container.ContainerUtil;
import nepisat.ee2.obj.gui.Container.Slots.SlotOut;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
 
public class TileEntityRenseiban  extends TileEE implements ISidedInventory{
	public static boolean kioku=false;;
	private static final int LOCK_INDEX = 9;
	private static final int[] MATTER_INDEXES = new int[] {12, 11, 13, 10, 14, 21, 15, 20, 16, 19, 17, 18};
	private static final int[] FUEL_INDEXES = new int[] {22, 23, 24, 25};
	public final LinkedList<EMCStacks> KIOKU = new LinkedList<EMCStacks>();
	private  final LinkedList<EMCStacks> KIOKU2 = new LinkedList<EMCStacks>();
	public ItemStack[] sampleItemStacks = new ItemStack[26];
	private EntityPlayer player;
	private TileEntityRenseiban par2TileEntity;
	private EMCStacks es = new EMCStacks(0,0,0);
 
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		//this.emc = par1NBTTagCompound.getInteger("strEMC");
 
	}
 
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
 
		//par1NBTTagCompound.setInteger("strEMC",emc);
		
 
	}
 
 
	@Override
	public Packet getDescriptionPacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeToNBT(nbtTagCompound);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbtTagCompound);
	}
	@Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
        this.readFromNBT(pkt.data);
    }
   
	public void checkForUpdates()
	{
		//if (this.worldObj != null){
		//}
	}
	public void KIOKU(EMCStacks stack){
		if (this.worldObj != null){
		EMCStacks copy = stack.copysize();
		if(KIOKU.indexOf(copy) == -1){
			KIOKU.add(copy);
			for(int i=10;i<22;i++){
				if(sampleItemStacks[i]==null){
					setInventorySlotContents(i,es.IteminEMC(copy));
					sampleItemStacks[i].stackSize=1;
					onInventoryChanged();
					break;
				}
			}
		}if(KIOKU2.indexOf(copy)== -1){
			GuiRenseiban.learnFlag=100;
		}else{
			GuiRenseiban.learnFlag=0;
		}
		KIOKU2.add(copy);
		return;
		}
	}
	public void Update() {
		if(sampleItemStacks[9]!=null){
    			sampleItemStacks[9] = null;
		}
	}

	// スロット数
	@Override
	public int getSizeInventory() {
		return this.sampleItemStacks.length;
	}
 
	// インベントリ内の任意のスロットにあるアイテムを取得
	@Override
	public ItemStack getStackInSlot(int par1) {
		return this.sampleItemStacks[par1];
	}
 
	@Override
	public ItemStack decrStackSize(int par1, int par2) {
		if (this.sampleItemStacks[par1] != null)
		{
			ItemStack itemstack;
 
			if (this.sampleItemStacks[par1].stackSize <= par2)
			{
				itemstack = this.sampleItemStacks[par1];
				this.sampleItemStacks[par1] = null;
				return itemstack;
			}
			else
			{
				itemstack = this.sampleItemStacks[par1].splitStack(par2);
 
				if (this.sampleItemStacks[par1].stackSize == 0)
				{
					this.sampleItemStacks[par1] = null;
				}
 
				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}
 
	@Override
	public ItemStack getStackInSlotOnClosing(int par1) {
		if (this.sampleItemStacks[par1] != null)
		{
			ItemStack itemstack = this.sampleItemStacks[par1];
			this.sampleItemStacks[par1] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}
 
	// インベントリ内のスロットにアイテムを入れる
	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
		this.sampleItemStacks[par1] = par2ItemStack;
 
		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
		{
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}
	}


	// インベントリの名前
	@Override
	public String getInvName() {
		return "Renseiban";
	}
 
	// 多言語対応かどうか
	@Override
	public boolean isInvNameLocalized() {
		return false;
	}
 
	// インベントリ内のスタック限界値
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
 
	@Override
	public void onInventoryChanged() {
		 if (this.worldObj != null)
	        {
	            this.blockMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
	            this.worldObj.markTileEntityChunkModified(this.xCoord, this.yCoord, this.zCoord, this);

	            if (this.getBlockType() != null)
	            {
	                this.worldObj.func_96440_m(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID);
	            }
	        }
	}
 
	// par1EntityPlayerがTileEntityを使えるかどうか
	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
	}
 
	@Override
	public void openChest() {}
 
	@Override
	public void closeChest() {
		setEMC(0);
	}

 
	//@Override
	//public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack) {
	//	return par1 == 2 ? false : (par1 == 1 ? this.isItemFuel(par2ItemStack) : true);
	//}
 
	//ホッパーにアイテムの受け渡しをする際の優先度
	//@Override
	//public int[] getAccessibleSlotsFromSide(int par1) {
	//	return par1 == 0 ? slots_bottom : (par1 == 1 ? slots_top : slots_sides);
	//}
 
	//ホッパーからアイテムを入れられるかどうか
	@Override
	public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3) {
		return this.isItemValidForSlot(par1, par2ItemStack);
	}
 
	//隣接するホッパーにアイテムを送れるかどうか
	@Override
	public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3) {
		return par3 != 0 || par1 != 1 || par2ItemStack.itemID == Item.bucketEmpty.itemID;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}
	public static LinkedList<ItemStack> getKnowledge(String username)
	{

		return new LinkedList<ItemStack>();
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}
