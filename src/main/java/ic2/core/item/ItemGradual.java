package ic2.core.item;

import ic2.api.item.IBoxable;
import ic2.core.Ic2Items;
import ic2.core.init.InternalName;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

public class ItemGradual extends ItemIC2 implements IBoxable
{
    public ItemGradual(Configuration config, InternalName internalName)
    {
        super(config, internalName);
        this.setMaxStackSize(1);
        this.setMaxDamage(10000);
        this.setNoRepair();
    }

    public boolean canBeStoredInToolbox(ItemStack itemstack)
    {
        return itemstack.itemID == Ic2Items.hydratingCell.itemID;
    }

    public void setDamageForStack(ItemStack stack, int dmg)
    {
        stack.setItemDamage(dmg);
    }

    public int getDamageOfStack(ItemStack stack)
    {
        return stack.getItemDamage();
    }

    public int getMaxDamageEx()
    {
        return this.getMaxDamage();
    }
}
