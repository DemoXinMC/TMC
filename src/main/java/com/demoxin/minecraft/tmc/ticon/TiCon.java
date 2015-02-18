package com.demoxin.minecraft.tmc.ticon;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import tterrag.core.common.compat.CompatabilityRegistry;
import tterrag.core.common.compat.ICompatability;
import tterrag.core.common.util.CreativeTabsCustom;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

public class TiCon implements ICompatability
{
	public static Item oreberryBerry;
	public static Block oreberryBush;
	public static CreativeTabs creativeTab;
	
	private static TiCon INSTANCE;
	
	public static void load()
	{
		if(INSTANCE == null)
			INSTANCE = new TiCon();
		
		switch(CompatabilityRegistry.INSTANCE.getState())
		{
			case INIT:
				INSTANCE.init();
				break;
			case POSTINIT:
				INSTANCE.postInit();
				break;
			case PREINIT:
				INSTANCE.preInit();
				break;
			default:
				break;
		}
	}
	
	public void preInit()
	{
		creativeTab = new CreativeTabsCustom("tmc.ticon");
		oreberryBerry = new ItemOreberry();
		oreberryBush = new BlockOreberry();
		GameRegistry.registerItem(oreberryBerry, "tmc.ticon.item.oreberry");
		GameRegistry.registerBlock(oreberryBush, ItemBlockOreberry.class, "tmc.ticon.block.oreberry");
		((CreativeTabsCustom) creativeTab).setDisplay(Item.getItemFromBlock(oreberryBush));
		GameRegistry.registerTileEntity(TileEntityOreberry.class, "tmc.ticon.oreberry");
	}
	
	public void init()
	{
		((ItemOreberry) oreberryBerry).oreDicting();
		((ItemOreberry) oreberryBerry).smelting();
	}
	
	public void postInit()
	{
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT)
			registerRenderers();
	}
	
	public void registerRenderers()
	{
		
	}
}
