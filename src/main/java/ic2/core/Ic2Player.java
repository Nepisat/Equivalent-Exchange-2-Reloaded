package ic2.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class Ic2Player extends EntityPlayer
{
    public Ic2Player(World world)
    {
        super(world, "[IC2]");
    }

    public boolean canCommandSenderUseCommand(int i, String s)
    {
        return false;
    }

    public ChunkCoordinates getPlayerCoordinates()
    {
        return null;
    }

    public void sendChatToPlayer(ChatMessageComponent chatmessagecomponent) {}
}
