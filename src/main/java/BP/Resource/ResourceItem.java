package BP.Resource;

import BP.En.EntityNoName;
import BP.En.Map;
import BP.springCloud.tool.FeignTool;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-09-21 11:01
 **/
public class ResourceItem extends EntityNoName {
    public ResourceItem(){

    }

    public ResourceItem(String _no) throws Exception{
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

        Map map = new Map("k_resource_item", "资源实例");
        map.AddTBStringPK(ResourceItemAttr.No, null, "编号", true, true,1, 40, 100);
        map.AddTBString(ResourceItemAttr.Code, null, "编号", false, false, 1, 40, 100);
        map.AddTBString(ResourceItemAttr.KindName, null, "资源组名称", true, false, 0, 100, 100);
        map.AddTBString(ResourceItemAttr.Kind, null, "资源组编码", true, false, 0, 100, 100);
        map.AddTBString(ResourceItemAttr.DeptId, null, "所属部门", true, false, 0, 100, 100);
        map.AddTBStringDoc(ResourceItemAttr.Abstracts, null, "性能参数", true, false);

        map.AddSearchAttr(ResourceItemAttr.KindName);
        this.set_enMap(map);
        return this.get_enMap();
    }

    @Override
    protected boolean beforeInsert() throws Exception {
        Long id= FeignTool.getSerialNumber("BP.Resource.ResourceItem");
        this.SetValByKey(ResourceAttr.No,id);

        //更改资源类别下数量
        String kindNo=this.GetValStrByKey(ResourceItemAttr.Kind);
        ResourceItems resourceItems=new ResourceItems();
        resourceItems.Retrieve(ResourceItemAttr.Kind,kindNo);
        Resource resource=new Resource(kindNo);
        resource.SetValByKey(ResourceAttr.Num,resourceItems.size()+1);
        resource.Update();

        return super.beforeInsert();
    }

    @Override
    protected boolean beforeDelete() throws Exception {

        //更改资源类别下数量
        String kindNo=this.GetValStrByKey(ResourceItemAttr.Kind);
        ResourceItems resourceItems=new ResourceItems();
        resourceItems.Retrieve(ResourceItemAttr.Kind,kindNo);
        Resource resource=new Resource(kindNo);
        resource.SetValByKey(ResourceAttr.Num,resourceItems.size()-1);
        resource.Update();

        return super.beforeInsert();
    }



}
