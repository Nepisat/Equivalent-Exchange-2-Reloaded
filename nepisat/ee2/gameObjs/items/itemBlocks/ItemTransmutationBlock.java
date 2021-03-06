package nepisat.ee2.gameObjs.items.itemBlocks;

import nepisat.ee2.utils.AchievementHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemTransmutationBlock extends ItemBlock
{
	public ItemTransmutationBlock(int block)
	{
		super(block);
	}
	
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) 
	{
		if (world != null)
		{
			player.addStat(AchievementHandler.TRANSMUTATION, 1);
		}
	}
}
