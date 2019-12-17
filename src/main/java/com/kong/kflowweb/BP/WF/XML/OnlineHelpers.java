package com.kong.kflowweb.BP.WF.XML;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 在线帮助s
*/
public class OnlineHelpers extends XmlEns
{
	/** 
	 在线帮助s
	*/
	public OnlineHelpers()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new OnlineHelper();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfWebApp() + "WF/OnlineHepler/";
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
}