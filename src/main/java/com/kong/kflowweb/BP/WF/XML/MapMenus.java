package com.kong.kflowweb.BP.WF.XML;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 映射菜单s
*/
public class MapMenus extends XmlEns
{

		
	/** 
	 映射菜单s
	*/
	public MapMenus()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new MapMenu();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfData() + "XML/XmlDB.xml";

	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "MapMenu";
	}
	@Override
	public Entities getRefEns()
	{
		return null; //new com.kong.kflowweb.BP.ZF1.AdminTools();
	}
}