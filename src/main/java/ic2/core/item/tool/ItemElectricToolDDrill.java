package ic2.core.item.tool;

import ic2.core.init.InternalName;
import net.minecraft.block.Block;
import net.minecraft.item.EnumToolMaterial;
import net.minecraftforge.common.Configuration;

public class ItemElectricToolDDrill extends ItemElectricToolDrill
{
    public ItemElectricToolDDrill(Configuration config, InternalName internalName)
    {
        super(config, internalName);
        super.toolMaterial = EnumToolMaterial.EMERALD;
        super.operationEnergyCost = 80;
        super.maxCharge = 10000;
        super.transferLimit = 100;
        super.tier = 1;
        super.efficiencyOnProperMaterial = 16.0F;
    }

    public void init()
    {
        super.init();
        super.mineableBlocks.add(Block.obsidian);
    }
}
