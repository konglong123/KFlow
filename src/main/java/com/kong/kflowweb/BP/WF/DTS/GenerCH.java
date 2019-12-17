package com.kong.kflowweb.BP.WF.DTS;

import com.kong.kflowweb.BP.DA.DataRow;
import com.kong.kflowweb.BP.DA.DataTable;
import com.kong.kflowweb.BP.En.Method;
import com.kong.kflowweb.BP.WF.ActionType;
import com.kong.kflowweb.BP.WF.Node;
import com.kong.kflowweb.BP.WF.Nodes;
import com.kong.kflowweb.BP.WF.TrackAttr;

/** 
 生成考核数据
*/
public class GenerCH extends Method
{
	/** 
	 生成考核数据
	*/
	public GenerCH()
	{
		this.Title = "生成考核数据（为所有的流程,根据最新配置的节点考核信息，生成考核数据。）";
		this.Help = "需要先删除运行时生成的数据，然后为每个流程的每个节点运行数据自动生成。";
	}
	/** 
	 设置执行变量
	 @return 
	*/
	@Override
	public void Init()
	{
	}
	/** 
	 当前的操纵员是否可以执行这个方法
	 * @throws Exception 
	*/
	@Override
	public boolean getIsCanDo() throws Exception
	{
		if (com.kong.kflowweb.BP.Web.WebUser.getNo().equals("admin"))
		{
			return true;
		}
		return false;
	}
	/** 
	 执行
	 @return 返回执行结果
	 * @throws Exception 
	*/
	@Override
	public Object Do() throws Exception
	{
		String err = "";
		try
		{
			//删除现有的数据。
			com.kong.kflowweb.BP.DA.DBAccess.RunSQL("DELETE FROM WF_CH");

			//查询全部的数据.
			com.kong.kflowweb.BP.WF.Nodes nds = new Nodes();
			nds.RetrieveAll();

			for (Node nd : nds.ToJavaList())
			{
				String sql = "SELECT * FROM ND" + Integer.parseInt(nd.getFK_Flow()) + "TRACK WHERE NDFrom=" + nd.getNodeID() + " ORDER BY WorkID, RDT ";
				DataTable dt = com.kong.kflowweb.BP.DA.DBAccess.RunSQLReturnTable(sql);
				String priRDT = null;
				String sdt = null;
				for (DataRow dr : dt.Rows)
				{
					//向下发送.
					int atInt = (Integer) dr.get(com.kong.kflowweb.BP.WF.TrackAttr.ActionType);
					ActionType at = ActionType.forValue(atInt);
					switch (at)
					{
						case Forward:
						case ForwardAskfor:
						case ForwardFL:
						case ForwardHL:
							break;
						default:
							continue;
					}

					//相关的变量.
					long workid = Long.parseLong(dr.get(TrackAttr.WorkID).toString());
					long fid = Long.parseLong(dr.get(TrackAttr.FID).toString());

					//当前的人员，如果不是就让其登录.
					String fk_emp = (String)((dr.get(TrackAttr.EmpFrom) instanceof String) ? dr.get(TrackAttr.EmpFrom) : null);
					if (!fk_emp.equals(com.kong.kflowweb.BP.Web.WebUser.getNo()))
					{
						try
						{
							com.kong.kflowweb.BP.WF.Dev2Interface.Port_Login(fk_emp, fk_emp);
						}
						catch (RuntimeException ex)
						{
							err += "@人员错误:" + fk_emp + "可能该人员已经删除." + ex.getMessage();
						}
					}

					//标题.
					String title = com.kong.kflowweb.BP.DA.DBAccess.RunSQLReturnStringIsNull("select title from wf_generworkflow where workid=" + workid, "");

					////调用他.
					//Glo.InitCH2017(nd.HisFlow, nd, workid, fid, title, priRDT, sdt,
					//    DataType.ParseSysDate2DateTime(dr[TrackAttr.RDT].ToString()));

					priRDT = dr.get(TrackAttr.RDT).toString();
					sdt = "无";
				}
			}
		}
		catch (RuntimeException ex)
		{
			return "生成考核失败:" + ex.getStackTrace();
		}

		//登录.
		com.kong.kflowweb.BP.WF.Dev2Interface.Port_Login("admin","0");
		return "执行成功,有如下信息:" + err;
	}
}