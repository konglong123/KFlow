package com.kong.kflowweb.BP.Sys;

import com.kong.kflowweb.BP.En.EntitiesMyPK;
import com.kong.kflowweb.BP.En.Entity;

/**
 * 列选择s
 */
public class CFields extends EntitiesMyPK
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 列选择s
	 */
	public CFields()
	{
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new CField();
	}
}