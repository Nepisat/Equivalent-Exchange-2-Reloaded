package ic2.core.util;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import java.util.EnumSet;
import net.minecraft.client.settings.KeyBinding;

class KeyboardClient$1 extends KeyHandler
{
    final KeyboardClient this$0;

    KeyboardClient$1(KeyboardClient var1, KeyBinding[] x0)
    {
        super(x0);
        this.this$0 = var1;
    }

    public String getLabel()
    {
        return "IC2Keyboard";
    }

    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.CLIENT);
    }

    public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {}

    public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {}
}
