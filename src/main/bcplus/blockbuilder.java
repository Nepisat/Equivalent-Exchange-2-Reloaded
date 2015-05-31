package bcplus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import buildcraft.api.gates.IAction;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.core.IBuilderInventory;
import buildcraft.core.IMachine;
import buildcraft.core.TileBuildCraft;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class blockbuilder extends Block implements IBuilderInventory, IPowerReceptor, IMachine{

	public blockbuilder(int par1, Material par2Material) {
		super(par1, par2Material);// TODO 自動生成されたコンストラクター・スタブ
		this.setCreativeTab(CreativeTabs.tabFood);
	}
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer player, int side, float disX, float disY, float disZ)
	{
		int px = x;
        int py = y;
        int pz = z;
			
			File file = new File("BC_BuilderTXT\\newFile2.txt");
			try {
				
				FileInputStream input = new FileInputStream(file);

				BufferedReader reader = new BufferedReader((new InputStreamReader(input, "8859_1")));
				
			
					
					String id;
					String meta;
					int counter=0;
					for (int i=0;i<=16*16*256; i++){
						System.out.println(counter);
						if(px==x+16){
							px=x;
							pz++;
						}else{
						
							id = reader.readLine();
							meta = reader.readLine();
							
							if(id==null){
								break;
							}
							if(meta==null){
								break;
							}
							w.setBlock(px, py, pz, Integer.parseInt(id),Integer.parseInt(meta),3);
							px++;
							
							//System.out.println(Integer.parseInt(id)+":"+Integer.parseInt(meta));
						//	w.setBlock(px, py, pz,Integer.parseInt(reader.readLine()));
							
						}
						if(pz==z+16){
							pz=z;
							py--;
						}
						if(py<=0){
							
							break;
						}
						
							
						
					}
					reader.close();
					
				
			} catch (IOException e1) {

				System.out.println(e1);
			}
		
		
		//ブロックが右クリックされた時
		return false;
	}
	@Override
	public int getSizeInventory() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}
	@Override
	public ItemStack getStackInSlot(int var1) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	@Override
	public String getInvName() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
	@Override
	public int getInventoryStackLimit() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}
	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
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
	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}
	@Override
	public boolean isActive() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}
	@Override
	public boolean manageFluids() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}
	@Override
	public boolean manageSolids() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}
	@Override
	public boolean allowAction(IAction action) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}
	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
	@Override
	public void doWork(PowerHandler workProvider) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	@Override
	public boolean isBuildingMaterial(int i) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
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
	public World getWorld() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
 
}
