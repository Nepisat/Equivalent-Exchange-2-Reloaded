package nepisat.ee2.gameObjs.items.itemEntities;

import nepisat.ee2.gameObjs.items.ItemPE;
import net.minecraft.client.renderer.texture.IconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LavaOrb extends ItemPE
{
	public LavaOrb(int i)
	{
		super(i);
		this.setUnlocalizedName("lava_orb");
		this.setMaxStackSize(1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		this.itemIcon = register.registerIcon(this.getTexture("entities", "lava_orb"));
	}
}
