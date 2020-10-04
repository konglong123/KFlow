package BP.springCloud.controller;

import BP.Judge.JudgeRuleService;
import BP.Judge.JudgeTool;
import BP.Judge.NodeRuleService;
import BP.Sys.MapAttr;
import BP.Sys.MapAttrAttr;
import BP.Sys.MapAttrs;
import BP.springCloud.entity.JudgeRuleM;
import BP.springCloud.entity.NodeRule;
import BP.springCloud.tool.PageTool;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-04-25 11:05
 **/
@Controller
@RequestMapping("judge")
public class JudgeController {
    private static Logger logger = LoggerFactory.getLogger(JudgeController.class);

    @Resource
    private JudgeRuleService judgeRuleService;
    
    @Resource
    NodeRuleService nodeRuleService;

    /**
    *@Description: 条件查找决策bean
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/25
    */
    @RequestMapping("findJudgeRule")
    public JSONObject findJudgeRule(HttpServletRequest request, HttpServletResponse response,@RequestBody JudgeRuleM judgeRule){
        try {
            List list=judgeRuleService.findJudgeRuleList(judgeRule);
            PageTool.TransToResultList(list,request,response);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
    *@Description: 检验表达式是否符合规范
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/28
    */
    @RequestMapping("testExpression")
    @ResponseBody
    public Map  testExpression(@RequestParam int no){
        JudgeRuleM rule=judgeRuleService.getJudgeRule(no);
        Map map= JudgeTool.testExpression(rule.getExpression());
        if ((boolean)map.get("success")) {
            rule.setIsTest(1);
            judgeRuleService.updateJudgeRule(rule);
        }
        return map;
    }

    /**
    *@Description: 为节点增加决策规则 ,
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/4/29 
    */
    @RequestMapping("addNodeRule")
    @ResponseBody
    public Map addJudgeRule(@RequestBody NodeRule nodeRule){
        JudgeRuleM rule=judgeRuleService.getJudgeRule(Integer.valueOf(nodeRule.getRuleNo()));
        Map map=JudgeTool.testExpression(rule.getExpression());

        //判断节点表单中，是否存在表达式中相应字段
        try {
            List<String> params = (List<String>) map.get("params");
            MapAttrs mapAttrs=new MapAttrs();
            mapAttrs.Retrieve(MapAttrAttr.FK_MapData,"ND"+nodeRule.getNodeId());
            List<MapAttr> list=mapAttrs.Tolist();
            for (String param:params){
                boolean flag=false;
                for (MapAttr attr:list){
                    if (attr.getKeyOfEn().equals(param)) {
                        flag=true;
                        break;
                    }
                }
                if (!flag){
                    map.put("message","表单中不存在参数："+param);
                    map.put("success",false);
                    return map;
                }

            }

            nodeRuleService.insertNodeRule(nodeRule);
        }catch (Exception e){
            logger.error(e.getMessage());
        }

        return map;
    }

    /**
    *@Description: 查找决策节点的规则 
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/4/29 
    */
    @RequestMapping("findNodeRule")
    public void findNodeRule(HttpServletRequest request, HttpServletResponse response, @RequestBody NodeRule nodeRule){
        try {
            List list=nodeRuleService.findNodeRuleList(nodeRule);
            PageTool.TransToResultList(list,request,response);
        }catch (Exception e){
            logger.error(e.getMessage());
        }

    }

    @RequestMapping("deleteNodeRule")
    @ResponseBody
    public void deleteNodeRule(int no){
        try {
            nodeRuleService.deleteNodeRule(no);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }
}
