package ic2.core.item;

import ic2.core.block.machine.tileentity.TileEntityStandardMachine;
import net.minecraft.item.ItemStack;

public interface IUpgradeItem
{
    int getExtraProcessTime(ItemStack var1, TileEntityStandardMachine var2);

    double getProcessTimeMultiplier(ItemStack var1, TileEntityStandardMachine var2);

    int getExtraEnergyDemand(ItemStack var1, TileEntityStandardMachine var2);

    double getEnergyDemandMultiplier(ItemStack var1, TileEntityStandardMachine var2);

    int getExtraEnergyStorage(ItemStack var1, TileEntityStandardMachine var2);

    double getEnergyStorageMultiplier(ItemStack var1, TileEntityStandardMachine var2);

    int getExtraTier(ItemStack var1, TileEntityStandardMachine var2);

    boolean onTick(ItemStack var1, TileEntityStandardMachine var2);

    void onProcessEnd(ItemStack var1, TileEntityStandardMachine var2, ItemStack var3);
}
