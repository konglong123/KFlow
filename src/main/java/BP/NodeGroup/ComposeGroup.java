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
        map.AddTBInt(ComposeGroupAttr.maxSaveNum, 0, "推荐结果保存数", true, false);
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


    public int getGroupNum() {
        return this.GetValIntByKey(ComposeGroupAttr.groupNum);
    }

    public void setGroupNum(int groupNum) {
        this.SetValByKey(ComposeGroupAttr.groupNum,groupNum);
    }

    public float getVariationPro() {
        return this.GetValFloatByKey(ComposeGroupAttr.variationPro);
    }

    public void setVariationPro(float variationPro) {
        this.SetValByKey(ComposeGroupAttr.variationPro,variationPro);
    }

    public float getAcrossPro() {
        return this.GetValFloatByKey(ComposeGroupAttr.acrossPro);
    }

    public void setAcrossPro(float acrossPro) {
        this.SetValByKey(ComposeGroupAttr.acrossPro,acrossPro);
    }

    public float getElitePro() {
        return this.GetValFloatByKey(ComposeGroupAttr.elitePro);
    }

    public void setElitePro(float elitePro) {
        this.SetValByKey(ComposeGroupAttr.elitePro,elitePro);
    }

    public int getGenerateNum() {
        return this.GetValIntByKey(ComposeGroupAttr.generateNum);
    }

    public void setGenerateNum(int generateNum) {
        this.SetValByKey(ComposeGroupAttr.generateNum,generateNum);
    }

    public int getMaxSaveNum() {
        return this.GetValIntByKey(ComposeGroupAttr.maxSaveNum);
    }

    public void setMaxSaveNum(int maxSaveNum) {
        this.SetValByKey(ComposeGroupAttr.maxSaveNum,maxSaveNum);
    }

    public double getScore() {
        return this.GetValDoubleByKey(ComposeGroupAttr.score);
    }

    public void setScore(double score) {
        this.SetValByKey(ComposeGroupAttr.score,score);
    }

    public String getAbstracts() {
        return this.GetValStrByKey(ComposeGroupAttr.abstracts);
    }

    public void setAbstracts(String abstracts) {
        this.SetValByKey(ComposeGroupAttr.abstracts,abstracts);
    }

    public String getFlowNo() {
        return this.GetValStrByKey(ComposeGroupAttr.flowNo);
    }

    public void setFlowNo(String flowNo) {
        this.SetValByKey(ComposeGroupAttr.flowNo,flowNo);
    }

    public String getNewFlowNo() {
        return this.GetValStrByKey(ComposeGroupAttr.newFlowNo);
    }

    public void setNewFlowNo(String newFlowNo) {
        this.SetValByKey(ComposeGroupAttr.newFlowNo,newFlowNo);
    }

    public String getHistory() {
        return this.GetValStrByKey(ComposeGroupAttr.history);
    }

    public void setHistory(String history) {
        this.SetValByKey(ComposeGroupAttr.history,history);
    }

    public Date getCreateTime() {
        return this.GetValDateTime(ComposeGroupAttr.createTime);
    }

    public void setCreateTime(Date createTime) {
        this.SetValByKey(ComposeGroupAttr.createTime,createTime);
    }
}
