package BP.springCloud.controller;

import BP.DA.DataTable;
import BP.Resource.ResourceService;
import BP.Resource.ResourceTaskAttr;
import BP.Resource.ResourceTasks;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
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
            Long noodId=Long.parseLong(request.getParameter("nodeId"));
            resourceTasks.RetrieveByAttr(ResourceTaskAttr.NodeId,noodId);
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
    *@Description:  查询资源对使用情况（）
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/2/26 
    */
    @RequestMapping("/getResourceLoad")
    @ResponseBody
    public Object getResourceLoad(HttpServletRequest request, HttpServletResponse response){
        String resourceNo=request.getParameter("resourceNo");
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        if (resourceNo!=null) {
            return resourceService.getResourceLoadForHighcharts(resourceNo,startTime,endTime);
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
    public Object bookResource(HttpServletRequest request, HttpServletResponse response){
        String resourceNo=request.getParameter("resourceNo");
        String nodeId=request.getParameter("nodeId");
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        String useTime=request.getParameter("useTime");
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date start = sdf.parse(startTime);
            Date end=sdf.parse(endTime);
            resourceService.bookResource(resourceNo,Long.parseLong(nodeId),start,end,Integer.parseInt(useTime));
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


}
