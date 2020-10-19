package BP.springCloud.controller;

import BP.Resource.*;
import BP.Task.NodeTask;
import BP.Task.NodeTaskAttr;
import BP.Task.NodeTaskService;
import BP.Task.NodeTasks;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Template.NodeAttr;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        try {
            String[] resList={"R_adams_1","S_LX_16T_1","P_L1_admin","S_R_A","S_J5_A1","HJ_RM_C"};
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<Node> nodeList=new Nodes(flow).toList();
            int count=0;
            for (Node node:nodeList){
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
                    int duringTime=Integer.valueOf(node.getDoc());
                    String end=formatter.format(new Date(start.getTime()+duringTime*3*60*60*1000));
                    resTask.SetValByKey(ResourceTaskAttr.BookEnd, end);
                    resTask.SetValByKey(ResourceTaskAttr.StartTime, startStr);
                    resTask.SetValByKey(ResourceTaskAttr.EndTime, end);
                    resTask.SetValByKey(ResourceTaskAttr.PlanStart, startStr);
                    resTask.SetValByKey(ResourceTaskAttr.PlanEnd, end);
                    resTask.SetValByKey(ResourceTaskAttr.UseTime, node.getDoc());
                    resTask.SetValByKey(ResourceTaskAttr.PlanId, plan.getNo());
                    resTask.SetValByKey(ResourceTaskAttr.IsPlan, 1);
                    resTask.SetValByKey(ResourceTaskAttr.IsFinish, 2);
                    resTask.Insert();
                    count++;
                }

            }

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
    *@Description: 随机设置节点的时间属性（最早开始、最晚结束、工时）
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/10/16
    */
    @RequestMapping("initNodeInfo")
    public JSONObject initNodeInfo(@RequestBody String flowId){
        try {
            String startTime="2020-01-01 00:00:00";
            String endTime="2021-12-30 00:00:00";
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start=formatter.parse(startTime);
            Date end=formatter.parse(endTime);
            int hourTime= 60 * 60 * 1000;


            Flow flow=new Flow(flowId);
            Node startNode=flow.getStartNode();

            List<Node> nextList=startNode.getHisToNodes().toList();

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }
    //更新节点任务时间信息
    @RequestMapping("updateNodeTask")
    public void updateNodeTask(String workGroupNo){
        NodeTasks tasks=new NodeTasks();
        try {
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

}
