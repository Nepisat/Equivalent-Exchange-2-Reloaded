package ic2.core.util;

import ic2.api.Direction;
import ic2.core.block.personal.IPersonalBlock;
import ic2.core.util.StackUtil$1;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

public final class StackUtil
{
    private static final Direction[] directions = Direction.values();
    private static final Random random = new Random();

    public static List<IInventory> getAdjacentInventories(TileEntity source)
    {
        ArrayList inventories = new ArrayList();
        Direction[] arr$ = directions;
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            Direction direction = arr$[i$];
            TileEntity target = direction.applyToTileEntity(source);

            if (target instanceof IInventory)
            {
                Object inventory = (IInventory)target;

                if (target instanceof TileEntityChest)
                {
                    Direction[] arr$1 = directions;
                    int len$1 = arr$1.length;

                    for (int i$1 = 0; i$1 < len$1; ++i$1)
                    {
                        Direction direction2 = arr$1[i$1];

                        if (direction2 != Direction.YN && direction2 != Direction.YP)
                        {
                            TileEntity target2 = direction2.applyToTileEntity(target);

                            if (target2 instanceof TileEntityChest)
                            {
                                inventory = new InventoryLargeChest("", (IInventory)inventory, (IInventory)target2);
                                break;
                            }
                        }
                    }
                }

                if (!(target instanceof IPersonalBlock) || source instanceof IPersonalBlock && ((IPersonalBlock)target).permitsAccess(((IPersonalBlock)source).getUsername()))
                {
                    inventories.add(inventory);
                }
            }
        }

