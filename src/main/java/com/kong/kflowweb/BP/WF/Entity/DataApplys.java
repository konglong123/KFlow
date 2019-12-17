package com.kong.kflowweb.BP.WF.Entity;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.En.Entity;

/**
 * 追加时间申请s
 */
public class DataApplys extends Entities
{
	// 构造
	/**
	 * 追加时间申请s
	 */
	public DataApplys()
	{
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new DataApply();
	}
}