package com.kong.kflowweb.BP.WF.XML;

import com.kong.kflowweb.BP.WF.Plant;
import com.kong.kflowweb.BP.XML.XmlEnNoName;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 工作一户式
*/
public class OneWorkXml extends XmlEnNoName
{

	public final String getName()
	{
		return this.GetValStringByKey(com.kong.kflowweb.BP.Web.WebUser.getSysLang());
	}
	public final String getURL()
	{
		if (com.kong.kflowweb.BP.WF.Glo.Plant == Plant.CCFlow)
            return this.GetValStringByKey("UrlCCFlow");
		return this.GetValStringByKey("UrlJFlow");
	}
	/** 
	 节点扩展信息
	*/
	public OneWorkXml()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new OneWorkXmls();
	}
}