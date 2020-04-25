package BP.Judge;

import BP.Sys.MapData;
import BP.WF.Node;
import BP.springCloud.entity.NodeTaskM;

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
    public static String judge(MapData data,String expression) throws Exception{
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
        return null;
    }

    /**
    *@Description: 调用决策bean，判断
    *@Param:
    *@return:  返回流向下一节点id
    *@Author: Mr.kong
    *@Date: 2020/4/25
    */
    public static String judge(NodeTaskM nodeTaskM,JudgeCondition judgeCondition){
        return null;
    }

    /**
    *@Description: 决策下发节点id
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/25
    */
    public static List<String> judge(NodeTaskM nodeTaskM) throws Exception{
        Node node=new Node(nodeTaskM.getNodeId());
        return null;
    }

}
