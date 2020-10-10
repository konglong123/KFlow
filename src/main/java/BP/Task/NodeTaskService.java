package BP.Task;

import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.NodeGroup.*;
import BP.Resource.*;
import BP.WF.Nodes;
import BP.springCloud.dao.ResourceTaskMDao;
import BP.springCloud.entity.JudgeRoute;
import BP.Judge.JudgeRouteManager;
import BP.Judge.JudgeTool;
import BP.Port.Emp;
import BP.Port.EmpAttr;
import BP.Port.Emps;
import BP.Tools.StringUtils;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.Template.*;
import BP.Web.WebUser;
import BP.springCloud.entity.GenerFlow;
import BP.springCloud.entity.NodeTaskM;
import BP.springCloud.entity.ResourceTaskM;
import BP.springCloud.tool.FeignTool;
import com.google.common.collect.Lists;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

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

    @Resource
    private JudgeRouteManager judgeRouteManager;


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

    public List findNodeTaskAllList(NodeTaskM nodeTaskM){
        try {
            return nodeTaskManage.findNodeTaskAllList(nodeTaskM);
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
            //更新GenerFlow状态
            GenerFlow generFlow=generFlowManager.getGenerFlowById(Long.parseLong(currentTask.getWorkId()));
            String activatedNodes=generFlow.getActivatedNodes();
            activatedNodes=activatedNodes.replace(currentTask.getNodeId()+",","");
            for (NodeTaskM next:nextTasks){
                flag=flag&startNodeTask(next);
                activatedNodes+=next.getNodeId()+",";
            }
            generFlow.setActivatedNodes(activatedNodes);
            generFlowManager.updateGenerFlow(generFlow);
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
        List<NodeTaskM> list=nodeTaskManage.findNodeTaskList(con);
        if (list==null||list.size()==0){
            //该work下没有未完成任务，结束该流程,
            GenerFlow currentWork=generFlowManager.getGenerFlowById(Long.parseLong(workId));
            currentWork.setYn(1);
            currentWork.setStatus(2);
            currentWork.setFinishTime(new Date());
            currentWork.setActivatedNodes("");
            currentWork.setUseTime(currentWork.getUseTime()+currentTask.getUseTime());
            generFlowManager.updateGenerFlow(currentWork);
        }
        return true;
    }

    /**
    *@Description: 决策节点根据条件选择下传任务
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/25
    */
    public List getCanStartNodeTask(NodeTaskM nodeTaskM){
        try{
            Node node=new Node(nodeTaskM.getNodeId());
            int runModel=node.GetValIntByKey(NodeAttr.RunModel);
            switch (runModel){
                case 9:
                    //如果存在judgeNodeId=当前节点，则说明该决策节点是匹配前置决策节点，不增加JudgeRoute信息
                    JudgeRoute con=new JudgeRoute();
                    con.setWorkId(nodeTaskM.getWorkId());
                    con.setJudgeNodeId(nodeTaskM.getNodeId());
                    List list=judgeRouteManager.findJudgeRouteList(con);
                    if (list.size()!=0){//走正常路线，获取后置连线节点
                        break;
                    }

                    List<String> nextNodes=JudgeTool.judge(nodeTaskM);
                    //记录分支信息（决策多分支，合并时使用）
                    JudgeRoute route=new JudgeRoute();
                    route.setNum(nextNodes.size());
                    route.setWorkId(nodeTaskM.getWorkId());
                    route.setJudgeNodeId(node.getJudgeNodeId()+"");
                    route.setRoutes(nextNodes.toString());
                    judgeRouteManager.insertJudgeRoute(route);

                    return nodeTaskManage.getNodeTaskByNodeIdsAndParentTaskId(nodeTaskM.getParentNodeTask(),nextNodes);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }

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

            // 计划时，如果没有指定负责人，则通过人员选择器进行选择执行人。
            nodeTaskM.setExecutor(getExecutor(nodeTaskM));
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
            String[] childFlows=childFlow.split("/");
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
        try{
            Node node=new Node(nodeTaskM.getNodeId());
            switch (node.getHisRunModel()){
                case Judge://检查是否有与该决策节点对应的前置决策节点，存在时，前置决策节点的所有分支运行到该处，才允许启动
                    JudgeRoute con=new JudgeRoute();
                    con.setWorkId(nodeTaskM.getWorkId());
                    con.setJudgeNodeId(nodeTaskM.getNodeId());
                    List<JudgeRoute> judgeRouteList=judgeRouteManager.findJudgeRouteList(con);
                    if (judgeRouteList.size()>0){
                        JudgeRoute route=judgeRouteList.get(0);
                        route.setArriveNum(route.getArriveNum()+1);
                        judgeRouteManager.updateJudgeRoute(route);
                        if (route.getArriveNum()==route.getNum()) //所有分支到齐
                            return true;
                        else
                            return false;
                    }

                default:
                    List<NodeTaskM> preList=getPreNodeTask(nodeTaskM);
                    if (preList!=null) {
                        for (NodeTaskM pre : preList) {
                            if (pre.getIsReady() != 3) {//前置节点任务存在未完成项，不允许启动该节点任务
                                return false;
                            }
                        }
                    }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
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
        return new ArrayList();
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
        if (isReadyNt==9||isReadyNt==1||isReadyNt==2) {//可以开始并且未完成状态下检查(计划完成后，)
                    //判断是否逾期
            Date planStart = nt.getPlanStartTime();
            Date planEnd = nt.getPlanEndTime();
            Date current = new Date();
            int rat = 1000 * 60 * 60 * 24;

            if (isReadyNt==1) {//未开始，
                int dayNumS = (int) ((planStart.getTime() - current.getTime()) / rat);
                if (dayNumS < 0)
                    return 4;//逾期开始
                else if (dayNumS < 3)
                    return 5;//三天内警告
            } else {//已经开始
                int dayNumE = (int) ((planEnd.getTime() - current.getTime()) / rat);
                if (dayNumE < 0)
                    return 7;//逾期结束
                else if (dayNumE < 3)
                    return 8;//警告结束
            }
            return 6;//正常
        }
        return nt.getIsReady();
    }
    

    public Long insertNodeTask(NodeTaskM nodeTaskM){

        return nodeTaskManage.insertNodeTask(nodeTaskM);
    }

    /**
    *@Description: 获取GenerFlow下层次不超过depth深度的所有节点任务进度
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/6
    */
    public List<JSONObject> getGantData(GenerFlow generFlow, int depth){
        List<JSONObject> data=new LinkedList<>();

        JSONObject temp=new JSONObject();
        temp.put("name","实例"+generFlow.getNo());
        temp.put("id",generFlow.getNo()+"");
        temp.put("owner",generFlow.getCreator());
        data.add(temp);

        NodeTaskM con=new NodeTaskM();
        con.setWorkId(generFlow.getWorkId()+"");
        List<NodeTaskM> list=nodeTaskManage.findNodeTaskAllList(con);
        transToGant(list,generFlow.getNo()+"",data,depth,0);

        return data;
    }


    public void transToGant(List<NodeTaskM> children,String parentId,List<JSONObject> data,int depth,int curDepth){

        if (children==null)
            return;

        NodeTaskM con=new NodeTaskM();

        for (NodeTaskM nodeTaskM:children){
            JSONObject point=new JSONObject();
            String id=nodeTaskM.getNo()+"-"+nodeTaskM.getNodeName();
            point.put("name",id);
            point.put("id",id);

            //获取前置节点任务
            List<NodeTaskM> preNodeTasks=getPreNodeTask(nodeTaskM);
            if (preNodeTasks!=null){
                List<String> dependencyList=new ArrayList<>(preNodeTasks.size());
                for (NodeTaskM pre:preNodeTasks){
                    dependencyList.add(pre.getNo()+"-"+pre.getNodeName());
                }
                point.put("dependency",dependencyList);
            }

            int shiCha=8 * 60 * 60 * 1000;//时间戳会存在时差问题

            int isReady=nodeTaskM.getIsReady();
            if (isReady==3||isReady==2)//已经开始
                point.put("start",nodeTaskM.getStartTime().getTime()+shiCha);
            else
                point.put("start",nodeTaskM.getPlanStartTime().getTime()+shiCha);

            if (nodeTaskM.getIsReady()==3)//已经结束
                point.put("end",nodeTaskM.getEndTime().getTime()+shiCha);
            else
                point.put("end",nodeTaskM.getPlanEndTime().getTime()+shiCha);

            JSONObject completed=new JSONObject();

            Double amount = (nodeTaskM.getUseTime()+0.0)/nodeTaskM.getTotalTime();
            if (nodeTaskM.getIsReady()==3)//已经完成时，不按用时计算
                amount=1.0;
            BigDecimal bd = new BigDecimal(amount);
            amount = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            completed.put("amount",amount);
            point.put("completed",completed);
            point.put("parent",parentId);
            point.put("owner",nodeTaskM.getExecutor());

            data.add(point);

            //深度满足，添加子流程信息
            if (curDepth<depth){
                //添加子流程任务信息
                con.setParentNodeTask(nodeTaskM.getNo()+"");
                List<NodeTaskM> nextChildren=nodeTaskManage.findNodeTaskAllList(con);
                transToGant(nextChildren,id,data,depth,curDepth+1);
            }

        }
    }

    public String getExecutor(NodeTaskM nodeTaskM){
        String nodeId=nodeTaskM.getNodeId();
        String executor=null;

        List<String> employeeNos=new ArrayList<>();//所有可选人员
        try {
            executor= WebUser.getNo();//默认自身
            Node node = new Node(nodeId);
            DataTable dt;
            switch (node.getHisDeliveryWay()) {
                case NoSelect:
                    break;
                case ByStation:
                    String sql = "SELECT No, Name FROM Port_Emp WHERE No IN (SELECT A.FK_Emp FROM " + BP.WF.Glo.getEmpStation() + " A, WF_NodeStation B WHERE A.FK_Station=B.FK_Station AND B.FK_Node=" + nodeId + ")";
                    dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
                    if (dt.Rows.size()!=0) {
                        for (DataRow dr : dt.Rows) {
                            employeeNos.add(dr.getValue(0).toString());
                        }
                    }
                    break;
                case ByDept:
                    NodeDepts nds=new NodeDepts();
                    nds.Retrieve(NodeEmpAttr.FK_Node, nodeId);
                    for (NodeDept nodeDept:nds.ToJavaList()){
                        Emps emps=new Emps();
                        emps.Retrieve(EmpAttr.FK_Dept,nodeDept.getFK_Dept());
                        for (Emp emp:emps.ToJavaList()){
                            employeeNos.add(emp.getNo());
                        }
                    }
                    break;
                case ByBindEmp:
                    NodeEmps nes = new NodeEmps();
                    nes.Retrieve(NodeEmpAttr.FK_Node, nodeId);
                    for (NodeEmp ne : nes.ToJavaList())
                        employeeNos.add(ne.getFK_Emp());
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }

        //此处后续可以增加调度算法进行人员选择（）此时返回一个随机人员
        Random random=new Random();
        if (employeeNos.size()>0) {
            int pos = random.nextInt(employeeNos.size());
            executor=employeeNos.get(pos);
        }
        return executor;
    }

    /**
    *@Description: 构建节点任务之间的拓扑关系
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/20
    */
    public boolean updateNodeTaskPreAfter(String startNodeId,Long workId){
        NodeTaskM con=new NodeTaskM();
        con.setWorkId(workId+"");
        //获取当前流程的所有节点任务
        List<NodeTaskM> nodeTaskMAllTask=findNodeTaskList(con);
        Map<String,NodeTaskM> map=new HashMap<>();
        for (NodeTaskM temp:nodeTaskMAllTask)
            map.put(temp.getNodeId(),temp);

        //遍历更新所有节点任务
        for (String nodeId:map.keySet()){
            NodeTaskM nodeTaskM=map.get(nodeId);
            String pre="";
            List<NodeTaskM> befores=getPreNodeTask(nodeTaskM);
            if (befores!=null){
                for (NodeTaskM before:befores){
                    pre=pre+before.getNo()+",";
                }
            }
            nodeTaskM.setPreNodeTask(pre);

            String after="";
            List<NodeTaskM> nexts=getAfterNodeTask(nodeTaskM);
            if (nexts!=null){
                for (NodeTaskM next:nexts)
                    after=after+next.getNo()+",";
            }
            nodeTaskM.setNextNodeTask(after);

            nodeTaskManage.updateNodeTask(nodeTaskM);
        }

        return true;
    }

    public List<JSONObject> getMyTaskGant(NodeTaskM con){

        try {
            List<NodeTaskM> list=nodeTaskManage.findNodeTaskList(con);
            List<JSONObject> data=new ArrayList<>();
            for (NodeTaskM nodeTaskM:list){
                JSONObject point=new JSONObject();
                String id=nodeTaskM.getNo()+"-"+nodeTaskM.getNodeName();
                point.put("name",id);
                point.put("id",id);

                int shiCha=8 * 60 * 60 * 1000;//时间戳会存在时差问题

                int isReady=nodeTaskM.getIsReady();
                if (isReady==3||isReady==2)//已经开始
                    point.put("start",nodeTaskM.getStartTime().getTime()+shiCha);
                else
                    point.put("start",nodeTaskM.getPlanStartTime().getTime()+shiCha);

                if (nodeTaskM.getIsReady()==3)//已经结束
                    point.put("end",nodeTaskM.getEndTime().getTime()+shiCha);
                else
                    point.put("end",nodeTaskM.getPlanEndTime().getTime()+shiCha);

                JSONObject completed=new JSONObject();

                Double amount = (nodeTaskM.getUseTime()+0.0)/nodeTaskM.getTotalTime();
                if (nodeTaskM.getIsReady()==3)//已经完成时，不按用时计算
                    amount=1.0;
                BigDecimal bd = new BigDecimal(amount);
                amount = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                completed.put("amount",amount);
                point.put("completed",completed);
                point.put("owner",nodeTaskM.getExecutor());

                data.add(point);
            }

            //按照开始时间进行排序
            data.sort(new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject o1, JSONObject o2) {
                    long start1=o1.getLong("start");
                    long start2=o2.getLong("start");
                    if (start1>start2)
                        return 1;
                    else if (start1<start2)
                        return -1;
                    return 0;
                }
            });
            return data;
        }catch (Exception e){
            logger.error(e.getMessage());
        }

        return new ArrayList<>();
    }

    /**
    *@Description: 整理tasks输出流程优化所需格式
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/9/8
    */
    public JSONObject getPlanData(List<NodeTaskM> taskMS){

        JSONObject data=new JSONObject();
        List<JSONObject> tasks=new ArrayList<>();
        for (NodeTaskM task:taskMS){
            JSONObject item=new JSONObject();
            item.put("taskUid",task.getNo());
            item.put("taskName",task.getNodeName());
            item.put("taskType",task.getTaskType());
            item.put("taskProjUid",task.getWorkGroupId());
            item.put("taskPriority",task.getTaskPriority());
            //转换成汉卿中默认单位（天，每天10小时）
            item.put("taskPlanDur",(task.getTotalTime()+0.0)/10);
            item.put("taskEarlyStartDateTime",task.getEarlyStartTime().getTime());
            item.put("taskLateFinishDateTime",task.getOldestFinishTime().getTime());
            item.put("taskWorkModel",task.getTaskWorkModel());
            tasks.add(item);
        }
        data.put("task",tasks);
        return data;
    }

    /**
     *@Description: 整理task间的连接关系，输出流程优化所需格式
     *@Param:
     *@return:
     *@Author: Mr.kong
     *@Date: 2020/9/8
     */
    public JSONObject getPlanLinkData(List<NodeTaskM> taskMS) throws Exception{
        JSONObject data=new JSONObject();
        List<JSONObject> linkList=new ArrayList<>();
        for (NodeTaskM task:taskMS){
            //普通连接
            List<NodeTaskM> after=getAfterNodeTask(task);
            for (NodeTaskM temp:after){
                JSONObject item=new JSONObject();
                item.put("taskLinkPreTaskUid",task.getNo());
                item.put("taskLinkSucTaskUid",temp.getNo());
                item.put("taskLinkType",0);
                linkList.add(item);
            }

           /* //真连接
            List<NodeTaskM> realAfter=getRealAfterTask(task);
            for (NodeTaskM temp:realAfter){
                JSONObject item=new JSONObject();
                item.put("taskLinkPreTaskUid",task.getNo());
                item.put("taskLinkSucTaskUid",temp.getNo());
                item.put("taskLinkType",1);
                linkList.add(item);
            }*/
           /* List<NodeTaskM> realBefore=getRealBeforeTask(task);
            for (NodeTaskM temp:realBefore){
                JSONObject item=new JSONObject();
                item.put("taskLinkPreTaskUid",temp.getNo());
                item.put("taskLinkSucTaskUid",task.getNo());
                item.put("taskLinkType",1);
                linkList.add(item);
            }*/

        }
        data.put("taskLink",linkList);
        return data;
    }

    public List<NodeTaskM> getRealAfterTask(NodeTaskM task) throws Exception{
        Node node=new Node(task.getNodeId());
        String realAfter=node.GetValStrByKey(NodeAttr.RealAfter);
        if (org.springframework.util.StringUtils.isEmpty(realAfter))
            return new ArrayList<>();
        List<String> nodeIds=new ArrayList<>();
        for (String nodeId:realAfter.split("/")){
            nodeIds.add(nodeId);
        }
        return nodeTaskManage.getNodeTaskByNodeIdsAndParentTaskId(task.getParentNodeTask(),nodeIds);
    }

    public List<NodeTaskM> getRealBeforeTask(NodeTaskM task) throws Exception{
        Node node=new Node(task.getNodeId());
        String realAfter=node.GetValStrByKey(NodeAttr.RealBefore);
        if (org.springframework.util.StringUtils.isEmpty(realAfter))
            return new ArrayList<>();
        List<String> nodeIds=new ArrayList<>();
        for (String nodeId:realAfter.split("/")){
            nodeIds.add(nodeId);
        }
        return nodeTaskManage.getNodeTaskByNodeIdsAndParentTaskId(task.getParentNodeTask(),nodeIds);
    }

    /**
    *@Description: 获取资源方案信息(认为资源方案是定义到资源实例)
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/9/8
    */
    public JSONObject getResourcePlanData(List<NodeTaskM> taskMList) throws Exception{
        JSONObject data=new JSONObject();
        List<JSONObject> resourcePlanList=new ArrayList<>();
        List<JSONObject> resourceNeedList=new ArrayList<>();
        Map<String, BP.Resource.Resource> resourceMap=new HashMap<>();

        for (NodeTaskM task:taskMList){
            ResourcePlans plans=new ResourcePlans();
            plans.Retrieve(ResourcePlanAttr.NodeId,task.getNodeId());
            List<ResourcePlan> tempList=plans.toList();
            for (ResourcePlan plan:tempList){
                JSONObject item=new JSONObject();
                item.put("resPlanUid",plan.getNo());
                item.put("resPlanTaskUid",task.getNo());
                item.put("resPlanPriority",1);//更改优先级
                resourcePlanList.add(item);

                ResourceTasks resourceTasks=new ResourceTasks();
                resourceTasks.Retrieve(ResourceTaskAttr.PlanId,plan.getNo());
                List<ResourceTask> resourceTaskList=resourceTasks.toList();
                for (ResourceTask temp:resourceTaskList){

                    String resourceNo=temp.GetValStrByKey(ResourceTaskAttr.ResourceNo);
                    BP.Resource.Resource resource=resourceMap.get(resourceNo);
                    if (resource==null) {
                        resource = new BP.Resource.Resource(resourceNo);
                        resourceMap.put(resourceNo,resource);
                    }

                    JSONObject resourceTaskItem=new JSONObject();
                    resourceTaskItem.put("resReqUid",temp.getNo());
                    resourceTaskItem.put("resReqResPlanUid",plan.getNo());
                    resourceTaskItem.put("resReqResType",resource.GetValIntByKey(ResourceAttr.Kind));
                    resourceTaskItem.put("resReqResUid",resource.getNo());
                    resourceTaskItem.put("resReqResWork",(temp.GetValIntByKey(ResourceTaskAttr.UseTime)+0.0)/10);
                    resourceTaskItem.put("resReqResAmount",temp.GetValIntByKey(ResourceTaskAttr.UseNum));
                    resourceTaskItem.put("resReqResWorkModel",task.getTaskWorkModel());
                    resourceNeedList.add(resourceTaskItem);
                }
            }
        }
        data.put("taskResPlan",resourcePlanList);
        data.put("taskResReq",resourceNeedList);

        //资源基本信息
        List<JSONObject> resourceHumanList=new ArrayList<>();
        List<JSONObject> resourceEquipmentList=new ArrayList<>();
        List<JSONObject> resourcePlaceList=new ArrayList<>();
        List<JSONObject> resourceKnowledgeList=new ArrayList<>();

        //资源已用
        List<JSONObject> resourceLoadList=new ArrayList<>();
        for (Map.Entry<String, BP.Resource.Resource> resourceEntry:resourceMap.entrySet()){
            ResourceTasks tasks=new ResourceTasks();
            tasks.Retrieve(ResourceTaskAttr.ResourceNo,resourceEntry.getKey(),ResourceTaskAttr.IsPlan,1);//已经计划的资源任务
            List<ResourceTask> list=tasks.toList();

            for (ResourceTask task:list){
                JSONObject item=new JSONObject();
                item.put("arUid",task.getNo());
                item.put("arResUid",task.GetValStrByKey(ResourceTaskAttr.ResourceNo));
                item.put("arResStartDateTime",task.GetValDateTime(ResourceTaskAttr.PlanStart).getTime());
                item.put("arResFinishDateTime",task.GetValDateTime(ResourceTaskAttr.PlanEnd).getTime());
                item.put("arResWork",(task.GetValIntByKey(ResourceTaskAttr.UseTime)+0.0)/10);
                item.put("arResAmount",task.GetValIntByKey(ResourceTaskAttr.UseNum));
                resourceLoadList.add(item);
            }

            BP.Resource.Resource resource=resourceEntry.getValue();
            int type=resource.GetValIntByKey(ResourceAttr.Kind);//0=人力@1=设备@2=环境@3=知识"
            JSONObject item=new JSONObject();
            switch (type){
                case 0:
                    item.put("humUid",resource.getNo());
                    resourceHumanList.add(item);
                    break;
                case 1:
                    item.put("equipUid",resource.getNo());
                    resourceEquipmentList.add(item);
                    break;
                case 2:
                    item.put("placeUid",resource.getNo());
                    item.put("placeArea",resource.GetValIntByKey(ResourceAttr.Num));
                    resourcePlaceList.add(item);
                    break;
                case 3:
                    item.put("knowlUid",resource.getNo());
                    item.put("knowlAmount",resource.GetValIntByKey(ResourceAttr.Num));
                    resourceKnowledgeList.add(item);
                    break;
            }

        }

        data.put("allocateResource",resourceLoadList);
        data.put("human",resourceHumanList);
        data.put("equipment",resourceEquipmentList);
        data.put("place",resourcePlaceList);
        data.put("knowledge",resourceKnowledgeList);
        return data;
    }


    /**
    *@Description: 认为资源方案定义到资源类别
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/9/22
    */
    public JSONObject getResourcePlanData2(List<NodeTaskM> taskMList) throws Exception{
        JSONObject data=new JSONObject();
        List<JSONObject> resourcePlanList=new ArrayList<>();
        List<JSONObject> resourceNeedList=new ArrayList<>();
        Map<String, ResourceItem> resourceMap=new HashMap<>();

        for (NodeTaskM task:taskMList){
            ResourcePlans plans=new ResourcePlans();
            plans.Retrieve(ResourcePlanAttr.NodeId,task.getNodeId());
            List<ResourcePlan> planBeforeList=plans.toList();
            for (ResourcePlan plan:planBeforeList){

                ResourceTasks resourceTasks=new ResourceTasks();
                resourceTasks.Retrieve(ResourceTaskAttr.PlanId,plan.getNo());
                List<ResourceTask> resourceTaskList=resourceTasks.toList();
                List<List<ResourceItem>> resourceItemList=new ArrayList<>();
                for (ResourceTask resourceTask:resourceTaskList){

                    String resourceNo=resourceTask.GetValStrByKey(ResourceTaskAttr.ResourceNo);
                    ResourceItems items=new ResourceItems();
                    items.Retrieve(ResourceItemAttr.Kind,resourceNo);
                    List<ResourceItem> itemList=items.toList();
                    resourceItemList.add(itemList);
                }
                //转换后的资源计划
                List<List<ResourceItem>> planAfterList=new ArrayList<>();
                getResourceItemPlan(resourceItemList,planAfterList,new ArrayList<>(),0);

                for (List<ResourceItem> tempPlan:planAfterList){
                    JSONObject item=new JSONObject();
                    String planId=FeignTool.getSerialNumber("BP.Resource.PlanTemp")+"_"+plan.getNo();
                    item.put("resPlanUid", planId);
                    item.put("resPlanTaskUid",task.getNo());
                    item.put("resPlanPriority",1);
                    resourcePlanList.add(item);

                    for (ResourceItem temp:tempPlan){

                        String resourceItemNo=temp.getNo();
                        ResourceItem resourceItem=resourceMap.get(resourceItemNo);
                        if (resourceItem==null) {
                            resourceItem = new ResourceItem(resourceItemNo);
                            resourceMap.put(resourceItemNo,resourceItem);
                        }

                        JSONObject resourceTaskItem=new JSONObject();
                        resourceTaskItem.put("resReqUid",FeignTool.getSerialNumber("BP.Resource.TaskTemp"));
                        resourceTaskItem.put("resReqResPlanUid",planId);
                        BP.Resource.Resource resource=new BP.Resource.Resource(temp.GetValStrByKey(ResourceItemAttr.Kind));
                        resourceTaskItem.put("resReqResType",resource.GetValIntByKey(ResourceAttr.Kind));
                        resourceTaskItem.put("resReqResUid",temp.getNo());
                        ResourceTasks resourceTasksTemp=new ResourceTasks();
                        resourceTasksTemp.Retrieve(ResourceTaskAttr.PlanId,plan.getNo(),ResourceTaskAttr.ResourceNo,resource.getNo());
                        ResourceTask resourceTemp=(ResourceTask) resourceTasksTemp.get(0);
                        resourceTaskItem.put("resReqResWork",resourceTemp.GetValIntByKey(ResourceTaskAttr.UseTime));
                        resourceTaskItem.put("resReqResAmount",resourceTemp.GetValIntByKey(ResourceTaskAttr.UseNum));
                        resourceTaskItem.put("resReqResWorkModel",task.getTaskWorkModel());
                        resourceNeedList.add(resourceTaskItem);
                    }
                }

            }
        }
        data.put("taskResPlan",resourcePlanList);
        data.put("taskResReq",resourceNeedList);

        //资源基本信息
        List<JSONObject> resourceHumanList=new ArrayList<>();
        List<JSONObject> resourceEquipmentList=new ArrayList<>();
        List<JSONObject> resourcePlaceList=new ArrayList<>();
        List<JSONObject> resourceKnowledgeList=new ArrayList<>();

        //资源已用
        List<JSONObject> resourceLoadList=new ArrayList<>();
        for (Map.Entry<String, ResourceItem> resourceEntry:resourceMap.entrySet()){
            ResourceTasks tasks=new ResourceTasks();
            tasks.Retrieve(ResourceTaskAttr.ResourceId,resourceEntry.getValue().GetValStrByKey(ResourceItemAttr.Kind),ResourceTaskAttr.IsPlan,1);//已经计划的资源任务
            List<ResourceTask> list=tasks.toList();

            for (ResourceTask task:list){
                JSONObject item=new JSONObject();
                item.put("arUid",task.getNo());
                item.put("arResUid",task.GetValStrByKey(ResourceTaskAttr.ResourceId));
                item.put("arResStartDateTime",task.GetValDateTime(ResourceTaskAttr.PlanStart).getTime());
                item.put("arResFinishDateTime",task.GetValDateTime(ResourceTaskAttr.PlanEnd).getTime());
                item.put("arResWork",(task.GetValIntByKey(ResourceTaskAttr.UseTime)+0.0)/10);
                item.put("arResAmount",task.GetValIntByKey(ResourceTaskAttr.UseNum));
                resourceLoadList.add(item);
            }

            ResourceItem resourceItem=resourceEntry.getValue();
            BP.Resource.Resource resource=new BP.Resource.Resource(resourceItem.GetValStrByKey(ResourceItemAttr.Kind));
            int type=resource.GetValIntByKey(ResourceAttr.Kind);//0=人力@1=设备@2=环境@3=知识"
            JSONObject item=new JSONObject();
            switch (type){
                case 0:
                    item.put("humUid",resourceItem.getNo());
                    resourceHumanList.add(item);
                    break;
                case 1:
                    item.put("equipUid",resourceItem.getNo());
                    resourceEquipmentList.add(item);
                    break;
                case 2:
                    item.put("placeUid",resourceItem.getNo());
                    item.put("placeArea",resource.GetValIntByKey(ResourceAttr.Num));
                    resourcePlaceList.add(item);
                    break;
                case 3:
                    item.put("knowlUid",resourceItem.getNo());
                    item.put("knowlAmount",resource.GetValIntByKey(ResourceAttr.Num));
                    resourceKnowledgeList.add(item);
                    break;
            }

        }

        data.put("allocateResource",resourceLoadList);
        data.put("human",resourceHumanList);
        data.put("equipment",resourceEquipmentList);
        data.put("place",resourcePlaceList);
        data.put("knowledge",resourceKnowledgeList);
        return data;
    }

    private void getResourceItemPlan(List<List<ResourceItem>> items,List<List<ResourceItem>> plans,List<ResourceItem> temp,int pos){
        if (pos==items.size()) {
            plans.add(temp);
            return;
        }
        for (ResourceItem item:items.get(pos)){
            List<ResourceItem> nextList=new ArrayList<>();
            nextList.addAll(temp);
            nextList.add(item);
            getResourceItemPlan(items,plans,nextList,pos+1);
        }


    }


    /**
    *@Description: 获取任务的分组（可调换分组）信息
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/9/14
    */
    public JSONObject getGroupData(List<NodeTaskM> taskMList) throws Exception{
        JSONObject data=new JSONObject();
        //<groupNo,>
        Map<String, NodeGroup> map=new HashMap<>();
        List<JSONObject> taskGroupList=new ArrayList<>();
        NodeGroupItems items=new NodeGroupItems();
        for (NodeTaskM taskM:taskMList){
            String nodeNo=taskM.getNodeId();
            items.Retrieve(NodeGroupItemAttr.node_no,nodeNo);
            List<NodeGroupItem> itemList=items.toList();
            for (NodeGroupItem item:itemList){
                String groupNo=item.GetValStrByKey(NodeGroupItemAttr.group_no);
                NodeGroup group=map.get(groupNo);
                if (group==null){
                    group=new NodeGroup(groupNo);
                    map.put(groupNo,group);
                }
                //模块分组不在统计范围内
                if (group.GetValIntByKey(NodeGroupAttr.type)==2)
                    continue;
                JSONObject temp=new JSONObject();
                temp.put("taskGroupUid",item.getNo());
                temp.put("taskGroupTaskUid",taskM.getNo());
                temp.put("taskGroupGroupUid",groupNo);
                taskGroupList.add(temp);
            }
        }

       /* List<JSONObject> groupList=new ArrayList<>();
        for (String groupNo:map.keySet()){
            JSONObject temp=new JSONObject();
            temp.put("groupUid",groupNo);
            groupList.add(temp);
        }*/

        data.put("taskGroup",taskGroupList);
        //data.put("group",groupList);
        return data;
    }


}
