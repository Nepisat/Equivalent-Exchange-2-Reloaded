package ic2.core.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.Direction;
import ic2.core.Ic2Items;
import ic2.core.block.machine.tileentity.TileEntityStandardMachine;
import ic2.core.init.InternalName;
import ic2.core.util.StackUtil;
import java.text.DecimalFormat;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemUpgradeModule extends ItemIC2 implements IUpgradeItem
{
    private static DecimalFormat decimalformat = new DecimalFormat("0.##");
    private final int upgradeCount;

    public ItemUpgradeModule(Configuration config, InternalName internalName)
    {
        super(config, internalName);
        this.setHasSubtypes(true);
        Ic2Items.overclockerUpgrade = new ItemStack(this, 1, 0);
        Ic2Items.transformerUpgrade = new ItemStack(this, 1, 1);
        Ic2Items.energyStorageUpgrade = new ItemStack(this, 1, 2);
        Ic2Items.ejectorUpgrade = new ItemStack(this, 1, 3);
        this.upgradeCount = 4;
    }

    public String getTextureFolder()
    {
        return "upgrade";
    }

    public String getTextureName(int index)
    {
        String ret = super.getTextureName(index);
        return ret != null ? ret : (index - this.upgradeCount < 6 ? InternalName.ejectorUpgrade.name() + "." + (index - this.upgradeCount) : null);
    }

    public Icon getIcon(ItemStack stack, int pass)
    {
        if (stack.getItemDamage() == 3)
        {
            byte dir = StackUtil.getOrCreateNbtData(stack).getByte("dir");

            if (dir >= 1 && dir <= 6)
            {
                return super.getIconFromDamage(this.upgradeCount + dir - 1);
            }
        }

        return super.getIcon(stack, pass);
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    public String getUnlocalizedName(ItemStack itemStack)
    {
        InternalName ret;

        switch (itemStack.getItemDamage())
        {
            case 0:
                ret = InternalName.overclockerUpgrade;
                break;

            case 1:
                ret = InternalName.transformerUpgrade;
                break;

            case 2:
                ret = InternalName.energyStorageUpgrade;
                break;

            case 3:
                ret = InternalName.ejectorUpgrade;
                break;

            default:
                return null;
        }

        return ret.name();
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
        super.addInformation(stack, player, list, par4);

        switch (stack.getItemDamage())
        {
            case 0:
                list.add(StatCollector.translateToLocalFormatted("ic2.tooltip.upgrade.overclocker.time", new Object[] {decimalformat.format(100.0D * Math.pow(0.7D, (double)stack.stackSize))}));
                list.add(StatCollector.translateToLocalFormatted("ic2.tooltip.upgrade.overclocker.power", new Object[] {decimalformat.format(100.0D * Math.pow(1.6D, (double)stack.stackSize))}));
                break;

            case 1:
                list.add(StatCollector.translateToLocalFormatted("ic2.tooltip.upgrade.transformer", new Object[] {Integer.valueOf(stack.stackSize)}));
                break;

            case 2:
                list.add(StatCollector.translateToLocalFormatted("ic2.tooltip.upgrade.storage", new Object[] {Integer.valueOf(10000 * stack.stackSize)}));
                break;

            case 3:
                String side;

                switch (StackUtil.getOrCreateNbtData(stack).getByte("dir") - 1)
                {
                    case 0:
                        side = "ic2.dir.west";
                        break;

                    case 1:
                        side = "ic2.dir.east";
                        break;

                    case 2:
                        side = "ic2.dir.bottom";
                        break;

                    case 3:
                        side = "ic2.dir.top";
                        break;

                    case 4:
                        side = "ic2.dir.north";
                        break;

                    case 5:
                        side = "ic2.dir.south";
                        break;

                    default:
                        side = "ic2.tooltip.upgrade.ejector.anyside";
                }

                list.add(StatCollector.translateToLocalFormatted("ic2.tooltip.upgrade.ejector", new Object[] {StatCollector.translateToLocal(side)}));
        }
    }

    public boolean onItemUse(ItemStack stack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float xOffset, float yOffset, float zOffset)
    {
        if (stack.getItemDamage() == 3)
        {
            int dir = 1 + (side + 2) % 6;
            NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);

            if (nbtData.getByte("dir") != dir)
            {
                nbtData.setByte("dir", (byte)dir);
                return true;
            }
        }

        return false;
    }

    public void getSubItems(int i, CreativeTabs tabs, List itemList)
    {
        for (int meta = 0; meta <= 32767; ++meta)
        {
            ItemStack stack = new ItemStack(this, 1, meta);

            if (this.getUnlocalizedName(stack) == null)
            {
                break;
            }

            itemList.add(stack);
        }
    }

    public int getExtraProcessTime(ItemStack stack, TileEntityStandardMachine parent)
    {
        return 0;
    }

    public double getProcessTimeMultiplier(ItemStack stack, TileEntityStandardMachine parent)
    {
        return stack.getItemDamage() == 0 ? 0.7D : 1.0D;
    }

    public int getExtraEnergyDemand(ItemStack stack, TileEntityStandardMachine parent)
    {
        return 0;
    }

    public double getEnergyDemandMultiplier(ItemStack stack, TileEntityStandardMachine parent)
    {
        return stack.getItemDamage() == 0 ? 1.6D : 1.0D;
    }

    public int getExtraEnergyStorage(ItemStack stack, TileEntityStandardMachine parent)
    {
        return stack.getItemDamage() == 2 ? 10000 : 0;
    }

    public double getEnergyStorageMultiplier(ItemStack stack, TileEntityStandardMachine parent)
    {
        return 1.0D;
    }

    public int getExtraTier(ItemStack stack, TileEntityStandardMachine parent)
    {
        return stack.getItemDamage() == 1 ? 1 : 0;
    }

    public boolean onTick(ItemStack stack, TileEntityStandardMachine parent)
    {
        if (stack.getItemDamage() == 3)
        {
            ItemStack output = parent.outputSlot.get();

            if (output != null && parent.energy >= 20)
            {
                int amount = Math.min(output.stackSize, parent.energy / 20);
                byte dir = StackUtil.getOrCreateNbtData(stack).getByte("dir");

                if (dir >= 1 && dir <= 6)
                {
                    TileEntity te = Direction.values()[dir - 1].applyToTileEntity(parent);

                    if (!(te instanceof IInventory))
                    {
                        return false;
                    }

                    amount = StackUtil.putInInventory((IInventory)te, StackUtil.copyWithSize(output, amount), false);
                }
                else
                {
                    amount = StackUtil.distribute(parent, StackUtil.copyWithSize(output, amount), false);
                }

                output.stackSize -= amount;

                if (output.stackSize <= 0)
                {
                    parent.outputSlot.clear();
                }

                parent.energy -= 20 * amount;
                return true;
            }
        }

        return false;
    }

    public void onProcessEnd(ItemStack stack, TileEntityStandardMachine parent, ItemStack output) {}
}
