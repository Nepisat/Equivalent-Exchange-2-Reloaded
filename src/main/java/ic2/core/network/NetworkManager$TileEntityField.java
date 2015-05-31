package ic2.core.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

public class NetworkManager$TileEntityField
{
    TileEntity te;
    String field;
    EntityPlayerMP target;

    final NetworkManager this$0;

    NetworkManager$TileEntityField(NetworkManager var1, TileEntity te, String field)
    {
        this.this$0 = var1;
        this.target = null;
        this.te = te;
        this.field = field;
    }

    NetworkManager$TileEntityField(NetworkManager var1, TileEntity te, String field, EntityPlayerMP target)
    {
        this.this$0 = var1;
        this.target = null;
        this.te = te;
        this.field = field;
        this.target = target;
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof NetworkManager$TileEntityField))
        {
            return false;
        }
        else
        {
            NetworkManager$TileEntityField tef = (NetworkManager$TileEntityField)obj;
            return tef.te == this.te && tef.field.equals(this.field);
        }
    }

    public int hashCode()
    {
        return this.te.hashCode() * 31 ^ this.field.hashCode();
    }
}
