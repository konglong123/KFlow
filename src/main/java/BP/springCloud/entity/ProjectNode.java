package BP.springCloud.entity;

import java.io.Serializable;

/**
 * 项目节点
 * @author pcc
 */
public class ProjectNode implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 编码
	 */
	private int No;
	
	/**
	 * 树节点名
	 */
	private String treeNodeName;
	
	/**
	 * 项目编码
	 */
	private int projectNo;
	
	/**
	 * 前置树节点
	 */
	private int beforeTreeNode;
	
	/**
	 * 流程编码
	 */
	private int flowNo;
	
	/**
	 * 流程节点编码
	 */
	private int flowNodeNo;
	
	/**
	 * 节点层
	 */
	private int nodeLevel;
	
	/**
	 * @return the No
	 */
	public int getNo() {
		return No;
	}
	
	/**
	 * @param No the No to set
	 */
	public void setNo(int No) {
		this.No = No;
	}
	
	/**
	 * @return the treeNodeName
	 */
	public String getTreeNodeName() {
		return treeNodeName;
	}
	
	/**
	 * @param treeNodeName the treeNodeName to set
	 */
	public void setTreeNodeName(String treeNodeName) {
		this.treeNodeName = treeNodeName;
	}
	
	/**
	 * @return the projectNo
	 */
	public int getProjectNo() {
		return projectNo;
	}
	
	/**
	 * @param projectNo the projectNo to set
	 */
	public void setProjectNo(int projectNo) {
		this.projectNo = projectNo;
	}
	
	/**
	 * @return the beforeTreeNode
	 */
	public int getBeforeTreeNode() {
		return beforeTreeNode;
	}
	
	/**
	 * @param beforeTreeNode the beforeTreeNode to set
	 */
	public void setBeforeTreeNode(int beforeTreeNode) {
		this.beforeTreeNode = beforeTreeNode;
	}
	
	/**
	 * @return the flowNo
	 */
	public int getFlowNo() {
		return flowNo;
	}
	
	/**
	 * @param flowNo the flowNo to set
	 */
	public void setFlowNo(int flowNo) {
		this.flowNo = flowNo;
	}
	
	/**
	 * @return the flowNodeNo
	 */
	public int getFlowNodeNo() {
		return flowNodeNo;
	}
	
	/**
	 * @param flowNodeNo the flowNodeNo to set
	 */
	public void setFlowNodeNo(int flowNodeNo) {
		this.flowNodeNo = flowNodeNo;
	}
	
	/**
	 * @return the nodeLevel
	 */
	public int getNodeLevel() {
		return nodeLevel;
	}
	
	/**
	 * @param nodeLevel the nodeLevel to set
	 */
	public void setNodeLevel(int nodeLevel) {
		this.nodeLevel = nodeLevel;
	}
	
}