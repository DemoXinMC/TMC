package com.demoxin.minecraft.tmc.ticon;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class RendererOreberry implements IItemRenderer
{

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        
    }
    
}
