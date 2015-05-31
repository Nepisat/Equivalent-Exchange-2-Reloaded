package ic2.core.item.tool;

import ic2.api.event.PaintEvent;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.audio.PositionSpec;
import ic2.core.init.InternalName;
import ic2.core.item.ItemIC2;
import ic2.core.util.StackUtil;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.oredict.OreDictionary;

public class ItemToolPainter extends ItemIC2
{
    private static final String[] dyes = new String[] {"dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite"};
    public int color;

    public ItemToolPainter(Configuration config, InternalName internalName, int col)
    {
        super(config, internalName);
        this.setMaxDamage(32);
        this.setMaxStackSize(1);
        this.color = col;
        MinecraftForge.EVENT_BUS.register(this);
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float a, float b, float c)
    {
        PaintEvent event = new PaintEvent(world, i, j, k, side, this.color);
        MinecraftForge.EVENT_BUS.post(event);

        if (event.painted)
        {
            if (IC2.platform.isSimulating())
            {
                this.damagePainter(entityplayer);
            }

            if (IC2.platform.isRendering())
            {
                IC2.audioManager.playOnce(entityplayer, PositionSpec.Hand, "Tools/Painter.ogg", true, IC2.audioManager.defaultVolume);
            }

            return true;
        }
        else
        {
            int blockId = world.getBlockId(i, j, k);

            if (Block.blocksList[blockId] != null && Block.blocksList[blockId].recolourBlock(world, i, j, k, ForgeDirection.VALID_DIRECTIONS[side], BlockColored.getBlockFromDye(this.color)))
            {
                this.damagePainter(entityplayer);

                if (IC2.platform.isRendering())
                {
                    IC2.audioManager.playOnce(entityplayer, PositionSpec.Hand, "Tools/Painter.ogg", true, IC2.audioManager.defaultVolume);
                }

                return true;
            }
            else
            {
                return false;
            }
        }
    }

    @ForgeSubscribe
    public boolean onEntityInteract(EntityInteractEvent event)
    {
        EntityPlayer player = event.entityPlayer;
        Entity entity = event.entity;

        if (!entity.worldObj.isRemote && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().itemID == super.itemID)
        {
            boolean ret = true;

            if (entity instanceof EntitySheep)
            {
                EntitySheep sheep = (EntitySheep)entity;
                int clr = BlockColored.getBlockFromDye(this.color);

                if (sheep.getFleeceColor() != clr)
                {
                    ret = false;
                    ((EntitySheep)entity).setFleeceColor(clr);
                    this.damagePainter(player);
                }
            }

            return ret;
        }
        else
        {
            return true;
        }
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (IC2.platform.isSimulating() && IC2.keyboard.isModeSwitchKeyDown(entityplayer))
        {
            NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemstack);
            boolean newValue = !nbtData.getBoolean("autoRefill");
            nbtData.setBoolean("autoRefill", newValue);

            if (newValue)
            {
                IC2.platform.messagePlayer(entityplayer, "Painter automatic refill mode enabled", new Object[0]);
            }
            else
            {
                IC2.platform.messagePlayer(entityplayer, "Painter automatic refill mode disabled", new Object[0]);
            }
        }

        return itemstack;
    }

    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean debugTooltips)
    {
        info.add(StatCollector.translateToLocal(Item.dyePowder.getUnlocalizedName(new ItemStack(Item.dyePowder, 1, this.color)) + ".name"));
    }

    private void damagePainter(EntityPlayer player)
    {
        if (player.inventory.mainInventory[player.inventory.currentItem].getItemDamage() >= player.inventory.mainInventory[player.inventory.currentItem].getMaxDamage() - 1)
        {
            int dyeIS = -1;
            NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(player.inventory.mainInventory[player.inventory.currentItem]);

            if (nbtData.getBoolean("autoRefill"))
            {
                for (int l = 0; l < player.inventory.mainInventory.length; ++l)
                {
                    if (player.inventory.mainInventory[l] != null)
                    {
                        Iterator i$ = OreDictionary.getOres(dyes[this.color]).iterator();

                        while (i$.hasNext())
                        {
                            ItemStack ore = (ItemStack)i$.next();

                            if (ore.isItemEqual(player.inventory.mainInventory[l]))
                            {
                                dyeIS = l;
                                break;
                            }
                        }
                    }
                }
            }

            if (dyeIS == -1)
            {
                player.inventory.mainInventory[player.inventory.currentItem] = Ic2Items.painter.copy();
            }
            else
            {
                if (--player.inventory.mainInventory[dyeIS].stackSize <= 0)
                {
                    player.inventory.mainInventory[dyeIS] = null;
                }

                player.inventory.mainInventory[player.inventory.currentItem].setItemDamage(0);
            }
        }
        else
        {
            player.inventory.mainInventory[player.inventory.currentItem].damageItem(1, player);
        }

        player.openContainer.detectAndSendChanges();
    }
}
