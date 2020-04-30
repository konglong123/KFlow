package BP.Judge;

import BP.springCloud.dao.JudgeRouteDao;
import BP.springCloud.entity.JudgeRoute;
import BP.springCloud.tool.FeignTool;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 决策路由管理器实现
 * @author pcc
 */
@Component
public class JudgeRouteManager{

	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(JudgeRouteManager.class);
	/**
	 * the JudgeRouteDao
	 */
	@Resource
	private JudgeRouteDao judgeRouteDao;
	
	
	
	/*===============================================================================*/
	/*                                以下是增删改查方法
	/*===============================================================================*/
	/**
	 * 获取决策路由
	 * @param No
	 * @return the JudgeRoute
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public JudgeRoute getJudgeRoute(Long No) {
		JudgeRoute judgeRoute = judgeRouteDao.getJudgeRoute(No);
		return judgeRoute;
	}
	/**
	 * 插入决策路由
	 * @param judgeRoute
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Long insertJudgeRoute(JudgeRoute judgeRoute) {
	     judgeRoute.setIsDelete(0);
		long id=FeignTool.getSerialNumber("BP.Judge.JudgeRoute");
		judgeRoute.setNo(id);
		judgeRouteDao.insertJudgeRoute(judgeRoute);
		return  id;
	}
	/**
	 * 更新决策路由
	 * @param judgeRoute
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Long updateJudgeRoute(JudgeRoute judgeRoute) {
		return judgeRouteDao.updateJudgeRoute(judgeRoute);
	}
	/**
	 * 删除决策路由
	 * @param No
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Long deleteJudgeRoute(Long No) {
		return judgeRouteDao.deleteJudgeRoute(No);
	}
	public List<JudgeRoute> findJudgeRouteList(JudgeRoute judgeRouteCondition) {
		List<JudgeRoute> list=judgeRouteDao.findJudgeRouteList(judgeRouteCondition);
		if (list==null)
			list=new ArrayList<JudgeRoute>();
		return list;
	}

	/*===============================================================================*/
	/*                                以下是get/set方法
	/*===============================================================================*/
	/**
	 * @return the judgeRouteDao
	 */
	public JudgeRouteDao getJudgeRouteDao() {
		return this.judgeRouteDao;
	}
	
	/**
	 * @param judgeRouteDao the judgeRouteDao to set
	 */
	public void setJudgeRouteDao(JudgeRouteDao judgeRouteDao) {
		this.judgeRouteDao = judgeRouteDao;
	}
	
}
