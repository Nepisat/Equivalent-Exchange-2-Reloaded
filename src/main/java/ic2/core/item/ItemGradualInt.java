package ic2.core.item;

import ic2.core.init.InternalName;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.Configuration;

public class ItemGradualInt extends ItemGradual
{
    protected int maxDmg;

    public ItemGradualInt(Configuration config, InternalName internalName, int maxdmg)
    {
        super(config, internalName);
        this.maxDmg = maxdmg;
    }

    public void setDamageForStack(ItemStack stack, int advDmg)
    {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
        nbtData.setInteger("advDmg", advDmg);

        if (this.maxDmg > 0)
        {
            double p = (double)advDmg / (double)this.maxDmg;
            int newDmg = (int)((double)stack.getMaxDamage() * p);

            if (newDmg >= stack.getMaxDamage())
            {
                newDmg = stack.getMaxDamage() - 1;
            }

            stack.setItemDamage(newDmg);
        }
    }

    public int getDamageOfStack(ItemStack stack)
    {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
        return nbtData.getInteger("advDmg");
    }

    public int getMaxDamageEx()
    {
        return this.maxDmg;
    }
}
