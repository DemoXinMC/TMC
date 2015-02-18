package com.demoxin.minecraft.tmc.ticon;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import tterrag.core.common.util.CreativeTabsCustom;

import com.demoxin.minecraft.tmc.util.InitableCompatability;

public class TiCon extends InitableCompatability
{
	public static Item oreberryBerry;
	public static Block oreberryBush;
	public static CreativeTabs creativeTab;
	
	public void preInit()
	{
	    creativeTab = new CreativeTabsCustom("tmc.ticon");
		oreberryBerry = new ItemOreberry();
		oreberryBush = new BlockOreberry();
		((CreativeTabsCustom)creativeTab).setDisplay(Item.getItemFromBlock(oreberryBush));
	}
	
	public void init()
	{
	    ((ItemOreberry)oreberryBerry).oreDicting();
	    ((ItemOreberry)oreberryBerry).smelting();
	}
}
