package com.demoxin.minecraft.tmc.ticon;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.demoxin.minecraft.tmc.data.OreHelper;
import com.demoxin.minecraft.tmc.data.OreHelper.Ore;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RendererOreberry implements ISimpleBlockRenderingHandler
{
    public static int renderId = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        if(modelId != renderId)
            return;
        
        if(block != TiCon.oreberryBush)
            return;
        
        BlockOreberry bush = (BlockOreberry)block;
        
        IIcon base;
        IIcon overlay;
        if(Minecraft.getMinecraft().gameSettings.fancyGraphics)
        {
            base = bush.textureBush[0];
            overlay = bush.textureBushOverlay[0];
        }
        else
        {
            base = bush.textureBush[1];
            overlay = bush.textureBushOverlay[1];
        }
        
        boolean hasOverlay = false;
        
        Ore ore = OreHelper.INSTANCE.getOreByMeta(metadata);
        
        if(ore == null)
            return;
        
        int color = 0xFFFFFF;
        if(ore.color != null)
            color = ore.color.getRGB();
            
        renderer.setRenderBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.75F, 0.875F);

        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1F, 0.0F);
        tessellator.setColorOpaque_I(color);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, base);
        tessellator.setColorOpaque_I(0xFFFFFF);
        if(hasOverlay)
            renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, overlay);
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.setColorOpaque_I(color);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, base);
        tessellator.setColorOpaque_I(0xFFFFFF);
        if(hasOverlay)
            renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, overlay);
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1F);
        tessellator.setColorOpaque_I(color);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, base);
        tessellator.setColorOpaque_I(0xFFFFFF);
        if(hasOverlay)
            renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, overlay);
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.setColorOpaque_I(color);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, base);
        tessellator.setColorOpaque_I(0xFFFFFF);
        if(hasOverlay)
            renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, overlay);
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0.0F, 0.0F);
        tessellator.setColorOpaque_I(color);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, base);
        tessellator.setColorOpaque_I(0xFFFFFF);
        if(hasOverlay)
            renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, overlay);
        tessellator.draw();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        tessellator.setColorOpaque_I(color);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, base);
        tessellator.setColorOpaque_I(0xFFFFFF);
        if(hasOverlay)
            renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, overlay);
        tessellator.draw();
        
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return renderId;
    }    
}
