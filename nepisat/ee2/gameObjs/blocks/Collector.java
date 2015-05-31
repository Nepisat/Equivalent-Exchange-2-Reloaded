package nepisat.ee2.gameObjs.blocks;

import nepisat.ee2.PECore;
import nepisat.ee2.gameObjs.tiles.CollectorMK1Tile;
import nepisat.ee2.gameObjs.tiles.CollectorMK2Tile;
import nepisat.ee2.gameObjs.tiles.CollectorMK3Tile;
import nepisat.ee2.gameObjs.tiles.TileEmc;
import nepisat.ee2.utils.Constants;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Collector extends BlockDirection
{
	@SideOnly(Side.CLIENT)
	private Icon front;
	@SideOnly(Side.CLIENT)
	private Icon top;
	private int tier;
	
	public Collector(int i,int tier) 
	{
		super(i,Material.glass);
		this.setUnlocalizedName("pe_collector_MK" + tier);
		this.setLightValue(Constants.COLLECTOR_LIGHT_VALS[tier - 1]);
		this.setHardness(0.3f);
		this.tier = tier;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
			switch (tier)
			{
				case 1:
					player.openGui(PECore.instance, Constants.COLLECTOR1_GUI, world, x, y, z);
					break;
				case 2:
					player.openGui(PECore.instance, Constants.COLLECTOR2_GUI, world, x, y, z);
					break;
				case 3:
					player.openGui(PECore.instance, Constants.COLLECTOR3_GUI, world, x, y, z);
					break;
			}
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entLiving, ItemStack stack)
	{
		int orientation = MathHelper.floor_double((double)(entLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		
		if (orientation == 0)
		{
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		}

		if (orientation == 1)
		{
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		}

		if (orientation == 2)
		{
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		}

		if (orientation == 3)
		{
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}
		
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		
		if (stack.hasTagCompound() && stack.stackTagCompound.getBoolean("ProjectEBlock") && tile instanceof TileEmc)
		{
			stack.stackTagCompound.setInteger("x", x);
			stack.stackTagCompound.setInteger("y", y);
			stack.stackTagCompound.setInteger("z", z);
			
			tile.readFromNBT(stack.stackTagCompound);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		this.blockIcon = register.registerIcon("projecte:collectors/other");
		this.front = register.registerIcon("projecte:collectors/front");
		this.top = register.registerIcon("projecte:collectors/top_"+Integer.toString(tier));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta)
	{
		if (meta == 0 && side == 3) 
		{
			return front;
		}
		
		if (side == 1) 
		{
			return top;
		}
		
		return side != meta ? this.blockIcon : front;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) 
	{
		if (tier == 1) 
		{
			return new CollectorMK1Tile();
		}
		
		if (tier == 2)
		{
			return new CollectorMK2Tile();
		}
		
		if (tier == 3)
		{
			return new CollectorMK3Tile();
		}
		
		return null;
	}

	
}
