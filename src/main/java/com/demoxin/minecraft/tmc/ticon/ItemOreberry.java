package com.demoxin.minecraft.tmc.ticon;

import java.util.List;

import tconstruct.library.crafting.Smeltery;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
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
        for(Ore ore : TMC.oreStorage.getStorage())
        {
            ItemStack input = new ItemStack(this);
            input.stackTagCompound = new NBTTagCompound();
            input.getTagCompound().setString("oreName", ore.name);
            ItemStack output = new ItemStack(ore.nugget.getItem(), 1, ore.nugget.getItemDamage());
            System.out.println("Smelting Recipe Added: " + input.getDisplayName() + " -> " + output.getDisplayName());
            GameRegistry.addSmelting(input, output, 0);
            
            Block displayBlock;
            int displayMeta;
            
            displayBlock = (ore.ore != null) ? Block.getBlockFromItem(ore.ore.getItem()) : Block.getBlockFromItem(ore.block.getItem());
            displayMeta = (ore.ore != null) ? ore.ore.getItemDamage() : ore.block.getItemDamage();
            Smeltery.addMelting(input.copy(), displayBlock, displayMeta, Smeltery.getLiquifyTemperature(ore.nugget.copy()), Smeltery.getSmelteryResult(ore.nugget.copy()));
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
        {
            stack.stackTagCompound = new NBTTagCompound();
            stack.getTagCompound().setString("oreName", "Iron");
        }
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
        if(pass != 0)
            return super.getColorFromItemStack(stack, pass);
        
        if(!stack.hasTagCompound())
            return 0xCCCCCC;
        
        if(TMC.oreStorage.getOreByName(stack.getTagCompound().getString("oreName")) == null)
            return 0xCCCCCC;
        
        return TMC.oreStorage.getOreByName(stack.getTagCompound().getString("oreName")).color.getRGB();
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
        String bbReplaced = template.replace("%bb", berryBit);
        String mbReplaced = bbReplaced.replace("%mb", ore.prettyName);
        return mbReplaced;
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
        System.out.println("getSubItems() called!");
        for(Ore ore : TMC.oreStorage.getStorage())
        {
            ItemStack berry = new ItemStack(this, 1, ore.meta);
            berry.stackTagCompound = new NBTTagCompound();
            berry.getTagCompound().setString("oreName", ore.name);
            list.add(berry);
        }
    }
}
