package nepisat.ee2.obj.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nepisat.ee2.Core.EE2;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemGreenDust extends ItemEE {
	   @SideOnly(Side.CLIENT)
       private Icon[] icon;

	public ItemGreenDust(int id) {
		super(id);
		//this.setUnlocalizedName("GreenDust");	//システム名の登録
		this.setMaxDamage(0);
	//	this.setTextureName("ee2:covalence_dust/low");	//テクスチャの指定
		this.setMaxStackSize(64);	//スタックできる量
		// TODO 自動生成されたコンストラクター・スタブ
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
		return ItemName(par1ItemStack.getItemDamage());
	}
	private String ItemName(int i){
		String a = "共有結合粉(Low)";
		String b = "共有結合粉(Middle)";
		String c= "共有結合粉(High)";
		if (i==0){
			return a;
		}
		if(i==1){
			return b;
		}
		if(i==2){
			return c;
		}
		return "";

	}
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		//メタデータでアイテムを追加する
		//par3List.add(new ItemStack(this, 1, <メタデータ>));
		par3List.add(new ItemStack(this, 1, 0));
		par3List.add(new ItemStack(this, 1, 1));
		par3List.add(new ItemStack(this, 1, 2));
	}
	  @Override
      @SideOnly(Side.CLIENT)
      public void registerIcons(IconRegister par1IconRegister) {
              //テクスチャのパス指定。
              //メタデータは0から2でつまり3未満
              //src/minecraft/assets/samplemod/items/itemsample_(メタデータ).png
      this.icon = new Icon[3];

      for (int i = 0; i < this.icon.length; ++i)
      {
          this.icon[i] = par1IconRegister.registerIcon("ee2:covalence_dust/dust" + i);
      }
}
}
