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
                //模块分组
                if (group.GetValIntByKey(NodeGroupAttr.type)!=1){
                    NodeGroupItems items = new NodeGroupItems();
                    items.Retrieve(NodeGroupItemAttr.group_no, group.getNo());
                    List<NodeGroupItem> itemList = items.toList();
                    //只初始化没有进行摘要总结的分组
                    if (StringUtils.isEmpty(group.GetValStrByKey(NodeGroupAttr.abstracts))) {
                        StringBuilder sb = new StringBuilder();
                        String flowName = group.GetValStrByKey(NodeGroupAttr.flow_name);
                        if (!StringUtils.isEmpty(flowName))
                            sb.append(flowName);
                        for (NodeGroupItem item : itemList) {
                            sb.append(item.GetValStrByKey(NodeGroupItemAttr.node_name));
                            sb.append("，");
                        }
                        group.SetValByKey(NodeGroupAttr.abstracts, sb.toString());
                    }
                    //检查模块是否符合“一输入、一输出结构”
                    if (!checkGroup(group)){
                        group.SetValByKey(NodeGroupAttr.type,0);
                    }else {
                        group.SetValByKey(NodeGroupAttr.type,2);
                    }
                    //更新总工时、节点数
                    group.SetValByKey(NodeGroupAttr.nodeNum,itemList.size());
                    int sumTime=0;
                    for (NodeGroupItem item:itemList) {
                        Node node=new Node(item.GetValStrByKey(NodeGroupItemAttr.node_no));
                        sumTime += node.getDoc();
                    }
                    group.SetValByKey(NodeGroupAttr.sumTime,sumTime);
                    group.Update();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    //初始化模块的输入输出节点编码
    private boolean checkGroup(NodeGroup group){
        try {
            NodeGroupItems items = new NodeGroupItems();
            items.Retrieve(NodeGroupItemAttr.group_no, group.getNo());
            Map<String,int[]> map=new HashMap<>();//int[0]进度，int[1]出度
            List<NodeGroupItem> itemList=items.toList();
            for (NodeGroupItem item:itemList){
                map.put(item.GetValStrByKey(NodeGroupItemAttr.node_no),new int[2]);
            }

            for (NodeGroupItem item:itemList){
                String nodeId=item.GetValStrByKey(NodeGroupItemAttr.node_no);
                Directions directions=new Directions();
                directions.Retrieve(DirectionAttr.ToNode,nodeId);
                List<Direction> list=directions.toList();
                for (Direction direction:list){
                    int[] mark=map.get(nodeId);
                    if (map.containsKey(direction.getNode()+"")){
                        //进度加1
                        mark[0]++;
                    }
                }

                directions.Retrieve(DirectionAttr.Node,nodeId);
                list=directions.toList();
                for (Direction direction:list){
                    int[] mark=map.get(nodeId);
                    if (map.containsKey(direction.getToNode()+"")){
                        //出度加1
                        mark[1]++;
                    }
                }
            }
            String in0="";
            String out0="";
            //判断出度为0的节点数为1，入度为0的节点数为1，则符合模块要求
            for (Map.Entry<String ,int[]> entry:map.entrySet()){
                int[] mark=entry.getValue();
                if (mark[0]==0){
                    if (in0.equals(""))
                        in0=entry.getKey();
                    else
                        return false;
                }

                if (mark[1]==0){
                    if (out0.equals(""))
                        out0=entry.getKey();
                    else
                        return false;
                }

            }
            group.SetValByKey(NodeGroupAttr.inNodeNo,in0);
            group.SetValByKey(NodeGroupAttr.outNodeNo,out0);
            group.Update();
            return true;
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return false;
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
        if (nodeList.size()>0)
            return count;
        return count+1;
    }

}
