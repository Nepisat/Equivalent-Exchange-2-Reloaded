package ic2.core.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2;
import ic2.core.block.TileEntityBlock$1;
import java.util.List;
import java.util.Vector;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;

public class TileEntityBlock extends TileEntity implements INetworkDataProvider, INetworkUpdateListener, IWrenchable
{
    private boolean active = false;
    private short facing = 0;
    public boolean prevActive = false;
    public short prevFacing = 0;
    public boolean loaded = false;
    @SideOnly(Side.CLIENT)
    private Icon[] lastRenderIcons;
    private int tesrMask;
    public int tesrTtl;
    private static final int defaultTesrTtl = 500;

    public void validate()
    {
        super.validate();
        IC2.addSingleTickCallback(super.worldObj, new TileEntityBlock$1(this));
    }

    public void invalidate()
    {
        if (this.loaded)
        {
            this.onUnloaded();
        }

        super.invalidate();
    }

    public void onChunkUnload()
    {
        if (this.loaded)
        {
            this.onUnloaded();
        }

        super.onChunkUnload();
    }

    public void onLoaded()
    {
        this.loaded = true;
    }

    public void onUnloaded()
    {
        this.loaded = false;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        this.prevFacing = this.facing = nbttagcompound.getShort("facing");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("facing", this.facing);
    }

    public final boolean canUpdate()
    {
        return false;
    }

    public boolean enableUpdateEntity()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void onRender()
    {
        Block block = this.getBlockType();

        if (this.lastRenderIcons == null)
        {
            this.lastRenderIcons = new Icon[6];
        }

        for (int side = 0; side < 6; ++side)
        {
            this.lastRenderIcons[side] = block.getBlockTexture(super.worldObj, super.xCoord, super.yCoord, super.zCoord, side);
        }

        this.tesrMask = 0;
    }

    public boolean getActive()
    {
        return this.active;
    }

    public void setActive(boolean active)
    {
        this.active = active;

        if (this.prevActive != active)
        {
            IC2.network.updateTileEntityField(this, "active");
        }

        this.prevActive = active;
    }

    public void setActiveWithoutNotify(boolean active)
    {
        this.active = active;
        this.prevActive = active;
    }

    public short getFacing()
    {
        return this.facing;
    }

    public List<String> getNetworkedFields()
    {
        Vector ret = new Vector(2);
        ret.add("active");
        ret.add("facing");
        return ret;
    }

    public void onNetworkUpdate(String field)
    {
        if (field.equals("active") && this.prevActive != this.active || field.equals("facing") && this.prevFacing != this.facing)
        {
            int reRenderMask = 0;
            Block block = this.getBlockType();

            if (this.lastRenderIcons == null)
            {
                reRenderMask = -1;
            }
            else
            {
                for (int side = 0; side < 6; ++side)
                {
                    Icon oldIcon = this.lastRenderIcons[side];

                    if (oldIcon instanceof BlockTextureStitched)
                    {
                        oldIcon = ((BlockTextureStitched)oldIcon).getRealTexture();
                    }

                    Icon newIcon = block.getBlockTexture(super.worldObj, super.xCoord, super.yCoord, super.zCoord, side);

                    if (newIcon instanceof BlockTextureStitched)
                    {
                        newIcon = ((BlockTextureStitched)newIcon).getRealTexture();
                    }

                    if (oldIcon != newIcon)
                    {
                        reRenderMask |= 1 << side;
                    }
                }
            }

            if (reRenderMask != 0)
            {
                if (reRenderMask >= 0 && this.prevFacing == this.facing && block.getRenderType() == IC2.platform.getRenderId("default"))
                {
                    this.tesrMask = reRenderMask;
                    this.tesrTtl = 500;
                }
                else
                {
                    super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
                }
            }

            this.prevActive = this.active;
            this.prevFacing = this.facing;
        }
    }

    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
    {
        return false;
    }

    public void setFacing(short facing)
    {
        this.facing = facing;

        if (this.prevFacing != facing)
        {
            IC2.network.updateTileEntityField(this, "facing");
        }

        this.prevFacing = facing;
    }

    public boolean wrenchCanRemove(EntityPlayer entityPlayer)
    {
        return true;
    }

    public float getWrenchDropRate()
    {
        return 1.0F;
    }

    public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
    {
        return new ItemStack(super.worldObj.getBlockId(super.xCoord, super.yCoord, super.zCoord), 1, super.worldObj.getBlockMetadata(super.xCoord, super.yCoord, super.zCoord));
    }

    public int getTesrMask()
    {
        return this.tesrMask;
    }

    public void onBlockBreak(int a, int b) {}
}
