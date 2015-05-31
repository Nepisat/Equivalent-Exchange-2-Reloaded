package ic2.core.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.init.InternalName;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemMigrate extends ItemIC2
{
    private final Item targetItem;

    public ItemMigrate(Configuration config, InternalName oldName, Item targetItem)
    {
        super(config, oldName);
        this.targetItem = targetItem;
        this.setCreativeTab((CreativeTabs)null);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {}

    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int meta)
    {
        return this.targetItem != null ? this.targetItem.getIconFromDamage(meta) : super.itemIcon;
    }

    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isHeld)
    {
        if (this.targetItem != null)
        {
            itemStack.itemID = this.targetItem.itemID;
        }
    }
}
