package com.kong.kflowweb.BP.Sys;

import com.kong.kflowweb.BP.En.EntitiesNoName;
import com.kong.kflowweb.BP.En.Entity;

public class ExcelFiles extends EntitiesNoName
{

	///#region 属性
	/** 
	 生成Excel模板实体
	 
	*/
	@Override
	public Entity getGetNewEntity()
	{
		return new ExcelFile();
	}

	///#endregion 属性


	///#region 构造方法
	public ExcelFiles()
	{
	}

	///#endregion 构造方法
}
