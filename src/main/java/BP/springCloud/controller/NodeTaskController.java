package BP.springCloud.controller;

import BP.En.Row;
import BP.Port.Emp;
import BP.Task.*;
import BP.Tools.StringUtils;
import BP.WF.*;
import BP.Web.WebUser;
import BP.springCloud.entity.GenerFlow;
import BP.springCloud.entity.NodeTaskM;
import BP.springCloud.tool.FeignTool;
import BP.springCloud.tool.PageTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
import java.util.*;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-03-04 18:06
 **/
@Controller()
@RequestMapping("nodeTask")
@Api(value = "节点任务",description = "节点任务操作 API", position = 100, protocols = "http")

public class NodeTaskController {

    private final Logger logger = LoggerFactory.getLogger(NodeTaskController.class);

    @Resource
    private NodeTaskService nodeTaskService;

    @Resource
    private GenerFlowService generFlowService;
    /**
    *@Description: 查询节点任务列表（条件查询）
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/3/4
    */
    @RequestMapping("/getNodeTasks")
    public void getNodeTaskList(HttpServletRequest request, HttpServletResponse response){
        try {
            NodeTaskM con=new NodeTaskM();
            String executor=request.getParameter("executor");
            if (executor!=null&&!executor.equals(""))
                con.setExecutor(executor);
            else
                con.setExecutor(WebUser.getNo());
            String nodeTaskNo=request.getParameter("nodeTaskNo");
            if (!StringUtils.isEmpty(nodeTaskNo))
                con.setNo(Long.parseLong(nodeTaskNo));

            String workId=request.getParameter("workId");
            if (!StringUtils.isEmpty(workId))
                con.setWorkId(workId);

            String flowNo=request.getParameter("flowNo");
            if (!StringUtils.isEmpty(flowNo))
                con.setFlowId(flowNo);

            String status=request.getParameter("status");
            if (!StringUtils.isEmpty(status))
                con.setStatus(Integer.parseInt(status));
            else {
                con.setIsReady(1);
            }

            List nodeTaskMList=nodeTaskService.findNodeTaskAllList(con);
            PageTool.TransToResultList(nodeTaskMList,request,response);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

/**
*@Description:
*@Param:
*@return:
*@Author: Mr.kong
*@Date: 2020/5/7
*/
    @RequestMapping("/getNodeTaskByNo")
    @ResponseBody
    public Object getNodeTask(String no){
        NodeTaskM nodeTask=nodeTaskService.getNodeTaskById(Long.parseLong(no));
        return JSONObject.fromObject(nodeTask);
    }

    /**
    *@Description:  获取节点表单最新版本数据
    *@Param: no为nodeTaskNo
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/5/7
    */
    @RequestMapping("/getLatestWork")
    @ResponseBody
    public Object getLatestWork(String no){
        NodeTaskM nodeTask=nodeTaskService.getNodeTaskById(Long.parseLong(no));
        try {
            Node node=new Node(nodeTask.getNodeId());
            Work work=node.getHisWork();
            work.setOID(Long.parseLong(nodeTask.getWorkId()));
            work.RetrieveFromDBSources();
            Row row=work.getRow();
            //当前数据不是最新版本数据，获取最新版本workId
            int newVersion=(int)row.get("newVersion");
            int oid=(int)row.get("OID");
            if (newVersion!=0&&newVersion!=oid)//有节点任务回退的情况
                nodeTask.setWorkId(newVersion+"");
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return JSONObject.fromObject(nodeTask);
    }

    @RequestMapping("/startNodeTask")
    public void startNodeTask(String no){
        try {
            NodeTaskM nodeTaskM=nodeTaskService.getNodeTaskById(Long.parseLong(no));
            if (nodeTaskM.getIsReady()==1){
                nodeTaskM.setStartTime(new Date());
                nodeTaskM.setIsReady(2);
                nodeTaskM.setStatus(2);
                nodeTaskService.updateNodeTask(nodeTaskM);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    /**
    *@Description: 完成当前节点任务，并激活下一节点任务
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/3/18
    */
    @ApiOperation(
            value = "完成节点任务",
            notes = "结束当前节点任务，启动后续节点任务",
            produces = "application/json, application/xml",
            consumes = "application/json, application/xml",
            response = JSONObject.class
    )
    @RequestMapping("sendNode")
    @ResponseBody
    public JSONObject sendNode(Long nodeTaskNo){
        JSONObject result=new JSONObject();
        try {
            //完善NodeTask信息
            NodeTaskM currentTask=nodeTaskService.getNodeTaskById(nodeTaskNo);

            //完成节点任务，并将节点任务进行下发（）
            String meg=nodeTaskService.finishNodeTask(currentTask);
            result.put("message",meg);
        }catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    /**
    *@Description: 人为更新流程引擎中所有节点任务状态
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/2
    */
    @RequestMapping("updateTasksStatus")
    @ResponseBody
    public JSONObject updateTasksStatus(@RequestBody int type){
        NodeTaskM con=new NodeTaskM();
        try {
            if (type==1)
                con.setExecutor(WebUser.getNo());//更新所执行任务
            List<NodeTaskM> nodeTaskMList=nodeTaskService.findNodeTaskList(con);
            for (NodeTaskM temp:nodeTaskMList){
                int status=nodeTaskService.getTaskStatus(temp);
                temp.setStatus(status);
                nodeTaskService.updateNodeTask(temp);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }


    /**
    *@Description: 查询该WorkId下面所有任务进展信息(单个实例层级)
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/5
    */
    public JSONObject getNodeTasksByWorkId(Long generFlowNo){
        return null;
    }

    /**
     *@Description: 查询该WorkId下面所有任务进展信息(多粒度实例层级)
     *@Param:
     *@return:
     *@Author: Mr.kong
     *@Date: 2020/4/5
     */
    public JSONObject getNodeTasksByGroupWorkId(String groupWorkId){
        return null;
    }


    /**
    *@Description: 查询流程实例下所有任务的甘特数据
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/8/30
    */
    @RequestMapping("getNodeTaskGantData")
    @ResponseBody
    public JSONObject getNodeTaskGantData(Long generFlowNo,int depth){
        List<JSONObject> seriesList=new ArrayList<>();

        JSONObject seriesN=new JSONObject();
        seriesN.put("name","实例"+generFlowNo);
        GenerFlow generFlow=generFlowService.getGenerFlow(generFlowNo);
        List<JSONObject> data= nodeTaskService.getGantData(generFlow,depth);
        seriesN.put("data",JSONArray.fromObject(data));
        seriesList.add(seriesN);

        JSONObject xAxis=new JSONObject();
        xAxis.put("currentDateIndicator",true);

        int shiCha=(8+8) * 60 * 60 * 1000;//时差，
        xAxis.put("min",generFlow.getCreateTime().getTime()+shiCha);
        if (generFlow.getStatus()!=2) {
            Date cur=new Date();
            xAxis.put("max", cur.getTime() + shiCha);
        }else
            xAxis.put("max",generFlow.getFinishTime().getTime()+shiCha);

        JSONObject result=new JSONObject();
        try {
            result.put("series", JSONArray.fromObject(seriesList));
            result.put("xAxis",xAxis);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return result;
    }

    /**
    *@Description:  查询用户名下的所有节点任务，甘特数据
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/8/30
    */
    @RequestMapping("getMyTaskGantData")
    @ResponseBody
    public JSONObject getMyTaskGantData(@RequestBody NodeTaskM con) throws Exception{
        List<JSONObject> seriesList=new ArrayList<>();

        JSONObject seriesN=new JSONObject();
        seriesN.put("name","任务甘特图");
        String userNo = WebUser.getNo();
        con.setExecutor(userNo);
        List<JSONObject> data= nodeTaskService.getMyTaskGant(con);
        seriesN.put("data",JSONArray.fromObject(data));
        seriesList.add(seriesN);

        JSONObject result=new JSONObject();
        try {
            result.put("series", JSONArray.fromObject(seriesList));
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return result;
    }

    @RequestMapping("checkNodeTaskIsFinish")
    @ResponseBody
    public boolean checkNodeTaskIsFinish(@RequestParam Long no){
        NodeTaskM nodeTaskM=nodeTaskService.getNodeTaskById(no);
        if (nodeTaskM.getIsReady()==3){
            return false;
        }
        return true;
    }

    /**
    *@Description: 下发监管命令 
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/5/20 
    */
    @RequestMapping("doManage")
    @ResponseBody
    public String doManage(HttpServletRequest request, HttpServletResponse response){
        String generFlowNo=request.getParameter("generFlowNo");
        String nodeId=request.getParameter("nodeId");
        String message=request.getParameter("message");

        String result="";
        try {
            //产生节点ReturnWork
            ReturnWork returnWork=new ReturnWork();
            returnWork.setWorkID(Integer.parseInt(generFlowNo));
            returnWork.setReturnToNode(Integer.parseInt(nodeId));
            returnWork.setBeiZhu(message);
            returnWork.setType(2);//监管命令
            returnWork.Insert();

            //短信通知
            NodeTasks tasks=new NodeTasks();
            tasks.Retrieve(NodeTaskAttr.WorkId,generFlowNo,NodeTaskAttr.NodeId,nodeId);
            NodeTask task=(NodeTask) tasks.get(0);
            String executor=task.getExecutor();
            Emp emp=new Emp(executor);
            if (!org.springframework.util.StringUtils.isEmpty(emp.getMobile())) {
                //FeignTool.sendPhoneMessage(emp.getMobile(), message);
                //FeignTool.sendPhoneMessage(emp.getMobile(), task.getNo());
                result="短信已经通知！";
            }else
                result="人员信息中无手机号！";
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return result;
    }

    /**
    *@Description: 查看节点任务消息（任务回退信息，监管信息，历史work信息等）
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/5/20
    */
    @RequestMapping("getTaskMessage")
    @ResponseBody
    public Object getTaskMessage(HttpServletRequest request, HttpServletResponse response){
        String nodeId=request.getParameter("nodeId");
        String workId=request.getParameter("workId");

        try {
            ReturnWorks rws = new ReturnWorks();
            rws.Retrieve(ReturnWorkAttr.WorkID, workId, ReturnWorkAttr.ReturnToNode, nodeId);
            PageTool.TransToResult(rws,request,response);

        }catch (Exception e){
            logger.error(e.getMessage());
        }

        return null;
    }

    @RequestMapping("getTaskInfoOneStatus")
    @ResponseBody
    public JSONObject getTaskInfoOneStatus(@RequestBody NodeTaskM con){
        JSONObject data=new JSONObject();
        try{
            String userNo=WebUser.getNo();
            con.setExecutor(userNo);
            List<NodeTaskM> list=nodeTaskService.findNodeTaskList(con);
            Map<String,JSONObject> map=new HashMap<>();
            for (NodeTaskM nodeTaskM:list){
                JSONObject temp=map.get(nodeTaskM.getWorkId());
                if (temp==null){
                    temp=new JSONObject();
                    temp.put("num",0);
                    temp.put("sum",0);
                    map.put(nodeTaskM.getWorkId(),temp);
                }
                temp.put("num",temp.getInt("num")+1);
                temp.put("sum",temp.getInt("sum")+nodeTaskM.getTotalTime());
            }

            //组装eChart数据
            List<String> xAxis=new ArrayList<>(map.size());
            List<Integer> bar=new ArrayList<>(map.size());
            List<Integer> line=new ArrayList<>(map.size());
            for (Map.Entry<String,JSONObject> item:map.entrySet()){
                xAxis.add(item.getKey());
                JSONObject object=item.getValue();
                bar.add(object.getInt("sum"));
                line.add(object.getInt("num"));
            }
            data.put("lineData",JSONArray.fromObject(line));
            data.put("barData",JSONArray.fromObject(bar));
            data.put("xAxisData",JSONArray.fromObject(xAxis));

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return data;
    }

    @RequestMapping("getTaskInfoOneStatusMulti")
    @ResponseBody
    public JSONObject getTaskInfoOneStatusMulti(@RequestBody NodeTaskM con){
        JSONObject data=new JSONObject();
        try{
            String userNo=WebUser.getNo();
            con.setExecutor(userNo);
            List<NodeTaskM> list=nodeTaskService.findNodeTaskList(con);
            Map<String,List<JSONObject>> map=new HashMap<>();
            List<String> workIdList=new ArrayList<>();
            for (NodeTaskM nodeTaskM:list){
                List<JSONObject> temp=map.get(nodeTaskM.getWorkId());
                if (temp==null){
                    temp=new ArrayList<JSONObject>();
                    map.put(nodeTaskM.getWorkId(),temp);
                    workIdList.add(nodeTaskM.getWorkId());
                }
                JSONObject barItem=new JSONObject();
                barItem.put("value",nodeTaskM.getTotalTime());
                barItem.put("taskId",nodeTaskM.getNo());
                temp.add(barItem);
            }

            JSONObject zero=new JSONObject();
            zero.put("value",0);
            JSONObject label=new JSONObject();
            label.put("show",true);
            label.put("position","insideRight");

            //组装eChart数据
            List<JSONObject> bar=new ArrayList<>(map.size());
            boolean mark=true;
            int count=0;
            while (mark){
                mark=false;
                JSONObject level=new JSONObject();
                List<JSONObject> levelData=new ArrayList<>(workIdList.size());
                for (String workId:workIdList){
                    List<JSONObject> tempList=map.get(workId);
                    if (tempList.size()>count){
                        levelData.add(tempList.get(count));
                        mark=true;
                    }else
                        levelData.add(zero);
                }
                count++;
                level.put("data",levelData);
                level.put("type","bar");
                level.put("label",label);
                level.put("stack","总量");
                bar.add(level);
            }

            data.put("series",JSONArray.fromObject(bar));
            data.put("xAxis",JSONArray.fromObject(workIdList));

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return data;
    }

    @RequestMapping("getTaskInfoForStatus")
    @ResponseBody
    public JSONObject getTaskInfoForStatus(@RequestBody NodeTaskM con){
        JSONObject data=new JSONObject();
        try {
            String userNo=WebUser.getNo();
            con.setExecutor(userNo);
            List<NodeTaskM> list=nodeTaskService.findNodeTaskList(con);

            //每种状态的任务
            List<NodeTaskM>[] items=new List[9];
            for (int i=0;i<items.length;i++){
                items[i]=new ArrayList();
            }
            for (NodeTaskM nodeTaskM:list){
                int status=nodeTaskM.getStatus();
                List<NodeTaskM> item;
                if (status==20){
                    item=items[0];
                }else {
                    item=items[status];
                }
                item.add(nodeTaskM);
            }

            String[] names={"未准备","可以开始","已经开始","已经完成","逾期开始","警告开始","正常","逾期结束","警告结束"};
            List<JSONObject> pieData=new ArrayList<>();
            for (int i=0;i<names.length;i++){
                JSONObject temp=new JSONObject();
                temp.put("name",names[i]);
                temp.put("value",items[i].size());
                if (i==0)
                    temp.put("status",20);
                else
                    temp.put("status",i);
                pieData.add(temp);
            }
            data.put("pieData",JSONArray.fromObject(pieData));

        }catch (Exception e){
            logger.error(e.getMessage());
        }

        return data;
    }


}
