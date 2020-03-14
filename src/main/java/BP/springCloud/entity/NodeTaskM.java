package BP.springCloud.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 节点任务
 * @author pcc
 */
public class NodeTaskM implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	private Long no;
	
	/**
	 * 工作id
	 */
	private String flowTaskId;
	
	/**
	 * 流程id
	 */
	private String flowId;
	
	/**
	 * 节点id
	 */
	private String nodeId;
	
	/**
	 * 节点任务状态（0未准备，1准备好，可以开始）2（已经开始），3(已经完成)
	 */
	private int isReady;
	
	/**
	 * 前置节点任务
	 */
	private String preNodeTask;
	
	/**
	 * 后置节点任务
	 */
	private String nextNodeTask;
	
	/**
	 * 父节点任务
	 */
	private String parentNodeTask;
	
	/**
	 * 预计总工作量（h）
	 */
	private int totalTime;
	
	/**
	 * 已用工作量（h）
	 */
	private int useTime;
	
	/**
	 * 计划开始时间（调度产生）
	 */
	private Date planStartTime;
	
	/**
	 * 计划结束时间（调度产生）
	 */
	private Date planEndTime;
	
	/**
	 * 实际开始时间
	 */
	private Date startTime;
	
	/**
	 * 实际结束时间
	 */
	private Date endTime;
	
	/**
	 * 执行人
	 */
	private String executor;
	
	/**
	 * @return the no
	 */
	public Long getNo() {
		return no;
	}
	
	/**
	 * @param no the no to set
	 */
	public void setNo(Long no) {
		this.no = no;
	}
	
	/**
	 * @return the flowTaskId
	 */
	public String getFlowTaskId() {
		return flowTaskId;
	}
	
	/**
	 * @param flowTaskId the flowTaskId to set
	 */
	public void setFlowTaskId(String flowTaskId) {
		this.flowTaskId = flowTaskId;
	}
	
	/**
	 * @return the flowId
	 */
	public String getFlowId() {
		return flowId;
	}
	
	/**
	 * @param flowId the flowId to set
	 */
	public void setFlowId(String flowId) {
		this.flowId = flowId;
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
	 * @return the isReady
	 */
	public int getIsReady() {
		return isReady;
	}
	
	/**
	 * @param isReady the isReady to set
	 */
	public void setIsReady(int isReady) {
		this.isReady = isReady;
	}
	
	/**
	 * @return the preNodeTask
	 */
	public String getPreNodeTask() {
		return preNodeTask;
	}
	
	/**
	 * @param preNodeTask the preNodeTask to set
	 */
	public void setPreNodeTask(String preNodeTask) {
		this.preNodeTask = preNodeTask;
	}
	
	/**
	 * @return the nextNodeTask
	 */
	public String getNextNodeTask() {
		return nextNodeTask;
	}
	
	/**
	 * @param nextNodeTask the nextNodeTask to set
	 */
	public void setNextNodeTask(String nextNodeTask) {
		this.nextNodeTask = nextNodeTask;
	}
	
	/**
	 * @return the parentNodeTask
	 */
	public String getParentNodeTask() {
		return parentNodeTask;
	}
	
	/**
	 * @param parentNodeTask the parentNodeTask to set
	 */
	public void setParentNodeTask(String parentNodeTask) {
		this.parentNodeTask = parentNodeTask;
	}
	
	/**
	 * @return the totalTime
	 */
	public int getTotalTime() {
		return totalTime;
	}
	
	/**
	 * @param totalTime the totalTime to set
	 */
	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}
	
	/**
	 * @return the useTime
	 */
	public int getUseTime() {
		return useTime;
	}
	
	/**
	 * @param useTime the useTime to set
	 */
	public void setUseTime(int useTime) {
		this.useTime = useTime;
	}
	
	/**
	 * @return the planStartTime
	 */
	public Date getPlanStartTime() {
		return planStartTime;
	}
	
	/**
	 * @param planStartTime the planStartTime to set
	 */
	public void setPlanStartTime(Date planStartTime) {
		this.planStartTime = planStartTime;
	}
	
	/**
	 * @return the planEndTime
	 */
	public Date getPlanEndTime() {
		return planEndTime;
	}
	
	/**
	 * @param planEndTime the planEndTime to set
	 */
	public void setPlanEndTime(Date planEndTime) {
		this.planEndTime = planEndTime;
	}
	
	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}
	
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}
	
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * @return the executor
	 */
	public String getExecutor() {
		return executor;
	}
	
	/**
	 * @param executor the executor to set
	 */
	public void setExecutor(String executor) {
		this.executor = executor;
	}
	
}