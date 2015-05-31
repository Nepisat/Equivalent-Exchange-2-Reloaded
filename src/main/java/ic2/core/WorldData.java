package ic2.core;

import ic2.core.network.NetworkManager$TileEntityField;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.WeakHashMap;
import net.minecraft.world.World;

public class WorldData
{
    private static Map<World, WorldData> mapping = new WeakHashMap();
    public Queue<ITickCallback> singleTickCallbacks = new ArrayDeque();
    public Set<ITickCallback> continuousTickCallbacks = new HashSet();
    public boolean continuousTickCallbacksInUse = false;
    public List<ITickCallback> continuousTickCallbacksToAdd = new ArrayList();
    public List<ITickCallback> continuousTickCallbacksToRemove = new ArrayList();
    public EnergyNet energyNet = new EnergyNet();
    public Set<NetworkManager$TileEntityField> networkedFieldsToUpdate = new HashSet();
    public int ticksLeftToNetworkUpdate = 2;

    public static WorldData get(World world)
    {
        if (world == null)
        {
            throw new IllegalArgumentException("world is null");
        }
        else
        {
            WorldData ret = (WorldData)mapping.get(world);

            if (ret == null)
            {
                ret = new WorldData();
                mapping.put(world, ret);
            }

            return ret;
        }
    }

    public static void onWorldUnload(World world)
    {
        mapping.remove(world);
    }
}
