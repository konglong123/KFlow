package com.kong.kflowweb.BP.CN;

import com.kong.kflowweb.BP.DA.DBUrl;
import com.kong.kflowweb.BP.DA.DBUrlType;
import com.kong.kflowweb.BP.DA.Depositary;
import com.kong.kflowweb.BP.En.AdjunctType;
import com.kong.kflowweb.BP.En.EnType;
import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.En.EntityNoName;
import com.kong.kflowweb.BP.En.Map;
import com.kong.kflowweb.BP.En.UAC;

/**
 * 片区
 */
public class PQ extends EntityNoName
{
	// 基本属性
	
	// 构造函数
	@Override
	public UAC getHisUAC() throws Exception
	{
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}
	
	/**
	 * 片区
	 */
	public PQ()
	{
	}
	
	public PQ(String no) throws Exception
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
		
		
		// /#region 基本属性
		map.setEnDBUrl(new DBUrl(DBUrlType.AppCenterDSN));
		map.setPhysicsTable("CN_PQ");
		map.setAdjunctType(AdjunctType.AllType);
		map.setDepositaryOfMap(Depositary.Application);
		map.setDepositaryOfEntity(Depositary.None);
		map.setIsCheckNoLength(false);
		map.setEnDesc("片区");
		map.setEnType(EnType.App);
		map.setCodeStruct("4");
		
		// /#endregion
		
		
		// /#region 字段
		map.AddTBStringPK(PQAttr.No, null, "编号", true, false, 0, 50, 50);
		map.AddTBString(PQAttr.Name, null, "名称", true, false, 0, 50, 200);
		
		// /#endregion
		
		this.set_enMap(map);
		return this.get_enMap();
	}
	
	@Override
	public Entities getGetNewEntities()
	{
		return new PQs();
	}
	
	// /#endregion
	
}
