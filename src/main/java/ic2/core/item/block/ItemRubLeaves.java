package ic2.core.item.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.Ic2Items;
import net.minecraft.block.Block;
import net.minecraft.item.ItemLeaves;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ColorizerFoliage;

public class ItemRubLeaves extends ItemLeaves
{
    public ItemRubLeaves(int par1)
    {
        super(par1);
        this.setHasSubtypes(false);
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        return ColorizerFoliage.getFoliageColorBirch();
    }

    /**
     * Returns the unlocalized name of this item.
     */
    public String getUnlocalizedName()
    {
        return super.getUnlocalizedName().substring(5);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        return this.getUnlocalizedName();
    }

    public String getItemDisplayName(ItemStack itemStack)
    {
        return StatCollector.translateToLocal("ic2." + this.getUnlocalizedName(itemStack));
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets an icon index based on an item's damage value
     */
    public Icon getIconFromDamage(int par1)
    {
        return Block.blocksList[Ic2Items.rubberLeaves.itemID].getIcon(0, par1);
    }
}
