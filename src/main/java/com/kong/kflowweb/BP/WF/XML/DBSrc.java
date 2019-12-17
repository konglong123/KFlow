package com.kong.kflowweb.BP.WF.XML;

import com.kong.kflowweb.BP.XML.XmlEnNoName;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 数据源类型
*/
public class DBSrc extends XmlEnNoName
{

		
	/** 
	 数据源类型
	*/
	public final String getSrcType()
	{
		return this.GetValStringByKey(DBSrcAttr.SrcType);
	}
	/** 
	 数据源类型URL
	*/
	public final String getUrl()
	{
		return this.GetValStringByKey(DBSrcAttr.Url);
	}
	/** 
	 数据源类型
	*/
	public DBSrc()
	{
	}
	/** 
	 数据源类型s
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new DBSrcs();
	}
}