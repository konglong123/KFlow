package BP.springCloud.controller;


import BP.Resource.ResourceAttr;
import BP.Resource.Resources;
import BP.springCloud.tool.FeignTool;
import BP.springCloud.tool.Page;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
                    out.print(result);
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



}
