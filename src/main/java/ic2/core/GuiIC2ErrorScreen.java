package ic2.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;

@SideOnly(Side.CLIENT)
public class GuiIC2ErrorScreen extends GuiScreen
{
    private final String title;
    private final String error;

    public GuiIC2ErrorScreen(String title, String error)
    {
        this.title = title;
        this.error = error;
    }

    public void drawScreen(int par1, int par2, float par3)
    {
        this.drawBackground(0);
        this.drawCenteredString(super.fontRenderer, this.title, super.width / 2, super.height / 4 - 60 + 20, 16777215);
        int add = 0;
        String[] split = this.error.split("\n");
        String[] arr$ = split;
        int len$ = split.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            String s = arr$[i$];
            this.drawString(super.fontRenderer, s, super.width / 2 - 180, super.height / 4 - 60 + 60 - 10 + add, 10526880);
            add += 10;
        }
    }
}
