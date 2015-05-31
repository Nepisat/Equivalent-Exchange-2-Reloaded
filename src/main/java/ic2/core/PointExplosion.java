package ic2.core;

import ic2.core.util.Util;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class PointExplosion extends Explosion
{
    private final World world;
    private final float dropRate;
    private final int entityDamage;

    public PointExplosion(World world, Entity entity, double x, double y, double z, float power, float dropRate, int entityDamage)
    {
        super(world, entity, x, y, z, power);
        this.world = world;
        this.dropRate = dropRate;
        this.entityDamage = entityDamage;
    }

    public void doExplosionA()
    {
        for (int entitiesInRange = Util.roundToNegInf(super.explosionX) - 1; entitiesInRange <= Util.roundToNegInf(super.explosionX) + 1; ++entitiesInRange)
        {
            for (int i$ = Util.roundToNegInf(super.explosionY) - 1; i$ <= Util.roundToNegInf(super.explosionY) + 1; ++i$)
            {
                for (int entity = Util.roundToNegInf(super.explosionZ) - 1; entity <= Util.roundToNegInf(super.explosionZ) + 1; ++entity)
                {
                    int blockId = this.world.getBlockId(entitiesInRange, i$, entity);

                    if (blockId > 0 && Block.blocksList[blockId].getExplosionResistance(super.exploder, this.world, entitiesInRange, i$, entity, super.explosionX, super.explosionY, super.explosionZ) < super.explosionSize * 10.0F)
                    {
                        super.affectedBlockPositions.add(new ChunkPosition(entitiesInRange, i$, entity));
                    }
                }
            }
        }

        List var5 = this.world.getEntitiesWithinAABBExcludingEntity(super.exploder, AxisAlignedBB.getAABBPool().getAABB(super.explosionX - 2.0D, super.explosionY - 2.0D, super.explosionZ - 2.0D, super.explosionX + 2.0D, super.explosionY + 2.0D, super.explosionZ + 2.0D));
        Iterator var6 = var5.iterator();

        while (var6.hasNext())
        {
            Entity var7 = (Entity)var6.next();
            var7.attackEntityFrom(DamageSource.setExplosionSource(this), (float)this.entityDamage);
        }

        super.explosionSize = 1.0F / this.dropRate;
    }
}
