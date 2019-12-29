package BP.springCloud;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
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
    public static Boolean updateToES(Map<String,Object> postBody){
        try{
            HttpEntity<Map> requestEntity = new HttpEntity<>(postBody, null);
            ResponseEntity<Boolean> resTemp = template.postForEntity("http://112.125.90.132:8082/es/addWorkflow", requestEntity, Boolean.class);
            Boolean pageResult=resTemp.getBody();
            return pageResult;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
