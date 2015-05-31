package ic2.core.util;

import ic2.core.block.personal.IPersonalBlock;
import java.util.Comparator;
import net.minecraft.inventory.IInventory;

final class StackUtil$1 implements Comparator<IInventory>
{
    public int compare(IInventory a, IInventory b)
    {
        return !(a instanceof IPersonalBlock) && b instanceof IPersonalBlock ? (!(b instanceof IPersonalBlock) && a instanceof IPersonalBlock ? b.getSizeInventory() - a.getSizeInventory() : 1) : -1;
    }

 
}
