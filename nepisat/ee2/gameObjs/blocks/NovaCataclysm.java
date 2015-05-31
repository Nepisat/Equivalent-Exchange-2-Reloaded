package nepisat.ee2.gameObjs.blocks;

import nepisat.ee2.gameObjs.ObjHandler;
import nepisat.ee2.gameObjs.entity.EntityNovaCataclysmPrimed;
import net.minecraft.block.BlockTNT;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Icon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class NovaCataclysm extends BlockTNT
{
	@SideOnly(Side.CLIENT)
	private Icon topIcon;
	@SideOnly(Side.CLIENT)
	private Icon bottomIcon;
	
	public NovaCataclysm(int i)
	{
		super(i);
		this.setUnlocalizedName("pe_nova_cataclysm");
		this.setCreativeTab(ObjHandler.cTab);
	}
	
	@Override
	public void primeTnt(World world, int x, int y, int z, int par5, EntityLivingBase entity)
	{
		if (world.isRemote || par5 != 1) return;
		
		if (entity == null)
			entity = world.getClosestPlayer(x, y, z, 64);
		
		EntityNovaCataclysmPrimed ent = new EntityNovaCataclysmPrimed(world, (double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), entity); 
		world.spawnEntityInWorld(ent);
		world.playSoundAtEntity(ent, "game.tnt.primed", 1.0F, 1.0F);
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion)
	{
		primeTnt(world, x, y, z, 1, null);
	}
	
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int p_149691_1_, int p_149691_2_)
	{
		return p_149691_1_ == 0 ? bottomIcon : (p_149691_1_ == 1 ? topIcon : this.blockIcon);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		this.blockIcon = register.registerIcon("projecte:explosives/nova1_side");
		topIcon = register.registerIcon("projecte:explosives/top");
		bottomIcon = register.registerIcon("projecte:explosives/bottom");
	}
}
