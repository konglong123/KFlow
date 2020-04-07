package BP.springCloud.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程实例
 * @author pcc
 */
public class GenerFlow implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	private Long no;
	
	/**
	 * 流程实例编码
	 */
	private Long workId;


	private Long parentWorkId;

	private Long workGroupId;

	/**
	 * 状态（1开始，2完成）
	 */
	private int status;
	
	/**
	 * 流程编码
	 */
	private int flowId;

	private int totalTime;

	private int useTime;

	/*处于激活状态的节点*/
	private String activatedNodes;

	/**
	 * 创建者
	 */
	private String creator;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 结束时间
	 */
	private Date finishTime;
	
	/**
	 * 删除标记
	 */
	private int yn;
	
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
	 * @return the workId
	 */
	public Long getWorkId() {
		return workId;
	}

	public Long getParentWorkId() {
		return parentWorkId;
	}

	public void setParentWorkId(Long parentWorkId) {
		this.parentWorkId = parentWorkId;
	}

	public Long getWorkGroupId() {
		return workGroupId;
	}

	public void setWorkGroupId(Long workGroupId) {
		this.workGroupId = workGroupId;
	}

	/**
	 * @param workId the workId to set
	 */
	public void setWorkId(Long workId) {
		this.workId = workId;
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
	 * @return the flowId
	 */
	public int getFlowId() {
		return flowId;
	}
	
	/**
	 * @param flowId the flowId to set
	 */
	public void setFlowId(int flowId) {
		this.flowId = flowId;
	}
	
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}
	
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	/**
	 * @return the finishTime
	 */
	public Date getFinishTime() {
		return finishTime;
	}
	
	/**
	 * @param finishTime the finishTime to set
	 */
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
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

	public int getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	public int getUseTime() {
		return useTime;
	}

	public void setUseTime(int useTime) {
		this.useTime = useTime;
	}

	public String getActivatedNodes() {
		return activatedNodes;
	}

	public void setActivatedNodes(String activatedNodes) {
		this.activatedNodes = activatedNodes;
	}
}