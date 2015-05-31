package nepisat.ee2.handlers;



import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nepisat.ee2.PECore;
import nepisat.ee2.api.IItemCharge;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.EnumSet;
import java.util.EnumSet;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.TickType;
public class KeyHandler extends cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler{


// I think I have to change the tickType, right? Setting it to Server didn't work

private EnumSet tickTypes = EnumSet.of(TickType.CLIENT);
private static boolean Charge = false;

public KeyHandler(KeyBinding[] keyBindings, boolean[] repeatings)
{
	super(keyBindings, repeatings);
	 this.keyDown = new boolean[keyBindings.length];
}


public static boolean keyPressed = false;
public String getLabel()
{


return "key.ee2.name";
}

public void keyDown(EnumSet<TickType> types, KeyBinding kb,boolean tickEnd, boolean isRepeat) {

	if(kb.keyCode==Keyboard.KEY_V){
		Charge = true;
	}
keyPressed = true;
}

public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
	if(kb.keyCode==Keyboard.KEY_V){
		Charge = false;
	}
keyPressed = false;
}

public EnumSet<TickType> ticks() {
return tickTypes;
}
public static boolean getCharge(){
	return Charge;
}
}

