package com.kong.kflowweb.BP.WF.HttpHandler;

import java.io.UnsupportedEncodingException;

import org.apache.http.protocol.HttpContext;

import com.kong.kflowweb.BP.DA.DataRow;
import com.kong.kflowweb.BP.DA.DataSet;
import com.kong.kflowweb.BP.DA.DataTable;
import com.kong.kflowweb.BP.DA.Log;
import com.kong.kflowweb.BP.Difference.Handler.WebContralBase;
import com.kong.kflowweb.BP.En.QueryObject;
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
import com.kong.kflowweb.BP.WF.XML.CCMenus;
import com.kong.kflowweb.BP.Web.WebUser;

public class App extends WebContralBase{
	 /**
	  * 初始化函数
	  * @param mycontext
	  */
    public App(HttpContext mycontext)
    {
        this.context = mycontext;
    }
    
    public App()
    {
    }
    
    /**
     * 获得发起流程
     * @return
     * @throws Exception 
     */
    public String Start_Init() throws Exception
    {
	    DataTable dt = com.kong.kflowweb.BP.WF.Dev2Interface.DB_GenerCanStartFlowsOfDataTable(WebUser.getNo());
		return com.kong.kflowweb.BP.Tools.Json.DataTableToJson(dt,false,false,true);
    }
    
    /**
     * 获得待办
     * @return 
     */
    public String Todolist_Init()
    {
    	String fk_node = this.GetRequestVal("FK_Node");
		DataTable dt = null;
		try {
			dt = com.kong.kflowweb.BP.WF.Dev2Interface.DB_GenerEmpWorksOfDataTable(WebUser.getNo(), this.getFK_Node());
		} catch (Exception e) {
			Log.DebugWriteError("AppACEHandler Todolist_Init()"+e.getMessage());
			e.printStackTrace();
		}
		return com.kong.kflowweb.BP.Tools.Json.DataTableToJson(dt,false,false,true);
    }
    
    /**
     * 运行
     *  @return 
     * @throws Exception 
     */
    public String Runing_Init() throws Exception
    {
    	DataTable dt = null;
		dt = com.kong.kflowweb.BP.WF.Dev2Interface.DB_GenerRuning();
		/*if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt.Columns.get("WorkID").ColumnName = "WorkID";
		}*/
		return com.kong.kflowweb.BP.Tools.Json.DataTableToJson(dt,false,false,true);
    }
    
    /**
     * 初始化赋值
     * @return
     * @throws Exception 
     */
    public String Home_Init() throws Exception
    {
    	java.util.Hashtable ht = new java.util.Hashtable();
		ht.put("UserNo", com.kong.kflowweb.BP.Web.WebUser.getNo());
		ht.put("UserName", com.kong.kflowweb.BP.Web.WebUser.getName());

		//系统名称.
		ht.put("SysName", com.kong.kflowweb.BP.Sys.SystemConfig.getSysName());


		ht.put("Todolist_EmpWorks", com.kong.kflowweb.BP.WF.Dev2Interface.getTodolist_EmpWorks());
		ht.put("Todolist_Runing", com.kong.kflowweb.BP.WF.Dev2Interface.getTodolist_Runing());
		ht.put("Todolist_Sharing", com.kong.kflowweb.BP.WF.Dev2Interface.getTodolist_Sharing());
		ht.put("Todolist_Draft", com.kong.kflowweb.BP.WF.Dev2Interface.getTodolist_Draft());
        //ht.put("Todolist_Apply", com.kong.kflowweb.BP.WF.Dev2Interface.Todolist_Apply); //申请下来的任务个数.

		//我发起
		MyStartFlows myStartFlows = new MyStartFlows();
		QueryObject obj = new QueryObject(myStartFlows);
		obj.AddWhere(MyStartFlowAttr.Starter, WebUser.getNo());
		obj.addAnd();
		//运行中\已完成\挂起\退回\转发\加签\批处理\
		obj.addLeftBracket();
		obj.AddWhere("WFState=2 or WFState=3 or WFState=4 or WFState=5 or WFState=6 or WFState=8 or WFState=10");
		obj.addRightBracket();
		obj.DoQuery();
		ht.put("Todolist_MyStartFlow", myStartFlows.size());

		//我参与
		MyJoinFlows myFlows = new MyJoinFlows();
		obj = new QueryObject(myFlows);
		obj.AddWhere("Emps like '%" + WebUser.getNo() + "%'");
		obj.DoQuery();
		ht.put("Todolist_MyFlow", myFlows.size());
		
		
		
		
		return com.kong.kflowweb.BP.Tools.Json.ToJsonEntityModel(ht);
    }
    
