package nepisat.ee2.gameObjs.items.tools;

import nepisat.ee2.gameObjs.items.ItemCharge;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DarkSword extends ItemCharge 
{
	public DarkSword(int i) 
	{
		super(i,"dm_sword", (byte) 3);
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase damaged, EntityLivingBase damager)
	{
		if (!(damager instanceof EntityPlayer))
		{
			return false;
		}
		
		DamageSourceA dmg = (DamageSourceA) DamageSourceA.causePlayerDamage((EntityPlayer) damager);
		
		byte charge = this.getCharge(stack);
		float totalDmg = 10.0f;
		
		if (charge > 0)
		{
			
			dmg.callsetDamageBypassesArmor();
			totalDmg += charge;
		}
		
		damaged.attackEntityFrom(dmg, totalDmg);
		return true;
	}

	@Override
	public float getStrVsBlock(ItemStack p_150893_1_, Block p_150893_2_)
	{
		if (p_150893_2_ == Block.web)
		{
			return 15.0F;
		}
		else
		{
			Material material = p_150893_2_.blockMaterial;
			return material != Material.plants && material != Material.vine && material != Material.coral && material != Material.leaves && material != Material.ground ? 1.0F : 1.5F;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
	{
		return EnumAction.block;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		return 72000;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
	}

	@Override
	public boolean canHarvestBlock(Block p_150897_1_)
	{
		return p_150897_1_ == Block.web;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		this.itemIcon = register.registerIcon(this.getTexture("dm_tools", "sword"));
	}
}
