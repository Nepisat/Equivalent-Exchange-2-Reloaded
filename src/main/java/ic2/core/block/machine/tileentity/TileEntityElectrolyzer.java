package ic2.core.block.machine.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.Ic2Items;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot$Access;
import ic2.core.block.invslot.InvSlot$InvSide;
import ic2.core.block.invslot.InvSlotConsumableId;
import ic2.core.block.machine.ContainerElectrolyzer;
import ic2.core.block.machine.gui.GuiElectrolyzer;
import ic2.core.block.wiring.TileEntityElectricBlock;
import java.util.Random;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityElectrolyzer extends TileEntityInventory implements IHasGui
{
    public static Random randomizer = new Random();
    public short energy = 0;
    public TileEntityElectricBlock mfe = null;
    public int ticker;
    public final InvSlotConsumableId waterSlot;
    public final InvSlotConsumableId hydrogenSlot;

    public TileEntityElectrolyzer()
    {
        this.ticker = randomizer.nextInt(16);
        this.waterSlot = new InvSlotConsumableId(this, "water", 0, InvSlot$Access.IO, 1, InvSlot$InvSide.TOP, new int[] {Ic2Items.waterCell.itemID});
        this.hydrogenSlot = new InvSlotConsumableId(this, "hydrogen", 1, InvSlot$Access.IO, 1, InvSlot$InvSide.BOTTOM, new int[] {Ic2Items.electrolyzedWaterCell.itemID});
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        this.energy = nbttagcompound.getShort("energy");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("energy", this.energy);
    }

    public String getInvName()
    {
        return "Electrolyzer";
    }

    public boolean enableUpdateEntity()
    {
        return IC2.platform.isSimulating();
    }

    public void updateEntity()
    {
        super.updateEntity();
        boolean needsInvUpdate = false;
        boolean turnActive = false;

        if (this.ticker++ % 16 == 0)
        {
            this.mfe = this.lookForMFE();
        }

        if (this.mfe != null)
        {
            if (this.shouldDrain() && this.canDrain())
            {
                needsInvUpdate = this.drain();
                turnActive = true;
            }

            if (this.shouldPower() && (this.canPower() || this.energy > 0))
            {
                needsInvUpdate = this.power();
                turnActive = true;
            }

            if (this.getActive() != turnActive)
            {
                this.setActive(turnActive);
                needsInvUpdate = true;
            }

            if (needsInvUpdate)
            {
                this.onInventoryChanged();
            }
        }
    }

    public boolean shouldDrain()
    {
        return this.mfe != null && (double)this.mfe.energy / (double)this.mfe.maxStorage >= 0.7D;
    }

    public boolean shouldPower()
    {
        return this.mfe != null && (double)this.mfe.energy / (double)this.mfe.maxStorage <= 0.3D;
    }

    public boolean canDrain()
    {
        return !this.waterSlot.isEmpty() && (this.hydrogenSlot.isEmpty() || this.hydrogenSlot.get().stackSize < this.hydrogenSlot.get().getMaxStackSize());
    }

    public boolean canPower()
    {
        return !this.hydrogenSlot.isEmpty() && (this.waterSlot.isEmpty() || this.waterSlot.get().stackSize < this.waterSlot.get().getMaxStackSize());
    }

    public boolean drain()
    {
        this.mfe.energy -= this.processRate();
        this.energy = (short)(this.energy + this.processRate());

        if (this.energy >= 20000)
        {
            this.energy = (short)(this.energy - 20000);
            this.waterSlot.consume(1);

            if (this.hydrogenSlot.isEmpty())
            {
                this.hydrogenSlot.put(Ic2Items.electrolyzedWaterCell.copy());
            }
            else
            {
                ++this.hydrogenSlot.get().stackSize;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean power()
    {
        if (this.energy > 0)
        {
            int out = Math.min(this.energy, this.processRate());
            this.energy = (short)(this.energy - out);
            this.mfe.energy += out;
            return false;
        }
        else
        {
            this.energy = (short)(this.energy + 12000 + 2000 * this.mfe.tier);
            this.hydrogenSlot.consume(1);

            if (this.waterSlot.isEmpty())
            {
                this.waterSlot.put(Ic2Items.waterCell.copy());
            }
            else
            {
                ++this.waterSlot.get().stackSize;
            }

            return true;
        }
    }

    public int processRate()
    {
        switch (this.mfe.tier)
        {
            case 2:
                return 8;

            case 3:
                return 32;

            default:
                return 2;
        }
    }

    public TileEntityElectricBlock lookForMFE()
    {
        ForgeDirection[] arr$ = ForgeDirection.VALID_DIRECTIONS;
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            ForgeDirection dir = arr$[i$];
            TileEntity te = super.worldObj.getBlockTileEntity(super.xCoord + dir.offsetX, super.yCoord + dir.offsetY, super.zCoord + dir.offsetZ);

            if (te instanceof TileEntityElectricBlock)
            {
                return (TileEntityElectricBlock)te;
            }
        }

        return null;
    }

    public int gaugeEnergyScaled(int i)
    {
        if (this.energy <= 0)
        {
            return 0;
        }
        else
        {
            int r = this.energy * i / 20000;

            if (r > i)
            {
                r = i;
            }

            return r;
        }
    }

    public ContainerBase getGuiContainer(EntityPlayer entityPlayer)
    {
        return new ContainerElectrolyzer(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return new GuiElectrolyzer(new ContainerElectrolyzer(entityPlayer, this));
    }

    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
