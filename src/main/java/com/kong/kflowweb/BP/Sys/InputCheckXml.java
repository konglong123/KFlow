package com.kong.kflowweb.BP.Sys;

import com.kong.kflowweb.BP.Web.WebUser;
import com.kong.kflowweb.BP.XML.XmlEnNoName;
import com.kong.kflowweb.BP.XML.XmlEns;

public class InputCheckXml extends XmlEnNoName
{
	// 属性
	public final String getName()
	{
		return this.GetValStringByKey(WebUser.getSysLang());
	}
	
	// 构造
	/**
	 * 节点扩展信息
	 */
	public InputCheckXml()
	{
	}
	
	public InputCheckXml(String no)
	{
		
	}
	
	/**
	 * 获取一个实例
	 */
	@Override
	public XmlEns getGetNewEntities()
	{
		return new InputCheckXmls();
	}
}