package com.kong.kflowweb.BP.WF.XML;

import com.kong.kflowweb.BP.XML.XmlEnNoName;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 抄送菜单
*/
public class CCMenu extends XmlEnNoName
{

		
	/** 
	 节点扩展信息
	*/
	public CCMenu()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new CCMenus();
	}
}