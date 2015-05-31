package nepisat.ee2.gameObjs.items.rings;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;


public class HarvestGoddess extends RingToggle
{
	public HarvestGoddess(int i) 
	{
		super(i,"harvest_god");
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) 
	{
		if (world.isRemote || par4 > 8 || !(entity instanceof EntityPlayer)) 
		{
			return;
		}
		
		super.onUpdate(stack, world, entity, par4, par5);
		
		EntityPlayer player = (EntityPlayer) entity;
		
		if (stack.getItemDamage() != 0)
		{
			double storedEmc = this.getEmc(stack);
			
			if (storedEmc == 0 && !this.consumeFuel(player, stack, 64, true))
			{
				stack.setItemDamage(0);
			}
			else
			{
				growNearbyRandomly(true, world, player);
				this.removeEmc(stack, 0.32F);
			}
		}
		else
		{
			growNearbyRandomly(false, world, player);
		}
	}
	
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
	{
		if (world.isRemote || !player.canPlayerEdit(x, y, z, par7, stack))
		{
			return false;
		}
		
		Block block = Block.blocksList[world.getBlockId(x, y, z)];
		
		if (player.isSneaking())
		{
			Object[] obj = getStackFromInventory(player.inventory.mainInventory, Item.dyePowder, 15, 4);

			if (obj == null) 
			{
				return false;
			}
			
			ItemStack boneMeal = (ItemStack) obj[1];

			if (boneMeal != null && useBoneMeal(world, x, y, z))
			{
				player.inventory.decrStackSize((Integer) obj[0], 4);
				player.inventoryContainer.detectAndSendChanges();
				return true;
			}
			
			return false;
		}
		
		return plantSeeds(world, player, x, y, z);
	}
	
	private boolean useBoneMeal(World world, int xCoord, int yCoord, int zCoord)
	{
		boolean result = false;
		
		for (int x = xCoord - 15; x <= xCoord + 15; x++)
			for (int z = zCoord - 15; z <= zCoord + 15; z++)
			{
				Block crop = Block.blocksList[world.getBlockId(x, yCoord, z)];
				
				if (crop instanceof IGrowable)
				{
					IGrowable growable = (IGrowable) crop;
					
					if (growable.func_149852_a(world, world.rand, x, yCoord, z))
					{
						if (!result)
						{
							result = true;
						}
						
						growable.func_149853_b(world, world.rand, x, yCoord, z);
					}
				}
			}
		
		return result;
	}
	
	private boolean plantSeeds(World world, EntityPlayer player, int xCoord, int yCoord, int zCoord)
	{
		boolean result = false;
		
		List<StackWithSlot> seeds = getAllSeeds(player.inventory.mainInventory);
		
		if (seeds.isEmpty())
		{
			return false;
		}
		
		for (int x = xCoord - 8; x <= xCoord + 8; x++)
			for (int z = zCoord - 8; z <= zCoord + 8; z++)
			{
				Block block = Block.blocksList[player.worldObj.getBlockId(x, yCoord, z)];
				
				if (block == null || block.blockID == 0) 
				{
					continue;
				}
				
				for (int i = 0; i < seeds.size(); i++)
				{
					StackWithSlot s = seeds.get(i);
					IPlantable plant;
					
					if (s.stack.getItem() instanceof IPlantable)
					{
						plant = (IPlantable) s.stack.getItem();
					}
					else
					{
						plant = (IPlantable) Block.blocksList[s.stack.itemID];
					}
					
					if (block.canSustainPlant(world, x, yCoord, z, ForgeDirection.UP, plant) && world.isAirBlock(x, yCoord + 1, z))
					{
						world.setBlock(x, yCoord + 1, z, plant.getPlantID(world, x, yCoord + 1, z));
						player.inventory.decrStackSize(s.slot, 1);
						player.inventoryContainer.detectAndSendChanges();
						
						s.stack.stackSize--;
						
						if (s.stack.stackSize <= 0)
						{
							seeds.remove(i);
						}
						
						if (!result)
						{
							result = true;
						}
					}
				}
			}
		
		return result;
	}
	
