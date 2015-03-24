package core;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import block.Cglass;


import block.cglassitem;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(name="+1.6.4",modid="plus164",version="1.0")
public class plus164 {
	public static Block cglass;
	public static int cglassid=4000;
	@EventHandler
	public void init(FMLInitializationEvent e){
		LanguageRegistry.addName(new ItemStack(cglass, 1, 0), "White Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 0), "ja_JP", "白色の色付きガラス");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 1), "Orange Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 1), "ja_JP", "橙色の色付きガラス");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 2), "Magenta Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 2), "ja_JP", "赤紫色の色付きガラス");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 3), "Light Blue Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 3), "ja_JP", "空色の色付きガラス");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 4), "Yellow Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 4), "ja_JP", "黄色の色付きガラス");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 5), "Lime Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 5), "ja_JP", "黄緑色の色付きガラス");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 6), "Pink Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 6), "ja_JP", "桃色の色付きガラス");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 7), "Gray Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 7), "ja_JP", "灰色の色付きガラス");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 8), "Light Gray Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 8), "ja_JP", "薄灰色の色付きガラス");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 9), "Cyan Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 9), "ja_JP", "水色の色付きガラス");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 10), "Purple Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 10), "ja_JP", "紫色の色付きガラス");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 11), "Blue Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 11), "ja_JP", "青色の色付きガラス");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 12), "Brown Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 12), "ja_JP", "茶色の色付きガラス");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 13), "Green Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 13), "ja_JP", "緑色の色付きガラス");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 14), "Red Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 14), "ja_JP", "赤色の色付きガラス");
		LanguageRegistry.addName(new ItemStack(cglass, 1, 15), "Black Stained Glass");
		LanguageRegistry.instance().addNameForObject(new ItemStack(cglass, 1, 15), "ja_JP", "黒色の色付きガラス");
	}
	@EventHandler
	public void pinit(FMLPreInitializationEvent e){
		//ブロックの登録
		cglass = new Cglass(cglassid, Material.rock).setUnlocalizedName("ColorGlassBlock").setCreativeTab(CreativeTabs.tabBlock);
		//GameRegistry.registerBlock(blockSample, <ItemBlock>.class, "blockSample");
		GameRegistry.registerBlock(cglass, cglassitem.class, "blockglasscolor");

	}
}
