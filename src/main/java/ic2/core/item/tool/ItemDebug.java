package ic2.core.item.tool;

import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import ic2.api.reactor.IReactor;
import ic2.api.tile.IEnergyStorage;
import ic2.core.IC2;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.TileEntityCrop;
import ic2.core.block.generator.tileentity.TileEntityBaseGenerator;
import ic2.core.block.machine.tileentity.TileEntityElectricMachine;
import ic2.core.block.personal.IPersonalBlock;
import ic2.core.init.InternalName;
import ic2.core.item.InfiniteElectricItemManager;
import ic2.core.item.ItemIC2;
import ic2.core.util.StackUtil;
import ic2.core.util.Util;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemDebug extends ItemIC2 implements ISpecialElectricItem
{
    private static final String[] modes = new String[] {"Interfaces and Fields", "Tile Data"};
    private static IElectricItemManager manager = null;

    public ItemDebug(Configuration config, InternalName internalName)
    {
        super(config, internalName);
        this.setHasSubtypes(false);

        if (!Util.inDev())
        {
            this.setCreativeTab((CreativeTabs)null);
        }
    }

    public String getUnlocalizedName(ItemStack stack)
    {
        return "debugItem";
    }

    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
        int mode = nbtData.getInteger("mode");

        if (player.isSneaking())
        {
            if (IC2.platform.isSimulating())
            {
                mode = (mode + 1) % modes.length;
                nbtData.setInteger("mode", mode);
                IC2.platform.messagePlayer(player, "Debug Item Mode: " + modes[mode], new Object[0]);
            }

            return false;
        }
        else
        {
            switch (mode)
            {
                case 0:
                    PrintStream var24 = new PrintStream(new FileOutputStream(FileDescriptor.out));
                    int var29 = world.getBlockId(x, y, z);
                    int meta = world.getBlockMetadata(x, y, z);
                    TileEntity tileEntity1 = world.getBlockTileEntity(x, y, z);
                    String plat = IC2.platform.isRendering() ? (IC2.platform.isSimulating() ? "sp" : "client") : "server";
                    String message;

                    if (var29 < Block.blocksList.length && Block.blocksList[var29] != null)
                    {
                        message = "[" + plat + "] id: " + var29 + " meta: " + meta + " name: " + Block.blocksList[var29].getUnlocalizedName() + " te: " + tileEntity1;
                    }
                    else
                    {
                        message = "[" + plat + "] id: " + var29 + " meta: " + meta + " name: null te: " + tileEntity1;
                    }

                    IC2.platform.messagePlayer(player, message, new Object[0]);
                    var24.println(message);

                    if (tileEntity1 != null)
                    {
                        message = "[" + plat + "] interfaces:";
                        Class var241 = tileEntity1.getClass();

                        do
                        {
                            Class[] var27 = var241.getInterfaces();
                            int len$ = var27.length;

                            for (int i$ = 0; i$ < len$; ++i$)
                            {
                                Class i = var27[i$];
                                message = message + " " + i.getName();
                            }

                            var241 = var241.getSuperclass();
                        }
                        while (var241 != null);

                        IC2.platform.messagePlayer(player, message, new Object[0]);
                        var24.println(message);
                    }

                    if (var29 < Block.blocksList.length && Block.blocksList[var29] != null && Block.blocksList[var29].blockID != 0)
                    {
                        var24.println("block fields:");
                        dumpObjectFields(var24, Block.blocksList[var29]);
                    }

                    if (tileEntity1 != null)
                    {
                        var24.println("tile entity fields:");
                        dumpObjectFields(var24, tileEntity1);
                    }

                    var24.flush();
                    break;

                case 1:
                    if (!IC2.platform.isSimulating())
                    {
                        return false;
                    }

                    TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

                    if (tileEntity instanceof TileEntityBlock)
                    {
                        TileEntityBlock var30 = (TileEntityBlock)tileEntity;
                        IC2.platform.messagePlayer(player, "Block: Active=" + var30.getActive() + " Facing=" + var30.getFacing(), new Object[0]);
                    }

                    if (tileEntity instanceof TileEntityBaseGenerator)
                    {
                        TileEntityBaseGenerator var26 = (TileEntityBaseGenerator)tileEntity;
                        IC2.platform.messagePlayer(player, "BaseGen: Fuel=" + var26.fuel + " Storage=" + var26.storage, new Object[0]);
                    }

                    if (tileEntity instanceof TileEntityElectricMachine)
                    {
                        TileEntityElectricMachine var25 = (TileEntityElectricMachine)tileEntity;
                        IC2.platform.messagePlayer(player, "ElecMachine: Energy=" + var25.energy, new Object[0]);
                    }

                    if (tileEntity instanceof IEnergyStorage)
                    {
                        IEnergyStorage var28 = (IEnergyStorage)tileEntity;
                        IC2.platform.messagePlayer(player, "EnergyStorage: Stored=" + var28.getStored(), new Object[0]);
                    }

                    if (tileEntity instanceof IReactor)
                    {
                        IReactor var291 = (IReactor)tileEntity;
                        IC2.platform.messagePlayer(player, "Reactor: Heat=" + var291.getHeat() + " MaxHeat=" + var291.getMaxHeat() + " HEM=" + var291.getHeatEffectModifier() + " Output=" + var291.getOutput(), new Object[0]);
                    }

                    if (tileEntity instanceof IPersonalBlock)
                    {
                        IPersonalBlock var301 = (IPersonalBlock)tileEntity;
                        IC2.platform.messagePlayer(player, "PersonalBlock: CanAccess=" + var301.permitsAccess(player), new Object[0]);
                    }

                    if (tileEntity instanceof TileEntityCrop)
                    {
                        TileEntityCrop var31 = (TileEntityCrop)tileEntity;
                        IC2.platform.messagePlayer(player, "PersonalBlock: Crop=" + var31.id + " Size=" + var31.size + " Growth=" + var31.statGrowth + " Gain=" + var31.statGain + " Resistance=" + var31.statResistance + " Nutrients=" + var31.nutrientStorage + " Water=" + var31.waterStorage + " GrowthPoints=" + var31.growthPoints, new Object[0]);
                    }
            }

            return IC2.platform.isSimulating();
        }
    }

    private static void dumpObjectFields(PrintStream ps, Object o)
    {
        Class fieldDeclaringClass = o.getClass();

        do
        {
            Field[] fields = fieldDeclaringClass.getDeclaredFields();
            Field[] arr$ = fields;
            int len$ = fields.length;

            for (int i$ = 0; i$ < len$; ++i$)
            {
                Field field = arr$[i$];

                if ((field.getModifiers() & 8) == 0 || fieldDeclaringClass != Block.class && fieldDeclaringClass != TileEntity.class)
                {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);

                    try
                    {
                        Object var13 = field.get(o);
                        ps.println(field.getName() + " class: " + fieldDeclaringClass.getName() + " type: " + field.getType());
                        ps.println("    identity hash: " + System.identityHashCode(o) + " hash: " + o.hashCode() + " modifiers: " + field.getModifiers());

                        if (field.getType().isArray())
                        {
                            ArrayList var14 = new ArrayList();

                            for (int var15 = 0; var15 < Array.getLength(var13); ++var15)
                            {
                                var14.add(Array.get(var13, var15));
                            }

                            var13 = var14;
                        }

                        if (var13 instanceof Iterable)
                        {
                            ps.println("    values:");
                            int var141 = 0;

                            for (Iterator var151 = ((Iterable)var13).iterator(); var151.hasNext(); ++var141)
                            {
                                ps.println("      [" + var141 + "] " + getValueString(var151.next()));
                            }
                        }
                        else
                        {
                            ps.println("    value: " + getValueString(var13));
                        }
                    }
                    catch (IllegalAccessException var12)
                    {
                        ps.println("name: " + fieldDeclaringClass.getName() + "." + field.getName() + " type: " + field.getType() + " value: <can\'t access>");
                    }
                    catch (NullPointerException var131)
                    {
                        ps.println("name: " + fieldDeclaringClass.getName() + "." + field.getName() + " type: " + field.getType() + " value: <null>");
                    }

                    field.setAccessible(accessible);
                }
            }

            fieldDeclaringClass = fieldDeclaringClass.getSuperclass();
        }
        while (fieldDeclaringClass != null);
    }

    private static String getValueString(Object o)
    {
        if (o == null)
        {
            return "<null>";
        }
        else
        {
            String ret = o.toString();

            if (o.getClass().isArray())
            {
                for (int i = 0; i < Array.getLength(o); ++i)
                {
                    ret = ret + " [" + i + "] " + Array.get(o, i);
                }
            }

            if (ret.length() > 100)
            {
                ret = ret.substring(0, 90) + "... (" + ret.length() + " more)";
            }

            return ret;
        }
    }

    public boolean canProvideEnergy(ItemStack itemStack)
    {
        return true;
    }

    public int getChargedItemId(ItemStack itemStack)
    {
        return super.itemID;
    }

    public int getEmptyItemId(ItemStack itemStack)
    {
        return super.itemID;
    }

    public int getMaxCharge(ItemStack itemStack)
    {
        return Integer.MAX_VALUE;
    }

    public int getTier(ItemStack itemStack)
    {
        return 1;
    }

    public int getTransferLimit(ItemStack itemStack)
    {
        return Integer.MAX_VALUE;
    }

    public IElectricItemManager getManager(ItemStack itemStack)
    {
        if (manager == null)
        {
            manager = new InfiniteElectricItemManager();
        }

        return manager;
    }
}
