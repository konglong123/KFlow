package BP.NodeGroup;

import BP.En.EntityNo;
import BP.En.Map;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.Template.Direction;
import BP.WF.Template.DirectionAttr;
import BP.WF.Template.Directions;
import BP.springCloud.tool.FeignTool;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-09-14 11:18
 **/
public class NodeGroup extends EntityNo{

    public NodeGroup(){

    }

    public NodeGroup(String _no) throws Exception{
        if (_no == null || _no.equals(""))
        {
            throw new RuntimeException(this.getEnDesc() + "@对表["
                    + this.getEnDesc() + "]进行查询前必须指定编号。");
        }

        this.setNo(_no);
        if (this.Retrieve() == 0)
        {
            throw new RuntimeException("@没有"
                    + this.get_enMap().getPhysicsTable() + ", No = " + getNo()
                    + "的记录。");
        }
    }
    @Override
    public Map getEnMap() {
        if (this.get_enMap() != null)
        {
            return this.get_enMap();
        }

        Map map = new Map("k_node_group", "分组");
        map.AddTBStringPK(NodeGroupAttr.No, null, "编号", true, true,1, 40, 100);
        map.AddTBString(NodeGroupAttr.name, null, "分组名", true, false, 1, 40, 100);
        map.AddTBString(NodeGroupAttr.flow_no, null, "流程编码", true, true, 0, 100, 100);
        map.AddTBString(NodeGroupAttr.flow_name, null, "流程名", true, true, 0, 100, 100);
        map.AddTBInt(NodeGroupAttr.nodeNum, 0, "节点数", true, true);
        map.AddTBString(NodeGroupAttr.inNodeNo, null, "入口节点编码", true, false, 0, 100, 100);
        map.AddTBString(NodeGroupAttr.outNodeNo, null, "出口节点编码", true, false, 0, 100, 100);
        map.AddTBInt(NodeGroupAttr.sumTime, 0, "总工时", true, true);

        map.AddDDLSysEnum(NodeGroupAttr.type, 2, "类型", true, true, NodeGroupAttr.type,
                "@1=可调换分组@2=模块分组");
        map.AddTBString(NodeGroupAttr.detail, null, "详情", true, false, 0, 100, 100);
        map.AddTBDecimal(NodeGroupAttr.score, 0d, "相似度值", true, false);
        map.AddTBStringDoc(NodeGroupAttr.abstracts, null, "概要", true, false);

        map.AddTBDateTime(NodeGroupAttr.create_time,"2000-01-01 00:00:00","创建时间",true,true);
        map.AddTBDateTime(NodeGroupAttr.update_time,"2000-01-01 00:00:00","更新时间",true,true);

        this.set_enMap(map);
        return this.get_enMap();
    }

    @Override
    protected boolean beforeInsert() throws Exception {
        Long id= FeignTool.getSerialNumber("BP.NodeGroup.NodeGroupItem");
        this.setNo(id+"");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date=new Date();
        this.SetValByKey(NodeGroupAttr.create_time,sdf.format(date));
        return super.beforeInsert();
    }

    @Override
    protected boolean beforeDelete() throws Exception {
        String groupNo=this.getNo();
        NodeGroupItems items=new NodeGroupItems();
        items.Delete(NodeGroupItemAttr.group_no,groupNo);
        return super.beforeInsert();
    }

    public static String[] getItems(String groupNo) throws Exception{
        NodeGroupItems items=new NodeGroupItems();
        items.Retrieve(NodeGroupItemAttr.group_no,groupNo);
        List<NodeGroupItem> itemList=items.toList();
        String[] nodeNos=new String[itemList.size()];
        int index=0;
        for (NodeGroupItem item:itemList) {
            nodeNos[index]=item.GetValStrByKey(NodeGroupItemAttr.node_no);
            index++;
        }
        return nodeNos;
    }

