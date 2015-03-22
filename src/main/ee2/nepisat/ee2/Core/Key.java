package nepisat.ee2.Core;
import java.util.EnumSet;
import java.util.Iterator;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
public class Key extends KeyHandler
{
     private EnumSet tickTypes = EnumSet.of(TickType.CLIENT);

     public Key(KeyBinding[] keyBindings, boolean[] repeatings)
     {
         super(keyBindings, repeatings);
     }
     @Override
     public String getLabel()
     {
         return "Energy";
     }
     @Override
     public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat)
     {
    	
    	return;
         //what to do when key is pressed/down
     }
     @Override
     public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd)
     {
         //What to do when key is released/up
     }
     @Override
     public EnumSet<TickType> ticks()
     {
         return tickTypes;
     }
}