package com.kong.kflowweb.BP.En;

import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlMenus;

/**
 * 取值s
 */
public class FrmPopVals extends XmlMenus
{
	// /#region 构造
	/**
	 * 取值s
	 */
	public FrmPopVals()
	{
	}
	
	// 重写基类属性或方法。
	/**
	 * 得到它的 Entity
	 */
	@Override
	public XmlEn getGetNewEntity()
	{
		return new FrmPopVal();
	}
	
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfDataUser() + "XML/FrmPopVal.xml";
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