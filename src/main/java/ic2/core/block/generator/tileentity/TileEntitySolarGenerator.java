package ic2.core.block.generator.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.block.generator.container.ContainerSolarGenerator;
import ic2.core.block.generator.gui.GuiSolarGenerator;
import java.util.Random;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenDesert;

public class TileEntitySolarGenerator extends TileEntityBaseGenerator
{
    public static Random randomizer = new Random();
    public int ticker;
    public boolean sunIsVisible = false;

    public TileEntitySolarGenerator()
    {
        super(1, 1);
        this.ticker = randomizer.nextInt(this.tickRate());
    }

    public void onLoaded()
    {
        super.onLoaded();
        this.updateSunVisibility();
    }

    public int gaugeFuelScaled(int i)
    {
        return i;
    }

    public boolean gainEnergy()
    {
        if (this.ticker++ % this.tickRate() == 0)
        {
            this.updateSunVisibility();
        }

        if (!this.sunIsVisible)
        {
            return false;
        }
        else
        {
            double gen = (double)IC2.energyGeneratorSolar / 100.0D;

            if (gen >= 1.0D || this.ticker % (100 - IC2.energyGeneratorSolar) == 0)
            {
                super.storage = (short)(super.storage + (int)Math.ceil(gen));
            }

            return true;
        }
    }

    public boolean gainFuel()
    {
        return false;
    }

    public void updateSunVisibility()
    {
        this.sunIsVisible = isSunVisible(super.worldObj, super.xCoord, super.yCoord + 1, super.zCoord);
    }

    public static boolean isSunVisible(World world, int x, int y, int z)
    {
        return world.isDaytime() && !world.provider.hasNoSky && world.canBlockSeeTheSky(x, y, z) && (world.getWorldChunkManager().getBiomeGenAt(x, z) instanceof BiomeGenDesert || !world.isRaining() && !world.isThundering());
    }

    public boolean needsFuel()
    {
        return true;
    }

    public String getInvName()
    {
        return "Solar Panel";
    }

    public ContainerBase getGuiContainer(EntityPlayer entityPlayer)
    {
        return new ContainerSolarGenerator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return new GuiSolarGenerator(new ContainerSolarGenerator(entityPlayer, this));
    }

    public int tickRate()
    {
        return 128;
    }

    public boolean delayActiveUpdate()
    {
        return true;
    }
}
