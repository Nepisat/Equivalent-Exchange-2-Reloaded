package nepisat.ee2.events;

import nepisat.ee2.handlers.PlayerChecks;
import nepisat.ee2.handlers.PlayerTimers;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.common.TickType;
public class TickEvents
{
	@ForgeSubscribe
	public void onServerTick(TickEvent.ServerTickEvent event)
	{
		if (event.phase == TickEvent.Phase.END)
		{
			PlayerChecks.update();
			PlayerTimers.update();
		}
	}
}
