package nepisat.ee2.gameObjs.items;

import nepisat.ee2.PECore;
import nepisat.ee2.utils.AchievementHandler;
import nepisat.ee2.utils.Constants;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TransmutationTablet extends ItemPE
{
	public TransmutationTablet(int i)
	{
		super(i);
		this.setUnlocalizedName("transmutation_tablet");
		this.setMaxStackSize(1);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (!world.isRemote)
		{
			player.openGui(PECore.instance, Constants.TRANSMUTE_TABLET_GUI, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		}
		
		return stack;
	}
	
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) 
	{
		super.onCreated(stack, world, player);
		
		if (!world.isRemote)
		{
			player.addStat(AchievementHandler.PORTABLE_TRANSMUTATION, 1);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		this.itemIcon = register.registerIcon(this.getTexture("transmute_tablet"));
	}
}
