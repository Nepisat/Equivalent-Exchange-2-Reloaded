package ic2.core.block.generator.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.block.BlockMultiID;
import ic2.core.block.generator.tileentity.TileEntityNuclearReactor;
import ic2.core.block.generator.tileentity.TileEntityReactorChamber;
import ic2.core.block.generator.tileentity.TileEntityReactorChamberElectric;
import ic2.core.init.InternalName;
import ic2.core.item.block.ItemBlockRare;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class BlockReactorChamber extends BlockMultiID
{
    public static Class <? extends TileEntityReactorChamber > tileEntityReactorChamberClass = TileEntityReactorChamberElectric.class;

    public BlockReactorChamber(Configuration config, InternalName internalName)
    {
        super(config, internalName, Material.iron, ItemBlockRare.class);
        this.setHardness(2.0F);
        this.setStepSound(Block.soundMetalFootstep);
        Ic2Items.reactorChamber = new ItemStack(this);
    }

    public String getTextureFolder()
    {
        return "generator";
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        if (!this.canPlaceBlockAt(world, i, j, k))
        {
            this.dropBlockAsItem_do(world, i, j, k, new ItemStack(world.getBlockId(i, j, k), 1, 0));
            world.setBlock(i, j, k, 0, 0, 7);
        }
    }

    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        int count = 0;

        if (this.isReactorAt(world, i + 1, j, k))
        {
            ++count;
        }

        if (this.isReactorAt(world, i - 1, j, k))
        {
            ++count;
        }

        if (this.isReactorAt(world, i, j + 1, k))
        {
            ++count;
        }

        if (this.isReactorAt(world, i, j - 1, k))
        {
            ++count;
        }

        if (this.isReactorAt(world, i, j, k + 1))
        {
            ++count;
        }

        if (this.isReactorAt(world, i, j, k - 1))
        {
            ++count;
        }

        return count == 1;
    }

    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        TileEntityNuclearReactor reactor = this.getReactorEntity(world, i, j, k);

        if (reactor == null)
        {
            this.onNeighborBlockChange(world, i, j, k, super.blockID);
        }
        else
        {
            int puffs = reactor.heat / 1000;

            if (puffs > 0)
            {
                puffs = world.rand.nextInt(puffs);
                int n;

                for (n = 0; n < puffs; ++n)
                {
                    world.spawnParticle("smoke", (double)((float)i + random.nextFloat()), (double)((float)j + 0.95F), (double)((float)k + random.nextFloat()), 0.0D, 0.0D, 0.0D);
                }

                puffs -= world.rand.nextInt(4) + 3;

                for (n = 0; n < puffs; ++n)
                {
                    world.spawnParticle("flame", (double)((float)i + random.nextFloat()), (double)((float)j + 1.0F), (double)((float)k + random.nextFloat()), 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    public boolean isReactorAt(World world, int x, int y, int z)
    {
        return world.getBlockTileEntity(x, y, z) instanceof TileEntityNuclearReactor && world.getBlockId(x, y, z) == Ic2Items.nuclearReactor.itemID && world.getBlockMetadata(x, y, z) == Ic2Items.nuclearReactor.getItemDamage();
    }

    public TileEntityNuclearReactor getReactorEntity(World world, int i, int j, int k)
    {
        if (this.isReactorAt(world, i + 1, j, k))
        {
            return (TileEntityNuclearReactor)world.getBlockTileEntity(i + 1, j, k);
        }
        else if (this.isReactorAt(world, i - 1, j, k))
        {
            return (TileEntityNuclearReactor)world.getBlockTileEntity(i - 1, j, k);
        }
        else if (this.isReactorAt(world, i, j + 1, k))
        {
            return (TileEntityNuclearReactor)world.getBlockTileEntity(i, j + 1, k);
        }
        else if (this.isReactorAt(world, i, j - 1, k))
        {
            return (TileEntityNuclearReactor)world.getBlockTileEntity(i, j - 1, k);
        }
        else if (this.isReactorAt(world, i, j, k + 1))
        {
            return (TileEntityNuclearReactor)world.getBlockTileEntity(i, j, k + 1);
        }
        else if (this.isReactorAt(world, i, j, k - 1))
        {
            return (TileEntityNuclearReactor)world.getBlockTileEntity(i, j, k - 1);
        }
        else
        {
            this.onNeighborBlockChange(world, i, j, k, world.getBlockId(i, j, k));
            return null;
        }
    }

    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float a, float b, float c)
    {
        if (entityplayer.isSneaking())
        {
            return false;
        }
        else
        {
            TileEntityNuclearReactor reactor = this.getReactorEntity(world, i, j, k);

            if (reactor == null)
            {
                this.onNeighborBlockChange(world, i, j, k, super.blockID);
                return false;
            }
            else
            {
                return !IC2.platform.isSimulating() ? true : IC2.platform.launchGui(entityplayer, reactor);
            }
        }
    }

    public TileEntity createTileEntity(World world, int meta)
    {
        try
        {
            return (TileEntity)tileEntityReactorChamberClass.newInstance();
        }
        catch (Throwable var4)
        {
            throw new RuntimeException(var4);
        }
    }

    public int idDropped(int meta, Random random, int j)
    {
        return Ic2Items.machine.itemID;
    }

    public int damageDropped(int meta)
    {
        return Ic2Items.machine.getItemDamage();
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.uncommon;
    }
}
