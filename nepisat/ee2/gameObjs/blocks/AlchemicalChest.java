package nepisat.ee2.gameObjs.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nepisat.ee2.PECore;
import nepisat.ee2.gameObjs.ObjHandler;
import nepisat.ee2.gameObjs.tiles.AlchChestTile;
import nepisat.ee2.utils.Constants;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

public class AlchemicalChest extends BlockDirection implements ITileEntityProvider
{
	private Random rand = new Random();
	
	public AlchemicalChest(int i) 
	{
		super(i,Material.rock);
		this.setUnlocalizedName("pe_alchemy_chest");
		this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
		this.setHardness(10.0f);
		
	}
	
	@Override
	public int idDropped(int par1, Random random, int par2)
	{
		return ObjHandler.alchChest.blockID;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public int getRenderType()
	{
		return Constants.CHEST_RENDER_ID;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote) 
		{
			player.openGui(PECore.instance, Constants.ALCH_CHEST_GUI, world, x, y, z);
		}
		
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) 
	{
		return new AlchChestTile();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		this.blockIcon = register.registerIcon("obsidian");
	}

	
}
