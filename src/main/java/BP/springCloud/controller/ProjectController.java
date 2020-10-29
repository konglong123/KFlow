package BP.springCloud.controller;

import BP.Project.ProjectNodeService;
import BP.Project.ProjectTree;
import BP.Project.ProjectTreeAttr;
import BP.Project.ProjectTrees;
import BP.Task.FlowGener;
import BP.Task.FlowGenerAttr;
import BP.Task.FlowGeners;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.springCloud.tool.PageTool;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        if (!StringUtils.isEmpty(generNo)) {
            FlowGener flowGener=new FlowGener(generNo);
            data.put("flag", flowGener.GetValByKey(FlowGenerAttr.Status));//@1=开始@2=完成@3=准备
        }
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

    @RequestMapping("getProjectList")
    @ResponseBody
    public JSONObject getProjectList(HttpServletRequest request, HttpServletResponse response){
        try {
            ProjectTrees trees = new ProjectTrees();
            trees.RetrieveAll();
            PageTool.TransToResult(trees,request,response);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    //获取项目统计信息（各种状态项目统计）
    @RequestMapping("getProjectInfo")
    @ResponseBody
    public JSONObject getProjectInfo(){
        try {
            JSONObject result=new JSONObject();
            int[] nums=new int[5];
            ProjectTrees trees = new ProjectTrees();
            trees.RetrieveAll();
            List<ProjectTree> projectList=trees.toList();
            for (ProjectTree project:projectList){
                int status=project.GetValIntByKey(ProjectTreeAttr.Status);
                nums[status]++;
            }
            List<JSONObject> data=new ArrayList<>(5);
            String[] names={"新建","运行中","挂起","结束","异常"};
            for (int i=0;i<5;i++){
                JSONObject item=new JSONObject();
                item.put("value",nums[i]);
                item.put("name",names[i]);
                data.add(item);
            }
            result.put("seriesData",data);
            result.put("legendData",names);
            return result;
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    //获取项目信息（某个状态下的项目统计）
    @RequestMapping("getProjectInfoForStatus")
    @ResponseBody
    public JSONObject getProjectInfoForStatus(String status){
        try {
            if (!StringUtils.isEmpty(status)) {
                JSONObject result = new JSONObject();
                ProjectTrees trees = new ProjectTrees();
                trees.Retrieve(ProjectTreeAttr.Status, status);
                List<ProjectTree> projectList = trees.toList();
                for (ProjectTree project : projectList) {

                }
                return result;
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    private JSONObject getProjectPercent(String projectNo){
        return null;
    }

}
