package ic2.core.item.tool;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.IC2;
import ic2.core.init.DefaultIds;
import ic2.core.init.InternalName;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeHooks;

public abstract class ItemElectricTool extends ItemTool implements IElectricItem
{
    public int operationEnergyCost;
    public int maxCharge;
    public int transferLimit;
    public int tier;
    public Set<Block> mineableBlocks = new HashSet();

    public ItemElectricTool(Configuration config, InternalName internalName, EnumToolMaterial toolmaterial, int operationEnergyCost)
    {
        super(IC2.getItemIdFor(config, internalName, DefaultIds.get(internalName)), 0.0F, toolmaterial, new Block[0]);
        this.operationEnergyCost = operationEnergyCost;
        this.setMaxDamage(27);
        this.setMaxStackSize(1);
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
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    public float getStrVsBlock(ItemStack tool, Block block)
    {
        return !ElectricItem.manager.canUse(tool, this.operationEnergyCost) ? 1.0F : (ForgeHooks.isToolEffective(tool, block, 0) ? super.efficiencyOnProperMaterial : (this.canHarvestBlock(block) ? super.efficiencyOnProperMaterial : 1.0F));
    }

    public float getStrVsBlock(ItemStack tool, Block block, int md)
    {
        return !ElectricItem.manager.canUse(tool, this.operationEnergyCost) ? 1.0F : (ForgeHooks.isToolEffective(tool, block, md) ? super.efficiencyOnProperMaterial : (this.canHarvestBlock(block) ? super.efficiencyOnProperMaterial : 1.0F));
    }

    /**
     * Returns if the item (tool) can harvest results from the block type.
     */
    public boolean canHarvestBlock(Block block)
    {
        return this.mineableBlocks.contains(block);
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1)
    {
        return true;
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    public int getItemEnchantability()
    {
        return 0;
    }

    public boolean isRepairable()
    {
        return false;
    }

    public boolean canProvideEnergy(ItemStack itemStack)
    {
        return false;
    }

    public int getChargedItemId(ItemStack itemStack)
    {
        return super.itemID;
    }

    public int getEmptyItemId(ItemStack itemStack)
    {
        return super.itemID;
    }

    public int getMaxCharge(ItemStack itemStack)
    {
        return this.maxCharge;
    }

    public int getTier(ItemStack itemStack)
    {
        return this.tier;
    }

    public int getTransferLimit(ItemStack itemStack)
    {
        return this.transferLimit;
    }

    public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLivingBase par7EntityLiving)
    {
        if (Block.blocksList[par3] == null)
        {
            IC2.log.severe("ItemElectricTool.onBlockDestroyed(): received invalid block id " + par3);
            return false;
        }
        else
        {
            if ((double)Block.blocksList[par3].getBlockHardness(par2World, par4, par5, par6) != 0.0D)
            {
                if (par7EntityLiving != null)
                {
                    ElectricItem.manager.use(par1ItemStack, this.operationEnergyCost, par7EntityLiving);
                }
                else
                {
                    ElectricItem.manager.discharge(par1ItemStack, this.operationEnergyCost, this.tier, true, false);
                }
            }

            return true;
        }
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int i, CreativeTabs tabs, List itemList)
    {
        ItemStack charged = new ItemStack(this, 1);
        ElectricItem.manager.charge(charged, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false);
        itemList.add(charged);
        itemList.add(new ItemStack(this, 1, this.getMaxDamage()));
    }
}
