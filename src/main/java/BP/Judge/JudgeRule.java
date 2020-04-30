package BP.Judge;

import BP.En.EntityNo;
import BP.En.Map;
import BP.Web.WebUser;
import BP.springCloud.tool.FeignTool;

import java.util.Date;

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
        map.AddTBStringPK(JudgeRuleAttr.No, null, "ID", true, true, 30, 50, 100);
        map.AddTBString(JudgeRuleAttr.Alias, null, "别名", true, false, 30, 50, 100);
        map.AddDDLSysEnum(JudgeRuleAttr.Type, 0, "决策规则方案", true, true, JudgeRuleAttr.Type,
                "@1=表达式@2=Bean");
        map.AddTBStringDoc(JudgeRuleAttr.Expression, null, "表达式", true, false);
        map.AddTBStringDoc(JudgeRuleAttr.BeanId, null, "BeanId", true, false);
        map.AddTBStringDoc(JudgeRuleAttr.Context, null, "备注", true, false);
        map.AddTBString(JudgeRuleAttr.Creator, null, "创建者", true, true, 30, 50, 100);

        this.set_enMap(map);
        return this.get_enMap();
    }

    @Override
    protected boolean beforeInsert() throws Exception {
        long id= FeignTool.getSerialNumber("BP.Judge.JudgeRule");
        this.SetValByKey(JudgeRuleAttr.No,id);
        this.SetValByKey(JudgeRuleAttr.Creator, WebUser.getNo());
        return super.beforeInsert();
    }
}
