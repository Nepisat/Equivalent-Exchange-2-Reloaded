package ic2.core.block.generator.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.block.generator.container.ContainerWaterGenerator;
import ic2.core.block.generator.gui.GuiWaterGenerator;
import ic2.core.block.invslot.InvSlot$Access;
import ic2.core.block.invslot.InvSlot$InvSide;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

public class TileEntityWaterGenerator extends TileEntityBaseGenerator
{
    public static Random randomizer = new Random();
    public int ticker;
    public int water = 0;
    public int microStorage = 0;
    public int maxWater = 2000;
    public final InvSlotConsumableLiquid fuelSlot;

    public TileEntityWaterGenerator()
    {
        super(2, 2);
        super.production = 2;
        this.ticker = randomizer.nextInt(this.tickRate());
        this.fuelSlot = new InvSlotConsumableLiquid(this, "fuel", 1, InvSlot$Access.NONE, 1, InvSlot$InvSide.TOP, FluidRegistry.WATER);
    }

    public void onLoaded()
    {
        super.onLoaded();
        this.updateWaterCount();
    }

    public int gaugeFuelScaled(int i)
    {
        return super.fuel <= 0 ? 0 : super.fuel * i / this.maxWater;
    }

    public boolean gainFuel()
    {
        if (super.fuel + 500 > this.maxWater)
        {
            return false;
        }
        else if (!this.fuelSlot.isEmpty())
        {
            ItemStack liquid = this.fuelSlot.consume(1);

            if (liquid == null)
            {
                return false;
            }
            else
            {
                super.fuel += 500;

                if (liquid.getItem().hasContainerItem())
                {
                    super.production = 1;
                }
                else
                {
                    super.production = 2;
                }

                return true;
            }
        }
        else if (super.fuel <= 0)
        {
            this.flowPower();
            super.production = this.microStorage / 100;
            this.microStorage -= super.production * 100;

            if (super.production > 0)
            {
                ++super.fuel;
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean gainFuelSub(ItemStack stack)
    {
        return false;
    }

    public boolean needsFuel()
    {
        return super.fuel <= this.maxWater;
    }

    public void flowPower()
    {
        if (this.ticker++ % this.tickRate() == 0)
        {
            this.updateWaterCount();
        }

        this.water = this.water * IC2.energyGeneratorWater / 100;

        if (this.water > 0)
        {
            this.microStorage += this.water;
        }
    }

    public void updateWaterCount()
    {
        int count = 0;

        for (int x = super.xCoord - 1; x < super.xCoord + 2; ++x)
        {
            for (int y = super.yCoord - 1; y < super.yCoord + 2; ++y)
            {
                for (int z = super.zCoord - 1; z < super.zCoord + 2; ++z)
                {
                    if (super.worldObj.getBlockId(x, y, z) == Block.waterMoving.blockID || super.worldObj.getBlockId(x, y, z) == Block.waterStill.blockID)
                    {
                        ++count;
                    }
                }
            }
        }

        this.water = count;
    }

    public String getInvName()
    {
        return "Water Mill";
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return new GuiWaterGenerator(new ContainerWaterGenerator(entityPlayer, this));
    }

    public int tickRate()
    {
        return 128;
    }

    public String getOperationSoundFile()
    {
        return "Generators/WatermillLoop.ogg";
    }

    public boolean delayActiveUpdate()
    {
        return true;
    }

    public ContainerBase getGuiContainer(EntityPlayer entityPlayer)
    {
        return new ContainerWaterGenerator(entityPlayer, this);
    }
}
