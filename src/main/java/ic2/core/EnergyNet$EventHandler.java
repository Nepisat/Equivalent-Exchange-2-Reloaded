package ic2.core;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;

public class EnergyNet$EventHandler
{
    public EnergyNet$EventHandler()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @ForgeSubscribe
    public void onEnergyTileLoad(EnergyTileLoadEvent event)
    {
        EnergyNet.getForWorld(event.world).addTileEntity((TileEntity)event.energyTile);
    }

    @ForgeSubscribe
    public void onEnergyTileUnload(EnergyTileUnloadEvent event)
    {
        EnergyNet.getForWorld(event.world).removeTileEntity((TileEntity)event.energyTile);
    }

    @ForgeSubscribe
    public void onEnergyTileSource(EnergyTileSourceEvent event)
    {
        event.amount = EnergyNet.getForWorld(event.world).emitEnergyFrom((IEnergySource)event.energyTile, event.amount);
    }
}
