package com.kong.kflowweb.BP.WF.XML;

import com.kong.kflowweb.BP.XML.XmlEnNoName;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 在线帮助
*/
public class OnlineHelper extends XmlEnNoName
{
		
	/** 
	 在线帮助
	*/
	public OnlineHelper()
	{
	}
	/** 
	 在线帮助
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new OnlineHelpers();
	}
}