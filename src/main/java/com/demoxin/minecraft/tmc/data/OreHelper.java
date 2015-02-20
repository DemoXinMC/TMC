package com.demoxin.minecraft.tmc.data;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

import com.demoxin.minecraft.tmc.util.ColorHelper;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class OreHelper implements IResourceManagerReloadListener
{
    public static OreHelper INSTANCE;
    
    protected List<Ore> oreStorage;
    protected List<Alloy> alloyStorage;
    protected List<Ore> tempStorage;
    protected int nextMeta;
    
    protected Map<Block, ArrayList<IMaterialHelper>> materialHelpers;
    
    public OreHelper()
    {
        oreStorage = new ArrayList<Ore>();
        alloyStorage = new ArrayList<Alloy>();
        tempStorage = new ArrayList<Ore>();
        nextMeta = 0;
        MinecraftForge.EVENT_BUS.register(this);
        INSTANCE = this;
    }
    
    public void loadOreDictionary()
    {
        String[] oreNames = OreDictionary.getOreNames();
        for(String oreName : oreNames)
        {
            ArrayList<ItemStack> entries = OreDictionary.getOres(oreName);
            
            if(entries.isEmpty())
                continue;
            
            OreRegisterEvent event = new OreRegisterEvent(oreName, entries.get(0));
            oreDictEventHandler(event);
        }
    }
    
    @SubscribeEvent
    public void oreDictEventHandler(OreRegisterEvent event)
    {
        String oreName = event.Name;
        oreName = oreName.replace("Aluminium", "Aluminum");
        oreName = oreName.replace("aluminium", "aluminum");
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
        
        if(tempOre.ingot != null && tempOre.nugget != null)
        {
            tempOre.meta = nextMeta++;
            oreStorage.add(tempOre);
            tempStorage.remove(tempOre);
            rebuildNames();
        }
    }
    
    public void addAlloy(String ore, String... materials)
    {
        alloyStorage.add(new Alloy(ore, materials));
    }
    
    public void rebuildColors()
    {
        for(Ore ore : oreStorage)
        {
            ArrayList<ItemStack> entries = OreDictionary.getOres("ingot" + ore.name);
            if(entries.isEmpty())
            {
                ore.color = new Color(0xFFFFFF);
                continue;
            }
            ore.color = ColorHelper.getAverageColor(entries);
            ore.glowy = false;
            ore.rarity = EnumRarity.common;
            for(ItemStack entry : entries)
            {
                if(entry.hasEffect(0))
                    ore.glowy = true;

                if(entry.getRarity().ordinal() > ore.rarity.ordinal())
                    ore.rarity = entry.getRarity();
            }
        }
    }
    
    public void rebuildNames()
    {
        for(Ore ore : oreStorage)
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
        return this.oreStorage;
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
        public EnumRarity rarity;
        
        public String getMaterial()
        {
            return this.name.toLowerCase();
        }
    }
    
    public static class Alloy
    {
        protected String ore;        
        protected ArrayList<String> materials;
        
        Alloy(String ore, ArrayList<String> materials)
        {
            this.ore = ore;
            this.materials = materials;
        }
        
        Alloy(String ore, String... materials)
        {
            this.ore = ore;
            this.materials = new ArrayList<String>();
            for(String material : materials)
                this.materials.add(material.toLowerCase());
        }
        
        public Ore getOre()
        {
            return OreHelper.INSTANCE.getOreByName(ore);
        }
        
        public void addMaterial(String material)
        {
            materials.add(material);
        }
        
        public boolean containsMaterial(String material)
        {
            for(String testMaterial : materials)
            {
                if(testMaterial.equals(material))
                    return true;
            }
            return false;
        }
        
        public boolean matches(ArrayList<String> materials)
        {
            if(getOre() == null)
                return false;
            for(String material : this.materials)
            {
                if(!materials.contains(material))
                    return false;
            }
            return true;
        }
        
        public ArrayList<String> getMaterials()
        {
            return new ArrayList<String>(materials);
        }
    }
    
    public Ore getOreByName(String name)
    {
        String oreName = name;
        oreName = oreName.replace("Aluminium", "Aluminum");
        oreName = oreName.replace("aluminium", "aluminum");
        for(Ore ore : oreStorage)
        {
            if(ore.name.equalsIgnoreCase(oreName))
                return ore;
        }
        return null;
    }
    
    public Ore getOreByMeta(int meta)
    {
        for(Ore ore : oreStorage)
        {
            if(ore.meta == meta)
                return ore;
        }
        return null;
    }
    
    public int getMetaForOre(String ore)
    {
        for(Ore storedOre : oreStorage)
        {
            if(ore.equals(storedOre.name))
                return storedOre.meta;
        }
        return 0;
    }
    
    public ArrayList<Alloy> getAlloysForMaterials(ArrayList<String> materials)
    {
        ArrayList<Alloy> results = new ArrayList<Alloy>();
        for(Alloy alloy : alloyStorage)
        {
            if(alloy.matches(materials))
                results.add(alloy);
        }
        return results;
    }
    
    public ArrayList<String> getMaterials(IBlockAccess blockAccess, int x, int y, int z)
    {
        Block block = blockAccess.getBlock(x, y, z);
        
        if(!materialHelpers.containsKey(block))
            return new ArrayList<String>();
        ArrayList<IMaterialHelper> helpers = materialHelpers.get(block);
        if(helpers.isEmpty())
            return new ArrayList<String>();
        
        ArrayList<String> results = new ArrayList<String>();
        
        for(IMaterialHelper helper : helpers)
        {
            ArrayList<String> materials = helper.getMaterials(blockAccess, x, y, z);
            ArrayList<Ore> ores = helper.getOres(blockAccess, x, y, z);
            
            for(String material : materials)
                results.add(material);
            
            for(Ore ore : ores)
                results.add(ore.getMaterial());
        }
        
        return results;
    }
    
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        rebuildColors();
        rebuildNames();
    }
    
    public static interface IMaterialHelper
    {
        public ArrayList<String> getMaterials(IBlockAccess blockAccess, int x, int y, int z);
        public ArrayList<Ore> getOres(IBlockAccess blockAccess, int x, int y, int z);
    }
}
