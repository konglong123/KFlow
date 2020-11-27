package BP.WF.Template;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import BP.Sys.*;
import BP.WF.CCBPM_DType;
import BP.WF.Flow;
import BP.WF.Node;
import BP.springCloud.tool.FeignTool;

/** 
 
 
*/
public class TemplateGlo
{
	/** 
	 创建一个流程.
	 
	 @param flowSort 流程类别
	 @return string
	 * @throws Exception 
	*/
	public static String NewFlow(String flowSort, String flowName, BP.WF.Template.DataStoreModel dsm, String ptable, String flowMark, String flowVer) throws Exception
	{
		//执行保存.
		BP.WF.Flow fl = new BP.WF.Flow();
		//修改类型为CCBPMN
		//fl.DType = string.IsNullOrEmpty(flowVer) ? 1 : Int32.Parse(flowVer);

		fl.setDType(CCBPM_DType.CCBPM.getValue());
		dsm = DataStoreModel.forValue(0);
		String flowNo = fl.DoNewFlow(flowSort, flowName, dsm, ptable, flowMark);
		fl.setNo(flowNo);

		/*//创建连线
		Direction drToNode = new Direction();
		drToNode.setFK_Flow(flowNo);
		drToNode.setNode(Integer.parseInt(Integer.parseInt(flowNo) + "01"));
		drToNode.setToNode(Integer.parseInt(Integer.parseInt(flowNo) + "02"));
		drToNode.Insert();*/

		return flowNo;
	}

	/** 
	 创建一个节点
	 
	 @param flowNo
	 @param x
	 @param y
	 @return 
	 * @throws Exception 
	*/
	public static int NewNode(String flowNo, int x, int y) throws Exception
	{
		BP.WF.Flow fl = new Flow(flowNo);
		BP.WF.Node nd = fl.DoNewNode(x, y);
		return nd.getNodeID();
	}



	/**
	*@Description: 复制一个节点（复制节点所有基本属性，节点对应表单）
	*@Param:
	*@return:
	*@Author: Mr.kong
	*@Date: 2020/4/5
	*/
	public static Node CopyNode(String flowNo,Node node) throws Exception{
		Node nodeNew=new Node();
		//复制基本属性
		String nodeId=FeignTool.getSerialNumber("BP.WF.Node")+"";
		nodeNew.setRow(node.getRow());
		nodeNew.setFK_Flow(flowNo);
		nodeNew.setNodeID(Integer.valueOf(nodeId));
		nodeNew.Insert();

		String mapDataKeyOld="ND"+node.getNodeID();
		String mapDateKeyNew="ND"+nodeNew.getNodeID();

		//复制表单信息
		MapData mapData=new MapData();
		mapData.Retrieve(MapDataAttr.No,mapDataKeyOld);
		mapData.setNo(mapDateKeyNew);
		mapData.Insert();

		//复制表单中分组信息，
		Map<String,Long> groupFieldMap=new HashMap<>();
		GroupFields groupFields=new GroupFields();
		groupFields.Retrieve(GroupFieldAttr.FrmID,mapDataKeyOld);
		List<GroupField> groupFieldList=groupFields.toList();
		for (GroupField groupField:groupFieldList){
			groupField.setFrmID(mapDateKeyNew);
			Long oldOid=groupField.getOID();
			groupField.setOID(0);//系统重新产生OID
			groupField.Insert();
			groupFieldMap.put(oldOid+"",groupField.getOID());
		}

		try {
			//复制表单中自定义字段信息
			MapAttrs mapAttrs=new MapAttrs();
			mapAttrs.Retrieve(MapAttrAttr.FK_MapData,mapDataKeyOld);
			List<MapAttr> mapAttrList=mapAttrs.toList();
			for (MapAttr mapAttr:mapAttrList){
				mapAttr.setFK_MapData(mapDateKeyNew);
				Long groupId=groupFieldMap.get(mapAttr.getGroupID()+"");
				if (groupId!=null&&groupId!=0){
					mapAttr.setGroupID(groupId);
				}
				String mapAttrKey=mapAttr.getMyPK();
				mapAttr.setMyPK(mapDateKeyNew+mapAttrKey.substring(mapDataKeyOld.length()));
				mapAttr.Insert();
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return nodeNew;
	}


	/** 
	 删除节点.
	 
	 @param nodeid
	 * @throws Exception 
	*/
	public static void DeleteNode(int nodeid) throws Exception
	{
		BP.WF.Node nd = new Node(nodeid);
		nd.Delete();
	}
	  
}