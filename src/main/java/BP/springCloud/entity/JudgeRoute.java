package BP.springCloud.entity;

import java.io.Serializable;

/**
 * 决策路由
 * @author pcc
 */
public class JudgeRoute implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	private Long No;
	
	/**
	 * 工作id
	 */
	private String workId;
	
	/**
	 * 总分支数量
	 */
	private int num;
	
	/**
	 * 匹配决策nodeid
	 */
	private String judgeNodeId;
	
	/**
	 * 已到达数目
	 */
	private int arriveNum;

	//流出分支节点id集合
	private String routes;

	//到达节点id集合
	private String arrives;

	private int isDelete;
	
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
	 * @return the num
	 */
	public int getNum() {
		return num;
	}
	
	/**
	 * @param num the num to set
	 */
	public void setNum(int num) {
		this.num = num;
	}
	
	/**
	 * @return the judgeNodeId
	 */
	public String getJudgeNodeId() {
		return judgeNodeId;
	}
	
	/**
	 * @param judgeNodeId the judgeNodeId to set
	 */
	public void setJudgeNodeId(String judgeNodeId) {
		this.judgeNodeId = judgeNodeId;
	}
	
	/**
	 * @return the arriveNum
	 */
	public int getArriveNum() {
		return arriveNum;
	}
	
	/**
	 * @param arriveNum the arriveNum to set
	 */
	public void setArriveNum(int arriveNum) {
		this.arriveNum = arriveNum;
	}

	public String getRoutes() {
		return routes;
	}

	public void setRoutes(String routes) {
		this.routes = routes;
	}

	public String getArrives() {
		return arrives;
	}

	public void setArrives(String arrives) {
		this.arrives = arrives;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
}