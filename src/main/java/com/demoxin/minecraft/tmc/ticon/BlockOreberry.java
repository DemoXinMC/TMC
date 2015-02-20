package com.demoxin.minecraft.tmc.ticon;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.demoxin.minecraft.tmc.TMC;
import com.demoxin.minecraft.tmc.data.OreHelper.IMaterialHelper;
import com.demoxin.minecraft.tmc.data.OreHelper.Ore;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockOreberry extends BlockLeavesBase implements ITileEntityProvider, IMaterialHelper
{
    public IIcon[] textureBush;
    public IIcon[] textureBushOverlay;
    public IIcon[] textureRipe;
    public IIcon[] textureRipeOverlay;
    
    protected BlockOreberry()
    {
        super(Material.leaves, false);
        this.setTickRandomly(true);
        this.setStepSound(Block.soundTypeMetal);
        this.setCreativeTab(TiCon.creativeTab);
    }
    
    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityOreberry();
    }
    
    @Override
    public boolean hasTileEntity(int meta)
    {
        return true;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
        for(Ore ore : TMC.oreStorage.getStorage())
        {
            ItemStack bush = new ItemStack(item, 1, ore.meta);
            bush.stackTagCompound = new NBTTagCompound();
            bush.getTagCompound().setString("oreName", ore.name);
            list.add(bush);
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.textureBush = new IIcon[2];
        this.textureBushOverlay = new IIcon[2];
        this.textureRipe = new IIcon[2];
        this.textureRipeOverlay = new IIcon[2];
        
        for(int i = 0; i < 2; ++i)
        {
            String setting = (i == 0) ? "fancy" : "fast";
            this.textureBush[i] = iconRegister.registerIcon(TMC.MODID + ":blockOreberry_bush_" + setting);
            this.textureBushOverlay[i] = iconRegister.registerIcon(TMC.MODID + ":blockOreberry_overlay_" + setting);
            this.textureRipe[i] = iconRegister.registerIcon(TMC.MODID + ":blockOreberry_bush_ripe_" + setting);
            this.textureRipeOverlay[i] = iconRegister.registerIcon(TMC.MODID + ":blockOreberry_overlay_ripe_" + setting);
        }
    }
    
    @Override
    public IIcon getIcon(int side, int metadata)
    {
        if(Minecraft.getMinecraft().gameSettings.fancyGraphics)
            return this.textureBush[0];
        else
            return this.textureBush[1];
    }
    
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    @Override
    public boolean renderAsNormalBlock ()
    {
        return false;
    }

    @Override
    public int getRenderType ()
    {
        return RendererOreberry.renderId;
    }
    
    @Override
    public ArrayList<String> getMaterials(IBlockAccess blockAccess, int x, int y, int z)
    {
        return new ArrayList<String>();
    }
    
    @Override
    public ArrayList<Ore> getOres(IBlockAccess blockAccess, int x, int y, int z)
    {
        ArrayList<Ore> result = new ArrayList<Ore>();
        result.add(((TileEntityOreberry) blockAccess.getTileEntity(x, y, z)).getOre());
        return result;
    }
    
}
