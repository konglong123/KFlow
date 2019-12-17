package com.kong.kflowweb.BP.WF.XML;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 方向条件类型s
*/
public class CondTypeXmls extends XmlEns
{

		
	/** 
	 方向条件类型s
	*/
	public CondTypeXmls()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new CondTypeXml();
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
		return "CondType";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}
}