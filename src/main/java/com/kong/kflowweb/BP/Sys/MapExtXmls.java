package com.kong.kflowweb.BP.Sys;

import java.util.List;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 
 
*/
public class MapExtXmls extends XmlEns
{

		
	/** 
	 考核率的数据元素
	 
	*/
	public MapExtXmls()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new MapExtXml();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfXML() + "MapExt.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "FieldExt";
	}
	
	@Override
	public Entities getRefEns()
	{
		return null; //new com.kong.kflowweb.BP.ZF1.AdminTools();
	}
	public List<MapExtXml> ToJavaList()
	{
		return (List<MapExtXml>)(Object)this;
	}
}