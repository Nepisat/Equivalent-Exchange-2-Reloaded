package nepisat.ee2.gameObjs.items.rings;

import nepisat.ee2.gameObjs.items.ItemPE;
import net.minecraft.client.renderer.texture.IconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IronBand extends ItemPE
{
	public IronBand(int i)
	{
		super(i);
		this.setUnlocalizedName("ring_iron_band");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		this.itemIcon = register.registerIcon(this.getTexture("rings", "iron_band"));//"ee2:rings/iron_band");
	}
}
