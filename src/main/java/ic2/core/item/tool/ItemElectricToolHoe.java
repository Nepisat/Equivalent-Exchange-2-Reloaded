package ic2.core.item.tool;

import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.init.InternalName;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;

public class ItemElectricToolHoe extends ItemElectricTool
{
    public ItemElectricToolHoe(Configuration config, InternalName internalName)
    {
        super(config, internalName, EnumToolMaterial.IRON, 50);
        super.maxCharge = 10000;
        super.transferLimit = 100;
        super.tier = 1;
        super.efficiencyOnProperMaterial = 16.0F;
    }

    public void init()
    {
        super.mineableBlocks.add(Block.dirt);
        super.mineableBlocks.add(Block.grass);
        super.mineableBlocks.add(Block.mycelium);
    }

    public boolean onBlockStartBreak(ItemStack itemStack, int x, int y, int z, EntityPlayer entityLiving)
    {
        ElectricItem.manager.use(itemStack, super.operationEnergyCost, entityLiving);
        return false;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!entityPlayer.canPlayerEdit(x, y, z, side, itemStack))
        {
            return false;
        }
        else if (!ElectricItem.manager.canUse(itemStack, super.operationEnergyCost))
        {
            return false;
        }
        else if (MinecraftForge.EVENT_BUS.post(new UseHoeEvent(entityPlayer, itemStack, world, x, y, z)))
        {
            return false;
        }
        else
        {
            int blockId = world.getBlockId(x, y, z);

            if ((side == 0 || !world.isAirBlock(x, y + 1, z) || blockId != Block.grass.blockID) && blockId != Block.dirt.blockID)
            {
                return false;
            }
            else
            {
                Block block = Block.tilledField;
                world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block.stepSound.getStepSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);

                if (IC2.platform.isSimulating())
                {
                    world.setBlock(x, y, z, block.blockID, 0, 3);
                    ElectricItem.manager.use(itemStack, super.operationEnergyCost, entityPlayer);
                }

                return true;
            }
        }
    }
}
