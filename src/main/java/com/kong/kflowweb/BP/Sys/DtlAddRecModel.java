package com.kong.kflowweb.BP.Sys;

import com.kong.kflowweb.BP.DA.*;
import com.kong.kflowweb.BP.En.*;

/** 
 明细表存盘方式
 
*/
public enum DtlAddRecModel
{
	/** 
	 自动初始化空白行
	 
	*/
	ByBlank,
	/** 
	 用按钮增加行
	 
	*/
	ByButton;

	public int getValue()
	{
		return this.ordinal();
	}

	public static DtlAddRecModel forValue(int value)
	{
		return values()[value];
	}
}