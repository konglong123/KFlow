package BP.Judge;

import BP.Sys.MapData;
import BP.Tools.BeanTool;
import BP.WF.Node;
import BP.springCloud.entity.NodeTaskM;
import org.springframework.context.ApplicationContext;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-04-25 11:15
 **/
public class JudgeTool {

    /**
    *@Description: 根据表达式，判断流向下个后续节点
    *@Param:  expression 表达式
    *@return:  下一节点id
    *@Author: Mr.kong
    *@Date: 2020/4/25
    */
    public static boolean judge(MapData data,String expression) throws Exception{
        //从expression中顺序抽取参数
        List<String> params=new ArrayList<>();
        for (String temp:expression.split("\\{")){

        }
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");


        int value = 7;
        String state = "正常";
        boolean flag = true;
        String st = "test";
        String str2 = "{value} > 5 && {st} == \"test\"  && {flag} == true";
        engine.put("value", value);
        engine.put("state", state);
        engine.put("flag", flag);
        engine.put("st", st);
        Object result2 = engine.eval(str2);
        System.out.println("结果类型:" + result2.getClass().getName() + ",结果:" + result2);
        return true;
    }

    /**
    *@Description: 决策下发节点id列表
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/25
    */
    public static List<String> judge(NodeTaskM nodeTaskM) throws Exception{
        JudgeRules rules=new JudgeRules();
        String nodeId=nodeTaskM.getNodeId();
        rules.Retrieve(JudgeRuleAttr.NodeId,nodeId);
        List<String> nextNodeIds=new ArrayList<>();
        List<JudgeRule> ruleList=rules.toList();
        MapData mapData=new MapData("ND"+nodeId);
        for (JudgeRule rule:ruleList){
            int type=rule.GetValIntByKey(JudgeRuleAttr.Type);
            switch (type){
                case 1:
                    if (judge(mapData,rule.GetValStrByKey(JudgeRuleAttr.Expression)))
                        nextNodeIds.add(rule.GetValStrByKey(JudgeRuleAttr.NextNodeId));
                    break;
                case 2:
                    JudgeCondition condition= BeanTool.getBean(JudgeCondition.class,rule.GetValStrByKey(JudgeRuleAttr.BeanId));
                    if (condition.judge(nodeTaskM))
                        nextNodeIds.add(rule.GetValStrByKey(JudgeRuleAttr.NextNodeId));
                    break;
            }
        }

        return nextNodeIds;
    }



}
