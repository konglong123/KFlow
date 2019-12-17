package com.kong.kflowweb.BP.DTS;

import com.kong.kflowweb.BP.DA.DBUrlType;
import com.kong.kflowweb.BP.DA.Log;
import com.kong.kflowweb.BP.En.Attr;
import com.kong.kflowweb.BP.En.ClassFactory;
import com.kong.kflowweb.BP.En.Entity;
import com.kong.kflowweb.BP.En.Map;

public class AddEmpLeng extends DataIOEn2
{
	public AddEmpLeng()
	{
		this.HisDoType = DoType.UnName;
		this.Title = "为操作员编号长度生级";
		this.HisRunTimeType = RunTimeType.UnName;
		this.FromDBUrl = DBUrlType.AppCenterDSN;
		this.ToDBUrl = DBUrlType.AppCenterDSN;
	}
	
	@Override
	public void Do()
	{
		String sql = "";
		String sql2 = "";
		
		java.util.ArrayList al = ClassFactory.GetObjects("com.kong.kflowweb.BP.En.Entity");
		for (Object obj : al)
		{
			Entity en = (Entity) ((obj instanceof Entity) ? obj : null);
			Map map = en.getEnMap();
			
			try
			{
				if (map.getIsView())
				{
					continue;
				}
			} catch (java.lang.Exception e)
			{
			}
			
			String table = en.getEnMap().getPhysicsTable();
			for (Attr attr : map.getAttrs())
			{
				if (attr.getKey().indexOf("Text") != -1)
				{
					continue;
				}
				
				if (attr.getKey().equals("Rec")
						|| attr.getKey().equals("FK_Emp")
						|| attr.getUIBindKey().equals("com.kong.kflowweb.BP.Port.Emps"))
				{
					sql += "\n update " + table + " set " + attr.getKey()
							+ "='01'||" + attr.getKey() + " WHERE length("
							+ attr.getKey() + ")=6;";
				} else if (attr.getKey().equals("Checker"))
				{
					sql2 += "\n update " + table + " set " + attr.getKey()
							+ "='01'||" + attr.getKey() + " WHERE length("
							+ attr.getKey() + ")=6;";
				}
			}
		}
		Log.DebugWriteInfo(sql);
		Log.DebugWriteInfo("===========================" + sql2);
	}
}