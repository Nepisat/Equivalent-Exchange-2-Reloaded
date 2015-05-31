package nepisat.ee2.gameObjs.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nepisat.ee2.PECore;
import nepisat.ee2.api.IExtraFunction;
import nepisat.ee2.utils.Constants;
import nepisat.ee2.utils.CoordinateBox;
import nepisat.ee2.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
public class MercurialEye extends ItemMode implements IExtraFunction
{
	public MercurialEye(int i)
	{
		super(i,"mercurial_eye", (byte) 4, new String[] {"Normal", "Transmutation"});
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (!world.isRemote)
		{
			MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, false);

			if (mop == null)
			{
				return stack;
			}

			ItemStack[] inventory = getInventory(stack);

			if (inventory[0] == null || inventory[1] == null)
			{
				return stack;
			}

			Block toSet = Block.blocksList[inventory[1].getItem().itemID];

			if (toSet.blockID== 0)
			{
				return stack;
			}

			double kleinEmc = ItemPE.getEmc(inventory[0]);
			int reqEmc = Utils.getEmcValue(inventory[1]);

			byte charge = getCharge(stack);
			byte mode = this.getMode(stack);

			int facing = MathHelper.floor_double((double) ((player.rotationYaw * 4F) / 360F) + 0.5D) & 3;
			ForgeDirection dir = ForgeDirection.getOrientation(mop.sideHit);
			Vec3 look = player.getLookVec();

			CoordinateBox box = null;

			if (dir == ForgeDirection.UP)
			{
				if (look.yCoord >= -1 && look.yCoord <= -0.8)
				{
					box = new CoordinateBox(mop.blockX - charge, mop.blockY + 1, mop.blockZ - charge, mop.blockX + charge, mop.blockY + 1, mop.blockZ + charge);
				}
				else if (facing == 0 || facing == 2)
				{
					box = new CoordinateBox(mop.blockX - charge, mop.blockY + 1, mop.blockZ, mop.blockX + charge, mop.blockY + (charge * 2), mop.blockZ);
				}
				else
				{
					box = new CoordinateBox(mop.blockX, mop.blockY + 1, mop.blockZ - charge, mop.blockX, mop.blockY + (charge * 2), mop.blockZ + charge);
				}
			}
			else if (dir == ForgeDirection.DOWN)
			{
				if (look.yCoord >= 0.8 && look.yCoord <= 1)
				{
					box = new CoordinateBox(mop.blockX - charge, mop.blockY - 1, mop.blockZ - charge, mop.blockX + charge, mop.blockY - 1, mop.blockZ + charge);
				}
				else if (facing == 0 || facing == 2)
				{
					box = new CoordinateBox(mop.blockX - charge, mop.blockY - (charge * 2), mop.blockZ, mop.blockX + charge, mop.blockY - 1, mop.blockZ);
				}
				else
				{
					box = new CoordinateBox(mop.blockX, mop.blockY - (charge * 2), mop.blockZ - charge, mop.blockX, mop.blockY - 1, mop.blockZ + charge);
				}
			}
			else if (dir == ForgeDirection.EAST)
			{
				box = new CoordinateBox(mop.blockX + 1, mop.blockY - charge, mop.blockZ - charge, mop.blockX + 1, mop.blockY + charge, mop.blockZ + charge);
			}
			else if (dir == ForgeDirection.WEST)
			{
				box = new CoordinateBox(mop.blockX - 1, mop.blockY - charge, mop.blockZ - charge, mop.blockX - 1, mop.blockY + charge, mop.blockZ + charge);
			}
			else if (dir == ForgeDirection.SOUTH)
			{
				box = new CoordinateBox(mop.blockX - charge, mop.blockY - charge, mop.blockZ + 1, mop.blockX + charge, mop.blockY + charge, mop.blockZ + 1);
			}
			else if (dir == ForgeDirection.NORTH)
			{
				box = new CoordinateBox(mop.blockX - charge, mop.blockY - charge, mop.blockZ - 1, mop.blockX + charge, mop.blockY + charge, mop.blockZ - 1);
			}

			if (box != null)
			{
				for (double x = box.minX; x <= box.maxX; x++)
				for (double y = box.minY; y <= box.maxY; y++)
				for (double z = box.minZ; z <= box.maxZ; z++)
				{
					Block b = Block.blocksList[world.getBlockId((int) x, (int) y, (int) z)];

					if (mode == 0)
					{
						if (b.blockID != 0)
						{
							continue;
						}

						if (kleinEmc >= reqEmc)
						{
							world.setBlock((int) x, (int) y, (int) z, toSet.blockID);
							removeKleinEMC(stack, reqEmc);
							kleinEmc -= reqEmc;
						}
						else
						{
							break;
						}
					}
					else
					{
						if (b.blockID==0)
						{
							if (kleinEmc >= reqEmc)
							{
								world.setBlock((int) x, (int) y, (int) z, toSet.blockID);
								removeKleinEMC(stack, reqEmc);
								kleinEmc -= reqEmc;
							}
							else
							{
								break;
							}
						}
						else
						{
							if (b == toSet || !Utils.doesItemHaveEmc(new ItemStack(b)))
							{
								continue;
							}

							int emc = Utils.getEmcValue(new ItemStack(b));

							if (emc > reqEmc)
							{
								int difference = emc - reqEmc;

								kleinEmc += MathHelper.clamp_int((int)kleinEmc, 0, Utils.getKleinStarMaxEmc(inventory[0]));
								addKleinEMC(stack, difference);
								world.setBlock((int) x, (int) y, (int) z, toSet.blockID);
							}
							else if (emc < reqEmc)
							{
								int difference = reqEmc - emc;

								if (kleinEmc >= difference)
								{
									kleinEmc -= difference;
									removeKleinEMC(stack, difference);
									world.setBlock((int) x, (int) y, (int) z, toSet.blockID);
								}
							}
							else
							{
								world.setBlock((int) x, (int) y, (int) z, toSet.blockID);
							}
						}
					}
				}
			}
		}

