package com.kong.kflowweb.BP.WF.XML;

import com.kong.kflowweb.BP.XML.XmlEnNoName;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 公文左边谓词
*/
public class GovWordLeft extends XmlEnNoName
{


		
	/** 
	 公文左边谓词
	*/
	public GovWordLeft()
	{
	}
	/** 
	 公文左边谓词s
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new GovWordLefts();
	}
}