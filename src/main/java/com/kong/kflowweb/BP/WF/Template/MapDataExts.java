package com.kong.kflowweb.BP.WF.Template;

import com.kong.kflowweb.BP.En.EntitiesNoName;
import com.kong.kflowweb.BP.En.Entity;

/** 
 表单属性s
 
*/
public class MapDataExts extends EntitiesNoName
{

		
	/** 
	 表单属性s
	 
	*/
	public MapDataExts()
	{
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapDataExt();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<MapDataExt> ToJavaList()
	{
		return (java.util.List<MapDataExt>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<MapDataExt> Tolist()
	{
		java.util.ArrayList<MapDataExt> list = new java.util.ArrayList<MapDataExt>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapDataExt)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}