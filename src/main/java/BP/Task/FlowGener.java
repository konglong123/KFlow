package BP.Task;

import BP.En.*;
import BP.Resource.ResourceTask;
import BP.Resource.ResourceTaskAttr;
import BP.Resource.ResourceTasks;
import BP.Sys.EnCfg;
import BP.Web.WebUser;

import java.util.List;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-04-07 16:24
 **/
public class FlowGener extends EntityNo {
    public FlowGener(){

    }

    public FlowGener(String _no) throws Exception{
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

        Map map = new Map("k_gener_flow", "流程实例");
        map.AddTBStringPK(FlowGenerAttr.No, null, "ID", true, true, 1, 10, 3);
        map.AddTBString(FlowGenerAttr.WorkId, null, "流程实例编码", true, true, 0, 50, 50);
        map.AddTBString(FlowGenerAttr.ParentWorkId, null, "父工作", true, true, 0, 50, 50);
        map.AddTBString(FlowGenerAttr.WorkGroupId, null, "工作组", true, true, 0, 50, 50);
        map.AddDDLSysEnum(FlowGenerAttr.Status, 3, "状态", true, false, FlowGenerAttr.Status,
                "@1=运行中@2=已完成@3=准备");
        map.AddTBString(FlowGenerAttr.FlowId, null, "流程编码", true, true, 0, 50, 50);
        map.AddTBInt(FlowGenerAttr.TotalTime, 0, "预计总用时(h)", true, true);
        map.AddTBInt(FlowGenerAttr.UseTime, 0, "已经用时(h)", true, true);

        map.AddTBDateTime(FlowGenerAttr.CreateTime, null, "创建时间", true, true);
        map.AddTBDateTime(FlowGenerAttr.FinishTime, null,"结束时间", true, true);
        map.AddTBString(FlowGenerAttr.Creator, null, "创建者", true, true, 0, 100, 100);
        map.AddTBStringDoc(FlowGenerAttr.ActivatedNodes, null, "激活的节点", true, true);


        RefMethod rm = new RefMethod();
        rm.Title = "流程详情";
        rm.ClassMethodName = this.toString() + ".FlowDetail";
        rm.Icon = "../../WF/Img/Event.png";
        rm.refMethodType= RefMethodType.RightFrameOpen;
        map.AddRefMethod(rm);

        this.set_enMap(map);
        return this.get_enMap();
    }

    @Override
    public UAC getHisUAC() throws Exception {
        if (_HisUAC == null) {
            _HisUAC = new UAC();
            _HisUAC.IsUpdate = true;
            _HisUAC.IsView = true;
            if (WebUser.getNo().equals("admin")){
                _HisUAC.IsDelete=true;
            }
        }
        return _HisUAC;
    }

    public final String FlowDetail()
    {
        return BP.WF.Glo.getCCFlowAppPath() + "/WF/Comm/En.htm?EnName=BP.WF.Flow&PKVal="+this.GetValStrByKey(FlowGenerAttr.FlowId);
    }

    public final void addActiveNode(String nodeId){
        String activeNodes=this.GetValStrByKey(FlowGenerAttr.ActivatedNodes);
        activeNodes=activeNodes+nodeId+",";
        this.SetValByKey(FlowGenerAttr.ActivatedNodes,activeNodes);
    }

    public final void delActiveNode(String nodeId){
        String activeNodes=this.GetValStrByKey(FlowGenerAttr.ActivatedNodes);
        activeNodes=activeNodes.replace(nodeId+",","");
        this.SetValByKey(FlowGenerAttr.ActivatedNodes,activeNodes);
    }

    @Override
    protected boolean beforeInsert() throws Exception {
        this.SetValByKey(FlowGenerAttr.Status,3);
        return super.beforeInsert();
    }

    @Override
    protected boolean beforeDelete() throws Exception {

        //删除对应的节点任务
        NodeTasks tasks=new NodeTasks();
        tasks.Retrieve(NodeTaskAttr.WorkId,this.GetValStrByKey(FlowGenerAttr.WorkId));
        List<NodeTask> list=tasks.toList();
        tasks.Delete(NodeTaskAttr.WorkId,this.GetValStrByKey(FlowGenerAttr.WorkId));

        //重置相应的资源任务
        for (NodeTask temp:list){
            ResourceTasks resourceTasks=new ResourceTasks();
            resourceTasks.Retrieve(ResourceTaskAttr.TaskId,temp.getNo());
            List<ResourceTask> resourceTaskList=resourceTasks.toList();
            for (ResourceTask rTask:resourceTaskList){
                rTask.SetValByKey(ResourceTaskAttr.IsPlan,2);
                rTask.Update();
            }
        }

        return super.beforeDelete();
    }
}
