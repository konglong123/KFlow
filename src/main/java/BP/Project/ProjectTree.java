package BP.Project;

import BP.En.EntityNo;
import BP.En.Map;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.Sys.EnCfg;
import BP.Task.NodeTaskAttr;
import BP.Tools.BeanTool;
import BP.WF.Flow;
import BP.springCloud.controller.FlowController;
import BP.springCloud.tool.FeignTool;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.beans.beancontext.BeanContext;
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
		map.AddTBStringPK(ProjectTreeAttr.No, null, "ID", true, true, 0, 50, 50);
		map.AddTBString(ProjectTreeAttr.ProjectName, null, "项目名", true, false, 0, 50, 50);
		map.AddTBString(ProjectTreeAttr.ProjectNo, null, "项目编码", true, false, 0, 50, 50);
		map.AddTBString(ProjectTreeAttr.FlowNo, null, "流程编码", true, false, 0, 50, 50);
		map.AddTBString(ProjectTreeAttr.FlowName, null, "流程名", true, false, 0, 50, 50);
		map.AddTBString(ProjectTreeAttr.Detail, null, "项目树详情", true, false, 0, 50, 50);
		map.AddDDLSysEnum(ProjectTreeAttr.Status, 0, "项目状态", true, false, ProjectTreeAttr.Status, "@0=新建@1=流程运行中@2=流程终止@3=项目结束");
		map.AddTBDateTime(ProjectTreeAttr.EarlyStart, "2000-01-01 00:00:00", "最早开始时间", true, false);
		map.AddTBDateTime(ProjectTreeAttr.LateFinish,  "2000-01-01 00:00:00","最晚完成时间", true, false);
		map.AddTBDateTime(ProjectTreeAttr.PlanStart, "2000-01-01 00:00:00", "计划开始时间", true, false);
		map.AddTBDateTime(ProjectTreeAttr.PlanFinish,  "2000-01-01 00:00:00","计划完成时间", true, false);
		map.AddTBDateTime(ProjectTreeAttr.ActualStart, "2000-01-01 00:00:00", "实际开始时间", true, false);
		map.AddTBDateTime(ProjectTreeAttr.ActualFinish,  "2000-01-01 00:00:00","实际完成时间", true, false);
		map.AddTBInt(ProjectTreeAttr.ActualDuring, 0, "持续时间", true, false);
		map.AddTBInt(ProjectTreeAttr.PlanDuring, 0, "预计工期", true, false);
		map.AddTBString(ProjectTreeAttr.Manage,  "admin","负责人", true, false,0,50,50);
		map.AddTBString(ProjectTreeAttr.Manage,  "admin","负责人", true, false,0,50,50);


		RefMethod rm = new RefMethod();
		rm.Title = "流程树";
		rm.ClassMethodName = this.toString() + ".gotoProjectTree";
		rm.Icon = "../../WF/Img/Event.png";
		rm.refMethodType= RefMethodType.RightFrameOpen;
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "启动流程";
		rm.ClassMethodName = this.toString() + ".startFlow";
		rm.Icon = "../../WF/Img/Event.png";
		rm.refMethodType= RefMethodType.Func;
		rm.Warning = "您确定要启动项目流程吗？";
		map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "终止项目";
		rm.ClassMethodName = this.toString() + ".endProject";
		rm.Icon = "../../WF/Img/Event.png";
		rm.refMethodType= RefMethodType.Func;
		rm.Warning = "您确定要终止项目流程吗？ \t\n ";
		map.AddRefMethod(rm);

		map.AddSearchAttr(ProjectTreeAttr.ProjectName);
		this.set_enMap(map);
		return this.get_enMap();
	}
	public final String gotoProjectTree()
	{
		return BP.WF.Glo.getCCFlowAppPath() + "/WF/Project/ProjectDetail.html?projectTreeNo="+this.getNo();
	}

	public final String startFlow()
	{
		try{
			FlowController flowController= BeanTool.getBean(FlowController.class);
			flowController.startProject(this.getNo());
			return "启动成功！";
		}catch (Exception e){
			return "启动失败！"+e.getMessage();
		}

	}

	public final String endProject(){
		return "未开发！";
	}

	@Override
	protected boolean beforeInsert() throws Exception {
		//设置基本信息
		long id= FeignTool.getSerialNumber("BP.WF.Work");
		this.SetValByKey(ProjectTreeAttr.No,id);
		this.SetValByKey(ProjectTreeAttr.ProjectNo,id);
		String flowNo=this.GetValStrByKey(ProjectTreeAttr.FlowNo);
		if (!StringUtils.isEmpty(flowNo)){
			Flow flow=new Flow(flowNo);
			this.SetValByKey(ProjectTreeAttr.FlowName,flow.getName());
		}

		/*//更新系统FlowInfo
		EnCfg enCfg=new EnCfg("System.FlowInfo");
		java.util.Map<String,String> map=enCfg.getMap();
		int projectNum=Integer.valueOf(map.get("projectNum"))+1;
		map.put("projectNum",projectNum+"");
		enCfg.setMap(map);
		enCfg.Update();*/

		return super.beforeInsert();
	}


	@Override
	protected void afterDelete() throws Exception {

		/*//更新系统FlowInfo
		EnCfg enCfg=new EnCfg("System.FlowInfo");
		java.util.Map<String,String> map=enCfg.getMap();
		int projectNum=Integer.valueOf(map.get("projectNum"))-1;
		map.put("projectNum",projectNum+"");
		enCfg.setMap(map);
		enCfg.Update();*/

		super.afterDelete();
	}
}