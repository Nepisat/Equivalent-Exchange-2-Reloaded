package ic2.core.block.machine.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.recipe.Recipes;
import ic2.core.BasicListRecipeManager;
import ic2.core.Ic2Items;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlot$Access;
import ic2.core.block.invslot.InvSlot$InvSide;
import ic2.core.block.machine.ContainerStandardMachine;
import ic2.core.block.machine.gui.GuiRecycler;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class TileEntityRecycler extends TileEntityStandardMachine
{
    public TileEntityRecycler()
    {
        super(1, 45);
        super.inputSlot = new InvSlot(this, "input", 0, InvSlot$Access.I, 1, InvSlot$InvSide.TOP);
    }

    public static void init(Configuration config)
    {
        Recipes.recyclerBlacklist = new BasicListRecipeManager();
        Recipes.recyclerBlacklist.add(new ItemStack(Block.thinGlass));
        Recipes.recyclerBlacklist.add(new ItemStack(Item.stick));
        Recipes.recyclerBlacklist.add(new ItemStack(Item.snowball));
        Recipes.recyclerBlacklist.add(Ic2Items.scaffold);

        if (config != null)
        {
            Property prop = config.get("general", "recyclerBlacklist", getRecyclerBlacklistString());
            prop.comment = "List of blocks and items which should not be turned into scrap by the recycler. Comma separated, format is id-metadata";
            setRecyclerBlacklistFromString(prop.getString());
        }
    }

    public void operate()
    {
        if (this.canOperate())
        {
            boolean itemBlacklisted = getIsItemBlacklisted(super.inputSlot.get());
            --super.inputSlot.get().stackSize;

            if (super.inputSlot.get().stackSize <= 0)
            {
                super.inputSlot.clear();
            }

            if (super.worldObj.rand.nextInt(recycleChance()) == 0 && !itemBlacklisted)
            {
                super.outputSlot.add(Ic2Items.scrap.copy());
            }
        }
    }

    public boolean canOperate()
    {
        return super.inputSlot.isEmpty() ? false : super.outputSlot.canAdd(Ic2Items.scrap);
    }

    public ItemStack getResultFor(ItemStack itemstack, boolean adjustInput)
    {
        return null;
    }

    public String getInvName()
    {
        return "Recycler";
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return new GuiRecycler(new ContainerStandardMachine(entityPlayer, this));
    }

    public static int recycleChance()
    {
        return 8;
    }

    public String getStartSoundFile()
    {
        return "Machines/RecyclerOp.ogg";
    }

    public String getInterruptSoundFile()
    {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate()
    {
        return 0.85F;
    }

    public static boolean getIsItemBlacklisted(ItemStack itemStack)
    {
        return Recipes.recyclerBlacklist.contains(itemStack);
    }

    private static String getRecyclerBlacklistString()
    {
        StringBuilder ret = new StringBuilder();
        boolean first = true;
        Iterator i$ = Recipes.recyclerBlacklist.iterator();

        while (i$.hasNext())
        {
            ItemStack entry = (ItemStack)i$.next();

            if (entry != null)
            {
                if (first)
                {
                    first = false;
                }
                else
                {
                    ret.append(", ");
                }

                ret.append(entry.itemID);

                if (entry.getItemDamage() != 0)
                {
                    ret.append("-");
                    ret.append(entry.getItemDamage());
                }
            }
        }

        return ret.toString();
    }

    private static void setRecyclerBlacklistFromString(String str)
    {
        String[] strParts = str.trim().split("\\s*,\\s*");
        String[] arr$ = strParts;
        int len$ = strParts.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            String strPart = arr$[i$];
            String[] idMeta = strPart.split("\\s*-\\s*");

            if (idMeta[0].length() != 0)
            {
                int blockId = Integer.parseInt(idMeta[0]);
                int metaData = 32767;

                if (idMeta.length == 2)
                {
                    metaData = Integer.parseInt(idMeta[1]);
                }

                ItemStack is = new ItemStack(blockId, 1, metaData);
                Recipes.recyclerBlacklist.add(is);
            }
        }
    }
}
