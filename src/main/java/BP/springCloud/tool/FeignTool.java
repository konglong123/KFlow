package BP.springCloud.tool;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: kflow-web
 * @description:数据同步到ES中
 * @author: Mr.Kong
 * @create: 2019-12-29 10:55
 **/
public class FeignTool {

    public static RestTemplate template=new RestTemplate();

    /**
    *@Description: 同步workflow信息到Es中
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2019/12/29
    */
    public static Boolean updateToES(String url,Map<String,Object> postBody){
        JSONObject jsonObject=(JSONObject)callFeign(url,postBody);
        return jsonObject.getBoolean("success");
    }


    /**
    *@Description:  请求序列号服务，获取主键
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/2/25
    */
    public static Long getSerialNumber(String className){
        String url="http://112.125.90.132:8082/sequence/getSequence";
        Map<String,Object> postBody=new HashMap<>(2);
        postBody.put("className",className);
        JSONObject result=(JSONObject) callFeign(url,postBody);
        if (result.getBoolean("success")){
            return result.getLong("seq");
        }
        return -1L;
    }

    private static Object callFeign(String url,Map<String,Object> postBody){
        try{
            HttpEntity<Map> requestEntity = new HttpEntity<>(postBody, null);
            ResponseEntity<JSONObject> resTemp = template.postForEntity(url, requestEntity, JSONObject.class);
            return resTemp.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *@Description: 请求url获取es分页返回结果（均通过对abstracts进行相似度计算）
     *@Param:
     *@return:
     *@Author: Mr.kong
     *@Date: 2020/2/23
     */
    public static void esQuery(String url, HttpServletRequest request, HttpServletResponse response) throws Exception{

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
            ResponseEntity<Page> resTemp = FeignTool.template.postForEntity(url, requestEntity, Page.class);
            Page pageResult=resTemp.getBody();
            Map<String, Object> jsonMap = new HashMap<>();//定义map
            jsonMap.put("total", pageResult.getTotalNums());//total键 存放总记录数，必须的
            if (pageResult.getData()==null){
                jsonMap.put("rows", new ArrayList<>());//消除查询结果为空时，前端报错
            }else {
                jsonMap.put("rows", pageResult.getData());//rows键 存放每页记录 list
            }
            String result = net.sf.json.JSONObject.fromObject(jsonMap).toString();//格式化result   一定要是JSONObject
            out.print(result);
            out.flush();
            out.close();

    }

    //发送手机短息
    public static String sendPhoneMessage(String mobile,String message){
        String host = "http://yzxyzm.market.alicloudapi.com";
        String path = "/yzx/verifySms";
        String method = "POST";
        String appcode = "2b47ef9f8e9c4dd89129440aafc0fd82";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("phone", mobile);
        querys.put("templateId", "TP1803086");
        querys.put("variable", "code:123456");
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
