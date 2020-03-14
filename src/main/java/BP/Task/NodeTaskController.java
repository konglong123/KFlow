package BP.Task;

import BP.Web.WebUser;
import BP.springCloud.entity.NodeTaskM;
import BP.springCloud.tool.PageTool;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-03-04 18:06
 **/
@Controller()
@RequestMapping("nodeTask")
public class NodeTaskController {

    private final Logger logger = LoggerFactory.getLogger(NodeTaskController.class);

    @Resource
    private NodeTaskService nodeTaskService;
    /**
    *@Description: 查询节点任务列表（条件查询）
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/3/4
    */
    @RequestMapping("/getNodeTasks")
    public void getNodeTaskList(HttpServletRequest request, HttpServletResponse response){
        String isReady=request.getParameter("isReady");
        NodeTasks nodeTasks=new NodeTasks();
        try {
            String executor= WebUser.getNo();
            if (isReady!=null&&!isReady.equals(""))
                nodeTasks.Retrieve(NodeTaskAttr.Executor, executor,NodeTaskAttr.IsReady,isReady);
            else
                nodeTasks.Retrieve(NodeTaskAttr.Executor, executor);
            //更新任务状态
            List<NodeTask> nodeTaskList=nodeTasks.toList();
            for (NodeTask nt:nodeTaskList){
                int isReadyNt=nt.GetValIntByKey(NodeTaskAttr.IsReady);
                nt.SetValByKey(NodeTaskAttr.Status,isReadyNt);
                if (isReadyNt==1) {//可以开始状态下检查(计划完成后，)
                    //判断是否逾期
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date planStart = sdf.parse(nt.GetValStringByKey(NodeTaskAttr.PlanStartTime));
                    Date planEnd = sdf.parse(nt.GetValStringByKey(NodeTaskAttr.PlanEndTime));
                    Date current = new Date();
                    Calendar calendar = Calendar.getInstance();
                    int rat = 1000 * 60 * 60 * 24;
                    int dayNumS = (int) ((planStart.getTime() - current.getTime()) / rat);
                    if (dayNumS < 0)
                        nt.SetValByKey(NodeTaskAttr.Status, 4);//逾期开始
                    else if (dayNumS < 3)
                        nt.SetValByKey(NodeTaskAttr.Status, 5);//三天内警告
                    else {
                        int dayNumE = (int) ((planEnd.getTime() - current.getTime()) / rat);
                        if (dayNumE < 0)
                            nt.SetValByKey(NodeTaskAttr.Status, 7);//逾期结束
                        else if (dayNumE < 3)
                            nt.SetValByKey(NodeTaskAttr.Status, 8);//警告结束
                        else
                            nt.SetValByKey(NodeTaskAttr.Status, 6);//正常
                    }
                }
            }
            PageTool.TransToResult(nodeTasks,request,response);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    /**
    *@Description: 节点任务状态变更时，更新节点状态（此时只支持普通节点间的状态流转） 
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/3/9 
    */
    @RequestMapping("sendToNextNodes")
    public void setToNextNodes(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String  toNodeID = request.getParameter("toNodeId");
        String fromNodeId=request.getParameter("fromNodeId");
        String flowId=request.getParameter("flowId");
        String workId=request.getParameter("workId");
        NodeTaskM nodeTaskM=new NodeTaskM();
        nodeTaskM.setFlowId(flowId);
        nodeTaskM.setFlowTaskId(workId);
        Date date=new Date();

        //更新节点任务为已完成节点
        nodeTaskM.setNodeId(fromNodeId);
        List<NodeTaskM> nodeTaskList=nodeTaskService.findNodeTaskList(nodeTaskM);
        if (nodeTaskList==null||nodeTaskList.size()!=1){
            throw new Exception("modeTask:唯一性有问题");
        }else {
            NodeTaskM temp=nodeTaskList.get(0);
            temp.setIsReady(3);//已完成
            temp.setEndTime(date);
            nodeTaskService.updateNodeTask(temp);
        }
        
        //更新下一节点任务为已经开始
        nodeTaskM.setNodeId(toNodeID);
        nodeTaskList=nodeTaskService.findNodeTaskList(nodeTaskM);
        if (nodeTaskList==null||nodeTaskList.size()!=1){
            throw new Exception("modeTask:唯一性有问题");
        }else {
            NodeTaskM temp=nodeTaskList.get(0);
            temp.setIsReady(1);//可以开始
            temp.setStartTime(date);
            nodeTaskService.updateNodeTask(temp);
        }

    }

    @RequestMapping("/getNodeTaskByNo")
    @ResponseBody
    public Object getNodeTask(String no){
        NodeTask nodeTask=new NodeTask();
        nodeTask.setNo(no);
        try {
            nodeTask.RetrieveByNo();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return nodeTask.ToJson();
    }

    @RequestMapping("/startNodeTask")
    public void startNodeTask(Long no){
        try {
            NodeTaskM nodeTaskM=new NodeTaskM();
            nodeTaskM.setNo(no);
            nodeTaskM.setIsReady(2);//已经开始
            nodeTaskService.updateNodeTask(nodeTaskM);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }



}
