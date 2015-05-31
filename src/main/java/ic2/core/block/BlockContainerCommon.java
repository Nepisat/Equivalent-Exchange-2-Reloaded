package ic2.core.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.IC2;
import ic2.core.init.DefaultIds;
import ic2.core.init.InternalName;
import ic2.core.item.block.ItemBlockIC2;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public abstract class BlockContainerCommon extends Block implements IRareBlock
{
    public final InternalName internalName;

    public BlockContainerCommon(Configuration config, InternalName internalName, Material material)
    {
        this(config, internalName, material, ItemBlockIC2.class);
    }

    public BlockContainerCommon(Configuration config, InternalName internalName, Material material, Class <? extends ItemBlockIC2 > itemClass)
    {
        super(IC2.getBlockIdFor(config, internalName, DefaultIds.get(internalName)), material);
        this.setUnlocalizedName(internalName.name());
        this.setCreativeTab(IC2.tabIC2);
        this.internalName = internalName;
        GameRegistry.registerBlock(this, itemClass, internalName.name());
    }

    public boolean hasTileEntity(int metadata)
    {
        return true;
    }

    public abstract TileEntity createTileEntity(World var1, int var2);

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.common;
    }
}
