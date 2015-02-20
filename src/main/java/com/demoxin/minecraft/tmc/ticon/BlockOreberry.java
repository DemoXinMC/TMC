package com.demoxin.minecraft.tmc.ticon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;
import tterrag.core.common.util.BlockCoord;
import tterrag.core.common.util.blockiterators.CubicBlockIterator;

import com.demoxin.minecraft.tmc.TMC;
import com.demoxin.minecraft.tmc.data.OreHelper;
import com.demoxin.minecraft.tmc.data.OreHelper.Alloy;
import com.demoxin.minecraft.tmc.data.OreHelper.IMaterialHelper;
import com.demoxin.minecraft.tmc.data.OreHelper.Ore;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockOreberry extends BlockLeavesBase implements ITileEntityProvider, IMaterialHelper, IPlantable
{
    public IIcon[] textureBush;
    public IIcon[] textureBushOverlay;
    public IIcon[] textureRipe;
    public IIcon[] textureRipeOverlay;
    protected Random random;
    
    protected BlockOreberry()
    {
        super(Material.leaves, false);
        this.setTickRandomly(true);
        this.setStepSound(Block.soundTypeMetal);
        this.setCreativeTab(TiCon.creativeTab);
        this.random = new Random();
        this.setHardness(0.3F);
        
        OreHelper.INSTANCE.registerMaterialHelper(this, this);
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
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack)
    {
        if(!stack.hasTagCompound())
            stack.stackTagCompound = new NBTTagCompound();
        
        if(!stack.getTagCompound().hasKey("oreName"))
            stack.getTagCompound().setString("oreName", "iron");

        TileEntityOreberry te = (TileEntityOreberry)world.getTileEntity(x, y, z);
        Ore ore = OreHelper.INSTANCE.getOreByName(stack.getTagCompound().getString("oreName"));
        
        if(ore == null)
            ore = OreHelper.INSTANCE.getOreByName("iron");
        te.ore = ore;
        te.growthState = 0;
    }
    
    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {
        this.dropBlockAsItem(world, x, y, z, meta, 0);
        super.onBlockHarvested(world, x, y, z, meta, player);
    }
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune)
    {
        ArrayList<ItemStack> dropped = new ArrayList<ItemStack>();
        TileEntity te = world.getTileEntity(x, y, z);
        if(!(te instanceof TileEntityOreberry))
            return dropped;
        
        TileEntityOreberry bushTE = (TileEntityOreberry)te;
        
        ItemStack bush = new ItemStack(this);
        bush.stackTagCompound = new NBTTagCompound();
        bush.getTagCompound().setString("oreName", bushTE.getOre().name);
        
        dropped.add(bush);
        return dropped;
    }
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        
        if (world.isRemote)
        {
            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
            return;
        }
        
        if(random.nextInt(20) > 20)
            return;
        
        if(world.getFullBlockLightValue(x, y, z) > 10)
            return;
        
        TileEntityOreberry te = (TileEntityOreberry)world.getTileEntity(x, y, z);
        
        switch(te.growthState)
        {
            case 0:
            case 1:
            case 2:
                te.growthState++;
                te.markDirty();
                world.markBlockForUpdate(x, y, z);
                break;
            case 3:
                attemptSpread(world, x, y, z, random);
                attemptAlloy(world, x, y, z, random);
                break;
        }
    }
    
    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
    {
        harvest(world, x, y, z, player);
    }

    /* Right-click harvests berries */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        updateTick(world, x, y, z, world.rand);
        return true; //harvest(world, x, y, z, player);
    }
    
    public void attemptSpread(World world, int x, int y, int z, Random random)
    {
        if(random.nextInt(50) > 0)
            return;
        
        TileEntityOreberry te = (TileEntityOreberry)world.getTileEntity(x, y, z);
        
        CubicBlockIterator coords = new CubicBlockIterator(new BlockCoord(x, y, z), 4);
        
        ArrayList<BlockCoord> possibleSpawns = new ArrayList<BlockCoord>();
        
        for(BlockCoord coord : coords)
            if(this.canPlaceBlockAt(world, coord.x, coord.y, coord.z))
                possibleSpawns.add(new BlockCoord(coord.x, coord.y, coord.z));
        
        if(possibleSpawns.isEmpty())
            return;
        
        BlockCoord toSpawn = possibleSpawns.get(world.rand.nextInt(possibleSpawns.size()));
        
        Ore ore = te.getOre();
        if(ore == null)
            return;
            
        world.setBlock(toSpawn.x, toSpawn.y, toSpawn.z, this);
        TileEntityOreberry newTE = (TileEntityOreberry)world.getTileEntity(toSpawn.x, toSpawn.y, toSpawn.z);
        newTE.growthState = 0;
        newTE.setOre(ore);
    }
    
    public void attemptAlloy(World world, int x, int y, int z, Random random)
    {
        if(random.nextInt(80) > 0)
            return;
        
        //TileEntityOreberry te = (TileEntityOreberry)world.getTileEntity(x, y, z);
        
        CubicBlockIterator coords = new CubicBlockIterator(new BlockCoord(x, y, z), 4);
        
        ArrayList<BlockCoord> possibleSpawns = new ArrayList<BlockCoord>();
        ArrayList<String> materials = new ArrayList<String>();
        
        for(BlockCoord coord : coords)
        {
            if(this.canPlaceBlockAt(world, coord.x, coord.y, coord.z))
                possibleSpawns.add(new BlockCoord(coord.x, coord.y, coord.z));
                
            materials.addAll(OreHelper.INSTANCE.getMaterials(world, coord.x, coord.y, coord.z));
        }
        
        ArrayList<Alloy> possibleAlloys = OreHelper.INSTANCE.getAlloysForMaterials(materials);
        
        /*
        ArrayList<Alloy> toRemove = new ArrayList<Alloy>();
        for(Alloy alloy : possibleAlloys)
        {
            if(!alloy.containsMaterial(te.getOre().getMaterial()))
                toRemove.add(alloy);
        }
        possibleAlloys.removeAll(toRemove);
        
        if(possibleAlloys.isEmpty() || possibleSpawns.isEmpty())
            return;
        */
        
        Alloy toCreate = possibleAlloys.get(world.rand.nextInt(possibleAlloys.size()));
        BlockCoord toSpawn = possibleSpawns.get(world.rand.nextInt(possibleSpawns.size()));
        
        Ore ore = toCreate.getOre();
        if(ore == null)
            return;
            
        world.setBlock(toSpawn.x, toSpawn.y, toSpawn.z, this);
        TileEntityOreberry newTE = (TileEntityOreberry)world.getTileEntity(toSpawn.x, toSpawn.y, toSpawn.z);
        newTE.growthState = 0;
        newTE.setOre(ore);
        
    }
    
    public boolean harvest(World world, int x, int y, int z, EntityPlayer player)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        
        if(te == null || !(te instanceof TileEntityOreberry))
            return false;
        
        TileEntityOreberry bushTE = (TileEntityOreberry)te;
        

        if(bushTE.growthState == 3)
        {
            if (world.isRemote)
                return true;

            bushTE.growthState = 2;
            world.markBlockForUpdate(x, y, z);
            bushTE.markDirty();
            ItemStack berry = new ItemStack(TiCon.oreberryBerry, random.nextInt(3) + 1);
            berry.stackTagCompound = new NBTTagCompound();
            berry.getTagCompound().setString("oreName", bushTE.ore.name);
            
            if(player instanceof FakePlayer || !player.inventory.addItemStackToInventory(berry))
            {
                EntityItem entityItem = new EntityItem(world, player.posX + 0.5D, player.posY + 0.5D, player.posZ + 0.5D, berry);
                world.spawnEntityInWorld(entityItem);
                if(!(player instanceof FakePlayer))
                    entityItem.onCollideWithPlayer(player);
            }
            else
            {
                player.worldObj.playSoundAtEntity(player, "random.pop", 0.2F, 1.0F);
                if(player instanceof EntityPlayerMP)
                    player.inventoryContainer.detectAndSendChanges();
            }
        }

        return false;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        TileEntityOreberry te = (TileEntityOreberry)world.getTileEntity(x, y, z);
        
        if(te == null)
            return AxisAlignedBB.getBoundingBox(x + 0.0625, y, z + 0.0625, (double) x + 0.9375D, (double) y + 0.9375D, (double) z + 0.9375D);
        
        int growthState = te.growthState;
        if (growthState == 0)
            return AxisAlignedBB.getBoundingBox((double) x + 0.25D, y, (double) z + 0.25D, (double) x + 0.75D, (double) y + 0.5D, (double) z + 0.75D);
        else if (growthState == 1)
            return AxisAlignedBB.getBoundingBox((double) x + 0.125D, y, (double) z + 0.125D, (double) x + 0.875D, (double) y + 0.75D, (double) z + 0.875D);
        else
            return AxisAlignedBB.getBoundingBox(x + 0.0625, y, z + 0.0625, (double) x + 0.9375D, (double) y + 0.9375D, (double) z + 0.9375D);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        TileEntityOreberry te = (TileEntityOreberry)world.getTileEntity(x, y, z);
        int growthState = te.growthState;
        if (growthState == 0)
            return AxisAlignedBB.getBoundingBox((double) x + 0.25D, y, (double) z + 0.25D, (double) x + 0.75D, (double) y + 0.5D, (double) z + 0.75D);
        else if (growthState == 1)
            return AxisAlignedBB.getBoundingBox((double) x + 0.125D, y, (double) z + 0.125D, (double) x + 0.875D, (double) y + 0.75D, (double) z + 0.875D);
        else
            return AxisAlignedBB.getBoundingBox(x, y, z, (double) x + 1.0D, (double) y + 1.0D, (double) z + 1.0D);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z)
    {
        TileEntityOreberry te = (TileEntityOreberry)blockAccess.getTileEntity(x, y, z);
        int growthState = te.growthState;

        float minX;
        float minY = 0F;
        float minZ;
        float maxX;
        float maxY;
        float maxZ;

        if (growthState == 0)
        {
            minX = minZ = 0.25F;
            maxX = maxZ = 0.75F;
            maxY = 0.5F;
        }
        else if (growthState == 1)
        {
            minX = minZ = 0.125F;
            maxX = maxZ = 0.875F;
            maxY = 0.75F;
        }
        else
        {
            minX = minZ = 0.0F;
            maxX = maxZ = 1.0F;
            maxY = 1.0F;
        }
        setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        if (world.getFullBlockLightValue(x, y, z) >= 13)
            return false;
        
        Block block = world.getBlock(x, y, z);
        if(!block.isReplaceable(world, x, y, z))
            return false;
        
        Block below = world.getBlock(x, y-1, z);
        if(!below.isSideSolid(world, x, y-1, z, ForgeDirection.UP))
            return false;
        
        return true;
    }
    
    public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant)
    {
        if (plant instanceof BlockOreberry)
        {
            TileEntity te = world.getTileEntity(x, y, z);
            if(te == null || !(te instanceof TileEntityOreberry))
                return false;
            return (((TileEntityOreberry)te).growthState >= 2);
        }
        return super.canSustainPlant(world, x, y, z, direction, plant);
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
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int meta)
    {
        int fancy = this.field_150121_P ? 1 : 0;
        TileEntity te = blockAccess.getTileEntity(x, y, z);
        if(te == null || !(te instanceof TileEntityOreberry))
            return this.textureBush[fancy];
        
        if(((TileEntityOreberry)te).growthState < 3)
            return this.textureBush[fancy];
        else
            return this.textureRipe[fancy];
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z)
    {
        TileEntity te = blockAccess.getTileEntity(x, y, z);
        if(te == null || !(te instanceof TileEntityOreberry))
            return 0xFFFFFF;
        if(((TileEntityOreberry)te).ore == null)
            return 0xFFFFFF;
        return ((TileEntityOreberry)te).ore.getColor();
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
    
    public void setGraphicsLevel(boolean flag)
    {
        field_150121_P = flag;
    }
    
    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        if(field_150121_P)
            return true;
        TileEntity te = world.getTileEntity(x, y, z);
        if(te == null || !(te instanceof TileEntityOreberry))
            return true;
        
        if(((TileEntityOreberry)te).growthState > 1 || field_150121_P)
            return super.shouldSideBeRendered(world, x, y, z, side);
        else
            return true;
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
        TileEntityOreberry te = (TileEntityOreberry)blockAccess.getTileEntity(x, y, z);
        if(te == null || te.growthState != 3)
            return result;
        result.add(te.getOre());
        return result;
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
    {
        return EnumPlantType.Cave;
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z)
    {
        return this;
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
    {
        return 0;
    }
    
    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        if (!(entity instanceof EntityItem))
            entity.attackEntityFrom(DamageSource.cactus, 1);
    }
    
}