    /**
     * 转换成菜单
     *  @return 
    
    
    public String Home_Menu()
    {
    	DataSet ds = new DataSet();

		com.kong.kflowweb.BP.WF.XML.ClassicMenus menus = new CCMenus();
		menus.RetrieveAll();

	   DataTable dtMain= menus.ToDataTable();
	   dtMain.TableName = "ClassicMenus";

	   ds.Tables.add(dtMain);

	   com.kong.kflowweb.BP.WF.XML.ClassicMenuAdvFuncs advMenms = new com.kong.kflowweb.BP.WF.XML.ClassicMenuAdvFuncs();
	   advMenms.RetrieveAll();

	   DataTable dtMenuAdv = advMenms.ToDataTable();
	   dtMenuAdv.TableName = "ClassicMenusAdv";
	   ds.Tables.add(dtMenuAdv);

	   return com.kong.kflowweb.BP.Tools.Json.ToJson(ds);
    } */
    
    
    /**
     * 控制台信息
     *  @return 
     * @throws Exception 
     */
    public String Index_Init() throws Exception
    {
    	java.util.Hashtable ht = new java.util.Hashtable();
		ht.put("Todolist_Runing", com.kong.kflowweb.BP.WF.Dev2Interface.getTodolist_Runing()); //运行中.
		ht.put("Todolist_EmpWorks", com.kong.kflowweb.BP.WF.Dev2Interface.getTodolist_EmpWorks()); //待办
		ht.put("Todolist_CCWorks", com.kong.kflowweb.BP.WF.Dev2Interface.getTodolist_CCWorks()); //抄送.

		//本周.
		ht.put("TodayNum", com.kong.kflowweb.BP.WF.Dev2Interface.getTodolist_CCWorks()); //抄送.

		return com.kong.kflowweb.BP.Tools.Json.ToJsonEntityModel(ht);
    }
    
    /**
     * 设置
     *  @return 
     */
    public String Setting_Init()
    {
    	return "";
    }
    
    /**
     * 登录
     *  @return 
     * @throws Exception 
     */
    public String Login_Submit() throws Exception
    {
    	String userNo = this.GetRequestVal("TB_UserNo");
		String pass = this.GetRequestVal("TB_Pass");

		com.kong.kflowweb.BP.Port.Emp emp = new Emp();
		emp.setNo(userNo);
		if (emp.RetrieveFromDBSources() ==0)
		{
			return "err@用户名或者密码错误.";
		}

		if (!pass.equals(emp.getPass()))
		{
			return "err@用户名或者密码错误.";
		}

		//调用登录方法.
		try {
			com.kong.kflowweb.BP.WF.Dev2Interface.Port_Login(emp.getNo(), emp.getName(), emp.getFK_Dept(), emp.getFK_DeptText(),null,null);
		} catch (UnsupportedEncodingException e) {
			Log.DebugWriteError("AppACEHandler Login_Submit():" + e.getMessage());
			e.printStackTrace();
		}

		return "登录成功.";
    }
    
    /**
     * 登录初始化
     *  @return 
     * @throws Exception 
     */
    public String Login_Init() throws Exception
    {
    	java.util.Hashtable ht = new java.util.Hashtable();
		ht.put("SysName", SystemConfig.getSysName());
		ht.put("ServiceTel", SystemConfig.getServiceTel());
		ht.put("UserNo", WebUser.getNo());

		return com.kong.kflowweb.BP.Tools.Json.ToJsonEntityModel(ht);
    }
    
    /**
     * 草稿
     *  @return 
     * @throws Exception 
     */
    public String Draft_Init() throws Exception
    {
    	DataTable dt = com.kong.kflowweb.BP.WF.Dev2Interface.DB_GenerDraftDataTable(this.getFK_Flow());
		return com.kong.kflowweb.BP.Tools.Json.DataTableToJson(dt,false,false,true);
    }
    
    /**
     * 授权登录
     *  @return 
     * @throws Exception 
     */
    public String LoginAs() throws Exception
    {
    	com.kong.kflowweb.BP.WF.Port.WFEmp wfemp = new com.kong.kflowweb.BP.WF.Port.WFEmp(this.getNo());
		if (wfemp.getAuthorIsOK() == false)
		{
			return "err@授权登录失败！";
		}
		com.kong.kflowweb.BP.Port.Emp emp1 = new com.kong.kflowweb.BP.Port.Emp(this.getNo());
		try {
			com.kong.kflowweb.BP.Web.WebUser.SignInOfGener(emp1, "CH", false, false, com.kong.kflowweb.BP.Web.WebUser.getNo(), com.kong.kflowweb.BP.Web.WebUser.getName());
		} catch (UnsupportedEncodingException e) {
			Log.DebugWriteError("AppACEHandler LoginAs():"+e.getMessage());
			e.printStackTrace();
		}
		return "success@授权登录成功！";
    }
    
