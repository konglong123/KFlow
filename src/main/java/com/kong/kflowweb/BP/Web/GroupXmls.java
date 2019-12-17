package com.kong.kflowweb.BP.Web;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlMenus;

public class GroupXmls extends XmlMenus
{
	// 构造
	/**
	 * 分组菜单s
	 */
	public GroupXmls()
	{
	}
	
	// 重写基类属性或方法。
	/**
	 * 得到它的 Entity
	 */
	@Override
	public XmlEn getGetNewEntity()
	{
		return new GroupXml();
	}
	
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfXML() + "Ens/Group.xml";
	}
	
	/**
	 * 物理表名
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
