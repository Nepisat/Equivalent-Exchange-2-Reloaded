package ic2.core.block.invslot;

import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot$Access;
import ic2.core.block.invslot.InvSlot$InvSide;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InvSlot
{
    public final TileEntityInventory base;
    public final String name;
    public final int oldStartIndex;
    private final ItemStack[] contents;
    protected final InvSlot$Access access;
    public final InvSlot$InvSide preferredSide;

    public InvSlot(TileEntityInventory base, String name, int oldStartIndex, InvSlot$Access access, int count)
    {
        this(base, name, oldStartIndex, access, count, InvSlot$InvSide.ANY);
    }

    public InvSlot(TileEntityInventory base, String name, int oldStartIndex, InvSlot$Access access, int count, InvSlot$InvSide preferredSide)
    {
        this.contents = new ItemStack[count];
        this.base = base;
        this.name = name;
        this.oldStartIndex = oldStartIndex;
        this.access = access;
        this.preferredSide = preferredSide;
        base.addInvSlot(this);
    }

    public void readFromNbt(NBTTagCompound nbtTagCompound)
    {
        NBTTagList contentsTag = nbtTagCompound.getTagList("Contents");

        for (int i = 0; i < contentsTag.tagCount(); ++i)
        {
            NBTTagCompound contentTag = (NBTTagCompound)contentsTag.tagAt(i);
            int index = contentTag.getByte("Index") & 255;
            this.put(index, ItemStack.loadItemStackFromNBT(contentTag));
        }
    }

    public void writeToNbt(NBTTagCompound nbtTagCompound)
    {
        NBTTagList contentsTag = new NBTTagList();

        for (int i = 0; i < this.contents.length; ++i)
        {
            if (this.contents[i] != null)
            {
                NBTTagCompound contentTag = new NBTTagCompound();
                contentTag.setByte("Index", (byte)i);
                this.contents[i].writeToNBT(contentTag);
                contentsTag.appendTag(contentTag);
            }
        }

        nbtTagCompound.setTag("Contents", contentsTag);
    }

    public int size()
    {
        return this.contents.length;
    }

    public ItemStack get()
    {
        return this.get(0);
    }

    public ItemStack get(int index)
    {
        return this.contents[index];
    }

    public void put(ItemStack content)
    {
        this.put(0, content);
    }

    public void put(int index, ItemStack content)
    {
        this.contents[index] = content;
    }

    public void clear()
    {
        for (int i = 0; i < this.contents.length; ++i)
        {
            this.contents[i] = null;
        }
    }

    public boolean accepts(ItemStack itemStack)
    {
        return true;
    }

    public boolean canInput()
    {
        return this.access == InvSlot$Access.I || this.access == InvSlot$Access.IO;
    }

    public boolean canOutput()
    {
        return this.access == InvSlot$Access.O || this.access == InvSlot$Access.IO;
    }

    public boolean isEmpty()
    {
        ItemStack[] arr$ = this.contents;
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            ItemStack itemStack = arr$[i$];

            if (itemStack != null)
            {
                return false;
            }
        }

        return true;
    }

    public void organize()
    {
        for (int dstIndex = 0; dstIndex < this.contents.length - 1; ++dstIndex)
        {
            ItemStack dst = this.contents[dstIndex];

            if (dst == null || dst.stackSize < dst.getMaxStackSize())
            {
                for (int srcIndex = dstIndex + 1; srcIndex < this.contents.length; ++srcIndex)
                {
                    ItemStack src = this.contents[srcIndex];

                    if (src != null)
                    {
                        if (dst == null)
                        {
                            this.contents[srcIndex] = null;
                            dst = src;
                            this.contents[dstIndex] = src;
                        }
                        else if (StackUtil.isStackEqual(dst, src))
                        {
                            int space = dst.getMaxStackSize() - dst.stackSize;

                            if (src.stackSize > space)
                            {
                                src.stackSize -= space;
                                dst.stackSize += space;
                                break;
                            }

                            this.contents[srcIndex] = null;
                            dst.stackSize += src.stackSize;
                        }
                    }
                }
            }
        }
    }

    public String toString()
    {
        String ret = this.name + "[" + this.contents.length + "]: ";

        for (int i = 0; i < this.contents.length; ++i)
        {
            ret = ret + this.contents[i];

            if (i < this.contents.length - 1)
            {
                ret = ret + ", ";
            }
        }

        return ret;
    }
}
