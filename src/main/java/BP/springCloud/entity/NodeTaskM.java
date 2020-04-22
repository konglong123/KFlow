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
	private Long No;
	
	/**
	 * 工作组
	 */
	private String workGroupId;
	
	/**
	 * 工作id
	 */
	private String workId;
	
	/**
	 * 流程id
	 */
	private String flowId;

	
	/**
	 * 节点id
	 */
	private String nodeId;
	
	/**
	 * 节点任务状态（-1未准备，1前置条件完成，可以开始）2（已经开始），3(已经完成)，4(计划完成)
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
	 * 最晚完成时间
	 */
	private Date oldestFinishTime;
	
	/**
	 * 最早开始时间
	 */
	private Date earlyStartTime;
	
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
	 * 节点任务状态（-1未准备，9，已经计划，1准备好，可以开始）2（已经开始），3(已经完成)，4（逾期），5（逾期前三天警告）
	 */
	private int status;
	
	/**
	 * 是否删除，0存在，1删除
	 */
	private int yn;

	private String nodeName;
	
	/**
	 * @return the No
	 */
	public Long getNo() {
		return No;
	}
	
	/**
	 * @param No the No to set
	 */
	public void setNo(Long No) {
		this.No = No;
	}
	
	/**
	 * @return the workGroupId
	 */
	public String getWorkGroupId() {
		return workGroupId;
	}
	
	/**
	 * @param workGroupId the workGroupId to set
	 */
	public void setWorkGroupId(String workGroupId) {
		this.workGroupId = workGroupId;
	}
	
	/**
	 * @return the workId
	 */
	public String getWorkId() {
		return workId;
	}
	
	/**
	 * @param workId the workId to set
	 */
	public void setWorkId(String workId) {
		this.workId = workId;
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
	 * @return the oldestFinishTime
	 */
	public Date getOldestFinishTime() {
		return oldestFinishTime;
	}
	
	/**
	 * @param oldestFinishTime the oldestFinishTime to set
	 */
	public void setOldestFinishTime(Date oldestFinishTime) {
		this.oldestFinishTime = oldestFinishTime;
	}
	
	/**
	 * @return the earlyestStartTime
	 */
	public Date getEarlyStartTime() {
		return earlyStartTime;
	}
	
	/**
	 * @param earlyStartTime the earlyestStartTime to set
	 */
	public void setEarlyStartTime(Date earlyStartTime) {
		this.earlyStartTime = earlyStartTime;
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
	
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * @return the yn
	 */
	public int getYn() {
		return yn;
	}
	
	/**
	 * @param yn the yn to set
	 */
	public void setYn(int yn) {
		this.yn = yn;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
}