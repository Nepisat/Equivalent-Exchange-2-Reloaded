package nepisat.ee2.gameObjs.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nepisat.ee2.gameObjs.ObjHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;

import java.util.List;

public class FuelBlock extends Block 
{
	@SideOnly(Side.CLIENT)
	private Icon icons[];
	
	public FuelBlock(int i) 
	{
		super(i,Material.rock);
		this.setUnlocalizedName("pe_fuel_block");
		this.setCreativeTab(ObjHandler.cTab);
		this.setHardness(0.5f);
		
	}
	
	@Override
	public int damageDropped(int meta)
	{
		return meta;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int item, CreativeTabs cTab, List list)
	{
		for (int i = 0; i < 3; i++)
		{
			list.add(new ItemStack(item , 1, i));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		icons = new Icon[3];
		
		for (int i = 0; i < 3; i++)
		{
			icons[i] = register.registerIcon("projecte:fuels_"+i);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta)
	{
		return icons[MathHelper.clamp_int(meta, 0, 2)];
	}
}
