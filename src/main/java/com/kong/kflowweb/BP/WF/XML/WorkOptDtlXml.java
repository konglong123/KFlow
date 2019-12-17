package com.kong.kflowweb.BP.WF.XML;

import com.kong.kflowweb.BP.XML.XmlEnNoName;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 工作明细选项
*/
public class WorkOptDtlXml extends XmlEnNoName
{
	/** 
	 名称
	*/
	public final String getName()
	{
		return this.GetValStringByKey(com.kong.kflowweb.BP.Web.WebUser.getSysLang());
	}
	/** 
	 超链接
	*/
	public final String getURL()
	{
		return this.GetValStringByKey("URL");
	}
		
	/** 
	 节点扩展信息
	*/
	public WorkOptDtlXml()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new WorkOptDtlXmls();
	}
	
}