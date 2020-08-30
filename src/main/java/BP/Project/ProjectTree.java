package BP.Project;

import BP.En.EntityNo;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.Task.NodeTaskAttr;
import BP.WF.Flow;
import BP.springCloud.tool.FeignTool;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * 项目
 * @author pcc
 */
public class ProjectTree extends EntityNo {
	public ProjectTree(){

	}

	public ProjectTree(String _no) throws Exception{
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
		//主键一定要采用AddTBStringPk,并且列为No（注意大小写）

		Map map = new Map("k_project_tree", "项目流程树");
		map.AddTBStringPK(ProjectTreeAttr.No, null, "ID", true, true, 1, 10, 3);
		map.AddTBString(ProjectTreeAttr.ProjectName, null, "项目树名", true, false, 0, 50, 50);
		map.AddTBString(ProjectTreeAttr.ProjectNo, null, "项目树编码", true, false, 0, 50, 50);
		map.AddTBString(ProjectTreeAttr.FlowNo, null, "流程编码", true, false, 0, 50, 50);
		map.AddTBString(ProjectTreeAttr.FlowName, null, "流程名", true, false, 0, 50, 50);
		map.AddTBString(ProjectTreeAttr.Detail, null, "项目树详情", true, false, 1, 50, 50);

		RefMethod rm = new RefMethod();
		rm.Title = "流程树";
		rm.ClassMethodName = this.toString() + ".gotoProjectTree";
		rm.Icon = "../../WF/Img/Event.png";
		rm.refMethodType= RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		map.AddSearchAttr(ProjectTreeAttr.ProjectName);
		this.set_enMap(map);
		return this.get_enMap();
	}
	public final String gotoProjectTree()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "/WF/Project/ProjectDetail.html?projectTreeNo="+this.getNo();
	}

	@Override
	protected boolean beforeInsert() throws Exception {
		long id= FeignTool.getSerialNumber("BP.Project.ProjectTree");
		this.SetValByKey(ProjectTreeAttr.No,id);
		this.SetValByKey(ProjectTreeAttr.ProjectNo,id);
		String flowNo=this.GetValStrByKey(ProjectTreeAttr.FlowNo);
		if (!StringUtils.isEmpty(flowNo)){
			Flow flow=new Flow(flowNo);
			this.SetValByKey(ProjectTreeAttr.FlowName,flow.getName());
		}
		return super.beforeInsert();
	}

	
}