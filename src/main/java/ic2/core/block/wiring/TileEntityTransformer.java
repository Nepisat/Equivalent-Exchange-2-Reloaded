package ic2.core.block.wiring;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.core.IC2;
import ic2.core.block.TileEntityBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;

public abstract class TileEntityTransformer extends TileEntityBlock implements IEnergySink, IEnergySource
{
    public int lowOutput;
    public int highOutput;
    public int maxStorage;
    public int energy = 0;
    public boolean redstone = false;
    public boolean addedToEnergyNet = false;

    public TileEntityTransformer(int low, int high, int max)
    {
        this.lowOutput = low;
        this.highOutput = high;
        this.maxStorage = max;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        this.energy = nbttagcompound.getInteger("energy");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("energy", this.energy);
    }

    public boolean enableUpdateEntity()
    {
        return IC2.platform.isSimulating();
    }

    public void onLoaded()
    {
        super.onLoaded();

        if (IC2.platform.isSimulating())
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }
    }

    public void onUnloaded()
    {
        if (IC2.platform.isSimulating() && this.addedToEnergyNet)
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }

        super.onUnloaded();
    }

    public void updateEntity()
    {
        super.updateEntity();
        this.updateRedstone();

        if (this.redstone)
        {
            if (this.energy >= this.highOutput)
            {
                EnergyTileSourceEvent var3 = new EnergyTileSourceEvent(this, this.highOutput);
                MinecraftForge.EVENT_BUS.post(var3);
                this.energy -= this.highOutput - var3.amount;
            }
        }
        else
        {
            for (int var31 = 0; var31 < 4 && this.energy >= this.lowOutput; ++var31)
            {
                EnergyTileSourceEvent event = new EnergyTileSourceEvent(this, this.lowOutput);
                MinecraftForge.EVENT_BUS.post(event);
                this.energy -= this.lowOutput - event.amount;
            }
        }
    }

    public void updateRedstone()
    {
        boolean red = super.worldObj.isBlockIndirectlyGettingPowered(super.xCoord, super.yCoord, super.zCoord);

        if (red != this.redstone)
        {
            if (this.addedToEnergyNet)
            {
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            }

            this.addedToEnergyNet = false;
            this.redstone = red;
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
            this.setActive(this.redstone);
        }
    }

    public boolean isAddedToEnergyNet()
    {
        return this.addedToEnergyNet;
    }

    public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
    {
        return this.redstone ? !this.facingMatchesDirection(direction) : this.facingMatchesDirection(direction);
    }

    public boolean emitsEnergyTo(TileEntity receiver, Direction direction)
    {
        return this.redstone ? this.facingMatchesDirection(direction) : !this.facingMatchesDirection(direction);
    }

    public boolean facingMatchesDirection(Direction direction)
    {
        return direction.toSideValue() == this.getFacing();
    }

    public int getMaxEnergyOutput()
    {
        return this.redstone ? this.highOutput : this.lowOutput;
    }

    public int demandsEnergy()
    {
        return this.maxStorage - this.energy;
    }

    public int injectEnergy(Direction directionFrom, int amount)
    {
        if (amount > this.getMaxSafeInput())
        {
            IC2.explodeMachineAt(super.worldObj, super.xCoord, super.yCoord, super.zCoord);
            return 0;
        }
        else if (this.energy >= this.maxStorage)
        {
            return amount;
        }
        else
        {
            this.energy += amount;
            return 0;
        }
    }

    public int getMaxSafeInput()
    {
        return this.redstone ? this.lowOutput : (this.highOutput != 2048 ? this.highOutput : Integer.MAX_VALUE);
    }

    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
    {
        return this.getFacing() != side;
    }

    public void setFacing(short side)
    {
        if (this.addedToEnergyNet)
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
        }

        super.setFacing(side);

        if (this.addedToEnergyNet)
        {
            this.addedToEnergyNet = false;
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            this.addedToEnergyNet = true;
        }
    }
}
