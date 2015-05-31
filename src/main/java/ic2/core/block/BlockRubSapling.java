package ic2.core.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.init.DefaultIds;
import ic2.core.init.InternalName;
import ic2.core.item.block.ItemBlockRare;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.EnumPlantType;

public class BlockRubSapling extends BlockSapling
{
    public BlockRubSapling(Configuration config, InternalName internalName)
    {
        super(IC2.getBlockIdFor(config, internalName, DefaultIds.get(internalName)));
        this.setHardness(0.0F);
        this.setStepSound(Block.soundGrassFootstep);
        this.setUnlocalizedName(internalName.name());
        this.setCreativeTab(IC2.tabIC2);
        Ic2Items.rubberSapling = new ItemStack(this);
        GameRegistry.registerBlock(this, ItemBlockRare.class, internalName.name());
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        super.blockIcon = iconRegister.registerIcon("ic2:" + this.getUnlocalizedName());
    }

    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
        return super.blockIcon;
    }

    public String getUnlocalizedName()
    {
        return super.getUnlocalizedName().substring(5);
    }

    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if (IC2.platform.isSimulating())
        {
            if (!this.canBlockStay(world, i, j, k))
            {
                this.dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
                world.setBlock(i, j, k, 0, 0, 7);
            }
            else if (world.getBlockLightValue(i, j + 1, k) >= 9 && random.nextInt(30) == 0)
            {
                this.growTree(world, i, j, k, random);
            }
        }
    }

    public void growTree(World world, int i, int j, int k, Random random)
    {
        (new WorldGenRubTree()).grow(world, i, j, k, random);
    }

    public int damageDropped(int i)
    {
        return 0;
    }

    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float a, float b, float c)
    {
        if (!IC2.platform.isSimulating())
        {
            return false;
        }
        else
        {
            ItemStack equipped = entityplayer.getCurrentEquippedItem();

            if (equipped == null)
            {
                return false;
            }
            else
            {
                if (equipped.getItem() == Item.dyePowder && equipped.getItemDamage() == 15)
                {
                    this.growTree(world, i, j, k, world.rand);

                    if (!entityplayer.capabilities.isCreativeMode)
                    {
                        --equipped.stackSize;
                    }

                    entityplayer.swingItem();
                }

                return false;
            }
        }
    }

    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
    }

    public EnumPlantType getPlantType(World world, int x, int y, int z)
    {
        return EnumPlantType.Plains;
    }
}
