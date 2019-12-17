package com.kong.kflowweb.BP.Sys;

import com.kong.kflowweb.BP.DA.*;
import com.kong.kflowweb.BP.En.*;

/** 
 附件上传方式
 
*/
public enum AthUploadWay
{
	/** 
	 继承模式
	 
	*/
	Inherit,
	/** 
	 协作模式
	 
	*/
	Interwork;

	public int getValue()
	{
		return this.ordinal();
	}

	public static AthUploadWay forValue(int value)
	{
		return values()[value];
	}
}