package ic2.core.block.machine.tileentity;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.Ic2Items;
import ic2.core.Ic2Player;
import ic2.core.InvSlotConsumableBlock;
import ic2.core.audio.AudioSource;
import ic2.core.audio.PositionSpec;
import ic2.core.block.invslot.InvSlot$Access;
import ic2.core.block.invslot.InvSlot$InvSide;
import ic2.core.block.invslot.InvSlotConsumableId;
import ic2.core.block.machine.ContainerMiner;
import ic2.core.block.machine.gui.GuiMiner;
import ic2.core.block.machine.tileentity.TileEntityMiner$MineResult;
import ic2.core.block.machine.tileentity.TileEntityMiner$Mode;
import ic2.core.item.tool.ItemScanner;
import ic2.core.util.Liquid;
import ic2.core.util.Liquid$LiquidData;
import ic2.core.util.StackUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeHooks;

public class TileEntityMiner extends TileEntityElectricMachine implements IHasGui
{
    private TileEntityMiner$Mode lastMode;
    public int progress;
    private int scannedLevel;
    private int scanRange;
    private int lastX;
    private int lastZ;
    public boolean pumpMode;
    public boolean canProvideLiquid;
    public int liquidX;
    public int liquidY;
    public int liquidZ;
    private AudioSource audioSource;
    public final InvSlotConsumableId drillSlot;
    public final InvSlotConsumableBlock pipeSlot;
    public final InvSlotConsumableId scannerSlot;

    public TileEntityMiner()
    {
        super(1000, IC2.enableMinerLapotron ? 3 : 1, 0);
        this.lastMode = TileEntityMiner$Mode.None;
        this.progress = 0;
        this.scannedLevel = -1;
        this.scanRange = 0;
        this.pumpMode = false;
        this.canProvideLiquid = false;
        this.drillSlot = new InvSlotConsumableId(this, "drill", 3, InvSlot$Access.IO, 1, InvSlot$InvSide.TOP, new int[] {Ic2Items.miningDrill.itemID, Ic2Items.diamondDrill.itemID});
        this.pipeSlot = new InvSlotConsumableBlock(this, "pipe", 2, InvSlot$Access.IO, 1, InvSlot$InvSide.SIDE);
        this.scannerSlot = new InvSlotConsumableId(this, "scanner", 1, InvSlot$Access.IO, 1, InvSlot$InvSide.BOTTOM, new int[] {Ic2Items.odScanner.itemID, Ic2Items.ovScanner.itemID});
    }

    public void onLoaded()
    {
        super.onLoaded();
        this.scannedLevel = -1;
        this.lastX = super.xCoord;
        this.lastZ = super.zCoord;
        this.canProvideLiquid = false;
    }

