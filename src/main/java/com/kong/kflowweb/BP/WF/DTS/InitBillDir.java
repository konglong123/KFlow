package com.kong.kflowweb.BP.WF.DTS;

import java.io.File;

import com.kong.kflowweb.BP.DA.DBUrlType;
import com.kong.kflowweb.BP.DTS.DataIOEn;
import com.kong.kflowweb.BP.DTS.DoType;
import com.kong.kflowweb.BP.DTS.RunTimeType;
import com.kong.kflowweb.BP.En.QueryObject;
import com.kong.kflowweb.BP.Port.Dept;
import com.kong.kflowweb.BP.Port.Depts;
import com.kong.kflowweb.BP.WF.Template.BillTemplate;
import com.kong.kflowweb.BP.WF.Template.BillTemplates;

public class InitBillDir extends DataIOEn
{
	/** 
	 流程时效考核
	 
	*/
	public InitBillDir()
	{
		this.HisDoType = DoType.UnName;
		this.Title = "<font color=green><b>创建单据目录(运行在每次更改单据文号或每年一天)</b></font>";
		this.HisRunTimeType = RunTimeType.UnName;
		this.FromDBUrl = DBUrlType.AppCenterDSN;
		this.ToDBUrl = DBUrlType.AppCenterDSN;
	}
	/** 
	 创建单据目录
	 * @throws Exception 
	 
	*/
	@Override
	public void Do() throws Exception
	{
		if (true) //此方法暂时排除，不需要创建目录。
		{
			return;
		}
		Depts Depts = new Depts();
		QueryObject qo = new QueryObject(Depts);
  //      qo.AddWhere("Grade", " < ", 4);
		qo.DoQuery();

		BillTemplates funcs = new BillTemplates();
		funcs.RetrieveAll();


		String path = com.kong.kflowweb.BP.WF.Glo.getFlowFileBill();
		int year = new java.util.Date().getYear();//.Year.toString();
		File file = new File(path);
		if (file.exists() == false)
		{
			file = new File(path);
			file.mkdirs();
		}
		file = new File(path + "\\\\" + year);
		if (file.exists() == false)
		{
			file.mkdirs();
		}


		for (Dept Dept : Depts.ToJavaList())
		{
			file = new File(path + "\\\\" + year + "\\\\" + Dept.getNo());
			if (file.exists() == false)
			{
				file.mkdirs();
			}

			for (BillTemplate func : funcs.ToJavaList())
			{
				file = new File(path + "\\\\" + year + "\\\\" + Dept.getNo() + "\\\\" + func.getNo());
				if (file.exists() == false)
				{
					file.mkdirs();
				}
			}
		}
	}
}