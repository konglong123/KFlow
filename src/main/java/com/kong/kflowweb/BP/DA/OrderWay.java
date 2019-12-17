package com.kong.kflowweb.BP.DA;

/**
 * 排序方式
 */
public enum OrderWay
{
	/**
	 * 升序
	 */
	OrderByUp,
	/**
	 * 降序
	 */
	OrderByDown;
	
	public int getValue()
	{
		return this.ordinal();
	}
	
	public static OrderWay forValue(int value)
	{
		return values()[value];
	}
}