	private List<StackWithSlot> getAllSeeds(ItemStack[] inv) 
	{
		List<StackWithSlot> result = new ArrayList();
		
		for (int i = 0; i < inv.length; i++)
		{
			ItemStack stack = inv[i];
			
			if (stack != null)
			{
				if (stack.getItem() instanceof IPlantable)
				{
					result.add(new StackWithSlot(stack, i));
					continue;
				}
				
				Block block = Block.blocksList[stack.getItem().itemID];
				
				if (block != null && block instanceof IPlantable)
				{
					result.add(new StackWithSlot(stack, i));
				}
			}
		}
		
		return result;
	}
	
	private Object[] getStackFromInventory(ItemStack[] inv, Class<?> type)
	{
		Object[] obj = new Object[2];
		
		for (int i = 0; i < inv.length;i++)
		{
			ItemStack stack = inv[i];
			
			if (stack != null && type.isInstance(stack.getItem()))
			{
				obj[0] = i;
				obj[1] = stack;
				return obj;
			}
		}
		return null;
	}
	
	private Object[] getStackFromInventory(ItemStack[] inv, Item item, int meta)
	{
		Object[] obj = new Object[2];
		
		for (int i = 0; i < inv.length;i++)
		{
			ItemStack stack = inv[i];
			
			if (stack != null && stack.getItem() == item && stack.getItemDamage() == meta)
			{
				obj[0] = i;
				obj[1] = stack;
				return obj;
			}
		}
		
		return null;
	}
	
	private Object[] getStackFromInventory(ItemStack[] inv, Item item, int meta, int minAmount)
	{
		Object[] obj = new Object[2];
		
		for (int i = 0; i < inv.length;i++)
		{
			ItemStack stack = inv[i];
			
			if (stack != null && stack.stackSize >= minAmount && stack.getItem() == item && stack.getItemDamage() == meta)
			{
				obj[0] = i;
				obj[1] = stack;
				return obj;
			}
		}
		
		return null;
	}
	
	private void growNearbyRandomly(boolean harvest, World world, Entity player)
	{
		int chance = harvest ? 16 : 32;
		
		for (int x = (int) (player.posX - 5); x <= player.posX + 5; x++)
			for (int y = (int) (player.posY - 3); y <= player.posY + 3; y++)
				for (int z = (int) (player.posZ - 5); z <= player.posZ + 5; z++)
				{
					Block crop = Block.blocksList[world.getBlockId(x, y, z)];
					
					if (crop instanceof BlockGrass)
					{
						continue;
					}
					
					if (crop instanceof IShearable)
					{
						if (harvest)
						{
							world.destroyBlock(x, y, z, true);
						}
					}
					else if (crop instanceof IGrowable)
					{
						IGrowable growable = (IGrowable) crop;
						
						if(harvest && !growable.func_149851_a(world, x, y, z, false))
						{
							world.destroyBlock(x, y, z, true);
						}
						else if (world.rand.nextInt(chance) == 0)
						{
							growable.func_149853_b(world, world.rand, x, y, z);
						}
					}
					else if (crop instanceof IPlantable)
					{
						if (world.rand.nextInt(chance / 4) == 0)
						{
							for (int i = 0; i < (harvest ? 8 : 4); i++)
							{
								crop.updateTick(world, x, y, z, world.rand);
							}
						}
						
						if (harvest)
						{
							if (crop == Block.reed || crop == Block.cactus)
							{
								boolean shouldHarvest = true;
								
								for (int i = 1; i < 3; i++)
								{
									if (Block.blocksList[world.getBlockId(x, y + i, z)] != crop)
									{
										shouldHarvest = false;
										break;
									}
								}
								
								if (shouldHarvest)
								{
									for (int i = crop == Block.reed ? 1 : 0; i < 3; i++)
									{
										world.destroyBlock(x, y + i, z, true);
									}
								}
							}
						}
					}
				}
	}
	
	@Override
	public void changeMode(EntityPlayer player, ItemStack stack)
	{
		if (stack.getItemDamage() == 0)
		{
			if (this.getEmc(stack) == 0 && !this.consumeFuel(player, stack, 64, true))
			{
				//NOOP (used to be sounds)
			}
			else
			{
				stack.setItemDamage(1);
			}
		}
		else
		{
			stack.setItemDamage(0);
		}
	}
	
	private class StackWithSlot
	{
		public final int slot;
		public final ItemStack stack;
		
		public StackWithSlot(ItemStack stack, int slot) 
		{
			this.stack = stack.copy();
			this.slot = slot;
		}
	}
}
