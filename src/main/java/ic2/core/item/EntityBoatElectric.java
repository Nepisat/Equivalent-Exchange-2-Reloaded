package ic2.core.item;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.Ic2Items;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityBoatElectric extends EntityIC2Boat
{
    private static final int euConsume = 4;
    private boolean accelerated = false;

    public EntityBoatElectric(World par1World)
    {
        super(par1World);
    }

    protected ItemStack getItem()
    {
        return Ic2Items.boatElectric.copy();
    }

    protected double getBreakMotion()
    {
        return 0.5D;
    }

    protected void breakBoat(double motion)
    {
        this.entityDropItem(this.getItem(), 0.0F);
    }

    protected double getAccelerationFactor()
    {
        return this.accelerated ? 1.5D : 0.25D;
    }

    protected double getTopSpeed()
    {
        return 0.7D;
    }

    protected boolean isOnWater(AxisAlignedBB aabb)
    {
        return super.worldObj.isAABBInMaterial(aabb, Material.water) || super.worldObj.isAABBInMaterial(aabb, Material.lava);
    }

    public String getTexture()
    {
        return "textures/models/boatElectric.png";
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.isImmuneToFire = true;
        this.extinguish();

        if (super.ridingEntity != null)
        {
            super.ridingEntity.extinguish();
        }

        this.accelerated = false;

        if ((Math.abs(super.motionX) > 0.1D || Math.abs(super.motionZ) > 0.1D) && super.riddenByEntity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)super.riddenByEntity;

            for (int i = 0; i < 4; ++i)
            {
                if (player.inventory.armorInventory[i] != null && player.inventory.armorInventory[i].getItem() instanceof IElectricItem && ElectricItem.manager.discharge(player.inventory.armorInventory[i], 4, Integer.MAX_VALUE, true, true) == 4)
                {
                    ElectricItem.manager.discharge(player.inventory.armorInventory[i], 4, Integer.MAX_VALUE, true, false);
                    this.accelerated = true;
                    break;
                }
            }
        }

        super.onUpdate();
    }
}
