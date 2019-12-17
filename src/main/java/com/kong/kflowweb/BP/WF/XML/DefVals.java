package com.kong.kflowweb.BP.WF.XML;

import java.util.List;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 默认值s
*/
public class DefVals extends XmlEns
{

		
	/** 
	 考核率的数据元素
	*/
	public DefVals()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new DefVal();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfData() + "XML/DefVal.xml";
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
		return null;
	}
	public List<DefVal> ToJavaList()
	{
		return (List<DefVal>)(Object)this;
	}
}