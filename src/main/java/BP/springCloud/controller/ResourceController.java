package BP.springCloud.controller;

import BP.DA.DataTable;
import BP.Resource.*;
import BP.springCloud.entity.ResourceTaskM;
import BP.springCloud.tool.PageTool;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-02-26 14:43
 **/
@Controller
@RequestMapping("/resource")
public class ResourceController {
    private final Logger logger = LoggerFactory.getLogger(ResourceController.class);

    @Resource
    private ResourceService resourceService;

    //获取节点资源方案
    @RequestMapping("/getNodeResourcePlans")
    public void getNodeResourcePlans(HttpServletRequest request, HttpServletResponse response) {
        try {
            ResourcePlans resourcePlans=new ResourcePlans();
            Long nodeId=Long.parseLong(request.getParameter("nodeId"));
            resourcePlans.RetrieveByAttr(ResourcePlanAttr.NodeId,nodeId);

            PageTool.TransToResult(resourcePlans,request,response);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    //创建资源方案
    @RequestMapping("addResourcePlan")
    public void addResourcePlan(HttpServletResponse response,HttpServletRequest request){
        ResourcePlan plan=new ResourcePlan();
        try {
            String nodeId=request.getParameter("nodeId");
            plan.SetValByKey(ResourcePlanAttr.NodeId,nodeId);
            plan.Insert();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    @RequestMapping("delResourcePlan")
    public void delResourcePlan(HttpServletResponse response,HttpServletRequest request){
        try {
            String no=request.getParameter("no");
            ResourcePlan plan=new ResourcePlan(no);
            plan.Delete();
            //删除资源方案下预定资源
            ResourceTasks tasks=new ResourceTasks();
            tasks.Delete(ResourceTaskAttr.PlanId,no);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }
    /**
    *@Description: 获取节点占用资源列表
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/2/26
    */
    @RequestMapping("/getNodeResources")
    public void getNodeResources(HttpServletRequest request, HttpServletResponse response){
        try {
            response.setCharacterEncoding("utf-8");
            request.setCharacterEncoding("utf-8");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            //请求页码、每页显示行数、偏移、总数
            int page, rows;
            String input_page = request.getParameter("page");
            page = (input_page == null) ? 1 : Integer.parseInt(input_page);
            String input_rows = request.getParameter("rows");
            rows = (input_rows == null) ? 10 : Integer.parseInt(input_rows);

            ResourceTasks resourceTasks=new ResourceTasks();
            Long nodeId=Long.parseLong(request.getParameter("nodeId"));
            String planId=request.getParameter("planId");
            resourceTasks.Retrieve(ResourceTaskAttr.NodeId,nodeId,ResourceTaskAttr.PlanId,planId);
            DataTable dt =resourceTasks.ToDataTableField();//此方法针对tinyint默认转换成boolean
            List resourceList=dt.Rows;

            Map<String, Object> jsonMap = new HashMap<>();//定义map
            int allNum=resourceList.size();
            jsonMap.put("total", allNum);//total键 存放总记录数，必须的
            if (allNum==0){
                jsonMap.put("rows", new ArrayList<>(1));//消除查询结果为空时，前端报错
            }else {
                List rList=new ArrayList(rows);
                int endPos=page*rows;
                endPos=endPos<allNum?endPos:allNum;
                for (int i=(page-1)*rows;i<endPos;i++){
                    Map<String,Object> temp=(Map) resourceList.get(i);
                    for (String str:temp.keySet()){
                        Object item=temp.get(str);
                        if (item instanceof Date){
                            temp.put(str,item.toString());
                        }
                    }
                    rList.add(temp);
                }
                jsonMap.put("rows", rList);//rows键 存放每页记录 list
            }
            JSONObject json = new JSONObject();
            json.putAll(jsonMap);
            String result = json.toString();
            out.print(result);
            out.flush();
            out.close();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    /**
    *@Description:  查询资源对使用情况（根据资源类别）
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/2/26 
    */
    @RequestMapping("/getResourceLoad")
    @ResponseBody
    public Object getResourceLoad(HttpServletRequest request, HttpServletResponse response){
        String resourceNo=request.getParameter("resourceNo");
        if (resourceNo!=null) {
            return resourceService.getResourceLoad(resourceNo);
        }
        return null;
    }
    
    /**
    *@Description: 节点预定资源
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/2/26 
    */
    @RequestMapping("/bookResource")
    @ResponseBody
    public Object bookResource(@RequestBody ResourceTaskM con){

        try{
            resourceService.bookResource(con);
        }catch (Exception e){
            logger.error(e.getMessage());
            return e.getMessage();
        }
        return null;
    }

    @RequestMapping("/deleteResourceTask")
    @ResponseBody
    public Object deleteResourceTask(HttpServletRequest request, HttpServletResponse response){
        String no=request.getParameter("no");
        if (no!=null&&!no.equals("")){
            resourceService.deleteResourceTask(no);
        }
        return null;
    }

    //获取该类资源下所有资源实例的甘特图
    @RequestMapping("/getResourceKindGant")
    @ResponseBody
    public JSONObject getResourceKindGant(HttpServletRequest request){
        String resourceNo=request.getParameter("resourceNo");
        if (resourceNo!=null) {
            return resourceService.getResourceKindGant(resourceNo);
        }
        return null;
    }

    //获取资源实例在不同项目角度的甘特图
    @RequestMapping("/getResourceItemGant")
    @ResponseBody
    public JSONObject getResourceItemGant(HttpServletRequest request){
        String resourceNo=request.getParameter("resourceNo");
        if (resourceNo!=null) {
            return resourceService.getResourceItemGant(resourceNo);
        }
        return null;
    }

    //获取资源的平均负载数据（资源类别下的资源实例）
    @RequestMapping("/getResourceLoadAve")
    @ResponseBody
    public JSONObject getResourceLoadAve(HttpServletRequest request){
        String resourceNo=request.getParameter("resourceNo");
        try {
            if (resourceNo != null) {
                ResourceItems items = new ResourceItems();
                items.Retrieve(ResourceItemAttr.Kind, resourceNo);
                List<ResourceItem> itemList=items.toList();
                List<Integer> dataList=new ArrayList<>(itemList.size());
                List<String> dataAxis=new ArrayList<>(itemList.size());
                for (ResourceItem item:itemList){
                    List<JSONObject> data=getResourceItemLoadDaily(item.getNo()).getJSONArray("data");
                    double sum=0.0;
                    for (JSONObject temp:data){
                        sum+=temp.getDouble("score");
                    }
                    dataList.add((int)(sum*1000/data.size()));
                    dataAxis.add(item.getNo());
                }

                JSONObject result=new JSONObject();
                result.put("data",dataList);
                result.put("dataAxis",dataAxis);
                return result;
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    //获取资源的每天负载数据（资源类别下的资源实例）
    @RequestMapping("/getResourceLoadDaily")
    @ResponseBody
    public JSONObject getResourceLoadDaily(HttpServletRequest request){
        String resourceNo=request.getParameter("resourceId");
        try {
            if (resourceNo!=null) {
                JSONObject data= getResourceItemLoadDaily(resourceNo);
                List<JSONObject> list=data.getJSONArray("data");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                JSONObject result=new JSONObject();
                List<String> category=new ArrayList<>(list.size());
                List<Double> value=new ArrayList<>(list.size());

                for (JSONObject item:list){
                    category.add(formatter.format(new Date(item.getLong("time"))));
                    value.add(item.getDouble("score")*1000);
                }
                result.put("categoryData",category);
                result.put("valueData",value);
                return result;
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    private JSONObject getResourceItemLoadDaily(String itemId) throws Exception{
        ResourceTasks tasks=new ResourceTasks();
        tasks.Retrieve(ResourceTaskAttr.ResourceId,itemId,ResourceTaskAttr.IsPlan,1);
        List<ResourceTask> taskList=tasks.toList();
        JSONObject result=new JSONObject();
        result.put("resId",itemId);
        if (taskList.size()>0) {

            Collections.sort(taskList, new Comparator<ResourceTask>() {
                @Override
                public int compare(ResourceTask o1, ResourceTask o2) {
                    long temp = o1.GetValDateTime(ResourceTaskAttr.PlanStart).getTime() - o2.GetValDateTime(ResourceTaskAttr.PlanStart).getTime();
                    if (temp < 0)
                        return -1;
                    else if (temp > 0)
                        return 1;
                    return 0;
                }
            });
            int shiCha=8 * 60 * 60 * 1000;//时差，
            long start=taskList.get(0).GetValDateTime(ResourceTaskAttr.PlanStart).getTime()+shiCha;
            long end=taskList.get(taskList.size()-1).GetValDateTime(ResourceTaskAttr.PlanEnd).getTime()+shiCha;
            long day=24*60*60*1000;
            start=start/day*day;
            end=(end/day+1)*day;
            double[] loads=new double[Integer.valueOf((end-start)/day+"")+1];

            for (ResourceTask task : taskList) {
                long taskStart=task.GetValDateTime(ResourceTaskAttr.PlanStart).getTime()+shiCha;
                long taskEnd=task.GetValDateTime(ResourceTaskAttr.PlanEnd).getTime()+shiCha;
                int index=Integer.valueOf((taskStart-start)/day+"");
                int indexEnd=Integer.valueOf((taskEnd-start)/day+"");
                if (index==indexEnd){
                    loads[index]+=(taskEnd-taskStart+0.0d)/day;
                }else {
                    loads[index]+=(1-(taskStart%day+0.0d)/day);
                    index++;
                    while (index<indexEnd){
                        loads[index++]+=1;
                    }
                    loads[index]+=(taskEnd%day+0.0d)/day;
                }
            }

            List<JSONObject> data = new ArrayList<>(taskList.size());
            for (double score:loads){
                JSONObject temp=new JSONObject();
                temp.put("score",score);
                temp.put("time",start);
                start+=day;
                data.add(temp);
            }
            result.put("data",data);
        }

        return result;
    }

}
