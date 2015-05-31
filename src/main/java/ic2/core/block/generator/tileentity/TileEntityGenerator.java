package ic2.core.block.generator.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.block.generator.container.ContainerGenerator;
import ic2.core.block.generator.gui.GuiGenerator;
import ic2.core.block.invslot.InvSlotConsumableFuel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

public class TileEntityGenerator extends TileEntityBaseGenerator
{
    public int itemFuelTime = 0;
    public final InvSlotConsumableFuel fuelSlot = new InvSlotConsumableFuel(this, "fuel", 1, 1, false);

    public TileEntityGenerator()
    {
        super(IC2.energyGeneratorBase, 4000);
    }

    public int gaugeFuelScaled(int i)
    {
        if (super.fuel <= 0)
        {
            return 0;
        }
        else
        {
            if (this.itemFuelTime <= 0)
            {
                this.itemFuelTime = super.fuel;
            }

            return Math.min(super.fuel * i / this.itemFuelTime, i);
        }
    }

    public boolean gainFuel()
    {
        int fuelValue = this.fuelSlot.consumeFuel() / 4;

        if (fuelValue == 0)
        {
            return false;
        }
        else
        {
            super.fuel += fuelValue;
            this.itemFuelTime = fuelValue;
            return true;
        }
    }

    public String getInvName()
    {
        return "Generator";
    }

    public boolean isConverting()
    {
        return super.fuel > 0;
    }

    public String getOperationSoundFile()
    {
        return "Generators/GeneratorLoop.ogg";
    }

    public ContainerBase getGuiContainer(EntityPlayer entityPlayer)
    {
        return new ContainerGenerator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return new GuiGenerator(new ContainerGenerator(entityPlayer, this));
    }
}
