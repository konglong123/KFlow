package BP.Task;

import BP.Tools.StringUtils;
import BP.springCloud.entity.GenerFlow;
import BP.springCloud.tool.PageTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-04-05 20:50
 **/
@Controller
@RequestMapping("generFlow")
public class GenerFlowController {

    private final Logger logger = LoggerFactory.getLogger(GenerFlowController.class);

    @Resource
    private GenerFlowService generFlowService;

    /**
    *@Description: 分页条件查询流程实例
    *@Param:  
    *@return:  
    *@Author: Mr.kong
    *@Date: 2020/4/5 
    */
    @RequestMapping("getGenerFlowList")
    public void getGenerFlowList(HttpServletRequest request, HttpServletResponse response){
        try {
            GenerFlow con=new GenerFlow();
            String generFlowNo=request.getParameter("generFlowNo");
            if (!StringUtils.isEmpty(generFlowNo))
                con.setNo(Long.parseLong(generFlowNo));

            String flowNo=request.getParameter("flowNo");
            if (!StringUtils.isEmpty(flowNo))
                con.setFlowId(Integer.parseInt(flowNo));

            String workId=request.getParameter("workId");
            if (!StringUtils.isEmpty(workId))
                con.setWorkId(Long.parseLong(workId));

            String workGroupId=request.getParameter("workGroupId");
            if (!StringUtils.isEmpty(workGroupId))
                con.setWorkGroupId(Long.parseLong(workGroupId));

            String parentWorkId=request.getParameter("parentWorkId");
            if (!StringUtils.isEmpty(parentWorkId))
                con.setParentWorkId(Long.parseLong(parentWorkId));

            String creator=request.getParameter("creator");
            if (!StringUtils.isEmpty(creator))
                con.setCreator(creator);

            String yn=request.getParameter("yn");
            if (!StringUtils.isEmpty(yn))
                con.setYn(Integer.parseInt(yn));

            List generFlowList=generFlowService.findGenerFlowList(con);
            PageTool.TransToResultList(generFlowList,request,response);
        }catch (Exception e){
            logger.error(e.getMessage());
        }

    }

    @RequestMapping("getGenerFlowByNo")
    @ResponseBody
    public GenerFlow getGenerFlowByNo(Long no){
        try {
            GenerFlow generFlow=generFlowService.getGenerFlow(no);
            return generFlow;
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
    *@Description:  返回generFlowNo的实时进展数据，并封装成Gant可以读取的json格式
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/4/6
    */
    @RequestMapping("getGantData")
    @ResponseBody
    public JSONObject getGantData(Long generFlowNo,int depth){
        JSONObject result=generFlowService.getGantData(generFlowNo,depth);
        return result;
    }
}
