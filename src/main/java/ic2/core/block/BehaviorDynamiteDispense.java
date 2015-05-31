package ic2.core.block;

import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.world.World;

public class BehaviorDynamiteDispense extends BehaviorProjectileDispense
{
    private boolean sticky = false;

    public BehaviorDynamiteDispense(boolean sticky)
    {
        this.sticky = sticky;
    }

    /**
     * Return the projectile entity spawned by this dispense behavior.
     */
    protected IProjectile getProjectileEntity(World var1, IPosition var2)
    {
        return new EntityDynamite(var1, var2.getX(), var2.getY(), var2.getZ(), this.sticky);
    }
}
