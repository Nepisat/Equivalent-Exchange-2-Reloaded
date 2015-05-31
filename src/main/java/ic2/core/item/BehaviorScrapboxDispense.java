package ic2.core.item;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class BehaviorScrapboxDispense extends BehaviorDefaultDispenseItem
{
    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    protected ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        EnumFacing var3 = EnumFacing.getFront(par1IBlockSource.getBlockMetadata());
        IPosition var4 = BlockDispenser.getIPositionFromBlockSource(par1IBlockSource);
        par2ItemStack.splitStack(1);
        doDispense(par1IBlockSource.getWorld(), ItemScrapbox.getDrop(par1IBlockSource.getWorld()), 6, var3, var4);
        return par2ItemStack;
    }
}
