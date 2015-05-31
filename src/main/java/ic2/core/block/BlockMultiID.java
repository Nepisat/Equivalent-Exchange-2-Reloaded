package ic2.core.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.init.InternalName;
import ic2.core.item.block.ItemBlockIC2;
import ic2.core.util.StackUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;

public abstract class BlockMultiID extends BlockContainerCommon
{
    public static final int[][] sideAndFacingToSpriteOffset = new int[][] {{3, 2, 0, 0, 0, 0}, {2, 3, 1, 1, 1, 1}, {1, 1, 3, 2, 5, 4}, {0, 0, 2, 3, 4, 5}, {4, 5, 4, 5, 3, 2}, {5, 4, 5, 4, 2, 3}};
    @SideOnly(Side.CLIENT)
    protected Icon[][] textures;
    public int renderMask = 63;

    public BlockMultiID(Configuration config, InternalName internalName, Material material)
    {
        super(config, internalName, material);
    }

    public BlockMultiID(Configuration config, InternalName internalName, Material material, Class <? extends ItemBlockIC2 > itemClass)
    {
        super(config, internalName, material, itemClass);
    }

    public String getTextureFolder()
    {
        return null;
    }

    public String getTextureName(int index)
    {
        Item item = Item.itemsList[super.blockID];

        if (!item.getHasSubtypes())
        {
            return index == 0 ? this.getUnlocalizedName() : null;
        }
        else
        {
            ItemStack itemStack = new ItemStack(this, 1, index);
            String ret = item.getUnlocalizedName(itemStack);
            return ret == null ? null : ret.replace("item", "block");
        }
    }

    public int getTextureIndex(int meta)
    {
        return meta;
    }

    public int getFacing(int meta)
    {
        return 3;
    }

    public int getFacing(IBlockAccess iBlockAccess, int x, int y, int z)
    {
        TileEntity te = iBlockAccess.getBlockTileEntity(x, y, z);

        if (te instanceof TileEntityBlock)
        {
            return ((TileEntityBlock)te).getFacing();
        }
        else
        {
            int meta = iBlockAccess.getBlockMetadata(x, y, z);
            return this.getFacing(meta);
        }
    }

    public final int getTextureSubIndex(int side, int facing)
    {
        return this.getTextureSubIndex(side, facing, false);
    }

