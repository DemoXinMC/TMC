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
            
            if(ingots.isEmpty() || nuggets.isEmpty())
                continue;
            
            Ore newOre = new Ore();
            newOre.name = baseName;
            newOre.ingot = ingots.get(0).copy();
            newOre.nugget = nuggets.get(0).copy();
            
            if(!ores.isEmpty())
                newOre.ore = ores.get(0);
            else
                newOre.ore = null;
            
            newOre.meta = meta++;
        }
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
            itemStackName = itemStackName.replace(StatCollector.translateToLocal("tmc.langhelpers.matcher.ingot"), "").trim();
            ore.prettyName = itemStackName;
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
        public boolean glowy;
    }

    public Ore getOreByName(String name)
    {
        for(Ore ore : storage)
        {
            if(ore.name == name)
                return ore;
        }
        return null;
    }
    
    public int getMetaForOre(String ore)
    {
        for(Ore storedOre : storage)
        {
            if(ore == storedOre.name)
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
