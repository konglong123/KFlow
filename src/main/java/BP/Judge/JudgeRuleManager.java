package BP.Judge;

import BP.springCloud.dao.JudgeRuleDao;
import BP.springCloud.entity.JudgeRuleM;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 决策规则管理器实现
 * @author pcc
 */
@Component
public class JudgeRuleManager{

	/**
	 * Logger for this class
	 */
	private static final Logger log = Logger.getLogger(JudgeRuleManager.class);
	/**
	 * the JudgeRuleDao
	 */
	@Resource
	private JudgeRuleDao judgeRuleDao;
	
	
	
	/*===============================================================================*/
	/*                                以下是增删改查方法
	/*===============================================================================*/
	/**
	 * 获取决策规则
	 * @param No
	 * @return the JudgeRule
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public JudgeRuleM getJudgeRule(int No) {
		JudgeRuleM judgeRule = judgeRuleDao.getJudgeRuleM(No);
		return judgeRule;
	}
	/**
	 * 插入决策规则
	 * @param judgeRule
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int insertJudgeRule(JudgeRuleM judgeRule) {
	     Date date = new Date();
		judgeRuleDao.insertJudgeRuleM(judgeRule);
		int id=judgeRule.getNo();
		return  id;
	}
	/**
	 * 更新决策规则
	 * @param judgeRule
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Long updateJudgeRule(JudgeRuleM judgeRule) {
		return judgeRuleDao.updateJudgeRuleM(judgeRule);
	}
	/**
	 * 删除决策规则
	 * @param No
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Long deleteJudgeRule(int No) {
		return judgeRuleDao.deleteJudgeRuleM(No);
	}
	public List<JudgeRuleM> findJudgeRuleList(JudgeRuleM judgeRuleCondition) {
		return judgeRuleDao.findJudgeRuleMList( judgeRuleCondition);
	}

	/*===============================================================================*/
	/*                                以下是get/set方法
	/*===============================================================================*/
	/**
	 * @return the judgeRuleDao
	 */
	public JudgeRuleDao getJudgeRuleDao() {
		return this.judgeRuleDao;
	}
	
	/**
	 * @param judgeRuleDao the judgeRuleDao to set
	 */
	public void setJudgeRuleDao(JudgeRuleDao judgeRuleDao) {
		this.judgeRuleDao = judgeRuleDao;
	}
	
}
