package com.kong.kflowweb.BP.WF.XML;

import java.io.File;
import java.util.List;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 工作一户式s
*/
public class OneWorkXmls extends XmlEns
{

		
	/** 
	 工作一户式s
	*/
	public OneWorkXmls()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new OneWorkXml();
	}
	/** 
	 文件
	*/
	@Override
	public String getFile()
	{
		com.kong.kflowweb.BP.DA.Log.DebugWriteInfo(SystemConfig.getPathOfData() + "XML"+File.separator+"WFAdmin.xml");
		return SystemConfig.getPathOfData() + "XML"+File.separator+"WFAdmin.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "OneWork";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}
	public List<OneWorkXml> ToJavaListXmlEnss()
	{
		return (List<OneWorkXml>)(Object)this;
	}
}