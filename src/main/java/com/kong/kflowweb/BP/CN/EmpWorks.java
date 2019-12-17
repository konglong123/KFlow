package com.kong.kflowweb.BP.CN;

import com.kong.kflowweb.BP.En.Entities;
import com.kong.kflowweb.BP.En.Entity;

public class EmpWorks extends Entities
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public Entity getGetNewEntity()
	{
		
		return new EmpWork();
	}
	
}
