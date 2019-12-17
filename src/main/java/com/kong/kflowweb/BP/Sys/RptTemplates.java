package com.kong.kflowweb.BP.Sys;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.En.Entity;
import com.kong.kflowweb.BP.En.QueryObject;

/**
 * 实体集合
 */
public class RptTemplates extends Entities
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//  构造
	public RptTemplates()
	{
	}
	
	/**
	 * 查询
	 * 
	 * @param EnsName
	 * @throws Exception 
	 */
	public RptTemplates(String EnsName) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(RptTemplateAttr.EnsName, EnsName);
		qo.DoQuery();
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new RptTemplate();
	}
	//  查询方法
}