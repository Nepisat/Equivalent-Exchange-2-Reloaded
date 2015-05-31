package ic2.core.block;

import ic2.core.Ic2Items;
import ic2.core.init.InternalName;
import ic2.core.item.block.ItemBlockMetal;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class BlockMetal extends BlockSimple
{
    public BlockMetal(Configuration config, InternalName internalName)
    {
        super(config, internalName, Material.iron, ItemBlockMetal.class);
        this.setHardness(4.0F);
        this.setStepSound(Block.soundMetalFootstep);
        Ic2Items.bronzeBlock = new ItemStack(this, 1, 2);
        Ic2Items.copperBlock = new ItemStack(this, 1, 0);
        Ic2Items.tinBlock = new ItemStack(this, 1, 1);
        Ic2Items.uraniumBlock = new ItemStack(this, 1, 3);
    }

    public int damageDropped(int i)
    {
        return i;
    }

    public void getSubBlocks(int j, CreativeTabs tabs, List itemList)
    {
        for (int i = 0; i < 16; ++i)
        {
            ItemStack is = new ItemStack(this, 1, i);

            if (Item.itemsList[super.blockID].getUnlocalizedName(is) != null)
            {
                itemList.add(is);
            }
        }
    }

    public boolean isBeaconBase(World worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ)
    {
        int meta = worldObj.getBlockMetadata(x, y, z);
        return meta == 2 || meta == 3;
    }
}
