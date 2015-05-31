package ic2.core.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderIC2Boat extends RenderBoat
{
    protected ResourceLocation getBoatTextures(EntityBoat entity)
    {
        return new ResourceLocation("ic2", ((EntityIC2Boat)entity).getTexture());
    }
}
