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
            NodeTaskM nodeTaskM=nodeTaskService.getNodeTaskById(no);
            if (nodeTaskM.getIsReady()==1){
                nodeTaskM.setStartTime(new Date());
                nodeTaskM.setIsReady(2);
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
    public JSONObject updateTasksStatus(){
        NodeTaskM con=new NodeTaskM();
        List<NodeTaskM> nodeTaskMList=nodeTaskService.findNodeTaskList(con);
        for (NodeTaskM temp:nodeTaskMList){
            int status=nodeTaskService.getTaskStatus(temp);
            temp.setStatus(status);
            nodeTaskService.updateNodeTask(temp);
        }
        return null;
    }



}
