package com.kong.kflowweb.BP.Sys;

import java.util.ArrayList;
import java.util.List;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 
 
*/
public class ExpFucnXmls extends XmlEns
{
	
	public static ArrayList<ExpFucnXml> convertExpFucnXmls(Object obj)
	{
		return (ArrayList<ExpFucnXml>) obj;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 构造
	/**
	 * 考核率的数据元素
	 */
	public ExpFucnXmls()
	{
	}
	
	// 重写基类属性或方法。
	/**
	 * 得到它的 Entity
	 */
	@Override
	public XmlEn getGetNewEntity()
	{
		return new ExpFucnXml();
	}
	
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfXML() + "MapExt.xml";
	}
	
	/**
	 * 物理表名
	 */
	@Override
	public String getTableName()
	{
		return "ExpFunc";
	}
	
	@Override
	public Entities getRefEns()
	{
		return null; // new com.kong.kflowweb.BP.ZF1.AdminTools();
	}
	public List<ExpFucnXml> ToJavaList()
	{
		return (List<ExpFucnXml>)(Object)this;
	}
}