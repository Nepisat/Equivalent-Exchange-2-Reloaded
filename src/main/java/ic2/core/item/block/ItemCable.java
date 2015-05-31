package ic2.core.item.block;

import ic2.api.item.IBoxable;
import ic2.core.Ic2Items;
import ic2.core.block.wiring.BlockCable;
import ic2.core.init.InternalName;
import ic2.core.item.ItemIC2;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemCable extends ItemIC2 implements IBoxable
{
    public ItemCable(Configuration config, InternalName internalName)
    {
        super(config, internalName);
        this.setHasSubtypes(true);
        Ic2Items.copperCableItem = new ItemStack(this, 1, 1);
        Ic2Items.insulatedCopperCableItem = new ItemStack(this, 1, 0);
        Ic2Items.goldCableItem = new ItemStack(this, 1, 2);
        Ic2Items.insulatedGoldCableItem = new ItemStack(this, 1, 3);
        Ic2Items.doubleInsulatedGoldCableItem = new ItemStack(this, 1, 4);
        Ic2Items.ironCableItem = new ItemStack(this, 1, 5);
        Ic2Items.insulatedIronCableItem = new ItemStack(this, 1, 6);
        Ic2Items.doubleInsulatedIronCableItem = new ItemStack(this, 1, 7);
        Ic2Items.trippleInsulatedIronCableItem = new ItemStack(this, 1, 8);
        Ic2Items.glassFiberCableItem = new ItemStack(this, 1, 9);
        Ic2Items.tinCableItem = new ItemStack(this, 1, 10);
        Ic2Items.detectorCableItem = new ItemStack(this, 1, 11);
        Ic2Items.splitterCableItem = new ItemStack(this, 1, 12);
    }

    public String getUnlocalizedName(ItemStack itemstack)
    {
        int meta = itemstack.getItemDamage();
        InternalName ret;

        switch (meta)
        {
            case 0:
                ret = InternalName.itemCable;
                break;

            case 1:
                ret = InternalName.itemCableO;
                break;

            case 2:
                ret = InternalName.itemGoldCable;
                break;

            case 3:
                ret = InternalName.itemGoldCableI;
                break;

            case 4:
                ret = InternalName.itemGoldCableII;
                break;

            case 5:
                ret = InternalName.itemIronCable;
                break;

            case 6:
                ret = InternalName.itemIronCableI;
                break;

            case 7:
                ret = InternalName.itemIronCableII;
                break;

            case 8:
                ret = InternalName.itemIronCableIIII;
                break;

            case 9:
                ret = InternalName.itemGlassCable;
                break;

            case 10:
                ret = InternalName.itemTinCable;
                break;

            case 11:
                ret = InternalName.itemDetectorCable;
                break;

            case 12:
                ret = InternalName.itemSplitterCable;
                break;

            default:
                return null;
        }

        return ret.name();
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float a, float b, float c)
    {
        int blockId = world.getBlockId(x, y, z);

        if (blockId > 0)
        {
            if (blockId == Block.snow.blockID)
            {
                side = 1;
            }
            else if (blockId != Block.vine.blockID && blockId != Block.tallGrass.blockID && blockId != Block.deadBush.blockID && (Block.blocksList[blockId] == null || !Block.blocksList[blockId].isBlockReplaceable(world, x, y, z)))
            {
                switch (side)
                {
                    case 0:
                        --y;
                        break;

                    case 1:
                        ++y;
                        break;

                    case 2:
                        --z;
                        break;

                    case 3:
                        ++z;
                        break;

                    case 4:
                        --x;
                        break;

                    case 5:
                        ++x;
                }
            }
        }

        BlockCable block = (BlockCable)Block.blocksList[Ic2Items.insulatedCopperCableBlock.itemID];

        if ((blockId == 0 || world.canPlaceEntityOnSide(Ic2Items.insulatedCopperCableBlock.itemID, x, y, z, false, side, entityplayer, itemstack)) && world.checkNoEntityCollision(block.getCollisionBoundingBoxFromPool(world, x, y, z, itemstack.getItemDamage())) && world.setBlock(x, y, z, block.blockID, itemstack.getItemDamage(), 3))
        {
            block.onPostBlockPlaced(world, x, y, z, side);
            block.onBlockPlacedBy(world, x, y, z, entityplayer, itemstack);

            if (!entityplayer.capabilities.isCreativeMode)
            {
                --itemstack.stackSize;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public void getSubItems(int i, CreativeTabs tabs, List itemList)
    {
        for (int meta = 0; meta < 32767; ++meta)
        {
            ItemStack stack = new ItemStack(this, 1, meta);

            if (this.getUnlocalizedName(stack) == null)
            {
                break;
            }

            itemList.add(stack);
        }
    }

    public boolean canBeStoredInToolbox(ItemStack itemstack)
    {
        return true;
    }
}
