package ic2.core.item.block;

import net.minecraft.item.ItemStack;

public class ItemMachine2 extends ItemBlockRare
{
    public ItemMachine2(int i)
    {
        super(i);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int i)
    {
        return i;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack itemstack)
    {
        int meta = itemstack.getItemDamage();

        switch (meta)
        {
            case 0:
                return "blockTeleporter";

            case 1:
                return "blockTesla";

            case 2:
                return "blockCropmatron";

            default:
                return null;
        }
    }
}
