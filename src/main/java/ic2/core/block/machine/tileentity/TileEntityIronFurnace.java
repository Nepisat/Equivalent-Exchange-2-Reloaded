package ic2.core.block.machine.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlotConsumableFuel;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotProcessable;
import ic2.core.block.invslot.InvSlotProcessableSmelting;
import ic2.core.block.machine.ContainerIronFurnace;
import ic2.core.block.machine.gui.GuiIronFurnace;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityIronFurnace extends TileEntityInventory implements IHasGui
{
    public int fuel = 0;
    public int maxFuel = 0;
    public short progress = 0;
    public final short operationLength = 160;
    public final InvSlotProcessable inputSlot = new InvSlotProcessableSmelting(this, "input", 0, 1);
    public final InvSlotOutput outputSlot = new InvSlotOutput(this, "output", 2, 1);
    public final InvSlotConsumableFuel fuelSlot = new InvSlotConsumableFuel(this, "fuel", 1, 1, true);

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);

        try
        {
            this.fuel = nbttagcompound.getInteger("fuel");
        }
        catch (Throwable var4)
        {
            this.fuel = nbttagcompound.getShort("fuel");
        }

        try
        {
            this.maxFuel = nbttagcompound.getInteger("maxFuel");
        }
        catch (Throwable var3)
        {
            this.maxFuel = nbttagcompound.getShort("maxFuel");
        }

        this.progress = nbttagcompound.getShort("progress");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("fuel", this.fuel);
        nbttagcompound.setInteger("maxFuel", this.maxFuel);
        nbttagcompound.setShort("progress", this.progress);
    }

    public int gaugeProgressScaled(int i)
    {
        return this.progress * i / 160;
    }

    public int gaugeFuelScaled(int i)
    {
        if (this.maxFuel == 0)
        {
            this.maxFuel = this.fuel;

            if (this.maxFuel == 0)
            {
                this.maxFuel = 160;
            }
        }

        return this.fuel * i / this.maxFuel;
    }

    public boolean enableUpdateEntity()
    {
        return IC2.platform.isSimulating();
    }

    public void updateEntity()
    {
        super.updateEntity();
        boolean wasOperating = this.isBurning();
        boolean needsInvUpdate = false;

        if (this.fuel <= 0 && this.canOperate())
        {
            this.fuel = this.maxFuel = this.fuelSlot.consumeFuel();

            if (this.fuel > 0)
            {
                needsInvUpdate = true;
            }
        }

        if (this.isBurning() && this.canOperate())
        {
            ++this.progress;

            if (this.progress >= 160)
            {
                this.progress = 0;
                this.operate();
                needsInvUpdate = true;
            }
        }
        else
        {
            this.progress = 0;
        }

        if (this.fuel > 0)
        {
            --this.fuel;
        }

        if (wasOperating != this.isBurning())
        {
            this.setActive(this.isBurning());
            needsInvUpdate = true;
        }

        if (needsInvUpdate)
        {
            this.onInventoryChanged();
        }
    }

    public void operate()
    {
        this.outputSlot.add(this.inputSlot.process(false));
    }

    public boolean isBurning()
    {
        return this.fuel > 0;
    }

    public boolean canOperate()
    {
        ItemStack result = this.inputSlot.process(true);
        return result == null ? false : this.outputSlot.canAdd(result);
    }

    public ItemStack getResultFor(ItemStack itemstack)
    {
        return FurnaceRecipes.smelting().getSmeltingResult(itemstack);
    }

    public String getInvName()
    {
        return "Iron Furnace";
    }

    public ContainerBase getGuiContainer(EntityPlayer entityPlayer)
    {
        return new ContainerIronFurnace(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return new GuiIronFurnace(new ContainerIronFurnace(entityPlayer, this));
    }

    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
