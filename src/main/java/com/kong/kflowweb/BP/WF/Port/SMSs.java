package com.kong.kflowweb.BP.WF.Port;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.En.Entity;

/** 
 消息s
*/
public class SMSs extends Entities
{
	@Override
	public Entity getGetNewEntity()
	{
		return new SMS();
	}
	public SMSs()
	{
	}
}