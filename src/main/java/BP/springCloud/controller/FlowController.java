package BP.springCloud.controller;

import BP.Project.ProjectTree;
import BP.Project.ProjectTreeAttr;
import BP.Sys.EnCfg;
import BP.Task.GenerFlowService;
import BP.Task.NodeTaskService;
import BP.WF.Dev2Interface;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Template.NodeAttr;
import BP.Web.WebUser;
import BP.springCloud.entity.GenerFlow;
import BP.springCloud.entity.NodeTaskM;
import BP.springCloud.tool.FeignTool;
import BP.springCloud.tool.PageTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-04-02 14:51
 **/
@Controller
@RequestMapping("flow")
public class FlowController {
    private  final Logger logger = LoggerFactory.getLogger(FlowController.class);

    @Resource
    private NodeTaskService nodeTaskService;

    @Resource
    private GenerFlowService generFlowService;

    @RequestMapping("startFlow")
    @ResponseBody
    public JSONObject startFlow(String flowNo) {
        JSONObject result=new JSONObject();
        try {
            Long workGroupId= FeignTool.getSerialNumber("BP.WF.Work");
            Long parentWorkId=0-FeignTool.getSerialNumber("BP.WF.ParentWork");
            Flow flow=new Flow(flowNo);
            startFlow(workGroupId,parentWorkId,-1L,flow);
                //起始节点任务状态更新为可开始
                NodeTaskM con=new NodeTaskM();
                con.setWorkGroupId(workGroupId+"");
                con.setNodeId(flow.getStartNodeID()+"");
                List<NodeTaskM> list=nodeTaskService.findNodeTaskList(con);
                if (list!=null&&list.size()==1){
                    NodeTaskM startTask=list.get(0);
                    startTask.setIsReady(1);
                    startTask.setStatus(1);
                    nodeTaskService.updateNodeTask(startTask);
                }
                result.put("success",true);

        }catch (Exception e){
            logger.error(e.getMessage());
        }

        return result;
    }

    @RequestMapping("startProject")
    @ResponseBody
    public JSONObject startProject(String projectNo) {
        JSONObject result=new JSONObject();
        try {
            //启动项目时，发起流程，则ProjectNo作为GroupWorkNo
            Long workGroupId= Long.valueOf(projectNo);
            Long parentWorkId=0-FeignTool.getSerialNumber("BP.WF.ParentWork");
            ProjectTree project=new ProjectTree(projectNo);

            Flow flow=new Flow(project.GetValStrByKey(ProjectTreeAttr.FlowNo));
            long workId=startFlow(workGroupId,parentWorkId,-1L,flow);

            //更新项目信息
            project.SetValByKey(ProjectTreeAttr.Status,1);//流程启动
            project.SetValByKey(ProjectTreeAttr.GenerFlowNo,workId);
            project.Update();
                //起始节点任务状态更新为可开始
            NodeTaskM con=new NodeTaskM();
            con.setWorkGroupId(workGroupId+"");
            con.setNodeId(flow.getStartNodeID()+"");
            List<NodeTaskM> list=nodeTaskService.findNodeTaskList(con);
            if (list!=null&&list.size()==1){
                NodeTaskM startTask=list.get(0);
                startTask.setIsReady(1);
                startTask.setStatus(1);
                nodeTaskService.updateNodeTask(startTask);
            }
            result.put("success",true);

        }catch (Exception e){
            logger.error(e.getMessage());
        }

        return result;
    }

    /**
     *@Description: 启动流程，产生所有对节点任务
     *@Param:
     *@return:
     *@Author: Mr.kong
     *@Date: 2020/3/8
     */
    public  long startFlow(Long workGroupId,Long parentWorkId, Long parentTaskId, Flow flow) throws Exception{

        if (!beforeStart(flow))
            return -1L;

        Long workId= FeignTool.getSerialNumber("BP.WF.Work");

        int sumTime=0;
        Nodes nodes=new Nodes();
        nodes.Retrieve(NodeAttr.FK_Flow,flow.getNo());
        List<Node> nodeList=nodes.toList();
        for (Node node:nodeList){
                createNodeTask(workGroupId,workId,parentTaskId,node);
                sumTime+=Integer.valueOf(node.GetValStrByKey(NodeAttr.Doc));
        }

        //创建流程实例信息
        GenerFlow generFlow=new GenerFlow();
        generFlow.setNo(workId);
        generFlow.setWorkId(workId);
        generFlow.setWorkGroupId(workGroupId);
        generFlow.setParentWorkId(parentWorkId);
        generFlow.setFlowId(Integer.valueOf(flow.getNo()));
        generFlow.setStatus(1);
        generFlow.setCreator(WebUser.getNo());
        generFlow.setActivatedNodes(flow.getStartNodeID()+",");
        generFlow.setTotalTime(sumTime);
        generFlowService.insertGenerFlow(generFlow);

        //更新节点任务间关系
        nodeTaskService.updateNodeTaskPreAfter(flow.getStartNodeID()+"",workId);
        return workId;
    }

