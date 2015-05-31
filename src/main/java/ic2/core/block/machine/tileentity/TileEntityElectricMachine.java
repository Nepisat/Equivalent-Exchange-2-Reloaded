package ic2.core.block.machine.tileentity;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.core.IC2;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlotDischarge;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;

public abstract class TileEntityElectricMachine extends TileEntityInventory implements IEnergySink
{
    public int energy = 0;
    public int maxEnergy;
    public int maxInput;
    private boolean addedToEnergyNet = false;
    public final InvSlotDischarge dischargeSlot;

    public TileEntityElectricMachine(int maxenergy, int tier, int oldDischargeIndex)
    {
        this.maxEnergy = maxenergy;

        switch (tier)
        {
            case 1:
                this.maxInput = 32;
                break;

            case 2:
                this.maxInput = 128;
                break;

            case 3:
                this.maxInput = 512;
                break;

            default:
                this.maxInput = 2048;
        }

        this.dischargeSlot = new InvSlotDischarge(this, oldDischargeIndex, tier);
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

    public boolean enableUpdateEntity()
    {
        return IC2.platform.isSimulating();
    }

    public void updateEntity()
    {
        super.updateEntity();

        if (this.energy < this.maxEnergy)
        {
            int amount = this.dischargeSlot.discharge(this.maxEnergy - this.energy, true);

            if (amount > 0)
            {
                this.energy += amount;
                this.onInventoryChanged();
            }
        }
    }

    public boolean isAddedToEnergyNet()
    {
        return this.addedToEnergyNet;
    }

    public int demandsEnergy()
    {
        return this.maxEnergy - this.energy;
    }

    public int injectEnergy(Direction directionFrom, int amount)
    {
        if (amount > this.maxInput)
        {
            IC2.explodeMachineAt(super.worldObj, super.xCoord, super.yCoord, super.zCoord);
            return 0;
        }
        else if (this.energy >= this.maxEnergy)
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
        return this.maxInput;
    }

    public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
    {
        return true;
    }

    public boolean isRedstonePowered()
    {
        return super.worldObj.isBlockIndirectlyGettingPowered(super.xCoord, super.yCoord, super.zCoord);
    }

    public void setTier(int tier)
    {
        this.dischargeSlot.setTier(tier);

        switch (tier)
        {
            case 1:
                this.maxInput = 32;
                break;

            case 2:
                this.maxInput = 128;
                break;

            case 3:
                this.maxInput = 512;
                break;

            default:
                this.maxInput = 2048;
        }
    }
}
