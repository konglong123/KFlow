package com.kong.kflowweb.BP.WF.DTS;

import com.kong.kflowweb.BP.DA.DBUrlType;
import com.kong.kflowweb.BP.DTS.DataIOEn2;
import com.kong.kflowweb.BP.DTS.DoType;
import com.kong.kflowweb.BP.DTS.RunTimeType;

public class UserPort extends DataIOEn2
{
	/** 
	 调度人员,岗位,部门
	*/
	public UserPort()
	{
		this.HisDoType = DoType.UnName;
		this.Title = "生成流程部门(运行在系统第一次安装时或者部门变化时)";
		this.HisRunTimeType = RunTimeType.UnName;
		this.FromDBUrl = DBUrlType.AppCenterDSN;
		this.ToDBUrl = DBUrlType.AppCenterDSN;
	}
	@Override
	public void Do()
	{
		//执行调度部门。
		//com.kong.kflowweb.BP.Port.DTS.GenerDept gd = new com.kong.kflowweb.BP.Port.DTS.GenerDept();
		//gd.Do();
		// 调度人员信息。
		// Emp emp = new Emp(Web.WebUser.getNo());
		// emp.DoDTSEmpDeptStation();
	}
}