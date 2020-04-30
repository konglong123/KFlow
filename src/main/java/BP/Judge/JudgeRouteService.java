package BP.Judge;

import BP.springCloud.entity.JudgeRoute;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 决策路由服务实现
 * @author pcc
 */
@Component("judgeRouteService")
public class JudgeRouteService {
	/**
	 * Logger for this class
	 */
	 private static final Logger log = Logger.getLogger(JudgeRouteService.class);
	/**
	 * the JudgeRouteManager
	 */
	@Resource
	private JudgeRouteManager judgeRouteManager;
	/* 
	 * (non-Javadoc)
	 * @see WF.Nlp.service.JudgeRouteService\#add(WF.Nlp.domain.JudgeRoute)
	 */
	public Long insertJudgeRoute(JudgeRoute judgeRoute) {
		return judgeRouteManager.insertJudgeRoute(judgeRoute);
	}
	/* 
	 * (non-Javadoc)
	 * @see WF.Nlp.service.JudgeRouteService\#update(WF.Nlp.domain.JudgeRoute)
	 */
	public Long updateJudgeRoute(JudgeRoute judgeRoute) {
		return judgeRouteManager.updateJudgeRoute(judgeRoute);
	}
	/* 
	 * (non-Javadoc)
	 * @seeWF.Nlp.service.JudgeRouteService\#delete(java.lang.Long)
	 */
	public Long deleteJudgeRoute(Long id) {
		return judgeRouteManager.deleteJudgeRoute(id);
	}
	/* 
	 * (non-Javadoc)
	 * @see WF.Nlp.service.JudgeRouteService\#getJudgeRoute(Long id)
	 */
	public JudgeRoute getJudgeRoute(Long id) {
		return judgeRouteManager.getJudgeRoute(id);
	}
	/* 
	 * (non-Javadoc)
	 * @see WF.Nlp.service.JudgeRouteService\#getJudgeRouteListPage(${entityMeta.projectMeta.groupId}.domain.JudgeRoute, ${entityMeta.projectMeta.groupId}.common.Page)
	 */
	public List<JudgeRoute> findJudgeRouteList( JudgeRoute judgeRouteCondition) {
		return judgeRouteManager.findJudgeRouteList(judgeRouteCondition);
	}
}
