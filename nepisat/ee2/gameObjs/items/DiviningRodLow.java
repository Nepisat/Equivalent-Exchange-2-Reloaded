package nepisat.ee2.gameObjs.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nepisat.ee2.utils.CoordinateBox;
import nepisat.ee2.utils.Coordinates;
import nepisat.ee2.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class DiviningRodLow extends ItemPE
{
	private final int searchRange;
	
	public DiviningRodLow(int i)
	{
		super(i);
		this.setUnlocalizedName("divining_rod_1");
		searchRange = 3;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) 
	{
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (world.isRemote) return stack;
		
		MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, false);
		if (mop != null )
		{
		//	PacketHandler.sendTo(new SwingItemPKT(), (EntityPlayerMP) player);
			long totalEmc = 0;
			int numBlocks = 0;
			
			CoordinateBox box = getBoxFromDirection(ForgeDirection.getOrientation(mop.sideHit), new Coordinates(mop));
			
			for (int i = (int) box.minX; i <= box.maxX; i++)
				for (int j = (int) box.minY; j <= box.maxY; j++)
					for (int k = (int) box.minZ; k <= box.maxZ; k++)
					{
						Block block = Block.blocksList[world.getBlockId(i, j, k)];
						
						if (block.blockID==0)
						{
							continue;
						}
						
						ArrayList<ItemStack> drops = block.getBlockDropped(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
						
						if (drops.isEmpty())
						{
							continue;
						}
						
						int blockEmc = Utils.getEmcValue(drops.get(0));
						
						if (blockEmc == 0)
						{
							HashMap<ItemStack, ItemStack> map = (HashMap) FurnaceRecipes.smelting().getSmeltingList();
							
							for (Entry<ItemStack, ItemStack> entry : map.entrySet())
							{
								if (entry == null || entry.getKey() == null)
								{
									continue;
								}
								
								if (entry.getKey().getItem() == drops.get(0).getItem())
								{
									int currentValue = Utils.getEmcValue(entry.getValue());
									
									if (currentValue != 0)
									{
										totalEmc += currentValue;
									}	
								}
							}
						}
						else
						{
							totalEmc += blockEmc;
						}
						
						numBlocks++;	
					}
			
			player.addChatMessage(String.format("Average EMC for %d blocks: %,d", numBlocks, (totalEmc / numBlocks)));
		}
		
		return stack;
	}
	
	public CoordinateBox getBoxFromDirection(ForgeDirection direction, Coordinates coords)
	{
		int actualRange = searchRange - 1;
		
		if (direction.offsetX != 0)
		{
			if (direction.offsetX > 0)
				return new CoordinateBox(coords.x - actualRange, coords.y - 1, coords.z - 1, coords.x, coords.y + 1, coords.z + 1);
			else return new CoordinateBox(coords.x, coords.y - 1, coords.z - 1, coords.x + actualRange, coords.y + 1, coords.z + 1);
		}
		else if (direction.offsetY != 0)
		{
			if (direction.offsetY > 0)
				return new CoordinateBox(coords.x - 1, coords.y - actualRange, coords.z - 1, coords.x + 1, coords.y, coords.z + 1);
			else return new CoordinateBox(coords.x - 1, coords.y, coords.z - 1, coords.x + 1, coords.y + actualRange, coords.z + 1);
		}
		else
		{
			if (direction.offsetZ > 0)
				return new CoordinateBox(coords.x - 1, coords.y - 1, coords.z - actualRange, coords.x + 1, coords.y + 1, coords.z);
			else return new CoordinateBox(coords.x - 1, coords.y - 1, coords.z, coords.x + 1, coords.y + 1, coords.z + actualRange);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		this.itemIcon = register.registerIcon(this.getTexture("divining1"));
	}
}