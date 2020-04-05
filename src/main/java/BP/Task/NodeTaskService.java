package BP.Task;

import BP.Tools.StringUtils;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.Template.Direction;
import BP.WF.Template.DirectionAttr;
import BP.WF.Template.Directions;
import BP.WF.Template.FrmSubFlow;
import BP.springCloud.entity.GenerFlow;
import BP.springCloud.entity.NodeTaskM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    *@Description:  完成节点任务之前检查（所有节点子流程是否完成）
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/4/2 
    */
    public boolean beforeFinishNodeTask(NodeTaskM nodeTaskM){
        NodeTaskM con=new NodeTaskM();
        con.setParentNodeTask(nodeTaskM.getNo()+"");
        List childList=nodeTaskManage.findNodeTaskList(con);
        //所有子节点任务都已经完成
        if (childList==null||childList.size()==0)
            return true;
        else
            return false;
    }
    
    /**
    *@Description: 完成节点任务，并激活下一节点任务
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/3/17 
    */
    public  String finishNodeTask(NodeTaskM currentTask) throws Exception{

        if (!beforeFinishNodeTask(currentTask))
            return "当前节点任务不满足发送要求！";

        //结束节点任务
        currentTask.setIsReady(3);
        currentTask.setStatus(3);
        //设置节点任务为删除状态（）
        currentTask.setYn(1);
        currentTask.setEndTime(new Date());
        nodeTaskManage.updateNodeTask(currentTask);

        //开启next节点任务
        List<NodeTaskM> nextTasks=getCanStartNodeTask(currentTask);
        boolean flag=true;
        if (nextTasks==null||nextTasks.size()==0){
            //该部分流程已经没有后续节点，结束流程（结束子流程、父流程）
            finishWork(currentTask);
        }else {
            for (NodeTaskM next:nextTasks){
                flag=flag&startNodeTask(next);
            }
        }
        if (flag)
            return "发送成功，后续节点任务已经启动！";
        else 
            return "发送成功，后续节点任务未启动！";
    }

    /**
    *@Description: 结束当前节点所在流程
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/3/17
    */

    public  boolean finishWork(NodeTaskM currentTask){
        String workId=currentTask.getWorkId();

        NodeTaskM con=new NodeTaskM();
        con.setWorkId(workId);
        List list=nodeTaskManage.findNodeTaskList(con);

        if (list==null||list.size()==0){
            //该work下没有未完成任务，结束该流程,
            GenerFlow currentWork=generFlowManager.getGenerFlowById(Long.parseLong(workId));
            currentWork.setYn(1);
            currentWork.setStatus(2);
            currentWork.setFinishTime(new Date());
            generFlowManager.updateGenerFlow(currentWork);
        }
        return true;
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
        //启动节点任务前，判断能否启动（所有前置节点任务是否均完成）
        if (!beforeStartNodeTask(nodeTaskM))
            return false;
        
        //更新任务状态
        nodeTaskM.setIsReady(1);//可以开始
        nodeTaskM.setStatus(getTaskStatus(nodeTaskM));
        if (StringUtils.isEmpty(nodeTaskM.getExecutor())){
            //通过人员选择器进行选择执行人。此时人为指定
            nodeTaskM.setExecutor("admin");
        }
        nodeTaskManage.updateNodeTask(nodeTaskM);

        //开启子流程任务
        List<NodeTaskM> subStartTasks=getSubFlowStartNodeTask(nodeTaskM);
        if (subStartTasks!=null) {
            for (NodeTaskM sub:subStartTasks) {
                this.startNodeTask(sub);
            }
        }
        return true;
    }

    /**
    *@Description: 获取所有子流程的开始节点任务
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/3
    */
    public List<NodeTaskM> getSubFlowStartNodeTask(NodeTaskM nodeTaskM){

        try {
            FrmSubFlow subFlow=new FrmSubFlow(Integer.valueOf(nodeTaskM.getNodeId()));
            String childFlow=subFlow.getSFDefInfo();
            if (childFlow==null||childFlow.equals("")){
                return null;
            }
            String[] childFlows=childFlow.split("%");
            List<String> childs=new ArrayList<>(childFlows.length);
            for (String childFlowNo:childFlows){
                Flow flow=new Flow(childFlowNo);
                int startNodeID=flow.getStartNodeID();
                childs.add(startNodeID+"");
            }
            return nodeTaskManage.getNodeTaskByNodeIdsAndParentTaskId(nodeTaskM.getNo()+"",childs);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }
    /**
    *@Description: 开始节点任务前检查（检查所有前置节点是否完成，所有子流程能否启动）
     * 后续还需要增加对相应资源的判断（是否已经齐套）
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/3/17 
    */
    public boolean beforeStartNodeTask(NodeTaskM nodeTaskM){
        List<NodeTaskM> preList=getPreNodeTask(nodeTaskM);
        if (preList!=null) {
            for (NodeTaskM pre : preList) {
                if (pre.getIsReady() != 3) {//前置节点任务存在未完成项，不允许启动该节点任务
                    return false;
                }
            }
        }
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
            return nodeTaskManage.getNodeTaskByNodeIdsAndParentTaskId(nodeTaskM.getParentNodeTask(),nodeIds);
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
                nodeIds.add(dir.getNode()+"");
            }
            return nodeTaskManage.getNodeTaskByNodeIdsAndParentTaskId(nodeTaskM.getParentNodeTask(),nodeIds);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }


    /**
    *@Description: 更新节点状态
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/2
    */
    public int getTaskStatus(NodeTaskM nt){
        int isReadyNt=nt.getIsReady();
        if (isReadyNt!=0&&isReadyNt!=3) {//可以开始并且未完成状态下检查(计划完成后，)
                    //判断是否逾期
            Date planStart = nt.getPlanStartTime();
            Date planEnd = nt.getPlanEndTime();
            Date current = new Date();
            Calendar calendar = Calendar.getInstance();
            int rat = 1000 * 60 * 60 * 24;
            int dayNumS = (int) ((planStart.getTime() - current.getTime()) / rat);
            if (dayNumS < 0)
                return 4;//逾期开始
            else if (dayNumS < 3)
                return 5;//三天内警告
            else {
                int dayNumE = (int) ((planEnd.getTime() - current.getTime()) / rat);
                if (dayNumE < 0)
                    return 7;//逾期结束
                else if (dayNumE < 3)
                    return 8;//警告结束
                else
                    return 6;//正常
            }
        }
        return nt.getIsReady();
    }
    

    public Long insertNodeTask(NodeTaskM nodeTaskM){
        return nodeTaskManage.insertNodeTask(nodeTaskM);
    }
}
