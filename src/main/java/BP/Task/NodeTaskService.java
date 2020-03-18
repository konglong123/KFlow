package BP.Task;

import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.Template.Direction;
import BP.WF.Template.DirectionAttr;
import BP.WF.Template.Directions;
import BP.springCloud.entity.GenerFlow;
import BP.springCloud.entity.NodeTaskM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-03-04 18:11
 **/
@Service
public class NodeTaskService {
    private  final Logger logger = LoggerFactory.getLogger(NodeTaskService.class);

    @Resource
    private NodeTaskManage nodeTaskManage;

    @Resource
    private GenerFlowManager generFlowManager;

    public NodeTaskM getNodeTaskById(Long no){
        return nodeTaskManage.getNodeTaskById(no);
    }

    public List findNodeTaskList(NodeTaskM nodeTaskM){
        try {
            return nodeTaskManage.findNodeTaskList(nodeTaskM);
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    public Long updateNodeTask(NodeTaskM nodeTaskM){
        return nodeTaskManage.updateNodeTask(nodeTaskM);
    }

    
    /**
    *@Description: 完成节点任务，并激活下一节点任务
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/3/17 
    */
    public  String finishNodeTask(NodeTaskM currentTask) throws Exception{

        //结束节点任务
        currentTask.setIsReady(3);
        //设置节点任务为删除状态（）
        currentTask.setYn(1);
        currentTask.setEndTime(new Date());
        nodeTaskManage.updateNodeTask(currentTask);

        //开启next节点任务
        List<NodeTaskM> nextTasks=getCanStartNodeTask(currentTask);
        if (nextTasks==null||nextTasks.size()==0){
            //该部分流程已经没有后续节点，结束流程（结束子流程、父流程）
            finishWork(currentTask);
        }else {
            for (NodeTaskM next:nextTasks){
                startNodeTask(next);
            }
        }
        return null;
    }

    /**
    *@Description: 结束当前节点所在流程
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/3/17
    */

    public  boolean finishWork(NodeTaskM currentTask){

        NodeTaskM con=new NodeTaskM();
        con.setFlowTaskId(currentTask.getFlowTaskId());
        List list=nodeTaskManage.findNodeTaskList(con);

        if (list==null||list.size()==0){
            //该work下没有未完成任务，结束该work
            String workId=currentTask.getFlowTaskId();
            GenerFlow currentWork=generFlowManager.getGenerFlowById(Long.parseLong(workId));
            currentWork.setYn(1);
            currentWork.setStatus(2);
            currentWork.setFinishTime(new Date());
            generFlowManager.updateGenerFlow(currentWork);
            return true;
        }

        return false;
    }

    public List getCanStartNodeTask(NodeTaskM nodeTaskM){

        return getAfterNodeTask(nodeTaskM);
    }

    /**
     *@Description: 激活节点任务，同时需要激活该节点中，子流程任务（循环激活，直到激活所有子流程）
     *@Param:
     *@return:
     *@Author: Mr.kong
     *@Date: 2020/3/17
     */
    public boolean startNodeTask(NodeTaskM nodeTaskM){
        //更新任务状态
        nodeTaskM.setIsReady(1);//可以开始
        nodeTaskManage.updateNodeTask(nodeTaskM);

        //开启子流程任务
        NodeTaskM subStartNodeTask=getSubFlowStartNodeTask(nodeTaskM);
        if (subStartNodeTask!=null)
            this.startNodeTask(subStartNodeTask);

        return true;
    }

    public NodeTaskM getSubFlowStartNodeTask(NodeTaskM nodeTaskM){

        //获取子流程的起始节点任务
        NodeTaskM con=new NodeTaskM();
        con.setParentNodeTask(nodeTaskM.getNo()+"");
        List<NodeTaskM> subNodeTasks=nodeTaskManage.findNodeTaskList(con);
        if (subNodeTasks==null||subNodeTasks.size()==0)
            return null;
        try {
            NodeTaskM subTask=subNodeTasks.get(0);
            String flowNo=subTask.getFlowId();
            Flow flow=new Flow(flowNo);
            int startNodeId=flow.getStartNodeID();

            con.setNodeId(startNodeId+"");
            List<NodeTaskM> subStartNodeTasks=nodeTaskManage.findNodeTaskList(con);
            if (subStartNodeTasks==null)
                return null;
            return subStartNodeTasks.get(0);

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }
    /**
    *@Description: 开始节点任务前检查（检查所有前置节点是否完成，所有子流程能否启动） 
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/3/17 
    */
    public boolean beforeStartNodeTask(NodeTaskM nodeTaskM){
        return true;
    }
    
    /**
    *@Description: 获取后置节点任务 
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/3/17 
    */
    public List getAfterNodeTask(NodeTaskM nodeTaskM){
        try {
            Directions directions=new Directions();
            
            directions.Retrieve(DirectionAttr.Node,nodeTaskM.getNodeId());
            
            List<Direction> directionList=directions.toList();
            List<String> nodeIds=new ArrayList<>(directionList.size());
            for (Direction dir:directionList){
                nodeIds.add(dir.getToNode()+"");
            }
            return nodeTaskManage.getNodeTaskByNodeIds(nodeIds);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }
    
    /**
    *@Description: 获取前置节点任务 
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/3/17 
    */
    public List getPreNodeTask(NodeTaskM nodeTaskM){
        try {
            Directions directions=new Directions();
            
            directions.Retrieve(DirectionAttr.ToNode,nodeTaskM.getNodeId());
            
            List<Direction> directionList=directions.toList();
            List<String> nodeIds=new ArrayList<>(directionList.size());
            for (Direction dir:directionList){
                nodeIds.add(dir.getToNode()+"");
            }
            return nodeTaskManage.getNodeTaskByNodeIds(nodeIds);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;}
    

}
