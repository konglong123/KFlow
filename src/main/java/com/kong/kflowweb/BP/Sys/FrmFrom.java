package com.kong.kflowweb.BP.Sys;

import com.kong.kflowweb.BP.DA.*;
import com.kong.kflowweb.BP.En.*;

public enum FrmFrom
{
	Flow,
	Node,
	Dtl;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FrmFrom forValue(int value)
	{
		return values()[value];
	}
}