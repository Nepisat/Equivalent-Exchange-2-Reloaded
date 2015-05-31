package ic2.core.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.init.InternalName;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

public abstract class BaseElectricItem extends ItemIC2 implements IElectricItem
{
    public int maxCharge;
    public int transferLimit = 100;
    public int tier = 1;

    public BaseElectricItem(Configuration config, InternalName internalName)
    {
        super(config, internalName);
        this.setMaxDamage(27);
        this.setMaxStackSize(1);
    }

    public boolean canProvideEnergy(ItemStack itemStack)
    {
        return false;
    }

    public int getChargedItemId(ItemStack itemStack)
    {
        return super.itemID;
    }

    public int getEmptyItemId(ItemStack itemStack)
    {
        return super.itemID;
    }

    public int getMaxCharge(ItemStack itemStack)
    {
        return this.maxCharge;
    }

    public int getTier(ItemStack itemStack)
    {
        return this.tier;
    }

    public int getTransferLimit(ItemStack itemStack)
    {
        return this.transferLimit;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List itemList)
    {
        ItemStack itemStack = new ItemStack(this, 1);

        if (this.getChargedItemId(itemStack) == super.itemID)
        {
            ItemStack charged = new ItemStack(this, 1);
            ElectricItem.manager.charge(charged, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false);
            itemList.add(charged);
        }

        if (this.getEmptyItemId(itemStack) == super.itemID)
        {
            itemList.add(new ItemStack(this, 1, this.getMaxDamage()));
        }
    }
}
