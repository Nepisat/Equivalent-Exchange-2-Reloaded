package nepisat.ee2.gameObjs.items.tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nepisat.ee2.gameObjs.ObjHandler;
import nepisat.ee2.gameObjs.entity.EntityLootBall;
import nepisat.ee2.gameObjs.items.ItemMode;
import nepisat.ee2.utils.AchievementHandler;
import nepisat.ee2.utils.CoordinateBox;
import nepisat.ee2.utils.Coordinates;
import nepisat.ee2.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ForgeHooks;

import java.util.ArrayList;
import java.util.List;

public class RedPick extends ItemMode
{
	public RedPick(int i)
	{
		super(i,"rm_pick", (byte) 4, new String[] {"Standard", "3x Tallshot", "3x Wideshot", "3x Longshot"});
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world,int block, int x, int y, int z, EntityLivingBase eLiving)
	{
		if (world.isRemote || !(eLiving instanceof EntityPlayer))
		{
			return false;
		}
		
		EntityPlayer player = (EntityPlayer) eLiving;
		byte mode = this.getMode(stack);
		
		if (mode == 0)
		{
			return false;
		}
			
		MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, false);
		CoordinateBox box = null;
		
		if (mop == null )
		{
			return false;
		}
		
		ForgeDirection direction = ForgeDirection.getOrientation(mop.sideHit);
		
		if (mode == 1)
		{
			box = new CoordinateBox(x, y - 1, z, x, y + 1, z);
		}
		else if (mode == 2)
		{
			if (direction.offsetX != 0)
			{
				box = new CoordinateBox(x, y, z - 1, x, y, z + 1);
			}
			else if (direction.offsetZ != 0)
			{
				box = new CoordinateBox(x - 1, y, z, x + 1, y, z);
			}
			else
			{
				int dir = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
				
				if (dir == 0 || dir == 2)
				{
					box = new CoordinateBox(x, y, z - 1, x, y, z + 1);
				}
				else
				{
					box = new CoordinateBox(x - 1, y, z, x + 1, y, z);
				}
			}
		}
		else
		{
			if (direction.offsetX == 1)
			{
				box = new CoordinateBox(x - 2, y, z, x, y, z);
			}
			else if (direction.offsetX == - 1)
			{
				box = new CoordinateBox(x, y, z, x + 2, y, z);
			}
			else if (direction.offsetZ == 1)
			{
				box = new CoordinateBox(x, y, z - 2, x, y, z);
			}
			else if (direction.offsetZ == -1)
			{
				box = new CoordinateBox(x, y, z, x, y, z + 2);
			}
			else if (direction.offsetY == 1)
			{
				box = new CoordinateBox(x, y - 2, z, x, y, z);
			}
			else 
			{
				box = new CoordinateBox(x, y, z, x, y + 2, z);
			}
		}
		
		List<ItemStack> drops = new ArrayList<ItemStack>();
		
		for (int i = (int) box.minX; i <= box.maxX; i++)
			for (int j = (int) box.minY; j <= box.maxY; j++)
				for (int k = (int) box.minZ; k <= box.maxZ; k++)
				{
					Block b = Block.blocksList[world.getBlockId(i, j, k)];
					
					if (b.blockID != 0 && b.getBlockHardness(world, i, j, k) != -1 && (canHarvestBlock(Block.blocksList[block], stack) || ForgeHooks.canToolHarvestBlock(Block.blocksList[block], world.getBlockMetadata(i, j, k), stack)))
					{
						drops.addAll(Utils.getBlockDrops(world, player, b, stack, i, j, k));
						world.setBlockToAir(i, j, k);
					}
				}
			
		world.spawnEntityInWorld(new EntityLootBall(world, drops, x, y, z));
		return false;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (!world.isRemote)
		{
			int offset = this.getCharge(stack) + 3;
			CoordinateBox box = new CoordinateBox(player.posX - offset, player.posY - offset, player.posZ - offset, player.posX + offset, player.posY + offset, player.posZ + offset);
			List<ItemStack> drops = new ArrayList<ItemStack>();
			
			for (int x = (int) box.minX; x <= box.maxX; x++)
				for (int y = (int) box.minY; y <= box.maxY; y++)
					for (int z = (int) box.minZ; z <= box.maxZ; z++)
					{
						Block block = Block.blocksList[world.getBlockId(x, y, z)];
						
						if (Utils.isOre(block) && block.getBlockHardness(world, x, y, z) != -1 && (canHarvestBlock(block, stack) || ForgeHooks.canToolHarvestBlock(block, world.getBlockMetadata(x, y, z), stack)))
						{
							Utils.harvestVein(world, player, stack, new Coordinates(x, y, z), block, drops, 0);
						}
					}
			
			if (!drops.isEmpty())
			{
				world.spawnEntityInWorld(new EntityLootBall(world, drops, player.posX, player.posY, player.posZ));
			//	PacketHandler.sendTo(new SwingItemPKT(), (EntityPlayerMP) player);
			}
		}
		
		return stack;
	}
	
	@Override
	public boolean canHarvestBlock(Block block, ItemStack stack) 
	{
		return block.blockMaterial == Material.iron || block.blockMaterial == Material.anvil || block.blockMaterial == Material.rock;
	}
	
	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass)
	{
		if (toolClass.equals("pickaxe"))
		{
			//mine TiCon blocks as well
			return 4;
		}
		
		return -1;
	}
	
	@Override
	public float getDigSpeed(ItemStack stack, Block block, int metadata)
	{
		if (block == ObjHandler.matterBlock || block == ObjHandler.dmFurnaceOff || block == ObjHandler.dmFurnaceOn || block == ObjHandler.rmFurnaceOff || block == ObjHandler.rmFurnaceOn)
		{
			return 1200000.0F;
		}
		
		if (canHarvestBlock(block, stack) || ForgeHooks.canToolHarvestBlock(block, metadata, stack))
		{
			return 16.0f + (14.0F * this.getCharge(stack));
		}
		
		return 1.0F;
	}
	
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) 
	{
		super.onCreated(stack, world, player);
		
		if (!world.isRemote)
		{
			player.addStat(AchievementHandler.RM_PICK, 1);
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
		this.itemIcon = register.registerIcon(this.getTexture("rm_tools", "pick"));
	}
}
