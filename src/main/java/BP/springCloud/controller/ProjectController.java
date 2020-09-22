package BP.springCloud.controller;

import BP.Project.ProjectNodeService;
import BP.Project.ProjectTree;
import BP.Project.ProjectTreeAttr;
import BP.Task.FlowGener;
import BP.Task.FlowGenerAttr;
import BP.Task.FlowGeners;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.springCloud.entity.ProjectNode;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-08-26 10:43
 **/
@Controller
@RequestMapping("Project")
public class ProjectController {
    private  final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Resource
    private ProjectNodeService projectNodeService;

    @RequestMapping("getProjectTreeData")
    @ResponseBody
    public JSONObject getProjectTreeData(String projectNo){
        JSONObject data=new JSONObject();
        try{
            ProjectTree projectTree=new ProjectTree(projectNo);
            Flow flow=new Flow(projectTree.GetValStrByKey(ProjectTreeAttr.FlowNo));
            data=getTreeData(flow,projectTree.GetValStrByKey(ProjectTreeAttr.GenerFlowNo));

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return data;
    }

    private JSONObject getTreeData(Flow flow,String generNo) throws Exception{
        List<JSONObject> children=new ArrayList<>();
        Nodes nodes=flow.getHisNodes();
        for (Node temp:nodes.ToJavaList()){
            String[] subFlowNos=temp.getSubFlowNos();
            for (String flowNo:subFlowNos){
                Flow subFlow=new Flow(flowNo);
                FlowGeners geners=new FlowGeners();
                geners.Retrieve(FlowGenerAttr.ParentWorkId,generNo,FlowGenerAttr.FlowId,flowNo);
                List<FlowGener> generList=geners.toList();
                JSONObject subData=new JSONObject();
                if (generList.size()>0)
                    subData=getTreeData(subFlow,generList.get(0).getNo());
                else
                    subData=getTreeData(subFlow,"");
                subData.put("name",temp.getNo()+"_"+subData.get("name"));
                children.add(subData);
            }
        }
        JSONObject data=new JSONObject();
        data.put("name",flow.getName());
        data.put("value",flow.getNo());
        data.put("generNo",generNo);
        data.put("children",children);
        return data;
    }

    @RequestMapping("addProjectTree")
    @ResponseBody
    public JSONObject addProjectTree(ProjectTree projectTree){
        return null;
    }

    @RequestMapping("deleteProjectTree")
    @ResponseBody
    public JSONObject deleteProjectTree(int projectTreeNo){
        return null;
    }

    @RequestMapping("deleteProjectTreeNode")
    @ResponseBody
    public JSONObject deleteProjectTreeNode(int projectTreeNodeNo){
        return null;
    }

    @RequestMapping("addProjectTreeNode")
    @ResponseBody
    public JSONObject addProjectTreeNode(@RequestBody ProjectNode projectNode){
        return null;
    }
}
