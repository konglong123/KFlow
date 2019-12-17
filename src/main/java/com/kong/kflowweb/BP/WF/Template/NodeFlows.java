package com.kong.kflowweb.BP.WF.Template;

import java.util.ArrayList;

import com.kong.kflowweb.BP.En.EntitiesMM;
import com.kong.kflowweb.BP.En.Entity;
import com.kong.kflowweb.BP.En.QueryObject;
import com.kong.kflowweb.BP.WF.Node;
import com.kong.kflowweb.BP.WF.Nodes;
import com.kong.kflowweb.BP.WF.Port.Emp;
import com.kong.kflowweb.BP.WF.Port.Emps;

/**
 * 节点调用子流程
 */
public class NodeFlows extends EntitiesMM
{
	/**
	 * 他的调用子流程
	 * @throws Exception 
	 */
	public final Emps getHisEmps() throws Exception
	{
		Emps ens = new Emps();
		for (NodeFlow ns : convertNodeFlows(this))
		{
			ens.AddEntity(new Emp(ns.getFK_Flow()));
		}
		return ens;
	}
	
	/**
	 * 他的工作节点
	 * @throws Exception 
	 */
	public final Nodes getHisNodes() throws Exception
	{
		Nodes ens = new Nodes();
		for (NodeFlow ns : convertNodeFlows(this))
		{
			ens.AddEntity(new Node(ns.getFK_Node()));
		}
		return ens;
		
	}
	
	/**
	 * 节点调用子流程
	 */
	public NodeFlows()
	{
	}
	
	/**
	 * 节点调用子流程
	 * 
	 * @param NodeID
	 *            节点ID
	 * @throws Exception 
	 */
	public NodeFlows(int NodeID) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeFlowAttr.FK_Node, NodeID);
		qo.DoQuery();
	}
	
	/**
	 * 节点调用子流程
	 * 
	 * @param EmpNo
	 *            EmpNo
	 * @throws Exception 
	 */
	public NodeFlows(String EmpNo) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeFlowAttr.FK_Flow, EmpNo);
		qo.DoQuery();
	}
	
	/**
	 * 得调用它的 Entity
	 */
	@Override
	public Entity getGetNewEntity()
	{
		return new NodeFlow();
	}
	
	/**
	 * 取调用一个调用子流程集合能够访问调用的节点s
	 * 
	 * @param sts
	 *            调用子流程集合
	 * @return
	 * @throws Exception 
	 */
	public final Nodes GetHisNodes(Emps sts) throws Exception
	{
		Nodes nds = new Nodes();
		Nodes tmp = new Nodes();
		for (Emp st : sts.ToJavaList())
		{
			tmp = this.GetHisNodes(st.getNo());
			for (Node nd : tmp.ToJavaList())
			{
				if (nds.Contains(nd))
				{
					continue;
				}
				nds.AddEntity(nd);
			}
		}
		return nds;
	}
	
	/**
	 * 调用子流程对应的节点
	 * 
	 * @param EmpNo
	 *            调用子流程编号
	 * @return 节点s
	 * @throws Exception 
	 */
	public final Nodes GetHisNodes(String EmpNo) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeFlowAttr.FK_Flow, EmpNo);
		qo.DoQuery();
		
		Nodes ens = new Nodes();
		for (NodeFlow en : convertNodeFlows(this))
		{
			ens.AddEntity(new Node(en.getFK_Node()));
		}
		return ens;
	}
	
	/**
	 * 转向此节点的集合的 Nodes
	 * 
	 * @param nodeID
	 *            此节点的ID
	 * @return 转向此节点的集合的Nodes (FromNodes)
	 * @throws Exception 
	 */
	public final Emps GetHisEmps(int nodeID) throws Exception
	{
		QueryObject qo = new QueryObject(this);
		qo.AddWhere(NodeFlowAttr.FK_Node, nodeID);
		qo.DoQuery();
		
		Emps ens = new Emps();
		for (NodeFlow en : convertNodeFlows(this))
		{
			ens.AddEntity(new Emp(en.getFK_Flow()));
		}
		return ens;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<NodeFlow> convertNodeFlows(Object obj)
	{
		return (ArrayList<NodeFlow>) obj;
	}
}