package ic2.core.block;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityStickyDynamite extends EntityDynamite
{
    public EntityStickyDynamite(World world)
    {
        super(world);
        super.sticky = true;
    }

    public EntityStickyDynamite(World world, EntityLivingBase entityliving)
    {
        super(world, entityliving);
        super.sticky = true;
    }
}
