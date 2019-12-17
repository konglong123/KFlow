package com.kong.kflowweb.BP.Web;

import com.kong.kflowweb.BP.XML.XmlEnNoName;
import com.kong.kflowweb.BP.XML.XmlEns;

public class GroupXml extends XmlEnNoName
{
	// 构造
	/**
	 * 分组菜单
	 */
	public GroupXml()
	{
		
	}
	
	/**
	 * 分组菜单
	 * 
	 * @param no
	 *            编号
	 */
	public GroupXml(String no)
	{
		this.RetrieveByPK(GroupXmlAttr.No, no);
	}
	
	/**
	 * 获取一个实例
	 */
	@Override
	public XmlEns getGetNewEntities()
	{
		return new GroupXmls();
	}
}
