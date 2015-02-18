package com.demoxin.minecraft.tmc.util;

import tterrag.core.common.compat.ICompatability;
import tterrag.core.common.util.RegisterTime;

public abstract class InitableCompatability implements ICompatability
{
	public static RegisterTime[] registerAllTimes()
	{
		return new RegisterTime[] { RegisterTime.PREINIT, RegisterTime.INIT, RegisterTime.POSTINIT };
	}
}
