package com.kong.kflowweb.BP.WF.XML;

import com.kong.kflowweb.BP.XML.XmlEnNoName;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 消息类型
*/
public class MsgType extends XmlEnNoName
{

		
	/** 
	 节点扩展信息
	*/
	public MsgType()
	{
	}
	/** 
	 获取一个实例
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new MsgTypes();
	}
}