        Collections.sort(inventories, new StackUtil$1());
        return inventories;
    }

    public static int distribute(TileEntity source, ItemStack itemStack, boolean simulate)
    {
        int transferred = 0;
        Iterator i$ = getAdjacentInventories(source).iterator();

        while (i$.hasNext())
        {
            IInventory inventory = (IInventory)i$.next();
            int amount = putInInventory(inventory, itemStack, simulate);
            transferred += amount;
            itemStack.stackSize -= amount;

            if (itemStack.stackSize == 0)
            {
                break;
            }
        }

        itemStack.stackSize += transferred;
        return transferred;
    }

    public static ItemStack fetch(TileEntity source, ItemStack itemStack, boolean simulate)
    {
        ItemStack ret = null;
        int oldStackSize = itemStack.stackSize;
        Iterator i$ = getAdjacentInventories(source).iterator();

        while (i$.hasNext())
        {
            IInventory inventory = (IInventory)i$.next();
            ItemStack transferred = getFromInventory(inventory, itemStack, simulate);

            if (transferred != null)
            {
                if (ret == null)
                {
                    ret = transferred;
                }
                else
                {
                    ret.stackSize += transferred.stackSize;
                    itemStack.stackSize -= transferred.stackSize;
                }

                if (ret.stackSize == itemStack.stackSize)
                {
                    break;
                }
            }
        }

        itemStack.stackSize = oldStackSize;
        return ret;
    }

    public static void distributeDrop(TileEntity source, List<ItemStack> itemStacks)
    {
        Iterator i$ = itemStacks.iterator();
        ItemStack itemStack;

        while (i$.hasNext())
        {
            itemStack = (ItemStack)i$.next();
            int amount = distribute(source, itemStack, false);

            if (amount == itemStack.stackSize)
            {
                i$.remove();
            }
            else
            {
                itemStack.stackSize -= amount;
            }
        }

        i$ = itemStacks.iterator();

        while (i$.hasNext())
        {
            itemStack = (ItemStack)i$.next();
            dropAsEntity(source.worldObj, source.xCoord, source.yCoord, source.zCoord, itemStack);
        }

        itemStacks.clear();
    }

    public static ItemStack getFromInventory(IInventory inventory, ItemStack itemStackDestination, boolean simulate)
    {
        ItemStack ret = null;
        int toTransfer = itemStackDestination.stackSize;

        for (int i = 0; i < inventory.getSizeInventory(); ++i)
        {
            ItemStack itemStack = inventory.getStackInSlot(i);

            if (itemStack != null && isStackEqual(itemStack, itemStackDestination))
            {
                if (ret == null)
                {
                    ret = copyWithSize(itemStack, 0);
                }

                int transfer = Math.min(toTransfer, itemStack.stackSize);

                if (!simulate)
                {
                    itemStack.stackSize -= transfer;

                    if (itemStack.stackSize == 0)
                    {
                        inventory.setInventorySlotContents(i, (ItemStack)null);
                    }
                }

                toTransfer -= transfer;
                ret.stackSize += transfer;

                if (toTransfer == 0)
                {
                    return ret;
                }
            }
        }

        return ret;
    }

    public static int putInInventory(IInventory inventory, ItemStack itemStackSource, boolean simulate)
    {
        int transferred = 0;
        int i;
        ItemStack itemStack;
        int transfer;

        for (i = 0; i < inventory.getSizeInventory(); ++i)
        {
            if (inventory.isItemValidForSlot(i, itemStackSource))
            {
                itemStack = inventory.getStackInSlot(i);

                if (itemStack != null && itemStack.isItemEqual(itemStackSource))
                {
                    transfer = Math.min(itemStackSource.stackSize - transferred, itemStack.getMaxStackSize() - itemStack.stackSize);

                    if (!simulate)
                    {
                        itemStack.stackSize += transfer;
                    }

                    transferred += transfer;

                    if (transferred == itemStackSource.stackSize)
                    {
                        return transferred;
                    }
                }
            }
        }

        for (i = 0; i < inventory.getSizeInventory(); ++i)
        {
            if (inventory.isItemValidForSlot(i, itemStackSource))
            {
                itemStack = inventory.getStackInSlot(i);

                if (itemStack == null)
                {
                    transfer = Math.min(itemStackSource.stackSize - transferred, itemStackSource.getMaxStackSize());

                    if (!simulate)
                    {
                        ItemStack dest = copyWithSize(itemStackSource, transfer);
                        inventory.setInventorySlotContents(i, dest);
                    }

                    transferred += transfer;

                    if (transferred == itemStackSource.stackSize)
                    {
                        return transferred;
                    }
                }
            }
        }

        return transferred;
    }

    public static void dropAsEntity(World world, int x, int y, int z, ItemStack itemStack)
    {
        if (itemStack != null)
        {
            double f = 0.7D;
            double dx = (double)world.rand.nextFloat() * f + (1.0D - f) * 0.5D;
            double dy = (double)world.rand.nextFloat() * f + (1.0D - f) * 0.5D;
            double dz = (double)world.rand.nextFloat() * f + (1.0D - f) * 0.5D;
            EntityItem entityItem = new EntityItem(world, (double)x + dx, (double)y + dy, (double)z + dz, itemStack.copy());
            entityItem.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld(entityItem);
        }
    }

    public static ItemStack copyWithSize(ItemStack itemStack, int newSize)
    {
        ItemStack ret = itemStack.copy();
        ret.stackSize = newSize;
        return ret;
    }

    public static NBTTagCompound getOrCreateNbtData(ItemStack itemStack)
    {
        NBTTagCompound ret = itemStack.getTagCompound();

        if (ret == null)
        {
            ret = new NBTTagCompound("tag");
            itemStack.setTagCompound(ret);
        }

        return ret;
    }

    public static boolean isStackEqual(ItemStack stack1, ItemStack stack2)
    {
        return stack1 != null && stack2 != null && stack1.itemID == stack2.itemID && (!stack1.getHasSubtypes() && !stack1.isItemStackDamageable() || stack1.getItemDamage() == stack2.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }

    public static boolean damageItemStack(ItemStack itemStack, int amount)
    {
        if (itemStack.attemptDamageItem(amount, random))
        {
            --itemStack.stackSize;
            itemStack.setItemDamage(0);
            return itemStack.stackSize <= 0;
        }
        else
        {
            return false;
        }
    }
}
