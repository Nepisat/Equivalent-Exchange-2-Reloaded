package ic2.core.block.machine.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.recipe.Recipes;
import ic2.core.BasicMachineRecipeManager;
import ic2.core.Ic2Items;
import ic2.core.block.invslot.InvSlotProcessableGeneric;
import ic2.core.block.machine.ContainerStandardMachine;
import ic2.core.block.machine.gui.GuiMacerator;
import ic2.core.util.StackUtil;
import java.util.List;
import java.util.Vector;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TileEntityMacerator extends TileEntityStandardMachine
{
    public static List<Entry<ItemStack, ItemStack>> recipes = new Vector();

    public TileEntityMacerator()
    {
        super(2, 400);
        super.inputSlot = new InvSlotProcessableGeneric(this, "input", 0, 1, Recipes.macerator);
    }

    public static void init()
    {
        Recipes.macerator = new BasicMachineRecipeManager();
        Recipes.macerator.addRecipe(new ItemStack(Block.oreIron), StackUtil.copyWithSize(Ic2Items.ironDust, 2));
        Recipes.macerator.addRecipe(new ItemStack(Block.oreGold), StackUtil.copyWithSize(Ic2Items.goldDust, 2));
        Recipes.macerator.addRecipe(new ItemStack(Item.coal), Ic2Items.coalDust);
        Recipes.macerator.addRecipe(new ItemStack(Item.ingotIron), Ic2Items.ironDust);
        Recipes.macerator.addRecipe(new ItemStack(Item.ingotGold), Ic2Items.goldDust);
        Recipes.macerator.addRecipe(new ItemStack(Block.cloth), new ItemStack(Item.silk));
        Recipes.macerator.addRecipe(new ItemStack(Block.gravel), new ItemStack(Item.flint));
        Recipes.macerator.addRecipe(new ItemStack(Block.stone), new ItemStack(Block.cobblestone));
        Recipes.macerator.addRecipe(new ItemStack(Block.cobblestone), new ItemStack(Block.sand));
        Recipes.macerator.addRecipe(new ItemStack(Block.sandStone), new ItemStack(Block.sand));
        Recipes.macerator.addRecipe(new ItemStack(Block.ice), new ItemStack(Item.snowball));
        Recipes.macerator.addRecipe(new ItemStack(Block.blockClay), StackUtil.copyWithSize(Ic2Items.clayDust, 2));
        Recipes.macerator.addRecipe(new ItemStack(Block.glowStone), new ItemStack(Item.glowstone, 4));
        Recipes.macerator.addRecipe(new ItemStack(Item.bone), new ItemStack(Item.dyePowder, 5, 15));
        Recipes.macerator.addRecipe(Ic2Items.plantBall, new ItemStack(Block.dirt, 8));
        Recipes.macerator.addRecipe(Ic2Items.coffeeBeans, StackUtil.copyWithSize(Ic2Items.coffeePowder, 3));
        Recipes.macerator.addRecipe(new ItemStack(Item.blazeRod), new ItemStack(Item.blazePowder, 5));
        Recipes.macerator.addRecipe(new ItemStack(Item.spiderEye), StackUtil.copyWithSize(Ic2Items.grinPowder, 2));
        Recipes.macerator.addRecipe(new ItemStack(Item.poisonousPotato), Ic2Items.grinPowder);
        Recipes.macerator.addRecipe(new ItemStack(Block.blockNetherQuartz), new ItemStack(Item.netherQuartz, 4));
        Recipes.macerator.addRecipe(new ItemStack(Block.blockNetherQuartz, 1, 1), new ItemStack(Item.netherQuartz, 4));
        Recipes.macerator.addRecipe(new ItemStack(Block.blockNetherQuartz, 1, 2), new ItemStack(Item.netherQuartz, 4));
        Recipes.macerator.addRecipe(new ItemStack(Block.stoneSingleSlab, 1, 7), new ItemStack(Item.netherQuartz, 2));
        Recipes.macerator.addRecipe(new ItemStack(Block.stairsNetherQuartz, 1), new ItemStack(Item.netherQuartz, 6));
    }

    public ItemStack getResultFor(ItemStack itemStack, boolean adjustInput)
    {
        return (ItemStack)Recipes.macerator.getOutputFor(itemStack, adjustInput);
    }

    public String getInvName()
    {
        return "Macerator";
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return new GuiMacerator(new ContainerStandardMachine(entityPlayer, this));
    }

    public String getStartSoundFile()
    {
        return "Machines/MaceratorOp.ogg";
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
