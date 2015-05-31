package ic2.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public interface IHasGui extends IInventory
{
    ContainerBase getGuiContainer(EntityPlayer var1);

    @SideOnly(Side.CLIENT)
    GuiScreen getGui(EntityPlayer var1, boolean var2);

    void onGuiClosed(EntityPlayer var1);
}
