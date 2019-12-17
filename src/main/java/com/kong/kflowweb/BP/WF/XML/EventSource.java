package com.kong.kflowweb.BP.WF.XML;

import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 事件源
 
*/
public class EventSource extends XmlEn
{

		
	public final String getNo()
	{
		return this.GetValStringByKey("No");
	}
	public final String getName()
	{
		return this.GetValStringByKey(com.kong.kflowweb.BP.Web.WebUser.getSysLang());
	}
	/** 
	 事件源
	*/
	public EventSource()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new EventSources();
	}
}