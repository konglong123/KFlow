package BP.springCloud.tool;

import BP.Task.*;
import BP.Tools.BeanTool;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Template.*;
import BP.Web.WebUser;
import BP.springCloud.entity.NodeTaskM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-03-08 13:46
 **/
public class KFlowTool {
    private  final Logger logger = LoggerFactory.getLogger(KFlowTool.class);


    /**
     *@Description: 启动流程，产生所有对节点任务
     *@Param:
     *@return:
     *@Author: Mr.kong
     *@Date: 2020/3/8
     */
    public  Boolean startFlow(Long workId,Long parentTaskId,Flow flow) throws Exception{

        if (!beforeStart(flow))
            return false;

        Nodes nodes=new Nodes();
        nodes.Retrieve(NodeAttr.FK_Flow,flow.getNo());
        List<Node> nodeList=nodes.toList();
        Boolean flag=true;
        int startNodeId=flow.getStartNodeID();
        for (Node node:nodeList){
           /* String runModel=node.GetValStrByKey(NodeAttr.RunModel);
            //普通节点、子线程节点才产生节点任务（合流、分流节点不产生）
            if (!runModel.equals("0")&&!runModel.equals("4")){
                continue;
            }*/
            if (startNodeId==node.getNodeID())
                flag=flag&startNodeTask(workId,parentTaskId,node,WebUser.getNo(),1);
            else
                flag=flag&startNodeTask(workId,parentTaskId,node,"",0);
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
    public  Boolean startNodeTask(Long workID,Long parentTaskId,Node node,String userNo,int isReady) throws Exception{
        int nodeId=node.getNodeID();

        //创建节点任务
        Long nodeTaskId= FeignTool.getSerialNumber("BP.Task.NodeTask");
        NodeTask nodeTask=new NodeTask();
        nodeTask.SetValByKey(NodeTaskAttr.TotalTime,node.GetValStringByKey(NodeAttr.Doc));
        nodeTask.SetValByKey(NodeTaskAttr.NodeId,nodeId);
        nodeTask.SetValByKey(NodeTaskAttr.FlowId,node.GetValStringByKey(NodeAttr.FK_Flow));
        nodeTask.SetValByKey(NodeTaskAttr.FlowTaskId,workID);
        nodeTask.SetValByKey(NodeTaskAttr.ParentNodeTask,parentTaskId);
        nodeTask.SetValByKey(NodeTaskAttr.No,nodeTaskId);
        nodeTask.SetValByKey(NodeTaskAttr.IsReady,isReady);
        nodeTask.SetValByKey(NodeTaskAttr.Executor,userNo);
        nodeTask.Insert();



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
