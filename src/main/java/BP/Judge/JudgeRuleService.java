package BP.Judge;

import BP.springCloud.entity.JudgeRuleM;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 决策规则服务实现
 * @author pcc
 */
@Component("judgeRuleService")
public class JudgeRuleService {
	/**
	 * Logger for this class
	 */
	 private static final Logger log = Logger.getLogger(JudgeRuleService.class);
	/**
	 * the JudgeRuleManager
	 */
	@Resource
	private JudgeRuleManager judgeRuleManager;
	/* 
	 * (non-Javadoc)
	 * @see WF.Nlp.service.JudgeRuleService\#add(WF.Nlp.domain.JudgeRule)
	 */

	public int insertJudgeRule(JudgeRuleM judgeRule) {
		return judgeRuleManager.insertJudgeRule(judgeRule);
	}
	/* 
	 * (non-Javadoc)
	 * @see WF.Nlp.service.JudgeRuleService\#update(WF.Nlp.domain.JudgeRule)
	 */

	public Long updateJudgeRule(JudgeRuleM judgeRule) {
		return judgeRuleManager.updateJudgeRule(judgeRule);
	}
	/* 
	 * (non-Javadoc)
	 * @seeWF.Nlp.service.JudgeRuleService\#delete(java.lang.Long)
	 */
	public Long deleteJudgeRule(int id) {
		return judgeRuleManager.deleteJudgeRule(id);
	}
	/* 
	 * (non-Javadoc)
	 * @see WF.Nlp.service.JudgeRuleService\#getJudgeRule(Long id)
	 */
	public JudgeRuleM getJudgeRule(int id) {
		return judgeRuleManager.getJudgeRule(id);
	}
	/* 
	 * (non-Javadoc)
	 * @see WF.Nlp.service.JudgeRuleService\#getJudgeRuleListPage(${entityMeta.projectMeta.groupId}.domain.JudgeRule, ${entityMeta.projectMeta.groupId}.common.Page)
	 */
	public List<JudgeRuleM> findJudgeRuleList(JudgeRuleM judgeRuleCondition) {
		return judgeRuleManager.findJudgeRuleList(judgeRuleCondition);
	}
}
