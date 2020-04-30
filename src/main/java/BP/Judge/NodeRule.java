package BP.Judge;

import BP.En.EntityNo;
import BP.En.Map;
import BP.springCloud.tool.FeignTool;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-04-29 17:00
 **/
public class NodeRule extends EntityNo {
    public NodeRule(){

    }
    public NodeRule(String _no) throws Exception{
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

        Map map = new Map("k_node_rule", "节点决策");
        map.AddTBStringPK(NodeRuleAttr.No, null, "ID", true, true, 30, 50, 100);
        map.AddTBString(NodeRuleAttr.NodeId, null, "节点编码", true, false, 30, 50, 100);
        map.AddTBString(NodeRuleAttr.NextNodeId, null, "流向节点编码", true, true, 30, 50, 100);
        map.AddTBString(NodeRuleAttr.RuleNo, null, "规则编码", true, true, 30, 50, 100);

        this.set_enMap(map);
        return this.get_enMap();
    }

    @Override
    protected boolean beforeInsert() throws Exception {
        long id=FeignTool.getSerialNumber("BP.Judge.NodeRule");
        this.SetValByKey(NodeRuleAttr.No,id);
        return super.beforeInsert();
    }
}
