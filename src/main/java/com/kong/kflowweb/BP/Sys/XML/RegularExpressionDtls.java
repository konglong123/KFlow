package com.kong.kflowweb.BP.Sys.XML;

import java.util.ArrayList;
import java.util.List;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.Sys.GENoName;
import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 
 
*/
public class RegularExpressionDtls extends XmlEns
{
	
	public static ArrayList<RegularExpressionDtl> convertRegularExpressionDtls(
			Object obj)
	{
		return (ArrayList<RegularExpressionDtl>) obj;
	}
	public List<RegularExpressionDtl> ToJavaList()
	{
		return (List<RegularExpressionDtl>)(Object)this;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造
	/**
	 * 考核率的数据元素
	 */
	public RegularExpressionDtls()
	{
	}
	
	// 重写基类属性或方法。
	/**
	 * 得到它的 Entity
	 */
	@Override
	public XmlEn getGetNewEntity()
	{
		return new RegularExpressionDtl();
	}
	
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfData() + "/XML/RegularExpression.xml";
	}
	
	/**
	 * 物理表名
	 */
	@Override
	public String getTableName()
	{
		return "Dtl";
	}
	
	@Override
	public Entities getRefEns()
	{
		return null;
	}
}