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
public class CCMobile_WorkOpt_OneWork extends WebContralBase
{
	/**
	 * 构造函数
	 */
	public CCMobile_WorkOpt_OneWork()
	{
	
	}
	

    ///#region xxx 界面 .
	public final String TimeBase_Init() throws Exception
	{
		WF_WorkOpt_OneWork en = new WF_WorkOpt_OneWork(this.context);
		return en.TimeBase_Init();
	}
	/** 
	 执行撤销操作.
	 
	 @return 
	 * @throws Exception 
	*/
	public final String TimeBase_UnSend() throws Exception
	{
		WF_WorkOpt_OneWork en = new WF_WorkOpt_OneWork(this.context);
		return en.OP_UnSend();
	}
	public final String TimeBase_OpenFrm() throws Exception
	{
		WF en = new WF(this.context);
		return en.Runing_OpenFrm();
	}

		///#endregion xxx 界面方法.

}