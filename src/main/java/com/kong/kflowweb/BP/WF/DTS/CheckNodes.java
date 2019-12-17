package com.kong.kflowweb.BP.WF.DTS;

import com.kong.kflowweb.BP.DA.DBUrlType;
import com.kong.kflowweb.BP.DTS.DoType;

public class CheckNodes extends com.kong.kflowweb.BP.DTS.DataIOEn
{
	/** 
	 调度人员,岗位,部门
	 
	*/
	public CheckNodes()
	{
		this.HisDoType = DoType.UnName;
		this.Title = "修复节点信息";
	  //  this.HisRunTimeType = RunTimeType.UnName;
		this.FromDBUrl = DBUrlType.AppCenterDSN;
		this.ToDBUrl = DBUrlType.AppCenterDSN;
	}
	@Override
	public void Do()
	{

		//MDCheck md = new MDCheck();
		//md.Do();

		//执行调度部门。
		//com.kong.kflowweb.BP.Port.DTS.GenerDept gd = new com.kong.kflowweb.BP.Port.DTS.GenerDept();
		//gd.Do();

		// 调度人员信息。
		// Emp emp = new Emp(Web.WebUser.getNo());
		// emp.DoDTSEmpDeptStation();
	}
}