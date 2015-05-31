package ic2.core.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.IBoxable;
import ic2.core.Ic2Items;
import ic2.core.init.InternalName;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.common.Configuration;

public class ItemBatteryDischarged extends ItemBattery implements IBoxable
{
    public ItemBatteryDischarged(Configuration config, InternalName internalName, int maxCharge, int transferLimit, int tier)
    {
        super(config, internalName, maxCharge, transferLimit, tier);
        this.setMaxDamage(0);
        this.setMaxStackSize(16);
    }

    public String getTextureName(int index)
    {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int meta)
    {
        return Ic2Items.chargedReBattery.getItem().getIconFromDamage(Ic2Items.chargedReBattery.getItem().getMaxDamage());
    }

    public int getChargedItemId(ItemStack itemstack)
    {
        return Ic2Items.chargedReBattery.itemID;
    }

    public boolean canBeStoredInToolbox(ItemStack itemstack)
    {
        return true;
    }
}
