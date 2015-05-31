package ic2.core.block.personal;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class TileEntityPersonalChestRenderer extends TileEntitySpecialRenderer
{
    private final ModelPersonalChest model = new ModelPersonalChest();
    private static final ResourceLocation texture = new ResourceLocation("ic2", "textures/models/newsafe.png");

    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTick)
    {
        if (tile instanceof TileEntityPersonalChest)
        {
            TileEntityPersonalChest safe = (TileEntityPersonalChest)tile;
            this.bindTexture(texture);
            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef((float)x, (float)y + 1.0F, (float)z + 1.0F);
            GL11.glScalef(1.0F, -1.0F, -1.0F);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            short angle;

            switch (safe.getFacing())
            {
                case 2:
                    angle = 180;
                    break;

                case 3:
                default:
                    angle = 0;
                    break;

                case 4:
                    angle = 90;
                    break;

                case 5:
                    angle = -90;
            }

            GL11.glRotatef((float)angle, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float lidAngle = safe.prevLidAngle + (safe.lidAngle - safe.prevLidAngle) * partialTick;
            lidAngle = 1.0F - lidAngle;
            lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;
            this.model.door.rotateAngleY = lidAngle * (float)Math.PI / 2.0F;
            this.model.renderAll();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
