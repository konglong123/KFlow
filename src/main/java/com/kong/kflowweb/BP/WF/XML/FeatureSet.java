package com.kong.kflowweb.BP.WF.XML;

import com.kong.kflowweb.BP.XML.XmlEnNoName;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 个性化设置
*/
public class FeatureSet extends XmlEnNoName
{

		
	public final String getName()
	{
		return this.GetValStringByKey(com.kong.kflowweb.BP.Web.WebUser.getSysLang());
	}
	/** 
	 节点扩展信息
	*/
	public FeatureSet()
	{
	}
	public FeatureSet(String no)
	{

	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new FeatureSets();
	}
}