package com.kong.kflowweb.BP.WF.HttpHandler;

import com.kong.kflowweb.BP.DA.*;
import com.kong.kflowweb.BP.Difference.Handler.WebContralBase;
import com.kong.kflowweb.BP.Sys.*;
import com.kong.kflowweb.BP.Web.*;
import com.kong.kflowweb.BP.Port.*;
import com.kong.kflowweb.BP.En.*;
import com.kong.kflowweb.BP.WF.*;
import com.kong.kflowweb.BP.WF.Template.*;

/** 
 页面功能实体
 
*/
public class CCMobile_MyFlow extends WebContralBase
{
	/**
	 * 构造函数
	 */
	public CCMobile_MyFlow()
	{
	
	}
	
	/** 
	 获得工作节点
	 
	 @return 
	 * @throws Exception 
	*/
	public final String GenerWorkNode() throws Exception
	{
		WF_MyFlow en = new WF_MyFlow(this.context);
		return en.GenerWorkNode();
	}
	/** 
	 获得toolbar
	 
	 @return 
	 * @throws Exception 
	*/
	public final String InitToolBar() throws Exception
	{
		WF_MyFlow en = new WF_MyFlow(this.context);
		return en.InitToolBarForMobile();
	}
	public final String MyFlow_Init() throws Exception
	{
		WF_MyFlow en = new WF_MyFlow(this.context);
		return en.MyFlow_Init();
	}

	public final String MyFlow_StopFlow() throws Exception
	{
		WF_MyFlow en = new WF_MyFlow(this.context);
		return en.MyFlow_StopFlow();
	}
	public final String Save() throws Exception
	{
		WF_MyFlow en = new WF_MyFlow(this.context);
		return en.Save();
	}
	public final String Send() throws Exception
	{
		WF_MyFlow en = new WF_MyFlow(this.context);
		return en.Send();
	}

	public final String Focus() throws Exception
    {
        com.kong.kflowweb.BP.WF.Dev2Interface.Flow_Focus( this.getWorkID());
        return "设置成功.";
    }
	
	public String StartGuide_Init() throws Exception
    {
        WF_MyFlow en = new WF_MyFlow(this.context);
        return en.StartGuide_Init();
    }
	
	public final String FrmGener_Init() throws Exception
	{
		WF_CCForm ccfrm = new WF_CCForm(this.context);
		return ccfrm.FrmGener_Init();
	}
	public final String FrmGener_Save() throws Exception
	{
		WF_CCForm ccfrm = new WF_CCForm(this.context);
		return ccfrm.FrmGener_Save();
	}

	public String MyFlowGener_Delete() throws Exception
    {
        com.kong.kflowweb.BP.WF.Dev2Interface.Flow_DoDeleteFlowByWriteLog(this.getFK_Flow(), this.getWorkID(), WebUser.getName()+"用户删除", true);
        return "删除成功...";
    }
	
	public final String AttachmentUpload_Down() throws Exception
	{
		WF_CCForm ccform = new WF_CCForm(this.context);
		return ccform.AttachmentUpload_Down();
	}
	

}