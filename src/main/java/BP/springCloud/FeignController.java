package BP.springCloud;

import com.alibaba.druid.support.json.JSONUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import io.micrometer.core.instrument.util.JsonUtils;
import net.sf.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
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

    private static RestTemplate template=new RestTemplate();

    @RequestMapping("/addWF")
    @ResponseBody
    public String add(Long id,String abstracts){
        String result=template.getForEntity("http://112.125.90.132:8082/es/addWorkflow?abstracts="+abstracts+"&id="+id,String.class).getBody();
        return result;
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
            ResponseEntity<Page> resTemp = template.postForEntity("http://112.125.90.132:8082/es/getWFDsl", requestEntity, Page.class);
            Page pageResult=resTemp.getBody();
            Map<String, Object> jsonMap = new HashMap<>();//定义map
            jsonMap.put("total", pageResult.getTotalNums());//total键 存放总记录数，必须的
            jsonMap.put("rows", pageResult.getData());//rows键 存放每页记录 list
            String result = JSONObject.fromObject(jsonMap).toString();//格式化result   一定要是JSONObject
            out.print(result);
            out.flush();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
    *@Description: RestTemplate属性设置
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2019/12/26 
    */
    private void setTemplate() throws Exception{
        // 解决(响应数据可能)中文乱码的问题
        List<HttpMessageConverter<?>> converterList = template.getMessageConverters();
        converterList.remove(1); // 移除原来的转换器
        // 设置字符编码为utf-8
        HttpMessageConverter<?> converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converterList.add(1, converter); // 添加新的转换器(注:convert顺序错误会导致失败)
        template.setMessageConverters(converterList);

        String httpBody = null;
        HttpEntity<String> httpEntity = new HttpEntity<String>(httpBody, null);
        URI uri = URI.create("http://112.125.90.132:8082/es/getWF");

        //  -------------------------------> 执行请求并返回结果
        // 此处的泛型  对应 响应体数据   类型;即:这里指定响应体的数据装配为String
        ResponseEntity<String> response =
                template.exchange(uri, HttpMethod.GET, httpEntity, String.class);

        // -------------------------------> 响应信息
        //响应码,如:401、302、404、500、200等
        System.err.println(response.getStatusCodeValue());
        Gson gson = new Gson();
        // 响应头
        System.err.println(gson.toJson(response.getHeaders()));
        // 响应体
        if(response.hasBody()) {
            System.err.println(response.getBody());
        }
    }
}
