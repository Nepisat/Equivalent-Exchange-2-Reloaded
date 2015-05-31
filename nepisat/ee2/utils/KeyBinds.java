package nepisat.ee2.utils;

import nepisat.ee2.PECore;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

public final class KeyBinds 
{
	public static KeyBinding[] array = new KeyBinding[] 
	{
		new KeyBinding("Charge", Keyboard.KEY_V),
		new KeyBinding("Mode", Keyboard.KEY_G),
		new KeyBinding("Fire Projectile", Keyboard.KEY_R),
		new KeyBinding("Extra Function", Keyboard.KEY_C),
		new KeyBinding("Armor effects", Keyboard.KEY_F)
	};
	
	public static int getChargeKeyCode()
	{
		return array[0].keyCode;
	}
	
	public static int getModeKeyCode()
	{
		return array[1].keyCode;
	}
	
	public static int getProjectileKeyCode()
	{
		return array[2].keyCode;
	}
	
	public static int getExtraFuncKeyCode()
	{
		return array[3].keyCode;
	}
	
	public static int getArmorEffectsKeyCode()
	{
		return array[4].keyCode;
	}
	
	public static boolean isPressed(int index)
	{
		
		return array[index].isPressed();
	}
	
	public static int getKeyCode(int index)
	{
		return array[index].keyCode;
	}
}
