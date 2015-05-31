package ic2.core.block;

import ic2.core.IC2;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityWall extends TileEntityBlock
{
    public int[] retextureRefId;
    public int[] retextureRefMeta;
    public int[] retextureRefSide;

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        this.retextureRefId = nbttagcompound.getIntArray("retextureRefId");
        this.retextureRefMeta = nbttagcompound.getIntArray("retextureRefMeta");
        this.retextureRefSide = nbttagcompound.getIntArray("retextureRefSide");

        if (this.retextureRefId.length != 6)
        {
            this.retextureRefId = null;
        }

        if (this.retextureRefMeta.length != 6)
        {
            this.retextureRefMeta = null;
        }

        if (this.retextureRefSide.length != 6)
        {
            this.retextureRefSide = null;
        }
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);

        if (this.retextureRefId != null)
        {
            nbttagcompound.setIntArray("retextureRefId", this.retextureRefId);
            nbttagcompound.setIntArray("retextureRefMeta", this.retextureRefMeta);
            nbttagcompound.setIntArray("retextureRefSide", this.retextureRefSide);
        }
    }

    public List<String> getNetworkedFields()
    {
        ArrayList ret = new ArrayList();
        ret.add("retextureRefId");
        ret.add("retextureRefMeta");
        ret.add("retextureRefSide");
        ret.addAll(super.getNetworkedFields());
        return ret;
    }

    public void onNetworkUpdate(String field)
    {
        if (field.equals("retextureRefId") || field.equals("retextureRefMeta") || field.equals("retextureRefSide"))
        {
            super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
        }

        super.onNetworkUpdate(field);
    }

    public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
    {
        return null;
    }

    public boolean retexture(int side, int referencedBlockId, int referencedMeta, int referencedSide)
    {
        boolean ret = false;
        boolean updateAll = false;

        if (this.retextureRefId == null)
        {
            this.retextureRefId = new int[6];
            this.retextureRefMeta = new int[6];
            this.retextureRefSide = new int[6];
            updateAll = true;
        }

        if (this.retextureRefId[side] != referencedBlockId || updateAll)
        {
            this.retextureRefId[side] = referencedBlockId;
            IC2.network.updateTileEntityField(this, "retextureRefId");
            ret = true;
        }

        if (this.retextureRefMeta[side] != referencedMeta || updateAll)
        {
            this.retextureRefMeta[side] = referencedMeta;
            IC2.network.updateTileEntityField(this, "retextureRefMeta");
            ret = true;
        }

        if (this.retextureRefSide[side] != referencedSide || updateAll)
        {
            this.retextureRefSide[side] = referencedSide;
            IC2.network.updateTileEntityField(this, "retextureRefSide");
            ret = true;
        }

        return ret;
    }
}
