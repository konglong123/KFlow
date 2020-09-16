package BP.NodeGroup;

import BP.En.EntityNo;
import BP.En.Map;
import BP.springCloud.tool.FeignTool;

import java.util.Date;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-09-16 17:30
 **/
public class ComposeGroup extends EntityNo {
    public ComposeGroup(){

    }

    public ComposeGroup(String _no) throws Exception{
        if (_no == null || _no.equals(""))
        {
            throw new RuntimeException(this.getEnDesc() + "@对表["
                    + this.getEnDesc() + "]进行查询前必须指定编号。");
        }

        this.setNo(_no);
        if (this.Retrieve() == 0)
        {
            throw new RuntimeException("@没有"
                    + this.get_enMap().getPhysicsTable() + ", No = " + getNo()
                    + "的记录。");
        }
    }
    @Override
    public Map getEnMap() {
        if (this.get_enMap() != null)
        {
            return this.get_enMap();
        }

        Map map = new Map("k_compose_group", "分组");
        map.AddTBStringPK(ComposeGroupAttr.No, null, "编号", true, true,1, 40, 100);
        map.AddTBInt(ComposeGroupAttr.groupNum, 0, "种群数", true, false);

        map.AddTBFloat(ComposeGroupAttr.variationPro, 0.5f, "变异率", true, false);
        map.AddTBFloat(ComposeGroupAttr.acrossPro, 0.7f, "交叉率", true, false);
        map.AddTBFloat(ComposeGroupAttr.elitePro, 0.1f, "精英比例", true, false);

        map.AddTBInt(ComposeGroupAttr.generateNum, 0, "进化代数", true, false);
        map.AddTBDecimal(ComposeGroupAttr.score, 0.1f, "相似度值", true, false);
        map.AddTBStringDoc(ComposeGroupAttr.abstracts, null, "概要", true, false);
        map.AddTBString(ComposeGroupAttr.flowNo, null, "流程编码", true, false, 0, 100, 100);
        map.AddTBString(ComposeGroupAttr.newFlowNo, null, "组合流程编码", true, false, 0, 100, 100);
        map.AddTBString(ComposeGroupAttr.history, null, "进化过程", true, false, 0, 100, 100);
        map.AddTBDateTime(ComposeGroupAttr.createTime, "2000-01-01 00:00:00", "创建时间", true, false);


        this.set_enMap(map);
        return this.get_enMap();
    }

    @Override
    protected boolean beforeInsert() throws Exception {
        Long id= FeignTool.getSerialNumber("BP.NodeGroup.ComposeGroup");
        this.setNo(id+"");
        return super.beforeInsert();
    }

    private String No;
    private int groupNum;
    private float variationPro;
    private float acrossPro;
    private float elitePro;
    private int generateNum;
    private double score;
    private String abstracts;
    private String flowNo;
    private String newFlowNo;
    private String history;
    private Date createTime;

    @Override
    public String getNo() {
        return No;
    }

    @Override
    public void setNo(String no) {
        No = no;
    }

    public int getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(int groupNum) {
        this.groupNum = groupNum;
    }

    public float getVariationPro() {
        return variationPro;
    }

    public void setVariationPro(float variationPro) {
        this.variationPro = variationPro;
    }

    public float getAcrossPro() {
        return acrossPro;
    }

    public void setAcrossPro(float acrossPro) {
        this.acrossPro = acrossPro;
    }

    public float getElitePro() {
        return elitePro;
    }

    public void setElitePro(float elitePro) {
        this.elitePro = elitePro;
    }

    public int getGenerateNum() {
        return generateNum;
    }

    public void setGenerateNum(int generateNum) {
        this.generateNum = generateNum;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }

    public String getNewFlowNo() {
        return newFlowNo;
    }

    public void setNewFlowNo(String newFlowNo) {
        this.newFlowNo = newFlowNo;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
