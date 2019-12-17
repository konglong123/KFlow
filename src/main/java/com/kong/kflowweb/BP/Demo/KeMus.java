package com.kong.kflowweb.BP.Demo;

import com.kong.kflowweb.BP.En.EntitiesNoName;
import com.kong.kflowweb.BP.En.Entity;

/**
 * 科目
 */
public class KeMus extends EntitiesNoName
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 143653647L;
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new KeMu();
	}
	
	/**
	 * 构造方法
	 */
	public KeMus()
	{
	}
}