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
			this.setCreativeTab(EE2.CreativeTabEE2);	//�N���G�C�e�B�u�̃^�u
			this.setUnlocalizedName("Kenzyanoisi");	//�V�X�e�����̓o�^
			this.setTextureName("ee2:kenzyanoisi");	//�e�N�X�`���̎w��
			this.setMaxStackSize(1);	//�X�^�b�N�ł����
		}
	 
		/*
		 * �ȉ����܂�
		 * ���ɋ@�\���Ȃ���Ώ����Ă�������
		 */
	 
		@Override
		public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float disX, float disY, float disZ)
		{
			//�A�C�e�����u���b�N�ɑ΂��ĉE�N���b�N�������ɌĂ΂��
			return false;
		}
	 
		@Override
		public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player)
		{
			//�A�C�e�����E�N���b�N���ɌĂ΂��
			return item;
		}
	
		
	}

