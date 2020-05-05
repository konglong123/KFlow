package BP.springCloud.controller;

import BP.En.Attr;
import BP.En.EntityOIDAttr;
import BP.Sys.*;
import BP.springCloud.tool.PageTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-05-05 11:43
 **/
@Controller
@RequestMapping("form")
public class FormController {

    private final Logger logger = LoggerFactory.getLogger(FormController.class);

    /**
    *@Description:查询节点表单所有属性，（默认属性需要排除）
    *@Param:  nodeId,节点id
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/5/5
    */
    @RequestMapping("getNodeFormAttrs")
    @ResponseBody
    public void getNodeFormAttrs(HttpServletRequest request, HttpServletResponse response){
        String nodeId=request.getParameter("nodeId");
        try {
            MapAttrs mapAttrs=new MapAttrs();
            mapAttrs.Retrieve(MapAttrAttr.FK_MapData,"ND"+nodeId);
            PageTool.TransToResult(mapAttrs,request,response);
        }catch (Exception e){
            logger.error(e.getMessage());
        }

    }

    /**
    *@Description: 引用节点表单
    *@Param:  type=1整体引用，type=2同流程部分引用，type=3不同流程部分引用
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/5/5
    */
    @RequestMapping("referNodeForm")
    public void referNodeForm(HttpServletRequest request, HttpServletResponse response){
        String type=request.getParameter("type");
        String curNodeId=request.getParameter("curNodeId");
        String nodeId=request.getParameter("nodeId");
        String fkMapData="ND"+curNodeId;

        try{
            //节点表单整体引用
            if (type.equals("1")){
                //当前节点表单字段
                MapAttrs curAttrs=new MapAttrs();
                curAttrs.Retrieve(MapAttrAttr.FK_MapData,"ND"+curNodeId);
                Set curSet=new HashSet();
                List<MapAttr> list=curAttrs.toList();
                for (MapAttr attr:list){
                    curSet.add(attr.getKeyOfEn());
                }

                //处理引用节点字段
                MapAttrs mapAttrs=new MapAttrs();
                mapAttrs.Retrieve(MapAttrAttr.FK_MapData,"ND"+nodeId);
                List<MapAttr> referList=mapAttrs.toList();
                Map<Integer,Integer> referGroup=new HashMap<>();//记录已经复制的GroupField
                for (MapAttr attr:referList){
                    if (curSet.contains(attr.getKeyOfEn()))
                        continue;//过滤重复字段

                    //判断分组是否需要复制
                    if (!referGroup.containsKey(attr.getGroupID())){
                        GroupField groupField=new GroupField(attr.getGroupID());
                        groupField.setFrmID(fkMapData);
                        groupField.setOID(0);
                        groupField.Insert();
                        referGroup.put(attr.getGroupID(),groupField.GetValIntByKey(EntityOIDAttr.OID));
                    }
                    MapAttr newAttr=new MapAttr();
                    newAttr.setRow(attr.getRow());
                    newAttr.setFK_MapData(fkMapData);
                    newAttr.setGroupID(referGroup.get(newAttr.getGroupID()));
                    newAttr.SetValByKey(MapAttrAttr.IsReferOut,2);//同流程表单属性
                    if (StringUtils.isEmpty(attr.getReferNodeId())){
                        newAttr.setReferNodeID("ND"+nodeId);
                    }
                    newAttr.setUIIsEnable(false);//不可编辑
                    newAttr.Insert();
                }

            }else if (type.equals("2")){//节点表单部分引用
                String attrNos=request.getParameter("attrNos");
                String[] attrs=attrNos.split(",");
                if (attrs.length>1){
                    GroupField groupField=new GroupField();
                    groupField.setFrmID(fkMapData);
                    groupField.setOID(0);
                    groupField.setLab("引用节点");
                    groupField.Insert();

                    for (String no:attrs){
                        MapAttr attr=new MapAttr(no);
                        MapAttr newAttr=new MapAttr();
                        newAttr.setRow(attr.getRow());
                        newAttr.setFK_MapData(fkMapData);
                        newAttr.setGroupID(groupField.getOID());
                        newAttr.SetValByKey(MapAttrAttr.IsReferOut,2);//同流程表单属性
                        if (StringUtils.isEmpty(attr.getReferNodeId())){
                            newAttr.setReferNodeID("ND"+nodeId);
                        }
                        newAttr.setUIIsEnable(false);//不可编辑
                        newAttr.Insert();
                    }
                }

            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }

    }

     /**
     *@Description:  父子流程间表单属性引用
     *@Param:
     *@return:
     *@Author: Mr.kong
     *@Date: 2020/5/5
     */
     @RequestMapping("referFromParent")
     @ResponseBody
     public void referFromParent(HttpServletRequest request, HttpServletResponse response){

     }

     /**
     *@Description: 条件查询表单字段
     *@Param:
     *@return:
     *@Author: Mr.kong
     *@Date: 2020/5/5
     */
     @RequestMapping("getMapAttrByCondition")
     @ResponseBody
     public void getMapAttrByCondition(HttpServletRequest request, HttpServletResponse response){

     }
}
