package com.kong.kflowweb.BP.WF.XML;

import com.kong.kflowweb.BP.XML.XmlEnNoName;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 公文右边谓词
 
*/
public class GovWordRight extends XmlEnNoName
{

		
	/** 
	 公文右边谓词
	*/
	public GovWordRight()
	{
	}
	/** 
	 公文右边谓词s
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new GovWordRights();
	}
}