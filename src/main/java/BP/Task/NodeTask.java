package BP.Task;

import BP.En.EntitiesNo;
import BP.En.EntityNo;
import BP.En.Map;
import BP.WF.Node;

/**
 * @program: kflow-web
 * @description: 节点任务
 * @author: Mr.Kong
 * @create: 2020-03-04 17:33
 **/
public class NodeTask extends EntityNo {
    @Override
    public Map getEnMap() {
        if (this.get_enMap() != null)
        {
            return this.get_enMap();
        }

        Map map = new Map("k_node_task", "节点任务");
        map.AddTBString(NodeTaskAttr.No, null, "ID", false, true, 1, 20, 100);
        map.AddTBString(NodeTaskAttr.FlowTaskId, null, "流程任务编码", true, true, 0, 50, 50);
        map.AddTBString(NodeTaskAttr.FlowId, null, "流程编码", true, true, 1, 50, 50);
        map.AddTBString(NodeTaskAttr.NodeId, null, "节点编码", true, true, 0, 50, 50);
        map.AddDDLSysEnum(NodeTaskAttr.IsReady, 0, "任务状态", true, false, NodeTaskAttr.IsReady,
                "@0=未准备@1=可以开始@2=已经开始@3=已经完成@4=逾期@5=警告");
        map.AddDDLSysEnum(NodeTaskAttr.Status, 0, "提示信息", true, false, NodeTaskAttr.Status,
                "@0=未准备@1=可以开始@2=已经开始@3=已经完成@4=逾期开始@5=警告开始@6=正常@7=逾期结束@8=警告结束");
        map.AddTBString(NodeTaskAttr.PreNodeTask, null, "前置任务", true, false, 0, 50, 50);
        map.AddTBString(NodeTaskAttr.NextNodeTask, null, "后置任务", true, false, 0, 50, 50);
        map.AddTBString(NodeTaskAttr.ParentNodeTask, null, "父任务", true, false, 0, 50, 50);
        map.AddTBInt(NodeTaskAttr.TotalTime, 0, "预估总时间", true, true);
        map.AddTBInt(NodeTaskAttr.UseTime, 0, "已用时间", true, false);
        map.AddTBDateTime(NodeTaskAttr.PlanStartTime,  "预计开始时间", true, false);
        map.AddTBDateTime(NodeTaskAttr.PlanEndTime, "预计结束时间", true, false);
        map.AddTBDateTime(NodeTaskAttr.StartTime, "实际开始时间", true, false);
        map.AddTBDateTime(NodeTaskAttr.EndTime,  "实际结束时间", true, false);
        map.AddTBString(NodeTaskAttr.Executor, null, "执行人", true, false, 0, 100, 100);
        this.set_enMap(map);
        return this.get_enMap();
    }

    public NodeTask(String _no) throws Exception{
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
    public NodeTask(){

    }
}
