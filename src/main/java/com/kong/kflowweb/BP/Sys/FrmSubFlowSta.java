package com.kong.kflowweb.BP.Sys;

import com.kong.kflowweb.BP.DA.*;
import com.kong.kflowweb.BP.En.*;
import com.kong.kflowweb.BP.WF.Template.*;
import com.kong.kflowweb.BP.WF.*;

/** 
 父子流程控件状态
 
*/
public enum FrmSubFlowSta
{
	/** 
	 不可用
	 
	*/
	Disable,
	/** 
	 可用
	 
	*/
	Enable,
	/** 
	 只读
	 
	*/
	Readonly;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FrmSubFlowSta forValue(int value)
	{
		return values()[value];
	}
}