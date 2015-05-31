package ic2.core.item.block;

import ic2.core.Ic2Items;
import ic2.core.block.TileEntityBarrel;
import ic2.core.init.InternalName;
import ic2.core.item.ItemBooze;
import ic2.core.item.ItemIC2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemBarrel extends ItemIC2
{
    public ItemBarrel(Configuration config, InternalName internalName)
    {
        super(config, internalName);
        this.setMaxStackSize(1);
    }

    public String getItemDisplayName(ItemStack itemstack)
    {
        int v = ItemBooze.getAmountOfValue(itemstack.getItemDamage());
        return v > 0 ? "" + v + "L Booze Barrel" : "Empty Booze Barrel";
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float a, float b, float c)
    {
        if (world.getBlockId(i, j, k) == Ic2Items.scaffold.itemID && world.getBlockMetadata(i, j, k) < 5)
        {
            world.setBlock(i, j, k, Ic2Items.blockBarrel.itemID, 0, 3);
            ((TileEntityBarrel)world.getBlockTileEntity(i, j, k)).set(itemstack.getItemDamage());

            if (!entityplayer.capabilities.isCreativeMode)
            {
                --entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem].stackSize;

                if (entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem].stackSize == 0)
                {
                    entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = null;
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }
}
