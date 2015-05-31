package nepisat.ee2.gameObjs.blocks;

import java.util.Random;

import nepisat.ee2.gameObjs.ObjHandler;
import nepisat.ee2.gameObjs.tiles.TileEmc;
import nepisat.ee2.gameObjs.tiles.TileEmcDirection;
import nepisat.ee2.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public abstract class BlockDirection extends BlockContainer
{
	private Random rand = new Random();
	
	public BlockDirection(int i,Material material) 
	{
		super(i,material);
		this.setCreativeTab(ObjHandler.cTab);
		
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack stack)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		
		if (stack.hasTagCompound() && stack.stackTagCompound.getBoolean("ProjectEBlock") && tile instanceof TileEmc)
		{
			stack.stackTagCompound.setInteger("x", x);
			stack.stackTagCompound.setInteger("y", y);
			stack.stackTagCompound.setInteger("z", z);
			
			tile.readFromNBT(stack.stackTagCompound);
		}
		
		if (tile instanceof TileEmcDirection)
		{
			((TileEmcDirection) tile).setRelativeOrientation(entityLiving, false);
		}
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int block, int noclue)
	{
		IInventory tile = (IInventory) world.getBlockTileEntity(x, y, z);
		
		if (tile == null)
		{
			return;
		}
		
		for (int i = 0; i < tile.getSizeInventory(); i++)
		{
			ItemStack stack = tile.getStackInSlot(i);
			
			if (stack == null)
			{
				continue;
			}
			
			Utils.spawnEntityItem(world, stack, x, y, z);
		}
		
		world.func_96440_m(x, y, z, block);
		super.breakBlock(world, x, y, z, block, noclue);
	}
	
	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) 
	{
		if (world.isRemote)
		{
			return;
		}
		
		ItemStack stack = player.getHeldItem();
		
		if (stack != null && stack.getItem() == ObjHandler.philosStone)
		{
			TileEntity tile = world.getBlockTileEntity(x, y, z);
			
			if (tile instanceof TileEmcDirection)
			{
				((TileEmcDirection) tile).setRelativeOrientation(player, true);
			}
			else
			{
				int orientation = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
				
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
			}
		}
	}
}