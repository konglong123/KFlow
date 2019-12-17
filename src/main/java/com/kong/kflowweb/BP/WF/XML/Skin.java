package com.kong.kflowweb.BP.WF.XML;

import com.kong.kflowweb.BP.XML.XmlEnNoName;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 皮肤
*/
public class Skin extends XmlEnNoName
{
	public final String getName()
	{
		return this.GetValStringByKey(com.kong.kflowweb.BP.Web.WebUser.getSysLang());
	}
	public final String getCSS()
	{
		return this.GetValStringByKey("CSS");
	}


		
	/** 
	 节点扩展信息
	*/
	public Skin()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new Skins();
	}
}