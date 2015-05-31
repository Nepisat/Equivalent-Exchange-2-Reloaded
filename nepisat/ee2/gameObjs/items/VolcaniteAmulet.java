package nepisat.ee2.gameObjs.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nepisat.ee2.api.IProjectileShooter;
import nepisat.ee2.gameObjs.entity.EntityLavaProjectile;
import nepisat.ee2.handlers.PlayerChecks;
import nepisat.ee2.utils.Constants;
import nepisat.ee2.utils.KeyBinds;
import nepisat.ee2.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidHandler;

import org.lwjgl.input.Keyboard;

import java.util.List;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "Baubles")
public class VolcaniteAmulet extends ItemPE implements IProjectileShooter, IBauble
{
	public VolcaniteAmulet(int i)
	{
		super(i);
		this.setUnlocalizedName("volcanite_amulet");
		this.setMaxStackSize(1);
		this.setContainerItem(this);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int sideHit, float f1, float f2, float f3)
	{
		if (!world.isRemote)
		{
			TileEntity tile = world.getBlockTileEntity(x, y, z);

			if (tile instanceof IFluidHandler)
			{
				IFluidHandler tank = (IFluidHandler) tile;

				if (Utils.canFillTank(tank, FluidRegistry.LAVA, sideHit))
				{
					int consumed = (int) Utils.consumePlayerFuel(player, 32);

					if (consumed != -1)
					{
						Utils.fillTank(tank, FluidRegistry.LAVA, sideHit, 1000 * (consumed / 32));
						return true;
					}
				}
			}
		}

		return false;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int invSlot, boolean par5)
	{
		if (invSlot > 8 || !(entity instanceof EntityPlayer)) return;
		
		EntityPlayer player = (EntityPlayer) entity;

		int x = (int) Math.floor(player.posX);
		int y = (int) (player.posY - player.getYOffset());
		int z = (int) Math.floor(player.posZ);
		
		if ((world.getBlockId(x, y - 1, z) == Block.lavaStill.blockID || world.getBlockId(x, y - 1, z) == Block.lavaMoving.blockID) && world.getBlockId(x, y, z) == 0)
		{
			if (!player.isSneaking())
			{
				player.motionY = 0.0D;
				player.fallDistance = 0.0F;
				player.onGround = true;
			}
				
			if (!world.isRemote && player.capabilities.getWalkSpeed() < 0.25F)
			{
				Utils.setPlayerWalkSpeed(player, 0.25F);
			}
		}
		else if (!world.isRemote)
		{
			if (player.capabilities.getWalkSpeed() != Constants.PLAYER_WALK_SPEED)
			{
				Utils.setPlayerWalkSpeed(player, Constants.PLAYER_WALK_SPEED);
			}
		}
		
		if (!world.isRemote)
		{
			if (!player.isImmuneToFire())
			{
				Utils.setPlayerFireImmunity(player, true);
			}

			PlayerChecks.addPlayerFireChecks((EntityPlayerMP) player);
		}
	}
	
	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public boolean shootProjectile(EntityPlayer player, ItemStack stack) 
	{
		if (Utils.consumePlayerFuel(player, 32) != -1)
		{
			player.worldObj.spawnEntityInWorld(new EntityLavaProjectile(player.worldObj, player));
			return true;
		}

		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		this.itemIcon = register.registerIcon(this.getTexture("rings", "volcanite_amulet"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
	{
		if (KeyBinds.getExtraFuncKeyCode() >= 0 && KeyBinds.getExtraFuncKeyCode() < Keyboard.getKeyCount())
		{
			list.add("Press " + Keyboard.getKeyName(KeyBinds.getProjectileKeyCode()) + " to fire a lava projectile");
		}

		list.add("Right-click to fill tanks.");
		list.add("All operations cost 32 EMC!");
	}
	
	@Override
	@Optional.Method(modid = "Baubles")
	public baubles.api.BaubleType getBaubleType(ItemStack itemstack)
	{
		return BaubleType.AMULET;
	}

	@Override
	@Optional.Method(modid = "Baubles")
	public void onWornTick(ItemStack stack, EntityLivingBase ent) 
	{
		if (!(ent instanceof EntityPlayer)) 
		{
			return;
		}
		
		EntityPlayer player = (EntityPlayer) ent;
		World world = player.worldObj;

		int x = (int) Math.floor(player.posX);
		int y = (int) (player.posY - player.getYOffset());
		int z = (int) Math.floor(player.posZ);
		
		if ((world.getBlockId(x, y - 1, z) == Block.lavaStill.blockID || world.getBlockId(x, y - 1, z) == Block.lavaMoving.blockID) && world.getBlockId(x, y, z) == 0)
		{
			if (!player.isSneaking())
			{
				player.motionY = 0.0D;
				player.fallDistance = 0.0F;
				player.onGround = true;
			}
				
			if (!world.isRemote && player.capabilities.getWalkSpeed() < 0.25F)
			{
				Utils.setPlayerWalkSpeed(player, 0.25F);
			}
		}
		else if (!world.isRemote)
		{
			if (player.capabilities.getWalkSpeed() != Constants.PLAYER_WALK_SPEED)
			{
				Utils.setPlayerWalkSpeed(player, Constants.PLAYER_WALK_SPEED);
			}
		}
		
		if (!world.isRemote && !player.isImmuneToFire())
		{
			Utils.setPlayerFireImmunity(player, true);
		}
	}

	@Override
	@Optional.Method(modid = "Baubles")
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {}

	@Override
	@Optional.Method(modid = "Baubles")
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) 
	{
		if (player instanceof EntityPlayer && !player.worldObj.isRemote)
		{
			Utils.setPlayerFireImmunity((EntityPlayer) player, false);
		}
	}

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