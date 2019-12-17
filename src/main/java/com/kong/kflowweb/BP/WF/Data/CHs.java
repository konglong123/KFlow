package com.kong.kflowweb.BP.WF.Data;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.En.Entity;

/** 
 时效考核s
*/
public class CHs extends Entities
{

		
	/** 
	 时效考核s
	*/
	public CHs()
	{
	}

	/** 
	 时效考核
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new CH();
	}
		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 @return List
	*/
	public final java.util.List<CH> ToJavaList()
	{
		return (java.util.List<CH>)(Object)this;
	}
	/** 
	 转化成list
	 @return List
	*/
	public final java.util.ArrayList<CH> Tolist()
	{
		java.util.ArrayList<CH> list = new java.util.ArrayList<CH>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((CH)this.get(i));
		}
		return list;
	}
}