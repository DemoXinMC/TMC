package com.demoxin.minecraft.tmc.data;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;
import tterrag.core.common.Handlers.Handler;
import tterrag.core.common.Handlers.Handler.HandlerType;

import com.demoxin.minecraft.tmc.util.ColorHelper;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Handler(HandlerType.FORGE)
public class OreStorage implements IResourceManagerReloadListener
{
    protected List<Ore> storage;
    protected List<Ore> tempStorage;
    protected int nextMeta;
    
    public OreStorage()
    {
        storage = new ArrayList<Ore>();
        tempStorage = new ArrayList<Ore>();
        nextMeta = 0;
    }
    
    @SubscribeEvent
    public void oreDictEventHandler(OreRegisterEvent event)
    {
        System.out.println("Ore Registration!");
        String oreName = event.Name;
        if(!oreName.contains("ingot") && !oreName.contains("nugget") && !oreName.contains("ore"))
            return;
        
        String baseName = oreName.replace("ingot", "");
        baseName = baseName.replace("nugget", "");
        baseName = baseName.replace("ore", "");
        
        Ore storedOre = getOreByName(baseName);
        if(storedOre != null)
        {
            if(storedOre.block == null)
                if(oreName.equals("block" + storedOre.name))
                    storedOre.block = event.Ore.copy();
            
            if(storedOre.ore == null)
                if(oreName.equals("ore" + storedOre.name))
                    storedOre.ore = event.Ore.copy();
            
            return;
        }
        
        Ore tempOre = null;
        for(Ore ore : tempStorage)
        {
            if(ore.name.equals(baseName))
                tempOre = ore;
        }
        if(tempOre == null)
        {
            tempOre = new Ore();
            tempOre.name = baseName;
            tempStorage.add(tempOre);
        }
        
        if(oreName.equals("ingot" + tempOre.name) && tempOre.ingot == null)
            tempOre.ingot = event.Ore.copy();
        
        if(oreName.equals("nugget" + tempOre.name) && tempOre.nugget == null)
            tempOre.nugget = event.Ore.copy();
        
        if(oreName.equals("ore" + tempOre.name) && tempOre.ore == null)
            tempOre.ore = event.Ore.copy();
        
        if(oreName.equals("block" + tempOre.name) && tempOre.ore == null)
            tempOre.block = event.Ore.copy();
        
        if(tempOre.ingot != null && tempOre.nugget != null && (tempOre.ore != null || tempOre.block != null))
        {
            tempOre.meta = nextMeta++;
            storage.add(tempOre);
            tempStorage.remove(tempOre);
            rebuildNames();
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
