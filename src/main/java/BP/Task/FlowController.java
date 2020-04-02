package BP.Task;

import BP.DA.DBAccess;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Template.FrmSubFlow;
import BP.WF.Template.NodeAttr;
import BP.Web.WebUser;
import BP.springCloud.dao.NodeTaskMDao;
import BP.springCloud.entity.GenerFlow;
import BP.springCloud.entity.NodeTaskM;
import BP.springCloud.tool.FeignTool;
import BP.springCloud.tool.KFlowTool;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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
    private  final Logger logger = LoggerFactory.getLogger(KFlowTool.class);

    @Resource
    private NodeTaskService nodeTaskService;

    @Resource
    private GenerFlowService generFlowService;

    @RequestMapping("startFlow")
    @ResponseBody
    public JSONObject startFlow(String flowNo) {
        JSONObject result=new JSONObject();
        try {
            Long workid= FeignTool.getSerialNumber("BP.WF.Work");

            //创建流程实例信息
            GenerFlow generFlow=new GenerFlow();
            generFlow.setNo(workid);
            generFlow.setWorkId(workid);
            generFlow.setFlowId(Integer.valueOf(flowNo));
            generFlow.setStatus(1);
            generFlow.setCreater(WebUser.getNo());
            generFlowService.insertGenerFlow(generFlow);

            Flow flow=new Flow(flowNo);
            if (startFlow(workid+"",-1L,flow)){
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
    public  boolean startFlow(String workId, Long parentTaskId, Flow flow) throws Exception{

        if (!beforeStart(flow))
            return false;

        Nodes nodes=new Nodes();
        nodes.Retrieve(NodeAttr.FK_Flow,flow.getNo());
        List<Node> nodeList=nodes.toList();
        Boolean flag=true;
        int startNodeId=flow.getStartNodeID();
        for (Node node:nodeList){
                flag=flag&createNodeTask(workId,parentTaskId,node,"",0);
        }

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
    public  Boolean createNodeTask(String workID,Long parentTaskId,Node node,String userNo,int isReady) throws Exception{
        int nodeId=node.getNodeID();

        //创建节点任务
        Long nodeTaskId= FeignTool.getSerialNumber("BP.Task.NodeTask");
        NodeTaskM nodeTask=new NodeTaskM();
        nodeTask.setTotalTime(Integer.valueOf(node.GetValStringByKey(NodeAttr.Doc)));
        nodeTask.setNodeId(nodeId+"");
        nodeTask.setFlowId(node.GetValStringByKey(NodeAttr.FK_Flow));
        nodeTask.setFlowTaskId(workID);
        nodeTask.setParentNodeTask(parentTaskId+"");
        nodeTask.setNo(nodeTaskId);
        nodeTask.setIsReady(isReady);
        nodeTask.setExecutor(userNo);
        Date now=new Date();
        nodeTask.setPlanEndTime(now);
        nodeTask.setPlanStartTime(now);
        nodeTask.setEndTime(now);
        nodeTask.setStartTime(now);
        nodeTaskService.insertNodeTask(nodeTask);



        //启动子流程
        FrmSubFlow subFlow=new FrmSubFlow(nodeId);
        //String childFlow=subFlow.getSFActiveFlows();
        String childFlow=subFlow.getSFDefInfo();
        if (childFlow==null||childFlow.equals(""))
            return true;
        String[] childFlows=childFlow.split("%");
        Boolean flag=true;
        for (String childFlowNo:childFlows){
            Flow f=new Flow(childFlowNo);
            flag=flag&startFlow(workID,nodeTaskId,f);
        }

        return flag;
    }

}
