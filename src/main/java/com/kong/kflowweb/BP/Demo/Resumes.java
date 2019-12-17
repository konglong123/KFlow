package com.kong.kflowweb.BP.Demo;

import com.kong.kflowweb.BP.En.EntitiesOID;
import com.kong.kflowweb.BP.En.Entity;

/**
 * 简历组
 */
public class Resumes extends EntitiesOID
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1242424L;
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new Resume();
	}
	
	/**
	 * 构造函数
	 */
	public Resumes()
	{
	}
}