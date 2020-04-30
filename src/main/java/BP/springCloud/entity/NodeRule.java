package BP.springCloud.entity;

import java.io.Serializable;

/**
 * 节点规则
 * @author pcc
 */
public class NodeRule implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	private int No;
	
	/**
	 * 节点id
	 */
	private String nodeId;
	
	/**
	 * 流向节点id
	 */
	private String nextNodeId;
	
	/**
	 * 规则no
	 */
	private String ruleNo;
	
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
	 * @return the nodeId
	 */
	public String getNodeId() {
		return nodeId;
	}
	
	/**
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
	/**
	 * @return the nextNodeId
	 */
	public String getNextNodeId() {
		return nextNodeId;
	}
	
	/**
	 * @param nextNodeId the nextNodeId to set
	 */
	public void setNextNodeId(String nextNodeId) {
		this.nextNodeId = nextNodeId;
	}
	
	/**
	 * @return the ruleNo
	 */
	public String getRuleNo() {
		return ruleNo;
	}
	
	/**
	 * @param ruleNo the ruleNo to set
	 */
	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}
	
}