package com.kong.kflowweb.BP.Sys;

import com.kong.kflowweb.BP.DA.*;
import com.kong.kflowweb.BP.En.*;
import com.kong.kflowweb.BP.Sys.XML.*;
import com.kong.kflowweb.BP.XML.XmlEn;
import com.kong.kflowweb.BP.XML.XmlEns;

/** 
 分组内容
 
*/
public class FieldGroupXml extends XmlEn
{
	//#region 属性
	public final String getNo()
	{
		return this.GetValStringByKey("No");
	}
	public final String getName()
	{
		return this.GetValStringByKey(com.kong.kflowweb.BP.Web.WebUser.getSysLang());
	}
	public final String getDesc()
	{
		return this.GetValStringByKey(com.kong.kflowweb.BP.Web.WebUser.getSysLang()+"Desc");
	}

		///#endregion


		
	/** 
	 节点扩展信息
	 
	*/
	public FieldGroupXml()
	{
	}
	/** 
	 获取一个实例s
	 
	*/
	@Override
	public XmlEns getGetNewEntities()
	{
		return new FieldGroupXmls();
	}

		///#endregion
}