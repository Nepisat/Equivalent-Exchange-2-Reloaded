package ic2.core.item.tool;

import ic2.api.item.ElectricItem;
import ic2.core.Ic2Items;
import ic2.core.init.InternalName;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemTreetapElectric extends ItemElectricTool
{
    public ItemTreetapElectric(Configuration config, InternalName internalName)
    {
        super(config, internalName, EnumToolMaterial.IRON, 50);
        super.maxCharge = 10000;
        super.transferLimit = 100;
        super.tier = 1;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (world.getBlockId(x, y, z) == Ic2Items.rubberWood.itemID && ElectricItem.manager.canUse(itemStack, super.operationEnergyCost))
        {
            if (ItemTreetap.attemptExtract(entityPlayer, world, x, y, z, side, (List)null))
            {
                ElectricItem.manager.use(itemStack, super.operationEnergyCost, entityPlayer);
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
}
