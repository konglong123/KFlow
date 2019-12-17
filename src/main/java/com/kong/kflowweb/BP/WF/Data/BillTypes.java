package com.kong.kflowweb.BP.WF.Data;

import com.kong.kflowweb.BP.En.Entity;
import com.kong.kflowweb.BP.En.SimpleNoNames;

/**
 * 单据类型
 */
public class BillTypes extends SimpleNoNames
{
	/**
	 * 单据类型s
	 */
	public BillTypes()
	{
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new BillType();
	}
}