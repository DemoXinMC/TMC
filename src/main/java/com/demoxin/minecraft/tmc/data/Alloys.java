package com.demoxin.minecraft.tmc.data;

public class Alloys
{
    public static void loadKnownAlloys(OreHelper oreHelper)
    {
        // Vanilla
        oreHelper.addAlloy("obsidian", "water", "lava");
        
        // Tinker's Construct
        oreHelper.addAlloy("bronze", "copper", "tin");
        oreHelper.addAlloy("aluminumbrass", "copper", "aluminum");
        oreHelper.addAlloy("steel", "iron", "coal");
        oreHelper.addAlloy("alumite", "iron", "obsidian", "aluminum");
        oreHelper.addAlloy("manyullyn", "cobalt", "ardite");
        oreHelper.addAlloy("pigiron", "iron", "emerald", "blood");
        
        // Thermal Foundation
        oreHelper.addAlloy("invar", "iron", "nickel");
        oreHelper.addAlloy("electrum", "gold", "silver");
        oreHelper.addAlloy("enderiumblend", "tin", "silver", "platinum", "ender");
        oreHelper.addAlloy("enderium", "enderiumblend", "pyrotheum");
        oreHelper.addAlloy("signalum", "copper", "silver", "redstone");
        oreHelper.addAlloy("lumium", "tin", "silver", "glowstone");
        oreHelper.addAlloy("fluxedelectrum", "electrum", "redstone", "pyrotheum");
        
        // Ender IO
        oreHelper.addAlloy("conductiveiron", "iron", "redstone");
        oreHelper.addAlloy("pulsatingiron", "iron", "ender");
        oreHelper.addAlloy("electricalsteel", "iron", "coal", "silicon");
        oreHelper.addAlloy("darksteel", "iron", "coal", "obsidian");
        oreHelper.addAlloy("energeticalloy", "gold", "redstone", "glowstone");
        oreHelper.addAlloy("vibrantalloy", "energeticalloy", "ender");
        oreHelper.addAlloy("redstonealloy", "redstone", "silicon");
        oreHelper.addAlloy("enderiumbase", "tin", "silver", "platinum");
        oreHelper.addAlloy("enderium", "enderiumbase", "pyrotheum", "ender");
        oreHelper.addAlloy("soularium", "gold", "soulsand");
        
        // Metallurgy
        oreHelper.addAlloy("steel", "iron", "manganese");
        oreHelper.addAlloy("hepatizon", "gold", "bronze");
        oreHelper.addAlloy("damascussteel", "iron", "bronze");
        oreHelper.addAlloy("angmallen", "iron", "gold");
        oreHelper.addAlloy("brass", "copper", "zinc");
        oreHelper.addAlloy("shadowsteel", "shadowiron", "lemurite");
        oreHelper.addAlloy("inolashite", "alduorite", "ceruclase");
        oreHelper.addAlloy("amordrine", "kalendrite", "platinum");
        oreHelper.addAlloy("blacksteel", "infuscolium", "deepiron");
        oreHelper.addAlloy("haderoth", "mithril", "rubracium");
        oreHelper.addAlloy("quicksilver", "mithril", "silver");
        oreHelper.addAlloy("celenegil", "orichalcum", "platinum");
        oreHelper.addAlloy("tartarite", "atlarus", "adamantine");
        oreHelper.addAlloy("desichalkos", "eximite", "meutoite");
        
    }
}
