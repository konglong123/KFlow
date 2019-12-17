package com.kong.kflowweb.BP.WF.Entity;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.En.Entity;

/**
 * 流程删除日志s
 */
public class WorkFlowDeleteLogs extends Entities
{
	// 构造
	/**
	 * 流程删除日志s
	 */
	public WorkFlowDeleteLogs()
	{
	}
	
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new WorkFlowDeleteLog();
	}
}