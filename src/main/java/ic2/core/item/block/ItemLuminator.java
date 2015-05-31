package ic2.core.item.block;

import ic2.core.IC2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLuminator extends ItemBlockRare
{
    public ItemLuminator(int i)
    {
        super(i);
        this.setCreativeTab(IC2.tabIC2);
    }

    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        return world.setBlock(x, y, z, this.getBlockID(), side, 3);
    }
}
