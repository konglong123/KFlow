package BP.springCloud.controller;

import BP.Ga.*;
import BP.NodeGroup.*;
import BP.Tools.Json;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Template.Direction;
import BP.WF.Template.DirectionAttr;
import BP.WF.Template.Directions;
import BP.springCloud.tool.FeignTool;
import BP.springCloud.tool.PageTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-09-16 20:17
 **/
@Controller
@RequestMapping("composeGroup")
public class ComposeGroupController {

    private final Logger logger = LoggerFactory.getLogger(ComposeGroupController.class);

    /**
    *@Description: 获取flow下所有历史组合
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/9/16
    */
    @RequestMapping("getComposeGroups")
    @ResponseBody
    public void getComposeGroups(HttpServletRequest request, HttpServletResponse response){
        String flowNo=request.getParameter("flowNo");
        try {
            ComposeGroups groups=new ComposeGroups();
            groups.Retrieve(ComposeGroupAttr.flowNo,flowNo);
            PageTool.TransToResult(groups,request,response);
        }catch (Exception e){
            logger.error(e.getMessage());
        }

    }


    /**
    *@Description: 流程推荐（生成新流程）
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/10/4
    */
    @RequestMapping("composeNodeGroup")
    @ResponseBody
    public JSONObject composeNodeGroup(HttpServletRequest request, HttpServletResponse response) {
        try {
            String flowNo=request.getParameter("flowNo");
            ComposeGroup composeGroup=new ComposeGroup();
            composeGroup.setFlowNo(flowNo);
            composeGroup.setGroupNum(Integer.valueOf(request.getParameter("groupNum")));
            composeGroup.setGenerateNum(Integer.valueOf(request.getParameter("generateNum")));
            composeGroup.setVariationPro1(Float.valueOf(request.getParameter("variationPro1")));
            composeGroup.setVariationPro2(Float.valueOf(request.getParameter("variationPro2")));
            composeGroup.setAcrossPro2(Float.valueOf(request.getParameter("acrossPro2")));
            composeGroup.setAcrossPro1(Float.valueOf(request.getParameter("acrossPro1")));
            composeGroup.setElitePro(Float.valueOf(request.getParameter("elitePro")));
            composeGroup.setMaxSaveNum(Integer.valueOf(request.getParameter("saveNum")));
            composeGroup.setThreshold(Float.valueOf(request.getParameter("threshold")));

            GeneticAth geneticAth = new GeneticAth(flowNo,composeGroup);
            JSONObject history = geneticAth.run(2);

            //持久化训练过程数据
            String aveNo2= FeignTool.getSerialNumber("BP.History")+"";
            String maxNo2=FeignTool.getSerialNumber("BP.History")+"";
            insertHistory(history.getJSONArray("aveHistory").iterator(),aveNo2);
            insertHistory(history.getJSONArray("maxHistory").iterator(),maxNo2);

            JSONArray flows=history.getJSONArray("flows");
            Iterator it=flows.iterator();
            while (it.hasNext()){
                JSONObject item=(JSONObject)it.next();
                composeGroup.setNewFlowNo(item.getString("flowNo"));
                composeGroup.setScore(item.getDouble("score"));
                //保存该次训练结果
                composeGroup.setHistory(aveNo2+"_"+maxNo2);
                composeGroup.Insert();
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        JSONObject result=new JSONObject();
        return result;
    }


    /**
     * @Description: 初始化分组摘要信息（所有节点进行拼装）
     * @Param:
     * @return:
     * @Author: Mr.kong
     * @Date: 2020/9/16
     */
    @RequestMapping("initNodeGroup")
    @ResponseBody
    public com.alibaba.fastjson.JSONObject initNodeGroup() {
        try {
            NodeGroups groups=new NodeGroups();
            groups.RetrieveAll();
            List<NodeGroup> groupList=groups.toList();
            for (NodeGroup group:groupList){
                group.initInfo();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }



    @RequestMapping("getComposeHistory")
    @ResponseBody
    public JSONObject getComposeHistory(HttpServletRequest request){
        try {
            String no = request.getParameter("no");
            ComposeGroup composeGroup = new ComposeGroup(no);
            String history=composeGroup.getHistory();
            String[] nos=history.split("_");
            Historys historys=new Historys();
            JSONObject result=new JSONObject();

            int i=0;
            for (String historyNo:nos) {
                historys.Retrieve("history_no", historyNo);
                List<History> historyList = historys.toList();
                List<Double> aveHistory = new ArrayList<>();
                for (History temp : historyList) {
                    aveHistory.add(temp.getScore());
                }
                i++;
                result.put("history"+i,aveHistory);

            }

            return result;
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    //对比算法效果
    @RequestMapping("analyseGA")
    @ResponseBody
    public JSONObject composeNodeGroupRand(HttpServletRequest request) {
        try {
            String flowNo=request.getParameter("flowNo");
            ComposeGroup composeGroup=new ComposeGroup();
            composeGroup.setFlowNo(flowNo);
            composeGroup.setGroupNum(Integer.valueOf(request.getParameter("groupNum")));
            composeGroup.setGenerateNum(Integer.valueOf(request.getParameter("generateNum")));


            GeneticAthRand geneticAth1 = new GeneticAthRand(composeGroup);
            JSONObject history = geneticAth1.run(1);
            //持久化训练过程数据
            String aveNo1= FeignTool.getSerialNumber("BP.History")+"";
            String maxNo1=FeignTool.getSerialNumber("BP.History")+"";

            String aveNo2= FeignTool.getSerialNumber("BP.History")+"";
            String maxNo2=FeignTool.getSerialNumber("BP.History")+"";
            GeneticAthRand geneticAth2 = new GeneticAthRand(composeGroup);
            geneticAth2.groupGeneAll=geneticAth1.groupGeneAll;
            JSONObject history2=geneticAth2.run(2);
            insertHistory(history.getJSONArray("aveHistory").iterator(),aveNo1);
            insertHistory(history.getJSONArray("maxHistory").iterator(),maxNo1);

            insertHistory(history2.getJSONArray("aveHistory").iterator(),aveNo2);
            insertHistory(history2.getJSONArray("maxHistory").iterator(),maxNo2);


            //保存该次训练结果
            composeGroup.setHistory(aveNo1+"_"+maxNo1+"_"+aveNo2+"_"+maxNo2);
            composeGroup.Insert();


        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public void insertHistory(Iterator it,String historyNo) throws Exception{
        int i=Integer.parseInt(historyNo)*500+1;
        while (it.hasNext()){
            History historyTemp=new History();
            historyTemp.setScore((Double) it.next());
            historyTemp.setHistoryNo(historyNo);
            historyTemp.setNo(i+"");
            historyTemp.Insert();
            i++;
        }
    }

    @RequestMapping("autoLayout")
    @ResponseBody
    public JSONObject autoLayout(String flowNo) {
        try {
            Flow flow=new Flow(flowNo);
            List<Node> nodeList=flow.getHisNodes().toList();
            Node start=flow.getStartNode();
            //初始化节点所在图的最大深度
            Map<String,Integer> map=new HashMap<>();
            map.put(start.getNo(),1);
            initNodeX(map,start);

            //依据degree层次遍历,自动布局
            map=new HashMap<>();
            map.put(start.getNo(),start.getY());
            initNodeY(map,start);

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    private void initNodeX(Map<String,Integer> map,Node node ) throws Exception{
        int X=200;//布局横向间隔
        List<Node> nodeList=node.getHisToNodes().toList();
        int beforeDegree=map.get(node.getNo());
        for (Node next:nodeList){
            //没有被遍历，或者遍历的深度小于现在探索的深度
            if (!map.containsKey(next.getNo())||map.get(next.getNo())<beforeDegree+1){
                map.put(next.getNo(),beforeDegree+1);
                next.setX(node.getX()+X);
                next.Update();
                initNodeX(map,next);
            }
        }
    }

    private int initNodeY(Map<String,Integer> map,Node node) throws Exception{
        int height=80;//竖向间隔
        List<Node> nodeList=node.getHisToNodes().toList();
        int count=0;
        for (Node next:nodeList){
            if (!map.containsKey(next.getNo())||map.get(next.getNo())<node.getY()-height*count){
                next.setY(node.getY()-height*count);
                map.put(next.getNo(),next.getY());
                next.Update();
            }
            count+=initNodeY(map,next);
        }
        if (nodeList.size()>1)
            return count;
        return 1;
    }

}
