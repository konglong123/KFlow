package com.kong.kflowweb.BP.WF.Template.XML;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 模式s
 
*/
public class ModeSortXmls extends XmlEns
{

		
	/** 
	 模式s
	 
	*/
	public ModeSortXmls()
	{
	}

		///#endregion


		///#region 重写基类属性或方法。
	/** 
	 得到它的 Entity 
	 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new ModeSortXml();
	}
	/** 
	 文件位置
	 
	*/
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfWebApp() + "/WF/Admin/AccepterRole/AccepterRole.xml";

	}
	/** 
	 物理表名
	 
	*/
	@Override
	public String getTableName()
	{
		return "ModelSort";
	}
	/** 
	 关联的实体
	 
	*/
	@Override
	public Entities getRefEns()
	{
		return null;
	}

		///#endregion

}