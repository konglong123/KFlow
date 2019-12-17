package com.kong.kflowweb.BP.Web;

public enum UserWorkDev
{
		PC,
		Mobile,
		TablePC;

		public int getValue()
		{
			return this.ordinal();
		}

		public static UserWorkDev forValue(int value)
		{
			return values()[value];
		}
}
