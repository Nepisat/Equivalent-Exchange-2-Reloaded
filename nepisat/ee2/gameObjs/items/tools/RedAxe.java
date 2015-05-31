package nepisat.ee2.gameObjs.items.tools;

import java.util.ArrayList;
import java.util.List;

import nepisat.ee2.gameObjs.entity.EntityLootBall;
import nepisat.ee2.gameObjs.items.ItemCharge;
import nepisat.ee2.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RedAxe extends ItemCharge
{
	public RedAxe(int i)
	{
		super(i,"rm_axe", (byte) 4);
	}
	
	@Override
	public boolean canHarvestBlock(Block block, ItemStack stack)
	{
		return block.blockMaterial == Material.wood || block.blockMaterial == Material.plants || block.blockMaterial == Material.vine;
	}
	
	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass) 
	{
		if (toolClass.equals("axe"))
		{
			return 4;
		}
		
		return -1;
	}
	
	@Override
	public float getDigSpeed(ItemStack stack, Block block, int metadata)
	{
		if (canHarvestBlock(block, stack) || ForgeHooks.canToolHarvestBlock(block, metadata, stack))
		{
			return 16.0f + (14.0f * this.getCharge(stack));
		}
		
		return 1.0F;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (!world.isRemote)
		{
			byte charge = this.getCharge(stack);
			
			if (charge == 0)
			{
				return stack;
			}
			
			List<ItemStack> drops = new ArrayList();
			
			for (int x = (int) player.posX - (5 * charge); x <= player.posX + (5 * charge); x++)
				for (int y = (int) player.posY - (10 * charge); y <= player.posY + (10 * charge); y++)
					for (int z = (int) player.posZ - (5 * charge); z <= player.posZ + (5 * charge); z++)
					{
						Block block = Block.blocksList[world.getBlockId(x, y, z)];
						
						if (block.blockID==0)
						{
							continue;
						}
						
						ItemStack s = new ItemStack(block);
						int oreIds = OreDictionary.getOreID(s);
						
						if (oreIds == 0)
						{
							continue;
						}
						
						String oreName = OreDictionary.getOreName(oreIds);
						
						if (oreName.equals("logWood") || oreName.equals("treeLeaves"))
						{
							ArrayList<ItemStack> blockDrops = Utils.getBlockDrops(world, player, block, stack, x, y, z);
						
							if (!blockDrops.isEmpty())
							{
								drops.addAll(blockDrops);
							}
						
							world.setBlockToAir(x, y, z);
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
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		this.itemIcon = register.registerIcon(this.getTexture("rm_tools", "axe"));
	} 
}
