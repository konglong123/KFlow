package BP.springCloud.dao;

import BP.springCloud.entity.JudgeRuleM;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 决策规则Dao
 * @author pcc
 * @Date 2020-04-29 14:37:44
 */
public interface JudgeRuleDao {

	/**
	 * 获取决策规则
	 * @param No
	 * @return the JudgeRuleM
	 */
	public JudgeRuleM getJudgeRuleM(int No);

	/**
	 * 插入决策规则
	 * @param judgeRuleM
	 */
	public Long insertJudgeRuleM(JudgeRuleM judgeRuleM);
	/**
	 * 更新决策规则
	 * @param judgeRuleM
	 */
	public Long updateJudgeRuleM(JudgeRuleM judgeRuleM);
	/**
	 * 删除决策规则
	 * @param No
	 */
	public Long deleteJudgeRuleM(int No);
	/**
	* @Description: 按条件获取决策规则列表
	* @param
	* @param
	* @return
	*/
	public List findJudgeRuleMList( JudgeRuleM judgeRule);
}
