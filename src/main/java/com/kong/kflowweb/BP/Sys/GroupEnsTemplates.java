package com.kong.kflowweb.BP.Sys;

import com.kong.kflowweb.BP.En.EntitiesOID;
import com.kong.kflowweb.BP.En.Entity;
import com.kong.kflowweb.BP.En.QueryObject;

/**
 * 实体集合
 */
public class GroupEnsTemplates extends EntitiesOID
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造
	public GroupEnsTemplates()
	{
	}
	
	/**
	 * @param emp
	 * @throws Exception 
	 */
	public GroupEnsTemplates(String emp) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(GroupEnsTemplateAttr.Rec, emp);
		qo.addOr();
		qo.AddWhere(GroupEnsTemplateAttr.Rec, "admin");
		qo.DoQuery();
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new GroupEnsTemplate();
	}
	// 查询方法
}