package ic2.core.slot;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class SlotArmor extends Slot
{
    /** The inventory we want to extract a slot from. */
    private final InventoryPlayer inventory;
    private final int armorType;

    public SlotArmor(InventoryPlayer inventory, int armorType, int xDisplayPosition, int yDisplayPosition)
    {
        super(inventory, 36 + (3 - armorType), xDisplayPosition, yDisplayPosition);
        this.inventory = inventory;
        this.armorType = armorType;
    }

    public boolean isItemValid(ItemStack itemStack)
    {
        return itemStack.getItem().isValidArmor(itemStack, this.armorType, this.inventory.player);
    }

    @SideOnly(Side.CLIENT)
    public Icon getBackgroundIconIndex()
    {
        return ItemArmor.func_94602_b(this.armorType);
    }
}
