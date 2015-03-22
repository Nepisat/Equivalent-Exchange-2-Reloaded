package nepisat.ee2.event;

import nepisat.ee2.EMC.BlockEMCMapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class ToolTipEvent {
	@ForgeSubscribe(priority = EventPriority.LOWEST)
    public void addToolTip(ItemTooltipEvent event){
		/*
    	ItemStack stack = event.itemStack;
    	if(!stack.hasTagCompound()){
			for (int i = 0; i<173; i++){
				if (stack.itemID == BlockEMCMapper.is[i].itemID){
					if (BlockEMCMapper.is[i].hasTagCompound()){
						if(stack.stackSize <= 1){
							event.toolTip.add(EnumChatFormatting.GREEN + "EMC: "+EnumChatFormatting.WHITE+BlockEMCMapper.loadEMC(BlockEMCMapper.is[i]));
						}else{
							event.toolTip.add(EnumChatFormatting.GREEN + "EMC: "+EnumChatFormatting.WHITE+BlockEMCMapper.loadEMC(BlockEMCMapper.is[i]));
							event.toolTip.add(EnumChatFormatting.YELLOW + "スタックEMC: "+EnumChatFormatting.WHITE+BlockEMCMapper.loadEMC(BlockEMCMapper.is[i])*stack.stackSize);
						}
						
					}
				}
			}
    	}
    	*/
	}
	
}
