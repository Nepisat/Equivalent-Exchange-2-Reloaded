package ic2.core.item.tool;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.IC2;
import ic2.core.init.DefaultIds;
import ic2.core.init.InternalName;
import ic2.core.util.Util;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.Configuration;

public class ItemIC2Axe extends ItemAxe
{
    private final Object repairMaterial;
    public float efficiencyOnProperMaterial;

    public ItemIC2Axe(Configuration config, InternalName internalName, EnumToolMaterial enumtoolmaterial, float efficiency, Object repairMaterial)
    {
        super(IC2.getItemIdFor(config, internalName, DefaultIds.get(internalName)), enumtoolmaterial);
        this.efficiencyOnProperMaterial = efficiency;
        this.repairMaterial = repairMaterial;
        this.setUnlocalizedName(internalName.name());
        this.setCreativeTab(IC2.tabIC2);
        GameRegistry.registerItem(this, internalName.name());
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        super.itemIcon = iconRegister.registerIcon("ic2:" + this.getUnlocalizedName());
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
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return this.getUnlocalizedName();
    }

    public String getItemDisplayName(ItemStack itemStack)
    {
        return StatCollector.translateToLocal("ic2." + this.getUnlocalizedName(itemStack));
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    public int getItemEnchantability()
    {
        return 13;
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    public boolean getIsRepairable(ItemStack stack1, ItemStack stack2)
    {
        return stack2 != null && Util.matchesOD(stack2, this.repairMaterial);
    }
}
