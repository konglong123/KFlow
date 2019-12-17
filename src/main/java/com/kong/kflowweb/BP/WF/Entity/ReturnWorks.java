package com.kong.kflowweb.BP.WF.Entity;

import java.util.ArrayList;
import java.util.List;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.En.Entity;
import com.kong.kflowweb.BP.Sys.XML.RegularExpressionDtl;

/**
 * 退回轨迹s
 */
public class ReturnWorks extends Entities
{
	// 构造
	
	public static ArrayList<ReturnWork> convertReturnWorks(Object obj)
	{
		return (ArrayList<ReturnWork>) obj;
	}
	public List<ReturnWork> ToJavaList()
	{
		return (List<ReturnWork>)(Object)this;
	}
	/**
	 * 退回轨迹s
	 */
	public ReturnWorks()
	{
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new ReturnWork();
	}
}