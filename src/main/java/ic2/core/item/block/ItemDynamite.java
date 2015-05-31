package ic2.core.item.block;

import ic2.api.item.IBoxable;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.block.BehaviorDynamiteDispense;
import ic2.core.block.EntityDynamite;
import ic2.core.block.EntityStickyDynamite;
import ic2.core.init.InternalName;
import ic2.core.item.ItemIC2;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemDynamite extends ItemIC2 implements IBoxable
{
    public boolean sticky;

    public ItemDynamite(Configuration config, InternalName internalName, boolean st)
    {
        super(config, internalName);
        this.sticky = st;
        this.setMaxStackSize(16);
        BlockDispenser.dispenseBehaviorRegistry.putObject(this, new BehaviorDynamiteDispense(this.sticky));
    }

    public int getMetadata(int i)
    {
        return i;
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float a, float b, float c)
    {
        if (this.sticky)
        {
            return false;
        }
        else
        {
            if (l == 0)
            {
                --j;
            }

            if (l == 1)
            {
                ++j;
            }

            if (l == 2)
            {
                --k;
            }

            if (l == 3)
            {
                ++k;
            }

            if (l == 4)
            {
                --i;
            }

            if (l == 5)
            {
                ++i;
            }

            int i1 = world.getBlockId(i, j, k);

            if (i1 == 0 && Block.blocksList[Ic2Items.dynamiteStick.itemID].canPlaceBlockAt(world, i, j, k))
            {
                world.setBlock(i, j, k, Ic2Items.dynamiteStick.itemID, 0, 3);
                --itemstack.stackSize;
                return true;
            }
            else
            {
                return true;
            }
        }
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (!entityplayer.capabilities.isCreativeMode)
        {
            --itemstack.stackSize;
        }

        world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (Item.itemRand.nextFloat() * 0.4F + 0.8F));

        if (IC2.platform.isSimulating())
        {
            if (this.sticky)
            {
                world.spawnEntityInWorld(new EntityStickyDynamite(world, entityplayer));
            }
            else
            {
                world.spawnEntityInWorld(new EntityDynamite(world, entityplayer));
            }
        }

        return itemstack;
    }

    public boolean canBeStoredInToolbox(ItemStack itemstack)
    {
        return true;
    }
}
