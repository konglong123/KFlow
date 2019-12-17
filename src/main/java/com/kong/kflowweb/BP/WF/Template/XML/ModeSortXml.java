package com.kong.kflowweb.BP.WF.Template.XML;

import com.kong.kflowweb.BP.XML.XmlEnNoName;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 模式
 
*/
public class ModeSortXml extends XmlEnNoName
{

		
	/** 
	 节点扩展信息
	 
	*/
	public ModeSortXml()
	{
	}
	/** 
	 获取一个实例
	 
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new ModeSortXmls();
	}

		///#endregion
}