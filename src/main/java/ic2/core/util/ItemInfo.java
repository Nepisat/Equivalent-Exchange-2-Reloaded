package ic2.core.util;

import ic2.api.info.IEnergyValueProvider;
import ic2.api.info.IFuelValueProvider;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ItemInfo implements IEnergyValueProvider, IFuelValueProvider
{
    public int getEnergyValue(ItemStack itemStack)
    {
        Item item = itemStack.getItem();
        return item instanceof IElectricItem ? ElectricItem.manager.getCharge(itemStack) : (item.itemID != Ic2Items.suBattery.itemID && item.itemID != Item.redstone.itemID ? 0 : (item.itemID == Ic2Items.suBattery.itemID ? 1200 : 800));
    }

    public int getFuelValue(ItemStack itemStack, boolean allowLava)
    {
        int itemId = itemStack.itemID;
        FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(itemStack);

        if (liquid != null && liquid.getFluid() == FluidRegistry.LAVA)
        {
            return allowLava ? 2000 : 0;
        }
        else if (itemId == Ic2Items.filledFuelCan.itemID)
        {
            NBTTagCompound data = StackUtil.getOrCreateNbtData(itemStack);

            if (itemStack.getItemDamage() > 0)
            {
                data.setInteger("value", itemStack.getItemDamage());
            }

            return data.getInteger("value") * 2;
        }
        else
        {
            return itemId == Ic2Items.scrap.itemID && !IC2.enableBurningScrap ? 0 : TileEntityFurnace.getItemBurnTime(itemStack);
        }
    }
}
