package BP.Task;

import BP.Sys.EnCfg;
import BP.Web.WebUser;
import BP.springCloud.dao.GenerFlowDao;
import BP.springCloud.entity.GenerFlow;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 流程实例管理器实现
 * @author pcc
 */
@Component
public class GenerFlowManager  {

	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(GenerFlowManager.class);
	/**
	 * the KGenerFlowDao
	 */
	@Resource
	private GenerFlowDao generFlowDao;
	
	
	
	/*===============================================================================*/
	/*                                以下是增删改查方法
	/*===============================================================================*/
	/**
	 * 获取流程实例
	 * @param no
	 * @return the KGenerFlow
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public GenerFlow getGenerFlowById(Long no) {
		GenerFlow generFlow = generFlowDao.getGenerFlowById(no);
		return generFlow;
	}
	/**
	 * 插入流程实例
	 * @param
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Long insertGenerFlow(GenerFlow generFlow) {
	    Date date = new Date();
        generFlow.setYn(0);
        generFlow.setCreateTime(date);
        generFlow.setFinishTime(date);
        generFlowDao.insertGenerFlow(generFlow);


		Long id=generFlow.getNo();
		return  id;
	}
	/**
	 * 更新流程实例
	 * @param
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Long updateGenerFlow(GenerFlow generFlow) {
		return generFlowDao.updateGenerFlow(generFlow);
	}
	/**
	 * 删除流程实例
	 * @param no
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Long deleteGenerFlow(Long no) {

		return generFlowDao.deleteGenerFlow(no);
	}

	public List findGenerFlowList(GenerFlow generFlow) {
	    return generFlowDao.findGenerFlowList(generFlow);
	}

    public List findGenerFlowListAll(GenerFlow generFlow) {
	    generFlow.setYn(2);
        return generFlowDao.findGenerFlowList(generFlow);
    }

	/*===============================================================================*/
	/*                                以下是get/set方法
	/*===============================================================================*/
	/**
	 * @return the kGenerFlowDao
	 */
	public GenerFlowDao getGenerFlowDao() {
		return this.generFlowDao;
	}
	
	/**
	 * @param
	 */
	public void setGenerFlowDao(GenerFlowDao generFlowDao) {
		this.generFlowDao =generFlowDao;
	}
	
}
