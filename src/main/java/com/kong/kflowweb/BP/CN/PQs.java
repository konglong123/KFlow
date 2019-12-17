package com.kong.kflowweb.BP.CN;

import com.kong.kflowweb.BP.En.EntitiesNoName;
import com.kong.kflowweb.BP.En.Entity;

/**
 * 片区
 */
public class PQs extends EntitiesNoName
{
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new PQ();
	}
	
	// 构造方法
	/**
	 * 片区s
	 */
	public PQs()
	{
	}
}
