package com.kong.kflowweb.BP.WF.HttpHandler;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import org.apache.http.protocol.HttpContext;

import com.kong.kflowweb.BP.BPMN.Glo;
import com.kong.kflowweb.BP.DA.DBAccess;
import com.kong.kflowweb.BP.DA.DBType;
import com.kong.kflowweb.BP.DA.DataRow;
import com.kong.kflowweb.BP.DA.DataSet;
import com.kong.kflowweb.BP.DA.DataTable;
import com.kong.kflowweb.BP.DA.Log;
import com.kong.kflowweb.BP.DA.Paras;
import com.kong.kflowweb.BP.Difference.Handler.WebContralBase;
import com.kong.kflowweb.BP.En.QueryObject;
import com.kong.kflowweb.BP.GPM.Dept;
import com.kong.kflowweb.BP.Port.Emp;
import com.kong.kflowweb.BP.Sys.SysEnum;
import com.kong.kflowweb.BP.Sys.SysEnumAttr;
import com.kong.kflowweb.BP.Sys.SysEnums;
import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.WF.DotNetToJavaStringHelper;
import com.kong.kflowweb.BP.WF.Flow;
import com.kong.kflowweb.BP.WF.FlowAppType;
import com.kong.kflowweb.BP.WF.WFSta;
import com.kong.kflowweb.BP.WF.Data.MyJoinFlows;
import com.kong.kflowweb.BP.WF.Data.MyStartFlowAttr;
import com.kong.kflowweb.BP.WF.Data.MyStartFlows;
import com.kong.kflowweb.BP.WF.Template.FlowSort;
import com.kong.kflowweb.BP.WF.Template.FlowSorts;
import com.kong.kflowweb.BP.Web.WebUser;

public class Tester extends WebContralBase{
	 /**
	  * 初始化函数
	  * @param mycontext
	  */
    public Tester(HttpContext mycontext)
    {
        this.context = mycontext;
    }
    
    public Tester()
    {
    }
    
    //#region 组织结构维护.
    /// <summary>
    /// 初始化组织结构部门表维护.
    /// </summary>
    /// <returns></returns>
    public String Tester_Init() throws Exception
    {
    	return com.kong.kflowweb.BP.WF.AppClass.JobSchedule(233);
    }

	
}
