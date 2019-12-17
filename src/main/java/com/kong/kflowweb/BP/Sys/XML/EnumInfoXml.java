package com.kong.kflowweb.BP.Sys.XML;

import com.kong.kflowweb.BP.Web.WebUser;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/**
 * EnumInfoXml 的摘要说明，属性的配置。
 */
public class EnumInfoXml extends XmlEn
{
	// 属性
	public final String getKey()
	{
		return this.GetValStringByKey("Key");
	}
	
	/**
	 * Vals
	 */
	public final String getVals()
	{
		return this.GetValStringByKey(WebUser.getSysLang());
	}
	
	// 构造
	public EnumInfoXml()
	{
	}
	
	public EnumInfoXml(String key)
	{
		this.RetrieveByPK("Key", key);
	}
	
	/**
	 * 获取一个实例
	 */
	@Override
	public XmlEns getGetNewEntities()
	{
		return new EnumInfoXmls();
	}
}