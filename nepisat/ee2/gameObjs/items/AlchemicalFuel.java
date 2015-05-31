package nepisat.ee2.gameObjs.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AlchemicalFuel extends ItemPE
{
	private final String[] names = new String[] {"alchemical_coal", "mobius", "aeternalis"};
	@SideOnly(Side.CLIENT)
	private Icon[] icons;
	
	public AlchemicalFuel(int i)
	{
		super(i);
		this.setUnlocalizedName("fuel");
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{	
		return super.getUnlocalizedName()+ "_"+names[stack.getItemDamage()];
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs cTab, List list)
	{
		for (int i = 0; i < 3; ++i)
			list.add(new ItemStack(item, 1, i));
	}
	
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return icons[MathHelper.clamp_int(par1, 0, 3)];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		icons = new Icon[3];
		for (int i = 0; i < 3; i++)
			icons[i] = register.registerIcon(this.getTexture("fuels", names[i]));
	}
}
