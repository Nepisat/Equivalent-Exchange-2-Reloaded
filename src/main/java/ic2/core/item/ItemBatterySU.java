package ic2.core.item;

import ic2.api.item.ElectricItem;
import ic2.api.item.IBoxable;
import ic2.api.item.IElectricItem;
import ic2.core.init.InternalName;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemBatterySU extends ItemIC2 implements IBoxable
{
    public int capacity;
    public int tier;

    public ItemBatterySU(Configuration config, InternalName internalName, int capacity, int tier)
    {
        super(config, internalName);
        this.capacity = capacity;
        this.tier = tier;
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (itemstack.itemID != super.itemID)
        {
            return itemstack;
        }
        else
        {
            int energy = this.capacity;

            for (int i = 0; i < 9 && energy > 0; ++i)
            {
                ItemStack stack = entityplayer.inventory.mainInventory[i];

                if (stack != null && Item.itemsList[stack.itemID] instanceof IElectricItem && stack != itemstack)
                {
                    energy -= ElectricItem.manager.charge(stack, energy, this.tier, true, false);
                }
            }

            if (energy != this.capacity)
            {
                --itemstack.stackSize;
            }

            return itemstack;
        }
    }

    public boolean canBeStoredInToolbox(ItemStack itemstack)
    {
        return true;
    }
}
