package ic2.core;

import ic2.api.tile.ExplosionWhitelist;
import ic2.core.ExplosionIC2$1;
import ic2.core.ExplosionIC2$DropData;
import ic2.core.ExplosionIC2$ItemWithMeta;
import ic2.core.ExplosionIC2$XZposition;
import ic2.core.block.EntityNuke;
import ic2.core.item.armor.ItemArmorHazmat;
import ic2.core.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class ExplosionIC2 extends Explosion
{
    private final Random ExplosionRNG;
    private final World worldObj;
    private final int mapHeight;
    public double explosionX;
    public double explosionY;
    public double explosionZ;
    public Entity exploder;
    public float power;
    public float explosionDropRate;
    public float explosionDamage;
    public boolean nuclear;
    public EntityLivingBase igniter;
    public List<Entry<Integer, Entity>> entitiesInRange;
    public Map<EntityPlayer, Vec3> vecMap;
    public Map<ChunkPosition, Boolean> destroyedBlockPositions;
    private ChunkCache chunkCache;
    private static final double dropPowerLimit = 8.0D;
    private static final double damageAtDropPowerLimit = 32.0D;
    private static final double accelerationAtDropPowerLimit = 0.7D;
    private static final double motionLimit = 60.0D;
    private static final int secondaryRayCount = 5;

    public ExplosionIC2(World world, Entity entity, double x, double y, double z, float power, float drop, float entitydamage)
    {
        super(world, entity, x, y, z, power);
        this.ExplosionRNG = new Random();
        this.nuclear = false;
        this.entitiesInRange = new ArrayList();
        this.vecMap = new HashMap();
        this.destroyedBlockPositions = new HashMap();
        this.worldObj = world;
        this.mapHeight = IC2.getWorldHeight(world);
        this.exploder = entity;
        this.power = power;
        this.explosionDropRate = drop;
        this.explosionDamage = entitydamage;
        this.explosionX = x;
        this.explosionY = y;
        this.explosionZ = z;
    }

    public ExplosionIC2(World world, Entity entity, double x, double y, double z, float power, float drop, float entitydamage, boolean nuclear)
    {
        this(world, entity, x, y, z, power, drop, entitydamage);
        this.nuclear = nuclear;
    }

    public ExplosionIC2(World world, Entity entity, double x, double y, double z, float power, float drop, float entitydamage, EntityLivingBase igniter)
    {
        this(world, entity, x, y, z, power, drop, entitydamage);
        this.igniter = igniter;
    }

    public void doExplosion()
    {
        if (this.power > 0.0F)
        {
            double maxDistance = (double)this.power / 0.4D;
            int maxDistanceInt = (int)Math.ceil(maxDistance);
            this.chunkCache = new ChunkCache(this.worldObj, (int)this.explosionX - maxDistanceInt, (int)this.explosionY - maxDistanceInt, (int)this.explosionZ - maxDistanceInt, (int)this.explosionX + maxDistanceInt, (int)this.explosionY + maxDistanceInt, (int)this.explosionZ + maxDistanceInt, 0);
            List entities = this.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.getAABBPool().getAABB(this.explosionX - maxDistance, this.explosionY - maxDistance, this.explosionZ - maxDistance, this.explosionX + maxDistance, this.explosionY + maxDistance, this.explosionZ + maxDistance));
            Iterator entitiesAreInRange = entities.iterator();

            while (entitiesAreInRange.hasNext())
            {
                Entity steps = (Entity)entitiesAreInRange.next();

                if (steps instanceof EntityLivingBase || steps instanceof EntityItem)
                {
                    this.entitiesInRange.add(new SimpleEntry(Integer.valueOf((int)((steps.posX - this.explosionX) * (steps.posX - this.explosionX) + (steps.posY - this.explosionY) * (steps.posY - this.explosionY) + (steps.posZ - this.explosionZ) * (steps.posZ - this.explosionZ))), steps));
                }
            }

            boolean var37 = !this.entitiesInRange.isEmpty();

            if (var37)
            {
                Collections.sort(this.entitiesInRange, new ExplosionIC2$1(this));
            }

            int var38 = (int)Math.ceil(Math.PI / Math.atan(1.0D / maxDistance));
            double entry;

            for (int blocksToDrop = 0; blocksToDrop < 2 * var38; ++blocksToDrop)
            {
                for (int i$ = 0; i$ < var38; ++i$)
                {
                    entry = (Math.PI * 2D) / (double)var38 * (double)blocksToDrop;
                    double i$1 = Math.PI / (double)var38 * (double)i$;
                    this.shootRay(this.explosionX, this.explosionY, this.explosionZ, entry, i$1, (double)this.power, var37 && blocksToDrop % 8 == 0 && i$ % 8 == 0);
                }
            }

            Iterator var39 = this.entitiesInRange.iterator();

            while (var39.hasNext())
            {
                Entry var40 = (Entry)var39.next();
                Entity var42 = (Entity)var40.getValue();
                double xZposition = var42.motionX * var42.motionX + var42.motionY * var42.motionY + var42.motionZ * var42.motionZ;

                if (xZposition > 3600.0D)
                {
                    double entry2 = Math.sqrt(3600.0D / xZposition);
                    var42.motionX *= entry2;
                    var42.motionY *= entry2;
                    var42.motionZ *= entry2;
                }
            }

            int var46;
            int var49;

            if (this.isNuclear())
            {
                var39 = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getAABBPool().getAABB(this.explosionX - 100.0D, this.explosionY - 100.0D, this.explosionZ - 100.0D, this.explosionX + 100.0D, this.explosionY + 100.0D, this.explosionZ + 100.0D)).iterator();

                while (var39.hasNext())
                {
                    EntityLiving var44 = (EntityLiving)var39.next();

                    if (!ItemArmorHazmat.hasCompleteHazmat(var44))
                    {
                        entry = var44.getDistance(this.explosionX, this.explosionY, this.explosionZ);
                        var46 = (int)(120.0D * (100.0D - entry));
                        var49 = (int)(80.0D * (30.0D - entry));

                        if (var46 >= 0)
                        {
                            var44.addPotionEffect(new PotionEffect(Potion.hunger.id, var46, 0));
                        }

                        if (var49 >= 0)
                        {
                            var44.addPotionEffect(new PotionEffect(IC2Potion.radiation.id, var49, 0));
                        }
                    }
                }
            }

            IC2.network.initiateExplosionEffect(this.worldObj, this.explosionX, this.explosionY, this.explosionZ);
            HashMap var41 = new HashMap();
            Iterator var43 = this.destroyedBlockPositions.entrySet().iterator();
            Entry var45;

            while (var43.hasNext())
            {
                var45 = (Entry)var43.next();
                int var47 = ((ChunkPosition)var45.getKey()).x;
                var46 = ((ChunkPosition)var45.getKey()).y;
                var49 = ((ChunkPosition)var45.getKey()).z;
                int itemWithMeta = this.chunkCache.getBlockId(var47, var46, var49);

                if (itemWithMeta != 0)
                {
                    if (((Boolean)var45.getValue()).booleanValue())
                    {
                        double count = (double)((float)var47 + this.worldObj.rand.nextFloat());
                        double entityitem = (double)((float)var46 + this.worldObj.rand.nextFloat());
                        double effectZ = (double)((float)var49 + this.worldObj.rand.nextFloat());
                        double d3 = count - this.explosionX;
                        double d4 = entityitem - this.explosionY;
                        double d5 = effectZ - this.explosionZ;
                        double effectDistance = (double)MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
                        d3 /= effectDistance;
                        d4 /= effectDistance;
                        d5 /= effectDistance;
                        double d7 = 0.5D / (effectDistance / (double)this.power + 0.1D);
                        d7 *= (double)(this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3F);
                        d3 *= d7;
                        d4 *= d7;
                        d5 *= d7;
                        this.worldObj.spawnParticle("explode", (count + this.explosionX) / 2.0D, (entityitem + this.explosionY) / 2.0D, (effectZ + this.explosionZ) / 2.0D, d3, d4, d5);
                        this.worldObj.spawnParticle("smoke", count, entityitem, effectZ, d3, d4, d5);
                        Block block = Block.blocksList[itemWithMeta];
                        int meta = this.worldObj.getBlockMetadata(var47, var46, var49);
                        Iterator i$2 = block.getBlockDropped(this.worldObj, var47, var46, var49, meta, 0).iterator();

                        while (i$2.hasNext())
                        {
                            ItemStack itemStack = (ItemStack)i$2.next();

                            if (this.worldObj.rand.nextFloat() <= this.explosionDropRate)
                            {
                                ExplosionIC2$XZposition xZposition1 = new ExplosionIC2$XZposition(var47 / 2, var49 / 2);

                                if (!var41.containsKey(xZposition1))
                                {
                                    var41.put(xZposition1, new HashMap());
                                }

                                Map map = (Map)var41.get(xZposition1);
                                ExplosionIC2$ItemWithMeta itemWithMeta1 = new ExplosionIC2$ItemWithMeta(itemStack.itemID, itemStack.getItemDamage());

                                if (!map.containsKey(itemWithMeta1))
                                {
                                    map.put(itemWithMeta1, new ExplosionIC2$DropData(itemStack.stackSize, var46));
                                }
                                else
                                {
                                    map.put(itemWithMeta1, ((ExplosionIC2$DropData)map.get(itemWithMeta1)).add(itemStack.stackSize, var46));
                                }
                            }
                        }
                    }

                    this.worldObj.setBlock(var47, var46, var49, 0, 0, 7);
                    Block.blocksList[itemWithMeta].onBlockDestroyedByExplosion(this.worldObj, var47, var46, var49, this);
                }
            }

            var43 = var41.entrySet().iterator();

            while (var43.hasNext())
            {
                var45 = (Entry)var43.next();
                ExplosionIC2$XZposition var48 = (ExplosionIC2$XZposition)var45.getKey();
                Iterator var50 = ((Map)var45.getValue()).entrySet().iterator();

                while (var50.hasNext())
                {
                    Entry var51 = (Entry)var50.next();
                    ExplosionIC2$ItemWithMeta var52 = (ExplosionIC2$ItemWithMeta)var51.getKey();
                    int stackSize;

                    for (int var53 = ((ExplosionIC2$DropData)var51.getValue()).n; var53 > 0; var53 -= stackSize)
                    {
                        stackSize = Math.min(var53, 64);
                        EntityItem var54 = new EntityItem(this.worldObj, (double)((float)var48.x + this.worldObj.rand.nextFloat()) * 2.0D, (double)((ExplosionIC2$DropData)var51.getValue()).maxY + 0.5D, (double)((float)var48.z + this.worldObj.rand.nextFloat()) * 2.0D, new ItemStack(var52.itemId, stackSize, var52.metaData));
                        var54.delayBeforeCanPickup = 10;
                        this.worldObj.spawnEntityInWorld(var54);
                    }
                }
            }
        }
    }

    private void shootRay(double x, double y, double z, double phi, double theta, double power, boolean killEntities)
    {
        double deltaX = Math.sin(theta) * Math.cos(phi);
        double deltaY = Math.cos(theta);
        double deltaZ = Math.sin(theta) * Math.sin(phi);
        int step = 0;

        while (true)
        {
            int blockY = Util.roundToNegInf(y);

            if (blockY < 0 || blockY >= this.mapHeight)
            {
                break;
            }

            int blockX = Util.roundToNegInf(x);
            int blockZ = Util.roundToNegInf(z);
            int blockId = this.chunkCache.getBlockId(blockX, blockY, blockZ);
            double absorption = 0.5D;

            if (blockId > 0)
            {
                absorption += ((double)Block.blocksList[blockId].getExplosionResistance(this.exploder, this.worldObj, blockX, blockY, blockZ, this.explosionX, this.explosionY, this.explosionZ) + 4.0D) * 0.3D;
            }

            if (absorption > 1000.0D && !ExplosionWhitelist.isBlockWhitelisted(Block.blocksList[blockId]))
            {
                absorption = 0.5D;
            }
            else
            {
                if (absorption > power)
                {
                    break;
                }

                if (blockId > 0)
                {
                    ChunkPosition i = new ChunkPosition(blockX, blockY, blockZ);

                    if (power > 8.0D)
                    {
                        this.destroyedBlockPositions.put(i, Boolean.valueOf(false));
                    }
                    else if (!this.destroyedBlockPositions.containsKey(i))
                    {
                        this.destroyedBlockPositions.put(i, Boolean.valueOf(true));
                    }
                }
            }

            int var39;

            if (killEntities && (step + 4) % 8 == 0 && power >= 0.25D)
            {
                int i1;

                if (step != 4)
                {
                    i1 = step * step - 25;
                    int entity = 0;
                    int dx = this.entitiesInRange.size() - 1;

                    do
                    {
                        var39 = (entity + dx) / 2;
                        int distance = ((Integer)((Entry)this.entitiesInRange.get(var39)).getKey()).intValue();

                        if (distance < i1)
                        {
                            entity = var39 + 1;
                        }
                        else if (distance > i1)
                        {
                            dx = var39 - 1;
                        }
                        else
                        {
                            dx = var39;
                        }
                    }
                    while (entity < dx);
                }
                else
                {
                    var39 = 0;
                }

                int distanceMax = step * step + 25;

                for (i1 = var39; i1 < this.entitiesInRange.size() && ((Integer)((Entry)this.entitiesInRange.get(var39)).getKey()).intValue() < distanceMax; ++i1)
                {
                    Entity var40 = (Entity)((Entry)this.entitiesInRange.get(var39)).getValue();

                    if ((var40.posX - x) * (var40.posX - x) + (var40.posY - y) * (var40.posY - y) + (var40.posZ - z) * (var40.posZ - z) <= 25.0D)
                    {
                        var40.attackEntityFrom(this.getDamageSource(), (float)((int)(32.0D * power / 8.0D)));

                        if (var40 instanceof EntityPlayer)
                        {
                            EntityPlayer var41 = (EntityPlayer)var40;

                            if (this.isNuclear() && this.igniter != null && var41.username.equals(this.igniter) && var41.getHealth() <= 0.0F)
                            {
                                IC2.achievements.issueAchievement(var41, "dieFromOwnNuke");
                            }
                        }

                        double var42 = var40.posX - this.explosionX;
                        double dy = var40.posY - this.explosionY;
                        double dz = var40.posZ - this.explosionZ;
                        double distance1 = Math.sqrt(var42 * var42 + dy * dy + dz * dz);
                        var40.motionX += var42 / distance1 * 0.7D * power / 8.0D;
                        var40.motionY += dy / distance1 * 0.7D * power / 8.0D;
                        var40.motionZ += dz / distance1 * 0.7D * power / 8.0D;

                        if (!var40.isEntityAlive())
                        {
                            this.entitiesInRange.remove(i1);
                            --i1;
                        }
                    }
                }
            }

            if (absorption > 10.0D)
            {
                for (var39 = 0; var39 < 5; ++var39)
                {
                    this.shootRay(x, y, z, this.ExplosionRNG.nextDouble() * 2.0D * Math.PI, this.ExplosionRNG.nextDouble() * Math.PI, absorption * 0.4D, false);
                }
            }

            power -= absorption;
            x += deltaX;
            y += deltaY;
            z += deltaZ;
            ++step;
        }
    }

    public EntityLivingBase getExplosivePlacedBy()
    {
        return this.igniter;
    }

    private DamageSource getDamageSource()
    {
        return this.isNuclear() ? IC2DamageSource.setNukeSource(this) : DamageSource.setExplosionSource(this);
    }

    private boolean isNuclear()
    {
        return this.nuclear || this.exploder instanceof EntityNuke;
    }
}
