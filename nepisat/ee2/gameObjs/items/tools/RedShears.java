package nepisat.ee2.gameObjs.items.tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nepisat.ee2.gameObjs.entity.EntityLootBall;
import nepisat.ee2.gameObjs.items.ItemCharge;
import nepisat.ee2.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RedShears extends ItemCharge
{
	public RedShears(int i)
	{
		super(i,"rm_shears", (byte) 4);
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, int block, int x, int y, int z, EntityLivingBase ent)
	{
		if (Block.blocksList[block].blockMaterial != Material.leaves && Block.blocksList[block] != Block.web && Block.blocksList[block] != Block.vine && Block.blocksList[block] != Block.tripWire && !(Block.blocksList[block] instanceof IShearable))
		{
			return super.onBlockDestroyed(stack, world, block, x, y, z, ent);
		}
		else
		{
			return true;
		}
	}
	
	@Override
	public boolean canHarvestBlock(Block block, ItemStack stack) 
	{
		return block == Block.web || block == Block.redstoneWire || block == Block.tripWire;
	}
	
	@Override
	public float getDigSpeed(ItemStack stack, Block block, int metadata) 
	{
		if (canHarvestBlock(block, stack))
		{
			return 14.0f + (12.0f * this.getCharge(stack));
		}
		
		return 1.0f;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (!world.isRemote)
		{
			byte charge = this.getCharge(stack);
			
			int offset = 0;
			
			switch (charge)
			{
				case 0:
					offset = 4;
					break;
				case 1:
					offset = 8;
					break;
				case 2:
					offset = 16;
					break;
				case 3:
					offset = 32;
					break;
			}
			
			AxisAlignedBB bBox = player.boundingBox.expand(offset, offset / 2, offset);
			List<Entity> list = world.getEntitiesWithinAABB(IShearable.class, bBox);
			
			if (list.isEmpty())
			{
				return stack;
			}
			
			List<ItemStack> drops = new ArrayList<ItemStack>();
			
			for (Entity ent : list)
			{
				IShearable target = (IShearable) ent;
				
				if (target.isShearable(stack, ent.worldObj, (int) ent.posX, (int) ent.posY, (int) ent.posZ))
				{
					ArrayList<ItemStack> entDrops = target.onSheared(stack, ent.worldObj, (int) ent.posX, (int) ent.posY, (int) ent.posZ, EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack));
					
					if (!entDrops.isEmpty())
					{
						for (ItemStack drop : entDrops)
						{
							drop.stackSize = MathHelper.clamp_int(drop.stackSize + Utils.randomIntInRange(6, 3), 0, 64);
						}
						
						drops.addAll(entDrops);
					}
				}
			}
			
			if (!drops.isEmpty())
			{
				world.spawnEntityInWorld(new EntityLootBall(world, drops, player.posX, player.posY, player.posZ));
				//PacketHandler.sendTo(new SwingItemPKT(), (EntityPlayerMP) player);
			}
		}
		
		return stack;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, EntityPlayer player)
	{
		if (player.worldObj.isRemote)
		{
			return false;
		}
		
		Block block = Block.blocksList[player.worldObj.getBlockId(x, y, z)];
		
		if (block instanceof IShearable)
		{
			IShearable target = (IShearable) block;
			
			if (target.isShearable(itemstack, player.worldObj, x, y, z))
			{
				ArrayList<ItemStack> drops = target.onSheared(itemstack, player.worldObj, x, y, z, EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemstack));
				Random rand = new Random();

				for(ItemStack stack : drops)
				{
					float f = 0.7F;
					double d = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
					double d1 = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
					double d2 = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
					EntityItem entityitem = new EntityItem(player.worldObj, (double)x + d, (double)y + d1, (double)z + d2, stack);
					entityitem.delayBeforeCanPickup = 10;
					player.worldObj.spawnEntityInWorld(entityitem);
				}

				itemstack.damageItem(1, player);
				player.addStat(StatList.mineBlockStatArray[block.blockID], 1);
			}
		}
	
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		this.itemIcon = register.registerIcon(this.getTexture("rm_tools", "shears"));
	}
}
