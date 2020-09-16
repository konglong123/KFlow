package BP.springCloud.controller;

import BP.Ga.GeneticAth;
import BP.NodeGroup.*;
import BP.springCloud.tool.PageTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-09-16 20:17
 **/
@Controller
@RequestMapping("composeGroup")
public class ComposeGroupController {

    private final Logger logger = LoggerFactory.getLogger(ComposeGroupController.class);

    /**
    *@Description: 获取flow下所有历史组合
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/9/16
    */
    @RequestMapping("getComposeGroups")
    @ResponseBody
    public void getComposeGroups(HttpServletRequest request, HttpServletResponse response){
        String flowNo=request.getParameter("flowNo");
        try {
            ComposeGroups groups=new ComposeGroups();
            groups.Retrieve(ComposeGroupAttr.flowNo,flowNo);
            PageTool.TransToResult(groups,request,response);
        }catch (Exception e){
            logger.error(e.getMessage());
        }

    }

    /**
    *@Description:  推荐分组组合
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/9/16
    */
    @RequestMapping("composeNodeGroup")
    @ResponseBody
    public com.alibaba.fastjson.JSONObject composeNodeGroup(@RequestBody ComposeGroup composeGroup) {
        try {
            GeneticAth geneticAth = new GeneticAth(composeGroup.getFlowNo());
            com.alibaba.fastjson.JSONObject data = geneticAth.run();
            return data;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return null;
    }


    /**
     * @Description: 初始化分组摘要信息（所有节点进行拼装）
     * @Param:
     * @return:
     * @Author: Mr.kong
     * @Date: 2020/9/16
     */
    @RequestMapping("initNodeGroup")
    @ResponseBody
    public com.alibaba.fastjson.JSONObject initNodeGroup() {
        try {
            NodeGroups groups=new NodeGroups();
            groups.RetrieveAll();
            List<NodeGroup> groupList=groups.toList();
            for (NodeGroup group:groupList){
                //只初始化没有进行总结的分组
                if (StringUtils.isEmpty(group.GetValStrByKey(NodeGroupAttr.abstracts))) {
                    NodeGroupItems items = new NodeGroupItems();
                    items.Retrieve(NodeGroupItemAttr.group_no, group.getNo());
                    List<NodeGroupItem> itemList = items.toList();
                    StringBuilder sb = new StringBuilder();
                    for (NodeGroupItem item : itemList) {
                        sb.append(item.GetValStrByKey(NodeGroupItemAttr.node_name));
                        sb.append("，");
                    }
                    group.SetValByKey(NodeGroupAttr.abstracts, sb.toString());
                    group.Update();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
