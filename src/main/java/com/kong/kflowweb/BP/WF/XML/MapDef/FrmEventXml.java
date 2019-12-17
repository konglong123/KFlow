package com.kong.kflowweb.BP.WF.XML.MapDef;

import com.kong.kflowweb.BP.Web.WebUser;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/**
 * 表单事件
 */
public class FrmEventXml extends XmlEn
{
	// 属性
	public final String getNo()
	{
		return this.GetValStringByKey("No");
	}
	
	public final String getName()
	{
		return this.GetValStringByKey(WebUser.getSysLang());
	}
	
	/**
	 * 图片
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
		String url = this.GetValStringByKey("Url");
		if (url.equals(""))
		{
			url = "javascript:" + this.GetValStringByKey("OnClick");
		}
		return url;
	}
	
	// 构造
	/**
	 * 表单事件
	 */
	public FrmEventXml()
	{
	}
	
	/**
	 * 获取一个实例
	 */
	@Override
	public XmlEns getGetNewEntities()
	{
		return new FrmEventXmls();
	}
}