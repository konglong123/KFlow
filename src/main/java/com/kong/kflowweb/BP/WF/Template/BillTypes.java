package com.kong.kflowweb.BP.WF.Template;

import com.kong.kflowweb.BP.En.EntitiesNoName;
import com.kong.kflowweb.BP.En.Entity;

/** 
 单据类型
*/
public class BillTypes extends EntitiesNoName
{
	/** 
	 单据类型s
	*/
	public BillTypes()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new BillType();
	}
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<BillType> ToJavaList()
	{
		return (java.util.List<BillType>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<BillType> Tolist()
	{
		java.util.ArrayList<BillType> list = new java.util.ArrayList<BillType>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((BillType)this.get(i));
		}
		return list;
	}
}