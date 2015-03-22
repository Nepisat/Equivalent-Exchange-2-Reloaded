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
		
    	ItemStack stack = event.itemStack;
    	if(!stack.hasTagCompound()){
    		if(BlockEMCMapper.getEmc(stack)!=0){
						if(stack.stackSize <= 1){
							event.toolTip.add(EnumChatFormatting.GREEN + "EMC: "+EnumChatFormatting.WHITE+(BlockEMCMapper.getEmc(stack)));
						}else{
							event.toolTip.add(EnumChatFormatting.GREEN + "EMC: "+EnumChatFormatting.WHITE+BlockEMCMapper.getEmc(stack));
							event.toolTip.add(EnumChatFormatting.YELLOW + "スタックEMC: "+EnumChatFormatting.WHITE+BlockEMCMapper.getEmc(stack)*stack.stackSize);
						}
				}
			}
		}
		
}