    public void initInfo() throws Exception{
        NodeGroup group=this;
        NodeGroupItems items = new NodeGroupItems();
        items.Retrieve(NodeGroupItemAttr.group_no, group.getNo());
        List<NodeGroupItem> itemList = items.toList();
        //模块分组
        if (group.GetValIntByKey(NodeGroupAttr.type)!=1){
            //只初始化没有进行摘要总结的分组
            if (StringUtils.isEmpty(group.GetValStrByKey(NodeGroupAttr.abstracts))) {
                StringBuilder sb = new StringBuilder();
                String flowName = group.GetValStrByKey(NodeGroupAttr.flow_name);
                if (!StringUtils.isEmpty(flowName))
                    sb.append(flowName);
                for (NodeGroupItem item : itemList) {
                    sb.append(item.GetValStrByKey(NodeGroupItemAttr.node_name));
                    sb.append("，");
                }
                group.SetValByKey(NodeGroupAttr.abstracts, sb.toString());
            }

        }
        //检查模块是否符合“一输入、一输出结构”
        if (!initInOutNode()){
            group.SetValByKey(NodeGroupAttr.type,0);
        }else {
            group.SetValByKey(NodeGroupAttr.type,2);
        }
        //更新总工时、节点数
        group.SetValByKey(NodeGroupAttr.nodeNum,itemList.size());
        int sumTime=0;
        for (NodeGroupItem item:itemList) {
            Node node=new Node(item.GetValStrByKey(NodeGroupItemAttr.node_no));
            sumTime += node.getDoc();
        }
        //更新流程名
        String flowNo=this.GetValStrByKey(NodeGroupAttr.flow_no);
        Flow flow=new Flow(flowNo);
        this.SetValByKey(NodeGroupAttr.flow_name,flow.getName());
        group.SetValByKey(NodeGroupAttr.sumTime,sumTime);
        group.Update();
    }

    //初始化模块的输入输出节点编码
    public boolean initInOutNode() throws Exception{
            NodeGroup group=this;
            NodeGroupItems items = new NodeGroupItems();
            items.Retrieve(NodeGroupItemAttr.group_no, group.getNo());
            java.util.Map<String,int[]> map=new HashMap<>();//int[0]进度，int[1]出度
            List<NodeGroupItem> itemList=items.toList();
            for (NodeGroupItem item:itemList){
                map.put(item.GetValStrByKey(NodeGroupItemAttr.node_no),new int[2]);
            }

            for (NodeGroupItem item:itemList){
                String nodeId=item.GetValStrByKey(NodeGroupItemAttr.node_no);
                Directions directions=new Directions();
                directions.Retrieve(DirectionAttr.ToNode,nodeId);
                List<Direction> list=directions.toList();
                for (Direction direction:list){
                    int[] mark=map.get(nodeId);
                    if (map.containsKey(direction.getNode()+"")){
                        //进度加1
                        mark[0]++;
                    }
                }

                directions.Retrieve(DirectionAttr.Node,nodeId);
                list=directions.toList();
                for (Direction direction:list){
                    int[] mark=map.get(nodeId);
                    if (map.containsKey(direction.getToNode()+"")){
                        //出度加1
                        mark[1]++;
                    }
                }
            }
            String in0="";
            String out0="";
            //判断出度为0的节点数为1，入度为0的节点数为1，则符合模块要求
            for (java.util.Map.Entry<String ,int[]> entry:map.entrySet()){
                int[] mark=entry.getValue();
                if (mark[0]==0){
                    if (in0.equals(""))
                        in0=entry.getKey();
                    else
                        return false;
                }

                if (mark[1]==0){
                    if (out0.equals(""))
                        out0=entry.getKey();
                    else
                        return false;
                }

            }
            group.SetValByKey(NodeGroupAttr.inNodeNo,in0);
            group.SetValByKey(NodeGroupAttr.outNodeNo,out0);
            group.Update();
            return true;

    }

}
