package nepisat.ee2.gameObjs.items.itemEntities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nepisat.ee2.gameObjs.items.ItemPE;
import net.minecraft.client.renderer.texture.IconRegister;


public class WaterOrb extends ItemPE
{
	public WaterOrb(int i)
	{
		super(i);
		this.setUnlocalizedName("water_orb");
		this.setMaxStackSize(1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		this.itemIcon = register.registerIcon(this.getTexture("entities", "water_orb"));
	}
}
