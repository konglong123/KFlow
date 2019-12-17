package com.kong.kflowweb.BP.Sys;

import com.kong.kflowweb.BP.XML.XmlEnNoName;
import com.kong.kflowweb.BP.XML.XmlEns;

public class MapExtXml extends XmlEnNoName
{

		
	public final String getName()
	{
		return this.GetValStringByKey(com.kong.kflowweb.BP.Web.WebUser.getSysLang());
	}
	public final String getURL()
	{
		return this.GetValStringByKey("URL");
	}

		///#endregion


		
	/** 
	 节点扩展信息
	 
	*/
	public MapExtXml()
	{
	}
	/** 
	 获取一个实例
	 
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new MapExtXmls();
	}

		///#endregion
}