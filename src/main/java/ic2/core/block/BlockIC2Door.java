package ic2.core.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.IC2;
import ic2.core.init.DefaultIds;
import ic2.core.init.InternalName;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.Configuration;

public class BlockIC2Door extends BlockDoor
{
    public int itemDropped;
    private Icon[] textures;

    public BlockIC2Door(Configuration config, InternalName internalName)
    {
        super(IC2.getBlockIdFor(config, internalName, DefaultIds.get(internalName)), Material.iron);
        this.setHardness(50.0F);
        this.setResistance(150.0F);
        this.setStepSound(Block.soundMetalFootstep);
        this.disableStats();
        this.setUnlocalizedName(internalName.name());
        this.setCreativeTab((CreativeTabs)null);
        GameRegistry.registerBlock(this, internalName.name());
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.textures = new Icon[2];
        this.textures[0] = iconRegister.registerIcon("ic2:" + this.getUnlocalizedName().substring(5) + "." + InternalName.top);
        this.textures[1] = iconRegister.registerIcon("ic2:" + this.getUnlocalizedName().substring(5) + "." + InternalName.bottom);
    }

    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z, int side)
    {
        return this.getIcon(side, iBlockAccess.getBlockMetadata(x, y, z));
    }

    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
        return (meta & 8) == 8 ? this.textures[0] : this.textures[1];
    }

    public BlockIC2Door setItemDropped(int itemid)
    {
        this.itemDropped = itemid;
        return this;
    }

    public int idDropped(int meta, Random random, int j)
    {
        return (meta & 8) == 8 ? 0 : this.itemDropped;
    }
}
