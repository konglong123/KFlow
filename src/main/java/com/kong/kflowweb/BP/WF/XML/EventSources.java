package com.kong.kflowweb.BP.WF.XML;

import java.util.List;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 事件源s
 
*/
public class EventSources extends XmlEns
{
	/** 
	 事件源s
	*/
	public EventSources()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new EventSource();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfXML() + "/EventList.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "Source";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}
	public List<EventSource> ToJavaList()
	{
		return (List<EventSource>)(Object)this;
	}
}