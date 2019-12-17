package com.kong.kflowweb.BP.GPM;

import com.kong.kflowweb.BP.DA.*;
import com.kong.kflowweb.BP.Web.*;
import com.kong.kflowweb.BP.En.*;

/** 
 用户菜单s
 
*/
public class VGPMEmpMenus extends EntitiesMM
{

		///#region 构造
	/** 
	 用户s
	 
	*/
	public VGPMEmpMenus()
	{
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new VGPMEmpMenu();
	}

		///#endregion


		///#region 为了适应自动翻译成java的需要,把实体转换成List.
	/** 
	 转化成 java list,C#不能调用.
	 
	 @return List
	*/
	public final java.util.List<VGPMEmpMenu> ToJavaList()
	{
		return (java.util.List<VGPMEmpMenu>)(Object)this;
	}
	/** 
	 转化成list
	 
	 @return List
	*/
	public final java.util.ArrayList<VGPMEmpMenu> Tolist()
	{
		java.util.ArrayList<VGPMEmpMenu> list = new java.util.ArrayList<VGPMEmpMenu>();
		for (int i = 0; i < this.size(); i++)
		{
			list.add((VGPMEmpMenu)this.get(i));
		}
		return list;
	}

		///#endregion 为了适应自动翻译成java的需要,把实体转换成List.
}