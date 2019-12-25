package BP.springCloud;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import java.util.List;

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
        String result=template.getForEntity("http://112.125.90.132:8082/es/queryWorkflow?abstracts="+abstracts+"&id="+id,String.class).getBody();
        return result;
    }

    @RequestMapping("/getWF")
    @ResponseBody
    public List<WorkFlowES> queryWorkflow(String abstracts){
        List<WorkFlowES> list=template.getForEntity("http://112.125.90.132:8082/es/queryWorkflow?abstracts="+abstracts,List.class).getBody();
        return list;
    }
}
