package BP.springCloud.controller;

import BP.Project.ProjectTree;
import BP.Project.ProjectTreeAttr;
import BP.Project.ProjectTrees;
import BP.Resource.*;
import BP.Task.*;
import BP.WF.Flow;
import BP.WF.Flows;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Template.FlowAttr;
import BP.WF.Template.FlowExt;
import BP.WF.Template.FlowExts;
import BP.WF.Template.NodeAttr;
import BP.springCloud.entity.NodeTaskM;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.List;
import java.util.Queue;

/**
 * @program: kflow-web
 * @description: 生产测试数据
 * @author: Mr.Kong
 * @create: 2020-10-16 10:38
 **/
@RestController
@RequestMapping("test")
public class TestController {
    private  final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Resource
    private NodeTaskService nodeTaskService;

    /**
    *@Description: 生成资源任务数据（资源负载使用）
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/10/16 
    */
    @RequestMapping("createResourceTask")
    public JSONObject createResourceTask(@RequestBody String workGroup){
        try {
            ResourceTasks resourceTasks = new ResourceTasks();
            resourceTasks.RetrieveAll();
            List<ResourceTask> resList=resourceTasks.toList();
            for (ResourceTask task:resList){
                String resourceId=task.GetValStrByKey(ResourceTaskAttr.ResourceId);
                if (StringUtils.isEmpty(resourceId)){
                    String resourceNo=task.GetValStrByKey(ResourceTaskAttr.ResourceNo);
                    ResourceItems items=new ResourceItems();
                    items.Retrieve(ResourceItemAttr.Kind,resourceNo);
                    int pos=(int)Math.floor(Math.random()*items.size());
                    ResourceItem item=(ResourceItem) items.get(pos);
                    task.SetValByKey(ResourceTaskAttr.ResourceId,item.getNo());
                    task.Update();
                }
                String taskId=task.GetValStrByKey(ResourceTaskAttr.TaskId);
                if (StringUtils.isEmpty(taskId)){
                    NodeTasks nodeTasks=new NodeTasks();
                    nodeTasks.Retrieve(NodeTaskAttr.NodeId,task.GetValStrByKey(ResourceTaskAttr.NodeId));
                    if (nodeTasks.size()>0){
                        NodeTask nodeTask=(NodeTask)nodeTasks.get(0);
                        task.SetValByKey(ResourceTaskAttr.TaskId,nodeTask.getNo());
                        task.Update();
                    }
                }
            }

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }


    /**
    *@Description: 随机创建资源方案，并随机配置资源类别（资源数均为1）
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/10/16
    */
    @RequestMapping("createResourcePlan")
    public JSONObject createResourcePlan(@RequestBody String flow){
        updateResourcePlan(flow);
        return null;
    }

    private void updateResourcePlan(String flowId){
        try {
            String[] resList={"R_adams_1","S_LX_16T_1","P_L1_admin","S_R_A","S_J5_A1","HJ_RM_C"};
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<Node> nodeList=new Nodes(flowId).toList();
            int count=0;
            for (Node node:nodeList){
                ResourcePlans plans=new ResourcePlans();
                plans.Delete(ResourcePlanAttr.NodeId,node.getNodeID());
                ResourceTasks resourceTasks=new ResourceTasks();
                resourceTasks.Delete(ResourceTaskAttr.NodeId,node.getNodeID());

                ResourcePlan plan=new ResourcePlan();
                plan.SetValByKey(ResourcePlanAttr.NodeId,node.getNodeID());
                plan.Insert();
                int num=(int)Math.floor(4*Math.random())+1;
                for (int i=0;i<num;i++) {
                    ResourceTask resTask = new ResourceTask();
                    resTask.SetValByKey(ResourceTaskAttr.NodeId, node.getNodeID());
                    resTask.SetValByKey(ResourceTaskAttr.ResourceNo, resList[count%resList.length]);

                    Date start=node.GetValDateTime(NodeAttr.EarlyStart);
                    String startStr=formatter.format(start);
                    resTask.SetValByKey(ResourceTaskAttr.BookStart,startStr);
                    Date end=node.GetValDateTime(NodeAttr.LaterFinish);
                    String endStr=formatter.format(end);
                    resTask.SetValByKey(ResourceTaskAttr.BookEnd, endStr);
                    resTask.SetValByKey(ResourceTaskAttr.StartTime, startStr);
                    resTask.SetValByKey(ResourceTaskAttr.EndTime, endStr);
                    resTask.SetValByKey(ResourceTaskAttr.PlanStart, startStr);
                    resTask.SetValByKey(ResourceTaskAttr.PlanEnd, endStr);
                    resTask.SetValByKey(ResourceTaskAttr.UseTime, node.getDoc());
                    resTask.SetValByKey(ResourceTaskAttr.PlanId, plan.getNo());
                    resTask.SetValByKey(ResourceTaskAttr.IsPlan, 2);
                    resTask.SetValByKey(ResourceTaskAttr.IsFinish, 2);
                    resTask.Insert();
                    count++;
                }

            }

        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    /**
    *@Description: 随机设置节点的时间属性（最早开始、最晚结束、工时）
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/10/16
    */
    @RequestMapping("initFlowNodeInfo")
    public JSONObject initFlowNode(String startTime,String flowId){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long start=formatter.parse(startTime).getTime();
            initFlowNodeInfo(start,flowId);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    @RequestMapping("initProjectInfo")
    public JSONObject initProjectInfo(String startTime){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long start=formatter.parse(startTime).getTime();
            long week=7*24*60*60*1000L;
            long mouth=30*24*60*60*1000L;
            ProjectTrees trees=new ProjectTrees();
            trees.Retrieve(ProjectTreeAttr.Status,1);
            List<ProjectTree> projectList=trees.toList();
            for(ProjectTree tree:projectList){
                String flowId=tree.GetValStrByKey(ProjectTreeAttr.FlowNo);
                initFlowNodeInfo(start,flowId);
                start+=mouth;
                Flow flow=new Flow(flowId);
                tree.SetValByKey(ProjectTreeAttr.EarlyStart,formatter.format(flow.getStartNode().GetValDateTime(NodeAttr.EarlyStart).getTime()-week));
                tree.SetValByKey(ProjectTreeAttr.LateFinish,formatter.format(flow.getEndNode().GetValDateTime(NodeAttr.LaterFinish).getTime()+week));
                tree.Update();
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    private long initFlowNodeInfo(long start,String flowId) throws Exception{
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long dayTime= 24*60 * 60 * 1000L;
        Flow flow=new Flow(flowId);
        Node startNode=flow.getStartNode();
        startNode.SetValByKey(NodeAttr.EarlyStart,formatter.format(start));
        long totalTime=((int)Math.floor(Math.random()*3)+7)*dayTime;
        startNode.SetValByKey(NodeAttr.LaterFinish,formatter.format(start+totalTime+totalTime/2));
        startNode.SetValByKey(NodeAttr.Doc,totalTime/dayTime*8);
        startNode.Update();
        start=start+4*dayTime;

        Queue<Node> nextQueue=new ArrayDeque<>();
        nextQueue.addAll(startNode.getHisToNodes().toList());
        while (nextQueue.size()!=0){
            Node temp=nextQueue.poll();
            temp.SetValByKey(NodeAttr.EarlyStart,formatter.format(start));
            totalTime=((int)Math.floor(Math.random()*3)+7)*dayTime;
            temp.SetValByKey(NodeAttr.LaterFinish,formatter.format(start+totalTime+totalTime/2));
            temp.SetValByKey(NodeAttr.Doc,totalTime/dayTime*8);
            temp.Update();
            start=start+4*dayTime;
            nextQueue.addAll(temp.getHisToNodes().toList());
        }

        updateResourcePlan(flowId);
        return start;
    }


    //更新节点任务时间信息(模拟计划过程)
    @RequestMapping("updateNodeTask")
    public void updateNodeTask(String workGroupNo){
        NodeTasks tasks=new NodeTasks();
        try {
            ProjectTree tree=new ProjectTree(workGroupNo);
            tree.SetValByKey(ProjectTreeAttr.Status,5);//更新为已经计划
            tree.Update();
            //起始节点任务状态更新为可开始
            Flow flow=new Flow(tree.GetValStrByKey(ProjectTreeAttr.FlowNo));
            NodeTaskM NodeTaskCon = new NodeTaskM();
            NodeTaskCon.setWorkGroupId(tree.getNo() + "");
            NodeTaskCon.setNodeId(flow.getStartNodeID() + "");
            List<NodeTaskM> list = nodeTaskService.findNodeTaskList(NodeTaskCon);
            if (list != null && list.size() == 1) {
                NodeTaskM startTask = list.get(0);
                nodeTaskService.startNodeTask(startTask,null);
            }
            tasks.Retrieve(NodeTaskAttr.WorkGroupId, workGroupNo);
            List<NodeTask> taskList=tasks.toList();
            for (NodeTask task:taskList){
                Node node=new Node(task.getNodeId());
                String start=node.GetValStringByKey(NodeAttr.EarlyStart);
                String end=node.GetValStringByKey(NodeAttr.LaterFinish);
                task.SetValByKey(NodeTaskAttr.PlanStartTime,start);
                task.SetValByKey(NodeTaskAttr.PlanEndTime,end);
                task.SetValByKey(NodeTaskAttr.StartTime,start);
                task.SetValByKey(NodeTaskAttr.EndTime,end);
                task.Update();
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }

    }

    //仿真完成某个流程中所有节点任务
    @RequestMapping("sentNodeTask")
    public void sentNodeTask(String workNo){
        NodeTaskM con=new NodeTaskM();
        con.setWorkId(workNo);
        con.setIsReady(1);
        List<NodeTaskM> taskMList=nodeTaskService.findNodeTaskList(con);
        Queue<NodeTaskM> queue=new ArrayDeque();
        queue.addAll(taskMList);
        try {
            while (queue.size() != 0) {
                NodeTaskM task = queue.poll();
                nodeTaskService.finishNodeTask(task);
                taskMList=nodeTaskService.findNodeTaskList(con);
                queue.addAll(taskMList);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    //纠正资源需求中资源数量错误（设备和人力数量为0，环境知识为1）
    @RequestMapping("initResourceTaskInfo")
    public void initResourceTaskInfo(){
        try {
            ResourceTasks tasks=new ResourceTasks();
            tasks.RetrieveAll();
            List<ResourceTask> taskList=tasks.toList();
            for(ResourceTask task:taskList){
                String resourceNo=task.GetValStrByKey(ResourceTaskAttr.ResourceNo);
                BP.Resource.Resource resource=new BP.Resource.Resource(resourceNo);
                int type=resource.GetValIntByKey(ResourceAttr.Kind);
                //环境或者知识资源需要设置数量，设备人员数量为0
                if (type==0||type==1){
                    task.SetValByKey(ResourceTaskAttr.UseNum,0);
                }else {
                    task.SetValByKey(ResourceTaskAttr.UseNum,1);
                }
                task.Update();
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }

    }

    @RequestMapping("initFlowAbstract")
    public void initFlowAbstract(){
        try {
           /* FlowExts flows = new FlowExts();
            flows.RetrieveAll();
            List<FlowExt> flowList=flows.toList();
            for (FlowExt flow:flowList){
                StringBuilder sb=new StringBuilder();
                Nodes nodes=new Nodes();
                nodes.Retrieve(NodeAttr.FK_Flow,flow.getNo());
                for (int i=0;i<nodes.size();i++){
                    Node node=(Node) nodes.get(i);
                    sb.append(node.getName());
                    sb.append(",");
                }
                flow.SetValByKey(FlowAttr.Note,sb.toString());
                flow.Update();
            }*/
            FlowGeners geners=new FlowGeners();
            geners.RetrieveAll();
            List<FlowGener> list=geners.toList();
            for (FlowGener gener:list){
                String flowNo=gener.GetValStrByKey(FlowGenerAttr.FlowId);
                Flow flow=new Flow(flowNo);
                gener.SetValByKey(FlowGenerAttr.FlowName,flow.getName());
                gener.Update();
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }

    }


}
