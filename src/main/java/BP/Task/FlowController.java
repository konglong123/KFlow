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

            Flow flow=new Flow(flowNo);
            if (startFlow(workGroupId,-1L,-1L,flow)){
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
        generFlow.setCreater(WebUser.getNo());
        generFlowService.insertGenerFlow(generFlow);

        Nodes nodes=new Nodes();
        nodes.Retrieve(NodeAttr.FK_Flow,flow.getNo());
        List<Node> nodeList=nodes.toList();
        Boolean flag=true;
        for (Node node:nodeList){
                flag=flag&createNodeTask(workGroupId,workId,parentTaskId,node);
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
    public  Boolean createNodeTask(Long workGroupID,Long workId,Long parentTaskId,Node node) throws Exception{
        int nodeId=node.getNodeID();

        //创建节点任务
        Long nodeTaskId= FeignTool.getSerialNumber("BP.Task.NodeTask");

        NodeTaskM nodeTask=new NodeTaskM();
        nodeTask.setTotalTime(Integer.valueOf(node.GetValStringByKey(NodeAttr.Doc)));
        nodeTask.setNodeId(nodeId+"");
        nodeTask.setFlowId(node.GetValStringByKey(NodeAttr.FK_Flow));
        nodeTask.setWorkGroupId(workGroupID+"");
        nodeTask.setWorkId(workId+"");
        nodeTask.setParentNodeTask(parentTaskId+"");
        nodeTask.setNo(nodeTaskId);
        /*nodeTask.setIsReady(isReady);
        nodeTask.setExecutor(userNo);*/
        Date now=new Date();
        nodeTask.setPlanEndTime(now);
        nodeTask.setPlanStartTime(now);
        nodeTask.setEndTime(now);
        nodeTask.setStartTime(now);
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

}
