package com.kong.kflowweb.BP.Sys;

import com.kong.kflowweb.BP.DA.*;
import com.kong.kflowweb.BP.En.*;

/** 
 文件展现方式
 
*/
public enum FileShowWay
{
	/** 
	 表格
	 
	*/
	Table,
	/** 
	 图片
	 
	*/
	Pict,
	/** 
	 自由模式
	 
	*/
	Free;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FileShowWay forValue(int value)
	{
		return values()[value];
	}
}