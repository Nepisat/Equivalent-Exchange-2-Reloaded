package ic2.core.util;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.IC2;
import ic2.core.util.KeyboardClient$1;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

@SideOnly(Side.CLIENT)
public class KeyboardClient extends Keyboard
{
    private Minecraft mc = Minecraft.getMinecraft();
    private KeyBinding altKey = new KeyBinding("ALT Key", 56);
    private KeyBinding boostKey = new KeyBinding("Boost Key", 29);
    private KeyBinding modeSwitchKey = new KeyBinding("Mode Switch Key", 50);
    private KeyBinding sideinventoryKey = new KeyBinding("Side Inventory Key", 46);
    private int lastKeyState = 0;

    public KeyboardClient()
    {
        KeyBindingRegistry.registerKeyBinding(new KeyboardClient$1(this, new KeyBinding[] {this.altKey, this.boostKey, this.modeSwitchKey, this.sideinventoryKey}));
    }

    public void sendKeyUpdate()
    {
        int currentKeyState = (this.altKey.pressed ? 1 : 0) << 0 | (this.boostKey.pressed ? 1 : 0) << 1 | (this.mc.gameSettings.keyBindForward.pressed ? 1 : 0) << 2 | (this.modeSwitchKey.pressed ? 1 : 0) << 3 | (this.mc.gameSettings.keyBindJump.pressed ? 1 : 0) << 4 | (this.sideinventoryKey.pressed ? 1 : 0) << 5;

        if (currentKeyState != this.lastKeyState)
        {
            IC2.network.initiateKeyUpdate(currentKeyState);
            super.processKeyUpdate(IC2.platform.getPlayerInstance(), currentKeyState);
            this.lastKeyState = currentKeyState;
        }
    }
}
