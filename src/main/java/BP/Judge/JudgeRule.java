package BP.Judge;

import BP.En.EntityNo;
import BP.En.Map;
import BP.Task.FlowGenerAttr;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-04-25 16:24
 **/
public class JudgeRule extends EntityNo {
    public JudgeRule(){
    }
    public JudgeRule(String _no) throws Exception{
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

        Map map = new Map("k_judge_rule", "决策规则");
        map.AddTBStringPK(JudgeRuleAttr.No, null, "ID", true, true, 1, 10, 3);
        map.AddTBString(JudgeRuleAttr.NodeId, null, "节点编码", true, true, 0, 50, 50);
        map.AddTBString(JudgeRuleAttr.NextNodeId, null, "流向节点编码", true, true, 0, 50, 50);
        map.AddDDLSysEnum(JudgeRuleAttr.Type, 0, "决策规则方案", true, false, JudgeRuleAttr.Type,
                "@1=决策表达式@2=决策Bean");
        map.AddTBStringDoc(JudgeRuleAttr.Expression, null, "表达式", true, true);
        map.AddTBStringDoc(JudgeRuleAttr.BeanId, null, "BeanId", true, true);
        map.AddTBStringDoc(JudgeRuleAttr.Context, null, "备注", true, true);

        this.set_enMap(map);
        return this.get_enMap();
    }

}
