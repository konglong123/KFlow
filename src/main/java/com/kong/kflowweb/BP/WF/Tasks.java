package com.kong.kflowweb.BP.WF;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.En.Entity;

/**
 * 任务
 */
public class Tasks extends Entities
{
	// 方法
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new Task();
	}
	
	/**
	 * 任务
	 */
	public Tasks()
	{
	}
}