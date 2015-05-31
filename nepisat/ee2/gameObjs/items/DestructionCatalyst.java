package nepisat.ee2.gameObjs.items;

import java.util.ArrayList;
import java.util.List;

import nepisat.ee2.gameObjs.entity.EntityLootBall;
import nepisat.ee2.utils.CoordinateBox;
import nepisat.ee2.utils.Coordinates;
import nepisat.ee2.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DestructionCatalyst extends ItemCharge
{
	public DestructionCatalyst(int i) 
	{
		super(i,"destruction_catalyst", (byte) 4);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (world.isRemote) return stack;

		MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, false);

		if (mop != null )
		{
			int charge = this.getCharge(stack);
			int numRows;
			boolean hasAction = false;
			
			if (charge == 0)
			{
				numRows = 1;
			}
			else if (charge == 1)
			{
				numRows = 4;
			}
			else if (charge == 2)
			{
				numRows = 9;
			}
			else 
			{
				numRows = 16;
			}
			
			ForgeDirection direction = ForgeDirection.getOrientation(mop.sideHit);
			
			Coordinates coords = new Coordinates(mop);
			CoordinateBox box = getBoxFromDirection(direction, coords, numRows);
			
			List<ItemStack> drops = new ArrayList();
			
			for (int x = (int) box.minX; x <= box.maxX; x++)
				for (int y = (int) box.minY; y <= box.maxY; y++)
					for (int z = (int) box.minZ; z <= box.maxZ; z++)
					{
						Block block = Block.blocksList[world.getBlockId(x, y, z)];
						float hardness = block.getBlockHardness(world, x, y, z);
						
						if (block == null || block.blockID == 0|| hardness >= 50.0F || hardness == -1.0F)
						{
							continue;
						}
						
						if (!this.consumeFuel(player, stack, 8, true))
						{
							break;
						}
						
						if (!hasAction)
						{
							hasAction = true;
						}
						
						ArrayList<ItemStack> list = Utils.getBlockDrops(world, player, block, stack, x, y, z);
						
						if (list != null && list.size() > 0)
						{
							drops.addAll(list);
						}
						
						world.setBlockToAir(x, y, z);
						
						if (world.rand.nextInt(8) == 0)
						{
							//PacketHandler.sendToAllAround(new ParticlePKT("largesmoke", x, y, z), new TargetPoint(world.provider.dimensionId, x, y + 1, z, 32));
						}
					}
			
			//PacketHandler.sendTo(new SwingItemPKT(), (EntityPlayerMP) player);
			
			if (hasAction)
			{
				world.spawnEntityInWorld(new EntityLootBall(world, drops, player.posX, player.posY, player.posZ));
			}
		}
			
		return stack;
	}
	
	public CoordinateBox getBoxFromDirection(ForgeDirection direction, Coordinates coords, int charge)
	{
		charge--;
		
		if (direction.offsetX != 0)
		{
			if (direction.offsetX > 0)
				return new CoordinateBox(coords.x - charge, coords.y - 1, coords.z - 1, coords.x, coords.y + 1, coords.z + 1);
			else return new CoordinateBox(coords.x, coords.y - 1, coords.z - 1, coords.x + charge, coords.y + 1, coords.z + 1);
		}
		else if (direction.offsetY != 0)
		{
			if (direction.offsetY > 0)
				return new CoordinateBox(coords.x - 1, coords.y - charge, coords.z - 1, coords.x + 1, coords.y, coords.z + 1);
			else return new CoordinateBox(coords.x - 1, coords.y, coords.z - 1, coords.x + 1, coords.y + charge, coords.z + 1);
		}
		else
		{
			if (direction.offsetZ > 0)
				return new CoordinateBox(coords.x - 1, coords.y - 1, coords.z - charge, coords.x + 1, coords.y + 1, coords.z);
			else return new CoordinateBox(coords.x - 1, coords.y - 1, coords.z, coords.x + 1, coords.y + 1, coords.z + charge);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		this.itemIcon = register.registerIcon(this.getTexture("destruction_catalyst"));
	}
}
