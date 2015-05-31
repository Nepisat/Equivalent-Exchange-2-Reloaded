package ic2.core.block.machine.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.block.invslot.InvSlotProcessableSmelting;
import ic2.core.block.machine.ContainerStandardMachine;
import ic2.core.block.machine.gui.GuiElecFurnace;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class TileEntityElectricFurnace extends TileEntityStandardMachine
{
    public TileEntityElectricFurnace()
    {
        super(3, 130);
        super.inputSlot = new InvSlotProcessableSmelting(this, "input", 0, 1);
    }

    public ItemStack getResultFor(ItemStack input, boolean adjustInput)
    {
        ItemStack result = FurnaceRecipes.smelting().getSmeltingResult(input);

        if (adjustInput && result != null)
        {
            --input.stackSize;
        }

        return result;
    }

    public String getInvName()
    {
        return "Electric Furnace";
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return new GuiElecFurnace(new ContainerStandardMachine(entityPlayer, this));
    }

    public String getStartSoundFile()
    {
        return "Machines/Electro Furnace/ElectroFurnaceLoop.ogg";
    }

    public String getInterruptSoundFile()
    {
        return null;
    }
}
