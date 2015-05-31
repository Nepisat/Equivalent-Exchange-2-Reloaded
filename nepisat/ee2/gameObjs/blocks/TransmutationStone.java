package nepisat.ee2.gameObjs.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nepisat.ee2.PECore;
import nepisat.ee2.gameObjs.ObjHandler;
import nepisat.ee2.gameObjs.tiles.TileEmc;
import nepisat.ee2.gameObjs.tiles.TransmuteTile;
import nepisat.ee2.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import java.util.Random;

public class TransmutationStone extends Block implements ITileEntityProvider
{
	@SideOnly(Side.CLIENT)
	private Icon[] icon;
	
	public TransmutationStone(int i) 
	{
		super(i,Material.rock);
		this.setCreativeTab(ObjHandler.cTab);
		this.setUnlocalizedName("pe_transmutation_stone");
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
		this.setHardness(10.0f);
	}
	
	@Override
	public int idDropped(int par1, Random random, int par2)
	{
		return ObjHandler.transmuteStone.blockID;
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1) 
	{
		return new TransmuteTile();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		TransmuteTile tile = (TransmuteTile) world.getBlockTileEntity(x, y, z);
			
		//if (!tile.isUsed())
		//{
			tile.setPlayer(player);
			
			if (!world.isRemote)
			{
				player.openGui(PECore.MODID, Constants.TRANSMUTE_STONE_GUI, world, x, y, z);
			}
		//}
		//else
		//{
			//if (!world.isRemote)
			//{
			//	player.addChatMessage("Someone is already using this transmutation stone!");
			//}
		//}
		return true;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entLiving, ItemStack stack)
	{
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
	public Icon getIcon(int side, int meta)
	{
		if (side < 2)
		{
			return icon[side];
		}
		return icon[2];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		icon = new Icon[3];
		icon[0] = register.registerIcon("projecte:transmutation_stone/bottom");
		icon[1] = register.registerIcon("projecte:transmutation_stone/top");
		icon[2] = register.registerIcon("projecte:transmutation_stone/side");
	}

}
