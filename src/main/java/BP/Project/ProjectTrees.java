package BP.Project;

import BP.En.EntitiesNo;
import BP.En.Entity;
import BP.Tools.Json;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-08-26 18:42
 **/
public class ProjectTrees extends EntitiesNo {
    @Override
    public Entity getGetNewEntity() {
        return new ProjectTree();
    }

    public JSONObject getPlanData(){
        JSONObject data=new JSONObject();
        List<ProjectTree> list=this.toList();
        List<JSONObject> projects=new ArrayList<>();
        for (ProjectTree project:list){
            JSONObject item=new JSONObject();
            item.put("projUid",project.getNo());
            item.put("projName",project.GetValStrByKey(ProjectTreeAttr.ProjectName));
            item.put("projEarlyStartDateTime",project.GetValDateTime(ProjectTreeAttr.EarlyStart).getTime());
            item.put("projLateFinishDateTime",project.GetValDateTime(ProjectTreeAttr.LateFinish).getTime());
            item.put("projPlanDur",project.GetValIntByKey(ProjectTreeAttr.PlanDuring));
            projects.add(item);
        }
        data.put("project",projects);
        return data;
    }
}