		return stack;
	}

	private void addKleinEMC(ItemStack eye, int amount)
	{
		NBTTagList list = eye.stackTagCompound.getTagList("Items");

		for (int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound nbt = (NBTTagCompound) list.tagAt(i);

			if (nbt.getByte("Slot") == 0)
			{
				ItemStack kleinStar = ItemStack.loadItemStackFromNBT(nbt);

				NBTTagCompound tag = nbt.getCompoundTag("tag");

				double newEmc = MathHelper.clamp_int((int)tag.getDouble("StoredEMC") + amount, 0, Utils.getKleinStarMaxEmc(kleinStar));

				tag.setDouble("StoredEMC", newEmc);
				break;
			}
		}
	}

	private void removeKleinEMC(ItemStack eye, int amount)
	{
		NBTTagList list = eye.stackTagCompound.getTagList("Items");

		for (int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound nbt = (NBTTagCompound) list.tagAt(i);

			if (nbt.getByte("Slot") == 0)
			{
				NBTTagCompound tag = nbt.getCompoundTag("tag");
				tag.setDouble("StoredEMC", tag.getDouble("StoredEMC") - amount);
				break;
			}
		}
	}

	private ItemStack[] getInventory(ItemStack eye)
	{
		ItemStack[] result = new ItemStack[2];

		if (eye.hasTagCompound())
		{
			NBTTagList list = eye.stackTagCompound.getTagList("Items");

			for (int i = 0; i < list.tagCount(); i++)
			{
				NBTTagCompound nbt = (NBTTagCompound) list.tagAt(i);
				result[nbt.getByte("Slot")] = ItemStack.loadItemStackFromNBT(nbt);
			}
		}

		return result;
	}

	@Override
	public void doExtraFunction(ItemStack stack, EntityPlayer player) 
	{
		player.openGui(PECore.instance, Constants.MERCURIAL_GUI, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) 
	{
		return 1; 
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister register)
	{
		this.itemIcon = register.registerIcon(this.getTexture("mercurial_eye"));
	}
}
