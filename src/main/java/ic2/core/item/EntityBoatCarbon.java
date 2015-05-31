package ic2.core.item;

import ic2.core.Ic2Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityBoatCarbon extends EntityIC2Boat
{
    public EntityBoatCarbon(World par1World)
    {
        super(par1World);
    }

    protected ItemStack getItem()
    {
        return Ic2Items.boatCarbon.copy();
    }

    protected double getBreakMotion()
    {
        return 0.4D;
    }

    protected void breakBoat(double motion)
    {
        this.entityDropItem(this.getItem(), 0.0F);
    }

    public String getTexture()
    {
        return "textures/models/boatCarbon.png";
    }
}