    /**
     * 获取当前授权处理人的工作
     *  @return 
     */
    public String Todolist_Author()
    {
    	DataTable dt = null;
		try {
			dt = com.kong.kflowweb.BP.WF.Dev2Interface.DB_GenerEmpWorksOfDataTable(this.getNo(), this.getFK_Node());
		} catch (Exception e) {
			Log.DebugWriteError("AppACEHandler Todolist_Author():"+e.getMessage());
			e.printStackTrace();
		}
		String  a = com.kong.kflowweb.BP.Tools.Json.ToJson(dt);
		//转化大写的toJson.
		return com.kong.kflowweb.BP.Tools.Json.DataTableToJson(dt,false,false,true);
    }

    /**
     * 加载当前授权处理人
     *  @return 
     * @throws Exception 
     */
    public String Load_Author() throws Exception
    {
    	DataTable dt = com.kong.kflowweb.BP.DA.DBAccess.RunSQLReturnTable("SELECT * FROM WF_EMP WHERE AUTHOR='" + com.kong.kflowweb.BP.Web.WebUser.getNo() + "'");
		return com.kong.kflowweb.BP.Tools.Json.ToJson(dt);
    }
    
    /**
     * 抄送列表操作
     *  @return 
     * @throws Exception 
     */
    public String cc_Init() throws Exception
    {
    	String sta = this.GetRequestVal("Sta");
		if (sta == null || sta.equals(""))
		{
			sta = "-1";
		}

		int pageSize = 6; // int.Parse(pageSizeStr);

		String pageIdxStr = this.GetRequestVal("PageIdx");
		if (pageIdxStr == null)
		{
			pageIdxStr = "1";
		}
		int pageIdx = Integer.parseInt(pageIdxStr);

		//实体查询.
		com.kong.kflowweb.BP.WF.SMSs ss = new com.kong.kflowweb.BP.WF.SMSs();
		com.kong.kflowweb.BP.En.QueryObject qo = new com.kong.kflowweb.BP.En.QueryObject(ss);

		DataTable dt = null;
		if (sta.equals("-1"))
		{
			dt = com.kong.kflowweb.BP.WF.Dev2Interface.DB_CCList(com.kong.kflowweb.BP.Web.WebUser.getNo());
		}
		if (sta.equals("0"))
		{
			dt = com.kong.kflowweb.BP.WF.Dev2Interface.DB_CCList_UnRead(com.kong.kflowweb.BP.Web.WebUser.getNo());
		}
		if (sta.equals("1"))
		{
			dt = com.kong.kflowweb.BP.WF.Dev2Interface.DB_CCList_Read(com.kong.kflowweb.BP.Web.WebUser.getNo());
		}
		if (sta.equals("2"))
		{
			dt = com.kong.kflowweb.BP.WF.Dev2Interface.DB_CCList_Delete(com.kong.kflowweb.BP.Web.WebUser.getNo());
		}

		int allNum = qo.GetCount();
		qo.DoQuery(com.kong.kflowweb.BP.WF.SMSAttr.MyPK, pageSize, pageIdx);

		return com.kong.kflowweb.BP.Tools.Json.DataTableToJson(dt,false,false,true);
    }
    
    /**
     * 加载关注
     *  @return 
     * @throws Exception 
     */
    public String Focus_Init() throws Exception
    {
    	String flowNo = this.GetRequestVal("FK_Flow");

		int idx = 0;
		//获得关注的数据.
		DataTable dt = com.kong.kflowweb.BP.WF.Dev2Interface.DB_Focus(flowNo, com.kong.kflowweb.BP.Web.WebUser.getNo());
		SysEnums stas = new SysEnums("WFSta");
		String[] tempArr;
		for (DataRow dr : dt.Rows)
		{
			int wfsta = Integer.parseInt(dr.getValue("WFSta").toString());
			//edit by liuxc,2016-10-22,修复状态显示不正确问题
			String wfstaT  = ((SysEnum) stas.GetEntityByKey(SysEnumAttr.IntKey, wfsta)).getLab();
			String currEmp = "";

			if (wfsta != WFSta.Complete.getValue())
			{
				//edit by liuxc,2016-10-24,未完成时，处理当前处理人，只显示处理人姓名
				for (String emp : dr.getValue("ToDoEmps").toString().split("[;]", -1))
				{
					tempArr = emp.split("[,]", -1);

					currEmp += tempArr.length > 1 ? tempArr[1] : tempArr[0] + ",";
				}

				currEmp = DotNetToJavaStringHelper.trimEnd(currEmp, ',');

				//currEmp = dr["ToDoEmps"].ToString();
				//currEmp = currEmp.TrimEnd(';');
			}
			dr.setValue("ToDoEmps",currEmp);
			dr.setValue("FlowNote",wfstaT);
			dr.setValue("AtPara",(wfsta == com.kong.kflowweb.BP.WF.WFSta.Complete.getValue() ? DotNetToJavaStringHelper.trimEnd(DotNetToJavaStringHelper.trimStart(dr.getValue("Sender").toString(), '('), ')').split("[,]", -1)[1] : ""));
		}
		return com.kong.kflowweb.BP.Tools.Json.DataTableToJson(dt,false,false,true);
    }

