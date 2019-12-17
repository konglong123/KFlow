package com.kong.kflowweb.BP.Sys;

import com.kong.kflowweb.BP.En.EntitiesMyPK;
import com.kong.kflowweb.BP.En.Entity;

/**
 * 默认值s
 */
public class DefVals extends EntitiesMyPK {
	/** 
	 默认值
	 
	*/
	public DefVals() {
	}
	/** 
	 得到它的 Entity
	 
	*/
	@Override
	public Entity getGetNewEntity() {
		return new DefVal();
	}

}