package BP.Task;

import BP.WF.Flow;
import BP.springCloud.entity.GenerFlow;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 流程实例服务实现
 * @author pcc
 */
@Component("GenerFlowService")
public class GenerFlowService {
	/**
	 * Logger for this class
	 */
	 private static final Logger log = Logger.getLogger(GenerFlowService.class);
	/**
	 * the KGenerFlowManager
	 */
	@Resource
	private GenerFlowManager generFlowManager;
	/* 
	 * (non-Javadoc)
	 * @see WF.Task.service.KGenerFlowService\#add(WF.Task.domain.KGenerFlow)
	 */
	public Long insertGenerFlow(GenerFlow kGenerFlow) {
		return generFlowManager.insertGenerFlow(kGenerFlow);
	}
	/* 
	 * (non-Javadoc)
	 * @see WF.Task.service.KGenerFlowService\#update(WF.Task.domain.KGenerFlow)
	 */
	public Long updateGenerFlow(GenerFlow kGenerFlow) {
		return generFlowManager.updateGenerFlow(kGenerFlow);
	}
	/* 
	 * (non-Javadoc)
	 * @seeWF.Task.service.KGenerFlowService\#delete(java.lang.Long)
	 */
	public Long deleteGenerFlow(Long id) {
		return generFlowManager.deleteGenerFlow(id);
	}
	/* 
	 * (non-Javadoc)
	 * @see WF.Task.service.KGenerFlowService\#getKGenerFlow(Long id)
	 */
	public GenerFlow getGenerFlow(Long id) {
		return generFlowManager.getGenerFlowById(id);
	}
	/* 
	 * (non-Javadoc)
	 * @see WF.Task.service.KGenerFlowService\#getKGenerFlowListPage(${entityMeta.projectMeta.groupId}.domain.KGenerFlow, ${entityMeta.projectMeta.groupId}.common.Page)
	 */
	public List findGenerFlowList(GenerFlow generFlowCondition) {
		return generFlowManager.findGenerFlowList(generFlowCondition);
	}

	public List findGenerFlowAllList(GenerFlow generFlowCondition) {
		return generFlowManager.findGenerFlowListAll(generFlowCondition);
	}
	

	//组装成流程统计图数据
	public JSONObject getShowData(List<GenerFlow> list) throws Exception{
		list.sort(new Comparator<GenerFlow>() {
			@Override
			public int compare(GenerFlow o1, GenerFlow o2) {
				if (o1.getWorkId()-o2.getWorkId()>0)
					return 1;
				else if (o1.getWorkId()-o2.getWorkId()<0)
					return -1;
				return 0;
			}
		});
		JSONObject data=new JSONObject();
		int size=list.size();
		List<String> xAxis=new ArrayList<>(size);
		List<Integer> barDataUse=new ArrayList<>(size);
		List<Integer> barDataAll=new ArrayList<>(size);
		List<Float> lineData=new ArrayList<>(size);
		for (GenerFlow gener:list){
			Flow flow=new Flow(gener.getFlowId()+"");
			xAxis.add(gener.getWorkId()+"_"+flow.getName());
			barDataAll.add(gener.getTotalTime());
			if (gener.getStatus()==2) {//已经完成
				lineData.add(100f);
				barDataUse.add(gener.getTotalTime());
			}else {
				lineData.add(((gener.getUseTime() + 0.0f) *100)/ gener.getTotalTime());
				barDataUse.add(gener.getUseTime());
			}
		}

		data.put("xAxis",xAxis);
		data.put("barDataUse",barDataUse);
		data.put("barDataAll",barDataAll);
		data.put("lineData",lineData);
		return data;
	}

	public JSONObject getGantPointData(GenerFlow parent,GenerFlow generFlow){
		return null;
	}

	public JSONObject getGantPointData(GenerFlow parent,NodeTask nodeTask){
		return null;
	}
}
