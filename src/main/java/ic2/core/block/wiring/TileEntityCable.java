package ic2.core.block.wiring;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.EnergyNet;
import ic2.core.IC2;
import ic2.core.ITickCallback;
import ic2.core.Ic2Items;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.wiring.TileEntityCable$1;
import java.util.List;
import java.util.Vector;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityCable extends TileEntityBlock implements IEnergyConductor, INetworkTileEntityEventListener
{
    public short cableType = 0;
    public short color = 0;
    public byte foamed = 0;
    public byte foamColor = 0;
    public int[] retextureRefId;
    public int[] retextureRefMeta;
    public int[] retextureRefSide;
    public byte connectivity = 0;
    public byte renderSide = 0;
    private byte prevFoamed = 0;
    public boolean addedToEnergyNet = false;
    private ITickCallback continuousTickCallback = null;
    private static final int EventRemoveConductor = 0;

    public TileEntityCable(short type)
    {
        this.cableType = type;
    }

    public TileEntityCable() {}

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        this.cableType = nbttagcompound.getShort("cableType");
        this.color = nbttagcompound.getShort("color");
        this.foamColor = nbttagcompound.getByte("foamColor");
        this.foamed = nbttagcompound.getByte("foamed");
        this.retextureRefId = nbttagcompound.getIntArray("retextureRefId");
        this.retextureRefMeta = nbttagcompound.getIntArray("retextureRefMeta");
        this.retextureRefSide = nbttagcompound.getIntArray("retextureRefSide");

        if (this.retextureRefId.length != 6)
        {
            this.retextureRefId = null;
        }

        if (this.retextureRefMeta.length != 6)
        {
            this.retextureRefMeta = null;
        }

        if (this.retextureRefSide.length != 6)
        {
            this.retextureRefSide = null;
        }
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("cableType", this.cableType);
        nbttagcompound.setShort("color", this.color);
        nbttagcompound.setByte("foamed", this.foamed);
        nbttagcompound.setByte("foamColor", this.foamColor);

        if (this.retextureRefId != null)
        {
            nbttagcompound.setIntArray("retextureRefId", this.retextureRefId);
            nbttagcompound.setIntArray("retextureRefMeta", this.retextureRefMeta);
            nbttagcompound.setIntArray("retextureRefSide", this.retextureRefSide);
        }
    }

    public void onLoaded()
    {
        super.onLoaded();

        if (IC2.platform.isSimulating())
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
            this.onNeighborBlockChange();

            if (this.foamed == 1)
            {
                this.changeFoam(this.foamed, true);
            }
        }
    }

    public void onUnloaded()
    {
        if (IC2.platform.isSimulating() && this.addedToEnergyNet)
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }

        if (this.continuousTickCallback != null)
        {
            IC2.removeContinuousTickCallback(super.worldObj, this.continuousTickCallback);
            this.continuousTickCallback = null;
        }

        super.onUnloaded();
    }

    public void onNeighborBlockChange()
    {
        byte newConnectivity = 0;
        byte newRenderSide = 0;
        int mask = 1;
        Direction[] arr$ = Direction.values();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            Direction direction = arr$[i$];
            TileEntity neighbor = EnergyNet.getForWorld(super.worldObj).getNeighbor((TileEntity)this, direction);

            if ((neighbor instanceof IEnergyAcceptor && ((IEnergyAcceptor)neighbor).acceptsEnergyFrom(this, direction.getInverse()) || neighbor instanceof IEnergyEmitter && ((IEnergyEmitter)neighbor).emitsEnergyTo(this, direction.getInverse())) && this.canInteractWith(neighbor))
            {
                newConnectivity = (byte)(newConnectivity | mask);

                if (neighbor instanceof TileEntityCable && ((TileEntityCable)neighbor).getCableThickness() < this.getCableThickness())
                {
                    newRenderSide = (byte)(newRenderSide | mask);
                }
            }

            mask *= 2;
        }

        if (this.connectivity != newConnectivity)
        {
            this.connectivity = newConnectivity;
            IC2.network.updateTileEntityField(this, "connectivity");
        }

        if (this.renderSide != newRenderSide)
        {
            this.renderSide = newRenderSide;
            IC2.network.updateTileEntityField(this, "renderSide");
        }
    }

    public boolean shouldRefresh(int oldID, int newID, int oldMeta, int newMeta, World world, int x, int y, int z)
    {
        return oldID != newID;
    }

    public boolean changeColor(int newColor)
    {
        if ((this.foamed != 0 || this.color != newColor && this.cableType != 1 && this.cableType != 2 && this.cableType != 5 && this.cableType != 10 && this.cableType != 11 && this.cableType != 12) && (this.foamed <= 0 || this.foamColor != newColor))
        {
            if (IC2.platform.isSimulating())
            {
                if (this.foamed == 0)
                {
                    if (this.addedToEnergyNet)
                    {
                        MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
                    }

                    this.addedToEnergyNet = false;
                    this.color = (short)newColor;
                    MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
                    this.addedToEnergyNet = true;
                    IC2.network.updateTileEntityField(this, "color");
                    this.onNeighborBlockChange();
                }
                else
                {
                    this.foamColor = (byte)newColor;
                    IC2.network.updateTileEntityField(this, "foamColor");
                    this.retextureRefId = null;
                    this.retextureRefMeta = null;
                    this.retextureRefSide = null;
                    IC2.network.updateTileEntityField(this, "retextureRefId");
                    IC2.network.updateTileEntityField(this, "retextureRefMeta");
                    IC2.network.updateTileEntityField(this, "retextureRefSide");
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean changeFoam(byte foamed)
    {
        return this.changeFoam(foamed, false);
    }

    public boolean tryAddInsulation()
    {
        short target;

        switch (this.cableType)
        {
            case 1:
                target = 0;
                break;

            case 2:
                target = 3;
                break;

            case 3:
                target = 4;
                break;

            case 4:
            default:
                target = this.cableType;
                break;

            case 5:
                target = 6;
                break;

            case 6:
                target = 7;
                break;

            case 7:
                target = 8;
        }

        if (target != this.cableType)
        {
            if (IC2.platform.isSimulating())
            {
                this.changeType(target);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean tryRemoveInsulation()
    {
        short target;

        switch (this.cableType)
        {
            case 0:
                target = 1;
                break;

            case 1:
            case 2:
            case 5:
            default:
                target = this.cableType;
                break;

            case 3:
                target = 2;
                break;

            case 4:
                target = 3;
                break;

            case 6:
                target = 5;
                break;

            case 7:
                target = 6;
                break;

            case 8:
                target = 7;
        }

        if (target != this.cableType)
        {
            if (IC2.platform.isSimulating())
            {
                this.changeType(target);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public void changeType(short cableType)
    {
        super.worldObj.setBlockMetadataWithNotify(super.xCoord, super.yCoord, super.zCoord, cableType, 7);

        if (this.addedToEnergyNet)
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }

        this.addedToEnergyNet = false;
        this.cableType = cableType;
        MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
        this.addedToEnergyNet = true;
        IC2.network.updateTileEntityField(this, "cableType");
    }

    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
    {
        return false;
    }

    public boolean wrenchCanRemove(EntityPlayer entityPlayer)
    {
        return false;
    }

    public boolean isAddedToEnergyNet()
    {
        return this.addedToEnergyNet;
    }

    public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
    {
        return this.canInteractWith(emitter);
    }

    public boolean emitsEnergyTo(TileEntity receiver, Direction direction)
    {
        return this.canInteractWith(receiver);
    }

    public boolean canInteractWith(TileEntity te)
    {
        return !(te instanceof IEnergyTile) ? false : (te instanceof TileEntityCable ? this.canInteractWithCable((TileEntityCable)te) : (te instanceof TileEntityLuminator ? ((TileEntityLuminator)te).canCableConnectFrom(super.xCoord, super.yCoord, super.zCoord) : true));
    }

    public boolean canInteractWithCable(TileEntityCable cable)
    {
        return this.color == 0 || cable.color == 0 || this.color == cable.color;
    }

    public float getCableThickness()
    {
        return this.foamed == 2 ? 1.0F : getCableThickness(this.cableType);
    }

    public static float getCableThickness(int cableType)
    {
        float p = 1.0F;

        switch (cableType)
        {
            case 0:
                p = 6.0F;
                break;

            case 1:
                p = 4.0F;
                break;

            case 2:
                p = 3.0F;
                break;

            case 3:
                p = 5.0F;
                break;

            case 4:
                p = 6.0F;
                break;

            case 5:
                p = 6.0F;
                break;

            case 6:
                p = 8.0F;
                break;

            case 7:
                p = 10.0F;
                break;

            case 8:
                p = 12.0F;
                break;

            case 9:
                p = 4.0F;
                break;

            case 10:
                p = 5.0F;
                break;

            case 11:
                p = 8.0F;
                break;

            case 12:
                p = 8.0F;
                break;

            case 13:
                p = 16.0F;
        }

        return p / 16.0F;
    }

    public double getConductionLoss()
    {
        switch (this.cableType)
        {
            case 0:
                return 0.2D;

            case 1:
                return 0.3D;

            case 2:
                return 0.5D;

            case 3:
                return 0.45D;

            case 4:
                return 0.4D;

            case 5:
                return 1.0D;

            case 6:
                return 0.95D;

            case 7:
                return 0.9D;

            case 8:
                return 0.8D;

            case 9:
                return 0.025D;

            case 10:
                return 0.025D;

            case 11:
                return 0.5D;

            case 12:
                return 0.5D;

            default:
                return 0.025D;
        }
    }

    public int getInsulationEnergyAbsorption()
    {
        switch (this.cableType)
        {
            case 0:
                return 32;

            case 1:
                return 8;

            case 2:
                return 8;

            case 3:
                return 32;

            case 4:
                return 128;

            case 5:
                return 0;

            case 6:
                return 128;

            case 7:
                return 512;

            case 8:
                return 1000000;

            case 9:
                return 1000000;

            case 10:
                return 3;

            case 11:
                return 1000000;

            case 12:
                return 9001;

            default:
                return 0;
        }
    }

    public int getInsulationBreakdownEnergy()
    {
        return 9001;
    }

    public int getConductorBreakdownEnergy()
    {
        switch (this.cableType)
        {
            case 0:
                return 33;

            case 1:
                return 33;

            case 2:
                return 129;

            case 3:
                return 129;

            case 4:
                return 129;

            case 5:
                return 1000000;

            case 6:
                return 1000000;

            case 7:
                return 1000000;

            case 8:
                return 1000000;

            case 9:
                return 513;

            case 10:
                return 6;

            case 11:
                return 2049;

            case 12:
                return 2049;

            default:
                return 0;
        }
    }

    public void removeInsulation() {}

    public void removeConductor()
    {
        super.worldObj.setBlock(super.xCoord, super.yCoord, super.zCoord, 0, 0, 7);
        IC2.network.initiateTileEntityEvent(this, 0, true);
    }

    public List<String> getNetworkedFields()
    {
        Vector ret = new Vector();
        ret.add("cableType");
        ret.add("color");
        ret.add("foamed");
        ret.add("foamColor");
        ret.add("retextureRefId");
        ret.add("retextureRefMeta");
        ret.add("retextureRefSide");
        ret.add("connectivity");
        ret.add("renderSide");
        ret.addAll(super.getNetworkedFields());
        return ret;
    }

    public void onNetworkUpdate(String field)
    {
        if (field.equals("foamed"))
        {
            if (this.prevFoamed != this.foamed)
            {
                if ((this.foamed != 0 || this.prevFoamed == 1) && this.foamed != 2)
                {
                    super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
                }
                else
                {
                    super.worldObj.setBlock(super.xCoord, super.yCoord, super.zCoord, Ic2Items.insulatedCopperCableBlock.itemID, (this.cableType + 1) % 16, 3);
                    super.worldObj.setBlock(super.xCoord, super.yCoord, super.zCoord, Ic2Items.insulatedCopperCableBlock.itemID, this.cableType, 3);
                }

                this.prevFoamed = this.foamed;
            }
        }
        else
        {
            super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
        }

        super.onNetworkUpdate(field);
    }

    public void onNetworkEvent(int event)
    {
        switch (event)
        {
            case 0:
                super.worldObj.playSoundEffect((double)((float)super.xCoord + 0.5F), (double)((float)super.yCoord + 0.5F), (double)((float)super.zCoord + 0.5F), "random.fizz", 0.5F, 2.6F + (super.worldObj.rand.nextFloat() - super.worldObj.rand.nextFloat()) * 0.8F);

                for (int l = 0; l < 8; ++l)
                {
                    super.worldObj.spawnParticle("largesmoke", (double)super.xCoord + Math.random(), (double)super.yCoord + 1.2D, (double)super.zCoord + Math.random(), 0.0D, 0.0D, 0.0D);
                }

                return;

            default:
                IC2.platform.displayError("An unknown event type was received over multiplayer.\nThis could happen due to corrupted data or a bug.\n\n(Technical information: event ID " + event + ", tile entity below)\n" + "T: " + this + " (" + super.xCoord + "," + super.yCoord + "," + super.zCoord + ")");
        }
    }

    public float getWrenchDropRate()
    {
        return 0.0F;
    }

    private boolean changeFoam(byte foamed, boolean duringLoad)
    {
        if (this.foamed == foamed && !duringLoad)
        {
            return false;
        }
        else if (!IC2.platform.isSimulating())
        {
            return true;
        }
        else
        {
            byte prevFoamed = this.foamed;
            this.foamed = foamed;

            if (this.continuousTickCallback != null)
            {
                IC2.removeContinuousTickCallback(super.worldObj, this.continuousTickCallback);
                this.continuousTickCallback = null;
            }

            if (foamed == 0 || foamed == 1)
            {
                if (this.retextureRefId != null)
                {
                    this.retextureRefId = null;
                    this.retextureRefMeta = null;
                    this.retextureRefSide = null;

                    if (!duringLoad)
                    {
                        IC2.network.updateTileEntityField(this, "retextureRefId");
                        IC2.network.updateTileEntityField(this, "retextureRefMeta");
                        IC2.network.updateTileEntityField(this, "retextureRefSide");
                    }
                }

                if (this.foamColor != 7)
                {
                    this.foamColor = 7;

                    if (!duringLoad)
                    {
                        IC2.network.updateTileEntityField(this, "foamColor");
                    }
                }
            }

            if ((foamed != 0 || prevFoamed == 1) && foamed != 2)
            {
                if (foamed == 1)
                {
                    this.continuousTickCallback = new TileEntityCable$1(this);
                    IC2.addContinuousTickCallback(super.worldObj, this.continuousTickCallback);
                }
            }
            else
            {
                BlockCable blockCable = (BlockCable)Block.blocksList[Ic2Items.insulatedCopperCableBlock.itemID];
                blockCable.enableBreakBlock = false;
                super.worldObj.setBlock(super.xCoord, super.yCoord, super.zCoord, Ic2Items.insulatedCopperCableBlock.itemID, (this.cableType + 1) % 16, 7);
                super.worldObj.setBlock(super.xCoord, super.yCoord, super.zCoord, Ic2Items.insulatedCopperCableBlock.itemID, this.cableType, 7);
                this.onLoaded();
                blockCable.enableBreakBlock = true;
            }

            if (!duringLoad)
            {
                IC2.network.updateTileEntityField(this, "foamed");
            }

            return true;
        }
    }

    public boolean retexture(int side, int referencedBlockId, int referencedMeta, int referencedSide)
    {
        if (this.foamed != 2)
        {
            return false;
        }
        else
        {
            boolean ret = false;
            boolean updateAll = false;

            if (this.retextureRefId == null)
            {
                this.retextureRefId = new int[6];
                this.retextureRefMeta = new int[6];
                this.retextureRefSide = new int[6];
                updateAll = true;
            }

            if (this.retextureRefId[side] != referencedBlockId || updateAll)
            {
                this.retextureRefId[side] = referencedBlockId;
                IC2.network.updateTileEntityField(this, "retextureRefId");
                ret = true;
            }

            if (this.retextureRefMeta[side] != referencedMeta || updateAll)
            {
                this.retextureRefMeta[side] = referencedMeta;
                IC2.network.updateTileEntityField(this, "retextureRefMeta");
                ret = true;
            }

            if (this.retextureRefSide[side] != referencedSide || updateAll)
            {
                this.retextureRefSide[side] = referencedSide;
                IC2.network.updateTileEntityField(this, "retextureRefSide");
                ret = true;
            }

            return ret;
        }
    }
}
