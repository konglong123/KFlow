package com.kong.kflowweb.BP.WF.Entity;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.En.Entity;

public class RememberMes extends Entities
{
	// 方法
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new RememberMe();
	}
	
	/**
	 * RememberMe
	 */
	public RememberMes()
	{
	}
}