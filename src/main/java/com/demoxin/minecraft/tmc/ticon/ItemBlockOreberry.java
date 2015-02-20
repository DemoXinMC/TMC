package com.demoxin.minecraft.tmc.ticon;

import com.demoxin.minecraft.tmc.TMC;
import com.demoxin.minecraft.tmc.data.OreHelper.Ore;

import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

public class ItemBlockOreberry extends ItemBlock
{
	
	public ItemBlockOreberry(Block block)
	{
		super(block);
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
    public String getItemStackDisplayName(ItemStack stack)
    {
        if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("oreName"))
            return "Erraneous Bush!";
        
        Ore ore = TMC.oreStorage.getOreByName(stack.getTagCompound().getString("oreName"));
        String berryBit = (ore.ore != null) ? StatCollector.translateToLocal("tmc.ticon.berry.ore") : StatCollector.translateToLocal("tmc.ticon.berry.alloy");
        String bushBit = StatCollector.translateToLocal("tmc.ticon.bush");
        String template = StatCollector.translateToLocal("tmc.ticon.bush.format");
        String bbReplaced = template.replace("%bb", berryBit);
        String mbReplaced = bbReplaced.replace("%mb", ore.prettyName);
        String bushReplaced = mbReplaced.replace("%bush", bushBit);
        return bushReplaced;
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
    public EnumRarity getRarity(ItemStack stack)
    {
        if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("oreName"))
            return EnumRarity.common;
        
        Ore ore = TMC.oreStorage.getOreByName(stack.getTagCompound().getString("oreName"));
        if(ore == null)
            return EnumRarity.common;
        
        return ore.rarity;
    }
}
