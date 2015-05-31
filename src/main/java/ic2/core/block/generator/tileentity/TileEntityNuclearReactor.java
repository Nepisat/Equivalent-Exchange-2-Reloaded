package ic2.core.block.generator.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.Direction;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.ContainerBase;
import ic2.core.ExplosionIC2;
import ic2.core.IC2;
import ic2.core.IC2DamageSource;
import ic2.core.IHasGui;
import ic2.core.Ic2Items;
import ic2.core.audio.AudioSource;
import ic2.core.audio.PositionSpec;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.generator.container.ContainerNuclearReactor;
import ic2.core.block.generator.gui.GuiNuclearReactor;
import ic2.core.block.invslot.InvSlotReactor;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public abstract class TileEntityNuclearReactor extends TileEntityInventory implements IHasGui, IReactor
{
    public static Random randomizer = new Random();
    private static final Direction[] directions = Direction.values();
    public float output = 0.0F;
    public int updateTicker;
    public int heat = 0;
    public int maxHeat = 10000;
    public float hem = 1.0F;
    public AudioSource audioSourceMain;
    public AudioSource audioSourceGeiger;
    private float lastOutput = 0.0F;
    public final InvSlotReactor reactorSlot;

    public TileEntityNuclearReactor()
    {
        this.updateTicker = randomizer.nextInt(this.getTickRate());
        this.reactorSlot = new InvSlotReactor(this, "reactor", 0, 54);
    }

    public void onUnloaded()
    {
        if (IC2.platform.isRendering())
        {
            IC2.audioManager.removeSources(this);
            this.audioSourceMain = null;
            this.audioSourceGeiger = null;
        }

        super.onUnloaded();
    }

    public String getInvName()
    {
        return "Nuclear Reactor";
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);

        try
        {
            this.heat = nbttagcompound.getInteger("heat");
        }
        catch (Exception var3)
        {
            this.heat = nbttagcompound.getShort("heat");
        }

        this.output = (float)nbttagcompound.getShort("output");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("heat", this.heat);
        nbttagcompound.setShort("output", (short)this.getOutput());
    }

    public boolean enableUpdateEntity()
    {
        return IC2.platform.isSimulating();
    }

    public void updateEntity()
    {
        super.updateEntity();
        this.sendEnergyToChambers(this.getOutput());

        if (this.updateTicker++ % this.getTickRate() == 0)
        {
            if (!super.worldObj.doChunksNearChunkExist(super.xCoord, super.yCoord, super.zCoord, 2))
            {
                this.output = 0.0F;
            }
            else
            {
                this.dropAllUnfittingStuff();
                this.output = 0.0F;
                this.maxHeat = 10000;
                this.hem = 1.0F;
                this.processChambers();

                if (this.calculateHeatEffects())
                {
                    return;
                }

                this.setActive(this.heat >= 1000 || this.output > 0.0F);
                this.onInventoryChanged();
            }

            IC2.network.updateTileEntityField(this, "output");
        }
    }

    public void dropAllUnfittingStuff()
    {
        short size = this.getReactorSize();

        for (int x = 0; x < 9; ++x)
        {
            for (int y = 0; y < 6; ++y)
            {
                ItemStack stack = this.getMatrixCoord(x, y);

                if (stack != null)
                {
                    if (stack.stackSize <= 0)
                    {
                        this.setMatrixCoord(x, y, (ItemStack)null);
                    }
                    else if (x >= size || !isUsefulItem(stack))
                    {
                        this.eject(stack);
                        this.setMatrixCoord(x, y, (ItemStack)null);
                    }
                }
            }
        }
    }

    public static boolean isUsefulItem(ItemStack item)
    {
        if (item == null)
        {
            return false;
        }
        else if (item.getItem() instanceof IReactorComponent)
        {
            return true;
        }
        else
        {
            int id = item.itemID;
            return id == Ic2Items.reEnrichedUraniumCell.itemID || id == Ic2Items.nearDepletedUraniumCell.itemID;
        }
    }

    public void eject(ItemStack drop)
    {
        if (IC2.platform.isSimulating() && drop != null)
        {
            float f = 0.7F;
            double d = (double)(super.worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(super.worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(super.worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(super.worldObj, (double)super.xCoord + d, (double)super.yCoord + d1, (double)super.zCoord + d2, drop);
            entityitem.delayBeforeCanPickup = 10;
            super.worldObj.spawnEntityInWorld(entityitem);
        }
    }

    public boolean calculateHeatEffects()
    {
        if (this.heat >= 4000 && IC2.platform.isSimulating() && IC2.explosionPowerReactorMax > 0.0F)
        {
            float power = (float)this.heat / (float)this.maxHeat;

            if (power >= 1.0F)
            {
                this.explode();
                return true;
            }
            else
            {
                int[] coord;
                int id;
                Material mat;

                if (power >= 0.85F && super.worldObj.rand.nextFloat() <= 0.2F * this.hem)
                {
                    coord = this.getRandCoord(2);

                    if (coord != null)
                    {
                        id = super.worldObj.getBlockId(coord[0], coord[1], coord[2]);

                        if (id == 0)
                        {
                            super.worldObj.setBlock(coord[0], coord[1], coord[2], Block.fire.blockID, 0, 7);
                        }
                        else if (Block.blocksList[id] != null && Block.blocksList[id].getBlockHardness(super.worldObj, coord[0], coord[1], coord[2]) <= -1.0F)
                        {
                            mat = Block.blocksList[id].blockMaterial;

                            if (mat != Material.rock && mat != Material.iron && mat != Material.lava && mat != Material.ground && mat != Material.clay)
                            {
                                super.worldObj.setBlock(coord[0], coord[1], coord[2], Block.fire.blockID, 0, 7);
                            }
                            else
                            {
                                super.worldObj.setBlock(coord[0], coord[1], coord[2], Block.lavaMoving.blockID, 15, 7);
                            }
                        }
                    }
                }

                if (power >= 0.7F)
                {
                    List var5 = super.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox((double)(super.xCoord - 3), (double)(super.yCoord - 3), (double)(super.zCoord - 3), (double)(super.xCoord + 4), (double)(super.yCoord + 4), (double)(super.zCoord + 4)));

                    for (id = 0; id < var5.size(); ++id)
                    {
                        Entity var6 = (Entity)var5.get(id);
                        var6.attackEntityFrom(IC2DamageSource.radiation, (float)((int)((float)super.worldObj.rand.nextInt(4) * this.hem)));
                    }
                }

                if (power >= 0.5F && super.worldObj.rand.nextFloat() <= this.hem)
                {
                    coord = this.getRandCoord(2);

                    if (coord != null)
                    {
                        id = super.worldObj.getBlockId(coord[0], coord[1], coord[2]);

                        if (id > 0 && Block.blocksList[id].blockMaterial == Material.water)
                        {
                            super.worldObj.setBlock(coord[0], coord[1], coord[2], 0, 0, 7);
                        }
                    }
                }

                if (power >= 0.4F && super.worldObj.rand.nextFloat() <= this.hem)
                {
                    coord = this.getRandCoord(2);

                    if (coord != null)
                    {
                        id = super.worldObj.getBlockId(coord[0], coord[1], coord[2]);

                        if (id > 0)
                        {
                            mat = Block.blocksList[id].blockMaterial;

                            if (mat == Material.wood || mat == Material.leaves || mat == Material.cloth)
                            {
                                super.worldObj.setBlock(coord[0], coord[1], coord[2], Block.fire.blockID, 0, 7);
                            }
                        }
                    }
                }

                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public int[] getRandCoord(int radius)
    {
        if (radius <= 0)
        {
            return null;
        }
        else
        {
            int[] c = new int[] {super.xCoord + super.worldObj.rand.nextInt(2 * radius + 1) - radius, super.yCoord + super.worldObj.rand.nextInt(2 * radius + 1) - radius, super.zCoord + super.worldObj.rand.nextInt(2 * radius + 1) - radius};
            return c[0] == super.xCoord && c[1] == super.yCoord && c[2] == super.zCoord ? null : c;
        }
    }

    public void processChambers()
    {
        short size = this.getReactorSize();

        for (int y = 0; y < 6; ++y)
        {
            for (int x = 0; x < size; ++x)
            {
                ItemStack thing = this.getMatrixCoord(x, y);

                if (thing != null && thing.getItem() instanceof IReactorComponent)
                {
                    IReactorComponent comp = (IReactorComponent)thing.getItem();
                    comp.processChamber(this, thing, x, y);
                }
            }
        }
    }

    public boolean produceEnergy()
    {
        return super.worldObj.isBlockIndirectlyGettingPowered(super.xCoord, super.yCoord, super.zCoord) && IC2.energyGeneratorNuclear != 0;
    }

    public ItemStack getMatrixCoord(int x, int y)
    {
        return x >= 0 && x < 9 && y >= 0 && y < 6 ? super.getStackInSlot(x + y * 9) : null;
    }

    public ItemStack getStackInSlot(int i)
    {
        int x = i % 9;
        short size = this.getReactorSize();
        return x >= size ? this.getMatrixCoord(size - 1, i / 9) : super.getStackInSlot(i);
    }

    public void setMatrixCoord(int x, int y, ItemStack stack)
    {
        if (x >= 0 && x < 9 && y >= 0 && y < 6)
        {
            super.setInventorySlotContents(x + y * 9, stack);
        }
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        int x = i % 9;
        short size = this.getReactorSize();

        if (x >= size)
        {
            this.setMatrixCoord(size - 1, i / 9, itemstack);
        }
        else
        {
            super.setInventorySlotContents(i, itemstack);
        }
    }

    public short getReactorSize()
    {
        if (super.worldObj == null)
        {
            return (short)9;
        }
        else
        {
            short rows = 3;
            Direction[] arr$ = directions;
            int len$ = arr$.length;

            for (int i$ = 0; i$ < len$; ++i$)
            {
                Direction direction = arr$[i$];
                TileEntity target = direction.applyToTileEntity(this);

                if (target instanceof TileEntityReactorChamber)
                {
                    ++rows;
                }
            }

            return rows;
        }
    }

    public int sendEnergyToChambers(int send)
    {
        send = this.sendEnergy(send);
        Direction[] arr$ = directions;
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            Direction value = arr$[i$];
            TileEntity te = value.applyToTileEntity(this);

            if (te instanceof TileEntityReactorChamber)
            {
                send = ((TileEntityReactorChamber)te).sendEnergy(send);
            }
        }

        return send;
    }

    public int getTickRate()
    {
        return 20;
    }

    public ContainerBase getGuiContainer(EntityPlayer entityPlayer)
    {
        return new ContainerNuclearReactor(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
    {
        return new GuiNuclearReactor(new ContainerNuclearReactor(entityPlayer, this));
    }

    public void onGuiClosed(EntityPlayer entityPlayer) {}

    public void onNetworkUpdate(String field)
    {
        if (field.equals("output"))
        {
            if (this.output > 0.0F)
            {
                if (this.lastOutput <= 0.0F)
                {
                    if (this.audioSourceMain == null)
                    {
                        this.audioSourceMain = IC2.audioManager.createSource(this, PositionSpec.Center, "Generators/NuclearReactor/NuclearReactorLoop.ogg", true, false, IC2.audioManager.defaultVolume);
                    }

                    if (this.audioSourceMain != null)
                    {
                        this.audioSourceMain.play();
                    }
                }

                if (this.output < 40.0F)
                {
                    if (this.lastOutput <= 0.0F || this.lastOutput >= 40.0F)
                    {
                        if (this.audioSourceGeiger != null)
                        {
                            this.audioSourceGeiger.remove();
                        }

                        this.audioSourceGeiger = IC2.audioManager.createSource(this, PositionSpec.Center, "Generators/NuclearReactor/GeigerLowEU.ogg", true, false, IC2.audioManager.defaultVolume);

                        if (this.audioSourceGeiger != null)
                        {
                            this.audioSourceGeiger.play();
                        }
                    }
                }
                else if (this.output < 80.0F)
                {
                    if (this.lastOutput < 40.0F || this.lastOutput >= 80.0F)
                    {
                        if (this.audioSourceGeiger != null)
                        {
                            this.audioSourceGeiger.remove();
                        }

                        this.audioSourceGeiger = IC2.audioManager.createSource(this, PositionSpec.Center, "Generators/NuclearReactor/GeigerMedEU.ogg", true, false, IC2.audioManager.defaultVolume);

                        if (this.audioSourceGeiger != null)
                        {
                            this.audioSourceGeiger.play();
                        }
                    }
                }
                else if (this.output >= 80.0F && this.lastOutput < 80.0F)
                {
                    if (this.audioSourceGeiger != null)
                    {
                        this.audioSourceGeiger.remove();
                    }

                    this.audioSourceGeiger = IC2.audioManager.createSource(this, PositionSpec.Center, "Generators/NuclearReactor/GeigerHighEU.ogg", true, false, IC2.audioManager.defaultVolume);

                    if (this.audioSourceGeiger != null)
                    {
                        this.audioSourceGeiger.play();
                    }
                }
            }
            else if (this.lastOutput > 0.0F)
            {
                if (this.audioSourceMain != null)
                {
                    this.audioSourceMain.stop();
                }

                if (this.audioSourceGeiger != null)
                {
                    this.audioSourceGeiger.stop();
                }
            }

            this.lastOutput = this.output;
        }

        super.onNetworkUpdate(field);
    }

    public float getWrenchDropRate()
    {
        return 0.8F;
    }

    public ChunkCoordinates getPosition()
    {
        return new ChunkCoordinates(super.xCoord, super.yCoord, super.zCoord);
    }

    public World getWorld()
    {
        return super.worldObj;
    }

    public int getHeat()
    {
        return this.heat;
    }

    public void setHeat(int heat)
    {
        this.heat = heat;
    }

    public int addHeat(int amount)
    {
        this.heat += amount;
        return this.heat;
    }

    public ItemStack getItemAt(int x, int y)
    {
        return this.getMatrixCoord(x, y);
    }

    public void setItemAt(int x, int y, ItemStack item)
    {
        this.setMatrixCoord(x, y, item);
    }

    public void explode()
    {
        float boomPower = 10.0F;
        float boomMod = 1.0F;
        int len$;

        for (int var8 = 0; var8 < 6; ++var8)
        {
            for (len$ = 0; len$ < this.getReactorSize(); ++len$)
            {
                ItemStack var9 = this.getMatrixCoord(len$, var8);

                if (var9 != null && var9.getItem() instanceof IReactorComponent)
                {
                    float var11 = ((IReactorComponent)var9.getItem()).influenceExplosion(this, var9);

                    if (var11 > 0.0F && var11 < 1.0F)
                    {
                        boomMod *= var11;
                    }
                    else
                    {
                        boomPower += var11;
                    }
                }

                this.setMatrixCoord(len$, var8, (ItemStack)null);
            }
        }

        boomPower *= this.hem * boomMod;
        IC2.log.log(Level.INFO, "Nuclear Reactor at " + super.worldObj.provider.dimensionId + ":(" + super.xCoord + "," + super.yCoord + "," + super.zCoord + ") melted (explosion power " + boomPower + ")");

        if (boomPower > IC2.explosionPowerReactorMax)
        {
            boomPower = IC2.explosionPowerReactorMax;
        }

        Direction[] var81 = directions;
        len$ = var81.length;

        for (int var91 = 0; var91 < len$; ++var91)
        {
            Direction var111 = var81[var91];
            TileEntity target = var111.applyToTileEntity(this);

            if (target instanceof TileEntityReactorChamber)
            {
                super.worldObj.setBlock(target.xCoord, target.yCoord, target.zCoord, 0, 0, 7);
            }
        }

        super.worldObj.setBlock(super.xCoord, super.yCoord, super.zCoord, 0, 0, 7);
        ExplosionIC2 var10 = new ExplosionIC2(super.worldObj, (Entity)null, (double)super.xCoord, (double)super.yCoord, (double)super.zCoord, boomPower, 0.01F, 1.5F, true);
        var10.doExplosion();
    }

    public int getMaxHeat()
    {
        return this.maxHeat;
    }

    public void setMaxHeat(int newMaxHeat)
    {
        this.maxHeat = newMaxHeat;
    }

    public float getHeatEffectModifier()
    {
        return this.hem;
    }

    public void setHeatEffectModifier(float newHEM)
    {
        this.hem = newHEM;
    }

    public int getOutput()
    {
        return (int)Math.floor((double)this.output);
    }

    public float addOutput(float energy)
    {
        return this.output += energy;
    }

    public int addOutput(int energy)
    {
        return (int)Math.floor((double)this.addOutput((float)energy));
    }

    @Deprecated
    public int getPulsePower()
    {
        return 1;
    }

    public abstract int sendEnergy(int var1);
}
