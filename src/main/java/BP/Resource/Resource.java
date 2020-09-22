package BP.Resource;

import BP.En.EntityNoName;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.springCloud.tool.FeignTool;

import java.util.HashMap;

/**
 * @program: kflow-web
 * @description: 资源
 * @author: Mr.Kong
 * @create: 2020-02-22 10:58
 **/
public class Resource extends EntityNoName {

    public Resource(){

    }

    public Resource(String _no) throws Exception{
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

        Map map = new Map("k_resource", "资源");
        map.AddTBStringPK(ResourceAttr.No, null, "编号", true, false,1, 40, 100);
        map.AddTBString(ResourceAttr.Code, null, "编号", false, false, 1, 40, 100);
        map.AddTBString(ResourceAttr.Name, null, "名称", true, false, 0, 100, 100);
        map.AddDDLSysEnum(ResourceAttr.Kind, 0, "类型", true, true, ResourceAttr.Kind,
                "@0=人力@1=设备@2=环境@3=知识");
        map.AddTBString(ResourceAttr.DeptId, null, "所属部门", true, false, 0, 100, 100);
        map.AddTBStringDoc(ResourceAttr.Abstracts, null, "性能参数", true, false);
        map.AddTBInt(ResourceAttr.Num, 1, "数量", true, false);
        map.AddSearchAttr(ResourceAttr.Kind);

        RefMethod rm = null;
        rm = new RefMethod();
        rm.Title = "所属资源";
        rm.ClassMethodName = this.toString() + ".getResourceItem";
        rm.Icon = "../../WF/Img/Event.png";
        rm.refMethodType= RefMethodType.LinkModel;
        map.AddRefMethod(rm);


        this.set_enMap(map);
        return this.get_enMap();
    }

    @Override
    protected boolean beforeInsert() throws Exception {
        Long id=FeignTool.getSerialNumber("BP.Resource.Resource");
        this.SetValByKey(ResourceAttr.Code,id);
        return super.beforeInsert();
    }

    @Override
    protected boolean beforeUpdate() throws Exception {
        java.util.Map<String, Object> postBody = new HashMap<>();
        String id=this.GetValStrByKey(ResourceAttr.Code);
        postBody.put("id",id);
        postBody.put("mysqlId",id);
        postBody.put("name",this.GetValStrByKey(ResourceAttr.Name));
        postBody.put("abstracts",this.GetValStrByKey(ResourceAttr.Abstracts));
        postBody.put("no",this.GetValStrByKey(ResourceAttr.No));
        postBody.put("kind",this.GetValStrByKey(ResourceAttr.Kind));
        postBody.put("deptId",this.GetValStrByKey(ResourceAttr.DeptId));
        String url="http://112.125.90.132:8082/es/addResource";
        FeignTool.updateToES(url,postBody);
        return super.beforeUpdate();
    }

    @Override
    protected void afterDelete() throws Exception {
        //删除es中数据
        java.util.Map<String, Object> postBody = new HashMap<>();
        String id=this.GetValStrByKey(ResourceAttr.Code);
        postBody.put("id",id);
        String url="http://112.125.90.132:8082/es/deleteEsResourceById";
        FeignTool.updateToES(url,postBody);

        super.afterDelete();
    }

    public final String getResourceItem(){
        return "/WF/WF/Comm/SearchBS.htm?EnsName=BP.Resource.ResourceItems&Key=" + this.getNo();
    }

}
