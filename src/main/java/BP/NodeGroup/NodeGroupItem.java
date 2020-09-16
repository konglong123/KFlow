package BP.NodeGroup;

import BP.En.EntityNo;
import BP.En.Map;
import BP.springCloud.tool.FeignTool;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-09-14 11:19
 **/
public class NodeGroupItem extends EntityNo {
    public NodeGroupItem(){

    }

    public NodeGroupItem(String _no) throws Exception{
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

        Map map = new Map("k_node_group_item", "分组元素");
        map.AddTBStringPK(NodeGroupItemAttr.No, null, "编号", true, true,1, 40, 100);
        map.AddTBString(NodeGroupItemAttr.group_no, null, "分组编码", true, false, 1, 40, 100);
        map.AddTBString(NodeGroupItemAttr.node_no, null, "节点编码", true, false, 0, 100, 100);
        map.AddTBString(NodeGroupItemAttr.node_name, null, "节点名", true, false, 0, 100, 100);
        this.set_enMap(map);
        return this.get_enMap();
    }

    @Override
    protected boolean beforeInsert() throws Exception {
        Long id= FeignTool.getSerialNumber("BP.NodeGroup.NodeGroupItem");
        this.setNo(id+"");

        //更新分组信息
        String groupNo=this.GetValStrByKey(NodeGroupItemAttr.group_no);
        NodeGroup group=new NodeGroup(groupNo);
        NodeGroupItems items=new NodeGroupItems();
        items.Retrieve(NodeGroupItemAttr.group_no,groupNo);
        group.SetValByKey(NodeGroupAttr.nodeNum,items.size()+1);
        group.Update();

        return super.beforeInsert();
    }

    @Override
    protected boolean beforeDelete() throws Exception {

        //更新分组信息
        String groupNO=this.GetValStrByKey(NodeGroupItemAttr.group_no);
        NodeGroup group=new NodeGroup(groupNO);
        NodeGroupItems items=new NodeGroupItems();
        items.Retrieve(NodeGroupItemAttr.group_no,groupNO);
        group.SetValByKey(NodeGroupAttr.nodeNum,items.size()-1);
        group.Update();

        return super.beforeDelete();
    }
}
