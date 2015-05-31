package ic2.core.item.armor;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.block.generator.tileentity.TileEntitySolarGenerator;
import ic2.core.init.InternalName;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemArmorSolarHelmet extends ItemArmorUtility
{
    public ItemArmorSolarHelmet(Configuration config, InternalName internalName)
    {
        super(config, internalName, InternalName.solar, 0);
        this.setMaxDamage(0);
    }

    public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack)
    {
        boolean ret = false;

        if (player.inventory.armorInventory[2] != null && player.inventory.armorInventory[2].getItem() instanceof IElectricItem && TileEntitySolarGenerator.isSunVisible(player.worldObj, (int)player.posX, (int)player.posY + 1, (int)player.posZ))
        {
            ret = ElectricItem.manager.charge(player.inventory.armorInventory[2], 1, Integer.MAX_VALUE, true, false) > 0;
        }

        if (ret)
        {
            player.inventoryContainer.detectAndSendChanges();
        }
    }

    public int getItemEnchantability()
    {
        return 0;
    }
}
