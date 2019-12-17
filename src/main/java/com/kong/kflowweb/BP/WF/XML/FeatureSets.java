package com.kong.kflowweb.BP.WF.XML;

import java.util.List;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 个性化设置s
*/
public class FeatureSets extends XmlEns
{

		
	/** 
	 考核率的数据元素
	*/
	public FeatureSets()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new FeatureSet();
	}
	@Override
	public String getFile()
	{
		return SystemConfig.getPathOfXML() + "FeatureSet.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "Item";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}
	public List<FeatureSet> ToJavaList()
	{
		return (List<FeatureSet>)(Object)this;
	}
}