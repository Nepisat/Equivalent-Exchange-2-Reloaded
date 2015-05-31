package ic2.core.block;

import ic2.core.ExplosionIC2;
import ic2.core.IC2;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityIC2Explosive extends Entity
{
    public DamageSource damageSource;
    public EntityLivingBase igniter;
    public int fuse;
    public float explosivePower;
    public float dropRate;
    public float damageVsEntitys;
    public Block renderBlock;

    public EntityIC2Explosive(World world)
    {
        super(world);
        this.fuse = 80;
        this.explosivePower = 4.0F;
        this.dropRate = 0.3F;
        this.damageVsEntitys = 1.0F;
        this.renderBlock = Block.dirt;
        super.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        super.yOffset = super.height / 2.0F;
    }

    public EntityIC2Explosive(World world, double d, double d1, double d2, int fuselength, float power, float rate, float damage, Block block)
    {
        this(world);
        this.setPosition(d, d1, d2);
        float f = (float)(Math.random() * Math.PI * 2.0D);
        super.motionX = (double)(-MathHelper.sin(f * (float)Math.PI / 180.0F) * 0.02F);
        super.motionY = 0.20000000298023224D;
        super.motionZ = (double)(-MathHelper.cos(f * (float)Math.PI / 180.0F) * 0.02F);
        super.prevPosX = d;
        super.prevPosY = d1;
        super.prevPosZ = d2;
        this.fuse = fuselength;
        this.explosivePower = power;
        this.dropRate = rate;
        this.damageVsEntitys = damage;
        this.renderBlock = block;
    }

    protected void entityInit() {}

    protected boolean canTriggerWalking()
    {
        return false;
    }

    public boolean canBeCollidedWith()
    {
        return !super.isDead;
    }

    public void onUpdate()
    {
        super.prevPosX = super.posX;
        super.prevPosY = super.posY;
        super.prevPosZ = super.posZ;
        super.motionY -= 0.04D;
        this.moveEntity(super.motionX, super.motionY, super.motionZ);
        super.motionX *= 0.98D;
        super.motionY *= 0.98D;
        super.motionZ *= 0.98D;

        if (super.onGround)
        {
            super.motionX *= 0.7D;
            super.motionZ *= 0.7D;
            super.motionY *= -0.5D;
        }

        if (this.fuse-- <= 0)
        {
            this.setDead();

            if (IC2.platform.isSimulating())
            {
                this.explode();
            }
        }
        else
        {
            super.worldObj.spawnParticle("smoke", super.posX, super.posY + 0.5D, super.posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private void explode()
    {
        ExplosionIC2 explosion = new ExplosionIC2(super.worldObj, this, super.posX, super.posY, super.posZ, this.explosivePower, this.dropRate, this.damageVsEntitys, this.igniter);
        explosion.doExplosion();
    }

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setByte("Fuse", (byte)this.fuse);
    }

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        this.fuse = nbttagcompound.getByte("Fuse");
    }

    public float getShadowSize()
    {
        return 0.0F;
    }

    public EntityIC2Explosive setIgniter(EntityLivingBase igniter)
    {
        this.igniter = igniter;
        return this;
    }
}
