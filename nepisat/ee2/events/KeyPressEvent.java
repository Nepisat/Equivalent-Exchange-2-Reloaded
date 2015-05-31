package nepisat.ee2.events;

import nepisat.ee2.events.InputEvent.KeyInputEvent;
import nepisat.ee2.utils.KeyBinds;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KeyPressEvent 
{
	@ForgeSubscribe
	public void keyPress(KeyInputEvent event)
	{
		for (int i = 0; i < KeyBinds.array.length; i++)
		{
			if (KeyBinds.isPressed(i))
			{
			//	PacketHandler.sendToServer(new KeyPressPKT(i));
			}
		}
	}
}