    /**
     * 取消关注
     *  @return 
     * @throws Exception 
     * @throws NumberFormatException 
     */
    public String Focus_Delete() throws NumberFormatException, Exception
    {
    	com.kong.kflowweb.BP.WF.Dev2Interface.Flow_Focus(Long.parseLong(this.GetRequestVal("WorkID")));
		return "您已取消关注！";
    }

    /**
     * 
     *  @return 
     * @throws Exception 
     */
    public String FlowRpt_Init() throws Exception
    {
    	StringBuilder Pub1 = new StringBuilder();
		com.kong.kflowweb.BP.WF.Flows fls = new com.kong.kflowweb.BP.WF.Flows();
		fls.RetrieveAll();

		FlowSorts ens = new FlowSorts();
		ens.RetrieveAll();

		DataTable dt = com.kong.kflowweb.BP.WF.Dev2Interface.DB_GenerCanStartFlowsOfDataTable(com.kong.kflowweb.BP.Web.WebUser.getNo());

		int cols = 3; //定义显示列数 从0开始。
		int widthCell = 100 / cols;
		Pub1.append("<Table width=100% border=0>");
		int idx = -1;
		boolean is1 = false;

		//string timeKey = "s" + this.Session.SessionID + DateTime.Now.ToString("yyMMddHHmmss");
		for (FlowSort en : ens.ToJavaListFs())
		{
			if (en.getParentNo().equals("0") || en.getParentNo().equals("") || en.getNo().equals(""))
			{
				continue;
			}

			idx++;
			if (idx == 0)
			{
				Pub1.append(AddTR(is1));
			}
				is1 = !is1;

			Pub1.append(AddTDBegin("width='" + widthCell+("%' border=0 valign=top")));
			//输出类别.
			//this.Pub1.AddFieldSet(en.Name);
			Pub1.append(AddB(en.getName()));
			Pub1.append(AddUL());

			for (Flow fl : fls.ToJavaList())
			{
				if (fl.getFlowAppType() == FlowAppType.DocFlow)
				{
					continue;
				}

				//如果该目录下流程数量为空就返回.
				if (fls.GetCountByKey(com.kong.kflowweb.BP.WF.Template.FlowAttr.FK_FlowSort, en.getNo()) == 0)
				{
					continue;
				}

				if (!fl.getFK_FlowSort().equals(en.getNo()))
				{
					continue;
				}

				for (DataRow dr : dt.Rows)
				{
					if (!dr.getValue("No").equals(fl.getNo()))
					{
						continue;
					}
					break;
				}

				Pub1.append(AddLi(" <a  href=\"javascript:WinOpen('../WF/Rpt/Search.aspx?RptNo=ND" + Integer.parseInt(fl.getNo()) + "MyRpt&FK_Flow=" + fl.getNo() + "');\" >" + fl.getName() + "</a> "));
			}

			Pub1.append(AddULEnd());

			Pub1.append(AddTDEnd());
			if (idx == cols - 1)
			{
				idx = -1;
				Pub1.append(AddTREnd());
			}
		}

		while (idx != -1)
		{
			idx++;
			if (idx == cols - 1)
			{
				idx = -1;
				Pub1.append(AddTD());
				Pub1.append(AddTREnd());
			}
			else
			{
				Pub1.append(AddTD());
			}
		}
		Pub1.append(AddTableEnd());
		return Pub1.toString();
    }
    
	/**添加公共的字符串拼接方法table
	*/
	public final String AddTR(boolean item)
	{
		if (item)
		{
			return "\n<TR bgcolor=AliceBlue >";
		}
		else
		{
			return "\n<TR bgcolor=white class=TR>";
		}
	}

	public final String AddTDBegin(String attr)
	{
		return "\n<TD " + attr + " nowrap >";
	}

	public final String AddB(String s)
	{
		if (s == null || s.equals(""))
		{
			return "";
		}
		return "<B>" + s + "</B>";
	}
	public final String AddUL()
	{
		return "<ul>";
	}

	public final String AddLi(String html)
	{
		return "<li>" + html + "</li> \t\n";
	}

	public final String AddULEnd()
	{
		return "</ul>\t\n";
	}

	public final String AddTDEnd()
	{
		return "\n</TD>";
	}

	public final String AddTREnd()
	{
		return "\n</TR>";
	}

	public final String AddTD()
	{
		return "\n<TD >&nbsp;</TD>";
	}

	public final String AddTableEnd()
	{
		return "</Table>";
	}

}
