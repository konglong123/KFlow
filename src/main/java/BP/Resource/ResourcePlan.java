package BP.Resource;

import BP.En.EntityNo;
import BP.En.Map;
import BP.Web.WebUser;
import BP.springCloud.tool.FeignTool;

import java.util.Date;

/**
 * @program: kflow-web
 * @description: 资源方案
 * @author: Mr.Kong
 * @create: 2020-06-13 08:12
 **/
public class ResourcePlan extends EntityNo {
    @Override
    public Map getEnMap() {
        if (this.get_enMap() != null)
        {
            return this.get_enMap();
        }

        Map map = new Map("k_resource_plan", "资源方案");
        map.AddTBStringPK(ResourcePlanAttr.No, null, "编号", true, true,1, 40, 100);
        map.AddTBString(ResourcePlanAttr.NodeId, null, "节点编码", true, true, 0, 100, 100);
        map.AddTBInt(ResourcePlanAttr.Priority, 1, "优先级", true, false);
        map.AddTBString(ResourcePlanAttr.Creator, null, "创建者", true, false,1, 40, 100);
        map.AddTBStringDoc(ResourcePlanAttr.Detail, null, "备注", true, false);

        map.AddSearchAttr(ResourcePlanAttr.NodeId);
        this.set_enMap(map);
        return this.get_enMap();
    }
    public ResourcePlan(String _no) throws Exception{
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
    public ResourcePlan(){}

    @Override
    protected boolean beforeInsert() throws Exception {
        Long id= FeignTool.getSerialNumber("BP.Resource.ResourcePlan");
        this.SetValByKey(ResourcePlanAttr.No,id);
        this.SetValByKey(ResourcePlanAttr.Creator, WebUser.getNo());
        return super.beforeInsert();
    }
}
