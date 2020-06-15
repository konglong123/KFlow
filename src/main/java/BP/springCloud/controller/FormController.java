package BP.springCloud.controller;

import BP.En.EntityOIDAttr;
import BP.En.UIContralType;
import BP.Sys.*;
import BP.WF.Node;
import BP.springCloud.tool.PageTool;
import com.alibaba.fastjson.JSONObject;
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
        int referNode=Integer.parseInt(nodeId);

        try{
            //节点表单整体引用
            if (type.equals("1")){
                //处理引用节点字段
                MapAttrs mapAttrs=new MapAttrs();
                mapAttrs.Retrieve(MapAttrAttr.FK_MapData,"ND"+nodeId);
                List<MapAttr> referList=mapAttrs.toList();
                Map<Integer,Integer> referGroup=new HashMap<>();//记录已经复制的GroupField
                for (MapAttr attr:referList){

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
                        newAttr.setReferNodeID(referNode);
                    }
                    newAttr.setUIIsEnable(false);//不可编辑
                    newAttr.Insert();
                }

            }else if (type.equals("2")){//节点表单部分引用
                String attrNos=request.getParameter("attrNos");
                String[] attrs=attrNos.split(",");
                if (attrs.length>0){
                    GroupField groupField=new GroupField();
                    groupField.setFrmID(fkMapData);
                    groupField.setOID(0);
                    groupField.setLab("引用节点");
                    groupField.Insert();

                    for (String no:attrs){
                        MapAttr attr=new MapAttr(no);
                        MapAttr newAttr=new MapAttr();
                        //附件类型，需要增加frmAttachment数据
                        if (attr.getUIContralType()== UIContralType.AthShow){
                            FrmAttachment attachment=new FrmAttachment(no);
                            FrmAttachment newAttachment=new FrmAttachment();
                            newAttachment.setRow(attachment.getRow());
                            newAttachment.setFK_MapData(fkMapData);
                            newAttachment.Insert();
                        }

                        newAttr.setRow(attr.getRow());
                        newAttr.setFK_MapData(fkMapData);
                        newAttr.setGroupID(groupField.getOID());
                        newAttr.SetValByKey(MapAttrAttr.IsReferOut,2);//同流程表单属性
                        if (StringUtils.isEmpty(attr.getReferNodeId())){
                            newAttr.setReferNodeID(referNode);
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
     public JSONObject referFromParent(HttpServletRequest request, HttpServletResponse response){
         String nodeId=request.getParameter("nodeId");
         String curNodeId=request.getParameter("curNodeId");
         String typeStr=request.getParameter("type");
         String attrNos=request.getParameter("attrNos");
         int type=Integer.parseInt(typeStr);
         String[] attrs=attrNos.split(",");
         JSONObject result=new JSONObject();

         if (!checkRefer(curNodeId,nodeId,type)){
             result.put("success",false);
             result.put("message","不满足节点表单引用关系！");
             return result;
         }

         try{
             if (attrs.length>0){
                 String fkMapData="ND"+curNodeId;
                 GroupField groupField=new GroupField();
                 groupField.setFrmID(fkMapData);
                 groupField.setOID(0);
                 groupField.setLab("引用节点");
                 groupField.Insert();

                 int referNode=Integer.parseInt(nodeId);
                 for (String no:attrs){
                     MapAttr attr=new MapAttr(no);
                     MapAttr newAttr=new MapAttr();
                     newAttr.setRow(attr.getRow());
                     newAttr.setFK_MapData(fkMapData);
                     newAttr.setGroupID(groupField.getOID());
                     newAttr.setIsReferOut(type);
                     if (attr.getReferNodeId()==0){
                         newAttr.setReferNodeID(referNode);
                     }
                     newAttr.setUIIsEnable(false);//不可编辑
                     newAttr.Insert();

                     //附件类型，需要增加frmAttachment数据
                     if (attr.getUIContralType()== UIContralType.AthShow){
                         FrmAttachment attachment=new FrmAttachment(no);
                         FrmAttachment newAttachment=new FrmAttachment();
                         newAttachment.setRow(attachment.getRow());
                         newAttachment.setFK_MapData(fkMapData);
                         newAttachment.Insert();
                     }
                 }
             }
             result.put("success",true);
         }catch (Exception e){
             logger.error(e.getMessage());
         }
         return result;
     }

     //检验curNode是否允许引用nodeId
    //判断能否允许引用（引用父时，父中是否包含该流程，，引用子时，该流程是否包含该子流程）
    private boolean checkRefer(String curNodeId,String nodeId,int type){
         try {
             Node curNode=new Node(curNodeId);
             Node node=new Node(nodeId);
             String[] subFlowNos=null;
             String flowNo=null;
             if (type==1){//引用父work
                 flowNo=curNode.getFK_Flow();
                 subFlowNos=node.getSubFlowNos();
             }else{//引用子work
                 flowNo=node.getFK_Flow();
                 subFlowNos=curNode.getSubFlowNos();
             }
             if (subFlowNos!=null){
                 for (String temp:subFlowNos){
                     if (temp.equals(flowNo))
                         return true;
                 }
             }
         }catch (Exception e){
             logger.error(e.getMessage());
             return false;
         }
         return false;
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
         try {
             String nodeId=request.getParameter("nodeId");
             String keyOfEn=request.getParameter("keyOfEn");
             MapAttrs attrs=new MapAttrs();
             boolean flag1=StringUtils.isEmpty(nodeId);
             boolean flag2=StringUtils.isEmpty(keyOfEn);
             if (!flag1&&!flag2){
                 attrs.Retrieve(MapAttrAttr.FK_MapData,"ND"+nodeId,MapAttrAttr.KeyOfEn,keyOfEn);
             }else if (!flag1){
                 attrs.Retrieve(MapAttrAttr.FK_MapData,"ND"+nodeId);
             }else if (!flag2){
                 attrs.Retrieve(MapAttrAttr.KeyOfEn,keyOfEn);
             }
             if (!attrs.isEmpty()){
                 PageTool.TransToResult(attrs,request,response);
             }
         }catch (Exception e){
             logger.error(e.getMessage());
         }


     }
}
