package nepisat.ee2.gameObjs.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nepisat.ee2.PECore;
import nepisat.ee2.gameObjs.ObjHandler;
import nepisat.ee2.gameObjs.tiles.CondenserMK2Tile;
import nepisat.ee2.utils.Constants;
import nepisat.ee2.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public class CondenserMK2 extends AlchemicalChest implements ITileEntityProvider
{
	public CondenserMK2(int i)
	{
		super(i);
		this.setUnlocalizedName("pe_condenser_mk2");
	}

	@Override
	public int idDropped(int par1, Random random, int par2)
	{
		return ObjHandler.condenserMk2.blockID;
	}

	@Override
	public int getRenderType()
	{
		return Constants.CONDENSER_MK2_RENDER_ID;
	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new CondenserMK2Tile();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			player.openGui(PECore.instance, Constants.CONDENSER_MK2_GUI, world, x, y, z);
		}

		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int block, int noclue)
	{
		IInventory tile = (IInventory) world.getBlockTileEntity(x, y, z);

		if (tile == null)
		{
			return;
		}

		for (int i = 1; i < tile.getSizeInventory(); i++)
		{
			ItemStack stack = tile.getStackInSlot(i);

			if (stack == null)
			{
				continue;
			}

			Utils.spawnEntityItem(world, stack, x, y, z);
		}

		world.func_96440_m(x, y, z, block);
		world.removeBlockTileEntity(x, y, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		this.blockIcon = register.registerIcon("obsidian");
	}
}
