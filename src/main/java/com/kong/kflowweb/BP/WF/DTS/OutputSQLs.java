package com.kong.kflowweb.BP.WF.DTS;

import com.kong.kflowweb.BP.DA.DBUrlType;
import com.kong.kflowweb.BP.DA.Log;
import com.kong.kflowweb.BP.DA.LogType;
import com.kong.kflowweb.BP.DTS.DataIOEn;
import com.kong.kflowweb.BP.DTS.DoType;
import com.kong.kflowweb.BP.DTS.RunTimeType;
import com.kong.kflowweb.BP.En.Map;
import com.kong.kflowweb.BP.Sys.PubClass;
import com.kong.kflowweb.BP.WF.Node;
import com.kong.kflowweb.BP.WF.Nodes;
import com.kong.kflowweb.BP.WF.WFState;

public class OutputSQLs extends DataIOEn
{
	/** 
	 流程时效考核
	 
	*/
	public OutputSQLs()
	{
		this.HisDoType = DoType.UnName;
		this.Title = "OutputSQLs for produces DTSCHofNode";
		this.HisRunTimeType = RunTimeType.UnName;
		this.FromDBUrl = DBUrlType.AppCenterDSN;
		this.ToDBUrl = DBUrlType.AppCenterDSN;
	}
	@Override
	public void Do() throws Exception
	{
		String sql = this.GenerSqls();
		PubClass.ResponseWriteBlueMsg(sql.replace("\n", "<BR>"));
	}
	public final String GenerSqls() throws Exception
	{
		Log.DefaultLogWriteLine(LogType.Info, com.kong.kflowweb.BP.Web.WebUser.getName() + "开始调度考核信息:" + new java.util.Date());//.ToString("yyyy-MM-dd HH:mm:ss"));
		String infoMsg = "", errMsg = "";

		Nodes nds = new Nodes();
		nds.RetrieveAll();

		String fromDateTime = new java.util.Date().getYear() + "-01-01";
		fromDateTime = "2004-01-01 00:00";
		//string fromDateTime=DateTime.Now.Year+"-01-01 00:00";
		//string fromDateTime=DateTime.Now.Year+"-01-01 00:00";
		String insertSql = "";
		String delSQL = "";
		String updateSQL = "";

		String sqls = "";
		int i = 0;
		for (Node nd : nds.ToJavaList())
		{
			if (nd.getIsPCNode()) // 如果是计算机节点.
			{
				continue;
			}
			i++;
			Map map = nd.getHisWork().getEnMap();
			delSQL = "\n DELETE FROM " + map.getPhysicsTable() + " WHERE  OID  NOT IN (SELECT WORKID FROM WF_GenerWorkFlow ) AND WFState= " + WFState.Runing.getValue();

			sqls += "\n\n\n -- NO:" + i + "、" + nd.getFK_Flow() + nd.getFlowName() + " :  " + map.getEnDesc() + " \n" + delSQL + "; \n" + insertSql + "; \n" + updateSQL + ";";
		}
		Log.DefaultLogWriteLineInfo(sqls);
		return sqls;
	}
}