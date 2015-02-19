package com.demoxin.minecraft.tmc.data;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import com.demoxin.minecraft.tmc.util.ColorHelper;

public class OreStorage implements IResourceManagerReloadListener
{
	public List<Ore> storage;
	
	public OreStorage()
	{
		storage = new ArrayList<Ore>();
	}
	
	public void loadOreDictionary()
	{
		String[] oreNames = OreDictionary.getOreNames();
		int meta = 0;
		
		for(String oreName : oreNames)
		{
			if(!oreName.contains("ingot"))
				continue;
			
			String baseName = oreName.replace("ingot", "");
			
			ArrayList<ItemStack> ingots = OreDictionary.getOres("ingot" + baseName);
			ArrayList<ItemStack> nuggets = OreDictionary.getOres("nugget" + baseName);
			ArrayList<ItemStack> ores = OreDictionary.getOres("ore" + baseName);
			ArrayList<ItemStack> blocks = OreDictionary.getOres("block" + baseName);
			
			if(ingots.isEmpty() || nuggets.isEmpty())
				continue;
			
			if(ores.isEmpty() && blocks.isEmpty())
				continue;
			
			Ore newOre = new Ore();
			newOre.name = baseName;
			newOre.ingot = new ItemStack(ingots.get(0).getItem(), 1, ingots.get(0).getItemDamage());
			newOre.nugget = new ItemStack(nuggets.get(0).getItem(), 1, nuggets.get(0).getItemDamage());
			
			if(!ores.isEmpty())
				newOre.ore = new ItemStack(ores.get(0).getItem(), 1, ores.get(0).getItemDamage());
			else
				newOre.ore = null;
			
			if(!blocks.isEmpty())
				newOre.block = new ItemStack(blocks.get(0).getItem(), 1, blocks.get(0).getItemDamage());
			else
				newOre.block = null;
			
			newOre.meta = meta++;
			
			storage.add(newOre);
		}
		rebuildNames();
		rebuildColors();
	}
	
	public void rebuildColors()
	{
		for(Ore ore : storage)
		{
			ArrayList<ItemStack> entries = OreDictionary.getOres("ingot" + ore.name);
			if(entries.isEmpty())
			{
				ore.color = new Color(0xFFFFFF);
				continue;
			}
			ore.color = ColorHelper.getAverageColor(entries);
			ore.glowy = false;
			for(ItemStack entry : entries)
			{
				if(entry.hasEffect(0))
					ore.glowy = true;
			}
		}
	}
	
	public void rebuildNames()
	{
		for(Ore ore : storage)
		{
			if(ore.ingot == null)
			{
				ore.prettyName = "Unknown";
				continue;
			}
			
			String itemStackName = ("" + StatCollector.translateToLocal(ore.ingot.getItem().getUnlocalizedNameInefficiently(ore.ingot) + ".name"));
			itemStackName = itemStackName.replace("" + StatCollector.translateToLocal("tmc.langhelpers.matcher.ingot") + "", "");
			ore.prettyName = itemStackName.trim();
			if(ore.prettyName == null || ore.prettyName.isEmpty())
				ore.prettyName = "PROBLEM";
		}
	}
	
	public List<Ore> getStorage()
	{
		return this.storage;
	}
	
	public static class Ore
	{
		public Color color;
		public String name;
		public String prettyName;
		public int meta;
		public ItemStack ingot;
		public ItemStack nugget;
		public ItemStack ore;
		public ItemStack block;
		public boolean glowy;
		
	}
	
	public Ore getOreByName(String name)
	{
		for(Ore ore : storage)
		{
			if(ore.name.equals(name))
				return ore;
		}
		return null;
	}
	
	public Ore getOreByMeta(int meta)
	{
		for(Ore ore : storage)
		{
			if(ore.meta == meta)
				return ore;
		}
		return null;
	}
	
	public int getMetaForOre(String ore)
	{
		for(Ore storedOre : storage)
		{
			if(ore.equals(storedOre.name))
				return storedOre.meta;
		}
		return 0;
	}
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		rebuildColors();
		rebuildNames();
	}
}
