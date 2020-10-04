package BP.Ga;

import BP.En.EntityNo;
import BP.En.Map;
import BP.NodeGroup.ComposeGroupAttr;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-10-04 16:22
 **/
public class History extends EntityNo {
    public History(){

    }

    public History(String _no) throws Exception{
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

        Map map = new Map("k_history", "历史记录");
        map.AddTBStringPK("No", null, "编号", true, true,1, 40, 100);
        map.AddTBDecimal("score", 0.0d, "分值", true, false);
        map.AddTBString("history_no", null, "历史编码", true, false, 0, 100, 100);

        this.set_enMap(map);
        return this.get_enMap();
    }

    public void setScore(double score){
        this.SetValByKey("score",score);
    }

    public double getScore(){
        return this.GetValDoubleByKey("score");
    }
    public void setHistoryNo(String historyNo){
        this.SetValByKey("history_no",historyNo);
    }
    public String getHistoryNo(){
        return this.GetValStrByKey("history_no");
    }

}
