package ic2.core.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.IC2;
import ic2.core.init.InternalName;
import java.util.List;
import java.util.logging.Level;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class BlockITNT extends BlockIC2Explosive
{
    public boolean isITNT;

    public BlockITNT(Configuration config, InternalName internalName)
    {
        super(config, internalName, internalName == InternalName.blockITNT);

        if (internalName == InternalName.blockITNT)
        {
            this.isITNT = true;
        }
        else
        {
            this.isITNT = false;
        }

        this.setHardness(0.0F);
        this.setStepSound(Block.soundGrassFootstep);
    }

    public EntityIC2Explosive getExplosionEntity(World world, float x, float y, float z, EntityLivingBase igniter)
    {
        Object ret;

        if (this.isITNT)
        {
            ret = new EntityItnt(world, (double)x, (double)y, (double)z);
        }
        else
        {
            ret = new EntityNuke(world, (double)x, (double)y, (double)z);
        }

        ((EntityIC2Explosive)ret).setIgniter(igniter);
        return (EntityIC2Explosive)ret;
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityliving, ItemStack itemStack)
    {
        if (!this.isITNT && entityliving instanceof EntityPlayer)
        {
            IC2.log.log(Level.INFO, "Player " + ((EntityPlayer)entityliving).username + " placed a nuke at " + world.provider.dimensionId + ":(" + x + "," + y + "," + z + ")");
        }
    }

    public void onIgnite(World world, EntityPlayer player, int x, int y, int z)
    {
        if (!this.isITNT)
        {
            IC2.log.log(Level.INFO, "Nuke at " + world.provider.dimensionId + ":(" + x + "," + y + "," + z + ") was ignited " + (player == null ? "indirectly" : "by " + player.username));
        }
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack)
    {
        return this.isITNT ? EnumRarity.common : EnumRarity.uncommon;
    }

    public void getSubBlocks(int i, CreativeTabs tabs, List itemList)
    {
        if (this.isITNT || IC2.enableCraftingNuke)
        {
            super.getSubBlocks(i, tabs, itemList);
        }
    }
}
