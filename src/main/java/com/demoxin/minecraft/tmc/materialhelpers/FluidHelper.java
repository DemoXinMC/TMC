package com.demoxin.minecraft.tmc.materialhelpers;

import java.util.ArrayList;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.demoxin.minecraft.tmc.data.OreHelper;
import com.demoxin.minecraft.tmc.data.OreHelper.IMaterialHelper;
import com.demoxin.minecraft.tmc.data.OreHelper.Ore;

public class FluidHelper implements IMaterialHelper
{
    public FluidHelper()
    {
        Map<String, Fluid> fluids = FluidRegistry.getRegisteredFluids();

        for (Map.Entry<String, Fluid> entry : fluids.entrySet())
            OreHelper.INSTANCE.registerMaterialHelper(entry.getValue().getBlock(), this);
    }

    @Override
    public ArrayList<String> getMaterials(IBlockAccess blockAccess, int x, int y, int z)
    {
        ArrayList<String> material = new ArrayList<String>();
        Block block = blockAccess.getBlock(x, y, z);
        if(block == null)
            return material;
        
        Fluid fluid = FluidRegistry.lookupFluidForBlock(block);
        
        if(fluid == null)
            return material;
        
        material.add(fluid.getName());
        return material;
    }

    @Override
    public ArrayList<Ore> getOres(IBlockAccess blockAccess, int x, int y, int z)
    {
        return new ArrayList<Ore>();
    }
    
}
