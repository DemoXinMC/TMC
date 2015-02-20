package com.demoxin.minecraft.tmc.ticon;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import com.demoxin.minecraft.tmc.TMC;
import com.demoxin.minecraft.tmc.data.OreHelper.Ore;

public class TileEntityOreberry extends TileEntity
{
	public int growthState;
	protected Ore ore;
	
	@Override
	public boolean canUpdate()
	{
		return false;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		super.writeToNBT(data);
		data.setInteger("growth", growthState);
		data.setString("oreName", ore.name);
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound data = new NBTTagCompound();
		data.setInteger("growth", growthState);
		data.setString("oreName", ore.name);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, data);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data)
	{
		super.readFromNBT(data);
		growthState = data.getInteger("growth");
		ore = TMC.oreStorage.getOreByName(data.getString("oreName"));
		if(ore == null)
			worldObj.setBlockToAir(xCoord, yCoord, zCoord);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		NBTTagCompound data = pkt.func_148857_g();
		growthState = data.getInteger("growth");
		ore = TMC.oreStorage.getOreByName(data.getString("oreName"));
		if(ore == null)
			worldObj.setBlockToAir(xCoord, yCoord, zCoord);
		
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
	}

    public Ore getOre()
    {
        return ore;
    }
    
    public void setOre(Ore ore)
    {
        if(this.ore == null)
            this.ore = ore;
    }
}
