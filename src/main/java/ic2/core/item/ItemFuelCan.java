package ic2.core.item;

import ic2.api.item.IBoxable;
import ic2.core.init.InternalName;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

public class ItemFuelCan extends ItemIC2 implements IBoxable
{
    public ItemFuelCan(Configuration config, InternalName internalName)
    {
        super(config, internalName);
    }

    public boolean canBeStoredInToolbox(ItemStack itemstack)
    {
        return false;
    }
}
