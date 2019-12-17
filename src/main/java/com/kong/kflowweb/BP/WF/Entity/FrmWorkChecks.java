package com.kong.kflowweb.BP.WF.Entity;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.En.Entity;
import com.kong.kflowweb.BP.Sys.SystemConfig;

/**
 * 审核组件s
 */
public class FrmWorkChecks extends Entities
{
	// 构造
	/**
	 * 审核组件s
	 */
	public FrmWorkChecks()
	{
	}
	
	/**
	 * 审核组件s
	 * 
	 * @param fk_mapdata
	 *            s
	 * @throws Exception 
	 */
	public FrmWorkChecks(String fk_mapdata) throws Exception
	{
		if (SystemConfig.getIsDebug())
		{
			this.Retrieve("No", fk_mapdata);
		} else
		{
			this.RetrieveFromCash("No", (Object) fk_mapdata);
		}
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new FrmWorkCheck();
	}
}