package ic2.core.network;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkItemEventListener;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.WorldData;
import ic2.core.item.IHandHeldInventory;
import ic2.core.item.armor.ItemArmorQuantumSuit;
import ic2.core.network.NetworkManager$TileEntityField;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class NetworkManager implements IPacketHandler
{
    public static final int updatePeriod = 2;
    private static int maxNetworkedFieldsToUpdate = 4000;
    private static final int maxPacketDataLength = 32766;

    public void onTick(World world)
    {
        WorldData worldData = WorldData.get(world);

        if (--worldData.ticksLeftToNetworkUpdate == 0)
        {
            this.sendUpdatePacket(world);
            worldData.ticksLeftToNetworkUpdate = 2;
        }
    }

    public void sendPlayerItemData(EntityPlayer player, int slot, Object ... data)
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(buffer);

        try
        {
            os.writeByte(10);
            os.writeByte(slot);
            os.writeInt(player.inventory.mainInventory[slot].itemID);
            os.writeShort(data.length);
            Object[] packet = data;
            int len$ = data.length;
            int i$ = 0;

            while (true)
            {
                if (i$ >= len$)
                {
                    os.close();
                    break;
                }

                Object o = packet[i$];
                DataEncoder.encode(os, o);
                ++i$;
            }
        }
        catch (IOException var10)
        {
            throw new RuntimeException(var10);
        }

        Packet250CustomPayload var11 = new Packet250CustomPayload();
        var11.channel = "ic2";
        var11.isChunkDataPacket = false;
        var11.data = buffer.toByteArray();
        var11.length = buffer.size();

        if (IC2.platform.isSimulating())
        {
            PacketDispatcher.sendPacketToPlayer(var11, (Player)player);
        }
        else
        {
            PacketDispatcher.sendPacketToServer(var11);
        }
    }

    public void updateTileEntityField(TileEntity te, String field)
    {
        WorldData worldData = WorldData.get(te.worldObj);
        worldData.networkedFieldsToUpdate.add(new NetworkManager$TileEntityField(this, te, field));

        if (worldData.networkedFieldsToUpdate.size() >= maxNetworkedFieldsToUpdate)
        {
            this.sendUpdatePacket(te.worldObj);
        }
    }

    public void initiateTileEntityEvent(TileEntity te, int event, boolean limitRange)
    {
        int maxDistance = limitRange ? 400 : MinecraftServer.getServer().getConfigurationManager().getEntityViewDistance() + 16;
        World world = te.worldObj;
        Packet250CustomPayload packet = null;
        Iterator i$ = world.playerEntities.iterator();

        while (i$.hasNext())
        {
            Object obj = i$.next();
            EntityPlayerMP entityPlayer = (EntityPlayerMP)obj;
            int distanceX = te.xCoord - (int)entityPlayer.posX;
            int distanceZ = te.zCoord - (int)entityPlayer.posZ;
            int distance;

            if (limitRange)
            {
                distance = distanceX * distanceX + distanceZ * distanceZ;
            }
            else
            {
                distance = Math.max(Math.abs(distanceX), Math.abs(distanceZ));
            }

            if (distance <= maxDistance)
            {
                if (packet == null)
                {
                    try
                    {
                        ByteArrayOutputStream e = new ByteArrayOutputStream();
                        DataOutputStream os = new DataOutputStream(e);
                        os.writeByte(1);
                        os.writeInt(world.provider.dimensionId);
                        os.writeInt(te.xCoord);
                        os.writeInt(te.yCoord);
                        os.writeInt(te.zCoord);
                        os.writeInt(event);
                        os.close();
                        packet = new Packet250CustomPayload();
                        packet.channel = "ic2";
                        packet.isChunkDataPacket = false;
                        packet.data = e.toByteArray();
                        packet.length = e.size();
                    }
                    catch (IOException var15)
                    {
                        throw new RuntimeException(var15);
                    }
                }

                PacketDispatcher.sendPacketToPlayer(packet, (Player)entityPlayer);
            }
        }
    }

    public void initiateItemEvent(EntityPlayer player, ItemStack itemStack, int event, boolean limitRange)
    {
        if (player.username.length() <= 127)
        {
            int maxDistance = limitRange ? 400 : MinecraftServer.getServer().getConfigurationManager().getEntityViewDistance() + 16;
            Packet250CustomPayload packet = null;
            Iterator i$ = player.worldObj.playerEntities.iterator();

            while (i$.hasNext())
            {
                Object obj = i$.next();
                EntityPlayerMP entityPlayer = (EntityPlayerMP)obj;
                int distanceX = (int)player.posX - (int)entityPlayer.posX;
                int distanceZ = (int)player.posZ - (int)entityPlayer.posZ;
                int distance;

                if (limitRange)
                {
                    distance = distanceX * distanceX + distanceZ * distanceZ;
                }
                else
                {
                    distance = Math.max(Math.abs(distanceX), Math.abs(distanceZ));
                }

                if (distance <= maxDistance)
                {
                    if (packet == null)
                    {
                        try
                        {
                            ByteArrayOutputStream e = new ByteArrayOutputStream();
                            DataOutputStream os = new DataOutputStream(e);
                            os.writeByte(2);
                            os.writeByte(player.username.length());
                            os.writeChars(player.username);
                            os.writeInt(itemStack.itemID);
                            os.writeInt(itemStack.getItemDamage());
                            os.writeInt(event);
                            os.close();
                            packet = new Packet250CustomPayload();
                            packet.channel = "ic2";
                            packet.isChunkDataPacket = false;
                            packet.data = e.toByteArray();
                            packet.length = e.size();
                        }
                        catch (IOException var15)
                        {
                            throw new RuntimeException(var15);
                        }
                    }

                    PacketDispatcher.sendPacketToPlayer(packet, (Player)entityPlayer);
                }
            }
        }
    }

    public void announceBlockUpdate(World world, int x, int y, int z)
    {
        Packet250CustomPayload packet = null;
        Iterator i$ = world.playerEntities.iterator();

        while (i$.hasNext())
        {
            Object obj = i$.next();
            EntityPlayerMP entityPlayer = (EntityPlayerMP)obj;
            int distance = Math.min(Math.abs(x - (int)entityPlayer.posX), Math.abs(z - (int)entityPlayer.posZ));

            if (distance <= MinecraftServer.getServer().getConfigurationManager().getEntityViewDistance() + 16)
            {
                if (packet == null)
                {
                    try
                    {
                        ByteArrayOutputStream e = new ByteArrayOutputStream();
                        DataOutputStream os = new DataOutputStream(e);
                        os.writeByte(3);
                        os.writeInt(world.provider.dimensionId);
                        os.writeInt(x);
                        os.writeInt(y);
                        os.writeInt(z);
                        os.writeShort(world.getBlockId(x, y, z));
                        os.writeByte(world.getBlockMetadata(x, y, z));
                        os.close();
                        packet = new Packet250CustomPayload();
                        packet.channel = "ic2";
                        packet.isChunkDataPacket = true;
                        packet.data = e.toByteArray();
                        packet.length = e.size();
                    }
                    catch (IOException var12)
                    {
                        throw new RuntimeException(var12);
                    }
                }

                PacketDispatcher.sendPacketToPlayer(packet, (Player)entityPlayer);
            }
        }
    }

    @Deprecated
    public void requestInitialData(INetworkDataProvider dataProvider) {}

    public void initiateClientItemEvent(ItemStack itemStack, int event) {}

    public void initiateClientTileEntityEvent(TileEntity te, int event) {}

    public void initiateGuiDisplay(EntityPlayerMP entityPlayer, IHasGui inventory, int windowId)
    {
        try
        {
            ByteArrayOutputStream e = new ByteArrayOutputStream();
            DataOutputStream os = new DataOutputStream(e);
            os.writeByte(4);
            MinecraftServer server = MinecraftServer.getServer();
            boolean isAdmin = server.getConfigurationManager().isPlayerOpped(entityPlayer.username);
            os.writeByte(isAdmin ? 1 : 0);

            if (inventory instanceof TileEntity)
            {
                TileEntity packet = (TileEntity)inventory;
                os.writeByte(0);
                os.writeInt(packet.worldObj.provider.dimensionId);
                os.writeInt(packet.xCoord);
                os.writeInt(packet.yCoord);
                os.writeInt(packet.zCoord);
            }
            else if (entityPlayer.inventory.getCurrentItem() != null && entityPlayer.inventory.getCurrentItem().getItem() instanceof IHandHeldInventory)
            {
                os.writeByte(1);
                os.writeInt(entityPlayer.inventory.currentItem);
            }
            else
            {
                IC2.platform.displayError("An unknown GUI type was attempted to be displayed.\nThis could happen due to corrupted data from a player or a bug.\n\n(Technical information: " + inventory + ")");
            }

            os.writeInt(windowId);
            os.close();
            Packet250CustomPayload packet1 = new Packet250CustomPayload();
            packet1.channel = "ic2";
            packet1.isChunkDataPacket = false;
            packet1.data = e.toByteArray();
            packet1.length = e.size();
            PacketDispatcher.sendPacketToPlayer(packet1, (Player)entityPlayer);
        }
        catch (IOException var9)
        {
            throw new RuntimeException(var9);
        }
    }

    public void sendInitialData(TileEntity te, EntityPlayerMP player)
    {
        if (te instanceof INetworkDataProvider)
        {
            WorldData worldData = WorldData.get(te.worldObj);
            Iterator i$ = ((INetworkDataProvider)te).getNetworkedFields().iterator();

            while (i$.hasNext())
            {
                String field = (String)i$.next();
                worldData.networkedFieldsToUpdate.add(new NetworkManager$TileEntityField(this, te, field, player));

                if (worldData.networkedFieldsToUpdate.size() >= maxNetworkedFieldsToUpdate)
                {
                    this.sendUpdatePacket(te.worldObj);
                }
            }
        }
    }

    private void sendUpdatePacket(World world)
    {
        WorldData worldData = WorldData.get(world);

        if (!worldData.networkedFieldsToUpdate.isEmpty())
        {
            Iterator i$ = world.playerEntities.iterator();

            while (i$.hasNext())
            {
                Object obj = i$.next();
                EntityPlayerMP entityPlayer = (EntityPlayerMP)obj;
                this.sendUpdatePacket(worldData.networkedFieldsToUpdate, entityPlayer);
            }

            worldData.networkedFieldsToUpdate.clear();
        }
    }

    private void sendUpdatePacket(Collection<NetworkManager$TileEntityField> networkedFieldsToUpdate, EntityPlayer entityPlayer)
    {
        byte[] fieldData = this.getFieldData(networkedFieldsToUpdate, entityPlayer);

        if (fieldData.length > 32766)
        {
            Object packet;

            if (networkedFieldsToUpdate instanceof List)
            {
                packet = (List)networkedFieldsToUpdate;
            }
            else
            {
                packet = new ArrayList(networkedFieldsToUpdate);
            }

            do
            {
                maxNetworkedFieldsToUpdate = Math.min(((List)packet).size(), maxNetworkedFieldsToUpdate) * 7 / 8;
                fieldData = this.getFieldData(((List)packet).subList(0, maxNetworkedFieldsToUpdate), entityPlayer);
            }
            while (fieldData.length > 32766);

            this.sendUpdatePacket(((List)packet).subList(maxNetworkedFieldsToUpdate, ((List)packet).size()), entityPlayer);
        }

        if (fieldData.length > 1)
        {
            Packet250CustomPayload packet1 = new Packet250CustomPayload();
            packet1.channel = "ic2";
            packet1.isChunkDataPacket = true;
            packet1.data = fieldData;
            packet1.length = fieldData.length;
            PacketDispatcher.sendPacketToPlayer(packet1, (Player)entityPlayer);
        }
    }

    public void onPacketData(INetworkManager network, Packet250CustomPayload packet, Player iplayer)
    {
        if (packet.data.length != 0)
        {
            EntityPlayer player = (EntityPlayer)iplayer;
            ByteArrayInputStream isRaw = new ByteArrayInputStream(packet.data, 1, packet.data.length - 1);

            try
            {
                DataInputStream e;
                int itemId;
                int itemStack;
                int var20;
                int var22;

                switch (packet.data[0])
                {
                    case 0:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    default:
                        break;

                    case 1:
                        e = new DataInputStream(isRaw);
                        var20 = e.readInt();
                        itemId = e.readInt();
                        var22 = e.readInt();

                        if (var20 < Item.itemsList.length)
                        {
                            Item var23 = Item.itemsList[var20];

                            if (var23 instanceof INetworkItemEventListener)
                            {
                                ((INetworkItemEventListener)var23).onNetworkEvent(itemId, player, var22);
                            }
                        }

                        break;

                    case 2:
                        e = new DataInputStream(isRaw);
                        var20 = e.readInt();
                        IC2.keyboard.processKeyUpdate(player, var20);
                        break;

                    case 3:
                        e = new DataInputStream(isRaw);
                        var20 = e.readInt();
                        itemId = e.readInt();
                        var22 = e.readInt();
                        int var25 = e.readInt();
                        itemStack = e.readInt();
                        WorldServer[] var27 = DimensionManager.getWorlds();
                        int var30 = var27.length;

                        for (int var32 = 0; var32 < var30; ++var32)
                        {
                            WorldServer var31 = var27[var32];

                            if (var20 == var31.provider.dimensionId)
                            {
                                TileEntity var33 = var31.getBlockTileEntity(itemId, var22, var25);

                                if (var33 instanceof INetworkClientTileEntityEventListener)
                                {
                                    ((INetworkClientTileEntityEventListener)var33).onNetworkEvent(player, itemStack);
                                }

                                return;
                            }
                        }

                        return;

                    case 4:
                        GZIPInputStream var18 = new GZIPInputStream(isRaw, packet.data.length - 1);
                        DataInputStream var19 = new DataInputStream(var18);
                        itemId = var19.readInt();

                        if (itemId != 1)
                        {
                            ((EntityPlayerMP)player).playerNetServerHandler.kickPlayerFromServer("IC2 network protocol version mismatch (expected 1 (1.118.401-lf), got " + itemId + ")");
                        }

                        boolean var21 = var19.readByte() != 0;
                        ItemArmorQuantumSuit.enableQuantumSpeedOnSprintMap.put(player, Boolean.valueOf(var21));
                        var19.readInt();
                        Properties var24 = new Properties();
                        var24.load(var19);
                        var19.close();
                        Iterator var29 = IC2.runtimeIdProperties.entrySet().iterator();

                        while (var29.hasNext())
                        {
                            Entry var26 = (Entry)var29.next();
                            String key = (String)var26.getKey();
                            String value = (String)var26.getValue();

                            if (!var24.containsKey(key))
                            {
                                ((EntityPlayerMP)player).playerNetServerHandler.kickPlayerFromServer("IC2 id value missing (" + key + ")");
                                return;
                            }

                            int separatorPos = key.indexOf(46);

                            if (separatorPos != -1)
                            {
                                String section = key.substring(0, separatorPos);
                                key.substring(separatorPos + 1);

                                if ((section.equals("block") || section.equals("item")) && !value.equals(var24.get(key)))
                                {
                                    ((EntityPlayerMP)player).playerNetServerHandler.kickPlayerFromServer("IC2 id mismatch (" + key + ": expected " + value + ", got " + var24.get(key) + ")");
                                }
                            }
                        }

                        return;

                    case 10:
                        e = new DataInputStream(isRaw);
                        byte slot = e.readByte();
                        itemId = e.readInt();
                        short dataCount = e.readShort();
                        Object[] data = new Object[dataCount];

                        for (itemStack = 0; itemStack < dataCount; ++itemStack)
                        {
                            data[itemStack] = DataEncoder.decode(e);
                        }

                        if (slot >= 0 && slot <= 9)
                        {
                            ItemStack var28 = player.inventory.mainInventory[slot];

                            if (var28 != null && var28.itemID == itemId)
                            {
                                Item item = Item.itemsList[var28.itemID];

                                if (item instanceof IPlayerItemDataListener)
                                {
                                    ((IPlayerItemDataListener)item).onPlayerItemNetworkData(player, slot, data);
                                }
                            }
                        }
                }
            }
            catch (IOException var17)
            {
                var17.printStackTrace();
            }
        }
    }

    public void initiateKeyUpdate(int keyState) {}

    public void sendLoginData() {}

    public void initiateExplosionEffect(World world, double x, double y, double z)
    {
        try
        {
            ByteArrayOutputStream e = new ByteArrayOutputStream();
            DataOutputStream os = new DataOutputStream(e);
            os.writeByte(5);
            os.writeInt(world.provider.dimensionId);
            os.writeDouble(x);
            os.writeDouble(y);
            os.writeDouble(z);
            os.close();
            Packet250CustomPayload packet = new Packet250CustomPayload();
            packet.channel = "ic2";
            packet.isChunkDataPacket = false;
            packet.data = e.toByteArray();
            packet.length = e.size();
            Iterator i$ = world.playerEntities.iterator();

            while (i$.hasNext())
            {
                Object player = i$.next();
                EntityPlayerMP entityPlayer = (EntityPlayerMP)player;

                if (entityPlayer.getDistanceSq(x, y, z) < 128.0D)
                {
                    PacketDispatcher.sendPacketToPlayer(packet, (Player)entityPlayer);
                }
            }
        }
        catch (IOException var14)
        {
            throw new RuntimeException(var14);
        }
    }

    private byte[] getFieldData(Collection<NetworkManager$TileEntityField> networkedFieldsToUpdate, EntityPlayer entityPlayer)
    {
        try
        {
            ByteArrayOutputStream e = new ByteArrayOutputStream();
            e.write(0);
            GZIPOutputStream gzip = new GZIPOutputStream(e);
            DataOutputStream os = new DataOutputStream(gzip);
            os.writeInt(entityPlayer.worldObj.provider.dimensionId);
            Iterator i$ = networkedFieldsToUpdate.iterator();

            while (i$.hasNext())
            {
                NetworkManager$TileEntityField tef = (NetworkManager$TileEntityField)i$.next();

                if (!tef.te.isInvalid() && tef.te.worldObj == entityPlayer.worldObj && (tef.target == null || tef.target == entityPlayer))
                {
                    int distance = Math.min(Math.abs(tef.te.xCoord - (int)entityPlayer.posX), Math.abs(tef.te.zCoord - (int)entityPlayer.posZ));

                    if (distance <= MinecraftServer.getServer().getConfigurationManager().getEntityViewDistance() + 16)
                    {
                        os.writeInt(tef.te.xCoord);
                        os.writeInt(tef.te.yCoord);
                        os.writeInt(tef.te.zCoord);
                        os.writeShort(tef.field.length());
                        os.writeChars(tef.field);
                        Field field = null;

                        try
                        {
                            Class e1 = tef.te.getClass();

                            do
                            {
                                try
                                {
                                    field = e1.getDeclaredField(tef.field);
                                }
                                catch (NoSuchFieldException var12)
                                {
                                    e1 = e1.getSuperclass();
                                }
                            }
                            while (field == null && e1 != null);

                            if (field == null)
                            {
                                throw new NoSuchFieldException(tef.field);
                            }

                            field.setAccessible(true);
                            DataEncoder.encode(os, field.get(tef.te));
                        }
                        catch (Exception var13)
                        {
                            throw new RuntimeException(var13);
                        }
                    }
                }
            }

            os.close();
            gzip.close();
            return e.toByteArray();
        }
        catch (IOException var14)
        {
            throw new RuntimeException(var14);
        }
    }
}
