package com.kong.kflowweb.BP.WF.HttpHandler;

import com.kong.kflowweb.BP.DA.DBAccess;
import com.kong.kflowweb.BP.DA.DBType;
import com.kong.kflowweb.BP.DA.DataRow;
import com.kong.kflowweb.BP.DA.DataSet;
import com.kong.kflowweb.BP.DA.DataTable;
import com.kong.kflowweb.BP.DA.DataType;
import com.kong.kflowweb.BP.Difference.Handler.WebContralBase;
import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.Web.WebUser;

public class WF_RptSearch extends WebContralBase{
	
	/**
	 * 构造函数
	 */
	public WF_RptSearch()
	{
	
	}
	
	/**
	 * 流程分布
	 * @return
	 * @throws Exception 
	 */
	public final String DistributedOfMy_Init() throws Exception
	{
		DataSet ds = new DataSet();

		//我发起的流程.
		String sql = "";
		sql = "select FK_Flow \"FK_Flow\", FlowName \"FlowName\",Count(WorkID) as \"Num\" FROM WF_GenerWorkFlow  WHERE Starter='" + com.kong.kflowweb.BP.Web.WebUser.getNo() + "' GROUP BY FK_Flow, FlowName ";
		DataTable dt = com.kong.kflowweb.BP.DA.DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "Start";
		ds.Tables.add(dt);

		//待办.
		sql = "select FK_Flow \"FK_Flow\", FlowName \"FlowName\",Count(WorkID) as \"Num\" FROM wf_empworks  WHERE FK_Emp='" + com.kong.kflowweb.BP.Web.WebUser.getNo() + "' GROUP BY FK_Flow, FlowName ";
		DataTable dtTodolist = com.kong.kflowweb.BP.DA.DBAccess.RunSQLReturnTable(sql);
		dtTodolist.TableName = "Todolist";
		ds.Tables.add(dtTodolist);

		//正在运行的流程.
		DataTable dtRuning = com.kong.kflowweb.BP.WF.Dev2Interface.DB_TongJi_Runing();
		dtRuning.TableName = "Runing";
		ds.Tables.add(dtRuning);


		//归档的流程.
		DataTable dtOK = com.kong.kflowweb.BP.WF.Dev2Interface.DB_TongJi_FlowComplete();
		dtOK.TableName = "OK";
	
		ds.Tables.add(dtOK);

		//返回结果.
		return com.kong.kflowweb.BP.Tools.Json.ToJson(ds);
	}
	
	public final String Default_Init()
	{
		java.util.Hashtable ht = new java.util.Hashtable();
		ht.put("MyStartFlow", "我发起的流程");
		ht.put("MyJoinFlow", "我参与的流程");



		//   ht.Add("MyDeptFlow", "我本部门发起的流程");
		//  ht.Add("MySubDeptFlow", "我本部门与子部门发起的流程");
		// ht.Add("AdvFlowsSearch", "高级查询");

		return com.kong.kflowweb.BP.Tools.Json.ToJsonEntitiesNoNameMode(ht);
	}

	 //打开表单.
    public String KeySearch_OpenFrm() throws Exception
    {
       com.kong.kflowweb.BP.WF.HttpHandler.WF wf=new WF(this.context);
        return wf.Runing_OpenFrm();
    }

    /** 功能列表
	 
	 @return 
     * @throws Exception 
*/
	public final String KeySearch_Query() throws Exception
	{
		String type = this.GetRequestVal("type");
		if (DataType.IsNullOrEmpty(type))
		{
			type = "ByTitle";
		}

		String keywords = this.GetValFromFrmByKey("TB_KWds");
		if (DataType.IsNullOrEmpty(keywords)==true)
		{
			keywords = this.GetRequestVal("TB_Key");
		}

		int myselft = this.GetRequestValInt("CHK_Myself");
		String sql = "";

		if (type.equals("ByWorkID"))
		{
				if (myselft == 1)
				{
					sql = "SELECT FlowName,NodeName,FK_Flow,FK_Node,WorkID,Title,StarterName,RDT,WFSta,Emps,TodoEmps,IsCanDo FROM WF_GenerWorkFlow WHERE  WorkID=" + keywords + " AND Emps LIKE '@%" + WebUser.getNo() + "%'";
				}
				else
				{
					sql = "SELECT FlowName,NodeName,FK_Flow,FK_Node,WorkID,Title,StarterName,RDT,WFSta,Emps,TodoEmps,IsCanDo FROM WF_GenerWorkFlow WHERE  WorkID=" + keywords;
				}
		}
		else if (type.equals("ByTitle"))
		{
				sql = "SELECT A.FlowName,A.NodeName,A.FK_Flow,A.FK_Node,A.WorkID,A.Title,A.StarterName,A.RDT,A.WFSta,A.Emps, A.TodoEmps, A.WFState ";
				sql += " FROM WF_GenerWorkFlow A ";
				sql += " WHERE (A.Title LIKE '%" + keywords + "%' ";
				sql += " or A.Starter LIKE '%" + keywords + "%' ";
				sql += " or A.StarterName LIKE '%" + keywords + "%') ";
				sql += " AND (A.Emps LIKE '@%" + WebUser.getNo() + "%' ";
				sql += " or A.TodoEmps LIKE '%" + WebUser.getNo() + "%') ";
				
				sql += " AND A.WFState!=0 ";
				
		}

		DataTable dt = DBAccess.RunSQLReturnTable(sql);
		dt.TableName = "WF_GenerWorkFlow";

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt.Columns.get("FLOWNAME").ColumnName = "FlowName";
			dt.Columns.get("FK_FLOW").ColumnName = "FK_Flow";
			dt.Columns.get("FK_NODE").ColumnName = "FK_Node";
			dt.Columns.get("NODENAME").ColumnName = "NodeName";
			dt.Columns.get("WORKID").ColumnName = "WorkID";
			dt.Columns.get("TITLE").ColumnName = "Title";
			dt.Columns.get("STARTER").ColumnName = "Starter";
			dt.Columns.get("WFSTA").ColumnName = "WFSta";

			dt.Columns.get("EMPS").ColumnName = "Emps";
			dt.Columns.get("TODOEMPS").ColumnName = "TodoEmps"; //处理人.
			dt.Columns.get("WFSTATE").ColumnName = "WFState"; //处理人.

		}

		///#region 加入当前用户信息.
		DataTable mydt = new DataTable();
		mydt.Columns.Add("No");
		mydt.Columns.Add("Name");
		mydt.Columns.Add("FK_Dept");

		DataRow dr = mydt.NewRow();
		dr.setValue2017("No", WebUser.getNo());
		dr.setValue2017("Name",WebUser.getName());
		dr.setValue2017("FK_Dept",WebUser.getFK_Dept());
		mydt.Rows.add(dr);
		mydt.TableName = "WebUser";
		///#endregion 加入当前用户信息.


		DataSet ds = new DataSet();
		ds.Tables.add(mydt);
		ds.Tables.add(dt);

		return com.kong.kflowweb.BP.Tools.Json.ToJson(dt);
	}
}
