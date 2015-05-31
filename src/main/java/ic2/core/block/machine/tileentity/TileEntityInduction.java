package ic2.core.block.machine.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotProcessable;
import ic2.core.block.invslot.InvSlotProcessableSmelting;
import ic2.core.block.machine.ContainerInduction;
import ic2.core.block.machine.gui.GuiInduction;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityInduction extends TileEntityElectricMachine implements IHasGui
{
    public int soundTicker;
    public static short maxHeat = 10000;
    public short heat = 0;
    public short progress = 0;
    public final InvSlotProcessable inputSlotA;
    public final InvSlotProcessable inputSlotB;
    public final InvSlotOutput outputSlotA;
    public final InvSlotOutput outputSlotB;

    public TileEntityInduction()
    {
        super(maxHeat, 2, 2);
        this.soundTicker = IC2.random.nextInt(64);
        this.inputSlotA = new InvSlotProcessableSmelting(this, "inputA", 0, 1);
        this.inputSlotB = new InvSlotProcessableSmelting(this, "inputB", 1, 1);
        this.outputSlotA = new InvSlotOutput(this, "outputA", 3, 1);
        this.outputSlotB = new InvSlotOutput(this, "outputB", 4, 1);
    }

    public String getInvName()
    {
        return IC2.platform.isRendering() ? "Induction Furnace" : "InductionFurnace";
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        this.heat = nbttagcompound.getShort("heat");
        this.progress = nbttagcompound.getShort("progress");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("heat", this.heat);
        nbttagcompound.setShort("progress", this.progress);
    }

    public String getHeat()
    {
        return "" + this.heat * 100 / maxHeat + "%";
    }

    public int gaugeProgressScaled(int i)
    {
        return i * this.progress / 4000;
    }

    public int gaugeFuelScaled(int i)
    {
        return i * Math.min(super.energy, super.maxEnergy) / super.maxEnergy;
    }

    public void updateEntity()
    {
        super.updateEntity();
        boolean needsInvUpdate = false;
        boolean newActive = this.getActive();

        if (this.heat == 0)
        {
            newActive = false;
        }

        if (this.progress >= 4000)
        {
            this.operate();
            needsInvUpdate = true;
            this.progress = 0;
            newActive = false;
        }

        boolean canOperate = this.canOperate();

        if (super.energy > 0 && (canOperate || this.isRedstonePowered()))
        {
            --super.energy;

            if (this.heat < maxHeat)
            {
                ++this.heat;
            }

            newActive = true;
        }
        else
        {
            this.heat = (short)(this.heat - Math.min(this.heat, 4));
        }

        if (newActive && this.progress != 0)
        {
            if (!canOperate || super.energy < 15)
            {
                if (!canOperate)
                {
                    this.progress = 0;
                }

                newActive = false;
            }
        }
        else if (canOperate)
        {
            if (super.energy >= 15)
            {
                newActive = true;
            }
        }
        else
        {
            this.progress = 0;
        }

        if (newActive && canOperate)
        {
            this.progress = (short)(this.progress + this.heat / 30);
            super.energy -= 15;
        }

        if (needsInvUpdate)
        {
            this.onInventoryChanged();
        }

        if (newActive != this.getActive())
        {
            this.setActive(newActive);
        }
    }

    public void operate()
    {
        this.operate(this.inputSlotA, this.outputSlotA);
        this.operate(this.inputSlotB, this.outputSlotB);
    }

    public void operate(InvSlotProcessable inputSlot, InvSlotOutput outputSlot)
    {
        if (this.canOperate(inputSlot, outputSlot))
        {
            outputSlot.add(inputSlot.process(false));
        }
    }

    public boolean canOperate()
    {
        return this.canOperate(this.inputSlotA, this.outputSlotA) || this.canOperate(this.inputSlotB, this.outputSlotB);
    }

    public boolean canOperate(InvSlotProcessable inputSlot, InvSlotOutput outputSlot)
    {
        if (inputSlot.isEmpty())
        {
            return false;
        }
        else
        {
            ItemStack processResult = inputSlot.process(true);
            return processResult == null ? false : outputSlot.canAdd(processResult);
        }
    }

    public ContainerBase getGuiContainer(EntityPlayer entityPlayer)
    {
        return new ContainerInduction(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return new GuiInduction(new ContainerInduction(entityPlayer, this));
    }

    public void onGuiClosed(EntityPlayer entityPlayer) {}

    public float getWrenchDropRate()
    {
        return 0.8F;
    }
}
