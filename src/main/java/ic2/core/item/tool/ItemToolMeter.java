package ic2.core.item.tool;

import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.core.EnergyNet;
import ic2.core.IC2;
import ic2.core.init.InternalName;
import ic2.core.item.ItemIC2;
import ic2.core.util.StackUtil;
import java.text.DecimalFormat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemToolMeter extends ItemIC2
{
    public ItemToolMeter(Configuration config, InternalName internalName)
    {
        super(config, internalName);
        super.maxStackSize = 1;
        this.setMaxDamage(0);
    }

    public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        TileEntity tileEntity = EnergyNet.getForWorld(world).getTileEntity(x, y, z);

        if ((tileEntity instanceof IEnergySource || tileEntity instanceof IEnergyConductor || tileEntity instanceof IEnergySink) && IC2.platform.isSimulating())
        {
            NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(itemstack);
            long currentTotalEnergyEmitted = EnergyNet.getForWorld(world).getTotalEnergyEmitted(tileEntity);
            long currentTotalEnergySunken = EnergyNet.getForWorld(world).getTotalEnergySunken(tileEntity);
            long currentMeasureTime = world.getWorldTime();

            if (nbtData.getInteger("lastMeasuredTileEntityX") == x && nbtData.getInteger("lastMeasuredTileEntityY") == y && nbtData.getInteger("lastMeasuredTileEntityZ") == z)
            {
                long measurePeriod = currentMeasureTime - nbtData.getLong("lastMeasureTime");

                if (measurePeriod < 1L)
                {
                    measurePeriod = 1L;
                }

                double deltaEmitted = (double)(currentTotalEnergyEmitted - nbtData.getLong("lastTotalEnergyEmitted")) / (double)measurePeriod;
                double deltaSunken = (double)(currentTotalEnergySunken - nbtData.getLong("lastTotalEnergySunken")) / (double)measurePeriod;
                DecimalFormat powerFormat = new DecimalFormat("0.##");
                IC2.platform.messagePlayer(entityplayer, "Measured power [EU/t]: " + powerFormat.format(deltaSunken) + " in " + powerFormat.format(deltaEmitted) + " out " + powerFormat.format(deltaSunken - deltaEmitted) + " gain" + " (avg. over " + measurePeriod + " ticks)", new Object[0]);
            }
            else
            {
                nbtData.setInteger("lastMeasuredTileEntityX", x);
                nbtData.setInteger("lastMeasuredTileEntityY", y);
                nbtData.setInteger("lastMeasuredTileEntityZ", z);
                IC2.platform.messagePlayer(entityplayer, "Starting new measurement", new Object[0]);
            }

            nbtData.setLong("lastTotalEnergyEmitted", currentTotalEnergyEmitted);
            nbtData.setLong("lastTotalEnergySunken", currentTotalEnergySunken);
            nbtData.setLong("lastMeasureTime", currentMeasureTime);
            return true;
        }
        else
        {
            return false;
        }
    }
}
