package ic2.core.block.machine.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.recipe.Recipes;
import ic2.core.BasicMachineRecipeManager;
import ic2.core.Ic2Items;
import ic2.core.block.invslot.InvSlotProcessableGeneric;
import ic2.core.block.machine.ContainerStandardMachine;
import ic2.core.block.machine.gui.GuiExtractor;
import ic2.core.util.StackUtil;
import java.util.List;
import java.util.Vector;
import java.util.Map.Entry;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class TileEntityExtractor extends TileEntityStandardMachine
{
    public static List<Entry<ItemStack, ItemStack>> recipes = new Vector();

    public TileEntityExtractor()
    {
        super(2, 400);
        super.inputSlot = new InvSlotProcessableGeneric(this, "input", 0, 1, Recipes.extractor);
    }

    public static void init()
    {
        Recipes.extractor = new BasicMachineRecipeManager();

        if (Ic2Items.rubberSapling != null)
        {
            Recipes.extractor.addRecipe(Ic2Items.rubberSapling, Ic2Items.rubber);
        }

        Recipes.extractor.addRecipe(Ic2Items.resin, StackUtil.copyWithSize(Ic2Items.rubber, 3));
        Recipes.extractor.addRecipe(Ic2Items.bioCell, Ic2Items.biofuelCell);
        Recipes.extractor.addRecipe(Ic2Items.hydratedCoalCell, Ic2Items.coalfuelCell);
        Recipes.extractor.addRecipe(Ic2Items.waterCell, Ic2Items.hydratingCell);
    }

    public ItemStack getResultFor(ItemStack itemStack, boolean adjustInput)
    {
        return (ItemStack)Recipes.extractor.getOutputFor(itemStack, adjustInput);
    }

    public String getInvName()
    {
        return "Extractor";
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return new GuiExtractor(new ContainerStandardMachine(entityPlayer, this));
    }

    public String getStartSoundFile()
    {
        return "Machines/ExtractorOp.ogg";
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
