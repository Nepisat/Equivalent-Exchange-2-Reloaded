package ic2.core.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderExplosiveBlock extends Render
{
    public RenderBlocks blockRenderer = new RenderBlocks();

    public RenderExplosiveBlock()
    {
        super.shadowSize = 0.5F;
    }

    public void render(EntityIC2Explosive entitytntprimed, double x, double y, double z, float f, float f1)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        float f3;

        if ((float)entitytntprimed.fuse - f1 + 1.0F < 10.0F)
        {
            f3 = 1.0F - ((float)entitytntprimed.fuse - f1 + 1.0F) / 10.0F;

            if (f3 < 0.0F)
            {
                f3 = 0.0F;
            }

            if (f3 > 1.0F)
            {
                f3 = 1.0F;
            }

            f3 *= f3;
            f3 *= f3;
            f3 = 1.0F + f3 * 0.3F;
            GL11.glScalef(f3, f3, f3);
        }

        f3 = (1.0F - ((float)entitytntprimed.fuse - f1 + 1.0F) / 100.0F) * 0.8F;
        this.bindTexture(TextureMap.locationBlocksTexture);
        this.blockRenderer.renderBlockAsItem(entitytntprimed.renderBlock, 0, entitytntprimed.getBrightness(f1));

        if (entitytntprimed.fuse / 5 % 2 == 0)
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, f3);
            this.blockRenderer.renderBlockAsItem(entitytntprimed.renderBlock, 0, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        GL11.glPopMatrix();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity entity, double x, double y, double z, float f, float f1)
    {
        this.render((EntityIC2Explosive)entity, x, y, z, f, f1);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return null;
    }
}
