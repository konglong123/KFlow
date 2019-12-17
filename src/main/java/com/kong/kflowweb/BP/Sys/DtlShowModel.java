package com.kong.kflowweb.BP.Sys;

import com.kong.kflowweb.BP.DA.*;
import com.kong.kflowweb.BP.En.*;

/** 
 从表显示方式
 
*/
public enum DtlShowModel
{
	/** 
	 表格方式
	 
	*/
	Table,
	/** 
	 卡片方式
	 
	*/
	Card;

	public int getValue()
	{
		return this.ordinal();
	}

	public static DtlShowModel forValue(int value)
	{
		return values()[value];
	}
}