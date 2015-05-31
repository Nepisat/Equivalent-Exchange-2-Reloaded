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

public class RedSword extends ItemCharge 
{
	public RedSword(int i) 
	{
		super(i,"rm_sword", (byte) 4);
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
		float totalDmg = 15.0f;
		
		if (charge > 0)
		{
			dmg.callsetDamageBypassesArmor();
			totalDmg += charge;
		}
		
		damaged.attackEntityFrom(dmg, totalDmg);
		return true;
	}

	@Override
	public float getStrVsBlock(ItemStack stack, Block block)
	{
		if (block == Block.web)
		{
			return 15.0F;
		}
		else
		{
			Material material = block.blockMaterial;
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
		this.itemIcon = register.registerIcon(this.getTexture("rm_tools", "sword"));
	}
}
