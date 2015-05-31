package plus164.block;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class Cglass extends Block{
	private Icon[] icons;
	private boolean localFlag=false;

	public Cglass(int i,Material m) {
		super(i,m);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setHardness(0.3F);
	
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2) {
		
		switch(par2)
		{
		case 0:
			return icons[0];
		case 1:
			return icons[1];
		case 2:
			return icons[2];
		case 3:
			return icons[3];
		case 4:
			return icons[4];
		case 5:
			return icons[5];
		case 6:
			return icons[6];
		case 7:
			return icons[7];
		case 8:
			return icons[8];
		case 9:
			return icons[9];
		case 10:
			return icons[10];
		case 11:
			return icons[11];
		case 12:
			return icons[12];
		case 13:
			return icons[13];
		case 14:
			return icons[14];
		case 15:
			return icons[15];
			
		default:
			return icons[0];
		}
	}
	
	 public boolean isOpaqueCube()
	    {
	        return false;
	    }

    public int getRenderBlockPass()
    {
        return 1;
    }
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        int var6 = par1IBlockAccess.getBlockId(par2, par3, par4);
        return !this.localFlag && var6 == this.blockID ? false : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
    }
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
	 @Override
       @SideOnly(Side.CLIENT)
        public void registerIcons(IconRegister par1IconRegister) {
                //郢晢ｿｽ縺醍ｹｧ?ｹ郢昶?ﾎ慕ｸｺ?ｮ郢昜ｻ｣縺幄ｬ厄ｿｽ?ｮ螢ｹ?ｽ
                //郢晢ｽ｡郢ｧ?ｿ郢晢ｿｽ?ｽ郢ｧ?ｿ邵ｺ?ｯ0邵ｺ荵晢ｽ?邵ｺ?ｧ邵ｺ?､邵ｺ?ｾ郢ｧ?ｽ隴幢ｽｪ雋?ｿｽ
                //src/minecraft/assets/samplemod/items/itemsample_(郢晢ｽ｡郢ｧ?ｿ郢晢ｿｽ?ｽ郢ｧ?ｿ).png
        this.icons= new Icon[16];
 
        for (int i = 0; i < this.icons.length; ++i)
        {
            this.icons[i] = par1IconRegister.registerIcon("plus164:glass/glass_" + i);
        }
    }
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		// 郢晢ｽ｡郢ｧ?ｿ郢晢ｿｽ?ｽ郢ｧ?ｿ邵ｺ?ｧ郢晄じﾎ溽ｹ晢ｿｽ縺醍ｹｧ螳夲ｽｿ?ｽ陷会ｿｽ笘?ｹｧ?ｽ
		// par3List.add(new ItemStack(par1, 1, <郢晢ｽ｡郢ｧ?ｿ郢晢ｿｽ?ｽ郢ｧ?ｿ>));
		for (int i = 0; i < 16; i++) {
			par3List.add(new ItemStack(par1, 1, i));
		}
	}
}
