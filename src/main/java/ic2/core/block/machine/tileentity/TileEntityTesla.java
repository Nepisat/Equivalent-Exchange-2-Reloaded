package ic2.core.block.machine.tileentity;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.core.IC2;
import ic2.core.IC2DamageSource;
import ic2.core.block.TileEntityBlock;
import ic2.core.item.armor.ItemArmorHazmat;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityTesla extends TileEntityBlock implements IEnergySink
{
    public int energy = 0;
    public int ticker = 0;
    public int maxEnergy = 10000;
    public int maxInput = 128;
    public boolean addedToEnergyNet = false;

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        this.energy = nbttagcompound.getShort("energy");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("energy", (short)this.energy);
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

        if (IC2.platform.isSimulating() && this.redstoned() && this.energy >= getCost())
        {
            int damage = this.energy / getCost();
            --this.energy;

            if (this.ticker++ % 32 == 0 && this.shock(damage))
            {
                this.energy = 0;
            }
        }
    }

    public boolean shock(int damage)
    {
        boolean shock = false;
        List list1 = super.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox((double)(super.xCoord - 4), (double)(super.yCoord - 4), (double)(super.zCoord - 4), (double)(super.xCoord + 5), (double)(super.yCoord + 5), (double)(super.zCoord + 5)));

        for (int l = 0; l < list1.size(); ++l)
        {
            EntityLivingBase victim = (EntityLivingBase)list1.get(l);

            if (!ItemArmorHazmat.hasCompleteHazmat(victim))
            {
                shock = true;
                victim.attackEntityFrom(IC2DamageSource.electricity, (float)damage);

                for (int i = 0; i < damage; ++i)
                {
                    super.worldObj.spawnParticle("reddust", victim.posX + (double)super.worldObj.rand.nextFloat(), victim.posY + (double)(super.worldObj.rand.nextFloat() * 2.0F), victim.posZ + (double)super.worldObj.rand.nextFloat(), 0.0D, 0.0D, 1.0D);
                }
            }
        }

        return shock;
    }

    public boolean redstoned()
    {
        return super.worldObj.isBlockIndirectlyGettingPowered(super.xCoord, super.yCoord, super.zCoord) || super.worldObj.isBlockIndirectlyGettingPowered(super.xCoord, super.yCoord, super.zCoord);
    }

    public static int getCost()
    {
        return 400;
    }

    public boolean isAddedToEnergyNet()
    {
        return this.addedToEnergyNet;
    }

    public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
    {
        return true;
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
}
