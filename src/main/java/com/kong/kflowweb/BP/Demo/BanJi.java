package com.kong.kflowweb.BP.Demo;

import com.kong.kflowweb.BP.DA.DBUrl;
import com.kong.kflowweb.BP.DA.DBUrlType;
import com.kong.kflowweb.BP.DA.Depositary;
import com.kong.kflowweb.BP.En.EnType;
import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.En.EntityNoName;
import com.kong.kflowweb.BP.En.Map;
import com.kong.kflowweb.BP.En.UAC;

/**
 * 班级
 */
public class BanJi extends EntityNoName
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 12323243636L;
	
	/**
	 * 班主任
	 */
	public final String getBZR()
	{
		return this.GetValStrByKey(BanJiAttr.BZR);
	}
	
	public final void setBZR(String value)
	{
		this.SetValByKey(BanJiAttr.BZR, value);
	}
	
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	
	/**
	 * 构造函数
	 */
	public BanJi()
	{
	}
	
	public BanJi(String no) throws Exception
	{
		super(no);
	}
	
	/**
	 * Map
	 */
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map();
		
		map.setEnDBUrl(new DBUrl(DBUrlType.AppCenterDSN));
		map.setPhysicsTable("Demo_BanJi");
		map.setDepositaryOfEntity(Depositary.None);
		map.setIsAllowRepeatName(true); // 是否允许名称重复.
		map.setEnDesc("班级");
		map.setEnType(EnType.App);
		map.setCodeStruct("3");// 让其编号为3位, 从001 到 999 .
		
		map.AddTBStringPK(BanJiAttr.No, null, "编号", true, true, 3, 3, 3);
		map.AddTBString(BanJiAttr.Name, null, "名称", true, false, 0, 50, 200);
		map.AddTBString(BanJiAttr.BZR, null, "班主任", true, false, 0, 50, 200);
		
		this.set_enMap(map);
		return this.get_enMap();
	}
	
	@Override
	public Entities getGetNewEntities()
	{
		return new BanJis();
	}
}