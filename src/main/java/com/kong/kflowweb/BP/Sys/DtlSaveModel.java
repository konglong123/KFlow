package com.kong.kflowweb.BP.Sys;

import com.kong.kflowweb.BP.DA.*;
import com.kong.kflowweb.BP.En.*;

public enum DtlSaveModel
{
	/** 
	 失去焦点自动存盘
	 
	*/
	AutoSave,
	/** 
	 由保存按钮触发存盘
	 
	*/
	HandSave;

	public int getValue()
	{
		return this.ordinal();
	}

	public static DtlSaveModel forValue(int value)
	{
		return values()[value];
	}
}