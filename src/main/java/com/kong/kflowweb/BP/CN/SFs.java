package com.kong.kflowweb.BP.CN;

import java.util.ArrayList;

import com.kong.kflowweb.BP.En.EntitiesNoName;
import com.kong.kflowweb.BP.En.Entity;

/**
 * 省份s
 */
public class SFs extends EntitiesNoName
{
	// 省份.
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new SF();
	}
	
	public static ArrayList<SF> convertSFs(Object obj)
	{
		return (ArrayList<SF>) obj;
	}
	
	// 构造方法
	/**
	 * 省份s
	 */
	public SFs()
	{
	}
}
