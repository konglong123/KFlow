package com.kong.kflowweb.BP.WF.XML;

import java.util.List;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 工作明细选项s
 
*/
public class WorkOptDtlXmls extends XmlEns
{

		
	/** 
	 工作明细选项s
	*/
	public WorkOptDtlXmls()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new WorkOptDtlXml();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfWebApp() + "WF/Style/Tools.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "WorkOptDtl";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}
	public List<WorkOptDtlXml> ToJavaList()
	{
		return (List<WorkOptDtlXml>)(Object)this;
	}
}