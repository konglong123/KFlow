package com.kong.kflowweb.BP.WF.XML;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.WF.Port.Dept;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 
*/
public class AdminMenus extends XmlEns
{
		
	/** 
	 考核率的数据元素
	*/
	public AdminMenus()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new AdminMenu();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfWebApp() + "/DataUser/XML/AdminMenu.xml";
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
		return null; //new com.kong.kflowweb.BP.ZF1.AdminAdminMenus();
	}
	
	public final java.util.List<AdminMenu> ToJavaList()
	{
		return (java.util.List<AdminMenu>)(Object)this;
	}
}