    /**
     *@Description: 流程发起前判断是否允许启动（流程使用资源是否到位，）
     *@Param:
     *@return:
     *@Author: Mr.kong
     *@Date: 2020/3/16
     */
    public  Boolean beforeStart(Flow flow){

        return true;
    }
    /**
     *@Description: 创建流程节点任务（如果该节点下包含子流程，则递归创建子流程任务）
     * 没有考虑事务问题（建议后续增加事务）
     *@Param:
     *@return:
     *@Author: Mr.kong
     *@Date: 2020/3/8
     */
    public  void createNodeTask(Long workGroupID,Long workId,Long parentTaskId,Node node) throws Exception{
        int nodeId=node.getNodeID();

        //创建节点任务
        Long nodeTaskId= FeignTool.getSerialNumber("BP.Task.NodeTask");

        NodeTaskM nodeTask=new NodeTaskM();
        nodeTask.setTotalTime(Integer.valueOf(node.GetValStringByKey(NodeAttr.Doc)));
        nodeTask.setNodeId(nodeId+"");
        nodeTask.setNodeName(node.getName());
        nodeTask.setFlowId(node.GetValStringByKey(NodeAttr.FK_Flow));
        nodeTask.setFlowName(node.getFlowName());
        nodeTask.setWorkGroupId(workGroupID+"");
        nodeTask.setWorkId(workId+"");
        nodeTask.setParentNodeTask(parentTaskId+"");
        nodeTask.setNo(nodeTaskId);
        nodeTask.setIsReady(20);//未准备
        nodeTask.setStatus(20);
        //因为执行人需要汉卿的计划，所以现在直接指定
        nodeTask.setExecutor(WebUser.getNo());
        Date start=node.GetValDateTime(NodeAttr.EarlyStart);
        Date end=node.GetValDateTime(NodeAttr.LaterFinish);
        nodeTask.setPlanEndTime(end);
        nodeTask.setPlanStartTime(start);
        nodeTask.setEndTime(end);
        nodeTask.setStartTime(start);
        nodeTask.setEarlyStartTime(start);
        nodeTask.setOldestFinishTime(end);
        nodeTask.setTaskType(node.GetValIntByKey(NodeAttr.TaskType));
        nodeTask.setTaskPriority(node.GetValIntByKey(NodeAttr.TaskPriority));
        nodeTask.setTaskWorkModel(node.GetValIntByKey(NodeAttr.WorkModel));
        nodeTaskService.insertNodeTask(nodeTask);

        String[] childFlows=node.getSubFlowNos();
        if (childFlows!=null) {
            for (String childFlowNo : childFlows) {
                Flow f = new Flow(childFlowNo);
                startFlow(workGroupID, workId, nodeTaskId, f);
            }
        }
    }


    @RequestMapping("getAfterNodes")
    @ResponseBody
    public JSONObject getAfterNodes(HttpServletRequest request, HttpServletResponse response,@RequestParam String nodeId){
        try {
            Node node=new Node(nodeId);
            Nodes nodes=node.getCanStartNode();
            PageTool.TransToResult(nodes,request,response);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
    *@Description: 检查回退节点设置是否符合条件
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/5/12
    */
    @RequestMapping("checkReturn")
    @ResponseBody
    public JSONObject checkReturnNode(HttpServletRequest request, HttpServletResponse response){
        String curNode=request.getParameter("curNode");
        String toNodeId=request.getParameter("toNode");
        JSONObject result=new JSONObject();
        try{
            List<Integer> needReturnNodes=Dev2Interface.getNeedReturnNodes(Integer.parseInt(curNode),Integer.parseInt(toNodeId));
            if (needReturnNodes==null)//检查不通过
                result.put("success",false);
            else
                result.put("success",true);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return result;
    }

    /**
    *@Description: 获取系统流程信息（流程数，节点数，实例数，项目数） 
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/9/1 
    */
    @RequestMapping("getSystemFlowInfo")
    @ResponseBody
    public JSONObject getSystemFlowInfo(){
        JSONObject data=new JSONObject();
        try {
            EnCfg enCfg = new EnCfg("System.FlowInfo");
            Map<String,String> map=enCfg.getMap();
            data.putAll(map);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return data;
    }

}
