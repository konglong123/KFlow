package com.kong.kflowweb.BP.WF.XML;

import java.util.List;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 工具栏s
*/
public class Tools extends XmlEns
{

		
	/** 
	 考核率的数据元素
	*/
	public Tools()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new Tool();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getCCFlowAppPath() + "WF/Style/Tools.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "Item";
	}
	@Override
	public Entities getRefEns()
	{
		return null; //new com.kong.kflowweb.BP.ZF1.AdminTools();
	}
	public List<Tool> ToJavaList()
	{
		return (List<Tool>)(Object)this;
	}
}