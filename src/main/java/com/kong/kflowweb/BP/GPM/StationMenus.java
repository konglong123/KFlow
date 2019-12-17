package com.kong.kflowweb.BP.GPM;

import com.kong.kflowweb.BP.DA.*;
import com.kong.kflowweb.BP.Web.*;
import com.kong.kflowweb.BP.En.*;

/** 
 岗位菜单s
 
*/
public class StationMenus extends EntitiesMM
{

		///#region 构造
	/** 
	 岗位s
	 
	*/
	public StationMenus()
	{
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new StationMenu();
	}

		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<StationMenu> ToJavaList()
	{
		return (java.util.List<StationMenu>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<StationMenu> Tolist()
	{
		java.util.ArrayList<StationMenu> list = new java.util.ArrayList<StationMenu>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((StationMenu)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}