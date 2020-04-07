package BP.Task;

import BP.springCloud.entity.GenerFlow;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
	
	/**
	*@Description: 查找子流程实例 
	*@Param:  
	*@return:  
	*@Author: Mr.kong
	*@Date: 2020/4/6 
	*/
	public List getChildGenerFlow(GenerFlow parent){
		GenerFlow con=new GenerFlow();
		con.setParentWorkId(parent.getWorkId());
		return generFlowManager.findGenerFlowListAll(con);
	}


	public JSONObject getGantData(Long generFlowNo,int depth){
		GenerFlow generFlow=generFlowManager.getGenerFlowById(generFlowNo);
		
		
		return null;
	}

	public JSONObject getGantPointData(GenerFlow parent,GenerFlow generFlow){
		return null;
	}

	public JSONObject getGantPointData(GenerFlow parent,NodeTask nodeTask){
		return null;
	}
}
