package com.kong.kflowweb.BP.WF.Template;

import java.io.File;

import com.kong.kflowweb.BP.WF.CCBPM_DType;
import com.kong.kflowweb.BP.WF.Flow;
import com.kong.kflowweb.BP.WF.Node;

/** 
 
 
*/
public class TemplateGlo
{
	/** 
	 创建一个流程.
	 
	 @param flowSort 流程类别
	 @return string
	 * @throws Exception 
	*/
	public static String NewFlow(String flowSort, String flowName, com.kong.kflowweb.BP.WF.Template.DataStoreModel dsm, String ptable, String flowMark, String flowVer) throws Exception
	{
		//执行保存.
		com.kong.kflowweb.BP.WF.Flow fl = new com.kong.kflowweb.BP.WF.Flow();
		//修改类型为CCBPMN
		//fl.DType = string.IsNullOrEmpty(flowVer) ? 1 : Int32.Parse(flowVer);

		fl.setDType(CCBPM_DType.CCBPM.getValue());

		String flowNo = fl.DoNewFlow(flowSort, flowName, dsm, ptable, flowMark);
		fl.setNo(flowNo);

		//如果为CCFlow模式则不进行写入Json串
		if (flowVer.equals("0"))
		{
			return flowNo;
		}

	  
		//创建连线
		Direction drToNode = new Direction();
		drToNode.setFK_Flow(flowNo);
		drToNode.setNode(Integer.parseInt(Integer.parseInt(flowNo) + "01"));
		drToNode.setToNode(Integer.parseInt(Integer.parseInt(flowNo) + "02"));
		drToNode.Insert();

		return flowNo;
	}
	/** 
	 创建一个节点
	 
	 @param flowNo
	 @param x
	 @param y
	 @return 
	 * @throws Exception 
	*/
	public static int NewNode(String flowNo, int x, int y) throws Exception
	{
		com.kong.kflowweb.BP.WF.Flow fl = new Flow(flowNo);
		com.kong.kflowweb.BP.WF.Node nd = fl.DoNewNode(x, y);
		return nd.getNodeID();
	}
	/** 
	 删除节点.
	 
	 @param nodeid
	 * @throws Exception 
	*/
	public static void DeleteNode(int nodeid) throws Exception
	{
		com.kong.kflowweb.BP.WF.Node nd = new Node(nodeid);
		nd.Delete();
	}
	  
}