package ic2.core.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class TextureIndexClient extends TextureIndex
{
    private final Map<String, List<Integer>> textureIndexes = new HashMap();

    public int get(int blockId, int index)
    {
        return -1;
    }

    public void reset()
    {
        this.textureIndexes.clear();
    }
}
