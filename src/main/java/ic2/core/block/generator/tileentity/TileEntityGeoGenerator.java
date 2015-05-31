package ic2.core.block.generator.tileentity;

import org.apache.commons.lang3.mutable.MutableObject;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.item.ElectricItem;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.generator.container.ContainerGeoGenerator;
import ic2.core.block.generator.gui.GuiGeoGenerator;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.FluidEvent.FluidFillingEvent;
import net.minecraftforge.fluids.FluidEvent.FluidSpilledEvent;

public class TileEntityGeoGenerator extends TileEntityBaseGenerator implements IFluidHandler
{
    public int maxLava = 24000;
    public final InvSlotConsumableLiquid fuelSlot;
    protected final FluidTank fluidTank;
    public TileEntityGeoGenerator()
    {
        super(IC2.energyGeneratorGeo, Math.max(IC2.energyGeneratorGeo, 32));
        this.fluidTank = new FluidTank(1000 * maxLava);
       
        this.fuelSlot = new InvSlotConsumableLiquid(this, "fuel", 1, 1, FluidRegistry.LAVA);
    }
    
    public int gaugeFuelScaled(int i)
    {
        return super.fuel <= 0 ? 0 : super.fuel * i / this.maxLava;
    }
    public void updateEntity()
    {
        super.updateEntity();
        //System.out.println(this.storage);
    }
/*
    public boolean gainFuel()
    {
        if (super.fuel + 1000 > this.maxLava)
        {
            return false;
        }
        else
        {
            ItemStack liquid = this.fuelSlot.consume(1);

            if (liquid == null)
            {
                return false;
            }
            else
            {
                super.fuel += 1000;
                System.out.println(this.fluidTank.getFluidAmount());
                return true;
            }
        }
    }

    public boolean gainFuelSub(ItemStack stack)
    {
        return false;
    }

    public boolean needsFuel()
    {
        return super.fuel <= this.maxLava;
    }

    public int distributeLava(int amount)
    {
        int need = this.maxLava - super.fuel;

        if (need > amount)
        {
            need = amount;
        }

        amount -= need;
        super.fuel += need / 2;
        return amount;
    }
*/
    public String getInvName()
    {
        return IC2.platform.isRendering() ? "Geothermal Generator" : "Geoth. Generator";
    }

    public String getOperationSoundFile()
    {
        return "Generators/GeothermalLoop.ogg";
    }

    public ContainerBase getGuiContainer(EntityPlayer entityPlayer)
    {
        return new ContainerGeoGenerator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return new GuiGeoGenerator(new ContainerGeoGenerator(entityPlayer, this));
    }

    public void onBlockBreak(int a, int b)
    {
        FluidEvent.fireEvent(new FluidSpilledEvent(new FluidStack(FluidRegistry.LAVA, 1000), super.worldObj, super.xCoord, super.yCoord, super.zCoord));
    }

  

    

    public int fill(FluidStack resource, boolean doFill)
    {
        if (resource.getFluid() != FluidRegistry.LAVA)
        {
            return 0;
        }
        else
        {
            int toAdd = Math.min(resource.amount, this.maxLava - super.fuel);

            if (doFill)
            {
                super.fuel += toAdd;
                FluidStack fluid = new FluidStack(resource, toAdd);
               // FluidEvent.fireEvent(new FluidFillingEvent(fluid, super.worldObj, super.xCoord, super.yCoord, super.zCoord, this));
            }

            return toAdd;
        }
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
    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return FluidRegistry.getFluidName(fluid.getID()).contentEquals("lava");
    }

    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return false;
    }

	@Override
	public boolean gainFuel() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}
	
}
