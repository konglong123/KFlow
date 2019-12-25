package BP.springCloud;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: BuaaFlow
 * @description:
 * @author: Mr.Kong
 * @create: 2019-12-08 10:48
 **/

public class WorkFlowES implements Serializable {
    private Long id;//ES主键
    /**标题*/
    private String title;

    /**摘要……是es检索的重点内容*/
    private String abstracts;
    /**完整流程在mysql中存放的id*/
    private String mysqlId;
    /**更新时间*/
    private Date updateTime;
    /**局部敏感hash值——用来智能匹配时缩小检索范围*/
    private Long sensitiveHash;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public String getMysqlId() {
        return mysqlId;
    }

    public void setMysqlId(String mysqlId) {
        this.mysqlId = mysqlId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getSensitiveHash() {
        return sensitiveHash;
    }

    public void setSensitiveHash(Long sensitiveHash) {
        this.sensitiveHash = sensitiveHash;
    }

    @Override
    public String toString() {
        return "WorkFlowES{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", abstracts='" + abstracts + '\'' +
                ", mysqlId='" + mysqlId + '\'' +
                ", updateTime=" + updateTime +
                ", sensitiveHash=" + sensitiveHash +
                '}';
    }
}
