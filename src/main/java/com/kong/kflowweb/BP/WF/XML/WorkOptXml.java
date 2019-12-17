package com.kong.kflowweb.BP.WF.XML;

import com.kong.kflowweb.BP.XML.XmlEnNoName;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 工作选项
*/
public class WorkOptXml extends XmlEnNoName
{


	public final String getName()
	{
		return this.GetValStringByKey(com.kong.kflowweb.BP.Web.WebUser.getSysLang());
	}
	public final String getCSS()
	{
		return this.GetValStringByKey("CSS");
	}

	public final String getURL()
	{
		return this.GetValStringByKey("URL");
	}
	/** 
	 节点扩展信息
	*/
	public WorkOptXml()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new WorkOptXmls();
	}
}