    public void onUnloaded()
    {
        if (IC2.platform.isRendering() && this.audioSource != null)
        {
            IC2.audioManager.removeSources(this);
            this.audioSource = null;
        }

        super.onUnloaded();
    }

    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        this.lastMode = TileEntityMiner$Mode.values()[nbtTagCompound.getInteger("lastMode")];
        this.progress = nbtTagCompound.getInteger("progress");
    }

    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("lastMode", this.lastMode.ordinal());
        nbtTagCompound.setInteger("progress", this.progress);
    }

    public void updateEntity()
    {
        super.updateEntity();
        this.chargeTools();

        if (this.work())
        {
            this.onInventoryChanged();
            this.setActive(true);
        }
        else
        {
            this.setActive(false);
        }
    }

    private void chargeTools()
    {
        if (!this.scannerSlot.isEmpty())
        {
            super.energy -= ElectricItem.manager.charge(this.scannerSlot.get(), super.energy, 2, false, false);
        }

        if (!this.drillSlot.isEmpty())
        {
            super.energy -= ElectricItem.manager.charge(this.drillSlot.get(), super.energy, 1, false, false);
        }
    }

    private boolean work()
    {
        int operationHeight = this.getOperationHeight();

        if (this.drillSlot.isEmpty())
        {
            return this.withDrawPipe(operationHeight);
        }
        else if (operationHeight >= 0)
        {
            int blockId = super.worldObj.getBlockId(super.xCoord, operationHeight, super.zCoord);

            if (blockId != Ic2Items.miningPipeTip.itemID)
            {
                return operationHeight > 0 ? this.digDown(operationHeight, false) : false;
            }
            else
            {
                TileEntityMiner$MineResult result = this.mineLevel(operationHeight);
                return result == TileEntityMiner$MineResult.Done ? this.digDown(operationHeight - 1, true) : result == TileEntityMiner$MineResult.Working;
            }
        }
        else
        {
            return false;
        }
    }

    private int getOperationHeight()
    {
        for (int y = super.yCoord - 1; y >= 0; --y)
        {
            int blockId = super.worldObj.getBlockId(super.xCoord, y, super.zCoord);

            if (blockId != Ic2Items.miningPipe.itemID)
            {
                return y;
            }
        }

        return -1;
    }

    private boolean withDrawPipe(int y)
    {
        if (this.lastMode != TileEntityMiner$Mode.Withdraw)
        {
            this.lastMode = TileEntityMiner$Mode.Withdraw;
            this.progress = 0;
        }

        if (y < 0 || super.worldObj.getBlockId(super.xCoord, y, super.zCoord) != Ic2Items.miningPipeTip.itemID)
        {
            ++y;
        }

        if (y != super.yCoord && super.energy >= 3)
        {
            if (this.progress < 20)
            {
                super.energy -= 3;
                ++this.progress;
            }
            else
            {
                this.progress = 0;
                this.removePipe(y);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    private void removePipe(int y)
    {
        super.worldObj.setBlockToAir(super.xCoord, y, super.zCoord);
        ArrayList drops = new ArrayList();
        drops.add(Ic2Items.miningPipe.copy());
        StackUtil.distributeDrop(this, drops);

        if (!this.pipeSlot.isEmpty() && this.pipeSlot.get().itemID != Ic2Items.miningPipe.itemID)
        {
            ItemStack filler = this.pipeSlot.consume(1);
            Item fillerItem = filler.getItem();

            if (fillerItem instanceof ItemBlock)
            {
                ((ItemBlock)fillerItem).onItemUse(filler, new Ic2Player(super.worldObj), super.worldObj, super.xCoord, y + 1, super.zCoord, 0, 0.0F, 0.0F, 0.0F);
            }
        }
    }

    private boolean digDown(int y, boolean removeTipAbove)
    {
        if (!this.pipeSlot.isEmpty() && this.pipeSlot.get().itemID == Ic2Items.miningPipe.itemID)
        {
            if (y < 0)
            {
                if (removeTipAbove)
                {
                    super.worldObj.setBlock(super.xCoord, y + 1, super.zCoord, Ic2Items.miningPipe.itemID);
                }

                return false;
            }
            else
            {
                TileEntityMiner$MineResult result = this.mineBlock(super.xCoord, y, super.zCoord);

                if (result != TileEntityMiner$MineResult.Failed_Temp && result != TileEntityMiner$MineResult.Failed_Perm)
                {
                    if (result == TileEntityMiner$MineResult.Done)
                    {
                        if (removeTipAbove)
                        {
                            super.worldObj.setBlock(super.xCoord, y + 1, super.zCoord, Ic2Items.miningPipe.itemID);
                        }

                        this.pipeSlot.consume(1);
                        super.worldObj.setBlock(super.xCoord, y, super.zCoord, Ic2Items.miningPipeTip.itemID);
                    }

                    return true;
                }
                else
                {
                    if (removeTipAbove)
                    {
                        super.worldObj.setBlock(super.xCoord, y + 1, super.zCoord, Ic2Items.miningPipe.itemID);
                    }

                    return false;
                }
            }
        }
        else
        {
            return false;
        }
    }

    private TileEntityMiner$MineResult mineLevel(int y)
    {
        if (this.scannerSlot.isEmpty())
        {
            return TileEntityMiner$MineResult.Done;
        }
        else
        {
            if (this.scannedLevel != y)
            {
                this.scanRange = ((ItemScanner)this.scannerSlot.get().getItem()).startLayerScan(this.scannerSlot.get());
            }

            if (this.scanRange <= 0)
            {
                return TileEntityMiner$MineResult.Failed_Temp;
            }
            else
            {
                this.scannedLevel = y;

                for (int x = super.xCoord - this.scanRange; x <= super.xCoord + this.scanRange; ++x)
                {
                    for (int z = super.zCoord - this.scanRange; z <= super.zCoord + this.scanRange; ++z)
                    {
                        int blockId = super.worldObj.getBlockId(x, y, z);
                        int meta = super.worldObj.getBlockMetadata(x, y, z);
                        boolean isValidTarget = false;

                        if (ItemScanner.isValuable(blockId, meta) && this.canMine(x, y, z))
                        {
                            isValidTarget = true;
                        }
                        else if (this.pumpMode)
                        {
                            Liquid$LiquidData var8 = Liquid.getLiquid(super.worldObj, x, y, z);

                            if (var8 != null && this.canPump(x, y, z))
                            {
                                isValidTarget = true;
                            }
                        }

                        if (isValidTarget)
                        {
                            TileEntityMiner$MineResult var81 = this.mineTowards(x, y, z);

                            if (var81 == TileEntityMiner$MineResult.Done)
                            {
                                return TileEntityMiner$MineResult.Working;
                            }

                            if (var81 != TileEntityMiner$MineResult.Failed_Perm)
                            {
                                return var81;
                            }
                        }
                    }
                }

                return TileEntityMiner$MineResult.Done;
            }
        }
    }

    private TileEntityMiner$MineResult mineTowards(int x, int y, int z)
    {
        int cx = super.xCoord;
        int cz = super.zCoord;
        boolean isBlocking;

        do
        {
            if (cx == x && cz == z)
            {
                this.lastX = super.xCoord;
                this.lastZ = super.zCoord;
                return TileEntityMiner$MineResult.Done;
            }

            boolean result1 = cx == this.lastX && cz == this.lastZ;

            if (Math.abs(x - cx) >= Math.abs(z - cz))
            {
                cx += x > cx ? 1 : -1;
            }
            else
            {
                cz += z > cz ? 1 : -1;
            }

            isBlocking = false;

            if (result1)
            {
                isBlocking = true;
            }
            else
            {
                int result = super.worldObj.getBlockId(cx, y, cz);

                if (result != 0 && !Block.blocksList[result].isAirBlock(super.worldObj, cx, y, cz))
                {
                    Liquid$LiquidData liquid = Liquid.getLiquid(super.worldObj, cx, y, cz);

                    if (liquid == null || liquid.isSource || this.pumpMode && this.canPump(x, y, z))
                    {
                        isBlocking = true;
                    }
                }
            }
        }
        while (!isBlocking);

        TileEntityMiner$MineResult result11 = this.mineBlock(cx, y, cz);

        if (result11 == TileEntityMiner$MineResult.Done)
        {
            this.lastX = cx;
            this.lastZ = cz;
        }

        return result11;
    }

    private TileEntityMiner$MineResult mineBlock(int x, int y, int z)
    {
        int blockId = super.worldObj.getBlockId(x, y, z);
        boolean isAirBlock = true;

        if (blockId != 0 && !Block.blocksList[blockId].isAirBlock(super.worldObj, x, y, z))
        {
            isAirBlock = false;
            Liquid$LiquidData energyPerTick = Liquid.getLiquid(super.worldObj, x, y, z);

            if (energyPerTick != null)
            {
                if (energyPerTick.isSource || this.pumpMode && this.canPump(x, y, z))
                {
                    this.liquidX = x;
                    this.liquidY = y;
                    this.liquidZ = z;
                    this.canProvideLiquid = true;
                    return this.pumpMode ? TileEntityMiner$MineResult.Failed_Temp : TileEntityMiner$MineResult.Failed_Perm;
                }
            }
            else if (!this.canMine(x, y, z))
            {
                return TileEntityMiner$MineResult.Failed_Perm;
            }
        }

        this.canProvideLiquid = false;
        short duration;
        TileEntityMiner$Mode mode1;
        byte energyPerTick1;

        if (isAirBlock)
        {
            mode1 = TileEntityMiner$Mode.MineAir;
            energyPerTick1 = 3;
            duration = 20;
        }
        else if (this.drillSlot.get().itemID == Ic2Items.miningDrill.itemID)
        {
            mode1 = TileEntityMiner$Mode.MineDrill;
            energyPerTick1 = 6;
            duration = 200;
        }
        else
        {
            if (this.drillSlot.get().itemID != Ic2Items.diamondDrill.itemID)
            {
                throw new IllegalStateException("invalid drill: " + this.drillSlot.get());
            }

            mode1 = TileEntityMiner$Mode.MineDDrill;
            energyPerTick1 = 20;
            duration = 50;
        }

        if (this.lastMode != mode1)
        {
            this.lastMode = mode1;
            this.progress = 0;
        }

        if (this.progress < duration)
        {
            if (super.energy >= energyPerTick1)
            {
                super.energy -= energyPerTick1;
                ++this.progress;
                return TileEntityMiner$MineResult.Working;
            }
        }
        else if (isAirBlock || this.harvestBlock(x, y, z, blockId))
        {
            this.progress = 0;
            return TileEntityMiner$MineResult.Done;
        }

        return TileEntityMiner$MineResult.Failed_Temp;
    }

    private boolean harvestBlock(int x, int y, int z, int blockId)
    {
        if (this.drillSlot.get().itemID == Ic2Items.miningDrill.itemID)
        {
            if (!ElectricItem.manager.use(this.drillSlot.get(), 50, (EntityLivingBase)null))
            {
                return false;
            }
        }
        else
        {
            if (this.drillSlot.get().itemID != Ic2Items.diamondDrill.itemID)
            {
                throw new IllegalStateException("invalid drill: " + this.drillSlot.get());
            }

            if (!ElectricItem.manager.use(this.drillSlot.get(), 80, (EntityLivingBase)null))
            {
                return false;
            }
        }

        int energyCost = 2 * (super.yCoord - y);

        if (super.energy >= energyCost)
        {
            super.energy -= energyCost;
            StackUtil.distributeDrop(this, Block.blocksList[blockId].getBlockDropped(super.worldObj, x, y, z, super.worldObj.getBlockMetadata(x, y, z), 0));
            super.worldObj.setBlockToAir(x, y, z);
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean canPump(int x, int y, int z)
    {
        return false;
    }

    public boolean canMine(int x, int y, int z)
    {
        int id = super.worldObj.getBlockId(x, y, z);
        int meta = super.worldObj.getBlockMetadata(x, y, z);

        if (id == 0)
        {
            return true;
        }
        else if (id != Ic2Items.miningPipe.itemID && id != Ic2Items.miningPipeTip.itemID && id != Block.chest.blockID)
        {
            if ((id == Block.waterMoving.blockID || id == Block.waterStill.blockID || id == Block.lavaMoving.blockID || id == Block.lavaStill.blockID) && this.isPumpConnected())
            {
                return true;
            }
            else
            {
                Block block = Block.blocksList[id];

                if (block.getBlockHardness(super.worldObj, x, y, z) < 0.0F)
                {
                    return false;
                }
                else if (block.canCollideCheck(meta, false) && block.blockMaterial.isToolNotRequired())
                {
                    return true;
                }
                else if (id == Block.web.blockID)
                {
                    return true;
                }
                else if (!this.drillSlot.isEmpty())
                {
                    try
                    {
                        HashMap var14 = (HashMap)ReflectionHelper.getPrivateValue(ForgeHooks.class, null, new String[] {"toolClasses"});
                        List tc = (List)var14.get(Integer.valueOf(this.drillSlot.get().itemID));

                        if (tc == null)
                        {
                            return this.drillSlot.get().canHarvestBlock(block);
                        }
                        else
                        {
                            Object[] ta = tc.toArray();
                            String cls = (String)ta[0];
                            int hvl = ((Integer)ta[1]).intValue();
                            HashMap toolHarvestLevels = (HashMap)ReflectionHelper.getPrivateValue(ForgeHooks.class,null, new String[] {"toolHarvestLevels"});
                            Integer bhl = (Integer)toolHarvestLevels.get(Arrays.asList(new Serializable[] {Integer.valueOf(block.blockID), Integer.valueOf(meta), cls}));
                            return bhl == null ? this.drillSlot.get().canHarvestBlock(block) : (bhl.intValue() > hvl ? false : this.drillSlot.get().canHarvestBlock(block));
                        }
                    }
                    catch (Throwable var141)
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
    }

    public boolean isPumpConnected()
    {
        return super.worldObj.getBlockTileEntity(super.xCoord, super.yCoord + 1, super.zCoord) instanceof TileEntityPump && ((TileEntityPump)super.worldObj.getBlockTileEntity(super.xCoord, super.yCoord + 1, super.zCoord)).canHarvest() ? true : (super.worldObj.getBlockTileEntity(super.xCoord, super.yCoord - 1, super.zCoord) instanceof TileEntityPump && ((TileEntityPump)super.worldObj.getBlockTileEntity(super.xCoord, super.yCoord - 1, super.zCoord)).canHarvest() ? true : (super.worldObj.getBlockTileEntity(super.xCoord + 1, super.yCoord, super.zCoord) instanceof TileEntityPump && ((TileEntityPump)super.worldObj.getBlockTileEntity(super.xCoord + 1, super.yCoord, super.zCoord)).canHarvest() ? true : (super.worldObj.getBlockTileEntity(super.xCoord - 1, super.yCoord, super.zCoord) instanceof TileEntityPump && ((TileEntityPump)super.worldObj.getBlockTileEntity(super.xCoord - 1, super.yCoord, super.zCoord)).canHarvest() ? true : (super.worldObj.getBlockTileEntity(super.xCoord, super.yCoord, super.zCoord + 1) instanceof TileEntityPump && ((TileEntityPump)super.worldObj.getBlockTileEntity(super.xCoord, super.yCoord, super.zCoord + 1)).canHarvest() ? true : super.worldObj.getBlockTileEntity(super.xCoord, super.yCoord, super.zCoord - 1) instanceof TileEntityPump && ((TileEntityPump)super.worldObj.getBlockTileEntity(super.xCoord, super.yCoord, super.zCoord - 1)).canHarvest()))));
    }

    public boolean isAnyPumpConnected()
    {
        return super.worldObj.getBlockTileEntity(super.xCoord, super.yCoord + 1, super.zCoord) instanceof TileEntityPump ? true : (super.worldObj.getBlockTileEntity(super.xCoord, super.yCoord - 1, super.zCoord) instanceof TileEntityPump ? true : (super.worldObj.getBlockTileEntity(super.xCoord + 1, super.yCoord, super.zCoord) instanceof TileEntityPump ? true : (super.worldObj.getBlockTileEntity(super.xCoord - 1, super.yCoord, super.zCoord) instanceof TileEntityPump ? true : (super.worldObj.getBlockTileEntity(super.xCoord, super.yCoord, super.zCoord + 1) instanceof TileEntityPump ? true : super.worldObj.getBlockTileEntity(super.xCoord, super.yCoord, super.zCoord - 1) instanceof TileEntityPump))));
    }

    public String getInvName()
    {
        return "Miner";
    }

    public int gaugeEnergyScaled(int i)
    {
        if (super.energy <= 0)
        {
            return 0;
        }
        else
        {
            int r = super.energy * i / 1000;

            if (r > i)
            {
                r = i;
            }

            return r;
        }
    }

    public ContainerBase getGuiContainer(EntityPlayer entityPlayer)
    {
        return new ContainerMiner(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return new GuiMiner(new ContainerMiner(entityPlayer, this));
    }

    public void onGuiClosed(EntityPlayer entityPlayer) {}

    public void onNetworkUpdate(String field)
    {
        if (field.equals("active") && super.prevActive != this.getActive())
        {
            if (this.audioSource == null)
            {
                this.audioSource = IC2.audioManager.createSource(this, PositionSpec.Center, "Machines/MinerOp.ogg", true, false, IC2.audioManager.defaultVolume);
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
