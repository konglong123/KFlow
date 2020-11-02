package BP.Judge;

import BP.En.Row;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.Sys.MapData;
import BP.Tools.BeanTool;
import BP.WF.GEWork;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Work;
import BP.springCloud.entity.NodeTaskM;
import org.springframework.context.ApplicationContext;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.*;

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
    public static boolean judge(Row row,String expression) throws Exception{

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Map map=testExpression(expression);
        if ((boolean)map.get("success")){
            List<String> params=(List<String>) map.get("params");
            for (String param:params){
                engine.put(param,row.get(param));
            }
            Object result=engine.eval((String) map.get("expression"));
            return (boolean)result;
        }else {
            throw  new Exception("决策表达式不符合规范！");
        }
    }

    /**
    *@Description: 检测表达式，格式是否正确,
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/28
    */
    public static Map testExpression(String expression){
        Map result=new HashMap();
        if (!checkExpression(expression)){
            result.put("success",false);
            return result;
        }

        try {
            //从expression中顺序抽取参数
            List<String> params = new ArrayList<>();
            //将参数的左右｛｝删除，形成新的表达式（js引擎运行新表达式）
            StringBuilder sb = new StringBuilder();
            String[] strArr=expression.split("\\{");
            for (String temp : strArr) {
                if (temp.equals(""))
                    continue;
                String[] strArrTemp = temp.split("\\}");
                params.add(strArrTemp[0]);
                for (String str:strArrTemp){
                    sb.append(str);
                }
            }
            result.put("expression",sb.toString());
            result.put("params",params);
            result.put("success",true);
            return result;

        }catch (Exception e){
            result.put("success",false);
            return result;
        }
    }

    //检查表达式，括号是否匹配
    private static boolean checkExpression(String expression){
        boolean flag=false;
        char[] cs=expression.toCharArray();
        for (char c:cs){
            if (c=='{'&&!flag) {
                flag=true;
                continue;
            }
            if (c=='}'&&flag){
                flag=false;
                continue;
            }
            if (c!='{'&&c!='}')
                continue;
            return false;
        }
        return true;
    }

    /**
    *@Description: 决策下发节点id列表,没有配置决策规则的流出方向，默认流通,
    *@Param:
    *@return:返回流通的节点id
    *@Author: Mr.kong
    *@Date: 2020/4/25
    */
    public static List<String> judge(NodeTaskM nodeTaskM) throws Exception{
        NodeRules nodeRules=new NodeRules();
        String nodeId=nodeTaskM.getNodeId();
        nodeRules.Retrieve(NodeRuleAttr.NodeId,nodeId);
        List<String> nextNodeIds=new ArrayList<>();
        List<NodeRule> nodeRuleList=nodeRules.toList();

        Set<String> nodeSet=new HashSet<>();
        //规则列表
        List<JudgeRule> ruleList=new ArrayList<>(nodeRuleList.size());
        for (NodeRule nodeRule:nodeRuleList){
            JudgeRule judgeRule=new JudgeRule(nodeRule.GetValStrByKey(NodeRuleAttr.RuleNo));
            ruleList.add(judgeRule);
            nodeSet.add(nodeRule.GetValStrByKey(NodeRuleAttr.NextNodeId));
        }

        //获取节点表单数据
        Node node=new Node(nodeTaskM.getNodeId());
        Work wk = node.getHisWork();
        wk.setOID(Integer.parseInt(nodeTaskM.getWorkId()));
        wk.RetrieveFromDBSources();
        wk.ResetDefaultVal();
        Row row=wk.getRow();

        int index=0;
        for (JudgeRule rule:ruleList){
            int type=rule.GetValIntByKey(JudgeRuleAttr.Type);
            switch (type){
                case 1:
                    if (judge(row,rule.GetValStrByKey(JudgeRuleAttr.Expression)))
                        nextNodeIds.add(nodeRuleList.get(index).GetValStrByKey(NodeRuleAttr.NextNodeId));
                    break;
                case 2:
                    JudgeCondition condition= BeanTool.getBean(JudgeCondition.class,rule.GetValStrByKey(JudgeRuleAttr.BeanId));
                    if (condition.judge(nodeTaskM))
                        nextNodeIds.add(nodeRuleList.get(index).GetValStrByKey(NodeRuleAttr.NextNodeId));
                    break;
            }
            index++;
        }

        //没有配置规则的默认流通
        List<Node> nextNodes=node.getHisToNodes().toList();
        for (Node temp:nextNodes){
            if (!nodeSet.contains(temp.getNo()))
                nextNodeIds.add(temp.getNo());
        }
        return nextNodeIds;
    }



}
