package com.kong.kflowweb.BP.WF.Template.XML;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 表单事件
 
*/
public class FrmEventXmls extends XmlEns
{

		
	/** 
	 考核率的数据元素
	 
	*/
	public FrmEventXmls()
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
		return new FrmEventXml();
	}
	@Override
	public String getFile()
	{
		   // return SystemConfig.getPathOfWebApp() + "\\WF\\MapDef\\Style\\XmlDB.xml";

		return SystemConfig.getPathOfData() + "/XML/XmlDB.xml";

	}
	/** 
	 物理表名
	 
	*/
	@Override
	public String getTableName()
	{
		return "FrmEvent";
	}
	@Override
	public Entities getRefEns()
	{
		return null; //new com.kong.kflowweb.BP.ZF1.AdminTools();
	}

		///#endregion

}