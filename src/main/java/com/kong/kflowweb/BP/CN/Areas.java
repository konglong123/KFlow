package com.kong.kflowweb.BP.CN;

import com.kong.kflowweb.BP.En.EntitiesNoName;
import com.kong.kflowweb.BP.En.Entity;

/**
 * 城市编码
 */
public class Areas extends EntitiesNoName
{
	/**
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new Area();
	}
	
	// 构造方法
	/**
	 * 城市编码s
	 */
	public Areas()
	{
	}
	
	/**
	 * 城市编码s
	 * 
	 * @param sf
	 *            省份
	 * @throws Exception 
	 */
	public Areas(String sf) throws Exception
	{
		this.Retrieve(AreaAttr.FK_SF, sf);
	}
}
