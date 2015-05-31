package ic2.core.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public abstract class EntityIC2Boat extends EntityBoat
{
    private boolean field_70279_a = true;
    private double speedMultiplier = 0.07D;
    private int boatPosRotationIncrements;
    private double boatX;
    private double boatY;
    private double boatZ;
    private double boatYaw;
    private double boatPitch;
    private double velocityX;
    private double velocityY;
    private double velocityZ;

    public EntityIC2Boat(World par1World)
    {
        super(par1World);
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else if (!super.worldObj.isRemote && !super.isDead)
        {
            this.setForwardDirection(-this.getForwardDirection());
            this.setTimeSinceHit(10);
            this.setDamageTaken(this.getDamageTaken() + par2 * 10.0F);
            this.setBeenAttacked();
            boolean flag = par1DamageSource.getEntity() instanceof EntityPlayer && ((EntityPlayer)par1DamageSource.getEntity()).capabilities.isCreativeMode;

            if (flag || this.getDamageTaken() > 40.0F)
            {
                if (super.riddenByEntity != null)
                {
                    super.riddenByEntity.mountEntity(this);
                }

                if (!flag)
                {
                    this.dropItem(par1DamageSource);
                }

                this.setDead();
            }

            return true;
        }
        else
        {
            return true;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.onEntityUpdate();

        if (this.getTimeSinceHit() > 0)
        {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }

        if (this.getDamageTaken() > 0.0F)
        {
            this.setDamageTaken(this.getDamageTaken() - 1.0F);
        }

        super.prevPosX = super.posX;
        super.prevPosY = super.posY;
        super.prevPosZ = super.posZ;
        byte b0 = 5;
        double d0 = 0.0D;

        for (int var25 = 0; var25 < b0; ++var25)
        {
            double d1 = super.boundingBox.minY + (super.boundingBox.maxY - super.boundingBox.minY) * (double)(var25 + 0) / (double)b0 - 0.125D;
            double d2 = super.boundingBox.minY + (super.boundingBox.maxY - super.boundingBox.minY) * (double)(var25 + 1) / (double)b0 - 0.125D;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB(super.boundingBox.minX, d1, super.boundingBox.minZ, super.boundingBox.maxX, d2, super.boundingBox.maxZ);

            if (this.isOnWater(axisalignedbb))
            {
                d0 += 1.0D / (double)b0;
            }
        }

        double var251 = Math.sqrt(super.motionX * super.motionX + super.motionZ * super.motionZ);
        double d4;
        double d5;

        if (var251 > 0.26249999999999996D)
        {
            d4 = Math.cos((double)super.rotationYaw * Math.PI / 180.0D);
            d5 = Math.sin((double)super.rotationYaw * Math.PI / 180.0D);

            for (int d11 = 0; (double)d11 < 1.0D + var251 * 60.0D; ++d11)
            {
                double d6 = (double)(super.rand.nextFloat() * 2.0F - 1.0F);
                double d7 = (double)(super.rand.nextInt(2) * 2 - 1) * 0.7D;
                double d8;
                double d9;

                if (super.rand.nextBoolean())
                {
                    d8 = super.posX - d4 * d6 * 0.8D + d5 * d7;
                    d9 = super.posZ - d5 * d6 * 0.8D - d4 * d7;
                    super.worldObj.spawnParticle("splash", d8, super.posY - 0.125D, d9, super.motionX, super.motionY, super.motionZ);
                }
                else
                {
                    d8 = super.posX + d4 + d5 * d6 * 0.7D;
                    d9 = super.posZ + d5 - d4 * d6 * 0.7D;
                    super.worldObj.spawnParticle("splash", d8, super.posY - 0.125D, d9, super.motionX, super.motionY, super.motionZ);
                }
            }
        }

        double var26;
        double var261;

        if (super.worldObj.isRemote && this.field_70279_a)
        {
            if (this.boatPosRotationIncrements > 0)
            {
                d4 = super.posX + (this.boatX - super.posX) / (double)this.boatPosRotationIncrements;
                d5 = super.posY + (this.boatY - super.posY) / (double)this.boatPosRotationIncrements;
                var261 = super.posZ + (this.boatZ - super.posZ) / (double)this.boatPosRotationIncrements;
                var26 = MathHelper.wrapAngleTo180_double(this.boatYaw - (double)super.rotationYaw);
                super.rotationYaw = (float)((double)super.rotationYaw + var26 / (double)this.boatPosRotationIncrements);
                super.rotationPitch = (float)((double)super.rotationPitch + (this.boatPitch - (double)super.rotationPitch) / (double)this.boatPosRotationIncrements);
                --this.boatPosRotationIncrements;
                this.setPosition(d4, d5, var261);
                this.setRotation(super.rotationYaw, super.rotationPitch);
            }
            else
            {
                d4 = super.posX + super.motionX;
                d5 = super.posY + super.motionY;
                var261 = super.posZ + super.motionZ;
                this.setPosition(d4, d5, var261);

                if (super.onGround)
                {
                    super.motionX *= 0.5D;
                    super.motionY *= 0.5D;
                    super.motionZ *= 0.5D;
                }

                super.motionX *= 0.9900000095367432D;
                super.motionY *= 0.949999988079071D;
                super.motionZ *= 0.9900000095367432D;
            }
        }
        else
        {
            if (d0 < 1.0D)
            {
                d4 = d0 * 2.0D - 1.0D;
                super.motionY += 0.03999999910593033D * d4;
            }
            else
            {
                if (super.motionY < 0.0D)
                {
                    super.motionY /= 2.0D;
                }

                super.motionY += 0.007000000216066837D;
            }

            if (super.riddenByEntity != null)
            {
                super.motionX += super.riddenByEntity.motionX * this.speedMultiplier * this.getAccelerationFactor();
                super.motionZ += super.riddenByEntity.motionZ * this.speedMultiplier * this.getAccelerationFactor();
            }

            d4 = Math.sqrt(super.motionX * super.motionX + super.motionZ * super.motionZ);
            double topSpeed = this.getTopSpeed();

            if (d4 > topSpeed)
            {
                d5 = topSpeed / d4;
                super.motionX *= d5;
                super.motionZ *= d5;
                d4 = topSpeed;
            }

            if (d4 > var251 && this.speedMultiplier < 0.35D)
            {
                this.speedMultiplier += (0.35D - this.speedMultiplier) / 35.0D;

                if (this.speedMultiplier > 0.35D)
                {
                    this.speedMultiplier = 0.35D;
                }
            }
            else
            {
                this.speedMultiplier -= (this.speedMultiplier - 0.07D) / 35.0D;

                if (this.speedMultiplier < 0.07D)
                {
                    this.speedMultiplier = 0.07D;
                }
            }

            if (super.onGround)
            {
                super.motionX *= 0.5D;
                super.motionY *= 0.5D;
                super.motionZ *= 0.5D;
            }

            this.moveEntity(super.motionX, super.motionY, super.motionZ);

            if (super.isCollidedHorizontally && var251 > this.getBreakMotion())
            {
                if (!super.worldObj.isRemote)
                {
                    this.setDead();
                    this.breakBoat(var251);
                }
            }
            else
            {
                super.motionX *= 0.9900000095367432D;
                super.motionY *= 0.949999988079071D;
                super.motionZ *= 0.9900000095367432D;
            }

            super.rotationPitch = 0.0F;
            d5 = (double)super.rotationYaw;
            var261 = super.prevPosX - super.posX;
            var26 = super.prevPosZ - super.posZ;

            if (var261 * var261 + var26 * var26 > 0.001D)
            {
                d5 = (double)((float)(Math.atan2(var26, var261) * 180.0D / Math.PI));
            }

            double d12 = MathHelper.wrapAngleTo180_double(d5 - (double)super.rotationYaw);

            if (d12 > 20.0D)
            {
                d12 = 20.0D;
            }

            if (d12 < -20.0D)
            {
                d12 = -20.0D;
            }

            super.rotationYaw = (float)((double)super.rotationYaw + d12);
            this.setRotation(super.rotationYaw, super.rotationPitch);

            if (!super.worldObj.isRemote)
            {
                List list = super.worldObj.getEntitiesWithinAABBExcludingEntity(this, super.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
                int l;

                if (list != null && !list.isEmpty())
                {
                    for (l = 0; l < list.size(); ++l)
                    {
                        Entity var27 = (Entity)list.get(l);

                        if (var27 != super.riddenByEntity && var27.canBePushed() && var27 instanceof EntityBoat)
                        {
                            var27.applyEntityCollision(this);
                        }
                    }
                }

                for (l = 0; l < 4; ++l)
                {
                    int var271 = MathHelper.floor_double(super.posX + ((double)(l % 2) - 0.5D) * 0.8D);
                    int j1 = MathHelper.floor_double(super.posZ + ((double)(l / 2) - 0.5D) * 0.8D);

                    for (int k1 = 0; k1 < 2; ++k1)
                    {
                        int l1 = MathHelper.floor_double(super.posY) + k1;
                        int i2 = super.worldObj.getBlockId(var271, l1, j1);

                        if (i2 == Block.snow.blockID)
                        {
                            super.worldObj.setBlockToAir(var271, l1, j1);
                        }
                        else if (i2 == Block.waterlily.blockID)
                        {
                            super.worldObj.destroyBlock(var271, l1, j1, true);
                        }
                    }
                }

                if (super.riddenByEntity != null && super.riddenByEntity.isDead)
                {
                    super.riddenByEntity = null;
                }
            }
        }
    }

    public void func_70270_d(boolean par1)
    {
        this.field_70279_a = par1;
    }

    /**
     * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
     * posY, posZ, yaw, pitch
     */
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
        if (this.field_70279_a)
        {
            this.boatPosRotationIncrements = par9 + 5;
        }
        else
        {
            double d3 = par1 - super.posX;
            double d4 = par3 - super.posY;
            double d5 = par5 - super.posZ;
            double d6 = d3 * d3 + d4 * d4 + d5 * d5;

            if (d6 <= 1.0D)
            {
                return;
            }

            this.boatPosRotationIncrements = 3;
        }

        this.boatX = par1;
        this.boatY = par3;
        this.boatZ = par5;
        this.boatYaw = (double)par7;
        this.boatPitch = (double)par8;
        super.motionX = this.velocityX;
        super.motionY = this.velocityY;
        super.motionZ = this.velocityZ;
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    public void setVelocity(double par1, double par3, double par5)
    {
        this.velocityX = super.motionX = par1;
        this.velocityY = super.motionY = par3;
        this.velocityZ = super.motionZ = par5;
    }

    public abstract String getTexture();

    public ItemStack getPickedResult(MovingObjectPosition target)
    {
        return this.getItem();
    }

    protected ItemStack getItem()
    {
        return new ItemStack(Item.boat);
    }

    protected void dropItem(DamageSource killer)
    {
        this.entityDropItem(this.getItem(), 0.0F);
    }

    protected double getBreakMotion()
    {
        return 0.2D;
    }

    protected void breakBoat(double motion)
    {
        int k;

        for (k = 0; k < 3; ++k)
        {
            this.dropItemWithOffset(Block.planks.blockID, 1, 0.0F);
        }

        for (k = 0; k < 2; ++k)
        {
            this.dropItemWithOffset(Item.stick.itemID, 1, 0.0F);
        }
    }

    protected double getAccelerationFactor()
    {
        return 1.0D;
    }

    protected double getTopSpeed()
    {
        return 0.35D;
    }

    protected boolean isOnWater(AxisAlignedBB aabb)
    {
        return super.worldObj.isAABBInMaterial(aabb, Material.water);
    }
}
