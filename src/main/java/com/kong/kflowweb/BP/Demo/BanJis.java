package com.kong.kflowweb.BP.Demo;

import com.kong.kflowweb.BP.En.EntitiesNoName;
import com.kong.kflowweb.BP.En.Entity;

/**
 * 班级组
 */
public class BanJis extends EntitiesNoName
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 124553636L;
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new BanJi();
	}
	
	/**
	 * 构造方法
	 */
	public BanJis()
	{
	}
}