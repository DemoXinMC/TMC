package com.demoxin.minecraft.tmc.ticon;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.oredict.OreDictionary;

import com.demoxin.minecraft.tmc.TMC;
import com.demoxin.minecraft.tmc.data.OreStorage;
import com.demoxin.minecraft.tmc.data.OreStorage.Ore;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemOreberry extends Item
{   
    public static ItemOreberry instance;
    protected IIcon textureTemplate;
    protected IIcon textureOverlay;
    
    public ItemOreberry()
    {
        super();
        instance = this;
        setMaxStackSize(64);
        setCreativeTab(TiCon.creativeTab);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    public void smelting()
    {
        for(OreStorage.Ore ore : TMC.oreStorage.getStorage())
        {
            ItemStack input = new ItemStack(this, 1, ore.meta);
            ItemStack output = ore.nugget.copy();
            GameRegistry.addSmelting(input, output, 0);
        }
    }
    
    public void oreDicting()
    {
        for(OreStorage.Ore ore : TMC.oreStorage.getStorage())
        {
            ItemStack toDict = new ItemStack(this, 1, ore.meta);
            OreDictionary.registerOre("nugget" + ore.name, toDict);
        }
    }
    
    @Override
    public int getDamage(ItemStack stack)
    {
        if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("oreName"))
            return 0;
        String oreName = stack.getTagCompound().getString("oreName");
        Ore ore = TMC.oreStorage.getOreByName(oreName);
        if(ore == null)
            return 0;
        return ore.meta;
    }
    
    @Override
    public void registerIcons(IIconRegister iconRegister)
    {
        textureTemplate = iconRegister.registerIcon(TMC.MODID + ":itemOreberry_base");
        textureOverlay = iconRegister.registerIcon(TMC.MODID + ":itemOreberry_overlay");
    }
    
    @Override
    public int getColorFromItemStack(ItemStack stack, int pass)
    {
        return pass == 0 ? TMC.oreStorage.getOreByName(stack.getTagCompound().getString("oreName")).color.getRGB() : super.getColorFromItemStack(stack, pass);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("oreName"))
            return "item.tmc.oreberry.NA";
        return "item.tmc.oreberry." + stack.getTagCompound().getString("oreName");
    }
    
    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("oreName"))
            return "Erraneous Berry!";
        
        Ore ore = TMC.oreStorage.getOreByName(stack.getTagCompound().getString("oreName"));
        String berryBit = (ore.ore != null) ? StatCollector.translateToLocal("tmc.ticon.berry.ore") : StatCollector.translateToLocal("tmc.ticon.berry.alloy");
        String template = StatCollector.translateToLocal("tmc.ticon.berry");
        return template.replace("%bb", berryBit).replace("%mb", ore.prettyName);
    }
    
    @Override
    public boolean hasEffect(ItemStack stack, int pass)
    {
        if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("oreName"))
            return false;
        
        Ore ore = TMC.oreStorage.getOreByName(stack.getTagCompound().getString("oreName"));
        if(ore == null)
            return false;
        
        return ore.glowy;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass)
    {
        return pass == 0 ? textureTemplate : textureOverlay;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list)
    {
        for(int i = 0; i < TMC.oreStorage.getStorage().size(); ++i)
        {
            ItemStack berry = new ItemStack(this);
            berry.stackTagCompound = new NBTTagCompound();
            berry.getTagCompound().setString("oreName", TMC.oreStorage.getStorage().get(i).name);
            list.add(berry);
        }
    }
}
