package com.kong.kflowweb.BP.WF.XML;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 流程一户式s
 
*/
public class RptXmls extends XmlEns
{

		
	/** 
	 流程一户式s
	*/
	public RptXmls()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new RptXml();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfData() + "XML/WFAdmin.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "RptFlow";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}
}