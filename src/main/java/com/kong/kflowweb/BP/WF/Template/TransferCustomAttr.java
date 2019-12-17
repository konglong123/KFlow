package com.kong.kflowweb.BP.WF.Template;

import com.kong.kflowweb.BP.En.EntityMyPKAttr;

/** 
 自定义运行路径 属性
*/
public class TransferCustomAttr extends EntityMyPKAttr
{
		
	/** 
	 工作ID
	*/
	public static final String WorkID = "WorkID";
	/** 
	 节点ID
	*/
	public static final String FK_Node = "FK_Node";
	/** 
	 处理人编号（多个人用逗号分开）
	*/
	public static final String Worker = "Worker";
	/**
          处理人显示（多个人用逗号分开）
    */
    public static final String WorkerName = "WorkerName";
	/** 
	 顺序
	*/
	public static final String Idx = "Idx";
	/** 
	 发起时间
	*/
	public static final String StartDT = "StartDT";
	/** 
	 计划完成日期
	*/
	public static final String PlanDT = "PlanDT";

	/** 
	 要启用的子流程编号
	*/
	public static final String SubFlowNo = "SubFlowNo";
	/** 
	 是否通过了
	*/
	public static final String TodolistModel = "TodolistModel";
		///#endregion
}