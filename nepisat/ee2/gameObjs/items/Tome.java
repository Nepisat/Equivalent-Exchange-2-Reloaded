package nepisat.ee2.gameObjs.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nepisat.ee2.gameObjs.ObjHandler;
import nepisat.ee2.utils.AchievementHandler;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class Tome extends ItemPE
{
	public Tome(int i)
	{
		super(i);
		this.setUnlocalizedName("tome");
		this.setCreativeTab(ObjHandler.cTab);
		this.setMaxStackSize(1);
		this.setMaxDamage(0);
	}

	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemstack)
	{
		return false; 
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) 
	{
		super.onCreated(stack, world, player);
		
		if (!world.isRemote)
		{
			player.addStat(AchievementHandler.TOME, 1);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		this.itemIcon = register.registerIcon(this.getTexture("tome"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4)
	{
		list.add("Unlocks all items with an EMC value in the tile.");
	}
}





