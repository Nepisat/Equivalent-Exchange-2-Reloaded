package ic2.core.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.init.InternalName;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemBattery extends BaseElectricItem
{
    public ItemBattery(Configuration config, InternalName internalName, int maxCharge, int transferLimit, int tier)
    {
        super(config, internalName);
        this.setNoRepair();
        super.maxCharge = maxCharge;
        super.transferLimit = transferLimit;
        super.tier = tier;
    }

    public String getTextureName(int index)
    {
        return index < 5 ? this.getUnlocalizedName() + "." + index : null;
    }

    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int meta)
    {
        return meta <= 1 ? super.textures[4] : (meta >= this.getMaxDamage() - 1 ? super.textures[0] : super.textures[3 - 3 * (meta - 2) / (this.getMaxDamage() - 4 + 1)]);
    }

    public boolean canProvideEnergy(ItemStack itemStack)
    {
        return true;
    }

    public int getEmptyItemId(ItemStack itemStack)
    {
        return super.itemID == Ic2Items.chargedReBattery.itemID ? Ic2Items.reBattery.itemID : super.getEmptyItemId(itemStack);
    }

    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityplayer)
    {
        if (IC2.platform.isSimulating() && itemStack.itemID == Ic2Items.chargedReBattery.itemID)
        {
            boolean transferred = false;

            for (int i = 0; i < 9; ++i)
            {
                ItemStack stack = entityplayer.inventory.mainInventory[i];

                if (stack != null && Item.itemsList[stack.itemID] instanceof IElectricItem && !(Item.itemsList[stack.itemID] instanceof ItemBattery))
                {
                    IElectricItem item = (IElectricItem)stack.getItem();
                    int transfer = ElectricItem.manager.discharge(itemStack, 2 * super.transferLimit, item.getTier(stack), true, true);
                    transfer = ElectricItem.manager.charge(stack, transfer, super.tier, true, false);
                    ElectricItem.manager.discharge(itemStack, transfer, item.getTier(stack), true, false);

                    if (transfer > 0)
                    {
                        transferred = true;
                    }
                }
            }

            if (transferred && !IC2.platform.isRendering())
            {
                entityplayer.openContainer.detectAndSendChanges();
            }
        }

        return itemStack;
    }
}
