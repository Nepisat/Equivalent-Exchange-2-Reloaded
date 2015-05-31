package ic2.core.item.tool;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.IC2;
import ic2.core.init.InternalName;
import ic2.core.util.StackUtil;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemToolWrenchElectric extends ItemToolWrench implements IElectricItem
{
    public ItemToolWrenchElectric(Configuration config, InternalName internalName)
    {
        super(config, internalName);
        this.setMaxDamage(27);
        this.setMaxStackSize(1);
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (IC2.platform.isSimulating() && IC2.keyboard.isModeSwitchKeyDown(entityplayer))
        {
            NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemstack);
            boolean newValue = !nbtData.getBoolean("losslessMode");
            nbtData.setBoolean("losslessMode", newValue);

            if (newValue)
            {
                IC2.platform.messagePlayer(entityplayer, "Lossless wrench mode enabled", new Object[0]);
            }
            else
            {
                IC2.platform.messagePlayer(entityplayer, "Lossless wrench mode disabled", new Object[0]);
            }
        }

        return itemstack;
    }

    public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        return IC2.keyboard.isModeSwitchKeyDown(entityPlayer) ? false : super.onItemUseFirst(itemstack, entityPlayer, world, x, y, z, side, hitX, hitY, hitZ);
    }

    public boolean canTakeDamage(ItemStack stack, int amount)
    {
        amount *= 50;
        return ElectricItem.manager.discharge(stack, amount, Integer.MAX_VALUE, true, true) == amount;
    }

    public void damage(ItemStack itemStack, int amount, EntityPlayer player)
    {
        ElectricItem.manager.use(itemStack, 50 * amount, player);
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
        return 12000;
    }

    public int getTier(ItemStack itemStack)
    {
        return 1;
    }

    public int getTransferLimit(ItemStack itemStack)
    {
        return 250;
    }

    public void getSubItems(int i, CreativeTabs tabs, List itemList)
    {
        ItemStack charged = new ItemStack(this, 1);
        ElectricItem.manager.charge(charged, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false);
        itemList.add(charged);
        itemList.add(new ItemStack(this, 1, this.getMaxDamage()));
    }

    public boolean overrideWrenchSuccessRate(ItemStack itemStack)
    {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);
        return nbtData.getBoolean("losslessMode");
    }

    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
        return false;
    }
}
