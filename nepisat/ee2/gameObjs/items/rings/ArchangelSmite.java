package nepisat.ee2.gameObjs.items.rings;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nepisat.ee2.gameObjs.entity.EntityHomingArrow;
import nepisat.ee2.gameObjs.items.ItemPE;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ArchangelSmite extends ItemPE
{
	public ArchangelSmite(int i)
	{
		super(i);
		this.setUnlocalizedName("archangel_smite");
		this.setMaxStackSize(1);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		EntityHomingArrow arrow = new EntityHomingArrow(world, player, 2.0F);

		if (!world.isRemote)
		{
			world.spawnEntityInWorld(arrow);
		}

		return stack;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		this.itemIcon = register.registerIcon(this.getTexture("rings", "archangel_smite"));
	}
}
