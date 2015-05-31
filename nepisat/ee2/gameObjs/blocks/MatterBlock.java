package nepisat.ee2.gameObjs.blocks;

import java.util.List;

import nepisat.ee2.gameObjs.ObjHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MatterBlock extends Block
{
	@SideOnly(Side.CLIENT)
	private Icon dmIcon;
	@SideOnly(Side.CLIENT)
	private Icon rmIcon;
	
	public MatterBlock(int i) 
	{
		super(i,Material.iron);
		this.setCreativeTab(ObjHandler.cTab);
	}
	
	@Override
	public float getBlockHardness(World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		
		if (meta == 0) 
		{
			return 1000000.0F;
		}
		else
		{
			return 2000000.0F;
		}
	}
	
	@Override
	public boolean canHarvestBlock(EntityPlayer player, int meta)
	{
		ItemStack stack = player.getHeldItem();
		
		if (stack != null)
		{
			if (meta == 1)
			{
				return stack.getItem() == ObjHandler.rmPick;
			}
			else
			{
				return stack.getItem() == ObjHandler.rmPick || stack.getItem() == ObjHandler.dmPick;
			}
		}
		
		return false;
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
		for (int i = 0; i <= 1; i++)
		{
			list.add(new ItemStack(item , 1, i));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		dmIcon = register.registerIcon("projecte:dm");
		rmIcon = register.registerIcon("projecte:rm");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta)
	{
		if (meta == 0) 
		{
			return dmIcon;
		}
		else return rmIcon;
	}
	
}
