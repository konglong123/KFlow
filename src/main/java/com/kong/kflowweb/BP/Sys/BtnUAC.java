package com.kong.kflowweb.BP.Sys;

import com.kong.kflowweb.BP.DA.*;
import com.kong.kflowweb.BP.En.*;

/** 按钮访问
 
*/
public enum BtnUAC
{
	/** 
	 不处理
	 
	*/
	None,
	/** 
	 按人员
	 
	*/
	ByEmp,
	/** 
	 按岗位
	 
	*/
	ByStation,
	/** 
	 按部门
	 
	*/
	ByDept,
	/** 
	 按sql
	 
	*/
	BySQL;

	public int getValue()
	{
		return this.ordinal();
	}

	public static BtnUAC forValue(int value)
	{
		return values()[value];
	}
}