package com.kong.kflowweb.BP.En;

import java.util.ArrayList;
import java.util.List;

/** 
 
 
*/
public class RefMethods extends ArrayList<RefMethod>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 加入
	 * 
	 * @param attr
	 *            attr
	 */
	public final void Add(RefMethod en)
	{
		if (this.IsExits(en))
		{
			return;
		}
		en.Index = this.size();
		this.add(en);
		/*
		 * warning en.Index = this.InnerList.size(); this.add(en);
		 */
	}
	
	/**
	 * 是不是存在集合里面
	 * 
	 * @param en
	 *            要检查的RefMethod
	 * @return true/false
	 */
	public final boolean IsExits(RefMethod en)
	{
		for (RefMethod dtl : this)
		{
			if (dtl.ClassMethodName.equals(en.ClassMethodName))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 能够看到的属性
	 */
	public final int getCountOfVisable()
	{
		int i = 0;
		for (RefMethod rm : this)
		{
			if (rm.Visable)
			{
				i++;
			}
		}
		return i;
	}
	
	/**
	 * 根据索引访问集合内的元素Attr。
	 */
	public final RefMethod getItem(int index)
	{
		return this.get(index);
		/*
		 * warning return (RefMethod)this.get(index);
		 */
	}
	
	public List<RefMethod> ToJavaList()
	{
		return (List<RefMethod>)(Object)this;
	}
}