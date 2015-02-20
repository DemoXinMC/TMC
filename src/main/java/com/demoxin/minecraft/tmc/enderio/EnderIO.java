package com.demoxin.minecraft.tmc.enderio;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;
import tterrag.core.common.compat.CompatabilityRegistry;
import tterrag.core.common.compat.ICompatability;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EnderIO implements ICompatability
{
    public static EnderIO INSTANCE;
    
    public static void load()
    {
        if(INSTANCE == null)
            INSTANCE = new EnderIO();
        
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
    
    EnderIO()
    {
        fixIngotNames();
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    public void preInit()
    {
        
    }
    
    public void init()
    {
        
    }
    
    public void postInit()
    {
        
    }
    
    public void fixIngotNames()
    {
        ArrayList<ItemStack> pulsatingIngots = OreDictionary.getOres("ingotPhasedIron");
        ArrayList<ItemStack> vibrantIngots = OreDictionary.getOres("ingotPhasedGold");
        
        if(!pulsatingIngots.isEmpty())
            OreDictionary.registerOre("ingotPulsatingIron", pulsatingIngots.get(0).copy());
        
        if(!vibrantIngots.isEmpty())
            OreDictionary.registerOre("ingotVibrantAlloy", vibrantIngots.get(0).copy());

    }
    
    @SubscribeEvent
    public void fixIngotNames(OreRegisterEvent event)
    {
        if(event.Name.equals("ingotPhasedIron"))
            OreDictionary.registerOre("ingotPulsatingIron", event.Ore.copy());
        
        if(event.Name.equals("ingotPhasedGold"))
            OreDictionary.registerOre("ingotVibrantAlloy", event.Ore.copy());
    }
}
