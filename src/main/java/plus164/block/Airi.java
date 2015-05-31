package plus164.block;

import cpw.mods.fml.common.network.Player;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.MouseEvent;

public class Airi extends Item {

	public Airi(int par1) {
		super(par1);
		this.setCreativeTab(CreativeTabs.tabFood);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public ItemStack onItemRightClick(ItemStack item, World w, EntityPlayer p)
	{
	//	System.out.println("X: "+p.posX);
		MovingObjectPosition mop = new MovingObjectPosition(p);
		System.out.println(mop.blockX);
		w.setBlock((int)p.posX, (int)p.prevPosY, (int)p.prevPosZ,Block.stone.blockID);
		//アイテムを右クリック時に呼ばれる
		return item;
	}
}
