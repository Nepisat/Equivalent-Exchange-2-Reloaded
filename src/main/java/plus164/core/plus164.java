package plus164.core;

import plus164.block.Airi;
import plus164.block.Cglass;
import plus164.block.cglassitem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(name="+1.6.4",modid="plus164",version="1.0")
public class plus164 {
	public static Block cglass;
	public static Item AirItem=new Airi(28000);
	public static int cglassid=4000;
	@EventHandler
	public void init(FMLInitializationEvent e){
		LanguageRegistry.addName(new ItemStack(cglass, 1, 0), "White Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 0), "ja_JP", "騾具ｽｽ豼ｶ?ｲ邵ｺ?ｮ豼ｶ?ｲ闔牙?窶ｳ郢ｧ?ｬ郢晢ｽｩ郢ｧ?ｹ");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 1), "Orange Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 1), "ja_JP", "隶門揃迚｡邵ｺ?ｮ豼ｶ?ｲ闔牙?窶ｳ郢ｧ?ｬ郢晢ｽｩ郢ｧ?ｹ");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 2), "Magenta Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 2), "ja_JP", "隘搾ｽ､驍擾ｽｫ豼ｶ?ｲ邵ｺ?ｮ豼ｶ?ｲ闔牙?窶ｳ郢ｧ?ｬ郢晢ｽｩ郢ｧ?ｹ");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 3), "Light Blue Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 3), "ja_JP", "驕ｨ?ｺ豼ｶ?ｲ邵ｺ?ｮ豼ｶ?ｲ闔牙?窶ｳ郢ｧ?ｬ郢晢ｽｩ郢ｧ?ｹ");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 4), "Yellow Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 4), "ja_JP", "魄滂ｿｽ迚｡邵ｺ?ｮ豼ｶ?ｲ闔牙?窶ｳ郢ｧ?ｬ郢晢ｽｩ郢ｧ?ｹ");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 5), "Lime Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 5), "ja_JP", "魄滂ｿｽ?ｷ鬘檎横邵ｺ?ｮ豼ｶ?ｲ闔牙?窶ｳ郢ｧ?ｬ郢晢ｽｩ郢ｧ?ｹ");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 6), "Pink Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 6), "ja_JP", "隴ｯ?ｽ迚｡邵ｺ?ｮ豼ｶ?ｲ闔牙?窶ｳ郢ｧ?ｬ郢晢ｽｩ郢ｧ?ｹ");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 7), "Gray Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 7), "ja_JP", "霓｣?ｰ豼ｶ?ｲ邵ｺ?ｮ豼ｶ?ｲ闔牙?窶ｳ郢ｧ?ｬ郢晢ｽｩ郢ｧ?ｹ");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 8), "Light Gray Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 8), "ja_JP", "髦ｮ?ｽ?ｽ豼ｶ?ｲ邵ｺ?ｮ豼ｶ?ｲ闔牙?窶ｳ郢ｧ?ｬ郢晢ｽｩ郢ｧ?ｹ");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 9), "Cyan Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 9), "ja_JP", "雎鯉ｽｴ豼ｶ?ｲ邵ｺ?ｮ豼ｶ?ｲ闔牙?窶ｳ郢ｧ?ｬ郢晢ｽｩ郢ｧ?ｹ");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 10), "Purple Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 10), "ja_JP", "驍擾ｽｫ豼ｶ?ｲ邵ｺ?ｮ豼ｶ?ｲ闔牙?窶ｳ郢ｧ?ｬ郢晢ｽｩ郢ｧ?ｹ");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 11), "Blue Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 11), "ja_JP", "鬮ｱ螳夂横邵ｺ?ｮ豼ｶ?ｲ闔牙?窶ｳ郢ｧ?ｬ郢晢ｽｩ郢ｧ?ｹ");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 12), "Brown Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 12), "ja_JP", "髣鯉ｽｶ豼ｶ?ｲ邵ｺ?ｮ豼ｶ?ｲ闔牙?窶ｳ郢ｧ?ｬ郢晢ｽｩ郢ｧ?ｹ");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 13), "Green Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 13), "ja_JP", "驍ｱ鬘檎横邵ｺ?ｮ豼ｶ?ｲ闔牙?窶ｳ郢ｧ?ｬ郢晢ｽｩ郢ｧ?ｹ");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 14), "Red Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 14), "ja_JP", "隘搾ｽ､豼ｶ?ｲ邵ｺ?ｮ豼ｶ?ｲ闔牙?窶ｳ郢ｧ?ｬ郢晢ｽｩ郢ｧ?ｹ");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 15), "Black Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 15), "ja_JP", "魄溷ｮ夂横邵ｺ?ｮ豼ｶ?ｲ闔牙?窶ｳ郢ｧ?ｬ郢晢ｽｩ郢ｧ?ｹ");
	}
	@EventHandler
	public void pinit(FMLPreInitializationEvent e){
		//郢晄じﾎ溽ｹ晢ｿｽ縺醍ｸｺ?ｮ騾具ｽｻ鬪ｭ?ｲ
		cglass = new Cglass(cglassid, Material.rock).setUnlocalizedName("ColorGlassBlock").setCreativeTab(CreativeTabs.tabBlock);
		GameRegistry.registerItem(AirItem, "AirItem");
		//GameRegistry.registerBlock(blockSample, <ItemBlock>.class, "blockSample");
		GameRegistry.registerBlock(cglass, cglassitem.class, "blockglasscolor");

	}
}
