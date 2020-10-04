package BP.springCloud.controller;

import BP.Ga.*;
import BP.NodeGroup.*;
import BP.Tools.Json;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    *@Description:  推荐分组组合
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/9/16
    */
    @RequestMapping("composeNodeGroupRand")
    @ResponseBody
    public JSONObject composeNodeGroupRand(HttpServletRequest request, HttpServletResponse response) {
        try {
            String flowNo=request.getParameter("flowNo");
            ComposeGroup composeGroup=new ComposeGroup();
            composeGroup.setFlowNo(flowNo);
            composeGroup.setGroupNum(Integer.valueOf(request.getParameter("groupNum")));
            composeGroup.setGenerateNum(Integer.valueOf(request.getParameter("generateNum")));
            composeGroup.setVariationPro(Float.valueOf(request.getParameter("variationPro")));
            composeGroup.setAcrossPro(Float.valueOf(request.getParameter("acrossPro")));
            composeGroup.setElitePro(Float.valueOf(request.getParameter("elitePro")));
            composeGroup.setMaxSaveNum(Integer.valueOf(request.getParameter("saveNum")));
            composeGroup.setThreshold(Float.valueOf(request.getParameter("threshold")));


            GeneticAthRand geneticAth = new GeneticAthRand(composeGroup);
            JSONObject history = geneticAth.run(1);

            //持久化训练过程数据
            JSONArray aveHistory=history.getJSONArray("aveHistory");
            JSONArray maxHistory=history.getJSONArray("maxHistory");
            String aveNo= FeignTool.getSerialNumber("BP.History")+"";
            String maxNo=FeignTool.getSerialNumber("BP.History")+"";

            insertHistory(aveHistory.iterator(),aveNo);
            insertHistory(maxHistory.iterator(),maxNo);

            //保存该次训练结果
            composeGroup.setHistory(aveNo+"_"+maxNo);
            composeGroup.Insert();


        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        JSONObject result=new JSONObject();
        return result;
    }



    /**
    *@Description: 随机生成组合数据，测试算法
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
            composeGroup.setVariationPro(Float.valueOf(request.getParameter("variationPro")));
            composeGroup.setAcrossPro(Float.valueOf(request.getParameter("acrossPro")));
            composeGroup.setElitePro(Float.valueOf(request.getParameter("elitePro")));
            composeGroup.setMaxSaveNum(Integer.valueOf(request.getParameter("saveNum")));
            composeGroup.setThreshold(Float.valueOf(request.getParameter("threshold")));

            GeneticAth geneticAth = new GeneticAth(flowNo,composeGroup);
            JSONObject data = geneticAth.run();
            JSONArray flows=data.getJSONArray("flows");
            Iterator it=flows.iterator();
            while (it.hasNext()){
                JSONObject item=(JSONObject)it.next();
                composeGroup.setNewFlowNo(item.getString("flowNo"));
                composeGroup.setScore(item.getDouble("score"));
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
                    //只初始化没有进行总结的分组
                    if (StringUtils.isEmpty(group.GetValStrByKey(NodeGroupAttr.abstracts))) {
                        NodeGroupItems items = new NodeGroupItems();
                        items.Retrieve(NodeGroupItemAttr.group_no, group.getNo());
                        List<NodeGroupItem> itemList = items.toList();
                        StringBuilder sb = new StringBuilder();
                        String flowName = group.GetValStrByKey(NodeGroupAttr.flow_name);
                        if (!StringUtils.isEmpty(flowName))
                            sb.append(flowName);
                        for (NodeGroupItem item : itemList) {
                            sb.append(item.GetValStrByKey(NodeGroupItemAttr.node_name));
                            sb.append("，");
                        }
                        group.SetValByKey(NodeGroupAttr.abstracts, sb.toString());
                        group.Update();
                    }
                }
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
            historys.Retrieve("history_no",nos[0]);
            List<History> historyList=historys.toList();
            List<Double> aveHistory=new ArrayList<>();
            for (History temp:historyList){
                aveHistory.add(temp.getScore());
            }

            historys.Retrieve("history_no",nos[1]);
            historyList=historys.toList();
            List<Double> maxHistory=new ArrayList<>();
            for (History temp:historyList){
                maxHistory.add(temp.getScore());
            }

            JSONObject result=new JSONObject();
            result.put("maxHistory",maxHistory);
            result.put("aveHistory",aveHistory);
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
            String aveNo= FeignTool.getSerialNumber("BP.History")+"";
            String maxNo=FeignTool.getSerialNumber("BP.History")+"";
            insertHistory(history.getJSONArray("aveHistory").iterator(),aveNo);

            GeneticAthRand geneticAth2 = new GeneticAthRand(composeGroup);
            geneticAth2.groupGeneAll=geneticAth1.groupGeneAll;
            history=geneticAth2.run(2);
            insertHistory(history.getJSONArray("aveHistory").iterator(),maxNo);

            //保存该次训练结果
            composeGroup.setHistory(aveNo+"_"+maxNo);
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

}
