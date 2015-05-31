package ic2.core.item.armor;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.init.InternalName;
import ic2.core.util.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemArmorStaticBoots extends ItemArmorUtility
{
    public ItemArmorStaticBoots(Configuration config, InternalName internalName)
    {
        super(config, internalName, InternalName.rubber, 3);
    }

    public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack)
    {
        if (player.inventory.armorInventory[2] != null && player.inventory.armorInventory[2].getItem() instanceof IElectricItem)
        {
            boolean ret = false;
            NBTTagCompound compound = StackUtil.getOrCreateNbtData(itemStack);
            boolean isNotWalking = player.ridingEntity != null || player.isInWater();

            if (!compound.hasKey("x") || isNotWalking)
            {
                compound.setInteger("x", (int)player.posX);
            }

            if (!compound.hasKey("z") || isNotWalking)
            {
                compound.setInteger("z", (int)player.posZ);
            }

            double distance = Math.sqrt((double)((compound.getInteger("x") - (int)player.posX) * (compound.getInteger("x") - (int)player.posX) + (compound.getInteger("z") - (int)player.posZ) * (compound.getInteger("z") - (int)player.posZ)));

            if (distance >= 5.0D)
            {
                compound.setInteger("x", (int)player.posX);
                compound.setInteger("z", (int)player.posZ);
                ret = ElectricItem.manager.charge(player.inventory.armorInventory[2], Math.min(3, (int)distance / 5), Integer.MAX_VALUE, true, false) > 0;
            }

            if (ret)
            {
                player.inventoryContainer.detectAndSendChanges();
            }
        }
    }
}
