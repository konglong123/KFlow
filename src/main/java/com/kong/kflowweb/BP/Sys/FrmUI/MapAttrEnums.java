package com.kong.kflowweb.BP.Sys.FrmUI;

import java.util.ArrayList;
import java.util.List;

import com.kong.kflowweb.BP.En.EntitiesMyPK;
import com.kong.kflowweb.BP.En.Entity;

/** 
实体属性s
*/
public class MapAttrEnums extends EntitiesMyPK
{
	/** 
	 实体属性s
	*/
	public MapAttrEnums()
	{
	}
	/** 
	 得到它的 Entity
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new MapAttrEnum();
	}
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final List<MapAttrEnum> ToJavaList()
	{
		return (List<MapAttrEnum>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final ArrayList<MapAttrEnum> Tolist()
	{
		ArrayList<MapAttrEnum> list = new ArrayList<MapAttrEnum>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((MapAttrEnum)this.get(i));
		}
		return list;
	}
}

