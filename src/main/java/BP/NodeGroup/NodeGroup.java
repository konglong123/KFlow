package BP.NodeGroup;

import BP.En.EntityNo;
import BP.En.Map;
import BP.springCloud.tool.FeignTool;

import java.text.SimpleDateFormat;
import java.util.Date;

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

        map.AddDDLSysEnum(NodeGroupAttr.type, 2, "类型", true, true, NodeGroupAttr.type,
                "@1=可调换分组@2=模块分组");
        map.AddTBString(NodeGroupAttr.detail, null, "详情", true, false, 0, 100, 100);
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
}
