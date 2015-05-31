package ic2.core.block.machine.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.Direction;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.audio.AudioSource;
import ic2.core.audio.PositionSpec;
import ic2.core.block.invslot.InvSlotConsumableLiquidContainer;
import ic2.core.block.machine.ContainerPump;
import ic2.core.block.machine.gui.GuiPump;
import ic2.core.util.StackUtil;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;

public class TileEntityPump extends TileEntityElectricMachine implements IHasGui
{
    public short pumpCharge = 0;
    private AudioSource audioSource;
    private int lastX;
    private int lastY;
    private int lastZ;
    private TileEntityMiner miner = null;
    public InvSlotConsumableLiquidContainer containerSlot = new InvSlotConsumableLiquidContainer(this, "container", 0, 1);

    public TileEntityPump()
    {
        super(200, 2, 1);
    }

    public void onUnloaded()
    {
        if (IC2.platform.isRendering() && this.audioSource != null)
        {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
        }

        this.miner = null;
        super.onUnloaded();
    }

    public void updateEntity()
    {
        super.updateEntity();

        if (!this.isPumpReady())
        {
            if (super.energy > 0)
            {
                int extraCharge = Math.min(super.energy, 128);
                super.energy -= extraCharge;
                this.pumpCharge = (short)(this.pumpCharge + extraCharge);
                this.setActive(true);
            }
            else
            {
                this.setActive(false);
            }
        }
        else if (this.operate())
        {
            this.pumpCharge = (short)(this.pumpCharge - 200);
            this.onInventoryChanged();
            this.setActive(true);
        }
        else
        {
            this.setActive(false);
        }
    }

    public String getInvName()
    {
        return "Pump";
    }

