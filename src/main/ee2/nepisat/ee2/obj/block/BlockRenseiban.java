package nepisat.ee2.obj.block;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nepisat.ee2.Core.EE2;
import nepisat.ee2.obj.tileentity.TileEntityRenseiban;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockRenseiban extends BlockContainer {
	private Icon[] icon;
	public BlockRenseiban(int par1, Material par2Material) {
		super(par1, par2Material);
		this.setUnlocalizedName("EE2_Renseiban");
		this.setCreativeTab(EE2.CreativeTabEE2);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
		this.setHardness(10.0f);
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
	//右クリックされた時の処理
	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){
		if (par1World.isRemote)
		{
			return true;
		}
		else
		{
			// GUIを開く
			par5EntityPlayer.openGui(EE2.instance, EE2.instance.RenseibanGuiID, par1World, par2, par3, par4);
			return true;
		}
	}
 

	@Override
	public TileEntity createNewTileEntity(World world) {
		// TileEntityの生成
		return new TileEntityRenseiban();
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
		icon[0] = register.registerIcon("ee2:transtone/bottom");
		icon[1] = register.registerIcon("ee2:transtone/top");
		icon[2] = register.registerIcon("ee2:transtone/side");
	}
}
