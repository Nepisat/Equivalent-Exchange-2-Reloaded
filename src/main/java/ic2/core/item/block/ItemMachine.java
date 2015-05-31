package ic2.core.item.block;

import net.minecraft.item.ItemStack;

public class ItemMachine extends ItemBlockRare
{
    public ItemMachine(int i)
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
                return "blockMachine";

            case 1:
                return "blockIronFurnace";

            case 2:
                return "blockElecFurnace";

            case 3:
                return "blockMacerator";

            case 4:
                return "blockExtractor";

            case 5:
                return "blockCompressor";

            case 6:
                return "blockCanner";

            case 7:
                return "blockMiner";

            case 8:
                return "blockPump";

            case 9:
                return "blockMagnetizer";

            case 10:
                return "blockElectrolyzer";

            case 11:
                return "blockRecycler";

            case 12:
                return "blockAdvMachine";

            case 13:
                return "blockInduction";

            case 14:
                return "blockMatter";

            case 15:
                return "blockTerra";

            default:
                return null;
        }
    }
}
