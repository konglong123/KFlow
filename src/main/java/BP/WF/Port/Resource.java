package BP.WF.Port;

import BP.En.EntityNoName;
import BP.En.Map;

/**
 * @program: kflow-web
 * @description: 资源
 * @author: Mr.Kong
 * @create: 2020-02-22 10:58
 **/
public class Resource extends EntityNoName {
    @Override
    public Map getEnMap() {
        if (this.get_enMap() != null)
        {
            return this.get_enMap();
        }

        Map map = new Map("k_resource", "资源");
        map.AddTBString(ResourceAttr.Id, null, "ID", false, true, 1, 20, 100);
        map.AddTBString(ResourceAttr.No, null, "编号", true, false, 1, 20, 100);
        map.AddTBString(ResourceAttr.Name, null, "名称", true, false, 0, 100, 100);
        map.AddDDLSysEnum(ResourceAttr.Kind, 0, "类型", true, true, ResourceAttr.Kind,
                "@1=人力@2=设备@3=环境@4=知识");
        map.AddTBString(ResourceAttr.DeptId, null, "所属部门", true, false, 0, 100, 100);
        map.AddTBStringDoc(ResourceAttr.Abstracts, null, "资源详情", true, false);
        map.AddSearchAttr(ResourceAttr.Kind);
        this.set_enMap(map);
        return this.get_enMap();
    }
}
