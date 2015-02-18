package com.demoxin.minecraft.tmc.ticon;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockOreberry extends BlockLeavesBase implements ITileEntityProvider
{
	private IIcon[] textureBush;
	private IIcon[] textureBushOverlay;
	private IIcon[] textureRipe;
	private IIcon[] textureRipeOverlay;
	
	protected BlockOreberry()
	{
		super(Material.leaves, false);
		this.setTickRandomly(true);
		this.setStepSound(Block.soundTypeMetal);
		// this.setCreativeTab();
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
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
		
	}
	
}
