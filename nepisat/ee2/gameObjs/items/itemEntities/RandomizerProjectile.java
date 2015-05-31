package nepisat.ee2.gameObjs.items.itemEntities;


import nepisat.ee2.gameObjs.items.ItemPE;
import net.minecraft.client.renderer.texture.IconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RandomizerProjectile extends ItemPE
{
	public RandomizerProjectile(int i)
	{
		super(i);
		this.setUnlocalizedName("randomizer");
		this.setMaxStackSize(1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		this.itemIcon = register.registerIcon(this.getTexture("entities", "randomizer"));
	}
}
