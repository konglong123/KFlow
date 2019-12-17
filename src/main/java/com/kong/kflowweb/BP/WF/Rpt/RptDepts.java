package com.kong.kflowweb.BP.WF.Rpt;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.En.Entity;

/** 
 报表部门 
 
*/
public class RptDepts extends Entities
{

		
	/** 
	 报表与部门集合
	 
	*/
	public RptDepts()
	{
	}

		///#endregion


		///#region 方法
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new RptDept();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<RptDept> ToJavaList()
	{
		return (java.util.List<RptDept>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<RptDept> Tolist()
	{
		java.util.ArrayList<RptDept> list = new java.util.ArrayList<RptDept>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((RptDept)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}