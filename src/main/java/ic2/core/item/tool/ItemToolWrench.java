package ic2.core.item.tool;

import ic2.api.tile.IWrenchable;
import ic2.core.IC2;
import ic2.core.audio.PositionSpec;
import ic2.core.block.machine.tileentity.TileEntityTerra;
import ic2.core.init.InternalName;
import ic2.core.item.ItemIC2;
import ic2.core.util.StackUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;

public class ItemToolWrench extends ItemIC2
{
    public ItemToolWrench(Configuration config, InternalName internalName)
    {
        super(config, internalName);
        this.setMaxDamage(160);
        this.setMaxStackSize(1);
    }

    public boolean canTakeDamage(ItemStack stack, int amount)
    {
        return true;
    }

    public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!this.canTakeDamage(itemstack, 1))
        {
            return false;
        }
        else
        {
            int blockId = world.getBlockId(x, y, z);
            Block block = Block.blocksList[blockId];

            if (block == null)
            {
                return false;
            }
            else
            {
                int metaData = world.getBlockMetadata(x, y, z);
                TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

                if (tileEntity instanceof TileEntityTerra)
                {
                    TileEntityTerra var20 = (TileEntityTerra)tileEntity;

                    if (var20.ejectBlueprint())
                    {
                        if (IC2.platform.isSimulating())
                        {
                            this.damage(itemstack, 1, entityPlayer);
                        }

                        if (IC2.platform.isRendering())
                        {
                            IC2.audioManager.playOnce(entityPlayer, PositionSpec.Hand, "Tools/wrench.ogg", true, IC2.audioManager.defaultVolume);
                        }

                        return IC2.platform.isSimulating();
                    }
                }

                if (tileEntity instanceof IWrenchable)
                {
                    IWrenchable var201 = (IWrenchable)tileEntity;

                    if (IC2.keyboard.isAltKeyDown(entityPlayer))
                    {
                        for (int var22 = 1; var22 < 6; ++var22)
                        {
                            if (entityPlayer.isSneaking())
                            {
                                side = (var201.getFacing() + 6 - var22) % 6;
                            }
                            else
                            {
                                side = (var201.getFacing() + var22) % 6;
                            }

                            if (var201.wrenchCanSetFacing(entityPlayer, side))
                            {
                                break;
                            }
                        }
                    }
                    else if (entityPlayer.isSneaking())
                    {
                        side += side % 2 * -2 + 1;
                    }

                    if (var201.wrenchCanSetFacing(entityPlayer, side))
                    {
                        if (IC2.platform.isSimulating())
                        {
                            var201.setFacing((short)side);
                            this.damage(itemstack, 1, entityPlayer);
                        }

                        if (IC2.platform.isRendering())
                        {
                            IC2.audioManager.playOnce(entityPlayer, PositionSpec.Hand, "Tools/wrench.ogg", true, IC2.audioManager.defaultVolume);
                        }

                        return IC2.platform.isSimulating();
                    }

                    if (this.canTakeDamage(itemstack, 10) && var201.wrenchCanRemove(entityPlayer))
                    {
                        if (IC2.platform.isSimulating())
                        {
                            if (IC2.enableLoggingWrench)
                            {
                                String var21 = tileEntity.getClass().getName().replace("TileEntity", "");
                                IC2.log.log(Level.INFO, "Player " + entityPlayer.username + " used the wrench to remove the " + var21 + " (" + blockId + "-" + metaData + ") at " + x + "/" + y + "/" + z);
                            }

                            boolean var221 = false;

                            if (var201.getWrenchDropRate() < 1.0F && this.overrideWrenchSuccessRate(itemstack))
                            {
                                if (!this.canTakeDamage(itemstack, 200))
                                {
                                    IC2.platform.messagePlayer(entityPlayer, "Not enough energy for lossless wrench operation", new Object[0]);
                                    return true;
                                }

                                var221 = true;
                                this.damage(itemstack, 200, entityPlayer);
                            }
                            else
                            {
                                var221 = world.rand.nextFloat() <= var201.getWrenchDropRate();
                                this.damage(itemstack, 10, entityPlayer);
                            }

                            ArrayList drops = block.getBlockDropped(world, x, y, z, metaData, 0);

                            if (var221)
                            {
                                ItemStack var23 = var201.getWrenchDrop(entityPlayer);

                                if (var23 != null)
                                {
                                    if (drops.isEmpty())
                                    {
                                        drops.add(var23);
                                    }
                                    else
                                    {
                                        drops.set(0, var23);
                                    }
                                }
                            }

                            Iterator var231 = drops.iterator();

                            while (var231.hasNext())
                            {
                                ItemStack itemStack = (ItemStack)var231.next();
                                StackUtil.dropAsEntity(world, x, y, z, itemStack);
                            }

                            world.setBlock(x, y, z, 0, 0, 3);
                        }

                        if (IC2.platform.isRendering())
                        {
                            IC2.audioManager.playOnce(entityPlayer, PositionSpec.Hand, "Tools/wrench.ogg", true, IC2.audioManager.defaultVolume);
                        }

                        return IC2.platform.isSimulating();
                    }
                }

                if (block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side)))
                {
                    if (IC2.platform.isSimulating())
                    {
                        this.damage(itemstack, 1, entityPlayer);
                    }

                    if (IC2.platform.isRendering())
                    {
                        IC2.audioManager.playOnce(entityPlayer, PositionSpec.Hand, "Tools/wrench.ogg", true, IC2.audioManager.defaultVolume);
                    }

                    return IC2.platform.isSimulating();
                }
                else
                {
                    return false;
                }
            }
        }
    }

    public boolean shouldPassSneakingClickToBlock(World par2World, int par4, int par5, int par6)
    {
        return true;
    }

    public void damage(ItemStack is, int damage, EntityPlayer player)
    {
        is.damageItem(damage, player);
    }

    public boolean overrideWrenchSuccessRate(ItemStack itemStack)
    {
        return false;
    }
}
