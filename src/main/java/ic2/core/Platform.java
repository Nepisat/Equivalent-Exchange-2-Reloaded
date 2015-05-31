package ic2.core;

import cpw.mods.fml.common.FMLCommonHandler;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.IBlockAccess;

public class Platform
{
    public boolean isSimulating()
    {
        return !FMLCommonHandler.instance().getEffectiveSide().isClient();
    }

    public boolean isRendering()
    {
        return !this.isSimulating();
    }

    public void displayError(String error)
    {
        throw new RuntimeException(("IndustrialCraft 2 Error\n\n=== IndustrialCraft 2 Error ===\n\n" + error + "\n\n===============================\n").replace("\n", System.getProperty("line.separator")));
    }

    public void displayError(Exception e, String error)
    {
        this.displayError("An unexpected Exception occured.\n\n" + this.getStackTrace(e) + "\n" + error);
    }

    public String getStackTrace(Exception e)
    {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        return writer.toString();
    }

    public EntityPlayer getPlayerInstance()
    {
        return null;
    }

    public void messagePlayer(EntityPlayer player, String message, Object ... args)
    {
        if (player instanceof EntityPlayerMP)
        {
            ChatMessageComponent msg;

            if (args.length > 0)
            {
                msg = ChatMessageComponent.createFromTranslationWithSubstitutions(message, args);
            }
            else
            {
                msg = ChatMessageComponent.createFromTranslationKey(message);
            }

            ((EntityPlayerMP)player).sendChatToPlayer(msg);
        }
    }

    public boolean launchGui(EntityPlayer player, IHasGui inventory)
    {
        if (player instanceof EntityPlayerMP)
        {
            EntityPlayerMP entityPlayerMp = (EntityPlayerMP)player;
            int windowId = entityPlayerMp.currentWindowId % 100 + 1;
            entityPlayerMp.currentWindowId = windowId;
            entityPlayerMp.closeContainer();
            IC2.network.initiateGuiDisplay(entityPlayerMp, inventory, windowId);
            player.openContainer = inventory.getGuiContainer(player);
            player.openContainer.windowId = windowId;
            player.openContainer.addCraftingToCrafters(entityPlayerMp);
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean launchGuiClient(EntityPlayer player, IHasGui inventory, boolean isAdmin)
    {
        return false;
    }

    public void profilerStartSection(String section) {}

    public void profilerEndSection() {}

    public void profilerEndStartSection(String section) {}

    public File getMinecraftDir()
    {
        return new File(".");
    }

    public void playSoundSp(String sound, float f, float g) {}

    public void resetPlayerInAirTime(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
        {
            ((EntityPlayerMP)player).playerNetServerHandler.ticksForFloatKick = 0;
        }
    }

    public int getBlockTexture(Block block, IBlockAccess world, int x, int y, int z, int side)
    {
        return 0;
    }

    public int addArmor(String name)
    {
        return 0;
    }

    public void removePotion(EntityLivingBase entity, int potion)
    {
        entity.removePotionEffect(potion);
    }

    public int getRenderId(String name)
    {
        return -1;
    }

    public void onPostInit() {}
}
