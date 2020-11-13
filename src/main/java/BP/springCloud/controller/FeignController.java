package BP.springCloud.controller;


import BP.NodeGroup.NodeGroup;
import BP.NodeGroup.NodeGroupAttr;
import BP.Project.ProjectTree;
import BP.Project.ProjectTreeAttr;
import BP.Project.ProjectTrees;
import BP.Resource.*;
import BP.Task.NodeTask;
import BP.Task.NodeTaskAttr;
import BP.Task.NodeTaskService;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.Template.Direction;
import BP.WF.Template.DirectionAttr;
import BP.WF.Template.Directions;
import BP.springCloud.entity.NodeTaskM;
import BP.springCloud.tool.FeignTool;
import BP.springCloud.tool.Page;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: kflow-web
 * @description:负责调用其他微服务，
 * @author: Mr.Kong
 * @create: 2019-12-24 14:05
 **/
@Controller
@RequestMapping("feign")
public class FeignController {
    private final Logger logger = LoggerFactory.getLogger(FeignController.class);

    @Resource
    private NodeTaskService nodeTaskService;

    @RequestMapping("/addWF")
    @ResponseBody
    public String add(Long id,String abstracts){
       /* String result=FeignTool.template.getForEntity("http://112.125.90.132:8082/es/addWorkflow?abstracts="+abstracts+"&id="+id,String.class).getBody();
        return result;*/
       return "no available";
    }

