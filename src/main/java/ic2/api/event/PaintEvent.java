package ic2.api.event;

import net.minecraft.world.World;
import net.minecraftforge.event.Cancelable;
import net.minecraftforge.event.world.WorldEvent;

@Cancelable
public class PaintEvent extends WorldEvent
{
    public final int x;
    public final int y;
    public final int z;
    public final int side;
    public final int color;
    public boolean painted = false;

    public PaintEvent(World world, int x, int y, int z, int side, int color)
    {
        super(world);
        this.x = x;
        this.y = y;
        this.z = z;
        this.side = side;
        this.color = color;
    }
}
