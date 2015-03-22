package nepisat.ee2.obj.item;
	import java.util.List;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nepisat.ee2.Core.EE2;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
	 
	public class ItemKenzyanoisi extends Item
	{
	 
		public ItemKenzyanoisi(int par1)
		{
			super(par1);
			this.setCreativeTab(EE2.CreativeTabEE2);	//クリエイティブのタブ
			this.setUnlocalizedName("Kenzyanoisi");	//システム名の登録
			this.setTextureName("ee2:kenzyanoisi");	//テクスチャの指定
			this.setMaxStackSize(1);	//スタックできる量
		}
	 
		/*
		 * 以下おまけ
		 * 特に機能がなければ消してください
		 */
	 
		@Override
		public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float disX, float disY, float disZ)
		{
			//アイテムをブロックに対して右クリックした時に呼ばれる
			return false;
		}
	 
		@Override
		public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player)
		{
			//アイテムを右クリック時に呼ばれる
			return item;
		}
	
		
	}

