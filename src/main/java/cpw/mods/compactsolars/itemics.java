package cpw.mods.compactsolars;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class itemics extends Item {
	   @SideOnly(Side.CLIENT)
       private Icon[] icon;
	   private int count=1;
	public itemics(int id) {
		super(id);
		this.setCreativeTab(ic2.core.IC2.tabIC2);
		this.setMaxDamage(0);
		this.setUnlocalizedName("CraftingIC2ITEMinCPS");
		this.setMaxStackSize(64);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1) {
		return icon[par1 % icon.length];
	}
	@Override
	public int getMetadata(int par1) {
		return par1;
	}
	

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		//名前の登録
		return this.getUnlocalizedName() + "_" + par1ItemStack.getItemDamage();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		CompactSolars c = new CompactSolars();
		for (int i=0; i< c.names.length; i++){
		par3List.add(new ItemStack(this, 1, i));
		}
	}
	  @Override
      @SideOnly(Side.CLIENT)
      public void registerIcons(IconRegister par1IconRegister) {
		  CompactSolars c = new CompactSolars();
		  this.icon = new Icon[c.names.length];

      for (int i = 0; i < this.icon.length; ++i)
      {
          this.icon[i] = par1IconRegister.registerIcon("compactsolars:matter_" + i);
      }
}
}
