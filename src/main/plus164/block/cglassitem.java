package block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class cglassitem extends ItemBlock{
  
		public cglassitem(int par1) {
			super(par1);
			this.setMaxDamage(0);
			this.setHasSubtypes(true);
		}
	 
		@Override
		public int getMetadata(int par1) {
			return par1;
		}
	 
		@Override
		public String getUnlocalizedName(ItemStack par1ItemStack) {
			return super.getUnlocalizedName() + "_" + par1ItemStack.getItemDamage();
		}
		 public boolean isOpaqueCube()
		    {
		        return false;
		    }

	    public int getRenderBlockPass()
	    {
	        return 1;
	    }
	    public boolean renderAsNormalBlock()
	    {
	        return false;
	    }
	 
}
