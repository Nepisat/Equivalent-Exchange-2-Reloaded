package ic2.core.item;

import ic2.core.Ic2Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityBoatRubber extends EntityIC2Boat
{
    public EntityBoatRubber(World par1World)
    {
        super(par1World);
    }

    protected ItemStack getItem()
    {
        return Ic2Items.boatRubber.copy();
    }

    protected double getBreakMotion()
    {
        return 0.23D;
    }

    protected void breakBoat(double motion)
    {
        this.playSound("random.pop", 16.0F, 8.0F);
        this.entityDropItem((motion > 0.26D ? Ic2Items.boatRubberBroken : Ic2Items.boatRubber).copy(), 0.0F);
    }

    public String getTexture()
    {
        return "textures/models/boatRubber.png";
    }
}
