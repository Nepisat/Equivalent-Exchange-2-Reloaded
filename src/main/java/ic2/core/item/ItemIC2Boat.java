package ic2.core.item;

import ic2.core.Ic2Items;
import ic2.core.init.InternalName;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class ItemIC2Boat extends ItemIC2
{
    public ItemIC2Boat(Configuration config, InternalName internalName)
    {
        super(config, internalName);
        this.setHasSubtypes(true);
        Ic2Items.boatCarbon = new ItemStack(this, 1, 0);
        Ic2Items.boatRubber = new ItemStack(this, 1, 1);
        Ic2Items.boatRubberBroken = new ItemStack(this, 1, 2);
        Ic2Items.boatElectric = new ItemStack(this, 1, 3);
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EntityIC2Boat entityboat = this.makeBoat(par1ItemStack, par2World, par3EntityPlayer);

        if (entityboat == null)
        {
            return par1ItemStack;
        }
        else
        {
            float f = 1.0F;
            float f1 = par3EntityPlayer.prevRotationPitch + (par3EntityPlayer.rotationPitch - par3EntityPlayer.prevRotationPitch) * f;
            float f2 = par3EntityPlayer.prevRotationYaw + (par3EntityPlayer.rotationYaw - par3EntityPlayer.prevRotationYaw) * f;
            double d0 = par3EntityPlayer.prevPosX + (par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * (double)f;
            double d1 = par3EntityPlayer.prevPosY + (par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * (double)f + 1.62D - (double)par3EntityPlayer.yOffset;
            double d2 = par3EntityPlayer.prevPosZ + (par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * (double)f;
            Vec3 vec3 = par2World.getWorldVec3Pool().getVecFromPool(d0, d1, d2);
            float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
            float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
            float f5 = -MathHelper.cos(-f1 * 0.017453292F);
            float f6 = MathHelper.sin(-f1 * 0.017453292F);
            float f7 = f4 * f5;
            float f8 = f3 * f5;
            double d3 = 5.0D;
            Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
            MovingObjectPosition movingobjectposition = par2World.clip(vec3, vec31, true);

            if (movingobjectposition == null)
            {
                return par1ItemStack;
            }
            else
            {
                Vec3 vec32 = par3EntityPlayer.getLook(f);
                boolean flag = false;
                float f9 = 1.0F;
                List list = par2World.getEntitiesWithinAABBExcludingEntity(par3EntityPlayer, par3EntityPlayer.boundingBox.addCoord(vec32.xCoord * d3, vec32.yCoord * d3, vec32.zCoord * d3).expand((double)f9, (double)f9, (double)f9));
                Iterator j = list.iterator();

                while (j.hasNext())
                {
                    Entity i = (Entity)j.next();

                    if (i.canBeCollidedWith())
                    {
                        float var34 = i.getCollisionBorderSize();
                        AxisAlignedBB var35 = i.boundingBox.expand((double)var34, (double)var34, (double)var34);

                        if (var35.isVecInside(vec3))
                        {
                            flag = true;
                        }
                    }
                }

                if (flag)
                {
                    return par1ItemStack;
                }
                else
                {
                    if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
                    {
                        int var33 = movingobjectposition.blockX;
                        int var341 = movingobjectposition.blockY;
                        int var351 = movingobjectposition.blockZ;

                        if (par2World.getBlockId(var33, var341, var351) == Block.snow.blockID)
                        {
                            --var341;
                        }

                        entityboat.setPosition((double)((float)var33 + 0.5F), (double)((float)var341 + 1.0F), (double)((float)var351 + 0.5F));
                        entityboat.rotationYaw = (float)(((MathHelper.floor_double((double)(par3EntityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);

                        if (!par2World.getCollidingBoundingBoxes(entityboat, entityboat.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty())
                        {
                            return par1ItemStack;
                        }

                        if (!par2World.isRemote)
                        {
                            par2World.spawnEntityInWorld(entityboat);
                        }

                        if (!par3EntityPlayer.capabilities.isCreativeMode)
                        {
                            --par1ItemStack.stackSize;
                        }
                    }

                    return par1ItemStack;
                }
            }
        }
    }

    protected EntityIC2Boat makeBoat(ItemStack stack, World world, EntityPlayer player)
    {
        switch (stack.getItemDamage())
        {
            case 0:
                return new EntityBoatCarbon(world);

            case 1:
                return new EntityBoatRubber(world);

            case 2:
            default:
                return null;

            case 3:
                return new EntityBoatElectric(world);
        }
    }

    public String getUnlocalizedName(ItemStack itemStack)
    {
        InternalName ret;

        switch (itemStack.getItemDamage())
        {
            case 0:
                ret = InternalName.boatCarbon;
                break;

            case 1:
                ret = InternalName.boatRubber;
                break;

            case 2:
                ret = InternalName.boatRubberBroken;
                break;

            case 3:
                ret = InternalName.boatElectric;
                break;

            default:
                return null;
        }

        return ret.name();
    }

    public void getSubItems(int i, CreativeTabs tabs, List itemList)
    {
        for (int meta = 0; meta <= 32767; ++meta)
        {
            ItemStack stack = new ItemStack(this, 1, meta);

            if (this.getUnlocalizedName(stack) == null)
            {
                break;
            }

            itemList.add(stack);
        }
    }
}
