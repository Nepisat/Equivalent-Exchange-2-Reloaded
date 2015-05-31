package nepisat.ee2.events;

import nepisat.ee2.gameObjs.ObjHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FovChangeEvent 
{
	@ForgeSubscribe
	public void onFOVChange(FOVUpdateEvent event)
	{
		ItemStack legs = event.entity.getCurrentArmor(1);
		
		if (legs != null && legs.getItem() == ObjHandler.gemLegs)
		{
			event.newfov -= 0.5f;
		}
	}
}
