package ic2.core.network;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkItemEventListener;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.network.INetworkUpdateListener;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.item.IHandHeldInventory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class NetworkManagerClient extends NetworkManager
{
    @Deprecated
    public void requestInitialData(INetworkDataProvider dataProvider) {}

    public void initiateClientItemEvent(ItemStack itemStack, int event)
    {
        try
        {
            ByteArrayOutputStream e = new ByteArrayOutputStream();
            DataOutputStream os = new DataOutputStream(e);
            os.writeByte(1);
            os.writeInt(itemStack.itemID);
            os.writeInt(itemStack.getItemDamage());
            os.writeInt(event);
            os.close();
            Packet250CustomPayload packet = new Packet250CustomPayload();
            packet.channel = "ic2";
            packet.isChunkDataPacket = false;
            packet.data = e.toByteArray();
            packet.length = e.size();
            PacketDispatcher.sendPacketToServer(packet);
        }
        catch (IOException var6)
        {
            throw new RuntimeException(var6);
        }
    }

    public void initiateKeyUpdate(int keyState)
    {
        try
        {
            ByteArrayOutputStream e = new ByteArrayOutputStream();
            DataOutputStream os = new DataOutputStream(e);
            os.writeByte(2);
            os.writeInt(keyState);
            os.close();
            Packet250CustomPayload packet = new Packet250CustomPayload();
            packet.channel = "ic2";
            packet.isChunkDataPacket = false;
            packet.data = e.toByteArray();
            packet.length = e.size();
            PacketDispatcher.sendPacketToServer(packet);
        }
        catch (IOException var5)
        {
            throw new RuntimeException(var5);
        }
    }

    public void initiateClientTileEntityEvent(TileEntity te, int event)
    {
        try
        {
            ByteArrayOutputStream e = new ByteArrayOutputStream();
            DataOutputStream os = new DataOutputStream(e);
            os.writeByte(3);
            os.writeInt(te.worldObj.provider.dimensionId);
            os.writeInt(te.xCoord);
            os.writeInt(te.yCoord);
            os.writeInt(te.zCoord);
            os.writeInt(event);
            os.close();
            Packet250CustomPayload packet = new Packet250CustomPayload();
            packet.channel = "ic2";
            packet.isChunkDataPacket = false;
            packet.data = e.toByteArray();
            packet.length = e.size();
            PacketDispatcher.sendPacketToServer(packet);
        }
        catch (IOException var6)
        {
            throw new RuntimeException(var6);
        }
    }

    public void sendLoginData()
    {
        try
        {
            ByteArrayOutputStream e = new ByteArrayOutputStream();
            e.write(4);
            GZIPOutputStream gzip = new GZIPOutputStream(e);
            DataOutputStream os = new DataOutputStream(gzip);
            os.writeInt(1);
            os.writeByte(IC2.enableQuantumSpeedOnSprint ? 1 : 0);
            ByteArrayOutputStream buffer2 = new ByteArrayOutputStream();
            IC2.runtimeIdProperties.store(buffer2, "");
            os.writeInt(buffer2.size());
            buffer2.writeTo(os);
            os.close();
            gzip.close();
            Packet250CustomPayload packet = new Packet250CustomPayload();
            packet.channel = "ic2";
            packet.isChunkDataPacket = false;
            packet.data = e.toByteArray();
            packet.length = e.size();
            PacketDispatcher.sendPacketToServer(packet);
        }
        catch (IOException var6)
        {
            throw new RuntimeException(var6);
        }
    }

    public void onPacketData(INetworkManager network, Packet250CustomPayload packet, Player iplayer)
    {
        if (packet.data.length != 0)
        {
            if (packet.data[0] >= 10)
            {
                super.onPacketData(network, packet, iplayer);
            }

            ByteArrayInputStream isRaw = new ByteArrayInputStream(packet.data, 1, packet.data.length - 1);

            try
            {
                DataInputStream e;
                int dimensionId;
                int currentItemPosition;
                WorldClient world;
                int var28;
                int var30;
                int var34;
                int var32;
                WorldClient var36;
                TileEntity var41;

                switch (packet.data[0])
                {
                    case 0:
                        GZIPInputStream var23 = new GZIPInputStream(isRaw, packet.data.length - 1);
                        DataInputStream var29 = new DataInputStream(var23);
                        var28 = var29.readInt();
                        WorldClient var33 = Minecraft.getMinecraft().theWorld;

                        if (var33.provider.dimensionId != var28)
                        {
                            return;
                        }

                        while (true)
                        {
                            try
                            {
                                var30 = var29.readInt();
                            }
                            catch (EOFException var21)
                            {
                                var29.close();
                                return;
                            }

                            var34 = var29.readInt();
                            var32 = var29.readInt();
                            var41 = var33.getBlockTileEntity(var30, var34, var32);
                            short var40 = var29.readShort();
                            char[] var44 = new char[var40];

                            for (int var42 = 0; var42 < var40; ++var42)
                            {
                                var44[var42] = var29.readChar();
                            }

                            String var46 = new String(var44);
                            Field var45 = null;

                            try
                            {
                                if (var41 != null)
                                {
                                    Class e1 = var41.getClass();

                                    do
                                    {
                                        try
                                        {
                                            var45 = e1.getDeclaredField(var46);
                                        }
                                        catch (NoSuchFieldException var19)
                                        {
                                            e1 = e1.getSuperclass();
                                        }
                                    }
                                    while (var45 == null && e1 != null);

                                    if (var45 == null)
                                    {
                                        IC2.log.warning("Can\'t find field " + var46 + " in te " + var41 + " at " + var30 + "/" + var34 + "/" + var32);
                                    }
                                    else
                                    {
                                        var45.setAccessible(true);
                                    }
                                }

                                Object var47 = DataEncoder.decode(var29);

                                if (var45 != null && var41 != null)
                                {
                                    var45.set(var41, var47);
                                }
                            }
                            catch (Exception var20)
                            {
                                throw new RuntimeException(var20);
                            }

                            if (var41 instanceof INetworkUpdateListener)
                            {
                                ((INetworkUpdateListener)var41).onNetworkUpdate(var46);
                            }
                        }

                    case 1:
                        e = new DataInputStream(isRaw);
                        dimensionId = e.readInt();
                        var28 = e.readInt();
                        currentItemPosition = e.readInt();
                        var30 = e.readInt();
                        var34 = e.readInt();
                        WorldClient var37 = Minecraft.getMinecraft().theWorld;

                        if (var37.provider.dimensionId != dimensionId)
                        {
                            return;
                        }

                        var41 = var37.getBlockTileEntity(var28, currentItemPosition, var30);

                        if (var41 instanceof INetworkTileEntityEventListener)
                        {
                            ((INetworkTileEntityEventListener)var41).onNetworkEvent(var34);
                        }

                        break;

                    case 2:
                        e = new DataInputStream(isRaw);
                        byte var26 = e.readByte();
                        char[] var27 = new char[var26];

                        for (currentItemPosition = 0; currentItemPosition < var26; ++currentItemPosition)
                        {
                            var27[currentItemPosition] = e.readChar();
                        }

                        String var31 = new String(var27);
                        var30 = e.readInt();
                        var34 = e.readInt();
                        var32 = e.readInt();
                        var36 = Minecraft.getMinecraft().theWorld;
                        Iterator var39 = var36.playerEntities.iterator();
                        EntityPlayer entityPlayer;

                        do
                        {
                            if (!var39.hasNext())
                            {
                                return;
                            }

                            Object var43 = var39.next();
                            entityPlayer = (EntityPlayer)var43;
                        }
                        while (!entityPlayer.username.equals(var31));

                        Item item = Item.itemsList[var30];

                        if (item instanceof INetworkItemEventListener)
                        {
                            ((INetworkItemEventListener)item).onNetworkEvent(var34, entityPlayer, var32);
                        }

                        break;

                    case 3:
                        e = new DataInputStream(isRaw);
                        dimensionId = e.readInt();
                        var28 = e.readInt();
                        currentItemPosition = e.readInt();
                        var30 = e.readInt();
                        short var35 = e.readShort();
                        byte var38 = e.readByte();
                        var36 = Minecraft.getMinecraft().theWorld;

                        if (var36.provider.dimensionId != dimensionId)
                        {
                            return;
                        }

                        var36.setBlock(var28, currentItemPosition, var30, var35, var38, 3);
                        break;

                    case 4:
                        e = new DataInputStream(isRaw);
                        EntityPlayer var24 = IC2.platform.getPlayerInstance();
                        boolean var25 = e.readByte() != 0;

                        switch (e.readByte())
                        {
                            case 0:
                                currentItemPosition = e.readInt();
                                var30 = e.readInt();
                                var34 = e.readInt();
                                var32 = e.readInt();
                                int windowId = e.readInt();
                                world = Minecraft.getMinecraft().theWorld;

                                if (world.provider.dimensionId != currentItemPosition)
                                {
                                    return;
                                }

                                TileEntity te = world.getBlockTileEntity(var30, var34, var32);

                                if (te instanceof IHasGui)
                                {
                                    IC2.platform.launchGuiClient(var24, (IHasGui)te, var25);
                                }

                                var24.openContainer.windowId = windowId;
                                return;

                            case 1:
                                currentItemPosition = e.readInt();
                                var30 = e.readInt();

                                if (currentItemPosition != var24.inventory.currentItem)
                                {
                                    return;
                                }

                                ItemStack currentItem = var24.inventory.getCurrentItem();

                                if (currentItem != null && currentItem.getItem() instanceof IHandHeldInventory)
                                {
                                    IC2.platform.launchGuiClient(var24, ((IHandHeldInventory)currentItem.getItem()).getInventory(var24, currentItem), var25);
                                }

                                var24.openContainer.windowId = var30;
                                return;

                            default:
                                return;
                        }

                    case 5:
                        e = new DataInputStream(isRaw);
                        dimensionId = e.readInt();
                        double x = e.readDouble();
                        double y = e.readDouble();
                        double z = e.readDouble();
                        world = Minecraft.getMinecraft().theWorld;

                        if (world.provider.dimensionId != dimensionId)
                        {
                            return;
                        }

                        world.playSoundEffect(x, y, z, "random.explode", 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
                        world.spawnParticle("hugeexplosion", x, y, z, 0.0D, 0.0D, 0.0D);
                }
            }
            catch (IOException var22)
            {
                var22.printStackTrace();
            }
        }
    }

    public void announceBlockUpdate(World world, int x, int y, int z)
    {
        if (IC2.platform.isSimulating())
        {
            super.announceBlockUpdate(world, x, y, z);
        }
    }
}
