package com.kong.kflowweb.BP.WF;

/** 
 流程类型
 
*/
public enum FlowType_del
{
	/** 
	 平面流程
	 
	*/
	Panel,
	/** 
	 分合流
	 
	*/
	FHL;

	public int getValue()
	{
		return this.ordinal();
	}

	public static FlowType_del forValue(int value)
	{
		return values()[value];
	}
}