package ic2.core;

import ic2.api.Direction;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.core.EnergyNet$1;
import ic2.core.EnergyNet$EnergyBlockLink;
import ic2.core.EnergyNet$EnergyPath;
import ic2.core.EnergyNet$EnergyTarget;
import ic2.core.EnergyNet$EventHandler;
import ic2.core.EnergyNet$PostPonedAddCallback;
import ic2.core.EnergyNet$Tile;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public final class EnergyNet
{
    public static final double minConductionLoss = 1.0E-4D;
    private static final Direction[] directions = Direction.values();
    private final Map<EnergyNet$Tile, List<EnergyNet$EnergyPath>> energySourceToEnergyPathMap = new HashMap();
    private final Map<EntityLivingBase, Integer> entityLivingToShockEnergyMap = new HashMap();
    private final Map<ChunkCoordinates, EnergyNet$Tile> registeredTiles = new HashMap();
    private static int apiDemandsErrorCooldown = 0;
    private static int apiEmitErrorCooldown = 0;

    public static void initialize()
    {
        new EnergyNet$EventHandler();
    }

    public static EnergyNet getForWorld(World world)
    {
        WorldData worldData = WorldData.get(world);
        return worldData.energyNet;
    }

    public static void onTick(World world)
    {
        IC2.platform.profilerStartSection("Shocking");
        EnergyNet energyNet = getForWorld(world);
        Iterator i$ = energyNet.entityLivingToShockEnergyMap.entrySet().iterator();

        while (i$.hasNext())
        {
            Entry entry = (Entry)i$.next();
            EntityLivingBase target = (EntityLivingBase)entry.getKey();
            int damage = (((Integer)entry.getValue()).intValue() + 63) / 64;

            if (target.isEntityAlive())
            {
                target.attackEntityFrom(IC2DamageSource.electricity, (float)damage);
            }
        }

        energyNet.entityLivingToShockEnergyMap.clear();

        if (world.provider.dimensionId == 0)
        {
            if (apiDemandsErrorCooldown > 0)
            {
                --apiDemandsErrorCooldown;
            }

            if (apiEmitErrorCooldown > 0)
            {
                --apiEmitErrorCooldown;
            }
        }

        IC2.platform.profilerEndSection();
    }

    public void addTileEntity(TileEntity te)
    {
        if (!IC2.platform.isSimulating())
        {
            IC2.log.warning("EnergyNet.addTileEntity: called for " + te + " client-side, aborting");
        }
        else if (!(te instanceof IEnergyTile))
        {
            IC2.log.warning("EnergyNet.addTileEntity: " + te + " doesn\'t implement IEnergyTile, aborting");
        }
        else if (te.isInvalid())
        {
            IC2.log.warning("EnergyNet.addTileEntity: " + te + " is invalid (TileEntity.isInvalid()), aborting");
        }
        else
        {
            ChunkCoordinates coords = new ChunkCoordinates(te.xCoord, te.yCoord, te.zCoord);

            if (this.registeredTiles.containsKey(coords))
            {
                IC2.log.warning("EnergyNet.addTileEntity: " + te + " is already added, aborting");
            }
            else if (!te.worldObj.blockExists(te.xCoord, te.yCoord, te.zCoord))
            {
                IC2.log.warning("EnergyNet.addTileEntity: " + te + " was added too early, postponing");
                IC2.addSingleTickCallback(te.worldObj, new EnergyNet$PostPonedAddCallback(te));
            }
            else
            {
                EnergyNet$Tile tile = new EnergyNet$Tile(this, te);
                this.registeredTiles.put(coords, tile);

                if (te instanceof IEnergyAcceptor)
                {
                    List arr$ = this.discover(tile, true, Integer.MAX_VALUE);
                    Iterator len$ = arr$.iterator();

                    while (len$.hasNext())
                    {
                        EnergyNet$EnergyPath i$ = (EnergyNet$EnergyPath)len$.next();
                        EnergyNet$Tile dir = i$.target;

                        if (this.energySourceToEnergyPathMap.containsKey(dir) && (double)((IEnergySource)dir.entity).getMaxEnergyOutput() > i$.loss)
                        {
                            this.energySourceToEnergyPathMap.remove(dir);
                        }
                    }
                }

                if (te instanceof IEnergySource)
                {
                    ;
                }

                ForgeDirection[] var11 = ForgeDirection.VALID_DIRECTIONS;
                int var12 = var11.length;

                for (int var13 = 0; var13 < var12; ++var13)
                {
                    ForgeDirection var14 = var11[var13];
                    int x = te.xCoord + var14.offsetX;
                    int y = te.yCoord + var14.offsetY;
                    int z = te.zCoord + var14.offsetZ;

                    if (te.worldObj.blockExists(x, y, z))
                    {
                        te.worldObj.notifyBlockOfNeighborChange(x, y, z, te.getBlockType().blockID);
                    }
                }
            }
        }
    }

    public void removeTileEntity(TileEntity te)
    {
        if (!IC2.platform.isSimulating())
        {
            IC2.log.warning("EnergyNet.removeTileEntity: called for " + te + " client-side, aborting");
        }
        else if (!(te instanceof IEnergyTile))
        {
            IC2.log.warning("EnergyNet.removeTileEntity: " + te + " doesn\'t implement IEnergyTile, aborting");
        }
        else
        {
            ChunkCoordinates coords = new ChunkCoordinates(te.xCoord, te.yCoord, te.zCoord);
            EnergyNet$Tile tile = (EnergyNet$Tile)this.registeredTiles.get(coords);

            if (tile == null)
            {
                IC2.log.warning("EnergyNet.removeTileEntity: " + te + " is already removed, aborting");
            }
            else
            {
                if (te instanceof IEnergyAcceptor)
                {
                    List block = this.discover(tile, true, Integer.MAX_VALUE);
                    Iterator arr$ = block.iterator();

                    while (arr$.hasNext())
                    {
                        EnergyNet$EnergyPath len$ = (EnergyNet$EnergyPath)arr$.next();
                        EnergyNet$Tile i$ = len$.target;

                        if (this.energySourceToEnergyPathMap.containsKey(i$) && (double)((IEnergySource)i$.entity).getMaxEnergyOutput() > len$.loss)
                        {
                            if (te instanceof IEnergyConductor)
                            {
                                this.energySourceToEnergyPathMap.remove(i$);
                            }
                            else
                            {
                                Iterator dir = ((List)this.energySourceToEnergyPathMap.get(i$)).iterator();

                                while (dir.hasNext())
                                {
                                    if (((EnergyNet$EnergyPath)dir.next()).target == tile)
                                    {
                                        dir.remove();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                if (te instanceof IEnergySource)
                {
                    this.energySourceToEnergyPathMap.remove(tile);
                }

                tile.destroy();
                this.registeredTiles.remove(coords);

                if (te.worldObj.blockExists(te.xCoord, te.yCoord, te.zCoord))
                {
                    Block var12 = te.getBlockType();
                    ForgeDirection[] var13 = ForgeDirection.VALID_DIRECTIONS;
                    int var14 = var13.length;

                    for (int var15 = 0; var15 < var14; ++var15)
                    {
                        ForgeDirection var16 = var13[var15];
                        int x = te.xCoord + var16.offsetX;
                        int y = te.yCoord + var16.offsetY;
                        int z = te.zCoord + var16.offsetZ;

                        if (te.worldObj.blockExists(x, y, z))
                        {
                            te.worldObj.notifyBlockOfNeighborChange(x, y, z, var12 != null ? var12.blockID : 0);
                        }
                    }
                }
            }
        }
    }

    public int emitEnergyFrom(IEnergySource energySource, int amount)
    {
        if (!IC2.platform.isSimulating())
        {
            if (apiEmitErrorCooldown == 0)
            {
                apiEmitErrorCooldown = 600;
                IC2.log.warning("EnergyNet.emitEnergyFrom: called for " + energySource + " client-side, aborting");
            }

            return amount;
        }
        else if (!(energySource instanceof TileEntity))
        {
            if (apiEmitErrorCooldown == 0)
            {
                apiEmitErrorCooldown = 600;
                IC2.log.warning("EnergyNet.emitEnergyFrom: " + energySource + " is no tile entity, aborting");
            }

            return amount;
        }
        else
        {
            TileEntity srcTe = (TileEntity)energySource;

            if (srcTe.isInvalid())
            {
                IC2.log.warning("EnergyNet.emitEnergyFrom: " + srcTe + " is invalid (TileEntity.isInvalid()), aborting");
                return amount;
            }
            else
            {
                ChunkCoordinates coords = new ChunkCoordinates(srcTe.xCoord, srcTe.yCoord, srcTe.zCoord);
                EnergyNet$Tile tile = (EnergyNet$Tile)this.registeredTiles.get(coords);

                if (tile == null)
                {
                    if (apiEmitErrorCooldown == 0)
                    {
                        apiEmitErrorCooldown = 600;
                        IC2.log.warning("EnergyNet.emitEnergyFrom: " + energySource + " is not added to the enet, aborting");
                    }

                    return amount;
                }
                else
                {
                    if (!this.energySourceToEnergyPathMap.containsKey(tile))
                    {
                        this.energySourceToEnergyPathMap.put(tile, this.discover(tile, false, energySource.getMaxEnergyOutput()));
                    }

                    Vector activeEnergyPaths = new Vector();
                    double totalInvLoss = 0.0D;
                    Iterator suppliedEnergyPaths = ((List)this.energySourceToEnergyPathMap.get(tile)).iterator();
                    EnergyNet$EnergyPath i$;

                    while (suppliedEnergyPaths.hasNext())
                    {
                        i$ = (EnergyNet$EnergyPath)suppliedEnergyPaths.next();
                        IEnergySink entry = (IEnergySink)i$.target.entity;

                        if (entry.demandsEnergy() > 0 && i$.loss < (double)amount)
                        {
                            totalInvLoss += 1.0D / i$.loss;
                            activeEnergyPaths.add(i$);
                        }
                    }

                    Collections.shuffle(activeEnergyPaths);

                    for (int var23 = activeEnergyPaths.size() - amount; var23 > 0; --var23)
                    {
                        i$ = (EnergyNet$EnergyPath)activeEnergyPaths.remove(activeEnergyPaths.size() - 1);
                        totalInvLoss -= 1.0D / i$.loss;
                    }

                    Iterator i$1;
                    EnergyNet$Tile conductor;
                    HashMap var24;
                    int var27;

                    for (var24 = new HashMap(); !activeEnergyPaths.isEmpty() && amount > 0; amount -= var27)
                    {
                        var27 = 0;
                        double var25 = 0.0D;
                        Vector energyInjected = activeEnergyPaths;
                        activeEnergyPaths = new Vector();
                        activeEnergyPaths.iterator();
                        i$1 = energyInjected.iterator();

                        while (i$1.hasNext())
                        {
                            EnergyNet$EnergyPath condTile = (EnergyNet$EnergyPath)i$1.next();
                            conductor = condTile.target;
                            IEnergySink conductor1 = (IEnergySink)conductor.entity;
                            int i$2 = (int)Math.floor((double)Math.round((double)amount / totalInvLoss / condTile.loss * 100000.0D) / 100000.0D);
                            int condTile1 = (int)Math.floor(condTile.loss);

                            if (i$2 > condTile1)
                            {
                                int te = conductor1.injectEnergy(condTile.targetDirection, i$2 - condTile1);

                                if (te == 0 && conductor1.demandsEnergy() > 0)
                                {
                                    activeEnergyPaths.add(condTile);
                                    var25 += 1.0D / condTile.loss;
                                }
                                else if (te >= i$2 - condTile1)
                                {
                                    te = i$2 - condTile1;

                                    if (apiDemandsErrorCooldown == 0)
                                    {
                                        apiDemandsErrorCooldown = 600;
                                        TileEntity conductor2 = conductor.entity;
                                        String shockEnergy = (conductor2.worldObj == null ? "unknown" : Integer.valueOf(conductor2.worldObj.provider.dimensionId)) + ":" + conductor2.xCoord + "," + conductor2.yCoord + "," + conductor2.zCoord;
                                        IC2.log.warning("API ERROR: " + conductor + " (" + shockEnergy + ") didn\'t implement demandsEnergy() properly, no energy from injectEnergy accepted (" + te + ") although demandsEnergy() requested " + (i$2 - condTile1) + ".");
                                    }
                                }

                                var27 += i$2 - te;
                                int var42 = i$2 - condTile1 - te;

                                if (!var24.containsKey(condTile))
                                {
                                    var24.put(condTile, Integer.valueOf(var42));
                                }
                                else
                                {
                                    var24.put(condTile, Integer.valueOf(var42 + ((Integer)var24.get(condTile)).intValue()));
                                }
                            }
                            else
                            {
                                activeEnergyPaths.add(condTile);
                                var25 += 1.0D / condTile.loss;
                            }
                        }

                        if (var27 == 0 && !activeEnergyPaths.isEmpty())
                        {
                            EnergyNet$EnergyPath var31 = (EnergyNet$EnergyPath)activeEnergyPaths.remove(activeEnergyPaths.size() - 1);
                            var25 -= 1.0D / var31.loss;
                        }

                        totalInvLoss = var25;
                    }

                    Iterator var26 = var24.entrySet().iterator();

                    while (var26.hasNext())
                    {
                        Entry var28 = (Entry)var26.next();
                        EnergyNet$EnergyPath energyPath = (EnergyNet$EnergyPath)var28.getKey();
                        int var29 = ((Integer)var28.getValue()).intValue();
                        energyPath.totalEnergyConducted += (long)var29;

                        if (var29 > energyPath.minInsulationEnergyAbsorption)
                        {
                            List var32 = srcTe.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox((double)(energyPath.minX - 1), (double)(energyPath.minY - 1), (double)(energyPath.minZ - 1), (double)(energyPath.maxX + 2), (double)(energyPath.maxY + 2), (double)(energyPath.maxZ + 2)));
                            Iterator var30 = var32.iterator();

                            while (var30.hasNext())
                            {
                                EntityLivingBase var33 = (EntityLivingBase)var30.next();
                                int var35 = 0;
                                Iterator var39 = energyPath.conductors.iterator();

                                while (true)
                                {
                                    if (var39.hasNext())
                                    {
                                        EnergyNet$Tile var37 = (EnergyNet$Tile)var39.next();
                                        TileEntity var41 = var37.entity;
                                        IEnergyConductor var43 = (IEnergyConductor)var41;

                                        if (!var33.boundingBox.intersectsWith(AxisAlignedBB.getBoundingBox((double)(var41.xCoord - 1), (double)(var41.yCoord - 1), (double)(var41.zCoord - 1), (double)(var41.xCoord + 2), (double)(var41.yCoord + 2), (double)(var41.zCoord + 2))))
                                        {
                                            continue;
                                        }

                                        int var40 = var29 - var43.getInsulationEnergyAbsorption();

                                        if (var40 > var35)
                                        {
                                            var35 = var40;
                                        }

                                        if (var43.getInsulationEnergyAbsorption() != energyPath.minInsulationEnergyAbsorption)
                                        {
                                            continue;
                                        }
                                    }

                                    if (this.entityLivingToShockEnergyMap.containsKey(var33))
                                    {
                                        this.entityLivingToShockEnergyMap.put(var33, Integer.valueOf(((Integer)this.entityLivingToShockEnergyMap.get(var33)).intValue() + var35));
                                    }
                                    else
                                    {
                                        this.entityLivingToShockEnergyMap.put(var33, Integer.valueOf(var35));
                                    }

                                    break;
                                }
                            }

                            if (var29 >= energyPath.minInsulationBreakdownEnergy)
                            {
                                var30 = energyPath.conductors.iterator();

                                while (var30.hasNext())
                                {
                                    conductor = (EnergyNet$Tile)var30.next();
                                    IEnergyConductor var38 = (IEnergyConductor)conductor.entity;

                                    if (var29 >= var38.getInsulationBreakdownEnergy())
                                    {
                                        var38.removeInsulation();

                                        if (var38.getInsulationEnergyAbsorption() < energyPath.minInsulationEnergyAbsorption)
                                        {
                                            energyPath.minInsulationEnergyAbsorption = var38.getInsulationEnergyAbsorption();
                                        }
                                    }
                                }
                            }
                        }

                        if (var29 >= energyPath.minConductorBreakdownEnergy)
                        {
                            i$1 = energyPath.conductors.iterator();

                            while (i$1.hasNext())
                            {
                                EnergyNet$Tile var34 = (EnergyNet$Tile)i$1.next();
                                IEnergyConductor var36 = (IEnergyConductor)var34.entity;

                                if (var29 >= var36.getConductorBreakdownEnergy())
                                {
                                    var36.removeConductor();
                                }
                            }
                        }
                    }

                    return amount;
                }
            }
        }
    }

    @Deprecated
    public long getTotalEnergyConducted(TileEntity tileEntity)
    {
        EnergyNet$Tile tile = (EnergyNet$Tile)this.registeredTiles.get(new ChunkCoordinates(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));

        if (tile == null)
        {
            IC2.log.warning("EnergyNet.getTotalEnergyConducted: " + tileEntity + " is not added to the enet, aborting");
            return 0L;
        }
        else
        {
            long ret = 0L;

            if (tileEntity instanceof IEnergyConductor || tileEntity instanceof IEnergySink)
            {
                List i$ = this.discover(tile, true, Integer.MAX_VALUE);
                Iterator energyPath = i$.iterator();

                while (energyPath.hasNext())
                {
                    EnergyNet$EnergyPath reverseEnergyPath = (EnergyNet$EnergyPath)energyPath.next();
                    EnergyNet$Tile srcTile = reverseEnergyPath.target;

                    if (this.energySourceToEnergyPathMap.containsKey(srcTile) && (double)((IEnergySource)srcTile.entity).getMaxEnergyOutput() > reverseEnergyPath.loss)
                    {
                        Iterator i$1 = ((List)this.energySourceToEnergyPathMap.get(srcTile)).iterator();

                        while (i$1.hasNext())
                        {
                            EnergyNet$EnergyPath energyPath1 = (EnergyNet$EnergyPath)i$1.next();

                            if (tileEntity instanceof IEnergySink && energyPath1.target == tile || tileEntity instanceof IEnergyConductor && energyPath1.conductors.contains(tile))
                            {
                                ret += energyPath1.totalEnergyConducted;
                            }
                        }
                    }
                }
            }

            EnergyNet$EnergyPath energyPath2;

            if (tileEntity instanceof IEnergySource && this.energySourceToEnergyPathMap.containsKey(tile))
            {
                for (Iterator i$2 = ((List)this.energySourceToEnergyPathMap.get(tile)).iterator(); i$2.hasNext(); ret += energyPath2.totalEnergyConducted)
                {
                    energyPath2 = (EnergyNet$EnergyPath)i$2.next();
                }
            }

            return ret;
        }
    }

    public long getTotalEnergyEmitted(TileEntity tileEntity)
    {
        EnergyNet$Tile tile = (EnergyNet$Tile)this.registeredTiles.get(new ChunkCoordinates(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));

        if (tile == null)
        {
            IC2.log.warning("EnergyNet.getTotalEnergyEmitted: " + tileEntity + " is not added to the enet, aborting");
            return 0L;
        }
        else
        {
            long ret = 0L;

            if (tileEntity instanceof IEnergyConductor)
            {
                List i$ = this.discover(tile, true, Integer.MAX_VALUE);
                Iterator energyPath = i$.iterator();

                while (energyPath.hasNext())
                {
                    EnergyNet$EnergyPath reverseEnergyPath = (EnergyNet$EnergyPath)energyPath.next();
                    EnergyNet$Tile srcTile = reverseEnergyPath.target;

                    if (this.energySourceToEnergyPathMap.containsKey(srcTile) && (double)((IEnergySource)srcTile.entity).getMaxEnergyOutput() > reverseEnergyPath.loss)
                    {
                        Iterator i$1 = ((List)this.energySourceToEnergyPathMap.get(srcTile)).iterator();

                        while (i$1.hasNext())
                        {
                            EnergyNet$EnergyPath energyPath1 = (EnergyNet$EnergyPath)i$1.next();

                            if (tileEntity instanceof IEnergyConductor && energyPath1.conductors.contains(tile))
                            {
                                ret += energyPath1.totalEnergyConducted;
                            }
                        }
                    }
                }
            }

            EnergyNet$EnergyPath energyPath2;

            if (tileEntity instanceof IEnergySource && this.energySourceToEnergyPathMap.containsKey(tile))
            {
                for (Iterator i$2 = ((List)this.energySourceToEnergyPathMap.get(tile)).iterator(); i$2.hasNext(); ret += energyPath2.totalEnergyConducted)
                {
                    energyPath2 = (EnergyNet$EnergyPath)i$2.next();
                }
            }

            return ret;
        }
    }

    public long getTotalEnergySunken(TileEntity tileEntity)
    {
        EnergyNet$Tile tile = (EnergyNet$Tile)this.registeredTiles.get(new ChunkCoordinates(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));

        if (tile == null)
        {
            IC2.log.warning("EnergyNet.getTotalEnergySunken: " + tileEntity + " is not added to the enet, aborting");
            return 0L;
        }
        else
        {
            long ret = 0L;

            if (tileEntity instanceof IEnergyConductor || tileEntity instanceof IEnergySink)
            {
                List reverseEnergyPaths = this.discover(tile, true, Integer.MAX_VALUE);
                Iterator i$ = reverseEnergyPaths.iterator();

                while (i$.hasNext())
                {
                    EnergyNet$EnergyPath reverseEnergyPath = (EnergyNet$EnergyPath)i$.next();
                    EnergyNet$Tile srcTile = reverseEnergyPath.target;

                    if (this.energySourceToEnergyPathMap.containsKey(srcTile) && (double)((IEnergySource)srcTile.entity).getMaxEnergyOutput() > reverseEnergyPath.loss)
                    {
                        Iterator i$1 = ((List)this.energySourceToEnergyPathMap.get(srcTile)).iterator();

                        while (i$1.hasNext())
                        {
                            EnergyNet$EnergyPath energyPath = (EnergyNet$EnergyPath)i$1.next();

                            if (tileEntity instanceof IEnergySink && energyPath.target == tile || tileEntity instanceof IEnergyConductor && energyPath.conductors.contains(tile))
                            {
                                ret += energyPath.totalEnergyConducted;
                            }
                        }
                    }
                }
            }

            return ret;
        }
    }

    public TileEntity getTileEntity(int x, int y, int z)
    {
        EnergyNet$Tile ret = (EnergyNet$Tile)this.registeredTiles.get(new ChunkCoordinates(x, y, z));
        return ret == null ? null : ret.entity;
    }

    public TileEntity getNeighbor(TileEntity te, Direction dir)
    {
        switch (EnergyNet$1.$SwitchMap$ic2$api$Direction[dir.ordinal()])
        {
            case 1:
                return this.getTileEntity(te.xCoord - 1, te.yCoord, te.zCoord);

            case 2:
                return this.getTileEntity(te.xCoord + 1, te.yCoord, te.zCoord);

            case 3:
                return this.getTileEntity(te.xCoord, te.yCoord - 1, te.zCoord);

            case 4:
                return this.getTileEntity(te.xCoord, te.yCoord + 1, te.zCoord);

            case 5:
                return this.getTileEntity(te.xCoord, te.yCoord, te.zCoord - 1);

            case 6:
                return this.getTileEntity(te.xCoord, te.yCoord, te.zCoord + 1);

            default:
                return null;
        }
    }

    public EnergyNet$Tile getNeighbor(EnergyNet$Tile te, Direction dir)
    {
        return te.neighbors[dir.ordinal()];
    }

    private List<EnergyNet$EnergyPath> discover(EnergyNet$Tile emitter, boolean reverse, int lossLimit)
    {
        HashMap reachedTileEntities = new HashMap();
        LinkedList tileEntitiesToCheck = new LinkedList();
        tileEntitiesToCheck.add(emitter);

        while (!tileEntitiesToCheck.isEmpty())
        {
            EnergyNet$Tile energyPaths = (EnergyNet$Tile)tileEntitiesToCheck.remove();

            if (!energyPaths.entity.isInvalid())
            {
                double i$ = 0.0D;

                if (energyPaths != emitter)
                {
                    i$ = ((EnergyNet$EnergyBlockLink)reachedTileEntities.get(energyPaths)).loss;
                }

                List tile = this.getValidReceivers(energyPaths, reverse);
                Iterator energyBlockLink = tile.iterator();

                while (energyBlockLink.hasNext())
                {
                    EnergyNet$EnergyTarget energyPath = (EnergyNet$EnergyTarget)energyBlockLink.next();

                    if (energyPath.tile != emitter)
                    {
                        double te = 0.0D;

                        if (energyPath.tile.entity instanceof IEnergyConductor)
                        {
                            te = ((IEnergyConductor)energyPath.tile.entity).getConductionLoss();

                            if (te < 1.0E-4D)
                            {
                                te = 1.0E-4D;
                            }

                            if (i$ + te >= (double)lossLimit)
                            {
                                continue;
                            }
                        }

                        if (!reachedTileEntities.containsKey(energyPath.tile) || ((EnergyNet$EnergyBlockLink)reachedTileEntities.get(energyPath.tile)).loss > i$ + te)
                        {
                            reachedTileEntities.put(energyPath.tile, new EnergyNet$EnergyBlockLink(energyPath.direction, i$ + te));

                            if (energyPath.tile.entity instanceof IEnergyConductor)
                            {
                                tileEntitiesToCheck.remove(energyPath.tile);
                                tileEntitiesToCheck.add(energyPath.tile);
                            }
                        }
                    }
                }
            }
        }

        LinkedList energyPaths1 = new LinkedList();
        Iterator i$1 = reachedTileEntities.entrySet().iterator();
        label109:

        while (i$1.hasNext())
        {
            Entry entry = (Entry)i$1.next();
            EnergyNet$Tile tile1 = (EnergyNet$Tile)entry.getKey();

            if (!reverse && tile1.entity instanceof IEnergySink || reverse && tile1.entity instanceof IEnergySource)
            {
                EnergyNet$EnergyBlockLink energyBlockLink1 = (EnergyNet$EnergyBlockLink)entry.getValue();
                EnergyNet$EnergyPath energyPath1 = new EnergyNet$EnergyPath();

                if (energyBlockLink1.loss > 0.1D)
                {
                    energyPath1.loss = energyBlockLink1.loss;
                }
                else
                {
                    energyPath1.loss = 0.1D;
                }

                energyPath1.target = tile1;
                energyPath1.targetDirection = energyBlockLink1.direction;

                if (!reverse && emitter.entity instanceof IEnergySource)
                {
                    while (true)
                    {
                        tile1 = this.getNeighbor(tile1, energyBlockLink1.direction);

                        if (tile1 == emitter)
                        {
                            break;
                        }

                        if (!(tile1.entity instanceof IEnergyConductor))
                        {
                            IC2.log.warning("EnergyNet: EnergyBlockLink corrupted (" + energyPath1.target.entity + " [" + energyPath1.target.entity.xCoord + " " + energyPath1.target.entity.yCoord + " " + energyPath1.target.entity.zCoord + "] -> " + tile1.entity + " [" + tile1.entity.xCoord + " " + tile1.entity.yCoord + " " + tile1.entity.zCoord + "] -> " + emitter.entity + " [" + emitter.entity.xCoord + " " + emitter.entity.yCoord + " " + emitter.entity.zCoord + "])");
                            continue label109;
                        }

                        TileEntity te1 = tile1.entity;
                        IEnergyConductor energyConductor = (IEnergyConductor)te1;

                        if (te1.xCoord < energyPath1.minX)
                        {
                            energyPath1.minX = te1.xCoord;
                        }

                        if (te1.yCoord < energyPath1.minY)
                        {
                            energyPath1.minY = te1.yCoord;
                        }

                        if (te1.zCoord < energyPath1.minZ)
                        {
                            energyPath1.minZ = te1.zCoord;
                        }

                        if (te1.xCoord > energyPath1.maxX)
                        {
                            energyPath1.maxX = te1.xCoord;
                        }

                        if (te1.yCoord > energyPath1.maxY)
                        {
                            energyPath1.maxY = te1.yCoord;
                        }

                        if (te1.zCoord > energyPath1.maxZ)
                        {
                            energyPath1.maxZ = te1.zCoord;
                        }

                        energyPath1.conductors.add(tile1);

                        if (energyConductor.getInsulationEnergyAbsorption() < energyPath1.minInsulationEnergyAbsorption)
                        {
                            energyPath1.minInsulationEnergyAbsorption = energyConductor.getInsulationEnergyAbsorption();
                        }

                        if (energyConductor.getInsulationBreakdownEnergy() < energyPath1.minInsulationBreakdownEnergy)
                        {
                            energyPath1.minInsulationBreakdownEnergy = energyConductor.getInsulationBreakdownEnergy();
                        }

                        if (energyConductor.getConductorBreakdownEnergy() < energyPath1.minConductorBreakdownEnergy)
                        {
                            energyPath1.minConductorBreakdownEnergy = energyConductor.getConductorBreakdownEnergy();
                        }

                        energyBlockLink1 = (EnergyNet$EnergyBlockLink)reachedTileEntities.get(tile1);

                        if (energyBlockLink1 == null)
                        {
                            TileEntity srcTe = emitter.entity;
                            TileEntity dstTe = energyPath1.target.entity;
                            IC2.platform.displayError("An energy network pathfinding entry is corrupted.\nThis could happen due to incorrect Minecraft behavior or a bug.\n\n(Technical information: energyBlockLink, tile entities below)\nE: " + srcTe + " (" + srcTe.xCoord + "," + srcTe.yCoord + "," + srcTe.zCoord + ")\n" + "C: " + te1 + " (" + te1.xCoord + "," + te1.yCoord + "," + te1.zCoord + ")\n" + "R: " + dstTe + " (" + dstTe.xCoord + "," + dstTe.yCoord + "," + dstTe.zCoord + ")");
                        }
                    }
                }

                energyPaths1.add(energyPath1);
            }
        }

        return energyPaths1;
    }

    private List<EnergyNet$EnergyTarget> getValidReceivers(EnergyNet$Tile emitter, boolean reverse)
    {
        LinkedList validReceivers = new LinkedList();
        Direction[] arr$ = directions;
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$)
        {
            Direction direction = arr$[i$];
            EnergyNet$Tile target = this.getNeighbor(emitter, direction);

            if (target != null)
            {
                Direction inverseDirection = direction.getInverse();

                if ((!reverse && emitter.entity instanceof IEnergyEmitter && ((IEnergyEmitter)emitter.entity).emitsEnergyTo(target.entity, direction) || reverse && emitter.entity instanceof IEnergyAcceptor && ((IEnergyAcceptor)emitter.entity).acceptsEnergyFrom(target.entity, direction)) && (!reverse && target.entity instanceof IEnergyAcceptor && ((IEnergyAcceptor)target.entity).acceptsEnergyFrom(emitter.entity, inverseDirection) || reverse && target.entity instanceof IEnergyEmitter && ((IEnergyEmitter)target.entity).emitsEnergyTo(emitter.entity, inverseDirection)))
                {
                    validReceivers.add(new EnergyNet$EnergyTarget(target, inverseDirection));
                }
            }
        }

        return validReceivers;
    }

    static Map access$000(EnergyNet x0)
    {
        return x0.registeredTiles;
    }
}
