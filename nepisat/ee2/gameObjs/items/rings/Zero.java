package nepisat.ee2.gameObjs.items.rings;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nepisat.ee2.api.IModeChanger;
import nepisat.ee2.gameObjs.items.ItemCharge;
import nepisat.ee2.utils.CoordinateBox;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;


@Optional.Interface(iface = "baubles.api.IBauble", modid = "Baubles")
public class Zero extends ItemCharge implements IModeChanger, IBauble
{
	@SideOnly(Side.CLIENT)
	private Icon ringOff;
	@SideOnly(Side.CLIENT)
	private Icon ringOn;
	
	public Zero(int i) 
	{
		super(i,"zero_ring", (byte) 4);
		this.setContainerItem(this);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) 
	{
		super.onUpdate(stack, world, entity, par4, par5);
		
		if (world.isRemote || par4 > 8 || stack.getItemDamage() == 0)
		{
			return;
		}

		CoordinateBox box = new CoordinateBox(entity.posX - 3, entity.posY - 3, entity.posZ - 3, entity.posX + 3, entity.posY + 3, entity.posZ + 3);

		for (int x = (int) box.minX; x <= box.maxX; x++)
			for (int y = (int) box.minY; y <= box.maxY; y++)
				for (int z = (int) box.minZ; z <= box.maxZ; z++)
				{
					
					Block b = Block.blocksList[world.getBlockId(x, y, z)];

					if (b == Block.waterMoving|| b == Block.waterStill)
					{
						world.setBlock(x, y, z, Block.ice.blockID);
					}
					else if (b.isBlockSolid(world, x, y, z, ForgeDirection.UP.flag))
					{
						Block b2 = Block.blocksList[world.getBlockId(x, y + 1, z)];

						if (b2.blockID == 0)
						{
							world.setBlock(x, y + 1, z, Block.snow.blockID);
						}
					}
				}
	}
	 
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (!world.isRemote)
		{
			CoordinateBox box = new CoordinateBox(player.boundingBox);
			int offset = 4 + this.getCharge(stack);
			box.expand(offset, offset, offset);
			
			for (int x = (int) box.minX; x <= box.maxX; x++)
				for (int y = (int) box.minY; y <= box.maxY; y++)
					for (int z = (int) box.minZ; z <= box.maxZ; z++)
					{
						Block b = Block.blocksList[world.getBlockId(x, y, z)];
						
						if (b == Block.waterStill|| b == Block.waterMoving)
						{
							world.setBlock(x, y, z, Block.ice.blockID);
						}
						else if (b.isBlockSolid(world, x, y, z, ForgeDirection.UP.flag))
						{
							Block b2 = Block.blocksList[world.getBlockId(x, y + 1, z)];
							
							if (b2.blockID == 0)
							{
								world.setBlock(x, y + 1, z, Block.snow.blockID);
							}
						}
					}
		}
		
		return stack;
	}
	
	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack stack)
	{
		return false;
	}

	@Override
	public byte getMode(ItemStack stack)
	{
		return (byte) stack.getItemDamage();
	}

	@Override
	public void changeMode(EntityPlayer player, ItemStack stack) 
	{
		stack.setItemDamage(stack.getItemDamage() == 0 ? 1 : 0);
	}
	
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int dmg)
	{
		return dmg == 0 ? ringOff : ringOn;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		ringOn = register.registerIcon(this.getTexture("rings", "zero_on"));
		ringOff = register.registerIcon(this.getTexture("rings", "zero_off"));
	}
	
	@Override
	@Optional.Method(modid = "Baubles")
	public baubles.api.BaubleType getBaubleType(ItemStack itemstack)
	{
		return BaubleType.RING;
	}

	@Override
	@Optional.Method(modid = "Baubles")
	public void onWornTick(ItemStack stack, EntityLivingBase player) 
	{
		this.onUpdate(stack, player.worldObj, player, 0, false);
	}

	@Override
	@Optional.Method(modid = "Baubles")
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {}

	@Override
	@Optional.Method(modid = "Baubles")
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {}

	@Override
	@Optional.Method(modid = "Baubles")
	public boolean canEquip(ItemStack itemstack, EntityLivingBase player) 
	{
		return true;
	}

	@Override
	@Optional.Method(modid = "Baubles")
	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) 
	{
		return true;
	}
}
