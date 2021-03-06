package com.demoxin.minecraft.tmc;

import tterrag.core.IModTT;
import tterrag.core.common.compat.CompatabilityRegistry;

import com.demoxin.minecraft.tmc.common.CommonProxy;
import com.demoxin.minecraft.tmc.data.OreHelper;
import com.demoxin.minecraft.tmc.materialhelpers.FluidHelper;
import com.demoxin.minecraft.tmc.util.InitableCompatability;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = TMC.MODID, version = TMC.VERSION)
public class TMC implements IModTT
{
	public static final String MODID = "TMC";
	public static final String VERSION = "@VERSION@";
	public static final String NAME = "The MOO Collective";
	
	@Instance(TMC.MODID)
	public static TMC INSTANCE;
	
	public static OreHelper oreStorage;
	
	@SidedProxy(clientSide = "com.demoxin.minecraft.tmc.client.ClientProxy", serverSide = "com.demoxin.minecraft.tmc.common.CommonProxy")
	public static CommonProxy proxy;
	
	public TMC()
	{
		CompatabilityRegistry.INSTANCE.registerCompat(InitableCompatability.registerAllTimes(), "com.demoxin.minecraft.tmc.ticon.TiCon", "TConstruct");
		CompatabilityRegistry.INSTANCE.registerCompat(InitableCompatability.registerAllTimes(), "com.demoxin.minecraft.tmc.mystcraft.MystCraft", "Mystcraft");
		CompatabilityRegistry.INSTANCE.registerCompat(InitableCompatability.registerAllTimes(), "com.demoxin.minecraft.tmc.enderio.EnderIO", "EnderIO");
		oreStorage = new OreHelper();
        oreStorage.loadOreDictionary();
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit();	
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit();
		new FluidHelper();
	}
	
	@Override
	public String modid()
	{
		return MODID;
	}
	
	@Override
	public String name()
	{
		return NAME;
	}
	
	@Override
	public String version()
	{
		return VERSION;
	}
}
