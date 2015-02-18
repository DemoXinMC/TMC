package com.demoxin.minecraft.tmc.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;

import com.demoxin.minecraft.tmc.TMC;
import com.demoxin.minecraft.tmc.common.CommonProxy;

public class ClientProxy extends CommonProxy
{
	public void postInit()
	{
		IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
		if(resourceManager != null && resourceManager instanceof IReloadableResourceManager)
			((IReloadableResourceManager) resourceManager).registerReloadListener(TMC.oreStorage);
	}
}
