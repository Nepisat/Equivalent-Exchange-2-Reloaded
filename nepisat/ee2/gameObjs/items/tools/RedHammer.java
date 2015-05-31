package nepisat.ee2.gameObjs.items.tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nepisat.ee2.gameObjs.ObjHandler;
import nepisat.ee2.gameObjs.entity.EntityLootBall;
import nepisat.ee2.gameObjs.items.ItemCharge;
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
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.ForgeDirection;
public class RedHammer extends ItemCharge
{
	public RedHammer(int i) 
	{
		super(i,"rm_hammer", (byte) 4);
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, int block, int x, int y, int z, EntityLivingBase eLiving)
	{
		if (world.isRemote || !(eLiving instanceof EntityPlayer) || this.getCharge(stack) == 0 || !canHarvestBlock(Block.blocksList[block], stack))
		{
			return false;
		}
		
		EntityPlayer player = (EntityPlayer) eLiving;

		MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, false);

		if (mop == null )
		{
			return false;
		}
		
		CoordinateBox box = getRelativeBox(new Coordinates(x, y, z), ForgeDirection.getOrientation(mop.sideHit), this.getCharge(stack));
		List<ItemStack> drops = new ArrayList<ItemStack>();
		
		for (int i = (int) box.minX; i <= box.maxX; i++)
			for (int j = (int) box.minY; j <= box.maxY; j++)
				for (int k = (int) box.minZ; k <= box.maxZ; k++)
				{
					Block b = Block.blocksList[world.getBlockId(i, j, k)];
					
					if (b.blockID!=0 && b.getBlockHardness(world, i, j, j) != -1 && canHarvestBlock(b, stack))
					{
						drops.addAll(Utils.getBlockDrops(world, player, b, stack, i, j, k));
						world.setBlockToAir(i, j, k);
					}
				}
		
		if (!drops.isEmpty())
		{
			world.spawnEntityInWorld(new EntityLootBall(world, drops, player.posX, player.posY, player.posZ));
			//PacketHandler.sendTo(new SwingItemPKT(), (EntityPlayerMP) player);
		}
		
		return true;
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
	public boolean canHarvestBlock(Block block, ItemStack stack) 
	{
		return block.blockMaterial == Material.iron || block.blockMaterial == Material.anvil || block.blockMaterial == Material.rock;
	}
	
	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass)
	{
		if (toolClass.equals("pickaxe") || toolClass.equals("chisel"))
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
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		this.itemIcon = register.registerIcon(this.getTexture("rm_tools", "hammer"));
	}
}