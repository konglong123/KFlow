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
        map.AddTBInt(ResourceTaskAttr.ResourceId, 0, "资源id", false, true);
        map.AddTBString(ResourceTaskAttr.ResourceNo, null, "资源编码", true, true, 0, 100, 100);
        map.AddTBInt(ResourceTaskAttr.NodeId, 0, "节点编码",true, true);
        map.AddTBInt(ResourceTaskAttr.WorkId, 0, "工作编码",true, true);
        map.AddTBDateTime(ResourceTaskAttr.StartTime,null,"开始时间",true,true);
        map.AddTBDateTime(ResourceTaskAttr.EndTime,null,"结束时间",true,true);
        map.AddTBDateTime(ResourceTaskAttr.BookStart,null,"预定开始时间",true,true);
        map.AddTBDateTime(ResourceTaskAttr.BookEnd,null,"预定结束时间",true,true);
        map.AddTBDateTime(ResourceTaskAttr.PlanStart,null,"计划开始时间",true,true);
        map.AddTBDateTime(ResourceTaskAttr.PlanEnd,null,"计划结束时间",true,true);
        map.AddTBInt(ResourceTaskAttr.IsPlan,0,"是否计划",true,true);
        map.AddTBInt(ResourceTaskAttr.IsFinish,0,"是否完成",true,true);
        //addTBData精确到日，addTBDataTime精确到时分
        this.set_enMap(map);
        return this.get_enMap();
    }
}