    @RequestMapping("/getWF")
    @ResponseBody
    public void queryWorkflow(HttpServletRequest request, HttpServletResponse response){
        try {
            FeignTool.esQuery("http://112.125.90.132:8082/es/getWFDsl",request,response);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    /**
    *@Description: RestTemplate属性设置
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2019/12/26 
    */
    private void setTemplate() throws Exception {

    }

    @RequestMapping("/getResources")
    @ResponseBody
    public void queryResources(HttpServletRequest request, HttpServletResponse response){
        //优先编码查询
        try {
            String resourceNo=request.getParameter("resourceNo");
            if (resourceNo!=null&&!resourceNo.equals("")){
                Resources resources=new Resources();

                    resources.RetrieveByAttr(ResourceAttr.No,resourceNo);
                    List list=resources.ToDataTableField().Rows;
                    Map<String, Object> jsonMap = new HashMap<>();//定义map
                    jsonMap.put("rows",list);
                    jsonMap.put("total",list.size());
                    response.setCharacterEncoding("utf-8");
                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    String result = JSONObject.fromObject(jsonMap).toString();//格式化result   一定要是JSONObject
                    //为适配es数据中no，将No转换成no
                    char[] cs=result.toCharArray();
                    for (int i=0;i<cs.length;i++){
                        if (cs[i]=='N')
                            cs[i]='n';
                    }
                    out.print(new String(cs));
                    out.flush();
                    out.close();

            }else{
                FeignTool.esQuery("http://112.125.90.132:8082/es/getResourceDsl",request,response);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    /**
    *@Description: nlp模型训练
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/3/29
    */
    @RequestMapping("trainNlp")
    public JSONObject trainNlpModel(HttpServletRequest request, HttpServletResponse response){
        return null;
    }


    /**
    *@Description: 计划调度基础数据封装
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/9/14
    */
    @ResponseBody
    @RequestMapping("planAllTask")
    public JSONObject planAllTask(@RequestBody JSONObject con) {
        JSONObject resultMes=new JSONObject();
        try {
            ProjectTrees projectList=new ProjectTrees();
            projectList.Retrieve(ProjectTreeAttr.Status,1);
            planProjects(projectList);
            updateProject(projectList);

        }catch (Exception e){
            logger.error(e.getMessage());
            resultMes.put("meg",e.getMessage());
            return resultMes;
        }
        resultMes.put("meg","计划成功！");
        return resultMes;
    }

    private void updateProject(ProjectTrees projectList) throws Exception{
        List<ProjectTree> treeList=projectList.toList();
        for (ProjectTree tree:treeList){
            tree.SetValByKey(ProjectTreeAttr.Status,5);//更新为已经计划
            tree.Update();

            //起始节点任务状态更新为可开始
            Flow flow=new Flow(tree.GetValStrByKey(ProjectTreeAttr.FlowNo));
            NodeTaskM NodeTaskCon = new NodeTaskM();
            NodeTaskCon.setWorkGroupId(tree.getNo() + "");
            NodeTaskCon.setNodeId(flow.getStartNodeID() + "");
            List<NodeTaskM> list = nodeTaskService.findNodeTaskList(NodeTaskCon);
            if (list != null && list.size() == 1) {
                NodeTaskM startTask = list.get(0);
                nodeTaskService.startNodeTask(startTask,null);
            }
        }
    }

    @ResponseBody
    @RequestMapping("planProjects")
    public JSONObject planProjects(@RequestBody List<String> projectNos) {
        JSONObject resultMes=new JSONObject();
        try {
            ProjectTrees projectList=new ProjectTrees();
            for (String projectNo:projectNos){
                ProjectTree tree=new ProjectTree(projectNo);
                projectList.add(tree);
            }
            planProjects(projectList);
            updateProject(projectList);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultMes.put("meg",e.getMessage());
            return resultMes;
        }
        resultMes.put("meg","计划成功！");
        return resultMes;
    }

    private void planProjects(ProjectTrees projectList) throws Exception{

            JSONObject data=new JSONObject();
            //封装项目信息
            JSONObject projectData=projectList.getPlanData();
            data.putAll(projectData);
            List<ProjectTree> projectTreeList=projectList.toList();
            List<String> projectNoList=new ArrayList<>();
            for (ProjectTree project:projectTreeList)
                projectNoList.add(project.getNo());

            //封装任务信息
            List<NodeTaskM> tasks=new ArrayList<>();
            NodeTaskM taskCon=new NodeTaskM();
            for (String projectNo:projectNoList){
                taskCon.setWorkGroupId(projectNo);
                List<NodeTaskM> tempList=nodeTaskService.findNodeTaskList(taskCon);
                //过滤已经完成的任务
                for(NodeTaskM temp:tempList){
                    if (temp.getIsReady()==3){//任务已经完成，不进行计划
                        continue;
                    }
                    tasks.add(temp);
                }
            }
            JSONObject taskData=nodeTaskService.getPlanData(tasks);
            data.putAll(taskData);

            //封装任务连接信息
            JSONObject linkData=nodeTaskService.getPlanLinkData(tasks);
            data.putAll(linkData);

            //封装分组信息
            JSONObject groupData=nodeTaskService.getGroupData(tasks);
            data.putAll(groupData);

            String[] holiday={"2020-01-01", "2020-01-24", "2020-01-25", "2020-01-26", "2020-01-27", "2020-01-28", "2020-01-29", "2020-01-30", "2020-04-04", "2020-04-05", "2020-04-06", "2020-05-01", "2020-05-02", "2020-05-03", "2020-05-04", "2020-05-05", "2020-06-25", "2020-06-26", "2020-06-27", "2020-10-01", "2020-10-02", "2020-10-03", "2020-10-04", "2020-10-05", "2020-10-06", "2020-10-07", "2020-10-08"};
            data.put("holiday",holiday);


            //封装资源，资源任务
            //JSONObject resource=nodeTaskService.getResourcePlanData(tasks);
            JSONObject resource=nodeTaskService.getResourcePlanData2(tasks);
            data.putAll(resource);

            LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.put("Content-Type", Collections.singletonList("application/json;charset=UTF-8"));

            String url="http://192.168.1.101:8082/pms/projOpt/testOptResult";
            HttpEntity<Map> requestEntity = new HttpEntity<>(data, headers);
            ResponseEntity<JSONObject> resTemp = FeignTool.template.postForEntity(url, requestEntity, JSONObject.class);
            JSONObject result=resTemp.getBody();

            //处理返回的数据
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            JSONArray newTasks=result.getJSONArray("nodeTask");
            for (int i=0;i<newTasks.size();i++){
                //更新资源任务信息
                JSONObject task=(JSONObject) newTasks.get(i);
                String taskNo=task.getString("taskNo");
                long planStart=task.getLong("planStart");
                long planEnd=task.getLong("planEnd");
                String planNo=task.getString("planNo").split("_")[1];
                NodeTask nodeTask=new NodeTask(taskNo);
                nodeTask.SetValByKey(NodeTaskAttr.PlanStartTime,formatter.format(new Date(planStart)));
                nodeTask.SetValByKey(NodeTaskAttr.PlanEndTime,formatter.format(new Date(planEnd)));
                nodeTask.SetValByKey(NodeTaskAttr.IsReady,9);//设置状态为“已经计划”
                nodeTask.Update();

                //更新资源占用信息
                JSONArray resources=task.getJSONArray("resList");
                for (int index=0;index<resources.size();index++){
                    JSONObject resTask=(JSONObject) resources.get(index);
                    String resItemNo=resTask.getString("resNo");
                    long planStartRes=task.getLong("planStart");
                    long planEndRes=task.getLong("planEnd");

                    ResourceItem item=new ResourceItem(resItemNo);
                    String resNo=item.GetValStrByKey(ResourceItemAttr.Kind);

                    ResourceTasks resourceTasks=new ResourceTasks();
                    resourceTasks.Retrieve(ResourceTaskAttr.ResourceNo,resNo,ResourceTaskAttr.PlanId,planNo);
                    ResourceTask resourceTask=(ResourceTask) resourceTasks.get(0);
                    resourceTask.SetValByKey(ResourceTaskAttr.PlanStart,formatter.format(new Date(planStartRes)));
                    resourceTask.SetValByKey(ResourceTaskAttr.PlanEnd,formatter.format(new Date(planEndRes)));
                    resourceTask.SetValByKey(ResourceTaskAttr.IsPlan,1);//更新状态为“已计划”（后续需要建立枚举）
                    resourceTask.SetValByKey(ResourceTaskAttr.ResourceId,resItemNo);
                    resourceTask.SetValByKey(ResourceTaskAttr.TaskId,taskNo);
                    resourceTask.Update();
                }
            }

            //更新分组内节点连接方向
            updateFlowDirection(newTasks,groupData);


    }

    private void updateFlowDirection(JSONArray newTasks,JSONObject groupData) throws Exception{
        JSONArray taskGroup=groupData.getJSONArray("taskGroup");
        //记录任务所在分组信息
        Map<String,String> taskGroupMap=new HashMap<>();
        for (int i=0;i<taskGroup.size();i++){
            JSONObject groupItem=(JSONObject)taskGroup.get(i);
            taskGroupMap.put(groupItem.getLong("taskGroupTaskUid")+"",groupItem.getString("taskGroupGroupUid"));
        }
        //每个分组下的所有节点任务
        Map<String,List<JSONObject>> groupTaskMap=new HashMap<>();
        for (int i=0;i<newTasks.size();i++){
            JSONObject task=(JSONObject)newTasks.get(i);
            String taskNo=task.getString("taskNo");
            if (taskGroupMap.containsKey(taskNo)){
                String groupNo=taskGroupMap.get(taskNo);
                List<JSONObject> taskList=groupTaskMap.get(groupNo);
                if (taskList==null){
                    taskList=new ArrayList<>();
                    groupTaskMap.put(groupNo,taskList);
                }
                taskList.add(task);
            }
        }
        //排序每个分组下任务
        for (Map.Entry<String,List<JSONObject>> entry:groupTaskMap.entrySet()){
            List<JSONObject> list=entry.getValue();
            Collections.sort(list, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject o1, JSONObject o2) {
                    long temp= o1.getLong("planStart")-o2.getLong("planStart");
                    if (temp<0)
                        return -1;
                    else if (temp>0)
                        return 1;
                    return 0;
                }
            });

            List<Node> nodeList=new ArrayList<>();
            Set<String> nodeNoSet=new HashSet<>();
            for (JSONObject temp:list){
                NodeTask nodeTask=new NodeTask(temp.getString("taskNo"));
                Node node=new Node(nodeTask.GetValStrByKey(NodeTaskAttr.NodeId));
                nodeList.add(node);
                nodeNoSet.add(node.getNo());
            }

            Directions directions=new Directions();
            List<Direction> directionList;
            for (Node node:nodeList){
                directions.Retrieve(DirectionAttr.ToNode,node.getNo());
                directionList=directions.toList();
                for (Direction direction:directionList){
                    if (nodeNoSet.contains(direction.getNode()+"")){//属于分组内部连接，删除
                        direction.Delete();
                    }else {//属于分组前端分界点
                        Direction newDir=new Direction();
                        newDir.setFK_Flow(direction.getFK_Flow());
                        newDir.setNode(direction.getNode());
                        newDir.setToNode(Integer.valueOf(nodeList.get(0).getNo()));
                        direction.Delete();
                        newDir.Insert();
                    }
                }

                directions.Retrieve(DirectionAttr.Node,node.getNo());
                directionList=directions.toList();
                for (Direction direction:directionList){
                    if (nodeNoSet.contains(direction.getToNode()+"")){
                        direction.Delete();
                    }else {
                        Direction newDir=new Direction();
                        newDir.setFK_Flow(direction.getFK_Flow());
                        newDir.setNode(Integer.valueOf(nodeList.get(nodeList.size()-1).getNo()));
                        newDir.setToNode(direction.getToNode());
                        direction.Delete();
                        newDir.Insert();
                    }
                }
            }

            NodeGroup nodeGroup=new NodeGroup(entry.getKey());
            //根据排序，重新组织分组内部连接方向
            for (int i=0;i<nodeList.size()-1;i++){
                Direction direction=new Direction();
                direction.setNode(Integer.valueOf(nodeList.get(i).getNo()));
                direction.setToNode(Integer.valueOf(nodeList.get(i+1).getNo()));
                direction.setFK_Flow(nodeGroup.GetValStrByKey(NodeGroupAttr.flow_no));
                direction.Insert();
            }
        }


    }


    @ResponseBody
    @RequestMapping("analysePlanData")
    public JSONObject analysePlanData(@RequestBody JSONObject con){
        return null;
    }

}
