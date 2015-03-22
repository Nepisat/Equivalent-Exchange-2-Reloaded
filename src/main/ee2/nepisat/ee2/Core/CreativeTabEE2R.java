package nepisat.ee2.Core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;

class CreativeTabEE2R extends CreativeTabs
{
    public CreativeTabEE2R(String par2Str) {
        super(par2Str);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	
    /**
     * the itemID for the item to be displayed on the tab
     */
    public int getTabIconItemIndex()
    {
        return EE2.Kenzyanoisi.itemID;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel()
	{
		return "EE2R";
	}
}