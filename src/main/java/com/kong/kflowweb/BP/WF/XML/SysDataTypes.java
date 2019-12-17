package com.kong.kflowweb.BP.WF.XML;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 
 
*/
public class SysDataTypes extends XmlEns
{

		
	/** 
	 考核率的数据元素
	*/
	public SysDataTypes()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new SysDataType();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfData() + "XML/SysDataType.xml";
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
}