    public final int getTextureSubIndex(int side, int facing, boolean active)
    {
        int ret = sideAndFacingToSpriteOffset[side][facing];
        return active ? ret + 6 : ret;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        int metaCount;

        for (metaCount = 0; this.getTextureName(metaCount) != null; ++metaCount)
        {
            ;
        }

        this.textures = new Icon[metaCount][12];
        String textureFolder = this.getTextureFolder() == null ? "" : this.getTextureFolder() + "/";

        for (int index = 0; index < metaCount; ++index)
        {
            String name = "ic2:" + textureFolder + this.getTextureName(index);

            for (int active = 0; active < 2; ++active)
            {
                for (int side = 0; side < 6; ++side)
                {
                    int subIndex = active * 6 + side;
                    String subName = name + ":" + subIndex;
                    BlockTextureStitched texture = new BlockTextureStitched(subName, subIndex);
                    this.textures[index][subIndex] = texture;
                    ((TextureMap)iconRegister).setTextureEntry(subName, texture);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z, int side)
    {
        int facing = this.getFacing(iBlockAccess, x, y, z);
        boolean active = isActive(iBlockAccess, x, y, z);
        int meta = iBlockAccess.getBlockMetadata(x, y, z);
        int index = this.getTextureIndex(meta);
        int subIndex = this.getTextureSubIndex(side, facing, active);

        if (index >= this.textures.length)
        {
            return null;
        }
        else
        {
            try
            {
                return this.textures[index][subIndex];
            }
            catch (Exception var12)
            {
                IC2.platform.displayError(var12, "Coordinates: " + x + "/" + y + "/" + z + "\n" + "Side: " + side + "\n" + "Block: " + this + "\n" + "Meta: " + meta + "\n" + "Facing: " + facing + "\n" + "Active: " + active + "\n" + "Index: " + index + "\n" + "SubIndex: " + subIndex);
                return null;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
        int facing = this.getFacing(meta);
        int index = this.getTextureIndex(meta);
        int subIndex = this.getTextureSubIndex(side, facing);

        if (index >= this.textures.length)
        {
            return null;
        }
        else
        {
            try
            {
                return this.textures[index][subIndex];
            }
            catch (Exception var7)
            {
                IC2.platform.displayError(var7, "Side: " + side + "\n" + "Block: " + this + "\n" + "Meta: " + meta + "\n" + "Facing: " + facing + "\n" + "Index: " + index + "\n" + "SubIndex: " + subIndex);
                return null;
            }
        }
    }

    public String getUnlocalizedName()
    {
        return super.getUnlocalizedName().substring(5);
    }

    public int getRenderType()
    {
        return IC2.platform.getRenderId("default");
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return (this.renderMask & 1 << side) != 0 ? super.shouldSideBeRendered(blockAccess, x, y, z, side) : false;
    }

    @SideOnly(Side.CLIENT)
    public void onRender(IBlockAccess blockAccess, int x, int y, int z)
    {
        TileEntity te = blockAccess.getBlockTileEntity(x, y, z);

        if (te instanceof TileEntityBlock)
        {
            ((TileEntityBlock)te).onRender();
        }
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float a, float b, float c)
    {
        if (entityPlayer.isSneaking())
        {
            return false;
        }
        else
        {
            TileEntity te = world.getBlockTileEntity(x, y, z);
            return te instanceof IHasGui ? (IC2.platform.isSimulating() ? IC2.platform.launchGui(entityPlayer, (IHasGui)te) : true) : false;
        }
    }

    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList ret = super.getBlockDropped(world, x, y, z, metadata, fortune);
        TileEntity te = world.getBlockTileEntity(x, y, z);

        if (te instanceof IInventory)
        {
            IInventory inv = (IInventory)te;

            for (int i = 0; i < inv.getSizeInventory(); ++i)
            {
                ItemStack itemStack = inv.getStackInSlot(i);

                if (itemStack != null)
                {
                    ret.add(itemStack);
                    inv.setInventorySlotContents(i, (ItemStack)null);
                }
            }
        }

        return ret;
    }

    public void breakBlock(World world, int x, int y, int z, int a, int b)
    {
        TileEntity te = world.getBlockTileEntity(x, y, z);

        if (te instanceof TileEntityBlock)
        {
            ((TileEntityBlock)te).onBlockBreak(a, b);
        }

        boolean firstItem = true;
        Iterator it = this.getBlockDropped(world, x, y, z, world.getBlockMetadata(x, y, z), 0).iterator();

        while (it.hasNext())
        {
            ItemStack itemStack = (ItemStack)it.next();

            if (firstItem)
            {
                firstItem = false;
            }
            else
            {
                StackUtil.dropAsEntity(world, x, y, z, itemStack);
            }
        }

        super.breakBlock(world, x, y, z, a, b);
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityliving, ItemStack itemStack)
    {
        if (IC2.platform.isSimulating())
        {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

            if (tileEntity instanceof IWrenchable)
            {
                IWrenchable te = (IWrenchable)tileEntity;

                if (entityliving == null)
                {
                    te.setFacing((short)2);
                }
                else
                {
                    int l = MathHelper.floor_double((double)(entityliving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

                    switch (l)
                    {
                        case 0:
                            te.setFacing((short)2);
                            break;

                        case 1:
                            te.setFacing((short)5);
                            break;

                        case 2:
                            te.setFacing((short)3);
                            break;

                        case 3:
                            te.setFacing((short)4);
                    }
                }
            }
        }
    }

    public void onBlockPreDestroy(World world, int x, int y, int z, int meta)
    {
        super.onBlockPreDestroy(world, x, y, z, meta);
        TileEntity te = world.getBlockTileEntity(x, y, z);

        if (te instanceof TileEntityBlock)
        {
            TileEntityBlock teb = (TileEntityBlock)te;

            if (teb.loaded)
            {
                teb.onUnloaded();
            }
        }
    }

    public static boolean isActive(IBlockAccess iblockaccess, int i, int j, int k)
    {
        TileEntity te = iblockaccess.getBlockTileEntity(i, j, k);
        return te instanceof TileEntityBlock ? ((TileEntityBlock)te).getActive() : false;
    }

    public void getSubBlocks(int j, CreativeTabs tabs, List itemList)
    {
        Item item = Item.itemsList[super.blockID];

        if (!item.getHasSubtypes())
        {
            itemList.add(new ItemStack(this));
        }
        else
        {
            for (int i = 0; i < 16; ++i)
            {
                ItemStack is = new ItemStack(this, 1, i);

                if (Item.itemsList[super.blockID].getUnlocalizedName(is) == null)
                {
                    break;
                }

                itemList.add(is);
            }
        }
    }

    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        return new ItemStack(this, 1, world.getBlockMetadata(x, y, z));
    }

    public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis)
    {
        if (axis == ForgeDirection.UNKNOWN)
        {
            return false;
        }
        else
        {
            TileEntity tileEntity = worldObj.getBlockTileEntity(x, y, z);

            if (tileEntity instanceof IWrenchable)
            {
                IWrenchable te = (IWrenchable)tileEntity;
                int newFacing = ForgeDirection.getOrientation(te.getFacing()).getRotation(axis).ordinal();

                if (te.wrenchCanSetFacing((EntityPlayer)null, newFacing))
                {
                    te.setFacing((short)newFacing);
                }
            }

            return false;
        }
    }
}
