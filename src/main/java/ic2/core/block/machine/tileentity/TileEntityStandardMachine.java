package ic2.core.block.machine.tileentity;

import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.audio.AudioSource;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotUpgrade;
import ic2.core.block.machine.ContainerStandardMachine;
import ic2.core.item.IUpgradeItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileEntityStandardMachine extends TileEntityElectricMachine implements IHasGui, INetworkTileEntityEventListener
{
    public short progress = 0;
    public final int defaultEnergyConsume;
    public final int defaultOperationLength;
    public final int defaultTier;
    public final int defaultEnergyStorage;
    public int energyConsume;
    public int operationLength;
    public int operationsPerTick;
    public float serverChargeLevel;
    public float serverProgress;
    public AudioSource audioSource;
    private static final int EventStart = 0;
    private static final int EventInterrupt = 1;
    private static final int EventStop = 2;
    public InvSlot inputSlot;
    public final InvSlotOutput outputSlot;
    public final InvSlotUpgrade upgradeSlot;

    public TileEntityStandardMachine(int energyPerTick, int length)
    {
        super(energyPerTick * length, 1, 1);
        this.defaultEnergyConsume = this.energyConsume = energyPerTick;
        this.defaultOperationLength = this.operationLength = length;
        this.defaultTier = 1;
        this.defaultEnergyStorage = energyPerTick * length;
        this.outputSlot = new InvSlotOutput(this, "output", 2, 1);
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 3, 4);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getShort("progress");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("progress", this.progress);
    }

    public float getChargeLevel()
    {
        float ret;

        if (IC2.platform.isSimulating())
        {
            ret = (float)super.energy / (float)(super.maxEnergy - super.maxInput + 1);

            if (ret > 1.0F)
            {
                ret = 1.0F;
            }
        }
        else
        {
            ret = this.serverChargeLevel;
        }

        return ret;
    }

    public float getProgress()
    {
        float ret;

        if (IC2.platform.isSimulating())
        {
            ret = (float)this.progress / (float)this.operationLength;

            if (ret > 1.0F)
            {
                ret = 1.0F;
            }
        }
        else
        {
            ret = this.serverProgress;
        }

        return ret;
    }

    public void setChargeLevel(float chargeLevel)
    {
        assert !IC2.platform.isSimulating();
        this.serverChargeLevel = chargeLevel;
    }

    public void setProgress(float progress)
    {
        assert !IC2.platform.isSimulating();
        this.serverProgress = progress;
    }

    public void onLoaded()
    {
        super.onLoaded();

        if (IC2.platform.isSimulating())
        {
            this.setOverclockRates();
        }
    }

    public void onUnloaded()
    {
        super.onUnloaded();

        if (IC2.platform.isRendering() && this.audioSource != null)
        {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
        }
    }

    public void onInventoryChanged()
    {
        super.onInventoryChanged();

        if (IC2.platform.isSimulating())
        {
            this.setOverclockRates();
        }
    }

    public void updateEntity()
    {
        super.updateEntity();
        boolean canOperate = this.canOperate();
        boolean needsInvUpdate = false;
        boolean newActive = this.getActive();

        if (this.progress >= this.operationLength)
        {
            this.operate();
            needsInvUpdate = true;
            this.progress = 0;
            newActive = false;
            IC2.network.initiateTileEntityEvent(this, 2, true);
        }

        canOperate = this.canOperate();

        if (newActive && this.progress != 0)
        {
            if (!canOperate || super.energy < this.energyConsume)
            {
                if (!canOperate)
                {
                    this.progress = 0;
                }

                newActive = false;
                IC2.network.initiateTileEntityEvent(this, 1, true);
            }
        }
        else if (canOperate)
        {
            if (super.energy >= this.energyConsume)
            {
                newActive = true;
                IC2.network.initiateTileEntityEvent(this, 0, true);
            }
        }
        else
        {
            this.progress = 0;
        }

        if (newActive)
        {
            ++this.progress;
            super.energy -= this.energyConsume;
        }

        for (int i = 0; i < this.upgradeSlot.size(); ++i)
        {
            ItemStack stack = this.upgradeSlot.get(i);

            if (stack != null && stack.getItem() instanceof IUpgradeItem && ((IUpgradeItem)stack.getItem()).onTick(stack, this))
            {
                needsInvUpdate = true;
            }
        }

        if (needsInvUpdate)
        {
            super.onInventoryChanged();
        }

        if (newActive != this.getActive())
        {
            this.setActive(newActive);
        }
    }

    public void setOverclockRates()
    {
        int extraProcessTime = 0;
        double processTimeMultiplier = 1.0D;
        int extraEnergyDemand = 0;
        double energyDemandMultiplier = 1.0D;
        int extraEnergyStorage = 0;
        double energyStorageMultiplier = 1.0D;
        int extraTier = 0;

        for (int var15 = 0; var15 < this.upgradeSlot.size(); ++var15)
        {
            ItemStack stack = this.upgradeSlot.get(var15);

            if (stack != null && stack.getItem() instanceof IUpgradeItem)
            {
                IUpgradeItem var16 = (IUpgradeItem)stack.getItem();
                extraProcessTime += var16.getExtraProcessTime(stack, this) * stack.stackSize;
                processTimeMultiplier *= Math.pow(var16.getProcessTimeMultiplier(stack, this), (double)stack.stackSize);
                extraEnergyDemand += var16.getExtraEnergyDemand(stack, this) * stack.stackSize;
                energyDemandMultiplier *= Math.pow(var16.getEnergyDemandMultiplier(stack, this), (double)stack.stackSize);
                extraEnergyStorage += var16.getExtraEnergyStorage(stack, this) * stack.stackSize;
                energyStorageMultiplier *= Math.pow(var16.getEnergyStorageMultiplier(stack, this), (double)stack.stackSize);
                extraTier += var16.getExtraTier(stack, this) * stack.stackSize;
            }
        }

        double var151 = (double)this.progress / (double)this.operationLength;
        double var161 = ((double)this.defaultOperationLength + (double)extraProcessTime) * 64.0D * processTimeMultiplier;
        this.operationsPerTick = (int)Math.min(Math.ceil(64.0D / var161), 2.147483647E9D);
        this.operationLength = (int)Math.round(var161 * (double)this.operationsPerTick / 64.0D);
        this.energyConsume = this.applyModifier(this.defaultEnergyConsume, extraEnergyDemand, energyDemandMultiplier);
        this.setTier(this.applyModifier(this.defaultTier, extraTier, 1.0D));
        super.maxEnergy = this.applyModifier(this.defaultEnergyStorage, extraEnergyStorage + super.maxInput - 1, energyStorageMultiplier);

        if (this.operationLength < 1)
        {
            this.operationLength = 1;
        }

        this.progress = (short)((int)Math.floor(var151 * (double)this.operationLength + 0.1D));
    }

    public void operate()
    {
        for (int i = 0; i < this.operationsPerTick; ++i)
        {
            if (!this.canOperate())
            {
                return;
            }

            ItemStack processResult;

            if (this.inputSlot.get().getItem().hasContainerItem())
            {
                processResult = this.getResultFor(this.inputSlot.get(), false).copy();
                this.inputSlot.put(this.inputSlot.get().getItem().getContainerItemStack(this.inputSlot.get()));
            }
            else
            {
                processResult = this.getResultFor(this.inputSlot.get(), true).copy();
            }

            if (this.inputSlot.get().stackSize <= 0)
            {
                this.inputSlot.clear();
            }

            for (int j = 0; j < this.upgradeSlot.size(); ++j)
            {
                ItemStack stack = this.upgradeSlot.get(j);

                if (stack != null && stack.getItem() instanceof IUpgradeItem)
                {
                    ((IUpgradeItem)stack.getItem()).onProcessEnd(stack, this, processResult);
                }
            }

            this.outputSlot.add(processResult);
        }
    }

    public boolean canOperate()
    {
        if (this.inputSlot.isEmpty())
        {
            return false;
        }
        else
        {
            ItemStack processResult = this.getResultFor(this.inputSlot.get(), false);
            return processResult == null ? false : this.outputSlot.canAdd(processResult);
        }
    }

    public abstract ItemStack getResultFor(ItemStack var1, boolean var2);

    public abstract String getInvName();

    public ContainerBase getGuiContainer(EntityPlayer entityPlayer)
    {
        return new ContainerStandardMachine(entityPlayer, this);
    }

    public void onGuiClosed(EntityPlayer entityPlayer) {}

    public String getStartSoundFile()
    {
        return null;
    }

    public String getInterruptSoundFile()
    {
        return null;
    }

    public void onNetworkEvent(int event)
    {
        if (this.audioSource == null && this.getStartSoundFile() != null)
        {
            this.audioSource = IC2.audioManager.createSource(this, this.getStartSoundFile());
        }

        switch (event)
        {
            case 0:
                if (this.audioSource != null)
                {
                    this.audioSource.play();
                }

                break;

            case 1:
                if (this.audioSource != null)
                {
                    this.audioSource.stop();

                    if (this.getInterruptSoundFile() != null)
                    {
                        IC2.audioManager.playOnce(this, this.getInterruptSoundFile());
                    }
                }

                break;

            case 2:
                if (this.audioSource != null)
                {
                    this.audioSource.stop();
                }
        }
    }

    private int applyModifier(int base, int extra, double multiplier)
    {
        double ret = (double)Math.round(((double)base + (double)extra) * multiplier);
        return ret > 2.147483647E9D ? Integer.MAX_VALUE : (int)ret;
    }
}
