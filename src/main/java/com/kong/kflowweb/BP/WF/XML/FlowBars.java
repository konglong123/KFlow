package com.kong.kflowweb.BP.WF.XML;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 流程按钮s
*/
public class FlowBars extends XmlEns
{

		
	/** 
	 考核率的数据元素
	*/
	public FlowBars()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new FlowBar();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfWebApp() + "DataUser/XML/BarOfTop.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "FlowBar";
	}
	@Override
	public Entities getRefEns()
	{
		return null; //new com.kong.kflowweb.BP.ZF1.AdminTools();
	}
}