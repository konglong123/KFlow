package BP.springCloud;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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
        return (Boolean) callFeign(url,postBody);
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
            return resTemp.getBody().getBoolean("success");
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
