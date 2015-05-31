package ic2.core.item;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.IC2;
import ic2.core.init.DefaultIds;
import ic2.core.init.InternalName;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.Configuration;

public class ItemIC2 extends Item
{
    private int rarity = 0;
    protected Icon[] textures;

    public ItemIC2(Configuration config, InternalName internalName)
    {
        super(IC2.getItemIdFor(config, internalName, DefaultIds.get(internalName)));
        this.setUnlocalizedName(internalName.name());
        this.setCreativeTab(IC2.tabIC2);
        GameRegistry.registerItem(this, internalName.name());
    }

    public String getTextureFolder()
    {
        return null;
    }

    public String getTextureName(int index)
    {
        return super.hasSubtypes ? this.getUnlocalizedName(new ItemStack(super.itemID, 1, index)) : (index == 0 ? this.getUnlocalizedName() : null);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        int indexCount;

        for (indexCount = 0; this.getTextureName(indexCount) != null; ++indexCount)
        {
            ;
        }

        this.textures = new Icon[indexCount];
        String textureFolder = this.getTextureFolder() == null ? "" : this.getTextureFolder() + "/";

        for (int index = 0; index < indexCount; ++index)
        {
            this.textures[index] = iconRegister.registerIcon("ic2:" + textureFolder + this.getTextureName(index));
        }
    }

    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int meta)
    {
        return meta < this.textures.length ? this.textures[meta] : (this.textures.length < 1 ? null : this.textures[0]);
    }

    public String getUnlocalizedName()
    {
        return super.getUnlocalizedName().substring(5);
    }

    public String getUnlocalizedName(ItemStack itemStack)
    {
        return this.getUnlocalizedName();
    }

    public String getItemDisplayName(ItemStack itemStack)
    {
        return StatCollector.translateToLocal("ic2." + this.getUnlocalizedName(itemStack));
    }

    public ItemIC2 setRarity(int rarity)
    {
        this.rarity = rarity;
        return this;
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.values()[this.rarity];
    }
}
