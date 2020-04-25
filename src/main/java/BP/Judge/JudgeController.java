package BP.Judge;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-04-25 11:05
 **/
@Controller
@RequestMapping("judge")
public class JudgeController {

    /**
    *@Description: 条件查找决策bean
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/25
    */
    @RequestMapping("findJudgeCondition")
    public JSONObject findJudgeCondition(HttpServletRequest request, HttpServletResponse response){
        return null;
    }


}
