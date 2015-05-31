package nepisat.ee2.gameObjs.items.tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nepisat.ee2.gameObjs.entity.EntityLootBall;
import nepisat.ee2.gameObjs.items.ItemCharge;
import nepisat.ee2.utils.CoordinateBox;
import nepisat.ee2.utils.Coordinates;
import nepisat.ee2.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class RedShovel extends ItemCharge
{
	public RedShovel(int i) 
	{
		super(i,"rm_shovel", (byte) 4);
	}
	
	@Override
	public boolean canHarvestBlock(Block block, ItemStack stack)
	{
		return block.blockMaterial == Material.grass || block.blockMaterial == Material.ground || block.blockMaterial == Material.sand || block.blockMaterial == Material.snow;
	}
	
	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass) 
	{
		if (toolClass.equals("shovel"))
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
			MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, false);
			
			if (mop != null )
			{
				CoordinateBox box = getRelativeBox(new Coordinates(mop), ForgeDirection.getOrientation(mop.sideHit), this.getCharge(stack) + 1);
				List<ItemStack> drops = new ArrayList<ItemStack>();
				byte charge = this.getCharge(stack);

				for (int x = (int) box.minX; x <= box.maxX; x++)
					for (int y = (int) box.minY; y <= box.maxY; y++)
						for (int z = (int) box.minZ; z <= box.maxZ; z++)
						{
							Block block = Block.blocksList[world.getBlockId(x, y, z)];
							
							if (block.blockID ==0 || block.getBlockHardness(world, x, y, z) == -1 || !canHarvestBlock(block, stack))
							{
								continue;
							}
							
							drops.addAll(Utils.getBlockDrops(world, player, block, stack, x, y, z));
							
							world.setBlockToAir(x, y, z);
						}

				if (!drops.isEmpty())
				{
					world.spawnEntityInWorld(new EntityLootBall(world, drops, player.posX, player.posY, player.posZ));
				//	PacketHandler.sendTo(new SwingItemPKT(), (EntityPlayerMP) player);
				}
			}
		}
		
		return stack;
	}
	
	private CoordinateBox getRelativeBox(Coordinates coords, ForgeDirection direction, int charge)
	{
		if (direction.offsetX != 0)
		{
			return new CoordinateBox(coords.x, coords.y - charge, coords.z - charge, coords.x, coords.y + charge, coords.z + charge);
		}
		else if (direction.offsetY != 0)
		{
			return new CoordinateBox(coords.x - charge, coords.y, coords.z - charge, coords.x + charge, coords.y, coords.z + charge);
		}
		else
		{
			return new CoordinateBox(coords.x - charge, coords.y - charge, coords.z, coords.x + charge, coords.y + charge, coords.z);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		this.itemIcon = register.registerIcon(this.getTexture("rm_tools", "shovel"));
	}
}
