package ic2.core.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.IC2;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.Resource;
import net.minecraft.client.resources.ResourceManager;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class BlockTextureStitched extends TextureAtlasSprite
{
    private BufferedImage comparisonImage;
    private TextureAtlasSprite mappedTexture;
    private final int subIndex;
    private static Map<String, BufferedImage> cachedImages = new HashMap();
    private static Map<Integer, List<BlockTextureStitched>> existingTextures = new HashMap();

    public BlockTextureStitched(String name, int subIndex)
    {
        super(name);
        this.subIndex = subIndex;
    }

    public void copyFrom(TextureAtlasSprite textureStitched)
    {
        if (textureStitched.getIconName().equals("missingno") && this.mappedTexture != null)
        {
            super.copyFrom(this.mappedTexture);
        }
        else
        {
            super.copyFrom(textureStitched);
        }
    }

    public void updateAnimation() {}

    public boolean load(ResourceManager manager, ResourceLocation location) throws IOException
    {
        String name = location.getResourcePath();
        int index = name.indexOf(58);

        if (index != -1)
        {
            int extStart = name.lastIndexOf(46);
            location = new ResourceLocation(location.getResourceDomain(), name.substring(0, index) + name.substring(extStart));
        }

        return this.loadSubImage(manager.getResource(location));
    }

    public boolean loadSubImage(Resource res) throws IOException
    {
        String name = this.getIconName();
        BufferedImage bufferedImage = (BufferedImage)cachedImages.get(name);

        if (bufferedImage == null)
        {
            bufferedImage = ImageIO.read(res.getInputStream());
            cachedImages.put(name, bufferedImage);
        }

        int size = bufferedImage.getHeight();
        int count = bufferedImage.getWidth() / size;
        int index = this.subIndex;

        if (count != 1 && count != 6 && count != 12)
        {
            if (count != 2)
            {
                IC2.log.warning("texture " + name + " is not properly sized");
                throw new IOException();
            }

            index /= 6;
        }
        else
        {
            index %= count;
        }

        super.width = size;
        super.height = size;
        this.comparisonImage = bufferedImage.getSubimage(index * size, 0, size, size);
        int[] rgbaData = new int[size * size];
        this.comparisonImage.getRGB(0, 0, size, size, rgbaData, 0, size);
        int hash = Arrays.hashCode(rgbaData);
        List matchingTextures = (List)existingTextures.get(Integer.valueOf(hash));

        if (matchingTextures != null)
        {
            int[] matchingTextures1 = new int[size * size];
            Iterator i$ = matchingTextures.iterator();

            while (i$.hasNext())
            {
                BlockTextureStitched matchingTexture = (BlockTextureStitched)i$.next();

                if (matchingTexture.comparisonImage.getWidth() == size)
                {
                    matchingTexture.comparisonImage.getRGB(0, 0, size, size, matchingTextures1, 0, size);

                    if (Arrays.equals(rgbaData, matchingTextures1))
                    {
                        this.mappedTexture = matchingTexture;
                        return false;
                    }
                }
            }

            matchingTextures.add(this);
        }
        else
        {
            ArrayList matchingTextures11 = new ArrayList();
            matchingTextures11.add(this);
            existingTextures.put(Integer.valueOf(hash), matchingTextures11);
        }

        super.framesTextureData.add(rgbaData);
        return true;
    }

    public Icon getRealTexture()
    {
        return (Icon)(this.mappedTexture == null ? this : this.mappedTexture);
    }

    public static void onPostStitch()
    {
        Iterator i$ = existingTextures.values().iterator();

        while (i$.hasNext())
        {
            List textures = (List)i$.next();
            BlockTextureStitched texture;

            for (Iterator i$1 = textures.iterator(); i$1.hasNext(); texture.comparisonImage = null)
            {
                texture = (BlockTextureStitched)i$1.next();
            }
        }

        cachedImages.clear();
        existingTextures.clear();
    }
}
