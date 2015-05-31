package nepisat.ee2.gameObjs.items.tools;

import nepisat.ee2.gameObjs.items.ItemCharge;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DarkHoe extends ItemCharge
{
	public DarkHoe(int i) 
	{
		super(i,"dm_hoe", (byte) 3);
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
	{
		if (!player.canPlayerEdit(x, y, z, par7, stack))
		{
			return false;
		}
		else
		{
			UseHoeEvent event = new UseHoeEvent(player, stack, world, x, y, z);
			if (MinecraftForge.EVENT_BUS.post(event))
			{
				return false;
			}

			if (event.getResult() == Result.ALLOW)
			{
				return true;
			}

			byte charge = this.getCharge(stack);
			boolean hasAction = false;
			boolean hasSoundPlayed = false;
			
			for (int i = x - charge; i <= x + charge; i++)
				for (int j = z - charge; j <= z + charge; j++)
				{
					Block block = Block.blocksList[world.getBlockId(i, y, j)];
					
					if (Block.blocksList[world.getBlockId(i, y + 1, j)].isAirBlock(world, i, y + 1, j) && (block == Block.grass || block == Block.dirt))
					{
						Block block1 = Block.tilledField;
						
						if (!hasSoundPlayed)
						{
							world.playSoundEffect((double)((float)i + 0.5F), (double)((float)y + 0.5F), (double)((float)j + 0.5F), block1.stepSound.getStepSound(), (block1.stepSound.getVolume() + 1.0F) / 2.0F, block1.stepSound.getPitch() * 0.8F);
							hasSoundPlayed = true;
						}
						
						if (world.isRemote)
						{
							return true;
						}
						else
						{
							 world.setBlock(i, y, j, block1.blockID);
							 
							 if (!hasAction)
							 {
								 hasAction = true;
							 }
						}
					}
				}
			
			return hasAction;
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
		this.itemIcon = register.registerIcon(this.getTexture("dm_tools", "hoe"));
	}
}
