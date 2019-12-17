package com.kong.kflowweb.BP.WF;

import java.util.ArrayList;

/** 
 工作流程集合.
*/
public class WorkFlows extends ArrayList<WorkFlow>
{

		
	/** 
	 工作流程
	 @param flow 流程编号
	 * @throws Exception 
	*/
	public WorkFlows(Flow flow) throws Exception
	{
		StartWorks ens = (StartWorks)flow.getHisStartNode().getHisWorks();
		ens.RetrieveAll(10000);
		for (Work sw : ens.ToJavaList())
		{
			this.Add(new WorkFlow(flow, sw.getOID(), sw.getFID()));
		}
	}
	/** 
	 工作流程集合
	*/
	public WorkFlows()
	{
	}
	/** 
	 工作流程集合
	 
	 @param flow 流程
	 @param flowState 工作ID 
	*/
	public WorkFlows(Flow flow, int flowState)
	{
		//StartWorks ens = (StartWorks)flow.HisStartNode.HisWorks;
		//QueryObject qo = new QueryObject(ens);
		//qo.AddWhere(StartWorkAttr.WFState, flowState);
		//qo.DoQuery();
		//foreach (StartWork sw in ens)
		//{
		//    this.Add(new WorkFlow(flow, sw.OID, sw.FID));
		//}
	}
	/** 
	 GetNotCompleteNode
	 @param flowNo 流程编号
	 @return StartWorks
	*/
	public static StartWorks GetNotCompleteWork(String flowNo)
	{
		return null;

		//Flow flow = new Flow(flowNo);
		//StartWorks ens = (StartWorks)flow.HisStartNode.HisWorks;
		//QueryObject qo = new QueryObject(ens);
		//qo.AddWhere(StartWorkAttr.WFState, "!=", 1);
		//qo.DoQuery();
		//return ens;

//            
//            foreach(StartWork sw in ens)
//            {
//                ens.AddEntity( new WorkFlow( flow, sw.OID) ) ; 
//            }
//            
	}
	/** 
	 增加一个工作流程
	 @param wn 工作流程
	*/
	public final void Add(WorkFlow wn)
	{
		this.Add(wn);
	}
	/** 
	 根据位置取得数据
	*/
	public final WorkFlow getItem(int index)
	{
		return (WorkFlow)this.get(index);
	}
	/** 
	 清除死节点。
	 死节点的产生，就是用户非法的操作，或者系统出现存储故障，造成的流程中的当前工作节点没有工作人员，从而不能正常的运行下去。
	 清除死节点，就是把他们放到死节点工作集合里面。
	 @return 
	*/
	public static String ClearBadWorkNode()
	{
		String infoMsg = "清除死节点的信息：";
		String errMsg = "清除死节点的错误信息：";
		return infoMsg + errMsg;
	}
}