package ic2.core.block.machine.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.Direction;
import ic2.api.recipe.Recipes;
import ic2.core.BasicMachineRecipeManager;
import ic2.core.Ic2Items;
import ic2.core.block.invslot.InvSlotProcessableGeneric;
import ic2.core.block.machine.ContainerStandardMachine;
import ic2.core.block.machine.gui.GuiCompressor;
import java.util.List;
import java.util.Vector;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityCompressor extends TileEntityStandardMachine
{
    public TileEntityPump validPump;
    public static List<Entry<ItemStack, ItemStack>> recipes = new Vector();

    public TileEntityCompressor()
    {
        super(2, 400);
        super.inputSlot = new InvSlotProcessableGeneric(this, "input", 0, 1, Recipes.compressor);
    }

    public static void init()
    {
        Recipes.compressor = new BasicMachineRecipeManager();
        Recipes.compressor.addRecipe(Ic2Items.plantBall, Ic2Items.compressedPlantBall);
        Recipes.compressor.addRecipe(Ic2Items.hydratedCoalDust, Ic2Items.hydratedCoalClump);
        Recipes.compressor.addRecipe(new ItemStack(Block.netherrack, 3), new ItemStack(Block.netherBrick));
        Recipes.compressor.addRecipe(new ItemStack(Block.sand), new ItemStack(Block.sandStone));
        Recipes.compressor.addRecipe(new ItemStack(Item.snowball), new ItemStack(Block.ice));
        Recipes.compressor.addRecipe(Ic2Items.waterCell, new ItemStack(Item.snowball));
        Recipes.compressor.addRecipe(Ic2Items.mixedMetalIngot, Ic2Items.advancedAlloy);
        Recipes.compressor.addRecipe(Ic2Items.carbonMesh, Ic2Items.carbonPlate);
        Recipes.compressor.addRecipe(Ic2Items.coalBall, Ic2Items.compressedCoalBall);
        Recipes.compressor.addRecipe(Ic2Items.coalChunk, new ItemStack(Item.diamond));
        Recipes.compressor.addRecipe(Ic2Items.constructionFoam, Ic2Items.constructionFoamPellet);
        Recipes.compressor.addRecipe(Ic2Items.cell, Ic2Items.airCell);
    }

    public ItemStack getResultFor(ItemStack itemStack, boolean adjustInput)
    {
        return (ItemStack)Recipes.compressor.getOutputFor(itemStack, adjustInput);
    }

    public boolean canOperate()
    {
        return super.canOperate() || this.getValidPump() != null && super.outputSlot.canAdd(new ItemStack(Item.snowball));
    }

    public void operate()
    {
        if (this.canOperate())
        {
            if (!super.inputSlot.isEmpty() && this.getResultFor(super.inputSlot.get(), false) != null)
            {
                super.operate();
            }
            else
            {
                TileEntityPump pump = this.getValidPump();

                if (pump == null)
                {
                    return;
                }

                pump.pumpCharge = 0;
                pump.clearLastBlock();
                super.outputSlot.add(new ItemStack(Item.snowball));
            }
        }
    }

    public TileEntityPump getValidPump()
    {
        if (this.validPump != null && !this.validPump.isInvalid())
        {
            FluidStack var7 = this.validPump.pump(this.validPump.xCoord, this.validPump.yCoord, this.validPump.zCoord);

            if (var7 != null && var7.getFluid() == FluidRegistry.WATER)
            {
                return this.validPump;
            }
        }

        this.validPump = null;
        Direction[] var71 = Direction.values();
        int len$ = var71.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            Direction dir = var71[i$];
            TileEntity te = dir.applyToTileEntity(this);

            if (te instanceof TileEntityPump)
            {
                FluidStack liquid = ((TileEntityPump)te).pump(this.validPump.xCoord, this.validPump.yCoord, this.validPump.zCoord);

                if (liquid != null && liquid.getFluid() == FluidRegistry.WATER)
                {
                    this.validPump = (TileEntityPump)te;
                    return this.validPump;
                }
            }
        }

        return null;
    }

    public String getInvName()
    {
        return "Compressor";
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return new GuiCompressor(new ContainerStandardMachine(entityPlayer, this));
    }

    public String getStartSoundFile()
    {
        return "Machines/CompressorOp.ogg";
    }

    public String getInterruptSoundFile()
    {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate()
    {
        return 0.85F;
    }
}
