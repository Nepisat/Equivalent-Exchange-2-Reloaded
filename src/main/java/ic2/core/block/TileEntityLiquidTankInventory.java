package ic2.core.block;

import java.util.Random;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.audio.AudioSource;
import ic2.core.audio.PositionSpec;
import ic2.core.block.invslot.InvSlotCharge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public abstract class TileEntityLiquidTankInventory extends TileEntityInventory implements IFluidHandler
{
    protected final FluidTank fluidTank;
    public static Random random = new Random();
    public int fuel = 0;
    public short storage = 0;
    public final short maxStorage;
    public int production;
    public int ticksSinceLastActiveUpdate;
    public int activityMeter = 0;
    public boolean addedToEnergyNet = false;
    public AudioSource audioSource;
    public InvSlotCharge chargeSlot;
    public TileEntityLiquidTankInventory(int tanksize,int maxStorage)
    {
        this.production = production;
        this.maxStorage = (short)maxStorage;
        this.ticksSinceLastActiveUpdate = random.nextInt(256);
        this.chargeSlot = new InvSlotCharge(this, 0, 1);
        this.fluidTank = new FluidTank(1000 * tanksize);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);

        try
        {
            this.fuel = nbttagcompound.getInteger("fuel");
        }
        catch (Throwable var3)
        {
            this.fuel = nbttagcompound.getShort("fuel");
        }

        this.storage = nbttagcompound.getShort("storage");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("fuel", this.fuel);
        nbttagcompound.setShort("storage", this.storage);
    }
    public int gaugeStorageScaled(int i)
    {
        return this.storage * i / this.maxStorage;
    }

    public abstract int gaugeFuelScaled(int var1);

    public void onLoaded()
    {
        super.onLoaded();

        if (IC2.platform.isSimulating())
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent((IEnergyTile) this));
            this.addedToEnergyNet = true;
        }
    }

    public void onUnloaded()
    {
        if (IC2.platform.isSimulating() && this.addedToEnergyNet)
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent((IEnergyTile) this));
            this.addedToEnergyNet = false;
        }

        if (IC2.platform.isRendering() && this.audioSource != null)
        {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
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
        boolean needsInvUpdate = false;

        if (this.needsFuel())
        {
            needsInvUpdate = this.gainFuel();
        }

        boolean newActive = this.gainEnergy();

        if (this.storage > this.maxStorage)
        {
            this.storage = this.maxStorage;
        }

        if (this.storage > 0)
        {
            int output;

            if (this.chargeSlot.getItem() != null)
            {
                output = ElectricItem.manager.charge(this.chargeSlot.get(), this.storage, 1, false, false);
                this.storage = (short)(this.storage - output);

                if (output > 0)
                {
                    needsInvUpdate = true;
                }
            }

            output = Math.min(this.production, this.storage);

            if (output > 0)
            {
                this.storage = (short)(this.storage + (this.sendEnergy(output) - output));
            }
        }

        if (needsInvUpdate)
        {
            this.onInventoryChanged();
        }

        if (!this.delayActiveUpdate())
        {
            this.setActive(newActive);
        }
        else
        {
            if (this.ticksSinceLastActiveUpdate % 256 == 0)
            {
                this.setActive(this.activityMeter > 0);
                this.activityMeter = 0;
            }

            if (newActive)
            {
                ++this.activityMeter;
            }
            else
            {
                --this.activityMeter;
            }

            ++this.ticksSinceLastActiveUpdate;
        }
    }

    public boolean gainEnergy()
    {
        if (this.isConverting())
        {
            this.storage = (short)(this.storage + this.production);
            --this.fuel;
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isConverting()
    {
        return this.fuel > 0 && this.storage + this.production <= this.maxStorage;
    }

    public boolean needsFuel()
    {
        return this.fuel <= 0 && this.storage + this.production <= this.maxStorage;
    }

    public abstract boolean gainFuel();

    public int sendEnergy(int send)
    {
        EnergyTileSourceEvent event = new EnergyTileSourceEvent((IEnergySource) this, send);
        MinecraftForge.EVENT_BUS.post(event);
        return event.amount;
    }

    public boolean isAddedToEnergyNet()
    {
        return this.addedToEnergyNet;
    }

    public boolean emitsEnergyTo(TileEntity receiver, Direction direction)
    {
        return true;
    }

    public int getMaxEnergyOutput()
    {
        return this.production;
    }

    public abstract String getInvName();

    public void onGuiClosed(EntityPlayer entityPlayer) {}

    public String getOperationSoundFile()
    {
        return null;
    }

    public boolean delayActiveUpdate()
    {
        return false;
    }

    public void onNetworkUpdate(String field)
    {
        if (field.equals("active") && super.prevActive != this.getActive())
        {
            if (this.audioSource == null && this.getOperationSoundFile() != null)
            {
                this.audioSource = IC2.audioManager.createSource(this, PositionSpec.Center, this.getOperationSoundFile(), true, false, IC2.audioManager.defaultVolume);
            }

            if (this.getActive())
            {
                if (this.audioSource != null)
                {
                    this.audioSource.play();
                }
            }
            else if (this.audioSource != null)
            {
                this.audioSource.stop();
            }
        }

        super.onNetworkUpdate(field);
    }

    public float getWrenchDropRate()
    {
        return 0.9F;
    }
    public FluidTank getFluidTank()
    {
        return this.fluidTank;
    }

    public int getFluidTankCapacity()
    {
        return this.getFluidTank().getCapacity();
    }

    public FluidStack getFluidStackfromTank()
    {
        return this.getFluidTank().getFluid();
    }

    public Fluid getFluidfromTank()
    {
        return this.getFluidStackfromTank().getFluid();
    }

    public int getTankAmount()
    {
        return this.getFluidTank().getFluidAmount();
    }

    public int getTankFluidId()
    {
        return this.getFluidStackfromTank().fluidID;
    }

    public int gaugeLiquidScaled(int i)
    {
        return this.getFluidTank().getFluidAmount() <= 0 ? 0 : this.getFluidTank().getFluidAmount() * i / this.getFluidTank().getCapacity();
    }

    public void setTankAmount(int amount, int fluidid)
    {
        this.getFluidTank().setFluid(new FluidStack(FluidRegistry.getFluid(fluidid), amount));
    }

    public boolean needsFluid()
    {
        return this.getFluidTank().getFluidAmount() <= this.getFluidTank().getCapacity();
    }

    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        return this.canFill(from, resource.getFluid()) ? this.getFluidTank().fill(resource, doFill) : 0;
    }

    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        return resource != null && resource.isFluidEqual(this.getFluidTank().getFluid()) ? (!this.canDrain(from, resource.getFluid()) ? null : this.getFluidTank().drain(resource.amount, doDrain)) : null;
    }

    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return this.getFluidTank().drain(maxDrain, doDrain);
    }

    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[] {this.getFluidTank().getInfo()};
    }

    public abstract boolean canFill(ForgeDirection var1, Fluid var2);

    public abstract boolean canDrain(ForgeDirection var1, Fluid var2);
}
