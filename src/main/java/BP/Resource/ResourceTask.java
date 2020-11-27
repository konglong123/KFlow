package BP.Resource;

import BP.En.EntityNo;
import BP.En.Map;

/**
 * @program: kflow-web
 * @description: 资源任务
 * @author: Mr.Kong
 * @create: 2020-02-25 14:10
 **/
public class ResourceTask extends EntityNo {

    @Override
    public Map getEnMap() {
        if (this.get_enMap() != null)
        {
            return this.get_enMap();
        }

        Map map = new Map("k_resource_task", "资源任务");
        map.AddTBIntPK(ResourceTaskAttr.No,0,"id",false,true);
        map.AddTBString(ResourceTaskAttr.ResourceId, null, "资源实例id", true, false, 0, 100, 100);
        map.AddTBString(ResourceTaskAttr.ResourceNo, null, "资源编码", true, false, 0, 100, 100);
        map.AddTBString(ResourceTaskAttr.PlanId, null, "资源方案编码", true, true, 0, 100, 100);
        map.AddTBInt(ResourceTaskAttr.NodeId, 0, "节点编码",true, true);
        map.AddTBString(ResourceTaskAttr.TaskId, null, "节点任务编码",true, true, 0, 100, 100);
        map.AddTBInt(ResourceTaskAttr.UseTime, 0, "占用时间",true, true);
        map.AddTBInt(ResourceTaskAttr.UseNum, 0, "占用数量",true, true);
        map.AddTBDateTime(ResourceTaskAttr.StartTime,null,"开始时间",true,true);
        map.AddTBDateTime(ResourceTaskAttr.EndTime,null,"结束时间",true,true);
        map.AddTBDateTime(ResourceTaskAttr.BookStart,null,"预定开始时间",true,false);
        map.AddTBDateTime(ResourceTaskAttr.BookEnd,null,"预定结束时间",true,false);
        map.AddTBDateTime(ResourceTaskAttr.PlanStart,null,"计划开始时间",true,false);
        map.AddTBDateTime(ResourceTaskAttr.PlanEnd,null,"计划结束时间",true,false);
        map.AddDDLSysEnum(ResourceTaskAttr.IsPlan, 2, "是否计划", true, false,ResourceTaskAttr.IsPlan,
                "@1=已经计划@2=未计划");
        map.AddDDLSysEnum(ResourceTaskAttr.IsFinish, 2, "是否完成", true, false,ResourceTaskAttr.IsFinish,
                "@1=完成@2=未完成");
        //addTBData精确到日，addTBDataTime精确到时分
        this.set_enMap(map);
        return this.get_enMap();
    }
    public ResourceTask(String _no) throws Exception{
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
    public ResourceTask(){}

    @Override
    protected boolean beforeInsert() throws Exception {
        String resourceNo=this.GetValStrByKey(ResourceTaskAttr.ResourceNo);
        Resource resource=new Resource(resourceNo);
        int type=resource.GetValIntByKey(ResourceAttr.Kind);
        //环境或者知识资源需要设置数量，设备人员数量为0
        if (type==2||type==3){
            this.SetValByKey(ResourceTaskAttr.UseNum,1);
        }
        return super.beforeInsert();
    }
}
