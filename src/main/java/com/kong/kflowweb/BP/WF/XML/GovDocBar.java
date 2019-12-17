package com.kong.kflowweb.BP.WF.XML;

import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 公文
*/
public class GovDocBar extends XmlEn
{

		
	public final String getNo()
	{
		return this.GetValStringByKey("No");
	}
	public final String getName()
	{
		return this.GetValStringByKey(com.kong.kflowweb.BP.Web.WebUser.getSysLang());
	}
	/** 
	 图片
	*/
	public final String getImg()
	{
		return this.GetValStringByKey("Img");
	}
	public final String getTitle()
	{
		return this.GetValStringByKey("Title");
	}
	public final String getUrl()
	{
		return this.GetValStringByKey("Url");
	}
		
	/** 
	 节点扩展信息
	*/
	public GovDocBar()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new GovDocBars();
	}
}