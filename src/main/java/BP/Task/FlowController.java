package BP.Task;

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
            if (startFlow(workGroupId,parentWorkId,-1L,flow)){
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
            }else {
                result.put("message","");
            }

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
    public  boolean startFlow(Long workGroupId,Long parentWorkId, Long parentTaskId, Flow flow) throws Exception{

        if (!beforeStart(flow))
            return false;

        Long workId= FeignTool.getSerialNumber("BP.WF.Work");

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
        generFlowService.insertGenerFlow(generFlow);

        Nodes nodes=new Nodes();
        nodes.Retrieve(NodeAttr.FK_Flow,flow.getNo());
        List<Node> nodeList=nodes.toList();
        Boolean flag=true;
        for (Node node:nodeList){
                flag=flag&createNodeTask(workGroupId,workId,parentTaskId,node);
        }

        //更新节点任务间关系
        nodeTaskService.updateNodeTaskPreAfter(flow.getStartNodeID()+"",workId);
        return flag;
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
    public  Boolean createNodeTask(Long workGroupID,Long workId,Long parentTaskId,Node node) throws Exception{
        int nodeId=node.getNodeID();

        //创建节点任务
        Long nodeTaskId= FeignTool.getSerialNumber("BP.Task.NodeTask");

        NodeTaskM nodeTask=new NodeTaskM();
        nodeTask.setTotalTime(Integer.valueOf(node.GetValStringByKey(NodeAttr.Doc)));
        nodeTask.setNodeId(nodeId+"");
        nodeTask.setNodeName(node.getName());
        nodeTask.setFlowId(node.GetValStringByKey(NodeAttr.FK_Flow));
        nodeTask.setWorkGroupId(workGroupID+"");
        nodeTask.setWorkId(workId+"");
        nodeTask.setParentNodeTask(parentTaskId+"");
        nodeTask.setNo(nodeTaskId);
        nodeTask.setIsReady(20);//未准备
        nodeTask.setStatus(20);
        //因为执行人需要汉卿的计划，所以现在直接指定
        nodeTask.setExecutor(WebUser.getNo());
        Date now=new Date();
        nodeTask.setPlanEndTime(now);
        nodeTask.setPlanStartTime(now);
        nodeTask.setEndTime(now);
        nodeTask.setStartTime(now);
        nodeTask.setEarlyStartTime(node.GetValDateTime(NodeAttr.EarlyStart));
        nodeTask.setOldestFinishTime(node.GetValDateTime(NodeAttr.LaterFinish));
        nodeTaskService.insertNodeTask(nodeTask);

        String[] childFlows=node.getSubFlowNos();
        Boolean flag=true;
        if (childFlows!=null) {
            for (String childFlowNo : childFlows) {
                Flow f = new Flow(childFlowNo);
                flag = flag & startFlow(workGroupID, workId, nodeTaskId, f);
            }
        }

        return flag;
    }

    @RequestMapping("getAfterNodes")
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


}
