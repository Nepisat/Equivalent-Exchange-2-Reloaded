package ic2.core.item.tool;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.event.RetextureEvent;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.IC2;
import ic2.core.init.InternalName;
import ic2.core.item.ItemIC2;
import ic2.core.network.IPlayerItemDataListener;
import ic2.core.util.StackUtil;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet15Place;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

public class ItemObscurator extends ItemIC2 implements IElectricItem, IPlayerItemDataListener
{
    private final int scanOperationCost = 20000;
    private final int printOperationCost = 5000;

    public ItemObscurator(Configuration config, InternalName internalName)
    {
        super(config, internalName);
        this.setMaxDamage(27);
        this.setMaxStackSize(1);

        if (IC2.platform.isRendering())
        {
            MinecraftForgeClient.registerItemRenderer(super.itemID, new RenderObscurator());
        }
    }

    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!entityPlayer.isSneaking() && ElectricItem.manager.canUse(itemStack, 5000))
        {
            NBTTagCompound blockId1 = StackUtil.getOrCreateNbtData(itemStack);
            int block1 = blockId1.getInteger("referencedBlockId");

            if (block1 > 0 && block1 < Block.blocksList.length && Block.blocksList[block1] != null && this.isBlockSuitable(Block.blocksList[block1]))
            {
                if (IC2.platform.isSimulating())
                {
                    RetextureEvent meta1 = new RetextureEvent(world, x, y, z, side, blockId1.getInteger("referencedBlockId"), blockId1.getInteger("referencedMeta"), blockId1.getInteger("referencedSide"));
                    MinecraftForge.EVENT_BUS.post(meta1);

                    if (meta1.applied)
                    {
                        ElectricItem.manager.use(itemStack, 5000, entityPlayer);
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    PacketDispatcher.sendPacketToServer(new Packet15Place(x, y, z, side, entityPlayer.inventory.getCurrentItem(), hitX, hitY, hitZ));
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            if (entityPlayer.isSneaking() && IC2.platform.isRendering() && ElectricItem.manager.canUse(itemStack, 20000))
            {
                int blockId = world.getBlockId(x, y, z);
                Block block = Block.blocksList[blockId];

                if (block != null && !block.isAirBlock(world, x, y, z) && this.isBlockSuitable(block))
                {
                    int meta = world.getBlockMetadata(x, y, z);

                    try
                    {
                        Icon nbtData1 = block.getIcon(side, meta);
                        Icon textureWorld = block.getBlockTexture(world, x, y, z, side);

                        if (nbtData1 == null || nbtData1 != textureWorld)
                        {
                            return false;
                        }
                    }
                    catch (Exception var16)
                    {
                        return false;
                    }

                    NBTTagCompound nbtData11 = StackUtil.getOrCreateNbtData(itemStack);

                    if (nbtData11.getInteger("referencedBlockId") != blockId || nbtData11.getInteger("referencedMeta") != meta || nbtData11.getInteger("referencedSide") != side)
                    {
                        IC2.network.sendPlayerItemData(entityPlayer, entityPlayer.inventory.currentItem, new Object[] {Integer.valueOf(blockId), Integer.valueOf(meta), Integer.valueOf(side)});
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public void onPlayerItemNetworkData(EntityPlayer entityPlayer, int slot, Object ... data)
    {
        ItemStack itemStack = entityPlayer.inventory.mainInventory[slot];

        if (ElectricItem.manager.use(itemStack, 20000, entityPlayer))
        {
            NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemStack);
            nbtData.setInteger("referencedBlockId", ((Integer)data[0]).intValue());
            nbtData.setInteger("referencedMeta", ((Integer)data[1]).intValue());
            nbtData.setInteger("referencedSide", ((Integer)data[2]).intValue());
        }
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(int i, CreativeTabs tabs, List itemList)
    {
        ItemStack charged = new ItemStack(this, 1);
        ElectricItem.manager.charge(charged, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false);
        itemList.add(charged);
        itemList.add(new ItemStack(this, 1, this.getMaxDamage()));
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
        return 100000;
    }

    public int getTier(ItemStack itemStack)
    {
        return 2;
    }

    public int getTransferLimit(ItemStack itemStack)
    {
        return 250;
    }

    private boolean isBlockSuitable(Block block)
    {
        return block.renderAsNormalBlock();
    }
}
