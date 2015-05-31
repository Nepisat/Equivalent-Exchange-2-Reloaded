package ic2.core.item.tool;

import ic2.core.IC2;
import ic2.core.init.InternalName;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

public class ItemElectricToolDrill extends ItemElectricTool
{
    public int soundTicker = 0;

    public ItemElectricToolDrill(Configuration config, InternalName internalName)
    {
        super(config, internalName, EnumToolMaterial.IRON, 50);
        super.maxCharge = 10000;
        super.transferLimit = 100;
        super.tier = 1;
        super.efficiencyOnProperMaterial = 8.0F;
    }

    public void init()
    {
        super.mineableBlocks.add(Block.cobblestone);
        super.mineableBlocks.add(Block.stoneSingleSlab);
        super.mineableBlocks.add(Block.stoneDoubleSlab);
        super.mineableBlocks.add(Block.stairsCobblestone);
        super.mineableBlocks.add(Block.stone);
        super.mineableBlocks.add(Block.sandStone);
        super.mineableBlocks.add(Block.stairsSandStone);
        super.mineableBlocks.add(Block.cobblestoneMossy);
        super.mineableBlocks.add(Block.oreIron);
        super.mineableBlocks.add(Block.blockIron);
        super.mineableBlocks.add(Block.oreCoal);
        super.mineableBlocks.add(Block.blockGold);
        super.mineableBlocks.add(Block.oreGold);
        super.mineableBlocks.add(Block.oreDiamond);
        super.mineableBlocks.add(Block.blockDiamond);
        super.mineableBlocks.add(Block.ice);
        super.mineableBlocks.add(Block.netherrack);
        super.mineableBlocks.add(Block.oreLapis);
        super.mineableBlocks.add(Block.blockLapis);
        super.mineableBlocks.add(Block.oreRedstone);
        super.mineableBlocks.add(Block.oreRedstoneGlowing);
        super.mineableBlocks.add(Block.brick);
        super.mineableBlocks.add(Block.stairsBrick);
        super.mineableBlocks.add(Block.glowStone);
        super.mineableBlocks.add(Block.grass);
        super.mineableBlocks.add(Block.dirt);
        super.mineableBlocks.add(Block.mycelium);
        super.mineableBlocks.add(Block.sand);
        super.mineableBlocks.add(Block.gravel);
        super.mineableBlocks.add(Block.snow);
        super.mineableBlocks.add(Block.blockSnow);
        super.mineableBlocks.add(Block.blockClay);
        super.mineableBlocks.add(Block.tilledField);
        super.mineableBlocks.add(Block.stoneBrick);
        super.mineableBlocks.add(Block.stairsStoneBrick);
        super.mineableBlocks.add(Block.netherBrick);
        super.mineableBlocks.add(Block.stairsNetherBrick);
        super.mineableBlocks.add(Block.slowSand);
        super.mineableBlocks.add(Block.anvil);
    }

    /**
     * Returns if the item (tool) can harvest results from the block type.
     */
    public boolean canHarvestBlock(Block block)
    {
        return block.blockMaterial != Material.rock && block.blockMaterial != Material.iron ? super.canHarvestBlock(block) : true;
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    public float getStrVsBlock(ItemStack itemstack, Block block)
    {
        ++this.soundTicker;

        if (this.soundTicker % 4 == 0)
        {
            IC2.platform.playSoundSp(this.getRandomDrillSound(), 1.0F, 1.0F);
        }

        return super.getStrVsBlock(itemstack, block);
    }

    public float getStrVsBlock(ItemStack itemstack, Block block, int md)
    {
        ++this.soundTicker;

        if (this.soundTicker % 4 == 0)
        {
            IC2.platform.playSoundSp(this.getRandomDrillSound(), 1.0F, 1.0F);
        }

        return super.getStrVsBlock(itemstack, block, md);
    }

    public String getRandomDrillSound()
    {
        switch (IC2.random.nextInt(4))
        {
            case 1:
                return "drillOne";

            case 2:
                return "drillTwo";

            case 3:
                return "drillThree";

            default:
                return "drill";
        }
    }
}
