package BP.springCloud;


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
            response.setCharacterEncoding("utf-8");
            request.setCharacterEncoding("utf-8");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            //请求页码、每页显示行数、偏移、总数
            int page,rows;
            String input_page=request.getParameter("page");
            page=(input_page==null)?1:Integer.parseInt(input_page);
            String input_rows=request.getParameter("rows");
            rows=(input_rows==null)?10:Integer.parseInt(input_rows);
            String abstracts=request.getParameter("abstracts");
            Map<String, Object> postBody = new HashMap<>();
            postBody.put("startPoint", (page-1)*rows);
            postBody.put("pageLength", rows);
            postBody.put("abstracts", abstracts);
            HttpEntity<Map> requestEntity = new HttpEntity<>(postBody, null);
            ResponseEntity<Page> resTemp = FeignTool.template.postForEntity("http://112.125.90.132:8082/es/getWFDsl", requestEntity, Page.class);
            Page pageResult=resTemp.getBody();
            Map<String, Object> jsonMap = new HashMap<>();//定义map
            jsonMap.put("total", pageResult.getTotalNums());//total键 存放总记录数，必须的
            if (pageResult.getData()==null){
                jsonMap.put("rows", new ArrayList<>());//消除查询结果为空时，前端报错
            }else {
                jsonMap.put("rows", pageResult.getData());//rows键 存放每页记录 list
            }
            String result = JSONObject.fromObject(jsonMap).toString();//格式化result   一定要是JSONObject
            out.print(result);
            out.flush();
            out.close();
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
}
