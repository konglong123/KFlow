package BP.springCloud.controller;


import BP.Project.ProjectTree;
import BP.Project.ProjectTreeAttr;
import BP.Project.ProjectTrees;
import BP.Resource.ResourceAttr;
import BP.Resource.Resources;
import BP.Task.NodeTaskService;
import BP.springCloud.entity.NodeTaskM;
import BP.springCloud.tool.FeignTool;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public JSONObject planAllTask(@RequestBody JSONObject con){
        JSONObject data=new JSONObject();

        try {
            //封装项目信息
            JSONObject projects=new JSONObject();
            ProjectTrees projectList=new ProjectTrees();
            projectList.Retrieve(ProjectTreeAttr.Status,1);
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
                tasks.addAll(tempList);
            }
            JSONObject taskData=nodeTaskService.getPlanData(tasks);
            data.putAll(taskData);

            //封装任务连接信息
            JSONObject linkData=nodeTaskService.getPlanLinkData(tasks);
            data.putAll(linkData);

            //封装分组信息
            JSONObject groupData=nodeTaskService.getGroupData(tasks);
            data.putAll(groupData);


            //封装资源，资源任务
            JSONObject resource=nodeTaskService.getResourcePlanData(tasks);
            data.putAll(resource);

        }catch (Exception e){
            logger.error(e.getMessage());
        }

        return data;
    }

    @ResponseBody
    @RequestMapping("analysePlanData")
    public JSONObject analysePlanData(@RequestBody JSONObject con){
        return null;
    }

}