    public boolean operate()
    {
        if (!this.canHarvest())
        {
            return false;
        }
        else
        {
            int drops;

            if (this.miner == null || this.miner.isInvalid())
            {
                this.miner = null;
                Direction[] var6 = Direction.values();
                int var8 = var6.length;

                for (drops = 0; drops < var8; ++drops)
                {
                    Direction var9 = var6[drops];

                    if (var9 != Direction.YP)
                    {
                        TileEntity var11 = var9.applyToTileEntity(this);

                        if (var11 instanceof TileEntityMiner)
                        {
                            this.miner = (TileEntityMiner)var11;
                            break;
                        }
                    }
                }
            }

            FluidStack var61 = null;

            if (this.miner != null)
            {
                if (this.miner.canProvideLiquid)
                {
                    var61 = this.pump(this.miner.liquidX, this.miner.liquidY, this.miner.liquidZ);
                }
            }
            else
            {
                ForgeDirection[] var7 = ForgeDirection.VALID_DIRECTIONS;
                drops = var7.length;

                for (int var91 = 0; var91 < drops; ++var91)
                {
                    ForgeDirection var10 = var7[var91];
                    var61 = this.pump(super.xCoord + var10.offsetX, super.yCoord + var10.offsetY, super.zCoord + var10.offsetZ);

                    if (var61 != null)
                    {
                        break;
                    }
                }
            }

            if (var61 != null)
            {
                ItemStack var81 = this.containerSlot.fill(var61, false);

                if (var81 != null)
                {
                    ArrayList var111 = new ArrayList();
                    var111.add(var81);
                    StackUtil.distributeDrop(this, var111);
                }
                else if (!this.putInChestBucket(var61))
                {
                    return false;
                }

                this.clearLastBlock();
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public void clearLastBlock()
    {
        super.worldObj.setBlockToAir(this.lastX, this.lastY, this.lastZ);
    }

    public FluidStack pump(int x, int y, int z)
    {
        this.lastX = x;
        this.lastY = y;
        this.lastZ = z;
        int blockId = super.worldObj.getBlockId(x, y, z);

        if (blockId == 0)
        {
            return null;
        }
        else
        {
            FluidStack ret = null;

            if (Block.blocksList[blockId] instanceof IFluidBlock)
            {
                IFluidBlock liquid = (IFluidBlock)Block.blocksList[blockId];
                ret = liquid.drain(super.worldObj, x, y, z, true);
            }
            else if (blockId != Block.waterStill.blockID && blockId != Block.waterMoving.blockID)
            {
                if (blockId == Block.lavaStill.blockID || blockId == Block.lavaMoving.blockID)
                {
                    if (super.worldObj.getBlockMetadata(x, y, z) != 0)
                    {
                        return null;
                    }

                    ret = new FluidStack(FluidRegistry.LAVA, 1000);
                }
            }
            else
            {
                if (super.worldObj.getBlockMetadata(x, y, z) != 0)
                {
                    return null;
                }

                ret = new FluidStack(FluidRegistry.WATER, 1000);
            }

            return ret;
        }
    }

    public boolean putInChestBucket(FluidStack liquid)
    {
        return this.putInChestBucket(super.xCoord, super.yCoord + 1, super.zCoord, liquid) || this.putInChestBucket(super.xCoord, super.yCoord - 1, super.zCoord, liquid) || this.putInChestBucket(super.xCoord + 1, super.yCoord, super.zCoord, liquid) || this.putInChestBucket(super.xCoord - 1, super.yCoord, super.zCoord, liquid) || this.putInChestBucket(super.xCoord, super.yCoord, super.zCoord + 1, liquid) || this.putInChestBucket(super.xCoord, super.yCoord, super.zCoord - 1, liquid);
    }

    public boolean putInChestBucket(int x, int y, int z, FluidStack liquid)
    {
        if (!(super.worldObj.getBlockTileEntity(x, y, z) instanceof TileEntityChest))
        {
            return false;
        }
        else
        {
            TileEntityChest chest = (TileEntityChest)super.worldObj.getBlockTileEntity(x, y, z);

            for (int i = 0; i < chest.getSizeInventory(); ++i)
            {
                ItemStack container = chest.getStackInSlot(i);

                if (container != null)
                {
                    ItemStack filled = FluidContainerRegistry.fillFluidContainer(liquid, container);

                    if (filled != null)
                    {
                        --container.stackSize;

                        if (container.stackSize <= 0)
                        {
                            chest.setInventorySlotContents(i, filled);
                        }
                        else
                        {
                            for (int var11 = 0; var11 < chest.getSizeInventory(); ++var11)
                            {
                                ItemStack itemStack = chest.getStackInSlot(var11);

                                if (itemStack == null)
                                {
                                    chest.setInventorySlotContents(var11, filled);
                                    return true;
                                }

                                if (StackUtil.isStackEqual(itemStack, filled) && itemStack.stackSize < itemStack.getMaxStackSize())
                                {
                                    ++itemStack.stackSize;
                                    return true;
                                }
                            }

                            ArrayList var111 = new ArrayList();
                            var111.add(filled);
                            StackUtil.distributeDrop(this, var111);
                        }

                        return true;
                    }
                }
            }

            return false;
        }
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        this.pumpCharge = nbttagcompound.getShort("pumpCharge");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("pumpCharge", this.pumpCharge);
    }

    public boolean isPumpReady()
    {
        return this.pumpCharge >= 200;
    }

    public boolean canHarvest()
    {
        return !this.isPumpReady() ? false : !this.containerSlot.isEmpty() || this.isBucketInChestAvaible();
    }

    public boolean isBucketInChestAvaible()
    {
        return this.isBucketInChestAvaible(super.xCoord, super.yCoord + 1, super.zCoord) || this.isBucketInChestAvaible(super.xCoord, super.yCoord - 1, super.zCoord) || this.isBucketInChestAvaible(super.xCoord + 1, super.yCoord, super.zCoord) || this.isBucketInChestAvaible(super.xCoord - 1, super.yCoord, super.zCoord) || this.isBucketInChestAvaible(super.xCoord, super.yCoord, super.zCoord + 1) || this.isBucketInChestAvaible(super.xCoord, super.yCoord, super.zCoord - 1);
    }

    public boolean isBucketInChestAvaible(int x, int y, int z)
    {
        if (!(super.worldObj.getBlockTileEntity(x, y, z) instanceof TileEntityChest))
        {
            return false;
        }
        else
        {
            TileEntityChest chest = (TileEntityChest)super.worldObj.getBlockTileEntity(x, y, z);

            for (int i = 0; i < chest.getSizeInventory(); ++i)
            {
                if (chest.getStackInSlot(i) != null && chest.getStackInSlot(i).itemID == Item.bucketEmpty.itemID)
                {
                    return true;
                }
            }

            return false;
        }
    }

    public ContainerBase getGuiContainer(EntityPlayer entityPlayer)
    {
        return new ContainerPump(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return new GuiPump(new ContainerPump(entityPlayer, this));
    }

    public void onGuiClosed(EntityPlayer entityPlayer) {}

    public void onNetworkUpdate(String field)
    {
        if (field.equals("active") && super.prevActive != this.getActive())
        {
            if (this.audioSource == null)
            {
                this.audioSource = IC2.audioManager.createSource(this, PositionSpec.Center, "Machines/PumpOp.ogg", true, false, IC2.audioManager.defaultVolume);
            }

            if (this.getActive())
            {
                if (this.audioSource != null)
                {
                    this.audioSource.play();
                }
            }
            else if (this.audioSource != null)
            {
                this.audioSource.stop();
            }
        }

        super.onNetworkUpdate(field);
    }
}
