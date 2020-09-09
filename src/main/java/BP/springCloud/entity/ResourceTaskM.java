package BP.springCloud.entity;

import java.util.Date;

/**
 * @program: kflow-web
 * @description: mybatis使用
 * @author: Mr.Kong
 * @create: 2020-02-28 09:07
 **/
public class ResourceTaskM {

    private Long no;
    private String resourceNo;
    private Long resourceId;
    private Long nodeId;
    private Date planEnd;
    private Date planStart;
    private Date startTime;
    private Date endTime;
    private Date bookStart;
    private Date bookEnd;
    private int isFinish;
    private int isPlan;
    private int useTime;
    private Long workId;
    private String planId;

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getResourceNo() {
        return resourceNo;
    }

    public void setResourceNo(String resourceNo) {
        this.resourceNo = resourceNo;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Date getPlanEnd() {
        return planEnd;
    }

    public void setPlanEnd(Date planEnd) {
        this.planEnd = planEnd;
    }

    public Date getPlanStart() {
        return planStart;
    }

    public void setPlanStart(Date planStart) {
        this.planStart = planStart;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getBookStart() {
        return bookStart;
    }

    public void setBookStart(Date bookStart) {
        this.bookStart = bookStart;
    }

    public Date getBookEnd() {
        return bookEnd;
    }

    public void setBookEnd(Date bookEnd) {
        this.bookEnd = bookEnd;
    }

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public int getIsPlan() {
        return isPlan;
    }

    public void setIsPlan(int isPlan) {
        this.isPlan = isPlan;
    }

    public int getUseTime() {
        return useTime;
    }

    public void setUseTime(int useTime) {
        this.useTime = useTime;
    }

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    private int useNum;

    public int getUseNum() {
        return useNum;
    }

    public void setUseNum(int useNum) {
        this.useNum = useNum;
    }

    @Override
    public String toString() {
        return "ResourceTaskM{" +
                "no=" + no +
                ", resourceNo='" + resourceNo + '\'' +
                ", resourceId=" + resourceId +
                ", nodeId=" + nodeId +
                ", planEnd=" + planEnd +
                ", planStart=" + planStart +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", bookStart=" + bookStart +
                ", bookEnd=" + bookEnd +
                ", isFinish=" + isFinish +
                ", isPlan=" + isPlan +
                '}';
    }
}
