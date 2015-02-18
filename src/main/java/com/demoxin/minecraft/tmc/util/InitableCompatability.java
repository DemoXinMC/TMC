package com.demoxin.minecraft.tmc.util;

import tterrag.core.common.compat.CompatabilityRegistry;
import tterrag.core.common.compat.ICompatability;
import tterrag.core.common.util.RegisterTime;

public abstract class InitableCompatability implements ICompatability
{
	public void load()
	{
		switch(CompatabilityRegistry.INSTANCE.getState())
		{
		case INIT:
			init();
			break;
		case POSTINIT:
			postInit();
			break;
		case PREINIT:
			preInit();
			break;
		default:
			break;
		}
	}
	
	public static RegisterTime[] registerAllTimes()
	{
	    return new RegisterTime[] { RegisterTime.PREINIT, RegisterTime.INIT, RegisterTime.POSTINIT };
	}
	
	public void preInit() {}
	public void init() {}
	public void postInit() {}
}
