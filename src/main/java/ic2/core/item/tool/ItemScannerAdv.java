package ic2.core.item.tool;

import ic2.api.item.ElectricItem;
import ic2.core.init.InternalName;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

public class ItemScannerAdv extends ItemScanner
{
    public ItemScannerAdv(Configuration config, InternalName internalName, int t)
    {
        super(config, internalName, t);
    }

    public int startLayerScan(ItemStack itemStack)
    {
        return ElectricItem.manager.use(itemStack, 250, (EntityLivingBase)null) ? 6 : 0;
    }
}
