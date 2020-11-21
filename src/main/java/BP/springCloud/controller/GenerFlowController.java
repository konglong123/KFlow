package BP.springCloud.controller;

import BP.Task.GenerFlowService;
import BP.Task.NodeTask;
import BP.Task.NodeTaskService;
import BP.Tools.StringUtils;
import BP.springCloud.entity.GenerFlow;
import BP.springCloud.entity.NodeTaskM;
import BP.springCloud.tool.PageTool;
import com.alibaba.fastjson.JSONObject;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Comparator;
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

    @Resource
    private NodeTaskService nodeTaskService;

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

            String status=request.getParameter("status");
            if (!StringUtils.isEmpty(status))
                con.setStatus(Integer.parseInt(status));

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
    *@Description: 查询多个流程实例的完成进度
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/9/3
    */
    @RequestMapping("getDataForGeners")
    @ResponseBody
    public JSONObject getDataForGeners(@RequestBody GenerFlow generFlow){
        try {
            List<GenerFlow> list = generFlowService.findGenerFlowList(generFlow);
            JSONObject data = generFlowService.getShowData(list);
            return data;
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     *@Description: 查询单个流程实例的各状态节点任务
     *@Param:
     *@return:
     *@Author: Mr.kong
     *@Date: 2020/9/3
     */
    @RequestMapping("getDataForOneGener")
    @ResponseBody
    public JSONObject getDataForOneGener(@RequestBody GenerFlow generFlow){
        JSONObject data=new JSONObject();
        if (generFlow.getNo()==null)
            return data;
        NodeTaskM con=new NodeTaskM();
        con.setWorkId(generFlow.getNo()+"");
        List<NodeTaskM> list=nodeTaskService.findNodeTaskAllList(con);

        //每种状态的任务
        List<NodeTaskM>[] items=new List[9];
        for (int i=0;i<items.length;i++){
            items[i]=new ArrayList();
        }
        for (NodeTaskM nodeTaskM:list){
            int status=nodeTaskM.getStatus();
            List<NodeTaskM> item;
            if (status==20){
                item=items[0];
            }else {
                item=items[status];
            }
            item.add(nodeTaskM);
        }

        String[] names={"未准备","可以开始","已经开始","已经完成","逾期开始","警告开始","正常","逾期结束","警告结束"};
        List<net.sf.json.JSONObject> pieData=new ArrayList<>();
        for (int i=0;i<names.length;i++){
            net.sf.json.JSONObject temp=new net.sf.json.JSONObject();
            temp.put("name",names[i]);
            temp.put("value",items[i].size());
            if (i==0)
                temp.put("status",20);
            else
                temp.put("status",i);
            pieData.add(temp);
        }
        data.put("pieData", JSONArray.fromObject(pieData));

        return data;
    }

}
