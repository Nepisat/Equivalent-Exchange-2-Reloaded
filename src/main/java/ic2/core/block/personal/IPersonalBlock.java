package ic2.core.block.personal;

import net.minecraft.entity.player.EntityPlayer;

public interface IPersonalBlock
{
    boolean permitsAccess(EntityPlayer var1);

    boolean permitsAccess(String var1);

    String getUsername();
}
