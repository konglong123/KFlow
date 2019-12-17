package com.kong.kflowweb.BP.WF.XML;

import java.util.List;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 消息类型s
*/
public class MsgTypes extends XmlEns
{

		
	/** 
	 考核率的数据元素
	*/
	public MsgTypes()
	{
	}
	/** 
	 得到它的 Entity 
	*/
	@Override
	public XmlEn getGetNewEntity()
	{
		return new MsgType();
	}
	/** 
	 XML文件位置.
	*/
	@Override
	public String getFile()
	{
		return SystemConfig.getCCFlowAppPath() + "WF/Data/XML/SysDataType.xml";
	}
	/** 
	 物理表名
	*/
	@Override
	public String getTableName()
	{
		return "MsgType";
	}
	@Override
	public Entities getRefEns()
	{
		return null;
	}
	public List<MsgType> toJavaList()
	{
		return (List<MsgType>)(Object)this;
	}

}