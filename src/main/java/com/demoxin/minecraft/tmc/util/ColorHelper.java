package com.demoxin.minecraft.tmc.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class ColorHelper
{
    public static Color getAverageColor(List<ItemStack> stacks)
    {
        ArrayList<Color> colors = new ArrayList<Color>();
        for(ItemStack stack : stacks)
        {
            ResourceLocation stackTextureResource = getStackTextureResource(stack);
            if(stackTextureResource == null)
                continue;
            
            BufferedImage image = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(stackTextureResource).getInputStream());
            Color avgColor = getAverageColor(image);
            
            int r = 0;
            int g = 0;
            int b = 0;
            int count = 0;
            for(int pass = 0; pass < stack.getItem().getRenderPasses(stack.getItemDamage()); ++pass)
            {
                int stackColor = stack.getItem().getColorFromItemStack(stack, pass);
                if(stackColor == 0xFFFFFF)
                    continue;
                
                Color parsedColor = new Color(stackColor);
                r += parsedColor.getRed();
                g += parsedColor.getGreen();
                b += parsedColor.getBlue();
                ++count;
            }

            colors.add((r > 0 || g > 0 || b > 0) ? new Color(r / count, g / count, b / count) : avgColor);
        }
        
        int r = 0;
        int g = 0;
        int b = 0;
        for(Color color : colors)
        {
            r += color.getRed();
            g += color.getGreen();
            b += color.getBlue();
        }
        return new Color(r / colors.size(), g / colors.size(), b / colors.size());
    }
    
    public static Color getAverageColor(BufferedImage image)
    {
        int r = 0;
        int g = 0;
        int b = 0;
        int count = 0;
        int threshold = 10;
        for (int x = 0; x < image.getWidth(); x++)
        {
            for (int y = 0; y < image.getHeight(); y++)
            {
                Color c = new Color(image.getRGB(x, y));
                if(c.getAlpha() != 255)
                    continue;
                if(c.getRed() <= threshold && c.getBlue() <= threshold && c.getGreen() <= threshold)
                    continue;
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
                count++;
            }
        }

        return new Color(r / count, g / count, b / count);
    }
    
    private static ResourceLocation getStackTextureResource(ItemStack stack)
    {
            IIcon icon = stack.getItem().getIcon(stack, 0);
            if (icon == null)
                return null;
            
            String iconName = icon.getIconName();
            if(iconName == null)
                return null;

            String string = "minecraft";

            int colonIndex = iconName.indexOf(58);
            if (colonIndex >= 0)
            {
                if (colonIndex > 1)
                    string = iconName.substring(0, colonIndex);

                iconName = iconName.substring(colonIndex + 1, iconName.length());
            }

            string = string.toLowerCase();
            iconName = "textures/items/" + iconName + ".png";
            return new ResourceLocation(string, iconName);
        }
    }
}
